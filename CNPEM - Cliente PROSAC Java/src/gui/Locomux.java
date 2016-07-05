package gui;

import javax.swing.SpinnerListModel;
import main.Board;
import main.Client;
import util.Complex;
import util.FFT;

public class Locomux extends javax.swing.JPanel {

    private Board board;
    
    private String[] spinnerValues = {"5 ms", "10 ms", "50 ms", "100 ms", "250 ms", "500 ms", "1000 ms"};
    
    
    /* DEBUG */
    private int measurei = 0;
    private Complex[] measures;
    
    public Locomux() {
        
        measures  = new Complex[128];
        initComponents();
        
        setSkips((String)spinner.getValue());
        
        canvas.setMaxOfChannel(0xFFFF, 0);
        canvas.setMinOfChannel(0, 0);
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
        
        int[][] data = new int[4][2]; // Four channels. Two values per channel (current and integration)
        int mode, integrations, interval, duration;
        
        for(int i = 0; i < 4; ++i)
        {
            int iData, iInteg;
            
            iData = 2+ i*2;
            iInteg = iData + 8;
            
            data[i][0] = (readBytes[iData]  <<  8) + readBytes[iData+1];
            data[i][1] = (readBytes[iInteg] << 16) + (readBytes[iInteg+1] << 8) + readBytes[iInteg+2];
        }
        
        integrations = readBytes[20];
        
        canvas.addMeasure(0, String.valueOf(data[Integer.parseInt(String.valueOf(spinnerChannel.getValue()))][0]));      
        
        try
        {
            mode  = Integer.parseInt(txtMode.getText());
            
        }
        catch(NumberFormatException ex)
        {
            mode = 0;
        }
        
        try
        {
            integrations = Integer.parseInt(txtIntegrations.getText());
        }
        catch(NumberFormatException ex)
        {
            integrations = 1;
        }
        
        try
        {
            interval = Integer.parseInt(txtInterval.getText());
        }
        catch(NumberFormatException ex)
        {
            interval = 1;
        }
        
        try
        {
            duration = Integer.parseInt(txtDuration.getText());
        }
        catch(NumberFormatException ex)
        {
            duration = 1;
            //txtDuration.setText("1");
        }
        
       
        
        writeBytes[2] = mode & 0xFF;
        writeBytes[3] = integrations & 0xFF;
        writeBytes[4] = interval & 0xFF;
        writeBytes[5] = duration & 0xFF;
        

        board.setWillCycle(false);
        board.setCycleCurve(0x80);

        board.setWillRamp(false);

        
        writeBytes[1] = board.getCycleCurve();
        
        
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtName = new javax.swing.JLabel();
        txtMode = new javax.swing.JTextField();
        txtIntegrations = new javax.swing.JTextField();
        lblMode = new javax.swing.JLabel();
        lblIntegrations = new javax.swing.JLabel();
        spinner = new javax.swing.JSpinner();
        canvas = new gui.Canvas(1);
        txtPosition = new javax.swing.JLabel();
        lblInterval = new javax.swing.JLabel();
        txtInterval = new javax.swing.JTextField();
        txtDuration = new javax.swing.JTextField();
        lblDuration = new javax.swing.JLabel();
        spinnerChannel = new javax.swing.JSpinner();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        txtName.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        txtName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtName.setText("MUX16BBP");

        txtMode.setText("0");

        txtIntegrations.setText("64");

        lblMode.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblMode.setText("Modo");

        lblIntegrations.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblIntegrations.setText("Integ");

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

        lblInterval.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblInterval.setText("Interv");

        txtInterval.setText("248");

        txtDuration.setText("48");

        lblDuration.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        lblDuration.setText("Dur");

        spinnerChannel.setModel(new javax.swing.SpinnerNumberModel(0, 0, 3, 1));

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
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(spinnerChannel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDuration))
                            .addComponent(lblInterval, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblIntegrations, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblMode, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMode, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtIntegrations, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtInterval, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDuration, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(spinner))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtDuration, txtIntegrations, txtInterval, txtMode});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMode)
                    .addComponent(txtPosition)
                    .addComponent(txtName))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIntegrations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblIntegrations))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblInterval))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDuration)
                            .addComponent(spinnerChannel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(canvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void spinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinnerStateChanged
        setSkips((String) spinner.getValue());
    }//GEN-LAST:event_spinnerStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gui.Canvas canvas;
    private javax.swing.JLabel lblDuration;
    private javax.swing.JLabel lblIntegrations;
    private javax.swing.JLabel lblInterval;
    private javax.swing.JLabel lblMode;
    private javax.swing.JSpinner spinner;
    private javax.swing.JSpinner spinnerChannel;
    private javax.swing.JTextField txtDuration;
    private javax.swing.JTextField txtIntegrations;
    private javax.swing.JTextField txtInterval;
    private javax.swing.JTextField txtMode;
    private javax.swing.JLabel txtName;
    private javax.swing.JLabel txtPosition;
    // End of variables declaration//GEN-END:variables
}
