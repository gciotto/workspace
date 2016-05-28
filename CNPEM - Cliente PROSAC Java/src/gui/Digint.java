package gui;

import main.Board;

public class Digint extends javax.swing.JPanel {

    private Board board;
    
    public Digint() {
        initComponents();
    }
    
    public void setBoard(Board b)
    {
        this.board = b;
        
        if(b == null)
            this.setVisible(false);
        else
        {
            txtName.setText(b.getModule().name);
            txtPosition.setText(String.valueOf(b.getPosition()));
            this.setVisible(true);
        }
        
    }
    
    public void refresh()
    {
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

        writeBytes[0] = 0;        
        writeBytes[1] = 0x80; 
    }
    
    
    public String getPosition()
    {
        return txtPosition.getText();
    }
    
    public void setPosition(String p)
    {
        txtPosition.setText(p);
    }    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
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

        lblportCByte1.setText("Byte c1:");

        lblByteC1.setText("0");

        lblportCByte2.setText("Byte c2:");

        lblByteC2.setText("0");

        lblportCByte3.setText("Byte c3:");

        lblByteC3.setText("0");

        lblportCByte4.setText("Byte c4:");

        lblByteC4.setText("0");

        lblportCByte5.setText("Byte c5:");

        lblByteC5.setText("0");

        lblportCByte6.setText("Byte c6:");

        lblByteC6.setText("0");

        lblportCByte7.setText("Byte c7:");

        lblByteC7.setText("0");

        lblportCByte8.setText("Byte c8:");

        lblByteC8.setText("0");

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
        );
    }// </editor-fold>                        

    // Variables declaration - do not modify                     
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
    // End of variables declaration                           
}
