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

import javax.swing.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Iventari extends JFrame {
    private AdminPage adminPage; 
    private final List<String> fullSuggestions = new ArrayList<>();

    

    public Iventari(AdminPage adminPage) {
        this.adminPage = adminPage;
        initComponents();
        addCloseListener(); 

       
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

   
    private void addCloseListener() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (adminPage != null) {
                    adminPage.setVisible(true);
                }
            }
        });
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

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT l.ID_Libri, l.ISBN, l.Titulli, l.Autori, l.Kategoria, l.Pershkrimi, l.Vendndodhja_raft, " +
                             "l.Cmim_Blerje, l.Cmim_Shitje, l.Cmim_Shumice, lp.Cmim_Promocion, " +
                             "l.Gjendje_Dyqan, l.Gjendje_Magazine " +
                             "FROM librat l " +
                             "LEFT JOIN librat_promocion lp ON l.ID_Libri = lp.ID_Libri")) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object promoObj = rs.getObject("Cmim_Promocion");
                Double cmimPromocional = promoObj != null ? ((Number) promoObj).doubleValue() : null;

                model.addRow(new Object[]{
                        rs.getInt("ID_Libri"),
                        rs.getString("ISBN"),
                        rs.getString("Titulli"),
                        rs.getString("Autori"),
                        rs.getString("Kategoria"),
                        "Shiko pershkrim",
                        rs.getString("Vendndodhja_raft"),
                        rs.getDouble("Cmim_Blerje"),
                        rs.getDouble("Cmim_Shitje"),
                        rs.getDouble("Cmim_Shumice"),
                        cmimPromocional,
                        rs.getInt("Gjendje_Dyqan"),
                        rs.getInt("Gjendje_Magazine")
                });
            }

            vendosRendererPromocioni();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gabim gjate ngarkimit te te dhenave nga databaza.");
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
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Object promoValue = table.getValueAt(row, 10);

            if (promoValue instanceof Number && ((Number) promoValue).doubleValue() > 0) {
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
            return c;
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        MbaruarBtn = new javax.swing.JButton();
        PakBtn = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        MbaruarValues = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        PakValues = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        DataField = new javax.swing.JTextField();
        TypeComboBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        IventariTable = new javax.swing.JTable();
        BestSellerLabel = new javax.swing.JTextField();
        BestSellerLabel.setForeground(new Color(204, 0, 51));
        SearchComboBox = new javax.swing.JComboBox<>();
        KerkoBtn2 = new javax.swing.JButton();
        RifreskoBtn1 = new javax.swing.JButton();
        artikujBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        TransferoBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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
                .addContainerGap(24, Short.MAX_VALUE))
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
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID LIbri ", "ISBN", "Titulli", "Autori", "Kategoria", "Pershkrimi", "Vendndodhja ne raft", "Cmimi Blerjes", "Cmjmi Shitjes", "Cmimi Shumices", "Cmimi Promocional", "Gjendje Dyqan", "Magazina"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
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

        RifreskoBtn1.setBackground(new java.awt.Color(255, 255, 255));
        RifreskoBtn1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        RifreskoBtn1.setText("🔄 Rifresko");
        RifreskoBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RifreskoBtn1ActionPerformed(evt);
            }
        });

        artikujBtn.setFont(new java.awt.Font("Serif", 1, 25)); // NOI18N
        artikujBtn.setText("📚");
        artikujBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                artikujBtnActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        jLabel3.setText("Artikujt");

        TransferoBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        TransferoBtn.setText("Transfero");
        TransferoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TransferoBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 1245, Short.MAX_VALUE)
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(TransferoBtn, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE)
        				.addGroup(layout.createParallelGroup(Alignment.LEADING)
        					.addGroup(layout.createSequentialGroup()
        						.addGap(50)
        						.addGroup(layout.createParallelGroup(Alignment.LEADING)
        							.addGroup(layout.createSequentialGroup()
        								.addComponent(SearchComboBox, 0, 699, Short.MAX_VALUE)
        								.addPreferredGap(ComponentPlacement.UNRELATED)
        								.addComponent(TypeComboBox, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
        								.addGap(18)
        								.addComponent(KerkoBtn2, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
        								.addGap(17)
        								.addComponent(RifreskoBtn1))
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
        								.addGap(51)
        								.addComponent(jButton3, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
        								.addGap(18)
        								.addGroup(layout.createParallelGroup(Alignment.LEADING)
        									.addComponent(BestSellerLabel, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
        									.addComponent(jLabel6))
        								.addGap(18)
        								.addComponent(artikujBtn)
        								.addGap(18)
        								.addComponent(jLabel3)
        								.addGap(42)
        								.addComponent(jLabel8)
        								.addGap(0)
        								.addComponent(DataField, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE))))
        					.addGroup(layout.createSequentialGroup()
        						.addContainerGap()
        						.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 1201, GroupLayout.PREFERRED_SIZE))))
        			.addGap(34))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
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
        									.addGap(32)
        									.addComponent(MbaruarValues, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
        								.addComponent(PakBtn, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
        								.addComponent(MbaruarBtn, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
        								.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        									.addComponent(BestSellerLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        									.addComponent(jButton3, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
        									.addComponent(artikujBtn, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))))
        						.addGroup(layout.createSequentialGroup()
        							.addGap(18)
        							.addGroup(layout.createParallelGroup(Alignment.LEADING)
        								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        									.addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        									.addComponent(DataField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        								.addComponent(jLabel3))))
        					.addGap(18)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(SearchComboBox, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        						.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        							.addComponent(TypeComboBox, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        							.addComponent(KerkoBtn2, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        							.addComponent(RifreskoBtn1)))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 309, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addComponent(TransferoBtn, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
        				.addGroup(layout.createSequentialGroup()
        					.addGap(67)
        					.addComponent(jLabel6)))
        			.addContainerGap(24, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>                        

    private void PakBtnActionPerformed(java.awt.event.ActionEvent evt) {                                       
    IventariNeMbarim dialog = new IventariNeMbarim(this, true);
    dialog.setVisible(true);

    }                                      

    private void DataFieldActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
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

    private void MbaruarBtnMouseClicked(java.awt.event.MouseEvent evt) {                                        

    }                                       

    private void BestSellerLabelActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
    }                                               

    private void PakBtnMouseClicked(java.awt.event.MouseEvent evt) {                                    
     
    }                                   

    private void KerkoBtn2ActionPerformed(java.awt.event.ActionEvent evt) {                                          
     String tipi = (String) TypeComboBox.getSelectedItem();
    String kerkimi = ((JTextField) SearchComboBox.getEditor().getEditorComponent()).getText().trim();
    DefaultTableModel model = (DefaultTableModel) IventariTable.getModel();
    model.setRowCount(0);

    if (kerkimi.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ju lutem shkruani nje vlerë specifike per kerkimin.");
        return;
    }

   
    if (kerkimi.contains(" - ")) {
        kerkimi = kerkimi.split(" - ")[0].trim();
    }

    
    String sql = "SELECT l.ID_Libri, l.ISBN, l.Titulli, l.Autori, l.Kategoria, l.Pershkrimi, " +
            "l.Vendndodhja_raft, l.Cmim_Blerje, l.Cmim_Shitje, l.Cmim_Shumice, " +
            "lp.Cmim_Promocion, l.Gjendje_Dyqan, l.Gjendje_Magazine " +
            "FROM librat l LEFT JOIN librat_promocion lp ON l.ID_Libri = lp.ID_Libri " +
            "WHERE ";

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
                JOptionPane.showMessageDialog(this, "ID duhet të jete numer.");
                return;
            }
            break;
        case "kategoria":
            sql += "l.Kategoria LIKE ?";
            kerkimi = "%" + kerkimi + "%";
            break;
        case "autori":
            sql += "l.Autori LIKE ?";
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
            Object promoObj = rs.getObject("Cmim_Promocion");
            Double cmimPromocional = promoObj != null ? ((Number) promoObj).doubleValue() : null;

            model.addRow(new Object[]{
                    rs.getInt("ID_Libri"),
                    rs.getString("ISBN"),
                    rs.getString("Titulli"),
                    rs.getString("Autori"),
                    rs.getString("Kategoria"),
                    "Shiko pershkrim",
                    rs.getString("Vendndodhja_raft"),
                    rs.getDouble("Cmim_Blerje"),
                    rs.getDouble("Cmim_Shitje"),
                    rs.getDouble("Cmim_Shumice"),
                    cmimPromocional,
                    rs.getInt("Gjendje_Dyqan"),
                    rs.getInt("Gjendje_Magazine")
            });
        }

        vendosRendererPromocioni(); 
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gabim gjate ekzekutimit te kerkimit.");
    }


    }                                         

    private void MbaruarBtnActionPerformed(java.awt.event.ActionEvent evt) {                                           

        IventarStokMbaruar dialog = new IventarStokMbaruar(this, true);
        dialog.setVisible(true);
    }                                          

    private void IventariTableComponentShown(java.awt.event.ComponentEvent evt) {                                             
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

    private void RifreskoBtn1ActionPerformed(java.awt.event.ActionEvent evt) {                                             
        shfaqTeDhenatNeTabele();
        ((JTextField) SearchComboBox.getEditor().getEditorComponent()).setText("Kerko");
        TypeComboBox.setSelectedItem("Emri");
    }                                            

    private void artikujBtnActionPerformed(java.awt.event.ActionEvent evt) {                                           
   
        Artikujt artikujtForm = new Artikujt(null);
        artikujtForm.setExtendedState(artikujtForm.getExtendedState() | javax.swing.JFrame.MAXIMIZED_BOTH);
        artikujtForm.setLocationRelativeTo(null);
        artikujtForm.setVisible(true);
    }                                          

    private void TransferoBtnActionPerformed(java.awt.event.ActionEvent evt) {                                             
       int selectedRow = IventariTable.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "⚠️ Zgjidh nje liber per ta transferuar!");
        return;
    }

  
    int idLibri = (int)IventariTable.getValueAt(selectedRow, 0);

    TransferoDialog dialog = new TransferoDialog(this, true, idLibri);
    dialog.setTitle("Transfero Librin");
    dialog.setVisible(true);
    
   shfaqTeDhenatNeTabele();
    }                                            


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
            java.util.logging.Logger.getLogger(Iventari.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Iventari.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Iventari.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Iventari.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Iventari().setVisible(true);
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
    private javax.swing.JButton TransferoBtn;
    private javax.swing.JComboBox<String> TypeComboBox;
    private javax.swing.JButton artikujBtn;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
}
    // End of variables declaration