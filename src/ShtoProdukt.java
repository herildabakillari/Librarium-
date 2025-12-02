import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;



public class ShtoProdukt extends  javax.swing.JDialog {

   private List<String> furnitoret = new ArrayList<>();
   
   
    public ShtoProdukt() {
        initComponents();
        setupAutoCompleteFurnitore(FurnitoriCombo);
    }

    private void setupAutoCompleteFurnitore(JComboBox<String> comboBox) {
    furnitoret.clear();
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT Emri_Biznesit FROM furnitoret ORDER BY Emri_Biznesit");
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            furnitoret.add(rs.getString("Emri_Biznesit"));
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

            for (String item : furnitoret) {
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

    comboBox.addActionListener(e -> {
        String selectedFurnitor = (String) comboBox.getSelectedItem();
        if (selectedFurnitor != null && !selectedFurnitor.trim().isEmpty()) {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT NIPT FROM furnitoret WHERE Emri_Biznesit = ? LIMIT 1")) {
                ps.setString(1, selectedFurnitor);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String nipt = rs.getString("NIPT");
                        KodiFurnitoritField.setText(nipt);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    });
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel1.setForeground(new Color(51, 102, 153));
        TitulliLabel = new javax.swing.JLabel();
        TitulliLabel.setForeground(new Color(51, 102, 153));
        AutoriLabel = new javax.swing.JLabel();
        AutoriLabel.setForeground(new Color(51, 102, 153));
        isbnLabel = new javax.swing.JLabel();
        isbnLabel.setForeground(new Color(51, 102, 153));
        KategoriaLabel = new javax.swing.JLabel();
        KategoriaLabel.setForeground(new Color(51, 102, 153));
        VendndodhjaLabel = new javax.swing.JLabel();
        VendndodhjaLabel.setForeground(new Color(51, 102, 153));
        TitulliField = new javax.swing.JTextField();
        AutoriField = new javax.swing.JTextField();
        PershkrimiField = new javax.swing.JTextField();
        isbnField = new javax.swing.JTextField();
        KategoriaField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel8.setForeground(new Color(51, 102, 153));
        FurnitoriLabel = new javax.swing.JLabel();
        FurnitoriLabel.setForeground(new Color(51, 102, 153));
        FurnitoriCombo = new javax.swing.JComboBox<>();
        NiptFurnitoriLabel = new javax.swing.JLabel();
        NiptFurnitoriLabel.setForeground(new Color(51, 102, 153));
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        KodiFurnitoritField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel14.setForeground(new Color(51, 102, 153));
        CmimLabel = new javax.swing.JLabel();
        CmimLabel.setForeground(new Color(51, 102, 153));
        CmimShitjeLabel = new javax.swing.JLabel();
        CmimShitjeLabel.setForeground(new Color(51, 102, 153));
        CmimShumiceLabel = new javax.swing.JLabel();
        CmimShumiceLabel.setForeground(new Color(51, 102, 153));
        CmimField = new javax.swing.JTextField();
        CmimBlerjeField = new javax.swing.JTextField();
        CmimShumiceField = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        PershkrimiLabel = new javax.swing.JLabel();
        PershkrimiLabel.setForeground(new Color(51, 102, 153));
        VendndodhjaField = new javax.swing.JTextField();
        RuajBtn = new javax.swing.JButton();
        RuajBtn.setForeground(new Color(51, 102, 153));
        AnulloBtn = new javax.swing.JButton();
        AnulloBtn.setForeground(new Color(51, 102, 153));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel1.setText("Te Dhena Libri");

        TitulliLabel.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N
        TitulliLabel.setText("Titulli:");

        AutoriLabel.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N
        AutoriLabel.setText("Autori:");

        isbnLabel.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N
        isbnLabel.setText("ISBN:");

        KategoriaLabel.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N
        KategoriaLabel.setText("Kategoria:");

        VendndodhjaLabel.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N
        VendndodhjaLabel.setText("Vendndodhja:");

        TitulliField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        TitulliField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TitulliFieldActionPerformed(evt);
            }
        });

        AutoriField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        AutoriField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AutoriFieldActionPerformed(evt);
            }
        });

        PershkrimiField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        PershkrimiField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PershkrimiFieldActionPerformed(evt);
            }
        });

        isbnField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        isbnField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isbnFieldActionPerformed(evt);
            }
        });

        KategoriaField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        KategoriaField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KategoriaFieldActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel8.setText("Te Dhena Furnitori");

        FurnitoriLabel.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N
        FurnitoriLabel.setText("Furnitori:");

        FurnitoriCombo.setEditable(true);
        FurnitoriCombo.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        FurnitoriCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kerko Furnitor..." }));
        FurnitoriCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FurnitoriComboActionPerformed(evt);
            }
        });

        NiptFurnitoriLabel.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N
        NiptFurnitoriLabel.setText("NIPT:");

        KodiFurnitoritField.setEditable(false);
        KodiFurnitoritField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        KodiFurnitoritField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KodiFurnitoritFieldActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jLabel14.setText("Financiare");

        CmimLabel.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N
        CmimLabel.setText("Cmim Shitje:");

        CmimShitjeLabel.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N
        CmimShitjeLabel.setText("Kosto Blerje:");

        CmimShumiceLabel.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N
        CmimShumiceLabel.setText("Cmim Shumice:");

        CmimField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        CmimBlerjeField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        CmimBlerjeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CmimBlerjeFieldActionPerformed(evt);
            }
        });

        CmimShumiceField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        jComboBox2.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Leke", "Euro" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jComboBox3.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Leke", "Euro" }));

        jComboBox4.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Leke", "Euro" }));

        jPanel1.setBackground(new Color(119, 136, 153));

        jLabel18.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Artikull i Ri ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PershkrimiLabel.setFont(new java.awt.Font("Serif", 0, 20)); // NOI18N
        PershkrimiLabel.setText("Pershkrimi:");

        VendndodhjaField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        VendndodhjaField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VendndodhjaFieldActionPerformed(evt);
            }
        });

        RuajBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        RuajBtn.setText("💾 Ruaj");
        RuajBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuajBtnActionPerformed(evt);
            }
        });

        AnulloBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        AnulloBtn.setText("❌ Anullo");
        AnulloBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnulloBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 1120, Short.MAX_VALUE)
        		.addGroup(layout.createSequentialGroup()
        			.addGap(61)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
        							.addComponent(jLabel1)
        							.addGroup(layout.createSequentialGroup()
        								.addGroup(layout.createParallelGroup(Alignment.LEADING)
        									.addComponent(TitulliLabel)
        									.addComponent(AutoriLabel))
        								.addGap(78)
        								.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        									.addComponent(AutoriField, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
        									.addComponent(TitulliField)))
        							.addGroup(layout.createSequentialGroup()
        								.addGroup(layout.createParallelGroup(Alignment.LEADING)
        									.addComponent(KategoriaLabel)
        									.addComponent(isbnLabel)
        									.addComponent(PershkrimiLabel))
        								.addPreferredGap(ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
        								.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        									.addComponent(PershkrimiField, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE)
        									.addComponent(KategoriaField, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE)
        									.addComponent(isbnField, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE)
        									.addComponent(VendndodhjaField, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE))))
        						.addComponent(VendndodhjaLabel))
        					.addPreferredGap(ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jLabel14)
        						.addGroup(layout.createSequentialGroup()
        							.addGroup(layout.createParallelGroup(Alignment.LEADING)
        								.addComponent(CmimLabel)
        								.addComponent(CmimShitjeLabel)
        								.addComponent(CmimShumiceLabel))
        							.addGap(30)
        							.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        								.addComponent(CmimField)
        								.addComponent(CmimBlerjeField)
        								.addComponent(CmimShumiceField, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE))
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        								.addComponent(jComboBox2, 0, 75, Short.MAX_VALUE)
        								.addComponent(jComboBox3, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        								.addComponent(jComboBox4, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        						.addComponent(jLabel8)
        						.addGroup(layout.createSequentialGroup()
        							.addGroup(layout.createParallelGroup(Alignment.LEADING)
        								.addComponent(FurnitoriLabel)
        								.addComponent(NiptFurnitoriLabel))
        							.addGap(83)
        							.addGroup(layout.createParallelGroup(Alignment.LEADING)
        								.addComponent(KodiFurnitoritField, 235, 235, 235)
        								.addComponent(FurnitoriCombo, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE))))
        					.addGap(182))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(461)
        					.addComponent(jLabel11)
        					.addGap(0)
        					.addComponent(jLabel13)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jLabel12, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE))))
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap(606, Short.MAX_VALUE)
        			.addComponent(AnulloBtn, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
        			.addGap(41)
        			.addComponent(RuajBtn, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
        			.addGap(165))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGap(60)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jLabel1)
        				.addComponent(jLabel14))
        			.addGap(28)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(CmimField, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jComboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(CmimLabel))
        					.addGap(15)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(CmimBlerjeField, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jComboBox3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(CmimShitjeLabel))
        					.addGap(15)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(CmimShumiceLabel)
        						.addComponent(CmimShumiceField, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jComboBox4, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
        					.addGap(47)
        					.addComponent(jLabel8)
        					.addGap(18)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(FurnitoriLabel)
        						.addComponent(FurnitoriCombo, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
        					.addGap(10)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(NiptFurnitoriLabel)
        						.addComponent(KodiFurnitoritField, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(5)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(TitulliLabel)
        						.addComponent(TitulliField, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
        					.addGap(20)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(AutoriLabel)
        						.addComponent(AutoriField, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
        					.addGap(20)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(isbnField, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
        						.addComponent(isbnLabel))
        					.addGap(20)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(KategoriaField, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        						.addComponent(KategoriaLabel))
        					.addGap(20)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(PershkrimiField, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
        						.addComponent(PershkrimiLabel))
        					.addGap(20)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(VendndodhjaLabel)
        						.addComponent(VendndodhjaField, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))))
        			.addGap(53)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(AnulloBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(RuajBtn))
        			.addGap(141)
        			.addComponent(jLabel13)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jLabel11)
        				.addComponent(jLabel12))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>                        

    private void TitulliFieldActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }                                            

    private void AutoriFieldActionPerformed(java.awt.event.ActionEvent evt) {                                            
        // TODO add your handling code here:
    }                                           

    private void PershkrimiFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
    }                                               

    private void isbnFieldActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                         

    private void KategoriaFieldActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // TODO add your handling code here:
    }                                              

    private void FurnitoriComboActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // TODO add your handling code here:
    }                                              

    private void CmimBlerjeFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
    }                                               

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void VendndodhjaFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
    }                                                

    private void AnulloBtnActionPerformed(java.awt.event.ActionEvent evt) {                                          
        dispose();
    }                                         

    private void RuajBtnActionPerformed(java.awt.event.ActionEvent evt) {                                        

    String isbn = isbnField.getText().trim();
    String titulli = TitulliField.getText().trim();
    String autori = AutoriField.getText().trim();
    String kategoria = KategoriaField.getText().trim();
    String pershkrimi = PershkrimiField.getText().trim();
    String vendndodhja = VendndodhjaField.getText().trim();
    String cmimBlerjeStr = CmimBlerjeField.getText().trim();
    String cmimShitjeStr = CmimField.getText().trim();
    String cmimShumiceStr = CmimShumiceField.getText().trim();
    String gjendjeDyqan = "0"; 
    String gjendjeMagazine = "0"; 
    String furnitori = (String) FurnitoriCombo.getSelectedItem();

   
    if (titulli.isEmpty() || autori.isEmpty() || furnitori == null || furnitori.trim().isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Ju lutem plotesoni fushat e detyrueshme: Titulli, Autori, Furnitori.");
        return;
    }

    try {
        double cmimBlerje = Double.parseDouble(cmimBlerjeStr);
        double cmimShitje = Double.parseDouble(cmimShitjeStr);
        double cmimShumice = Double.parseDouble(cmimShumiceStr);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO librat (ISBN, Titulli, Autori, Kategoria, Pershkrimi, Vendndodhja_raft, " +
                 "Cmim_Blerje, Cmim_Shitje, Cmim_Shumice, Gjendje_Dyqan, Gjendje_Magazine, Furnitori) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            ps.setString(1, isbn);
            ps.setString(2, titulli);
            ps.setString(3, autori);
            ps.setString(4, kategoria);
            ps.setString(5, pershkrimi);
            ps.setString(6, vendndodhja);
            ps.setDouble(7, cmimBlerje);
            ps.setDouble(8, cmimShitje);
            ps.setDouble(9, cmimShumice);
            ps.setInt(10, Integer.parseInt(gjendjeDyqan));    
            ps.setInt(11, Integer.parseInt(gjendjeMagazine)); 
            ps.setString(12, furnitori);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Libri u ruajt me sukses!");
                clearFormFields();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Gabim gjate ruajtjes se librit.");
            }
        }
    } catch (NumberFormatException nfe) {
        javax.swing.JOptionPane.showMessageDialog(this, "Vlera e cmimit duhet te jete numer.");
    } catch (SQLException ex) {
        ex.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Gabim në bazën e të dhënave: " + ex.getMessage());
    }
}

private void clearFormFields() {
    isbnField.setText("");
    TitulliField.setText("");
    AutoriField.setText("");
    KategoriaField.setText("");
    PershkrimiField.setText("");
    VendndodhjaField.setText("");
    CmimBlerjeField.setText("");
    CmimField.setText("");
    CmimShumiceField.setText("");
    FurnitoriCombo.setSelectedIndex(-1);
    KodiFurnitoritField.setText("");

    }                                       

    private void KodiFurnitoritFieldActionPerformed(java.awt.event.ActionEvent evt) {                                                    
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
            java.util.logging.Logger.getLogger(ShtoProdukt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShtoProdukt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShtoProdukt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShtoProdukt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ShtoProdukt().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton AnulloBtn;
    private javax.swing.JTextField AutoriField;
    private javax.swing.JLabel AutoriLabel;
    private javax.swing.JTextField CmimBlerjeField;
    private javax.swing.JTextField CmimField;
    private javax.swing.JLabel CmimLabel;
    private javax.swing.JLabel CmimShitjeLabel;
    private javax.swing.JTextField CmimShumiceField;
    private javax.swing.JLabel CmimShumiceLabel;
    private javax.swing.JComboBox<String> FurnitoriCombo;
    private javax.swing.JLabel FurnitoriLabel;
    private javax.swing.JTextField KategoriaField;
    private javax.swing.JLabel KategoriaLabel;
    private javax.swing.JTextField KodiFurnitoritField;
    private javax.swing.JLabel NiptFurnitoriLabel;
    private javax.swing.JTextField PershkrimiField;
    private javax.swing.JLabel PershkrimiLabel;
    private javax.swing.JButton RuajBtn;
    private javax.swing.JTextField TitulliField;
    private javax.swing.JLabel TitulliLabel;
    private javax.swing.JTextField VendndodhjaField;
    private javax.swing.JLabel VendndodhjaLabel;
    private javax.swing.JTextField isbnField;
    private javax.swing.JLabel isbnLabel;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration                   
}