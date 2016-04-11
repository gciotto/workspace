package gui;

import javax.swing.SpinnerListModel;
import main.Board;
import main.Client;

public class Locon extends javax.swing.JPanel {

    private Board board;
    
    private String[] spinnerValues = {"5 ms", "10 ms", "50 ms", "100 ms", "250 ms", "500 ms", "1000 ms"};
    
    public Locon() {
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
    
    public void refresh()
    {
        if(board == null)
            return;
        
        int[] readBytes = board.getReadBytes();
        
        int analog = (readBytes[2] << 8) + readBytes[3];
        int digital = readBytes[4];
        
        txtReadAnalog.setText(String.valueOf(analog));
        txtReadDigital.setText(String.valueOf(digital));
        
        canvas.addMeasure(txtReadAnalog.getText());
        
        
        
        
        int[] writeBytes = board.getWriteBytes();
        
        try
        {
            analog  = Integer.parseInt(txtWriteAnalog.getText());
            
        }
        catch(NumberFormatException ex)
        {
            analog = 0;
            txtWriteAnalog.setText("0");
        }
        
        try
        {
            digital = Integer.parseInt(txtWriteDigital.getText());
        }
        catch(NumberFormatException ex)
        {
            digital = 0;
            txtWriteDigital.setText("0");
        }
            
        
        /*if(analog > 4095)
            analog = 4095;
        else */if (analog < 0)
            analog = 0;
        
        if(digital > 255)
            digital = 255;
        else if (digital < 0)
            digital = 0;
        
        
        writeBytes[2] = (analog >> 8) & 0xFF;
        writeBytes[3] = analog & 0xFF;
        writeBytes[4] = digital;
        
        int cycleCurve = cbCycle.getSelectedIndex();
        
        if(cycleCurve == 0)
        {
            board.setWillCycle(false);
            board.setCycleCurve(0x80);
        }
        else
        {
            board.setWillCycle(true);
            board.setCycleCurve(cycleCurve - 1);
        }
        
        writeBytes[1] = board.getCycleCurve();
        
        
        
        int rampCurve = cbRamp.getSelectedIndex();
        
        if(rampCurve == 0)
        {
            board.setWillRamp(false);
            //board.setRampCurve(0x80);
        }
        else
        {
            board.setWillRamp(true);
            board.setRampCurve(rampCurve - 1);
                       
    
            board.setRampPulses(Integer.parseInt(String.valueOf(spinnerPulses.getValue())));
        }
                 
        
        
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtName = new javax.swing.JLabel();
        txtWriteAnalog = new javax.swing.JTextField();
        txtWriteDigital = new javax.swing.JTextField();
        lblAnalog = new javax.swing.JLabel();
        lblDigital = new javax.swing.JLabel();
        cbRamp = new javax.swing.JComboBox();
        cbCycle = new javax.swing.JComboBox();
        spinner = new javax.swing.JSpinner();
        canvas = new gui.Canvas();
        spinnerPulses = new javax.swing.JSpinner();
        txtReadAnalog = new javax.swing.JLabel();
        txtReadDigital = new javax.swing.JLabel();
        txtPosition = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txtName.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        txtName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtName.setText("LCN12BMP");

        txtWriteAnalog.setText("2000");

        txtWriteDigital.setText("128");

        lblAnalog.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblAnalog.setText("A");

        lblDigital.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblDigital.setText("D");

        cbRamp.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Não Rampa", "Rampa 0", "Rampa 1" }));

        cbCycle.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Não Cicla", "Cicla 0", "Cicla 1", "Cicla 2", "Cicla 3", "Cicla 4" }));

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

        spinnerPulses.setModel(new javax.swing.SpinnerNumberModel(1, 1, 4, 1));

        txtReadAnalog.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtReadAnalog.setText("65535");

        txtReadDigital.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtReadDigital.setText("255");

        txtPosition.setFont(new java.awt.Font("DejaVu Sans", 0, 18)); // NOI18N
        txtPosition.setText("99");
        txtPosition.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

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
                        .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
                    .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblDigital)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtReadDigital)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtWriteDigital, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblAnalog)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtReadAnalog, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtWriteAnalog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbCycle, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cbRamp, 0, 1, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerPulses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(spinner))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtWriteAnalog, txtWriteDigital});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtReadAnalog, txtReadDigital});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName)
                    .addComponent(txtWriteAnalog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPosition)
                    .addComponent(txtReadAnalog)
                    .addComponent(lblAnalog))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtWriteDigital, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtReadDigital)
                            .addComponent(lblDigital))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbCycle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spinnerPulses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbRamp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void spinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerStateChanged
        setSkips((String) spinner.getValue());
    }//GEN-LAST:event_spinnerStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gui.Canvas canvas;
    private javax.swing.JComboBox cbCycle;
    private javax.swing.JComboBox cbRamp;
    private javax.swing.JLabel lblAnalog;
    private javax.swing.JLabel lblDigital;
    private javax.swing.JSpinner spinner;
    private javax.swing.JSpinner spinnerPulses;
    private javax.swing.JLabel txtName;
    private javax.swing.JLabel txtPosition;
    private javax.swing.JLabel txtReadAnalog;
    private javax.swing.JLabel txtReadDigital;
    private javax.swing.JTextField txtWriteAnalog;
    private javax.swing.JTextField txtWriteDigital;
    // End of variables declaration//GEN-END:variables
}
