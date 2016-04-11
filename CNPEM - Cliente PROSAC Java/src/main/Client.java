package main;

import gui.Frame;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client 
{
    // Defines
    private static final int CONNECT_TIMEOUT = 2000; //miliseconds
    public  static final int UPDATE_TIMEOUT  = 5;   // miliseconds
    
    private static final int CYCLE_TIMER = 20000;   // Iterations
    private static final int RAMP_TIMER =  15000;

    
    // Curves
    private static short[][] rampCurves;
    
    private static List<int[]> rampPackets = new ArrayList<int[]>();
    
    
    // GUI
    static Frame frame;
    
    
    // Socket related
    static Socket socket;
    
    private static BufferedInputStream socketIn;
    private static BufferedOutputStream socketOut;   
    
    
    // Buffers
    private static int[] sendBuffer;
    private static int[] recvBuffer;
    
    
    // Boards
    private static List<Board> boardsList = new ArrayList<Board>();
   
    
    
    // Mutex
    
    private static final  Object lock = new Object();
    
    
    private static boolean connected = false;
    
    /* Initialization */
    public static void main(String[] args) throws InterruptedException
    {      
        frame = new Frame();
        socket = new Socket();
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() {
                frame.createAndShowGUI();
                setStatus(false, "Cliente iniciado");
            }
        });
        
        generateRamps();       
        
        
        int cycle_count = 0;
        int ramp_count = 0;
        
        while(true)
        {
            /*if(connected)
            {
                if(++cycle_count == CYCLE_TIMER)
                {
                    executeCommand(Command.CYCLE_ENABLE);
                    cycle_count = 0;
                }
            }*/
            if(connected)
                executeCommand(Command.NORMAL);
            
            Thread.sleep(UPDATE_TIMEOUT);            
        }
    }
    
    /* Populate ramp curves */

    public static void generateRamps()
    {
        rampCurves = new short[2][1021];
        
        for(int i = 0; i < rampCurves[0].length; ++i)
        {
            rampCurves[0][i] = (short) ((i+1)*30);
            rampCurves[1][i] = (short) (3*i);            
        }
    }
    
    
    /* Buttons */
    public static void btCommand(int cmd)
    {
        executeCommand(Command.findByCode(cmd));
    }
    
    public static void btConnect(String ip, String port)
    {
        if(connected)
        {
            connected = false;
            try {
                socket.shutdownInput();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                socket.shutdownOutput();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            frame.setDisconnected();
            return;
        }
        
        
        System.out.printf("Connect %s:%s\n", ip, port);
        
        if(connectTo(ip, Integer.parseInt(port)) < 0) // Erro
            return;
        
        connected = true;
        frame.setConnected();
        
        
        executeCommand(Command.IDENT);
        executeCommand(Command.END_IDENT);
    }
    
    /* GUI management */
    private static void setStatus(boolean isErr, String message)
    {
        String status = "";
        
        if(isErr)
            status += "Erro: ";
        
        status += message;
            
        
        frame.setStatus(status);
    }
    
    
    /* Connection management */
    public static int connectTo(String hostname, int port)
    {
        setStatus(false, "Conectando a " + hostname + ":" + port + " ...");
        
        if(connected)
        {
            setStatus(true, "Já está conectado!");
            return -1;
        }
        
        try 
        {
            InetAddress addr = InetAddress.getByName(hostname);
            SocketAddress sockaddr = new InetSocketAddress(addr, port);

            socket = new Socket();

            socket.connect(sockaddr, CONNECT_TIMEOUT);
            
            socketOut = new BufferedOutputStream(socket.getOutputStream());
            socketIn = new BufferedInputStream(socket.getInputStream());
            
        } catch (UnknownHostException e) {
            setStatus(true, "Host desconhecido: " + hostname);
            return -1;
            
        } catch (SocketTimeoutException e) {
            setStatus(true, "Timeout na conexão");
            return -1;
            
        } catch (IOException e) {
            setStatus(true,"Não foi possível abrir I/O");
            return -1;
        }
        
        setStatus(false, "Conectado!");
        return 0;
    }

    
    /* Command management */
    
    public static void executeCommand(Command cmd)
    {
        if(socketOut == null || socketIn == null)
        {
            setStatus(true, "Socket buffers nulos");
            return;
        }
        synchronized(lock)
        {
            prepareCommand(cmd);    
            frame.setCmdSend(sendBuffer);
            sendCommand();
            recvCommand();        
            processCommand();
            
            
            frame.setCmdRecv(recvBuffer);
            
            frame.refresh();
        }
    }
    
   
    public static void prepareCommand(Command cmd)
    {
        if(cmd == null)
        {
            setStatus(true, "Comando nulo");
            return;
        }
        
        int i;
        int sendBufferSize = 0;
        switch (cmd)
        {
            case CYCLE_ENABLE:
                frame.setCyclingEnabled();
                
            
            case ADJUST:                     
                System.out.println("");
                sendBufferSize += 2; // COMMAND and PACKET SIZE
                
                for(Board b: boardsList)
                    sendBufferSize += b.getWriteBytes().length;
                
                sendBuffer = new int[sendBufferSize];
                                
                i = 2;
                for(Board b: boardsList)
                    for(int j : b.getWriteBytes())
                        sendBuffer[i++] = j;
                

                sendBuffer[1] = sendBuffer.length - 2;
                sendBuffer[0] = cmd.bytecode;
                break;
      
                
            case RAMP_INIT:
                System.out.println("RAMP_INIT");
                frame.setRampSending();
                
                rampPackets.clear();
                for(Board b : boardsList)
                {
                    if(!b.getWillRamp())
                    {
                        System.out.println("Will not ramp: "+b.getPosition());
                        continue;
                    }
                    
                    int j = 0;
                    
                    int rampLength = rampCurves[b.getRampCurve()].length;                    
                    
                    while(j < rampLength)
                    {
                        int packetSize = 2*rampLength + 7;// 1 cmd 2 tamanho 4 header
                        
                        int[] packet = new int[packetSize]; 
                        
                        
                        packet[0] = Command.RAMP_BLOCK.bytecode;  // Command
                        packet[1] = ((packetSize - 3) >> 8) & 0xFF; // Packet
                        packet[2] = (packetSize - 3) & 0xFF;
                        packet[3] = b.getPosition() - 1;              // Which board
                        packet[4] = b.getRampPulses();            // Pulses to wait
                        packet[5] = (rampLength >> 8) & 0xFF;
                        packet[6] = rampLength & 0xFF;
                        
                        // Data
                        for(int k = 7; k < packetSize; k+=2)
                        {
                            if(j < rampLength)
                            {
                                short value = rampCurves[b.getRampCurve()][j++];
                                packet[k] = (value >> 8) & 0xFF;
                                packet[k+1] = value & 0xFF;
                            }
                            else                                
                            {    
                                packet[k]   = 0;
                                packet[k+1] = 0;
                            }
                            
                        }
                        
                        rampPackets.add(packet);
                    }
                }
                
                if(rampPackets.isEmpty())
                {
                    frame.setRampSent();
                    sendBuffer = new int[1];
                    sendBuffer[0] = Command.NORMAL.bytecode;
                }
                else   
                {
                    sendBufferSize += 2; // COMMAND and PACKET SIZE
                
                    for(Board b: boardsList)
                        sendBufferSize += b.getWriteBytes().length;
                    
                    sendBuffer = new int[sendBufferSize];
                    
                    i = 2;
                    for(Board b: boardsList)
                        for(int j : b.getWriteBytes())
                            sendBuffer[i++] = j;
                    
                    
                    sendBuffer[0] = cmd.bytecode;
                    sendBuffer[1] = sendBufferSize - 2;
                    
                    setStatus(false, "Rampa inicializada! Falta enviar: "+ rampPackets.size()+ " pacotes.");
                }
                
                                
                
                break;
                
            case RAMP_BLOCK:
                sendBuffer = rampPackets.remove(0);
                setStatus(false, "Pacote enviado! Falta enviar: "+ rampPackets.size()+ " pacotes.");
                
                if(rampPackets.size() == 0)
                {
                    frame.setRampSent();
                    setStatus(false, "Rampa pronta!");
                }
                
                break;
                
                
            case RAMP_ENABLE:
            case RAMP_ENABLE_CYCLIC:
                frame.setRampingEnabled();

                
                
            default:
                sendBuffer = new int[1];
                sendBuffer[0] = cmd.bytecode;
                break;
                
            
        }
    }
    
    public static void processCommand()
    {
        Command c = Command.findByCode(recvBuffer[0]);
        
        switch(c)
        { 
            case IDENT:
                boardsList.clear();
                
                //for(int i = 2; i < recvBuffer[1] + 2; ++i)
                for(int i = 2; i < 32 + 2; ++i)
                {
                    Module m = Module.findByType(recvBuffer[i]);
                    
                    if(m != Module.NONE)
                    {
                        Board b = new Board(i-2, m);
                        boardsList.add(b);
                    }
                }
                
                if(recvBuffer[1] > 32)                
                {
                    int[] version = new int[6];
                    
                    for (int i = 0; i < 6; i++) {
                        version[i] = recvBuffer[34 + i];
                    }
                    
                    frame.setVersion(version);
                    
                }
                
                frame.setBoardsList(boardsList);
                break;
            
            case RAMP_ABORTED:
                System.out.println("Rampa abortada. Retorno:");
                printCommand(recvBuffer);
                frame.setRampingDone();
                break;    
                
                
            case CYCLE_COMPLETED:
            case CYCLE_ABORTED:
            case RAMP_COMPLETED:
                if(c == Command.CYCLE_ABORTED || c == Command.CYCLE_COMPLETED)
                    frame.setCyclingDone();
                else if(rampPackets.size() == 0)
                    frame.setRampingDone();
                
            default:
                int i = 2;
                
                for( Board b : boardsList)                         
                    for(int j = 0; j < b.getReadBytes().length; ++j)
                        b.getReadBytes()[j] = recvBuffer[i++];
                
                break;
        }
    }  
    
    
    public static void sendCommand()
    {
        byte[] sendBuf = new byte[sendBuffer.length];
        int    sentBytes;
        
            
        for(int i = 0; i < sendBuffer.length; ++i)  
            sendBuf[i] = (byte) (sendBuffer[i] & 0xFF);
        
        try { 
            socketOut.write(sendBuf);
            socketOut.flush();
            
            if(sendBuf[0] != 0)
                System.out.println("Comando ["+((int) sendBuf[0])+"] Enviados "+sendBuf.length+" bytes");
        } catch (IOException ex) {
            setStatus(true, "Erro de I/O enviando comando");
        }
        
        sendBuffer = null;
        
    }
    
    public static void recvCommand()
    {
        byte[] recvBuf = new byte[256];
        int receivedBytes;
        
        try {
            receivedBytes = socketIn.read(recvBuf);
        } catch (IOException ex) {
            setStatus(true, "Erro de I/O recebendo resposta");
            return;
        }
        
        if(receivedBytes <= 0)
        {
            disconnected();
            return;
        }   
            
        recvBuffer = new int[receivedBytes];
        
        for(int i = 0; i < receivedBytes; ++i)
            recvBuffer[i] = ((int) recvBuf[i]) & 0xFF;       
        
    }
    
   
    public static void printCommand(int[] buf)
    {
        if(buf == null)
            return;
        
        if(buf[0] == 0)
            return;
        
        System.out.printf("[");
        for(int i = 0; i < buf.length; ++i)
            System.out.printf("%2X ", buf[i]);
        System.out.printf("]\n");
    }

    
    public static void disconnected()
    {
        frame.setDisconnected();
        connected = false;
        setStatus(false, "Desconectado");
    }

}
