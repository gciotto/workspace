package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Graphical representation of a DIGINT board. This class inherits from JPanel, therefore
 * it can be placed inside a window, such as a JFrame. In our application, it will be instantiated
 * by the BoardsFrame class.
 * 
 * According to the intracont reference manual, this board presents the following features:
 *  - Read Block (10 bytes): priority + flags + 8 input bytes
 *  - Write Block (2 bytes): priority + flags
 *  
 * Refer to {@link http://git.cnpem.br/bruno.martins/prosac/blob/master/modules/DIGIINT.c} 
 * for further details about how PROSAC handles them.
 * 
 * @see IBoard
 * @author Gustavo CIOTTO PINTON
 */

import main.Board;

public class Digint extends javax.swing.JPanel implements IBoard {

    private Board board;
    
    /**
     * Constructs a new panel. It calls initComponents function, which was generated by NetBeans
     * window editor.
     */
    public Digint() {
        initComponents();
    }
    
    /**
     * Registers this graphical interface with a Board object and updates its title and 
     * board address.
     * 
     * @param b Board object containing information about a detected board.
     */
    public void setBoard(Board b){
    	
        this.board = b;
        
        if(b == null)
            this.setVisible(false);
        else {
        	
            txtName.setText(b.getModule().name);
            txtPosition.setText(String.valueOf(b.getPosition()));
            this.setVisible(true);
        }
        
    }
    
    /**
     * Updates the interface fields according to the values in the Board object.
     * 
     * According to the intracont reference manual:
     *  - Read Block (10 bytes): priority + flags + 8 input bytes
     *  - Write Block (2 bytes): priority + flags 
     */
    public void refresh() {
    	
        if(board == null)
            return;
        
        int[] readBytes = board.getReadBytes();
        int[] writeBytes = board.getWriteBytes();
        
        int tPortC1 = readBytes[2], tPortC2 = readBytes[3], tPortC3 = readBytes[4], 
        	tPortC4 = readBytes[5], tPortC5 = readBytes[6], tPortC6 = readBytes[7],
        	tPortC7 = readBytes[8], tPortC8 = readBytes[9];
        
        lblByteC1.setText(Integer.toString(tPortC1));
        lblByteC2.setText(Integer.toString(tPortC2));
        lblByteC3.setText(Integer.toString(tPortC3));
        lblByteC4.setText(Integer.toString(tPortC4));
        lblByteC5.setText(Integer.toString(tPortC5));
        lblByteC6.setText(Integer.toString(tPortC6));
        lblByteC7.setText(Integer.toString(tPortC7));
        lblByteC8.setText(Integer.toString(tPortC8));

        /* DIGINT cannot cycle, thus flag must be 128 (0x80) */
        writeBytes[0] = 0;        
        writeBytes[1] = 0x80; 
        
    }
    
    /**
     * Gets the address of this board.
     * @return Address of this board
     */
    public String getPosition() {
        return txtPosition.getText();
    }
    
    /**
     * Sets the address of this board.
     * @param p New address of this board
     */
    public void setPosition(String p) {
        txtPosition.setText(p);
    }    

    /**
     * Code generated by NetBeans window editor.
     */
    @SuppressWarnings("unchecked")                          
    private void initComponents() {

        txtName = new javax.swing.JLabel();
        txtPosition = new javax.swing.JLabel();
        lblportCByte1 = new javax.swing.JLabel();
        lblByteC1 = new javax.swing.JLabel();
        lblportCByte2 = new javax.swing.JLabel();
        lblByteC2 = new javax.swing.JLabel();
        lblportCByte3 = new javax.swing.JLabel();
        lblByteC3 = new javax.swing.JLabel();
        lblportCByte4 = new javax.swing.JLabel();
        lblByteC4 = new javax.swing.JLabel();
        lblportCByte5 = new javax.swing.JLabel();
        lblByteC5 = new javax.swing.JLabel();
        lblportCByte6 = new javax.swing.JLabel();
        lblByteC6 = new javax.swing.JLabel();
        lblportCByte7 = new javax.swing.JLabel();
        lblByteC7 = new javax.swing.JLabel();
        lblportCByte8 = new javax.swing.JLabel();
        lblByteC8 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txtName.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        txtName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtName.setText("DIGINT");

        txtPosition.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        txtPosition.setText("99");
        txtPosition.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblportCByte1.setText("Byte 1:");
        lblByteC1.setText("0");

        lblportCByte2.setText("Byte 2:");
        lblByteC2.setText("0");

        lblportCByte3.setText("Byte 3:");
        lblByteC3.setText("0");

        lblportCByte4.setText("Byte 4:");
        lblByteC4.setText("0");

        lblportCByte5.setText("Byte 5:");
        lblByteC5.setText("0");

        lblportCByte6.setText("Byte 6:");
        lblByteC6.setText("0");

        lblportCByte7.setText("Byte 7:");
        lblByteC7.setText("0");

        lblportCByte8.setText("Byte 8:");
        lblByteC8.setText("0");

        setLayout(new BorderLayout());
        
        JPanel title = new JPanel();
        title.setLayout(new BorderLayout());
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        title.add(txtPosition, BorderLayout.EAST);
        title.add(txtName, BorderLayout.CENTER);
        
        add(title, BorderLayout.NORTH);
        
        JPanel data = new JPanel();
        data.setLayout(new GridLayout(5, 2, 10, 10));
        data.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        data.add(lblportCByte1);
        data.add(lblByteC1);
        
        data.add(lblportCByte2);
        data.add(lblByteC2);
        
        data.add(lblportCByte3);
        data.add(lblByteC3);
        
        data.add(lblportCByte4);
        data.add(lblByteC4);
        
        data.add(lblportCByte5);
        data.add(lblByteC5);
        
        data.add(lblportCByte6);
        data.add(lblByteC6);
        
        data.add(lblportCByte7);
        data.add(lblByteC7);
        
        data.add(lblportCByte8);
        data.add(lblByteC8);
        
        add(data);
        
        /*
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtPosition)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblportCByte1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblByteC1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblportCByte2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblByteC2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblportCByte3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblByteC3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblportCByte4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblByteC4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblportCByte5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblByteC5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblportCByte6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblByteC6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblportCByte7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblByteC7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblportCByte8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblByteC8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName)
                    .addComponent(txtPosition))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblportCByte1)
                    .addComponent(lblByteC1)
                    .addComponent(lblportCByte5)
                    .addComponent(lblByteC5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblportCByte2)
                    .addComponent(lblByteC2)
                    .addComponent(lblportCByte6)
                    .addComponent(lblByteC6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblportCByte3)
                    .addComponent(lblByteC3)
                    .addComponent(lblportCByte7)
                    .addComponent(lblByteC7))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblportCByte4)
                    .addComponent(lblByteC4)
                    .addComponent(lblportCByte8)
                    .addComponent(lblByteC8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );*/
    }// </editor-fold>                        

    /* Variables declaration - do not modify */                     
    private javax.swing.JLabel lblByteC1;
    private javax.swing.JLabel lblByteC2;
    private javax.swing.JLabel lblByteC3;
    private javax.swing.JLabel lblByteC4;
    private javax.swing.JLabel lblByteC5;
    private javax.swing.JLabel lblByteC6;
    private javax.swing.JLabel lblByteC7;
    private javax.swing.JLabel lblByteC8;
    private javax.swing.JLabel lblportCByte1;
    private javax.swing.JLabel lblportCByte2;
    private javax.swing.JLabel lblportCByte3;
    private javax.swing.JLabel lblportCByte4;
    private javax.swing.JLabel lblportCByte5;
    private javax.swing.JLabel lblportCByte6;
    private javax.swing.JLabel lblportCByte7;
    private javax.swing.JLabel lblportCByte8;
    private javax.swing.JLabel txtName;
    private javax.swing.JLabel txtPosition;                         
}
