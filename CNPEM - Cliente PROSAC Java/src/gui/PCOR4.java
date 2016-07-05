package gui;

import javax.swing.JLabel;
import javax.swing.SpinnerListModel;

import main.Board;
import main.Client;

public class PCOR4 extends javax.swing.JPanel {

    private Board board;
    
    private String[] spinnerValues = {"5 ms", "10 ms", "50 ms", "100 ms", "250 ms", "500 ms", "1000 ms"};
    
    public PCOR4() {
        initComponents();
        
        setSkips((String)spinner.getValue());
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
    
    public void setCanvasMax(int max){
    	for (int i = 0; i < 8; i++)
    		this.canvas.setMaxOfChannel(max, i);
    }
    
    public void setCanvasMin(int min){
    	for (int i = 0; i < 8; i++)
    		this.canvas.setMinOfChannel(min, i);
    }
    
    public void refresh()
    {
        if(board == null)
            return;
        
        int[] readBytes = board.getReadBytes();
        int[] writeBytes = board.getWriteBytes();
        
        float[] temperatures;
        temperatures = new float[8];
        
        for (int i = 0; i < 8; i++) {
        	int a = (readBytes[2+2*i] << 8) + readBytes[3+2*i];
        	temperatures[i] = a*100/4095;
            canvas.addMeasure(i, String.valueOf(a));
            lblTemps[i].setText(String.valueOf(temperatures[i]));
        }
        
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
    
    private void setSkips(String time)
    {
        int value = Integer.parseInt(time.split(" ")[0]);
        int skips = value/Client.UPDATE_TIMEOUT - 1;
        
        canvas.setSkip(skips);
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        txtName = new javax.swing.JLabel();
        spinner = new javax.swing.JSpinner();
        canvas = new gui.Canvas(8);
        txtPosition = new javax.swing.JLabel();
        lblAnalog = new javax.swing.JLabel();
        lblTemp1 = new javax.swing.JLabel();
        lblAnalog1 = new javax.swing.JLabel();
        lblTemp2 = new javax.swing.JLabel();
        lblAnalog2 = new javax.swing.JLabel();
        lblTemp3 = new javax.swing.JLabel();
        lblAnalog3 = new javax.swing.JLabel();
        lblTemp4 = new javax.swing.JLabel();
        lblAnalog4 = new javax.swing.JLabel();
        lblTemp5 = new javax.swing.JLabel();
        lblTemp8 = new javax.swing.JLabel();
        lblAnalog5 = new javax.swing.JLabel();
        lblTemp6 = new javax.swing.JLabel();
        lblAnalog6 = new javax.swing.JLabel();
        lblTemp7 = new javax.swing.JLabel();
        lblAnalog7 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txtName.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        txtName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtName.setText("PCOR4_20");

        spinner.setModel(new SpinnerListModel(spinnerValues));
        spinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinnerStateChanged(evt);
            }
        });

        javax.swing.GroupLayout canvasLayout = new javax.swing.GroupLayout(canvas);
        canvas.setLayout(canvasLayout);
        canvasLayout.setHorizontalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        canvasLayout.setVerticalGroup(
            canvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        txtPosition.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        txtPosition.setText("99");
        txtPosition.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblAnalog.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblAnalog.setText("Ch1:");

        lblTemp1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTemp1.setText("65535");

        lblAnalog1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblAnalog1.setText("Ch2:");

        lblTemp2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTemp2.setText("65535");

        lblAnalog2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblAnalog2.setText("Ch3:");

        lblTemp3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTemp3.setText("65535");

        lblAnalog3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblAnalog3.setText("Ch4:");

        lblTemp4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTemp4.setText("65535");

        lblAnalog4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblAnalog4.setText("Ch5:");

        lblTemp5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTemp5.setText("65535");

        lblTemp8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTemp8.setText("65535");

        lblAnalog5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblAnalog5.setText("Ch6:");

        lblTemp6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTemp6.setText("65535");

        lblAnalog6.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblAnalog6.setText("Ch7:");

        lblTemp7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTemp7.setText("65535");

        lblAnalog7.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblAnalog7.setText("Ch8:");

        lblTemps = new JLabel[8];
        
        lblTemps[0] = lblTemp1;
        lblTemps[1] = lblTemp2;
        lblTemps[2] = lblTemp3;
        lblTemps[3] = lblTemp4;
        lblTemps[4] = lblTemp5;
        lblTemps[5] = lblTemp6;
        lblTemps[6] = lblTemp7;
        lblTemps[7] = lblTemp8;
        
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
                        .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
                    .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spinner, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblAnalog)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTemp1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblAnalog1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTemp2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblAnalog2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTemp3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblAnalog3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTemp4, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblAnalog4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTemp5, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblAnalog5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTemp6, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblAnalog6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTemp7, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblAnalog7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTemp8, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtName)
                            .addComponent(txtPosition))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTemp1)
                            .addComponent(lblAnalog))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTemp2)
                            .addComponent(lblAnalog1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTemp3)
                            .addComponent(lblAnalog2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTemp4)
                            .addComponent(lblAnalog3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTemp5)
                            .addComponent(lblAnalog4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTemp6)
                            .addComponent(lblAnalog5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTemp7)
                            .addComponent(lblAnalog6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTemp8)
                            .addComponent(lblAnalog7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                        .addComponent(spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5))
        );
    }// </editor-fold>                        

    private void spinnerStateChanged(javax.swing.event.ChangeEvent evt) {                                     
        setSkips((String) spinner.getValue());
    }                                    

    // Variables declaration - do not modify                     
    private gui.Canvas canvas;
    private javax.swing.JLabel lblAnalog;
    private javax.swing.JLabel lblAnalog1;
    private javax.swing.JLabel lblAnalog2;
    private javax.swing.JLabel lblAnalog3;
    private javax.swing.JLabel lblAnalog4;
    private javax.swing.JLabel lblAnalog5;
    private javax.swing.JLabel lblAnalog6;
    private javax.swing.JLabel lblAnalog7;
    private javax.swing.JLabel lblTemp1;
    private javax.swing.JLabel lblTemp2;
    private javax.swing.JLabel lblTemp3;
    private javax.swing.JLabel lblTemp4;
    private javax.swing.JLabel lblTemp5;
    private javax.swing.JLabel lblTemp6;
    private javax.swing.JLabel lblTemp7;
    private javax.swing.JLabel lblTemp8;
    private javax.swing.JLabel[] lblTemps;
    private javax.swing.JSpinner spinner;
    private javax.swing.JLabel txtName;
    private javax.swing.JLabel txtPosition;
    // End of variables declaration                   
}
