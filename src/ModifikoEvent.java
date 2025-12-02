import java.awt.Frame;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.awt.Color;


public class ModifikoEvent extends javax.swing.JDialog {

      private int idEvent;
      
    public ModifikoEvent(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
         PershkrimiArea.setLineWrap(true);
         PershkrimiArea.setWrapStyleWord(true);
        setLocationRelativeTo(parent);
        addEventHandlers();
    }
    

    public void setEventData(int id, String pershkrimi, String libri_promovuar, Date dataOra,
                              String vendndodhja) {
        this.idEvent = id;
    PershkrimiArea.setText(pershkrimi);
    LibriField.setText(libri_promovuar);

    if (dataOra != null) {
        String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dataOra);
        DataOraField.setText(formattedDate);
        DataOraField.setToolTipText("Format: dd/MM/yyyy HH:mm");
    } else {
        DataOraField.setText("");
    }

    VendndodhjaField.setText(vendndodhja);
}

    private void addEventHandlers() {
        RuajBtn.addActionListener(e -> updateEvent());
        AnulloBtn.addActionListener(e -> dispose());
    }

    private void updateEvent() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE eventet SET Pershkrimi=?, Libri_Promovuar=?, Data_Ora=?, Vendndodhja=? WHERE ID_Eventi=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, PershkrimiArea.getText().trim());
            ps.setString(2, LibriField.getText().trim());
            
            String dataStr = DataOraField.getText().trim();
             
            java.util.Date utilDate = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dataStr);
            java.sql.Timestamp timestamp = new java.sql.Timestamp(utilDate.getTime());
            ps.setTimestamp(3, timestamp);
            ps.setString(4, VendndodhjaField.getText().trim());
           
            ps.setInt(5, idEvent);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Eventi u modifikua me sukses!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Asnje rresht nuk u përditesua!", "Gabim", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gabim gjate perditësimit: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        RuajBtn = new javax.swing.JButton();
        RuajBtn.setForeground(new Color(51, 102, 153));
        VendndodhjaField = new javax.swing.JTextField();
        AnulloBtn = new javax.swing.JButton();
        AnulloBtn.setForeground(new Color(51, 102, 153));
        LibriField = new javax.swing.JTextField();
        PershkrimiLabel = new javax.swing.JLabel();
        PershkrimiLabel.setForeground(new Color(51, 102, 153));
        VendndodhjaLabel = new javax.swing.JLabel();
        VendndodhjaLabel.setForeground(new Color(51, 102, 153));
        DataOraLabel = new javax.swing.JLabel();
        DataOraLabel.setForeground(new Color(51, 102, 153));
        LibriLabel = new javax.swing.JLabel();
        LibriLabel.setForeground(new Color(51, 102, 153));
        DataOraField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        PershkrimiArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        RuajBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        RuajBtn.setText("💾 Ruaj");
        RuajBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RuajBtnMouseClicked(evt);
            }
        });
        RuajBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuajBtnActionPerformed(evt);
            }
        });

        VendndodhjaField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        AnulloBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        AnulloBtn.setText("❌ Anullo");
        AnulloBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnulloBtnActionPerformed(evt);
            }
        });

        LibriField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        PershkrimiLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        PershkrimiLabel.setText("Pershkrimi:");

        VendndodhjaLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        VendndodhjaLabel.setText("Vendndodhja:");

        DataOraLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        DataOraLabel.setText("Data & Ora:");

        LibriLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        LibriLabel.setText("Libri i Promovuar:");

        DataOraField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        DataOraField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DataOraFieldActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new Color(119, 136, 153));

        jLabel1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Modifiko Event");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PershkrimiArea.setColumns(20);
        PershkrimiArea.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        PershkrimiArea.setRows(5);
        jScrollPane1.setViewportView(PershkrimiArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PershkrimiLabel)
                    .addComponent(DataOraLabel)
                    .addComponent(VendndodhjaLabel)
                    .addComponent(LibriLabel))
                .addGap(60, 60, 60)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(LibriField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                    .addComponent(DataOraField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(VendndodhjaField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1))
                .addContainerGap(94, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(AnulloBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(RuajBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PershkrimiLabel)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(LibriLabel))
                    .addComponent(LibriField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(DataOraLabel)
                    .addComponent(DataOraField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(VendndodhjaLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(VendndodhjaField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RuajBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnulloBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33))
        );

        pack();
    }// </editor-fold>                        

    private void RuajBtnMouseClicked(java.awt.event.MouseEvent evt) {                                     

    }                                    

    private void RuajBtnActionPerformed(java.awt.event.ActionEvent evt) {                                        

    }                                       

    private void AnulloBtnActionPerformed(java.awt.event.ActionEvent evt) {                                          
       dispose();

    }                                         

    private void DataOraFieldActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }                                            

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ModifikoEvent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModifikoEvent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModifikoEvent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModifikoEvent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ModifikoEvent dialog = new ModifikoEvent(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton AnulloBtn;
    private javax.swing.JTextField DataOraField;
    private javax.swing.JLabel DataOraLabel;
    private javax.swing.JTextField LibriField;
    private javax.swing.JLabel LibriLabel;
    private javax.swing.JTextArea PershkrimiArea;
    private javax.swing.JLabel PershkrimiLabel;
    private javax.swing.JButton RuajBtn;
    private javax.swing.JTextField VendndodhjaField;
    private javax.swing.JLabel VendndodhjaLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration                   
}