import java.awt.Frame;
import java.sql.*;
import javax.swing.*;
import java.awt.Color;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Modifiko extends javax.swing.JDialog {
    private int idShites;

    public Modifiko(Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(parent);
        addEventHandlers();
    }
    

    public void setShitesData(int id, String emri, String mbiemri, String tel,
                              String username, String password, int arka, Date dataRegj) {
        this.idShites = id;
        EmrField.setText(emri);
        MbiemrField.setText(mbiemri);
        TelField.setText(tel);
        UsernameField.setText(username);
        PasswordField.setText(password);
        ArkaField.setText(String.valueOf(arka));
        if (dataRegj != null) {
            String dataTxt = new java.text.SimpleDateFormat("dd/MM/yyyy").format(dataRegj);
            DataField.setText(dataTxt);
        }
    }

    private void addEventHandlers() {
        Ruajbtn.addActionListener(e -> updateShites());
        AnulloBtn.addActionListener(e -> dispose());
    }

    private void updateShites() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE shites SET Emri=?, Mbiemri=?, Nr_Kontakti=?, Username=?, Password=?, Arka=?, Data_Regjistrimit=? WHERE ID_Shitesi=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, EmrField.getText().trim());
            ps.setString(2, MbiemrField.getText().trim());
            ps.setString(3, TelField.getText());
            ps.setString(4, UsernameField.getText().trim());
            ps.setString(5, PasswordField.getText().trim());
            ps.setInt(6, Integer.parseInt(ArkaField.getText().trim()));

            String dataStr = DataField.getText().trim();
            java.util.Date utilDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(dataStr);
            ps.setDate(7, new java.sql.Date(utilDate.getTime()));

            ps.setInt(8, idShites);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Shitesi u modifikua me sukses!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Asnje rresht nuk u perditesua!", "Gabim", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gabim gjate perditesimit: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        MbiemrField = new javax.swing.JTextField();
        AnulloBtn = new javax.swing.JButton();
        AnulloBtn.setForeground(new Color(51, 102, 153));
        TelField = new javax.swing.JTextField();
        UsernameLabel = new javax.swing.JLabel();
        UsernameLabel.setForeground(new Color(51, 102, 153));
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
        DataField = new javax.swing.JFormattedTextField();
        EmrLabel = new javax.swing.JLabel();
        EmrLabel.setForeground(new Color(51, 102, 153));
        PasswordField = new javax.swing.JTextField();
        MbiemrLabel = new javax.swing.JLabel();
        MbiemrLabel.setForeground(new Color(51, 102, 153));
        ArkaField = new javax.swing.JTextField();
        TelLabel = new javax.swing.JLabel();
        TelLabel.setForeground(new Color(51, 102, 153));
        EmrField = new javax.swing.JTextField();
        Ruajbtn = new javax.swing.JButton();
        Ruajbtn.setForeground(new Color(51, 102, 153));
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        UsernameField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("ModifikoJDialog"); // NOI18N

        MbiemrField.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N

        AnulloBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        AnulloBtn.setText("❌ Anullo");

        TelField.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N

        UsernameLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        UsernameLabel.setText("Username:");

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

        EmrLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        EmrLabel.setText("Emri:");

        PasswordField.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N

        MbiemrLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        MbiemrLabel.setText("Mbiemri:");

        ArkaField.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N

        TelLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        TelLabel.setText("Nr. Telefoni:");

        EmrField.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N

        Ruajbtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        Ruajbtn.setText("💾 Ruaj");
        Ruajbtn.setToolTipText("");

        jPanel1.setBackground(new Color(119, 136, 153));

        jLabel1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Modifiko te Dhenat");

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addGap(47)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(EmrLabel)
        				.addComponent(MbiemrLabel)
        				.addComponent(TelLabel))
        			.addGap(21)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        					.addComponent(EmrField)
        					.addComponent(MbiemrField)
        					.addComponent(TelField, GroupLayout.PREFERRED_SIZE, 268, GroupLayout.PREFERRED_SIZE))
        				.addComponent(jLabel4))
        			.addGap(18, 26, Short.MAX_VALUE)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(DataLabel)
        				.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        					.addComponent(UsernameLabel)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(ArkaLabel)
        						.addComponent(PasswordLabel))))
        			.addGap(43)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        				.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        					.addComponent(DataField, GroupLayout.PREFERRED_SIZE, 322, GroupLayout.PREFERRED_SIZE)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(PasswordField, Alignment.TRAILING)
        						.addComponent(ArkaField, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 321, GroupLayout.PREFERRED_SIZE)))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(1)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jLabel5)
        						.addComponent(UsernameField))))
        			.addGap(72))
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap(711, Short.MAX_VALUE)
        			.addComponent(Ruajbtn, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
        			.addGap(76)
        			.addComponent(AnulloBtn, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
        			.addGap(54))
        		.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 1075, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGap(0)
        			.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGap(73)
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(jLabel5)
        					.addGap(49)
        					.addComponent(UsernameField, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        					.addGap(20)
        					.addComponent(PasswordField, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        					.addGap(20)
        					.addComponent(ArkaField, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        					.addGap(20)
        					.addComponent(DataField, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(jLabel4)
        					.addGap(7)
        					.addComponent(UsernameLabel)
        					.addGap(18)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
        							.addGap(112)
        							.addComponent(DataLabel))
        						.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
        							.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        								.addComponent(EmrLabel)
        								.addComponent(EmrField, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        								.addComponent(PasswordLabel))
        							.addGap(20)
        							.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        								.addComponent(MbiemrLabel)
        								.addGroup(layout.createSequentialGroup()
        									.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        										.addComponent(MbiemrField, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        										.addComponent(ArkaLabel))
        									.addGap(1)))
        							.addGap(20)
        							.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        								.addComponent(TelLabel)
        								.addComponent(TelField, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))))))
        			.addPreferredGap(ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(AnulloBtn, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
        				.addComponent(Ruajbtn, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
        			.addGap(47))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>                        

    private void DataFieldActionPerformed(java.awt.event.ActionEvent evt) {                                          
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
            java.util.logging.Logger.getLogger(Modifiko.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Modifiko.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Modifiko.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Modifiko.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Modifiko dialog = new Modifiko(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
}
    // End of variables declaration