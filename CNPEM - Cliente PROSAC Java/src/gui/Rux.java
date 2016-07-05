package gui;

import javax.swing.SpinnerListModel;
import main.Board;
import main.Client;

public class Rux extends javax.swing.JPanel {

    private Board board;
    
    private String[] spinnerValues = {"5 ms", "10 ms", "50 ms", "100 ms", "250 ms", "500 ms", "1000 ms"};
    
    public Rux() {
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
            
            this.board.setWillCycle(false);
            this.board.setWillRamp(false);
        }
        
    }

    public void setDigitalCanvasMax(int max){
    	
    	this.canvas.setMaxOfChannel(max, 4);
    }
    
    public void setDigitalCanvasMin(int min){
    	
    	this.canvas.setMinOfChannel(min, 4);
    }
    
    public void setAnalogCanvasMax(int max){
    	
    	for (int i =0 ; i < 4; i++)
    		this.canvas.setMaxOfChannel(max, i);
    }
    
    public void setAnalogCanvasMin(int min) {
    	
    	for (int i =0 ; i < 4; i++)
    		this.canvas.setMinOfChannel(min, i);
    }
    
    public void refresh()
    {
        if(board == null)
            return;
        
        int[] readBytes = board.getReadBytes();
        
        int analogCh1 = (readBytes[2] << 8) + readBytes[3],
        	analogCh2 = (readBytes[4] << 8) + readBytes[5],
        	analogCh3 = (readBytes[6] << 8) + readBytes[7],
        	analogCh4 = (readBytes[8] << 8) + readBytes[9];
        int digital = readBytes[10];
        
        txtReadAnalogChannel1.setText(String.valueOf(analogCh1));
        txtReadAnalogChannel2.setText(String.valueOf(analogCh2));
        txtReadAnalogChannel3.setText(String.valueOf(analogCh3));
        txtReadAnalogChannel4.setText(String.valueOf(analogCh4));
        txtReadDigital.setText(String.valueOf(digital));
        
        canvas.addMeasure(0, txtReadAnalogChannel1.getText());
        canvas.addMeasure(1, txtReadAnalogChannel2.getText());
        canvas.addMeasure(2, txtReadAnalogChannel3.getText());
        canvas.addMeasure(3, txtReadAnalogChannel4.getText());
        canvas.addMeasure(4, txtReadDigital.getText());
       
        int[] writeBytes = board.getWriteBytes();
        
        try
        {
            digital = Integer.parseInt(txtWriteDigital.getText());
        }
        catch(NumberFormatException ex)
        {
            digital = 0;
            txtWriteDigital.setText("0");
        }
            
        if(digital > 255)
            digital = 255;
        else if (digital < 0)
            digital = 0;
        
        writeBytes[2] = digital;  
        writeBytes[1] = 128;
        
    }
    
    
    
    /*public String[] getValues()
    {
        String[] values = new String[4];
        
        values[0] = txtWriteAnalog.getText();
        values[1] = txtWriteDigital.getText();
        
        
        int cycleCurve = cbCycle.getSelectedIndex();       
        cycleCurve = cycleCurve == 0 ? 0x80 : cycleCurve - 1;
        
        int rampCurve = cbRamp.getSelectedIndex();
        
        values[2] = String.valueOf(rampCurve);
        values[3] = String.valueOf(cycleCurve);
        
        
        return values;
    }
    
    public void setValues(String[] values)
    {
        return;
        /*if(values != null)
        {
            txtReadAnalog.setText(values[0]);
            txtReadDigital.setText(values[1]);
            canvas.addMeasure(values[0]);            
        }*
    }*/
    
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
        txtWriteDigital = new javax.swing.JTextField();
        lblAnalogChannel1 = new javax.swing.JLabel();
        lblDigital = new javax.swing.JLabel();
        spinner = new javax.swing.JSpinner();
        canvas = new gui.Canvas(5);
        txtReadAnalogChannel1 = new javax.swing.JLabel();
        txtReadDigital = new javax.swing.JLabel();
        txtPosition = new javax.swing.JLabel();
        lblAnalogChannel2 = new javax.swing.JLabel();
        txtReadAnalogChannel2 = new javax.swing.JLabel();
        lblAnalogChannel3 = new javax.swing.JLabel();
        txtReadAnalogChannel3 = new javax.swing.JLabel();
        lblAnalogChannel4 = new javax.swing.JLabel();
        txtReadAnalogChannel4 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txtName.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        txtName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtName.setText("RUX12BBP");

        txtWriteDigital.setText("128");
        txtWriteDigital.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtWriteDigitalActionPerformed(evt);
            }
        });

        lblAnalogChannel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblAnalogChannel1.setText("Channel 1");

        lblDigital.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblDigital.setText("D");

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

        txtReadAnalogChannel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtReadAnalogChannel1.setText("2000");

        txtReadDigital.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtReadDigital.setText("255");

        txtPosition.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        txtPosition.setText("99");
        txtPosition.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblAnalogChannel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblAnalogChannel2.setText("Channel 2");

        txtReadAnalogChannel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtReadAnalogChannel2.setText("2000");

        lblAnalogChannel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblAnalogChannel3.setText("Channel 3");

        txtReadAnalogChannel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtReadAnalogChannel3.setText("2000");

        lblAnalogChannel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblAnalogChannel4.setText("Channel 4");

        txtReadAnalogChannel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtReadAnalogChannel4.setText("2000");

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
                        .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE))
                    .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblDigital, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtReadDigital)
                        .addGap(18, 18, 18)
                        .addComponent(txtWriteDigital, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblAnalogChannel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtReadAnalogChannel1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblAnalogChannel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtReadAnalogChannel2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblAnalogChannel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtReadAnalogChannel3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblAnalogChannel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtReadAnalogChannel4, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 3, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName)
                    .addComponent(txtPosition)
                    .addComponent(lblAnalogChannel1)
                    .addComponent(txtReadAnalogChannel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtReadAnalogChannel2)
                            .addComponent(lblAnalogChannel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAnalogChannel3)
                            .addComponent(txtReadAnalogChannel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAnalogChannel4)
                            .addComponent(txtReadAnalogChannel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDigital)
                            .addComponent(txtReadDigital)
                            .addComponent(txtWriteDigital, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );
    }// </editor-fold>                         

    private void spinnerStateChanged(javax.swing.event.ChangeEvent evt) {                                     
        setSkips((String) spinner.getValue());
    }                                    

    private void txtWriteDigitalActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
    }                                               

    // Variables declaration - do not modify                     
    private gui.Canvas canvas;
    private javax.swing.JLabel lblAnalogChannel1;
    private javax.swing.JLabel lblAnalogChannel2;
    private javax.swing.JLabel lblAnalogChannel3;
    private javax.swing.JLabel lblAnalogChannel4;
    private javax.swing.JLabel lblDigital;
    private javax.swing.JSpinner spinner;
    private javax.swing.JLabel txtName;
    private javax.swing.JLabel txtPosition;
    private javax.swing.JLabel txtReadAnalogChannel1;
    private javax.swing.JLabel txtReadAnalogChannel2;
    private javax.swing.JLabel txtReadAnalogChannel3;
    private javax.swing.JLabel txtReadAnalogChannel4;
    private javax.swing.JLabel txtReadDigital;
    private javax.swing.JTextField txtWriteDigital;
    // End of variables declaration                   
}
