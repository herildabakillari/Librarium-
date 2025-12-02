import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import java.awt.Color;


public class ShtoShites extends javax.swing.JDialog {

   private Shites parentFrame;

   
    public ShtoShites(Shites parent, boolean modal) {
       super(parent, modal);
  this.parentFrame = parent;
    initComponents();
    pastroFushat(); 
   
    }
 private void ruajDheShfaq() {
        if (EmrField.getText().isEmpty() || MbiemrField.getText().isEmpty() ||
            TelField.getText().isEmpty() || UsernameField.getText().isEmpty() ||
            PasswordField.getText().isEmpty() || ArkaField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ju lutem plotesoni te gjitha fushat!", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String emri = EmrField.getText();
            String mbiemri = MbiemrField.getText();
            String telefoni = TelField.getText();
            String username = UsernameField.getText();
            String password = PasswordField.getText();
            int arka = Integer.parseInt(ArkaField.getText());

            String dataStr = DataField.getText();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date parsedDate = sdf.parse(dataStr);
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

            if (ekzistonNeDatabase(telefoni, username, password, arka)) {
                return;
            }

            if (ruajNeDatabase(emri, mbiemri, telefoni, username, password, arka, sqlDate)) {
    JOptionPane.showMessageDialog(this, "Shitesi u ruajt me sukses!");
    pastroFushat();
    dispose(); 
    parentFrame.loadSellersData();
}
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vlera e arkes duhet te jete numer i plote!", "Gabim", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gabim gjate ruajtjes: " + e.getMessage(), "Gabim", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean ekzistonNeDatabase(String telefoni, String username, String password, int arka) {
        String sql = "SELECT Nr_Kontakti, Username, Password, Arka FROM shites";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                if (rs.getString("Nr_Kontakti").equals(telefoni)) {
                    JOptionPane.showMessageDialog(this, "Ky numer telefoni ekziston tashme!", "Gabim", JOptionPane.ERROR_MESSAGE);
                    return true;
                }
                if (rs.getString("Username").equals(username)) {
                    JOptionPane.showMessageDialog(this, "Ky username eshte i zene!", "Gabim", JOptionPane.ERROR_MESSAGE);
                    return true;
                }
                if (rs.getString("Password").equals(password)) {
                    JOptionPane.showMessageDialog(this, "Ky password eshte i perdorur!", "Gabim", JOptionPane.ERROR_MESSAGE);
                    return true;
                }
                if (rs.getInt("Arka") == arka) {
                    JOptionPane.showMessageDialog(this, "Kjo arke eshte caktuar tashme per nje shites tjeter!", "Gabim", JOptionPane.ERROR_MESSAGE);
                    return true;
                }
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gabim gjate kontrollit te te dhenave ne databaze!");
            return true;
        }
    }
  private void pastroFushat() {
        EmrField.setText("");
        MbiemrField.setText("");
        TelField.setText("");
        UsernameField.setText("");
        PasswordField.setText("");
        ArkaField.setText("");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DataField.setText(sdf.format(new Date()));
    }

    private boolean ruajNeDatabase(String emri, String mbiemri, String telefoni,
                                   String username, String password, int arka, java.sql.Date dataReg) {
        String sql = "INSERT INTO shites (Emri, Mbiemri, Nr_Kontakti, Username, Password, Arka, Data_Regjistrimit) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, emri);
            pst.setString(2, mbiemri);
            pst.setString(3, telefoni);
            pst.setString(4, username);
            pst.setString(5, password);
            pst.setInt(6, arka);
            pst.setDate(7, dataReg);

            pst.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gabim gjate ruajtjes ne databaze!");
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        UsernameLabel = new javax.swing.JLabel();
        UsernameLabel.setForeground(new Color(51, 102, 153));
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        PasswordLabel = new javax.swing.JLabel();
        PasswordLabel.setForeground(new Color(51, 102, 153));
        ArkaLabel = new javax.swing.JLabel();
        ArkaLabel.setForeground(new Color(51, 102, 153));
        jLabel4 = new javax.swing.JLabel();
        jLabel4.setForeground(new Color(51, 102, 153));
        DataLabel = new javax.swing.JLabel();
        DataLabel.setForeground(new Color(51, 102, 153));
        jLabel5 = new javax.swing.JLabel();
        jLabel5.setForeground(new Color(51, 102, 153));
        UsernameField = new javax.swing.JTextField();
        EmrLabel = new javax.swing.JLabel();
        EmrLabel.setForeground(new Color(51, 102, 153));
        PasswordField = new javax.swing.JTextField();
        MbiemrLabel = new javax.swing.JLabel();
        MbiemrLabel.setForeground(new Color(51, 102, 153));
        ArkaField = new javax.swing.JTextField();
        TelLabel = new javax.swing.JLabel();
        TelLabel.setForeground(new Color(51, 102, 153));
        Ruajbtn = new javax.swing.JButton();
        Ruajbtn.setForeground(new Color(51, 102, 153));
        EmrField = new javax.swing.JTextField();
        AnulloBtn = new javax.swing.JButton();
        AnulloBtn.setForeground(new Color(51, 102, 153));
        MbiemrField = new javax.swing.JTextField();
        DataField = new javax.swing.JFormattedTextField();
        TelField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        UsernameLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        UsernameLabel.setText("Username:");

        jPanel3.setBackground(new Color(119, 136, 153));

        jLabel3.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Shites i Ri");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        PasswordLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        PasswordLabel.setText("Password:");

        ArkaLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        ArkaLabel.setText("Arka:");

        jLabel4.setFont(new java.awt.Font("Serif", 1, 22)); // NOI18N
        jLabel4.setText("Te Dhena Personale");

        DataLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        DataLabel.setText("Data e Regjistrimit:");

        jLabel5.setFont(new java.awt.Font("Serif", 1, 22)); // NOI18N
        jLabel5.setText("Te Dhena Llogarie");

        EmrLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        EmrLabel.setText("Emri:");

        MbiemrLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        MbiemrLabel.setText("Mbiemri:");

        TelLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        TelLabel.setText("Nr. Telefoni:");

        Ruajbtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        Ruajbtn.setText("💾 Ruaj");
        Ruajbtn.setToolTipText("");
        Ruajbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuajbtnActionPerformed(evt);
            }
        });

        EmrField.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N

        AnulloBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        AnulloBtn.setText("❌ Anullo");
        AnulloBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnulloBtnActionPerformed(evt);
            }
        });

        MbiemrField.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N

        try {
            DataField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        DataField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DataFieldActionPerformed(evt);
            }
        });

        TelField.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Ruajbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76)
                .addComponent(AnulloBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(EmrLabel)
                            .addComponent(MbiemrLabel)
                            .addComponent(TelLabel))
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(EmrField)
                            .addComponent(MbiemrField)
                            .addComponent(TelField, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(jLabel5))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(UsernameLabel)
                                    .addComponent(PasswordLabel)
                                    .addComponent(ArkaLabel))
                                .addGap(95, 95, 95))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(DataLabel)
                                .addGap(18, 18, 18)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(PasswordField)
                                    .addComponent(UsernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(DataField, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(ArkaField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(122, 122, 122)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(UsernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(UsernameLabel))
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(PasswordLabel)
                            .addComponent(PasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ArkaLabel)
                            .addComponent(ArkaField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(DataField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DataLabel)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(EmrLabel)
                            .addComponent(EmrField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(MbiemrLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(MbiemrField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TelLabel)
                            .addComponent(TelField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 152, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AnulloBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Ruajbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
        );

        pack();
    }// </editor-fold>                        

    private void DataFieldActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                         

    private void RuajbtnActionPerformed(java.awt.event.ActionEvent evt) {                                        
     ruajDheShfaq();
    }                                       

    private void AnulloBtnActionPerformed(java.awt.event.ActionEvent evt) {                                          
     dispose();
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
            java.util.logging.Logger.getLogger(ShtoShites.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShtoShites.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShtoShites.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShtoShites.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        
            java.awt.EventQueue.invokeLater(new Runnable() {
    public void run() {
        Shites shitesFrame = new Shites();
        ShtoShites dialog = new ShtoShites(shitesFrame, true);
        dialog.setVisible(true);
    }
});
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton AnulloBtn;
    private javax.swing.JTextField ArkaField;
    private javax.swing.JLabel ArkaLabel;
    private javax.swing.JFormattedTextField DataField;
    private javax.swing.JLabel DataLabel;
    private javax.swing.JTextField EmrField;
    private javax.swing.JLabel EmrLabel;
    private javax.swing.JTextField MbiemrField;
    private javax.swing.JLabel MbiemrLabel;
    private javax.swing.JTextField PasswordField;
    private javax.swing.JLabel PasswordLabel;
    private javax.swing.JButton Ruajbtn;
    private javax.swing.JTextField TelField;
    private javax.swing.JLabel TelLabel;
    private javax.swing.JTextField UsernameField;
    private javax.swing.JLabel UsernameLabel;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration                   
}