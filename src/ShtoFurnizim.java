import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.Color;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPanel;

public class ShtoFurnizim extends javax.swing.JDialog {
    
private javax.swing.event.TableModelListener thisTableModelListener;   
private List<String> titujt = new ArrayList<>();
private List<String> furnitoret = new ArrayList<>();

    public ShtoFurnizim(java.awt.Frame parent, boolean modal) {
    	
        super(parent, modal);
        initComponents();

       
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String currentDate = LocalDate.now().format(formatter);
        dataLbel.setText(currentDate);

        setupAutoCompleteLibra(ComboBoxLibra);
        setupAutoCompleteFurnitore(FurnitorCombobox);

       
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        thisTableModelListener = new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                if (row < 0 || column < 0) return;

                try {
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

                    // Merr vlerat
                    Object cmimiObj = model.getValueAt(row, 3);
                    Object sasiaObj = model.getValueAt(row, 4);
                    Object dyqanObj = model.getValueAt(row, 6);

                    int sasia = 0;
                    double cmimi = 0;

                    if (cmimiObj != null && !cmimiObj.toString().trim().isEmpty()) {
                        cmimi = Double.parseDouble(cmimiObj.toString());
                    }
                    if (sasiaObj != null && !sasiaObj.toString().trim().isEmpty()) {
                        sasia = Integer.parseInt(sasiaObj.toString());
                    }

                    
                    double kosto = cmimi * sasia;
                    Object currentKosto = model.getValueAt(row, 5);
                    if (currentKosto == null || !currentKosto.toString().equals(String.valueOf(kosto))) {
                        model.removeTableModelListener(thisTableModelListener);
                        model.setValueAt(kosto, row, 5); 
                        model.addTableModelListener(thisTableModelListener);
                    }

                   
                    if (dyqanObj != null && !dyqanObj.toString().trim().isEmpty()) {
                        int dyqan = Integer.parseInt(dyqanObj.toString());
                        int magazine = sasia - dyqan;
                        if (magazine < 0) magazine = 0;

                        model.removeTableModelListener(thisTableModelListener);
                        model.setValueAt(magazine, row, 7); 
                        model.addTableModelListener(thisTableModelListener);
                    }

                } catch (NumberFormatException ex) {
                   
                }
            }
        };



        model.addTableModelListener(thisTableModelListener);
    
    }

private void setupAutoCompleteLibra(JComboBox<String> comboBox) {
    titujt.clear();
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT Titulli FROM librat ORDER BY Titulli");
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            titujt.add(rs.getString("Titulli"));
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    comboBox.setEditable(true);
    JTextField tf = (JTextField) comboBox.getEditor().getEditorComponent();

    tf.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String text = tf.getText();
            int caret = tf.getCaretPosition();

            comboBox.hidePopup();
            comboBox.removeAllItems();

            for (String item : titujt) {
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
                        NiptField.setText(nipt);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    });
}
    private void kerkoDhePlotesoTabelen(String titulli) {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); 

    String query = "SELECT ID_Libri, ISBN, Titulli, Cmim_Blerje " +
                   "FROM librat WHERE Titulli LIKE ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {

        ps.setString(1, "%" + titulli + "%");

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] row = new Object[] {
                    rs.getInt("ID_Libri"),    
                    rs.getString("ISBN"),
                    rs.getString("Titulli"),
                    rs.getDouble("Cmim_Blerje"),
                    null,   
                    null,   
                    null,   
                    null 
                };

                model.addRow(row);
            }
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gabim ", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        FurnitorCombobox = new javax.swing.JComboBox<>();
        FurnitotRiBtn = new javax.swing.JButton();
        FurnitotRiBtn.setForeground(new Color(51, 102, 153));
        SearchBtn = new javax.swing.JButton();
        SearchBtn.setForeground(new Color(51, 102, 153));
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        ComboBoxLibra = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel2.setForeground(new Color(51, 102, 153));
        dataLbel = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel3.setForeground(new Color(51, 102, 153));
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel4.setForeground(new Color(51, 102, 153));
        NiptField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        FurnitorCombobox.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        FurnitorCombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kerko..." }));
        FurnitorCombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FurnitorComboboxActionPerformed(evt);
            }
        });

        FurnitotRiBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        FurnitotRiBtn.setText("➕ Shto Furnitor");
        FurnitotRiBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FurnitotRiBtnActionPerformed(evt);
            }
        });

        SearchBtn.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        SearchBtn.setText("🔍");
        SearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchBtnActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kodi", "ISBN", "Titulli", "Cmim Blerje", "Sasia", "Kosto", "Ne dyqan", "Ne magazine"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(45);
        jScrollPane1.setViewportView(jTable1);

        ComboBoxLibra.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        ComboBoxLibra.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Titulli ose ISBN..." }));
        ComboBoxLibra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComboBoxLibraActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel2.setText("Data:");

        jLabel3.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel3.setText("Furnitori:");

        jPanel1.setBackground(new Color(119, 136, 153));

        jLabel1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Furnizim i ri");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel4.setText("NIPT:");
        FshiRreshtBtn = new javax.swing.JButton();
        FshiRreshtBtn.setForeground(new Color(51, 102, 153));
        
                FshiRreshtBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
                FshiRreshtBtn.setText("🗑Fshi Rreshtin ");
                FshiRreshtBtn.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        FshiRreshtBtnActionPerformed(evt);
                    }
                });
        PastroBtn = new javax.swing.JButton();
        PastroBtn.setForeground(new Color(51, 102, 153));
        
                PastroBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
                PastroBtn.setText("❌ Pastro ");
                PastroBtn.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        PastroBtnActionPerformed(evt);
                    }
                });
        RuajBtn = new javax.swing.JButton();
        RuajBtn.setForeground(new Color(51, 102, 153));
        
                RuajBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
                RuajBtn.setText("💾 Ruaj");
                RuajBtn.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        RuajBtnActionPerformed(evt);
                    }
                });
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(119, 136, 153));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(ComboBoxLibra, GroupLayout.PREFERRED_SIZE, 874, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addComponent(SearchBtn)
        					.addContainerGap())
        				.addGroup(layout.createSequentialGroup()
        					.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(jLabel4)
        							.addGap(54)
        							.addComponent(NiptField, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE))
        						.addGroup(layout.createSequentialGroup()
        							.addGroup(layout.createParallelGroup(Alignment.LEADING)
        								.addComponent(jLabel2)
        								.addComponent(jLabel3))
        							.addGap(28)
        							.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        								.addComponent(FurnitorCombobox, 0, 182, Short.MAX_VALUE)
        								.addComponent(dataLbel))))
        					.addPreferredGap(ComponentPlacement.RELATED, 726, Short.MAX_VALUE)
        					.addComponent(FurnitotRiBtn)
        					.addGap(29))))
        		.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 1217, Short.MAX_VALUE)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap(766, Short.MAX_VALUE)
        			.addComponent(FshiRreshtBtn)
        			.addGap(18)
        			.addComponent(PastroBtn, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(RuajBtn, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
        			.addGap(22))
        		.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 1217, Short.MAX_VALUE)
        		.addComponent(panel, GroupLayout.DEFAULT_SIZE, 1217, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel2)
        						.addComponent(dataLbel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jLabel3)
        						.addComponent(FurnitorCombobox, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(NiptField, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jLabel4))
        					.addGap(28)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(ComboBoxLibra, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
        						.addComponent(SearchBtn, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)))
        				.addComponent(FurnitotRiBtn, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE))
        			.addGap(18)
        			.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 286, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(FshiRreshtBtn, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
        				.addComponent(PastroBtn, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
        				.addComponent(RuajBtn, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(panel, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
        			.addGap(16))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>                        

    private void FurnitorComboboxActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
    }                                                

                             

    private void FurnitotRiBtnActionPerformed(java.awt.event.ActionEvent evt) {                                              
    ShtoFurnitore dialog = new ShtoFurnitore((java.awt.Frame) this.getParent(), true);
    dialog.setLocationRelativeTo(this);
dialog.setVisible(true);

    }                                             

    private void FshiRreshtBtnActionPerformed(java.awt.event.ActionEvent evt) {                                              
       int selectedRow = jTable1.getSelectedRow();
if (selectedRow != -1) {
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.removeRow(selectedRow);
} else {
    JOptionPane.showMessageDialog(this, "Zgjidh nje rresht per te fshire!", "Info", JOptionPane.INFORMATION_MESSAGE);
}
    }                                             

    private void RuajBtnActionPerformed(java.awt.event.ActionEvent evt) {                                         
        String furnitor = (String) FurnitorCombobox.getSelectedItem();
        String nipt = NiptField.getText();
        String data = dataLbel.getText(); 

        if (furnitor == null || furnitor.isEmpty() || jTable1.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Ploteso furnitorin dhe shto te pakten nje liber.", "Gabim", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); 

          
            String sqlFurnizim = "INSERT INTO furnizimi (Furnitori_ID, Data_Furnizimit) VALUES ((SELECT ID_Furnitori FROM furnitoret WHERE Emri_Biznesit=?), ?)";
            try (PreparedStatement psF = conn.prepareStatement(sqlFurnizim, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psF.setString(1, furnitor);
                java.sql.Date sqlDate = new java.sql.Date(new java.text.SimpleDateFormat("dd/MM/yyyy").parse(data).getTime());
                psF.setDate(2, sqlDate);
                psF.executeUpdate();

                ResultSet rsKeys = psF.getGeneratedKeys();
                int idFurnizim = 0;
                if (rsKeys.next()) {
                    idFurnizim = rsKeys.getInt(1);
                }

              
                String sqlLibrat = "INSERT INTO furnizimi_librat (ID_Furnizimi, ID_Libri, Sasia, Cmim_Blerje) VALUES (?, ?, ?, ?)";
                try (PreparedStatement psL = conn.prepareStatement(sqlLibrat)) {
                    for (int i = 0; i < jTable1.getRowCount(); i++) {
                        int idLibri = Integer.parseInt(jTable1.getValueAt(i, 0).toString());
                        int sasia = Integer.parseInt(jTable1.getValueAt(i, 4) != null ? jTable1.getValueAt(i, 4).toString() : "0");
                        double cmim = Double.parseDouble(jTable1.getValueAt(i, 3).toString());

                        psL.setInt(1, idFurnizim);
                        psL.setInt(2, idLibri);
                        psL.setInt(3, sasia);
                        psL.setDouble(4, cmim);
                        psL.addBatch();
                    }
                    psL.executeBatch();
                }

               
                for (int i = 0; i < jTable1.getRowCount(); i++) {
                    int idLibri = Integer.parseInt(jTable1.getValueAt(i, 0).toString());

                   
                    int sasiaDyqan = 0;
                    int sasiaMagazine = 0;

                    Object dyqanObj = jTable1.getValueAt(i, 6);
                    Object magObj = jTable1.getValueAt(i, 7);

                    if (dyqanObj != null && !dyqanObj.toString().trim().isEmpty()) {
                        try {
                            sasiaDyqan = Integer.parseInt(dyqanObj.toString());
                        } catch(NumberFormatException e) {
                            JOptionPane.showMessageDialog(this, "Vlera 'Ne dyqan' duhet te jete numer per rreshtin " + (i+1));
                            conn.rollback();
                            return;
                        }
                    }

                    if (magObj != null && !magObj.toString().trim().isEmpty()) {
                        try {
                            sasiaMagazine = Integer.parseInt(magObj.toString());
                        } catch(NumberFormatException e) {
                            JOptionPane.showMessageDialog(this, "Vlera 'Ne magazine' duhet te jete numer per rreshtin " + (i+1));
                            conn.rollback();
                            return;
                        }
                    }

                    int sasiaTotal = Integer.parseInt(jTable1.getValueAt(i, 4).toString());
                    if (sasiaDyqan + sasiaMagazine != sasiaTotal) {
                        JOptionPane.showMessageDialog(this, "Sasia totale duhet te jete e barabarte me shumën e 'Ne dyqan' dhe 'Ne magazine' per rreshtin " + (i+1));
                        conn.rollback();
                        return;
                    }

                
                    String updateLibrat = "UPDATE librat SET Gjendje_Dyqan = Gjendje_Dyqan + ?, Gjendje_Magazine = Gjendje_Magazine + ? WHERE ID_Libri = ?";
                    try (PreparedStatement psUpdate = conn.prepareStatement(updateLibrat)) {
                        psUpdate.setInt(1, sasiaDyqan);
                        psUpdate.setInt(2, sasiaMagazine);
                        psUpdate.setInt(3, idLibri);
                        psUpdate.executeUpdate();
                    }
                }

            }

            conn.commit(); 
            JOptionPane.showMessageDialog(this, "Furnizimi u ruajt me sukses!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); 

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gabim gjate ruajtjes: " + ex.getMessage(), "Gabim", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void PastroBtnActionPerformed(java.awt.event.ActionEvent evt) {                                          
      DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
model.setRowCount(0);
    }                                         

    private void ComboBoxLibraActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
    }                                             

    private void SearchBtnActionPerformed(java.awt.event.ActionEvent evt) {                                          
        String input = ComboBoxLibra.getEditor().getItem().toString();

        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ju lutem shkruani titullin ose ISBN!", "Kujdes", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        
        String query = "SELECT ID_Libri, ISBN, Titulli, Cmim_Blerje " +
                       "FROM librat WHERE Titulli LIKE ? OR ISBN LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, "%" + input + "%");
            ps.setString(2, "%" + input + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idLibri = rs.getInt("ID_Libri");

                   
                    boolean ekziston = false;
                    for (int i = 0; i < model.getRowCount(); i++) {
                        if (model.getValueAt(i, 0).equals(idLibri)) {
                            ekziston = true;
                            break;
                        }
                    }

                    if (!ekziston) {
                        Object[] row = new Object[]{
                            idLibri,
                            rs.getString("ISBN"),
                            rs.getString("Titulli"),
                            rs.getDouble("Cmim_Blerje"),
                            null, 
                            null, 
                            null, 
                            null
                        };
                        model.addRow(row);
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gabim gjate kerkimit!", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
            java.util.logging.Logger.getLogger(ShtoFurnizim.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShtoFurnizim.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShtoFurnizim.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShtoFurnizim.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ShtoFurnizim dialog = new ShtoFurnizim(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> ComboBoxLibra;
    private javax.swing.JButton FshiRreshtBtn;
    private javax.swing.JComboBox<String> FurnitorCombobox;
    private javax.swing.JButton FurnitotRiBtn;
    private javax.swing.JTextField NiptField;
    private javax.swing.JButton PastroBtn;
    private javax.swing.JButton RuajBtn;
    private javax.swing.JButton SearchBtn;
    private javax.swing.JTextField dataLbel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
}