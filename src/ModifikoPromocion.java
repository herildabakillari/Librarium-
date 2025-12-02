import java.awt.Font;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;


public class ModifikoPromocion extends javax.swing.JDialog {

    private int idPromocioni; 
    private int selectedRowIndex = -1;

    
 public ModifikoPromocion(Frame parent, boolean modal, int idPromocioni) {
        super(parent, modal);
        this.idPromocioni = idPromocioni;
        initComponents(); 
        setupAutoComplete(TitulliComboBox);
        ngarkoTeDhenatPromocionit(); 
        jTable1.getTableHeader().setFont(new java.awt.Font("Serif", Font.BOLD, 18));
    }

public void setPromocionId(int id) {
    this.idPromocioni = id;
}
 private Vector<String> titujt = new Vector<>();
 
private void setupAutoComplete(JComboBox<String> comboBox) {
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

    comboBox.addActionListener(e -> {
        String selectedTitle = (String) comboBox.getSelectedItem();
        if (selectedTitle != null && !selectedTitle.trim().isEmpty()) {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT Cmim_Shitje FROM librat WHERE Titulli = ? LIMIT 1")) {
                ps.setString(1, selectedTitle);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        double cmimRef = rs.getDouble("Cmim_Shitje");
                        CmimiRefField.setText(String.valueOf(cmimRef));
                        ZbritjeField.setText("");
                        CmimPromField.setText("");
                        ZbritjeField.setEditable(true);
                        ZbritjeField.setEnabled(true);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    });
}
  
private void ngarkoTeDhenatPromocionit() {
    try (Connection con = DBConnection.getConnection()) {
        String queryPromo = "SELECT Emri_Promocionit, Data_Fillimit, Data_Mbarimit FROM promocione WHERE ID_Promocioni = ?";
        PreparedStatement pstPromo = con.prepareStatement(queryPromo);
        pstPromo.setInt(1, idPromocioni);
        ResultSet rsPromo = pstPromo.executeQuery();

        if (rsPromo.next()) {
            EmrField.setText(rsPromo.getString("Emri_Promocionit")); 
            DataFField.setText(rsPromo.getDate("Data_Fillimit").toString());
            DataMField.setText(rsPromo.getDate("Data_Mbarimit").toString());
        }

        // Librat
        String queryLibrat = "SELECT l.ID_Libri, l.Titulli, l.Cmim_Shitje, lp.Cmim_Promocion " +
                             "FROM librat_promocion lp " +
                             "JOIN librat l ON lp.ID_Libri = l.ID_Libri " +
                             "WHERE lp.ID_Promocioni = ?";
        PreparedStatement pstLibra = con.prepareStatement(queryLibrat);
        pstLibra.setInt(1, idPromocioni);
        ResultSet rsLibrat = pstLibra.executeQuery();

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        while (rsLibrat.next()) {
            int idLibri = rsLibrat.getInt("ID_Libri");
            String titulli = rsLibrat.getString("Titulli");
            double cmimiReference = rsLibrat.getDouble("Cmim_Shitje");
            double cmimiPromocional = rsLibrat.getDouble("Cmim_Promocion");

            model.addRow(new Object[]{
                idLibri,
                titulli,
                cmimiReference,
                cmimiPromocional
            });
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Gabim gjate ngarkimit: " + ex.getMessage());
    }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        AnulloBtn = new javax.swing.JButton();
        AnulloBtn.setForeground(new Color(51, 102, 153));
        jComboBox3 = new javax.swing.JComboBox<>();
        PastroBtn = new javax.swing.JButton();
        PastroBtn.setForeground(new Color(51, 102, 153));
        jLabel7 = new javax.swing.JLabel();
        jLabel7.setForeground(new Color(51, 102, 153));
        jComboBox4 = new javax.swing.JComboBox<>();
        ZbritjeField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel8.setForeground(new Color(51, 102, 153));
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        FshiBtn = new javax.swing.JButton();
        FshiBtn.setForeground(new Color(51, 102, 153));
        PastroTabeleBtn = new javax.swing.JButton();
        PastroTabeleBtn.setForeground(new Color(51, 102, 153));
        jLabel9 = new javax.swing.JLabel();
        jLabel9.setForeground(new Color(51, 102, 153));
        EmrField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        DataFField = new javax.swing.JTextField();
        DataMField = new javax.swing.JTextField();
        EmrLabel = new javax.swing.JLabel();
        EmrLabel.setForeground(new Color(51, 102, 153));
        TitulliComboBox = new javax.swing.JComboBox<>();
        dataFLabel = new javax.swing.JLabel();
        dataFLabel.setForeground(new Color(51, 102, 153));
        CmimiRefField = new javax.swing.JTextField();
        RuajBtn = new javax.swing.JButton();
        RuajBtn.setForeground(new Color(51, 102, 153));
        DataMLAbel = new javax.swing.JLabel();
        DataMLAbel.setForeground(new Color(51, 102, 153));
        CmimPromField = new javax.swing.JTextField();
        ShtoBtn = new javax.swing.JButton();
        ShtoBtn.setForeground(new Color(51, 102, 153));
        jLabel5 = new javax.swing.JLabel();
        jLabel5.setForeground(new Color(51, 102, 153));
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel6.setForeground(new Color(51, 102, 153));
        PerditesoBtn = new javax.swing.JButton();
        PerditesoBtn.setForeground(new Color(51, 102, 153));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        AnulloBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        AnulloBtn.setText("❌ Anullo");
        AnulloBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnulloBtnActionPerformed(evt);
            }
        });

        jComboBox3.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Leke", "€" }));

        PastroBtn.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        PastroBtn.setText("🔄 Pastro");
        PastroBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PastroBtnActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel7.setText("Cmimi Reference:");

        jComboBox4.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "%" }));

        ZbritjeField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ZbritjeFieldKeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel8.setText("Zbritja (ne %)");

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jTable1.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Titulli LIbrit", "Cmimi Reference ", "Cmimi Promocional"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(44);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel10.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        jLabel10.setText("📘 Librat ne Promocion");

        FshiBtn.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        FshiBtn.setText("❌ Fshi Liber");
        FshiBtn.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                FshiBtnComponentRemoved(evt);
            }
        });
        FshiBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FshiBtnActionPerformed(evt);
            }
        });

        PastroTabeleBtn.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        PastroTabeleBtn.setText("🔄 Pastro");
        PastroTabeleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PastroTabeleBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addGap(98)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addComponent(PastroTabeleBtn, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
        					.addGap(29)
        					.addComponent(FshiBtn)
        					.addGap(78))
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addGap(15)
        					.addComponent(jLabel10, GroupLayout.PREFERRED_SIZE, 231, GroupLayout.PREFERRED_SIZE)
        					.addGap(0, 437, Short.MAX_VALUE)))
        			.addGap(20))
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 732, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jLabel10)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 411, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(PastroTabeleBtn, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        				.addComponent(FshiBtn, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap())
        );
        jPanel1.setLayout(jPanel1Layout);

        jLabel9.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel9.setText("Cmimi promocional");

        EmrField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        jPanel2.setBackground(new Color(119, 136, 153));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Promocion i Ri");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        DataFField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        DataMField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        DataMField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DataMFieldActionPerformed(evt);
            }
        });

        EmrLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        EmrLabel.setText("Emri i Promocionit:");

        TitulliComboBox.setEditable(true);
        TitulliComboBox.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        TitulliComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Titulli..." }));
        TitulliComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TitulliComboBoxActionPerformed(evt);
            }
        });

        dataFLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        dataFLabel.setText("Data e Fillimit:");

        CmimiRefField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        RuajBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        RuajBtn.setText("💾 Ruaj");
        RuajBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuajBtnActionPerformed(evt);
            }
        });

        DataMLAbel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        DataMLAbel.setText("Data e Mbarimit:");

        CmimPromField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        CmimPromField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CmimPromFieldActionPerformed(evt);
            }
        });

        ShtoBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        ShtoBtn.setText("➕ Shto Liber");
        ShtoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShtoBtnActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Serif", 1, 22)); // NOI18N
        jLabel5.setText("➕ Shto Liber ne Promocion:");

        jComboBox2.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Leke", "€" }));

        jLabel6.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel6.setText("Titulli i Librit:");

        PerditesoBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        PerditesoBtn.setText("🔄 Perditeso");
        PerditesoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PerditesoBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, 1362, Short.MAX_VALUE)
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(layout.createSequentialGroup()
        					.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
        						.addGroup(layout.createParallelGroup(Alignment.LEADING)
        							.addGroup(layout.createSequentialGroup()
        								.addGap(52)
        								.addGroup(layout.createParallelGroup(Alignment.LEADING)
        									.addComponent(EmrLabel)
        									.addComponent(dataFLabel)
        									.addComponent(DataMLAbel))
        								.addPreferredGap(ComponentPlacement.UNRELATED)
        								.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        									.addComponent(DataFField)
        									.addComponent(DataMField, GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
        									.addComponent(EmrField)))
        							.addGroup(layout.createSequentialGroup()
        								.addGap(24)
        								.addComponent(jLabel5))
        							.addGroup(layout.createSequentialGroup()
        								.addGap(40)
        								.addGroup(layout.createParallelGroup(Alignment.LEADING)
        									.addComponent(jLabel6)
        									.addComponent(jLabel7)
        									.addComponent(jLabel8)
        									.addComponent(jLabel9))))
        						.addGroup(layout.createSequentialGroup()
        							.addGap(55)
        							.addComponent(PerditesoBtn, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
        							.addPreferredGap(ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
        							.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
        								.addComponent(TitulliComboBox, 0, 293, Short.MAX_VALUE)
        								.addGroup(layout.createSequentialGroup()
        									.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
        										.addComponent(CmimPromField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
        										.addComponent(CmimiRefField, Alignment.LEADING)
        										.addComponent(ZbritjeField))
        									.addGap(18)
        									.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        										.addComponent(jComboBox2, 0, 89, Short.MAX_VALUE)
        										.addComponent(jComboBox4, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        										.addComponent(jComboBox3, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        								.addGroup(layout.createSequentialGroup()
        									.addComponent(PastroBtn, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
        									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        									.addComponent(ShtoBtn)))))
        					.addGap(18)
        					.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        					.addGap(0, 0, Short.MAX_VALUE))
        				.addGroup(layout.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(AnulloBtn, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
        					.addGap(30)
        					.addComponent(RuajBtn, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
        					.addGap(90)))
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGap(26)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(EmrLabel)
        						.addComponent(EmrField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        					.addGap(29)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(dataFLabel)
        						.addComponent(DataFField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        					.addGap(29)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(DataMLAbel)
        						.addComponent(DataMField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        					.addGap(34)
        					.addComponent(jLabel5)
        					.addGap(33)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel6)
        						.addComponent(TitulliComboBox, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        					.addGap(27)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel7)
        						.addComponent(CmimiRefField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jComboBox2, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        					.addGap(33)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jLabel8)
        						.addComponent(jComboBox4, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        						.addComponent(ZbritjeField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        					.addGap(32)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(jComboBox3, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jLabel9)
        						.addComponent(CmimPromField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
        					.addGap(38)
        					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        						.addComponent(PerditesoBtn, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
        						.addComponent(ShtoBtn, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
        						.addComponent(PastroBtn, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)))
        				.addComponent(jPanel1, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(AnulloBtn, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
        				.addComponent(RuajBtn, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap())
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>                        

    private void AnulloBtnActionPerformed(java.awt.event.ActionEvent evt) {                                          
        dispose();
    }                                         

    private void PastroBtnActionPerformed(java.awt.event.ActionEvent evt) {                                          
        TitulliComboBox.setSelectedItem("");
        CmimiRefField.setText("");
        ZbritjeField.setText("");
        CmimPromField.setText("");
        TitulliComboBox.requestFocusInWindow();
        TitulliComboBox.setSelectedItem("");

    ShtoBtn.setVisible(true);
    PerditesoBtn.setVisible(false);
    }                                         

    private void FshiBtnComponentRemoved(java.awt.event.ContainerEvent evt) {                                         

    }                                        

    private void FshiBtnActionPerformed(java.awt.event.ActionEvent evt) {                                        
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Ju lutem selektoni nje rresht per te fshire.", "Gabim", JOptionPane.WARNING_MESSAGE);
        }
    }                                       

    private void PastroTabeleBtnActionPerformed(java.awt.event.ActionEvent evt) {                                                
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
    }                                               

    private void DataMFieldActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void TitulliComboBoxActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
    }                                               

    private void RuajBtnActionPerformed(java.awt.event.ActionEvent evt) {                                        
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tabela eshte bosh!", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String emriPromocionit = EmrField.getText().trim();
        if (emriPromocionit.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ju lutem shkruani emrin e promocionit.", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.sql.Date dataFillimit;
        java.sql.Date dataMbarimit;
        try {
            dataFillimit = java.sql.Date.valueOf(DataFField.getText().trim());
            dataMbarimit = java.sql.Date.valueOf(DataMField.getText().trim());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Data duhet te jete ne formatin yyyy-MM-dd.", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            String insertPromo = "INSERT INTO promocione (Emri_Promocionit, Data_Fillimit, Data_Mbarimit) VALUES (?, ?, ?)";
            try (PreparedStatement psPromo = conn.prepareStatement(insertPromo, Statement.RETURN_GENERATED_KEYS)) {
                psPromo.setString(1, emriPromocionit);
                psPromo.setDate(2, dataFillimit);
                psPromo.setDate(3, dataMbarimit);
                psPromo.executeUpdate();

                try (ResultSet rs = psPromo.getGeneratedKeys()) {
                    if (rs.next()) {
                        int promocionId = rs.getInt(1);

                        String insertLibrat = "INSERT INTO librat_promocion (ID_Promocioni, ID_Libri, Cmim_Promocion) VALUES (?, ?, ?)";
                        try (PreparedStatement psLibrat = conn.prepareStatement(insertLibrat)) {
                            for (int i = 0; i < model.getRowCount(); i++) {
                                Object libriIdObj = model.getValueAt(i, 0);
                                Object cmimPromoObj = model.getValueAt(i, 3);

                                if (libriIdObj == null || cmimPromoObj == null) {
                                    JOptionPane.showMessageDialog(this, "te dhenat mungojne ne rreshtin " + (i + 1), "Gabim", JOptionPane.ERROR_MESSAGE);
                                    conn.rollback();
                                    return;
                                }

                                int libriId;
                                double cmimPromo;
                                try {
                                    libriId = Integer.parseInt(libriIdObj.toString());
                                    cmimPromo = Double.parseDouble(cmimPromoObj.toString().replace(",", "."));
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(this, "Format i gabuar ne rreshtin " + (i + 1), "Gabim", JOptionPane.ERROR_MESSAGE);
                                    conn.rollback();
                                    return;
                                }

                                psLibrat.setInt(1, promocionId);
                                psLibrat.setInt(2, libriId);
                                psLibrat.setDouble(3, cmimPromo);
                                psLibrat.addBatch();
                            }

                            psLibrat.executeBatch();
                            conn.commit();
                            JOptionPane.showMessageDialog(this, "Promocioni u perditesua me sukses.");
                            dispose(); 
                        }
                    } else {
                        conn.rollback();
                        JOptionPane.showMessageDialog(this, "Nuk u gjenerua ID per promocionin.", "Gabim", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gabim ne ruajtjen e promocionit: " + ex.getMessage(), "Gabim", JOptionPane.ERROR_MESSAGE);
        }

    }                                       

    private void CmimPromFieldActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
    }                                             

    private void ShtoBtnActionPerformed(java.awt.event.ActionEvent evt) {                                        
        String titulli = (String) TitulliComboBox.getSelectedItem();
        if (titulli == null || titulli.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ju lutem zgjidhni nje titull libri.", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double cmimRef, cmimPromo, zbritje;
        try {
            cmimRef = Double.parseDouble(CmimiRefField.getText().replace(",", "."));
            String zbritjeStr = ZbritjeField.getText().trim();
            zbritje = zbritjeStr.isEmpty() ? 0.0 : Double.parseDouble(zbritjeStr.replace(",", "."));
            cmimPromo = Double.parseDouble(CmimPromField.getText().replace(",", "."));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Fushat e çmimeve duhet te jene numerike.", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cmimPromo > cmimRef) {
            JOptionPane.showMessageDialog(this, "Çmimi promocional nuk mund te jete me i madh se çmimi reference.", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT ID_Libri, Cmim_Blerje FROM librat WHERE Titulli = ? LIMIT 1")) {
            ps.setString(1, titulli);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int libId = rs.getInt("ID_Libri");
                double cmimBlerje = rs.getDouble("Cmim_Blerje");

                if (cmimPromo < cmimBlerje) {
                    int option = JOptionPane.showConfirmDialog(this,
                        "Çmimi promocional eshte me i vogel se çmimi i blerjes. A dëshironi te vazhdoni?",
                        "Kujdes", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (option != JOptionPane.YES_OPTION) return;
                }

                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
boolean ekziston = false;

for (int i = 0; i < model.getRowCount(); i++) {
    int ekzistuesId = (int) model.getValueAt(i, 0);
    if (ekzistuesId == libId) {
        
        model.setValueAt(titulli, i, 1);
        model.setValueAt(cmimRef, i, 2);
        model.setValueAt(cmimPromo, i, 3);
        ekziston = true;
        break;
    }
}

if (!ekziston) {
    model.addRow(new Object[]{libId, titulli, cmimRef, cmimPromo});
}


                TitulliComboBox.setSelectedItem("");
                CmimiRefField.setText("");
                ZbritjeField.setText("");
                CmimPromField.setText("");
                TitulliComboBox.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(this, "Libri me kete titull nuk u gjet.", "Gabim", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gabim gjate marrjes se te dhenave te librit.", "Gabim", JOptionPane.ERROR_MESSAGE);
        }
    }                                       

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {                                     
 int row = jTable1.getSelectedRow();
    if (row != -1) {
        String titulli = (String) jTable1.getValueAt(row, 1);
        double cmimiRef = (double) jTable1.getValueAt(row, 2);
        double cmimiPromo = (double) jTable1.getValueAt(row, 3);

        TitulliComboBox.setSelectedItem(titulli);
        CmimiRefField.setText(String.valueOf(cmimiRef));
        CmimPromField.setText(String.valueOf(cmimiPromo));

        double zbritja = ((cmimiRef - cmimiPromo) / cmimiRef) * 100;
        ZbritjeField.setText(String.format("%.2f", zbritja));

        
        ShtoBtn.setVisible(false);
        PerditesoBtn.setVisible(true);
    
        }
    
    }                                    

    private void ZbritjeFieldKeyReleased(java.awt.event.KeyEvent evt) {                                         
        try {
            double cmimRef = Double.parseDouble(CmimiRefField.getText().replace(",", "."));
            double zbritje = Double.parseDouble(ZbritjeField.getText().replace(",", "."));
            double cmimiPromo = cmimRef - (cmimRef * zbritje / 100);
            CmimPromField.setText(String.format("%.2f", cmimiPromo));
        } catch (NumberFormatException ex) {
            
        }
    }                                        

    private void PerditesoBtnActionPerformed(java.awt.event.ActionEvent evt) {                                             
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Ju lutem selektoni nje rresht per te perditesuar.");
        return;
    }

    String titulli = (String) TitulliComboBox.getSelectedItem();
    if (titulli == null || titulli.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Titulli nuk mund te jete bosh.", "Gabim", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        double cmimRef = Double.parseDouble(CmimiRefField.getText().replace(",", "."));
        double cmimPromo = Double.parseDouble(CmimPromField.getText().replace(",", "."));

        
        jTable1.setValueAt(titulli, selectedRow, 1);
        jTable1.setValueAt(cmimRef, selectedRow, 2);
        jTable1.setValueAt(cmimPromo, selectedRow, 3);

        
        TitulliComboBox.setSelectedItem("");
        CmimiRefField.setText("");
        CmimPromField.setText("");
        ZbritjeField.setText("");

        ShtoBtn.setVisible(true);
        PerditesoBtn.setVisible(false);

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Ju lutem vendosni çmime valide.", "Gabim", JOptionPane.ERROR_MESSAGE);
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
            java.util.logging.Logger.getLogger(ModifikoPromocion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ModifikoPromocion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ModifikoPromocion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ModifikoPromocion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               ModifikoPromocion dialog = new ModifikoPromocion(new javax.swing.JFrame(), true, 1); 

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
    private javax.swing.JTextField CmimPromField;
    private javax.swing.JTextField CmimiRefField;
    private javax.swing.JTextField DataFField;
    private javax.swing.JTextField DataMField;
    private javax.swing.JLabel DataMLAbel;
    private javax.swing.JTextField EmrField;
    private javax.swing.JLabel EmrLabel;
    private javax.swing.JButton FshiBtn;
    private javax.swing.JButton PastroBtn;
    private javax.swing.JButton PastroTabeleBtn;
    private javax.swing.JButton PerditesoBtn;
    private javax.swing.JButton RuajBtn;
    private javax.swing.JButton ShtoBtn;
    private javax.swing.JComboBox<String> TitulliComboBox;
    private javax.swing.JTextField ZbritjeField;
    private javax.swing.JLabel dataFLabel;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration                   
}