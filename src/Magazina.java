import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;


public class Magazina extends javax.swing.JFrame {
	 private PJesaPare pjesaPare; 
 private final List<String> fullSuggestions = new ArrayList<>();
   
 public Magazina(PJesaPare pjesaPare) {
     this.pjesaPare = pjesaPare;
        initComponents();
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (pjesaPare != null) {  
                    pjesaPare.setVisible(true);
                }
            }
        });
    

        TypeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Emri", "ISBN", "ID", "Kategoria", "Autori"}));
        TypeComboBox.addActionListener(e -> loadFullSuggestions((String) TypeComboBox.getSelectedItem()));
        loadFullSuggestions("Emri");

        String today = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        DataField.setText(today);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

        int colCount = IventariTable.getColumnModel().getColumnCount();
        for (int i = 0; i < colCount; i++) {
            IventariTable.getColumnModel().getColumn(i).setPreferredWidth(150);
        }

        enableAutoSuggest(SearchComboBox);

        
        ((JTextField) SearchComboBox.getEditor().getEditorComponent()).setText("Kerko");

       
        SearchComboBox.addActionListener(e -> {
            if (SearchComboBox.getSelectedItem() != null) {
                JTextField tf = (JTextField) SearchComboBox.getEditor().getEditorComponent();
                tf.setText("Kerko");
            }
        });

        shfaqTeDhenatNeTabele();
    }

    private void vendosRendererPromocioni() {
        PromoRowRenderer renderer = new PromoRowRenderer();
        for (int i = 0; i < IventariTable.getColumnCount(); i++) {
            IventariTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
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
            case "kategoria":
                sql = "SELECT DISTINCT Kategoria FROM librat";
                break;
            case "autori":
                sql = "SELECT DISTINCT Autori FROM librat";
                break;
            case "id":
                sql = "SELECT ID_Libri, Titulli FROM librat";
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
    private void shfaqTeDhenatNeTabele() {
        DefaultTableModel model = (DefaultTableModel) IventariTable.getModel();
        model.setRowCount(0); 

        String sql = "SELECT ID_Libri, Titulli, Autori, Kategoria, Vendndodhja_raft, Gjendje_Dyqan, Cmim_Shitje, Cmim_Shumice FROM librat";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("ID_Libri"),
                    rs.getString("Titulli"),
                    rs.getString("Autori"),
                    rs.getString("Kategoria"),
                    rs.getString("Vendndodhja_raft"),
                    rs.getInt("Gjendje_Dyqan"),
                    rs.getDouble("Cmim_Shitje"),
                    rs.getDouble("Cmim_Shumice")
                });
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gabim gjate ngarkimit te te dhenave te librave.");
        }
    }


    private void shfaqDialogPershkrimi(String pershkrimi) {
        JTextArea area = new JTextArea(pershkrimi);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setEditable(false);
        area.setFont(new Font("Serif", Font.PLAIN, 18));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(400, 200));
        JOptionPane.showMessageDialog(this, scroll, "Pershkrimi i Librit", JOptionPane.INFORMATION_MESSAGE);
    }

   private class PromoRowRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
                                                   boolean isSelected, boolean hasFocus, 
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        Object promoValue = table.getValueAt(row, 9); 
        
        if (promoValue != null && !promoValue.toString().isEmpty()) {
            try {
                double promo = Double.parseDouble(promoValue.toString());
                if (promo > 0) {
                    c.setBackground(new Color(255, 204, 204)); 
                    c.setForeground(Color.BLACK);
                } else {
                  
                    if (isSelected) {
                        c.setBackground(table.getSelectionBackground());
                        c.setForeground(table.getSelectionForeground());
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                }
            } catch (NumberFormatException e) {
               
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
            }
        } else {
            
            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            } else {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }
        }
        
        return c;
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        jLabel6.setForeground(new Color(51, 102, 153));
        jLabel8 = new javax.swing.JLabel();
        jLabel8.setForeground(new Color(51, 102, 153));
        DataField = new javax.swing.JTextField();
        DataField.setForeground(new Color(51, 102, 153));
        TypeComboBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        IventariTable = new javax.swing.JTable();
        BestSellerLabel = new javax.swing.JTextField();
        BestSellerLabel.setForeground(new Color(153, 0, 0));
        SearchComboBox = new javax.swing.JComboBox<>();
        KerkoBtn2 = new javax.swing.JButton();
        KerkoBtn2.setForeground(new Color(51, 102, 153));
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        MbaruarBtn = new javax.swing.JButton();
        PakBtn = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel2.setForeground(new Color(51, 102, 153));
        MbaruarValues = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel4.setForeground(new Color(51, 102, 153));
        PakValues = new javax.swing.JLabel();
        RifreskoBtn1 = new javax.swing.JButton();
        RifreskoBtn1.setForeground(new Color(51, 102, 153));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel6.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        jLabel6.setText("Me i Shitur");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("Data:");

        DataField.setEditable(false);
        DataField.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        DataField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DataFieldActionPerformed(evt);
            }
        });

        TypeComboBox.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        TypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Emri", "ISBN", "ID", "Kategoria", "Autori" }));

        IventariTable.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        IventariTable.setModel(new javax.swing.table.DefaultTableModel(
        	    new Object [][] {
        	        
        	    },
        	    new String [] {
        	        "ID Libri", "Titulli", "Autori", "Kategoria",
        	        "Vendndodhja ne raft", "Gjendje ne dyqan", "Cmim Shitjes", "Cmim Shumice"
        	    }
        	) {
        	    boolean[] canEdit = new boolean [] {
        	        false, false, false, false, false, false, false, false
        	    };

        	    public boolean isCellEditable(int rowIndex, int columnIndex) {
        	        return canEdit[columnIndex];
        	    }
        	});

        IventariTable.setRowHeight(45);
        IventariTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IventariTableMouseClicked(evt);
            }
        });
        IventariTable.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                IventariTableComponentShown(evt);
            }
        });
        jScrollPane1.setViewportView(IventariTable);

        BestSellerLabel.setEditable(false);
        BestSellerLabel.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        BestSellerLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BestSellerLabelActionPerformed(evt);
            }
        });

        SearchComboBox.setEditable(true);
        SearchComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kerko..." }));

        KerkoBtn2.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        KerkoBtn2.setText("🔍 Kerko");
        KerkoBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KerkoBtn2ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new Color(119, 136, 153));

        jLabel1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Magazina juaj");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        MbaruarBtn.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\StokIMbaruar.jpg")); // NOI18N
        MbaruarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MbaruarBtnMouseClicked(evt);
            }
        });
        MbaruarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MbaruarBtnActionPerformed(evt);
            }
        });

        PakBtn.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\StokIPaket.jpg")); // NOI18N
        PakBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PakBtnMouseClicked(evt);
            }
        });
        PakBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PakBtnActionPerformed(evt);
            }
        });

        jButton3.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\ArtikullShitur.jpg")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        jLabel2.setText("Stok i Mbaruar");

        jLabel4.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        jLabel4.setText("Stok i paket");

        RifreskoBtn1.setBackground(new java.awt.Color(255, 255, 255));
        RifreskoBtn1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        RifreskoBtn1.setText("🔄 Rifresko");
        RifreskoBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RifreskoBtn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 1324, Short.MAX_VALUE)
        		.addGroup(layout.createSequentialGroup()
        			.addGap(50)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(SearchComboBox, 0, 708, Short.MAX_VALUE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(TypeComboBox, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
        					.addGap(41)
        					.addComponent(KerkoBtn2, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
        					.addGap(40)
        					.addComponent(RifreskoBtn1)
        					.addGap(48))
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(MbaruarBtn, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(layout.createSequentialGroup()
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jLabel2))
        						.addGroup(layout.createSequentialGroup()
        							.addGap(34)
        							.addComponent(MbaruarValues, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
        					.addGap(80)
        					.addComponent(PakBtn, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(layout.createSequentialGroup()
        							.addGap(18)
        							.addComponent(jLabel4))
        						.addGroup(layout.createSequentialGroup()
        							.addGap(46)
        							.addComponent(PakValues, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)))
        					.addGap(80)
        					.addComponent(jButton3, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(jLabel6)
        							.addGap(418)
        							.addComponent(jLabel8)
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(DataField, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE))
        						.addComponent(BestSellerLabel, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE))))
        			.addContainerGap())
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 1273, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(41, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addGap(18)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(jLabel4)
        							.addGap(1)
        							.addComponent(PakValues, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        						.addGroup(layout.createSequentialGroup()
        							.addComponent(jLabel2)
        							.addGap(1)
        							.addComponent(MbaruarValues, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
        						.addComponent(PakBtn, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jButton3, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
        						.addComponent(MbaruarBtn, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(8)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        						.addComponent(DataField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jLabel6))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(BestSellerLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(SearchComboBox, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        				.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        					.addComponent(TypeComboBox, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        					.addComponent(KerkoBtn2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        					.addComponent(RifreskoBtn1)))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 578, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap())
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>                        

    private void DataFieldActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                         

    private void IventariTableMouseClicked(java.awt.event.MouseEvent evt) {                                           
        int row = IventariTable.getSelectedRow();
        int column = IventariTable.getSelectedColumn();

        if (column == 5 && row != -1) {
            int idLibri = (int) IventariTable.getValueAt(row, 0);
            try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT Pershkrimi FROM librat WHERE ID_Libri = ?")) {
                ps.setInt(1, idLibri);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String pershkrimi = rs.getString("Pershkrimi");
                    shfaqDialogPershkrimi(pershkrimi);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gabim gjate ngarkimit te pershkrimit.");
            }
        }
    }                                          

    private void IventariTableComponentShown(java.awt.event.ComponentEvent evt) {                                             
        // TODO add your handling code here:
    }                                            

    private void BestSellerLabelActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
    }                                               

    private void KerkoBtn2ActionPerformed(java.awt.event.ActionEvent evt) {                                          
        String tipi = (String) TypeComboBox.getSelectedItem();
        String kerkimi = ((JTextField) SearchComboBox.getEditor().getEditorComponent()).getText().trim();

        DefaultTableModel model = (DefaultTableModel) IventariTable.getModel();
        model.setRowCount(0);

        if (kerkimi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ju lutem shkruani nje vlere specifike per kerkimin.");
            return;
        }

        if (kerkimi.contains(" - ")) {
            kerkimi = kerkimi.split(" - ")[0].trim();
        }

        String sql = "SELECT ID_Libri, Titulli, Autori, Kategoria, Vendndodhja_raft, Gjendje_Dyqan, Cmim_Shitje, Cmim_Shumice " +
                     "FROM librat WHERE ";

        switch (tipi.toLowerCase()) {
            case "emri":
                sql += "Titulli LIKE ?";
                kerkimi = "%" + kerkimi + "%";
                break;
            case "isbn":
                sql += "ISBN = ?";
                break;
            case "id":
                sql += "ID_Libri = ?";
                try {
                    Integer.parseInt(kerkimi);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "ID duhet te jete numer.");
                    return;
                }
                break;
            case "kategoria":
                sql += "Kategoria LIKE ?";
                kerkimi = "%" + kerkimi + "%";
                break;
            case "autori":
                sql += "Autori LIKE ?";
                kerkimi = "%" + kerkimi + "%";
                break;
            default:
                return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (tipi.equalsIgnoreCase("id")) {
                ps.setInt(1, Integer.parseInt(kerkimi));
            } else {
                ps.setString(1, kerkimi);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("ID_Libri"),
                    rs.getString("Titulli"),
                    rs.getString("Autori"),
                    rs.getString("Kategoria"),
                    rs.getString("Vendndodhja_raft"),
                    rs.getInt("Gjendje_Dyqan"),
                    rs.getDouble("Cmim_Shitje"),
                    rs.getDouble("Cmim_Shumice")
                });
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gabim gjate ekzekutimit te kerkimit.");
        }
    } 

    private void MbaruarBtnMouseClicked(java.awt.event.MouseEvent evt) {                                        

    }                                       

    private void MbaruarBtnActionPerformed(java.awt.event.ActionEvent evt) {                                           

        IventarStokMbaruar dialog = new IventarStokMbaruar(this, true);
        dialog.setVisible(true);
    }                                          

    private void PakBtnMouseClicked(java.awt.event.MouseEvent evt) {                                    

    }                                   

    private void PakBtnActionPerformed(java.awt.event.ActionEvent evt) {                                       
        IventariNeMbarim dialog = new IventariNeMbarim(this, true);
        dialog.setVisible(true);
    }                                      

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        String sql = "SELECT l.Titulli, SUM(f.sasia) AS total_sasi " +
                     "FROM faturat f " +
                     "JOIN librat l ON f.id_produkti = l.ID_Libri " +
                     "WHERE f.data >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
                     "GROUP BY f.id_produkti " +
                     "ORDER BY total_sasi DESC " +
                     "LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String titulli = rs.getString("Titulli");
                int totalSasi = rs.getInt("total_sasi");

               
                BestSellerLabel.setText(titulli + " (" + totalSasi + " shitje)");
            } else {
                BestSellerLabel.setText("Nuk ka shitje kete jave");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gabim gjate gjetjes se produktit me te shitur.");
        }
    }
                                     

    private void RifreskoBtn1ActionPerformed(java.awt.event.ActionEvent evt) {                                             
    shfaqTeDhenatNeTabele();
    ((JTextField) SearchComboBox.getEditor().getEditorComponent()).setText("Kerko");
    TypeComboBox.setSelectedItem("Emri");
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
            java.util.logging.Logger.getLogger(Magazina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Magazina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Magazina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Magazina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Magazina().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JTextField BestSellerLabel;
    private javax.swing.JTextField DataField;
    private javax.swing.JTable IventariTable;
    private javax.swing.JButton KerkoBtn2;
    private javax.swing.JButton MbaruarBtn;
    private javax.swing.JLabel MbaruarValues;
    private javax.swing.JButton PakBtn;
    private javax.swing.JLabel PakValues;
    private javax.swing.JButton RifreskoBtn1;
    private javax.swing.JComboBox<String> SearchComboBox;
    private javax.swing.JComboBox<String> TypeComboBox;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration                   
}
