package gui;

import javax.swing.SpinnerListModel;
import main.Board;
import main.Client;

public class Statfnt extends javax.swing.JPanel {

    private Board board;
    
    public Statfnt() {
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
        
        int tPortA = readBytes[2], tPortB = readBytes[3], tPortC = readBytes[4];
        
        lblByteA.setText(Integer.toString(tPortA));
        lblByteB.setText(Integer.toString(tPortB));
        lblByteC.setText(Integer.toString(tPortC));        
                    
        writeBytes[0] = 0;        
        writeBytes[1] = 0x80; 
        
        if (chbReset.getState())
        	writeBytes[2] = 1 << 5;
        else writeBytes[2] = 0;
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
        lblportA = new javax.swing.JLabel();
        lblportB = new javax.swing.JLabel();
        lblportC = new javax.swing.JLabel();
        lblByteA = new javax.swing.JLabel();
        lblByteB = new javax.swing.JLabel();
        chbReset = new java.awt.Checkbox();
        lblByteC = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txtName.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        txtName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtName.setText("STATFNT");

        txtPosition.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        txtPosition.setText("99");
        txtPosition.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblportA.setText("Porta A [In]:");

        lblportB.setText("Porta B [In]:");

        lblportC.setText("Porta C [Out]:");

        lblByteA.setText("0");

        lblByteB.setText("0");

        chbReset.setLabel("Reset Font");

        lblByteC.setText("0");

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
                        .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblportA)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblByteA, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblportB)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblByteB, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblportC)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblByteC, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chbReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
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
                    .addComponent(lblportA)
                    .addComponent(lblByteA))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblportB)
                    .addComponent(lblByteB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblportC)
                        .addComponent(lblByteC))
                    .addComponent(chbReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
    }// </editor-fold>                        

    // Variables declaration - do not modify                     
    private java.awt.Checkbox chbReset;
    private javax.swing.JLabel lblByteA;
    private javax.swing.JLabel lblByteB;
    private javax.swing.JLabel lblByteC;
    private javax.swing.JLabel lblportA;
    private javax.swing.JLabel lblportB;
    private javax.swing.JLabel lblportC;
    private javax.swing.JLabel txtName;
    private javax.swing.JLabel txtPosition;
    // End of variables declaration                        
}
