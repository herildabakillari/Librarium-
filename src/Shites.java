import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;



public class Shites extends javax.swing.JFrame {
	  private AdminPage adminPage;
    
 private final List<String> shitesit = new ArrayList<>();

 public Shites(AdminPage adminPage) {
	    this.adminPage = adminPage;
	    initComponents();
	    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    this.setVisible(true);
	    setupAutoCompleteForShitesi(ShitesiComboBox);
	    loadSellersData();
	}

    private void setupAutoCompleteForShitesi(JComboBox<String> comboBox) {
    shitesit.clear();

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT Emri, Mbiemri FROM shites ORDER BY Emri");
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            String fullName = rs.getString("Emri") + " " + rs.getString("Mbiemri");
            shitesit.add(fullName);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    comboBox.setEditable(true);
    JTextField tf = (JTextField) comboBox.getEditor().getEditorComponent();

    tf.addKeyListener(new KeyAdapter() {
        public void keyReleased(KeyEvent e) {
            String text = tf.getText();
            int caret = tf.getCaretPosition();

            comboBox.hidePopup();
            comboBox.removeAllItems();

            for (String item : shitesit) {
                if (item.toLowerCase().contains(text.toLowerCase())) {
                    comboBox.addItem(item);
                }
            }

            tf.setText(text);
            tf.setCaretPosition(caret);

            if (comboBox.getItemCount() > 0) {
                comboBox.showPopup();
            }
        }
    });
  }  
private void mbushTabelenShites(String emri, String mbiemri) {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); 

    String sql = "SELECT Nr_Kontakti, Username, Arka FROM shites WHERE Emri = ? AND Mbiemri = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, emri);
        ps.setString(2, mbiemri);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String telefoni = rs.getString("Nr_Kontakti");
                String username = rs.getString("Username");
                int arka = rs.getInt("Arka");

                model.addRow(new Object[]{emri, mbiemri, telefoni, username, arka});
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gabim gjate marrjes se te dhenave.", "Gabim", JOptionPane.ERROR_MESSAGE);
    }
}

    protected void loadSellersData() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
            "ID", "Emri", "Mbiemri", "Nr. Telefoni", "Username", "Password", "Arka", "Data e Regjistrimit"
        });

        String sql = "SELECT ID_Shitesi, Emri, Mbiemri, Nr_Kontakti, Username, Password, Arka, Data_Regjistrimit FROM shites";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("ID_Shitesi"),
                    rs.getString("Emri"),
                    rs.getString("Mbiemri"),
                    rs.getString("Nr_Kontakti"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getInt("Arka"),
                    rs.getDate("Data_Regjistrimit")
                });
            }
            jTable1.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Gabim gjate marrjes se te dhenave:\n" + e.getMessage(),
                "SQL Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void kerkoShitesin(String input) {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); 

    String[] parts = input.trim().split("\\s+", 2); 
    String query;
    boolean hasBoth = parts.length == 2;

    if (hasBoth) {
        query = "SELECT * FROM shites WHERE Emri LIKE ? AND Mbiemri LIKE ?";
    } else {
        query = "SELECT * FROM shites WHERE Emri LIKE ? OR Mbiemri LIKE ?";
    }

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {

        if (hasBoth) {
            ps.setString(1, "%" + parts[0] + "%");
            ps.setString(2, "%" + parts[1] + "%");
        } else {
            ps.setString(1, "%" + parts[0] + "%");
            ps.setString(2, "%" + parts[0] + "%");
        }

        try (ResultSet rs = ps.executeQuery()) {
            boolean found = false;

            while (rs.next()) {
                found = true;
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("ID_Shitesi"));
                row.add(rs.getString("Emri"));
                row.add(rs.getString("Mbiemri"));
                row.add(rs.getString("Nr_Kontakti"));
                row.add(rs.getString("Username"));
                row.add(rs.getString("Password"));
                row.add(rs.getString("Arka"));
                row.add(rs.getDate("Data_Regjistrimit"));
                model.addRow(row);
            }

            if (!found) {
                JOptionPane.showMessageDialog(null, "Nuk u gjet asnje shites.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }

        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gabim gjate kerkimit te shitesit.", "Gabim", JOptionPane.ERROR_MESSAGE);
    }
}


  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        KerkoBtn = new javax.swing.JButton();
        KerkoBtn.setForeground(new Color(51, 102, 153));
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        ShtoShitesBtn = new javax.swing.JButton();
        ShtoShitesBtn.setForeground(new Color(51, 102, 153));
        ModifikoBtn = new javax.swing.JButton();
        ModifikoBtn.setForeground(new Color(51, 102, 153));
        FshiBtn = new javax.swing.JButton();
        FshiBtn.setForeground(new Color(51, 102, 153));
        jButton1 = new javax.swing.JButton();
        jButton1.setForeground(new Color(51, 102, 153));
        ShitesiComboBox = new javax.swing.JComboBox<>();
        RifreskoBtn = new javax.swing.JButton();
        RifreskoBtn.setForeground(new Color(51, 102, 153));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 102, 102));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel1.setText("Shitesit Aktuale");

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addGap(13, 13, 13))
        );

        jPanel2.setBackground(new Color(119, 136, 153));
        jPanel2.setForeground(new java.awt.Color(102, 102, 102));

        jLabel2.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Kerko Shites");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        KerkoBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        KerkoBtn.setText("🔍 Kerko");
        KerkoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KerkoBtnActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Emri", "Mbiemri", "Nr.Telefoni", "Username", "Password", "Arka", "Data.Regjistrimit"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(30);
        jScrollPane1.setViewportView(jTable1);

        ShtoShitesBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        ShtoShitesBtn.setText("➕ Shto Shites");
        ShtoShitesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShtoShitesBtnActionPerformed(evt);
            }
        });

        ModifikoBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        ModifikoBtn.setText("📝 Modifiko");
        ModifikoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifikoBtnActionPerformed(evt);
            }
        });

        FshiBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        FshiBtn.setText("✖ Fshi Shites");
        FshiBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FshiBtnActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jButton1.setText("🚪 Dil");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        ShitesiComboBox.setEditable(true);
        ShitesiComboBox.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        ShitesiComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kerko Shites..." }));
        ShitesiComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShitesiComboBoxActionPerformed(evt);
            }
        });

        RifreskoBtn.setBackground(new java.awt.Color(255, 255, 255));
        RifreskoBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        RifreskoBtn.setText("🔄 Rifresko");
        RifreskoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RifreskoBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 1095, Short.MAX_VALUE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
        			.addGap(25))
        		.addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, 1260, Short.MAX_VALUE)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(ShitesiComboBox, GroupLayout.PREFERRED_SIZE, 575, GroupLayout.PREFERRED_SIZE)
        					.addGap(64)
        					.addComponent(KerkoBtn, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
        					.addGap(72)
        					.addComponent(RifreskoBtn, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE))
        				.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        					.addGroup(layout.createSequentialGroup()
        						.addComponent(ModifikoBtn, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE)
        						.addGap(18)
        						.addComponent(FshiBtn, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
        						.addGap(18)
        						.addComponent(ShtoShitesBtn))
        					.addComponent(jScrollPane1, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 1214, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap(36, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)))
        			.addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        				.addComponent(ShitesiComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        					.addComponent(KerkoBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(RifreskoBtn, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)))
        			.addGap(27)
        			.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 389, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(ShtoShitesBtn, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
        				.addComponent(FshiBtn, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
        				.addComponent(ModifikoBtn, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE))
        			.addGap(45))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>                        

    private void ShtoShitesBtnActionPerformed(java.awt.event.ActionEvent evt) {                                              
      ShtoShites dialog = new ShtoShites(this, true);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
    
    }                                             

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        if (adminPage != null) {
            adminPage.setVisible(true); 
        }
        this.dispose(); 
    }  
                                   

    private void ModifikoBtnActionPerformed(java.awt.event.ActionEvent evt) {                                            

      int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Zgjidh nje rresht per te modifikuar!");
            return;
        }

        int id = (int) jTable1.getValueAt(selectedRow, 0);
        String emri = (String) jTable1.getValueAt(selectedRow, 1);
        String mbiemri = (String) jTable1.getValueAt(selectedRow, 2);
        String nrTel = (String) jTable1.getValueAt(selectedRow, 3);
        String username = (String) jTable1.getValueAt(selectedRow, 4);
        String password = (String) jTable1.getValueAt(selectedRow, 5);
        int arka = (int) jTable1.getValueAt(selectedRow, 6);
        java.sql.Date dataRegj = (java.sql.Date) jTable1.getValueAt(selectedRow, 7);

        Modifiko editDialog = new Modifiko(this, true);
        editDialog.setShitesData(id, emri, mbiemri, nrTel, username, password, arka, dataRegj);
        editDialog.setLocationRelativeTo(this);
        editDialog.setVisible(true);

        loadSellersData();
    }                                           

    private void FshiBtnActionPerformed(java.awt.event.ActionEvent evt) {                                        
  int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Zgjidh nje rresht per te fshire!");
            return;
        }

        int id = (int) jTable1.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "A je i sigurt qe do ta fshish shitesin?",
                "Konfirmim",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String sql = "DELETE FROM shites WHERE ID_Shitesi=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, id);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Shitesi u fshi!");
                loadSellersData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gabim gjate fshirjes: " + e.getMessage());
            }
        }
    }                                       

    private void formWindowOpened(java.awt.event.WindowEvent evt) {                                  
        JTableHeader header = jTable1.getTableHeader();
        header.setFont(new Font("Serif", Font.BOLD, 18));
        header.setBackground(new Color(119, 136, 153)); 
        header.setForeground(Color.WHITE); 
    }  
                                

    private void ShitesiComboBoxActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
    }                                               

    private void KerkoBtnActionPerformed(java.awt.event.ActionEvent evt) {                                         

      String selected = (String) ShitesiComboBox.getEditor().getItem();
kerkoShitesin(selected.trim());
    }                                        

    private void RifreskoBtnActionPerformed(java.awt.event.ActionEvent evt) {                                            
      
    loadSellersData();
    ShitesiComboBox.setSelectedItem(null);
    ShitesiComboBox.getEditor().setItem("");


    }                                           

  
    public static void main(String args[]) {
  
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Shites.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Shites.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Shites.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Shites.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Shites().setVisible(true);
     
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton FshiBtn;
    private javax.swing.JButton KerkoBtn;
    private javax.swing.JButton ModifikoBtn;
    private javax.swing.JButton RifreskoBtn;
    private javax.swing.JComboBox<String> ShitesiComboBox;
    private javax.swing.JButton ShtoShitesBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration                   
}
