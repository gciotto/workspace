package gui;

import java.util.List;

import javax.swing.JFrame;

import main.Board;
import main.Command;

public class Frame extends JFrame {
/*
    public Frame() {	
        //<editor-fold defaultstate="collapsed" desc=" GTK+ Look and feel ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("GTK+".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Frame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
    }
*/

    public void createAndShowGUI() 
    {        
        initComponents();
        
        /* Disables all commands before connection is established */
        this.commandsPanel.disableAll();
        this.setVisible(true);
    }

    
    public void setBoardsList(List<Board> list)
    {
       
    	/* Updates boards in BoardsFrame */
        for (Board b : list) {
        	this.boardsFrame.addBoard(b);
        }
        
        /* Sets new window visibles */
        this.boardsFrame.setVisible(true);
    }
    
    public void setStatus(String message)
    {
        txtStatus.setText(message);
    }
    
    public void refresh()  {
    	
        boardsFrame.refresh();
    }
    
    
    public void setVersion(int[] version)
    {
        connectionPanel.setVersion(version);
    }
    

    // BUTTONS
    public void setCyclingEnabled()
    {
        commandsPanel.setCyclingEnabled();        
    }
    
    public void setCyclingDone()
    {
        commandsPanel.setCyclingDone();
    }
    
    public void setRampSending()
    {
        commandsPanel.setRampSending();
    }
    
    public void setRampSent()
    {
        commandsPanel.setRampSent();
    }
    
    public void setRampingEnabled()
    {
        commandsPanel.setRampingEnabled();
    }
    
    public void setRampingDone()
    {
        commandsPanel.setRampingDone();
    }
    
    
    private String cmdToTxt(int[] cmd)
    {
        String txt = "";
        
        txt += Command.findByCode(cmd[0]).name;
        
        txt += " [";
        for(int i : cmd)
            txt += String.format("%2X ", i);
        txt += "]";
        
        return txt;
    }
    
    public void setCmdSend(int[] cmd)
    {
        if(cmd[0] == Command.NORMAL.bytecode)
            return;
        txtLastSend.setText(cmdToTxt(cmd));        
    }
    
    public void setCmdRecv(int[] cmd)
    {
        if(cmd[0] == Command.NORMAL.bytecode)
            return;
        txtLastRecv.setText(cmdToTxt(cmd));        
    }
    
    public void setDisconnected() 
    {
        connectionPanel.setDisconnected();
        commandsPanel.disableAll();
        
        this.boardsFrame.clearBoards();
        this.boardsFrame.setVisible(false);
    }
    
    public void setConnected()
    {
        connectionPanel.setConnected();
        commandsPanel.setNormal();
    }
    
    public void clearBoards(){
    	this.boardsFrame.clearBoards();
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

    	this.setResizable(false);
    	
    	boardsFrame = new gui.BoardsFrame();
    	boardsFrame.setVisible(false);
    	
        connectionPanel = new gui.Connection();
        commandsPanel = new gui.Commands();
        txtStatus = new javax.swing.JLabel();
        txtLastRecv = new javax.swing.JLabel();
        txtLastSend = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cliente de Teste Prosac");

        connectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Conexão"));

        commandsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Comandos"));

        txtStatus.setText("status");
        txtStatus.setBorder(javax.swing.BorderFactory.createTitledBorder("Status"));

        //boardsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Placas"));

        txtLastRecv.setText("recv");
        txtLastRecv.setBorder(javax.swing.BorderFactory.createTitledBorder("Último comando recebido (exceto 00h)"));

        txtLastSend.setText("send");
        txtLastSend.setBorder(javax.swing.BorderFactory.createTitledBorder("Último comando enviado (exceto 00h)"));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtLastSend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(connectionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(commandsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txtLastRecv, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(connectionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(commandsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLastSend)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLastRecv)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gui.Commands commandsPanel;
    private gui.Connection connectionPanel;
    private javax.swing.JLabel txtLastRecv;
    private javax.swing.JLabel txtLastSend;
    private javax.swing.JLabel txtStatus;
    private gui.BoardsFrame boardsFrame;
    // End of variables declaration//GEN-END:variables
}
