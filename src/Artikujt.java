import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;

public class Artikujt extends javax.swing.JFrame {

    private final List<String> fullSuggestions = new ArrayList<>();
    private Iventari parentFrame;

   
    public Artikujt(Iventari parentFrame) {
        this.parentFrame = parentFrame;
        initComponents();
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

       
        loadFullSuggestions("emri");
        enableAutoSuggest(SearchComboBox);

        TypeComboBox.addActionListener(e -> {
            String selectedType = ((String) TypeComboBox.getSelectedItem()).toLowerCase();
            loadFullSuggestions(selectedType);
            ((JTextField) SearchComboBox.getEditor().getEditorComponent()).setText("Kerko");
        });

        TypeComboBox.addActionListener(e -> loadDataIntoTable());

        loadDataIntoTable();
        addCloseListener(); 
    }

    private void addCloseListener() {
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (parentFrame != null) {
                    parentFrame.setVisible(true); 
                }
                dispose();
            }
        });
    }



    



     private Vector<String[]> merrSugjerimeNgaDB(String lloj) {
        Vector<String[]> res = new Vector<>();
        String sql = "";
        switch (lloj.toLowerCase()) {
            case "emri":
                sql = "SELECT Titulli FROM librat";
                break;
            case "isbn":
                sql = "SELECT ISBN, Titulli FROM librat";
                break;
            case "cmimi":
                sql = "SELECT DISTINCT Cmim_Shitje FROM librat";
                break;
            case "kosto":
                sql = "SELECT DISTINCT Cmim_Blerje FROM librat";
                break;
            case "id":
                sql = "SELECT ID_Libri, Titulli FROM librat";
                break;
            case "furnitori":
                sql = "SELECT Furnitori, Titulli FROM librat";
                break;
            default:
                return res;
        }

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (lloj.equalsIgnoreCase("isbn") || lloj.equalsIgnoreCase("id")) {
                    res.add(new String[]{rs.getString(1), rs.getString(2)});
                } else {
                    res.add(new String[]{rs.getString(1)});
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return res;
    }

    private void loadFullSuggestions(String tipi) {
        fullSuggestions.clear();
        for (String[] p : merrSugjerimeNgaDB(tipi)) {
            if (tipi.equalsIgnoreCase("isbn") || tipi.equalsIgnoreCase("id")) {
                fullSuggestions.add(p[0] + " - " + p[1]);
            } else {
                fullSuggestions.add(p[0]);
            }
        }
    }

    private void enableAutoSuggest(JComboBox<String> comboBox) {
        comboBox.setEditable(true);
        JTextField tf = (JTextField) comboBox.getEditor().getEditorComponent();

        tf.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String text = tf.getText();
                int caret = tf.getCaretPosition();

                comboBox.hidePopup();
                comboBox.removeAllItems();

                for (String item : fullSuggestions) {
                    if (item.toLowerCase().contains(text.toLowerCase())) {
                        comboBox.addItem(item);
                    }
                }

                tf.setText(text);
                tf.setCaretPosition(Math.min(caret, tf.getText().length()));

                if (comboBox.getItemCount() > 0) {
                    comboBox.showPopup();
                }
            }
        });
    }

    private void loadDataIntoTable() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        String sql = "SELECT ID_Libri, ISBN, Titulli, Cmim_Shitje, Cmim_Blerje, Cmim_Shumice, "
                   + "Gjendje_Dyqan, Gjendje_Magazine, Kategoria, Pershkrimi, Vendndodhja_raft, Furnitori "
                   + "FROM librat";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] rowData = {
                    rs.getInt("ID_Libri"),
                    rs.getString("ISBN"),
                    rs.getString("Titulli"),
                    rs.getDouble("Cmim_Shitje"),
                    rs.getDouble("Cmim_Blerje"),
                    rs.getString("Furnitori"),           
                    rs.getString("Vendndodhja_raft")      
                };
                model.addRow(rowData);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gabim gjate ngarkimit te te dhenave: " + e.getMessage(), "Gabim", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void ModifikoBtnActionPerformed1(java.awt.event.ActionEvent evt) {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Ju lutem zgjidhni nje liber per modifikim.");
            return;
        }

        int idLibri = (int) jTable1.getValueAt(selectedRow, 0);
        String sql = "SELECT * FROM librat WHERE ID_Libri = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idLibri);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String isbn = rs.getString("ISBN");
                    String titulli = rs.getString("Titulli");
                    String autori = rs.getString("Autori");
                    String kategoria = rs.getString("Kategoria");
                    String pershkrimi = rs.getString("Pershkrimi");
                    String vendndodhja = rs.getString("Vendndodhja_raft");
                    double cmimBlerje = rs.getDouble("Cmim_Blerje");
                    double cmimShitje = rs.getDouble("Cmim_Shitje");
                    double cmimShumice = rs.getDouble("Cmim_Shumice");
                    int gjendjeDyqan = rs.getInt("Gjendje_Dyqan");
                    int gjendjeMagazine = rs.getInt("Gjendje_Magazine");
                    String furnitori = rs.getString("Furnitori");

                    String niptFurnitori = "";
                    String sqlNipt = "SELECT NIPT FROM furnitoret WHERE Emri_Biznesit = ?";
                    try (PreparedStatement psNipt = conn.prepareStatement(sqlNipt)) {
                        psNipt.setString(1, furnitori);
                        try (ResultSet rsNipt = psNipt.executeQuery()) {
                            if (rsNipt.next()) {
                                niptFurnitori = rsNipt.getString("NIPT");
                            }
                        }
                    }

                    ModifikoLiber modifikimDialog = new ModifikoLiber(this, true);
                    modifikimDialog.setLiberData(
                        idLibri, isbn, titulli, autori, kategoria, pershkrimi, vendndodhja,
                        cmimBlerje, cmimShitje, cmimShumice,
                        gjendjeDyqan, gjendjeMagazine,
                        furnitori, niptFurnitori
                    );

                    modifikimDialog.setLocationRelativeTo(this);
                    modifikimDialog.setVisible(true);

                    loadDataIntoTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Nuk u gjet libri me ID: " + idLibri);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gabim: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
                            
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        ArtikullRiBtn = new javax.swing.JButton();
        ArtikullRiBtn.setForeground(new Color(51, 102, 153));
        ModifikoBtn = new javax.swing.JButton();
        ModifikoBtn.setForeground(new Color(51, 102, 153));
        FshiBtn1 = new javax.swing.JButton();
        FshiBtn1.setForeground(new Color(51, 102, 153));
        SearchComboBox = new javax.swing.JComboBox<>();
        TypeComboBox = new javax.swing.JComboBox<>();
        KerkoBtn2 = new javax.swing.JButton();
        KerkoBtn2.setForeground(new Color(51, 102, 153));
        RifreskoBtn1 = new javax.swing.JButton();
        RifreskoBtn1.setForeground(new Color(51, 102, 153));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addCloseListener(); 
        jPanel1.setBackground(new Color(119, 136, 153));

        jLabel1.setFont(new java.awt.Font("Serif", 1, 18)); 
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Artikujt");

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
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jTable1.setFont(new java.awt.Font("Serif", 0, 18)); 
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "ISBN", "Titulli", "Cmimi", "Kosto", "Furnitori"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(35);
        jScrollPane1.setViewportView(jTable1);

        ArtikullRiBtn.setFont(new java.awt.Font("Serif", 1, 18)); 
        ArtikullRiBtn.setText("➕ Shto ");
        ArtikullRiBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ArtikullRiBtnActionPerformed(evt);
            }
        });

        ModifikoBtn.setFont(new java.awt.Font("Serif", 1, 18)); 
        ModifikoBtn.setText(" 🖉 Modifiko ");
        ModifikoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifikoBtnActionPerformed1(evt);
            }
        });

        FshiBtn1.setFont(new java.awt.Font("Serif", 1, 18));
        FshiBtn1.setText(" ❌ Fshi ");
        FshiBtn1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                FshiBtn1MouseClicked(evt);
            }
        });
        FshiBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FshiBtn1ActionPerformed(evt);
            }
        });

        SearchComboBox.setEditable(true);
        SearchComboBox.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        SearchComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kerko.." }));

        TypeComboBox.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        TypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Emri", "ISBN", "ID", "Cmimi", "Kosto", "Furnitori" }));

        KerkoBtn2.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        KerkoBtn2.setText("🔍 Kerko");
        KerkoBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KerkoBtn2ActionPerformed(evt);
            }
        });

        RifreskoBtn1.setBackground(new java.awt.Color(255, 255, 255));
        RifreskoBtn1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        RifreskoBtn1.setText("🔄 Rifresko");
        RifreskoBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RifreskoBtn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(FshiBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(ModifikoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(ArtikullRiBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SearchComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 740, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(KerkoBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(RifreskoBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(65, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(SearchComboBox)
                    .addComponent(TypeComboBox)
                    .addComponent(KerkoBtn2, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(RifreskoBtn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ModifikoBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(FshiBtn1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ArtikullRiBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }                       

    private void FshiBtn1MouseClicked(java.awt.event.MouseEvent evt) {                                      
        
    }                                     

    private void FshiBtn1ActionPerformed(java.awt.event.ActionEvent evt) {                                         

    int selectedRow = jTable1.getSelectedRow();

    int idLibri = (int) jTable1.getValueAt(selectedRow, 0);

    int confirm = javax.swing.JOptionPane.showConfirmDialog(this, 
                    "Deshiron ta fshish kete Liber?", 
                    "Konfirmo Fshirjen", javax.swing.JOptionPane.YES_NO_OPTION);

    if (confirm != javax.swing.JOptionPane.YES_OPTION) {
        return; 
    }

    String sql = "DELETE FROM librat WHERE ID_Libri = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idLibri);
        ps.executeUpdate();

        
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.removeRow(selectedRow);

        javax.swing.JOptionPane.showMessageDialog(this, "Libri u fshi me sukses.");

    } catch (SQLException ex) {
    
}
       
    }                                        

    private void ArtikullRiBtnActionPerformed(java.awt.event.ActionEvent evt) {                                              
    ShtoProdukt shtoForm = new ShtoProdukt(); 
    shtoForm.setLocationRelativeTo(this); 
    shtoForm.setVisible(true);
    }                                             

    private void ModifikoBtnActionPerformed(java.awt.event.ActionEvent evt) {                                            
   
    int selectedRow = jTable1.getSelectedRow();
    
    int idLibri = (int) jTable1.getValueAt(selectedRow, 0);
    String sql = "SELECT * FROM librat WHERE ID_Libri = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idLibri);

        try (java.sql.ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String isbn = rs.getString("ISBN");
                String titulli = rs.getString("Titulli");
                String autori = rs.getString("Autori");
                String kategoria = rs.getString("Kategoria");
                String pershkrimi = rs.getString("Pershkrimi");
                String vendndodhja = rs.getString("Vendndodhja_raft");
                double cmimBlerje = rs.getDouble("Cmim_Blerje");
                double cmimShitje = rs.getDouble("Cmim_Shitje");
                double cmimShumice = rs.getDouble("Cmim_Shumice");
                int gjendjeDyqan = rs.getInt("Gjendje_Dyqan");
                int gjendjeMagazine = rs.getInt("Gjendje_Magazine");
                String furnitori = rs.getString("Furnitori");

                String niptFurnitori = "";
                String sqlNipt = "SELECT NIPT FROM furnitoret WHERE Emri_Biznesit = ?";
                try (PreparedStatement psNipt = conn.prepareStatement(sqlNipt)) {
                    psNipt.setString(1, furnitori);
                    try (java.sql.ResultSet rsNipt = psNipt.executeQuery()) {
                        if (rsNipt.next()) {
                            niptFurnitori = rsNipt.getString("NIPT");
                        }
                    }
                }

                
                ModifikoLiber modifikimDialog = new ModifikoLiber(this, true);

                modifikimDialog.setLiberData(
                    idLibri, isbn, titulli, autori, kategoria, pershkrimi, vendndodhja,
                    cmimBlerje, cmimShitje, cmimShumice,
                    gjendjeDyqan, gjendjeMagazine,
                    furnitori, niptFurnitori
                );

         
                modifikimDialog.setLocationRelativeTo(this);
                modifikimDialog.setVisible(true);

                loadDataIntoTable();
            } else {
                JOptionPane.showMessageDialog(this, "Nuk u gjet libri me ID: " + idLibri);
            }
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gabim" + e.getMessage());
        e.printStackTrace();
    }
    }                                           

    private void KerkoBtn2ActionPerformed(java.awt.event.ActionEvent evt) {                                          
     String tipi = (String) TypeComboBox.getSelectedItem();
    String kerkimi = ((JTextField) SearchComboBox.getEditor().getEditorComponent()).getText().trim();
    DefaultTableModel model = (DefaultTableModel)jTable1.getModel();
    model.setRowCount(0);

    
    if (kerkimi.contains(" - ")) {
        kerkimi = kerkimi.split(" - ")[0].trim();
    }

    String sql = "SELECT l.ID_Libri, l.ISBN, l.Titulli, l.Cmim_Shitje, l.Cmim_Blerje, l.Furnitori, "
            + "lp.Cmim_Promocion "
            + "FROM librat l LEFT JOIN librat_promocion lp ON l.ID_Libri = lp.ID_Libri "
            + "WHERE ";

    switch (tipi.toLowerCase()) {
        case "emri":
            sql += "l.Titulli LIKE ?";
            kerkimi = "%" + kerkimi + "%";
            break;
        case "isbn":
            sql += "l.ISBN = ?";
            break;
        case "id":
            sql += "l.ID_Libri = ?";
            try {
                Integer.parseInt(kerkimi);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID duhet te jete numer.");
                return;
            }
            break;
        case "cmimi":
            sql += "l.Cmim_Shitje = ?";
            break;
        case "kosto":
            sql += "l.Cmim_Blerje = ?";
            break;
        case "furnitori":
            sql += "l.Furnitori LIKE ?";
            kerkimi = "%" + kerkimi + "%";
            break;
        default:
            return;
    }

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

      
        if (tipi.equalsIgnoreCase("id")) {
            ps.setInt(1, Integer.parseInt(kerkimi));
        } else if (tipi.equalsIgnoreCase("cmimi") || tipi.equalsIgnoreCase("kosto")) {
            try {
                ps.setDouble(1, Double.parseDouble(kerkimi));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cmimi duhet te jte numer.");
                return;
            }
        } else {
            ps.setString(1, kerkimi);
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Object promoObj = rs.getObject("Cmim_Promocion");
            Double cmimPromocional = promoObj != null ? ((Number) promoObj).doubleValue() : null;

            model.addRow(new Object[]{
                    rs.getInt("ID_Libri"),
                    rs.getString("ISBN"),
                    rs.getString("Titulli"),
                    rs.getDouble("Cmim_Shitje"),
                    rs.getDouble("Cmim_Blerje"),
                    rs.getString("Furnitori"),
                    cmimPromocional
            });
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gabim");
    }
    
    }                                         

    private void RifreskoBtn1ActionPerformed(java.awt.event.ActionEvent evt) {                                             
     loadDataIntoTable();
        ((JTextField) SearchComboBox.getEditor().getEditorComponent()).setText("Kerko");
        TypeComboBox.setSelectedItem("Emri");
    }                                            

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Artikujt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Artikujt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Artikujt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Artikujt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Artikujt().setVisible(true);
            }
        });
    }

                        
    private javax.swing.JButton ArtikullRiBtn;
    private javax.swing.JButton FshiBtn1;
    private javax.swing.JButton KerkoBtn2;
    private javax.swing.JButton ModifikoBtn;
    private javax.swing.JButton RifreskoBtn1;
    private javax.swing.JComboBox<String> SearchComboBox;
    private javax.swing.JComboBox<String> TypeComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
                     
}