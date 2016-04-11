package gui;
public class Connection extends javax.swing.JPanel {

    private boolean connected = false;
    
    public Connection() {
        initComponents();
        
        setDisconnected();
    }

    
    public void setVersion(int[] version)
    {
        String ver = "Versão: ";
        
        ver += (char) version[0];
        
        ver += " ";
        
        ver += String.valueOf(version[1]) + ".";
        
        if(version[2] < 10)
            ver += "0";
        
        ver += String.valueOf(version[2]);
        
        ver += " Data: ";
        
        ver += String.valueOf(version[3]) + "/" + String.valueOf(version[4]) + "/" + String.valueOf(2000 + version[5]);
        
        lblVersion.setText(ver);
    }
    
    public void setConnected()
    {
        btConnect.setText("Desconectar");
        connected = true;
    }
    
    public void setDisconnected()
    {
        btConnect.setText("Conectar");
        connected = false;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblIP = new javax.swing.JLabel();
        txtIP = new javax.swing.JTextField();
        lblPort = new javax.swing.JLabel();
        txtPort = new javax.swing.JTextField();
        btConnect = new javax.swing.JButton();
        lblVersion = new javax.swing.JLabel();

        lblIP.setText("IP Nó:");

        txtIP.setText("10.128.11.1");

        lblPort.setText("Porta:");

        txtPort.setText("4000");

        btConnect.setText("Conectar");
        btConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btConnectActionPerformed(evt);
            }
        });

        lblVersion.setText("versao");
        lblVersion.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblIP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(lblPort)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btConnect)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblVersion)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIP)
                    .addComponent(txtIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btConnect)
                    .addComponent(txtPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPort)
                    .addComponent(lblVersion))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btConnectActionPerformed
        main.Client.btConnect(txtIP.getText(), txtPort.getText());
    }//GEN-LAST:event_btConnectActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btConnect;
    private javax.swing.JLabel lblIP;
    private javax.swing.JLabel lblPort;
    private javax.swing.JLabel lblVersion;
    private javax.swing.JTextField txtIP;
    private javax.swing.JTextField txtPort;
    // End of variables declaration//GEN-END:variables
}
