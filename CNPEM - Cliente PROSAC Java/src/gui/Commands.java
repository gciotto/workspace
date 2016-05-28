package gui;

public class Commands extends javax.swing.JPanel {

    public Commands() {
        initComponents();
        setNormal();
    }
    
    public void disableAll(){
    	
    	btCmd00.setEnabled(false);
        btCmd01.setEnabled(false);
        btCmd02.setEnabled(false);
        btCmd03.setEnabled(false);
        btCmd04.setEnabled(false);
        btCmd05.setEnabled(false);
        btCmdC8.setEnabled(false);
        btCmdC9.setEnabled(false);
        btCmdCB.setEnabled(false);
        btCmdCC.setEnabled(false);
        btCmdD0.setEnabled(false);
        btCmdE0.setEnabled(false);
        btCmdE1.setEnabled(false);
        btCmdCF.setEnabled(false);
    }
    
    public void setCyclingEnabled()
    {
        btCmd00.setEnabled(false);
        btCmd01.setEnabled(false);
        btCmd02.setEnabled(false);
        btCmd03.setEnabled(false);
        btCmd04.setEnabled(false);
        btCmd05.setEnabled(false);
        btCmdC8.setEnabled(false);
        btCmdC9.setEnabled(false);
        btCmdCB.setEnabled(false);
        btCmdCC.setEnabled(false);
        btCmdD0.setEnabled(false);
        btCmdE0.setEnabled(false);
        btCmdE1.setEnabled(true);        
    }
    
    public void setCyclingDone()
    {
        setNormal();
    }
    
    public void setRampSending()
    {
        btCmd00.setEnabled(false);
        btCmd01.setEnabled(false);
        btCmd02.setEnabled(false);
        btCmd03.setEnabled(false);
        btCmd04.setEnabled(false);
        btCmd05.setEnabled(false);
        btCmdC8.setEnabled(false);
        btCmdC9.setEnabled(true);
        btCmdCB.setEnabled(false);
        btCmdCC.setEnabled(false);
        btCmdD0.setEnabled(false);
        btCmdE0.setEnabled(false);
        btCmdE1.setEnabled(false);   
        
        
    }
    
    public void setRampSent()
    {
        setNormal();
    }
    
    public void setRampingEnabled()
    {
        btCmd00.setEnabled(false);
        btCmd01.setEnabled(false);
        btCmd02.setEnabled(false);
        btCmd03.setEnabled(false);
        btCmd04.setEnabled(false);
        btCmd05.setEnabled(false);
        btCmdC8.setEnabled(false);
        btCmdC9.setEnabled(false);
        btCmdCB.setEnabled(false);
        btCmdCC.setEnabled(true);
        btCmdD0.setEnabled(false);
        btCmdE0.setEnabled(false);
        btCmdE1.setEnabled(false);        
    }
    
    public void setRampingDone()
    {
        setNormal();
    }
    
    public void setNormal()
    {
        btCmd00.setEnabled(true);
        btCmd01.setEnabled(true);
        btCmd02.setEnabled(true);
        btCmd03.setEnabled(true);
        btCmd04.setEnabled(true);
        btCmd05.setEnabled(true);
        btCmdC8.setEnabled(true);
        btCmdC9.setEnabled(false);
        btCmdCB.setEnabled(true);
        btCmdCC.setEnabled(false);
        btCmdD0.setEnabled(true);
        btCmdE0.setEnabled(true);
        btCmdE1.setEnabled(false);        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        panelBasic = new javax.swing.JPanel();
        btCmd00 = new javax.swing.JButton();
        btCmd01 = new javax.swing.JButton();
        btCmd02 = new javax.swing.JButton();
        lblCmd02 = new javax.swing.JLabel();
        lblCmd01 = new javax.swing.JLabel();
        lblCmd00 = new javax.swing.JLabel();
        btCmd03 = new javax.swing.JButton();
        lblCmd03 = new javax.swing.JLabel();
        btCmd04 = new javax.swing.JButton();
        lblCmd04 = new javax.swing.JLabel();
        lblCmd05 = new javax.swing.JLabel();
        btCmd05 = new javax.swing.JButton();
        panelRamp = new javax.swing.JPanel();
        btCmdC8 = new javax.swing.JButton();
        lblCmdC8 = new javax.swing.JLabel();
        btCmdC9 = new javax.swing.JButton();
        lblCmdC9 = new javax.swing.JLabel();
        btCmdCB = new javax.swing.JButton();
        lblCmdCB = new javax.swing.JLabel();
        btCmdCC = new javax.swing.JButton();
        lblCmdCC = new javax.swing.JLabel();
        btCmdD0 = new javax.swing.JButton();
        lblCmdD0 = new javax.swing.JLabel();
        btCmdCF = new javax.swing.JButton();
        lblCmdCF = new javax.swing.JLabel();
        panelCycle = new javax.swing.JPanel();
        btCmdE0 = new javax.swing.JButton();
        lblCmdE0 = new javax.swing.JLabel();
        btCmdE1 = new javax.swing.JButton();
        lblCmdE1 = new javax.swing.JLabel();

        panelBasic.setBorder(javax.swing.BorderFactory.createTitledBorder("Básicos"));

        btCmd00.setText("00h");
        btCmd00.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCmd00ActionPerformed(evt);
            }
        });

        btCmd01.setText("01h");
        btCmd01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCmd01ActionPerformed(evt);
            }
        });

        btCmd02.setText("02h");
        btCmd02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCmd02ActionPerformed(evt);
            }
        });

        lblCmd02.setText("Identificação");

        lblCmd01.setText("Ajuste");

        lblCmd00.setText("Normal");

        btCmd03.setText("03h");
        btCmd03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCmd03ActionPerformed(evt);
            }
        });

        lblCmd03.setText("Fim Ident.");

        btCmd04.setText("04h");
        btCmd04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCmd04ActionPerformed(evt);
            }
        });

        lblCmd04.setText("Boot");

        lblCmd05.setText("Conf. Mensagem");

        btCmd05.setText("05h");
        btCmd05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCmd05ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBasicLayout = new javax.swing.GroupLayout(panelBasic);
        panelBasic.setLayout(panelBasicLayout);
        panelBasicLayout.setHorizontalGroup(
            panelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBasicLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBasicLayout.createSequentialGroup()
                        .addComponent(btCmd00)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCmd00))
                    .addGroup(panelBasicLayout.createSequentialGroup()
                        .addComponent(btCmd01)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCmd01))
                    .addGroup(panelBasicLayout.createSequentialGroup()
                        .addComponent(btCmd02)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCmd02)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBasicLayout.createSequentialGroup()
                        .addComponent(btCmd03)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCmd03))
                    .addGroup(panelBasicLayout.createSequentialGroup()
                        .addComponent(btCmd04)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCmd04))
                    .addGroup(panelBasicLayout.createSequentialGroup()
                        .addComponent(btCmd05)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCmd05))))
        );
        panelBasicLayout.setVerticalGroup(
            panelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBasicLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelBasicLayout.createSequentialGroup()
                        .addGroup(panelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btCmd03)
                            .addComponent(lblCmd03))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btCmd04)
                            .addComponent(lblCmd04))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btCmd05)
                            .addComponent(lblCmd05)))
                    .addGroup(panelBasicLayout.createSequentialGroup()
                        .addGroup(panelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btCmd00)
                            .addComponent(lblCmd00))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btCmd01)
                            .addComponent(lblCmd01))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btCmd02)
                            .addComponent(lblCmd02))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelRamp.setBorder(javax.swing.BorderFactory.createTitledBorder("Rampa"));

        btCmdC8.setText("C8h");
        btCmdC8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCmdC8ActionPerformed(evt);
            }
        });

        lblCmdC8.setText("Inic. Rampa");

        btCmdC9.setText("C9h");
        btCmdC9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCmdC9ActionPerformed(evt);
            }
        });

        lblCmdC9.setText("Transm. Bloco");

        btCmdCB.setText("CBh");
        btCmdCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCmdCBActionPerformed(evt);
            }
        });

        lblCmdCB.setText("Hab. Rampa");

        btCmdCC.setText("CCh");
        btCmdCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCmdCCActionPerformed(evt);
            }
        });

        lblCmdCC.setText("Abortar Rampa");

        btCmdD0.setText("D0h");
        btCmdD0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCmdD0ActionPerformed(evt);
            }
        });

        lblCmdD0.setText("Hab. Rampa Cíclica");

        btCmdCF.setText("CFh");
        btCmdCF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCmdCFActionPerformed(evt);
            }
        });

        lblCmdCF.setText("Hab. Leituras");

        javax.swing.GroupLayout panelRampLayout = new javax.swing.GroupLayout(panelRamp);
        panelRamp.setLayout(panelRampLayout);
        panelRampLayout.setHorizontalGroup(
            panelRampLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRampLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRampLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRampLayout.createSequentialGroup()
                        .addComponent(btCmdCB)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCmdCB))
                    .addGroup(panelRampLayout.createSequentialGroup()
                        .addGroup(panelRampLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRampLayout.createSequentialGroup()
                                .addComponent(btCmdC8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCmdC8))
                            .addGroup(panelRampLayout.createSequentialGroup()
                                .addComponent(btCmdC9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCmdC9)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelRampLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelRampLayout.createSequentialGroup()
                                .addComponent(btCmdCC)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCmdCC))
                            .addGroup(panelRampLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(panelRampLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelRampLayout.createSequentialGroup()
                                        .addComponent(btCmdCF)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblCmdCF))
                                    .addGroup(panelRampLayout.createSequentialGroup()
                                        .addComponent(btCmdD0)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblCmdD0)))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelRampLayout.setVerticalGroup(
            panelRampLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRampLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRampLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelRampLayout.createSequentialGroup()
                        .addGroup(panelRampLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btCmdCC)
                            .addComponent(lblCmdCC))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelRampLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btCmdD0)
                            .addComponent(lblCmdD0)))
                    .addGroup(panelRampLayout.createSequentialGroup()
                        .addGroup(panelRampLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btCmdC8)
                            .addComponent(lblCmdC8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelRampLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btCmdC9)
                            .addComponent(lblCmdC9))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelRampLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btCmdCB)
                    .addComponent(lblCmdCB)
                    .addGroup(panelRampLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btCmdCF)
                        .addComponent(lblCmdCF)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelCycle.setBorder(javax.swing.BorderFactory.createTitledBorder("Ciclagem"));

        btCmdE0.setText("E0h");
        btCmdE0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCmdE0ActionPerformed(evt);
            }
        });

        lblCmdE0.setText("Hab. Ciclagem");

        btCmdE1.setText("E1h");
        btCmdE1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCmdE1ActionPerformed(evt);
            }
        });

        lblCmdE1.setText("Abortar Ciclagem");

        javax.swing.GroupLayout panelCycleLayout = new javax.swing.GroupLayout(panelCycle);
        panelCycle.setLayout(panelCycleLayout);
        panelCycleLayout.setHorizontalGroup(
            panelCycleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCycleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCycleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCycleLayout.createSequentialGroup()
                        .addComponent(btCmdE0)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCmdE0))
                    .addGroup(panelCycleLayout.createSequentialGroup()
                        .addComponent(btCmdE1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCmdE1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCycleLayout.setVerticalGroup(
            panelCycleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCycleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCycleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btCmdE0)
                    .addComponent(lblCmdE0))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCycleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btCmdE1)
                    .addComponent(lblCmdE1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelBasic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelRamp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelCycle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelBasic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelRamp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelCycle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>                        

    private void btCmd00ActionPerformed(java.awt.event.ActionEvent evt) {                                        
        main.Client.btCommand(0x00);
    }                                       

    private void btCmd01ActionPerformed(java.awt.event.ActionEvent evt) {                                        
        main.Client.btCommand(0x01);
    }                                       

    private void btCmd02ActionPerformed(java.awt.event.ActionEvent evt) {                                        
        main.Client.btCommand(0x02);
    }                                       

    private void btCmd03ActionPerformed(java.awt.event.ActionEvent evt) {                                        
        main.Client.btCommand(0x03);
    }                                       

    private void btCmd04ActionPerformed(java.awt.event.ActionEvent evt) {                                        
        main.Client.btCommand(0x04);
    }                                       

    private void btCmd05ActionPerformed(java.awt.event.ActionEvent evt) {                                        
        main.Client.btCommand(0x05);
    }                                       

    private void btCmdC8ActionPerformed(java.awt.event.ActionEvent evt) {                                        
        main.Client.btCommand(0xC8);
    }                                       

    private void btCmdC9ActionPerformed(java.awt.event.ActionEvent evt) {                                        
        main.Client.btCommand(0xC9);
    }                                       

    private void btCmdCBActionPerformed(java.awt.event.ActionEvent evt) {                                        
        main.Client.btCommand(0xCB);
    }                                       

    private void btCmdCCActionPerformed(java.awt.event.ActionEvent evt) {                                        
        main.Client.btCommand(0xCC);
    }                                       

    private void btCmdD0ActionPerformed(java.awt.event.ActionEvent evt) {                                        
        main.Client.btCommand(0xD0);
    }                                       

    private void btCmdE0ActionPerformed(java.awt.event.ActionEvent evt) {                                        
        main.Client.btCommand(0xE0);
    }                                       

    private void btCmdE1ActionPerformed(java.awt.event.ActionEvent evt) {                                        
        main.Client.btCommand(0xE1);
    }                                       

    private void btCmdCFActionPerformed(java.awt.event.ActionEvent evt) {                                        
        main.Client.btCommand(0xCF);
    }                                       

    // Variables declaration - do not modify                     
    private javax.swing.JButton btCmd00;
    private javax.swing.JButton btCmd01;
    private javax.swing.JButton btCmd02;
    private javax.swing.JButton btCmd03;
    private javax.swing.JButton btCmd04;
    private javax.swing.JButton btCmd05;
    private javax.swing.JButton btCmdC8;
    private javax.swing.JButton btCmdC9;
    private javax.swing.JButton btCmdCB;
    private javax.swing.JButton btCmdCC;
    private javax.swing.JButton btCmdCF;
    private javax.swing.JButton btCmdD0;
    private javax.swing.JButton btCmdE0;
    private javax.swing.JButton btCmdE1;
    private javax.swing.JLabel lblCmd00;
    private javax.swing.JLabel lblCmd01;
    private javax.swing.JLabel lblCmd02;
    private javax.swing.JLabel lblCmd03;
    private javax.swing.JLabel lblCmd04;
    private javax.swing.JLabel lblCmd05;
    private javax.swing.JLabel lblCmdC8;
    private javax.swing.JLabel lblCmdC9;
    private javax.swing.JLabel lblCmdCB;
    private javax.swing.JLabel lblCmdCC;
    private javax.swing.JLabel lblCmdCF;
    private javax.swing.JLabel lblCmdD0;
    private javax.swing.JLabel lblCmdE0;
    private javax.swing.JLabel lblCmdE1;
    private javax.swing.JPanel panelBasic;
    private javax.swing.JPanel panelCycle;
    private javax.swing.JPanel panelRamp;
    // End of variables declaration                   
}
