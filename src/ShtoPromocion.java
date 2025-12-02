import java.awt.Font;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.Color;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ShtoPromocion extends javax.swing.JDialog {
    public ShtoPromocion(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initCustomLogic();
        jTable1.getTableHeader().setFont(new java.awt.Font("Serif", Font.BOLD, 18));
    }
    private int selectedRowIndex = -1;
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

    private void initCustomLogic() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        DataFField.setText(today);
        DataMField.setText(today);
        ZbritjeField.setEditable(false);
        ZbritjeField.setEnabled(false);
        setupAutoComplete(TitulliComboBox);
        ZbritjeField.getDocument().addDocumentListener(new DocumentListener() {
            private void updatePromoPrice() {
                try {
                    String cmimRefStr = CmimiRefField.getText().trim();
                    String zbritjeStr = ZbritjeField.getText().trim();
                    if (cmimRefStr.isEmpty() || zbritjeStr.isEmpty()) return;
                    double cmimRef = Double.parseDouble(cmimRefStr.replace(",", "."));
                    double zbritje = Double.parseDouble(zbritjeStr.replace(",", "."));
                    if (zbritje < 0 || zbritje > 100) {
                        JOptionPane.showMessageDialog(null, "Zbritja duhet te jete nga 0 deri ne 100%.", "Gabim", JOptionPane.ERROR_MESSAGE);
                        ZbritjeField.setText("");
                        CmimPromField.setText("");
                        return;
                    }
                    double cmimPromo = cmimRef * (1 - zbritje / 100.0);
                    CmimPromField.setText(String.format("%.2f", cmimPromo));
                } catch (NumberFormatException ex) {
                }
            }

            public void insertUpdate(DocumentEvent e) {
                updatePromoPrice();
            }

            public void removeUpdate(DocumentEvent e) {
                updatePromoPrice();
            }

            public void changedUpdate(DocumentEvent e) {
                updatePromoPrice();
            }
        });
        CmimPromField.getDocument().addDocumentListener(new DocumentListener() {
            private void validateAndClearDiscount() {
                SwingUtilities.invokeLater(() -> {
                    String cmimPromoText = CmimPromField.getText().trim();
                    String cmimRefText = CmimiRefField.getText().trim();
                    if (!cmimPromoText.isEmpty() && !cmimRefText.isEmpty()) {
                        try {
                            double cmimPromo = Double.parseDouble(cmimPromoText.replace(",", "."));
                            double cmimRef = Double.parseDouble(cmimRefText.replace(",", "."));
                            if (cmimPromo > cmimRef) {
                                JOptionPane.showMessageDialog(null, "Cmimi promocional nuk mund te jete me i madh se cmimi reference!", "Gabim", JOptionPane.ERROR_MESSAGE);
                                CmimPromField.setText("");
                            }
                        } catch (NumberFormatException ex) {
                        }
                    }
                });
            }

            public void insertUpdate(DocumentEvent e) {
                validateAndClearDiscount();
            }

            public void removeUpdate(DocumentEvent e) {
                validateAndClearDiscount();
            }

            public void changedUpdate(DocumentEvent e) {
                validateAndClearDiscount();
            }
        });
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

jTable1.getSelectionModel().addListSelectionListener(e -> {
    int selectedRow = jTable1.getSelectedRow();
    if (!e.getValueIsAdjusting() && selectedRow >= 0) {
        selectedRowIndex = selectedRow;

        Object titulliObj = model.getValueAt(selectedRow, 1);
        Object cmimiRefObj = model.getValueAt(selectedRow, 2);
        Object cmimiPromoObj = model.getValueAt(selectedRow, 3);

        if (titulliObj != null) TitulliComboBox.setSelectedItem(titulliObj.toString());
        if (cmimiRefObj != null) CmimiRefField.setText(cmimiRefObj.toString());
        if (cmimiPromoObj != null) {
            try {
                double cmimiRef = Double.parseDouble(cmimiRefObj.toString());
                double cmimiPromo = Double.parseDouble(cmimiPromoObj.toString());
                double zbritja = 100.0 * (1 - (cmimiPromo / cmimiRef));
                ZbritjeField.setText(String.format("%.2f", zbritja));
                CmimPromField.setText(String.format("%.2f", cmimiPromo));
            } catch (NumberFormatException ex) {
                ZbritjeField.setText("");
                CmimPromField.setText("");
            }
        }

        
        TitulliComboBox.setEnabled(false);
        CmimiRefField.setEnabled(false);

       
        ZbritjeField.setEnabled(true);
        ZbritjeField.setEditable(true);
        CmimPromField.setEnabled(true);
    }
});

    }
                                    
                                                           

      @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        EmrLabel = new javax.swing.JLabel();
        EmrLabel.setForeground(new Color(51, 102, 153));
        dataFLabel = new javax.swing.JLabel();
        dataFLabel.setForeground(new Color(51, 102, 153));
        DataMLAbel = new javax.swing.JLabel();
        DataMLAbel.setForeground(new Color(51, 102, 153));
        jLabel5 = new javax.swing.JLabel();
        jLabel5.setForeground(new Color(51, 102, 153));
        jLabel6 = new javax.swing.JLabel();
        jLabel6.setForeground(new Color(51, 102, 153));
        jLabel7 = new javax.swing.JLabel();
        jLabel7.setForeground(new Color(51, 102, 153));
        jLabel8 = new javax.swing.JLabel();
        jLabel8.setForeground(new Color(51, 102, 153));
        jLabel9 = new javax.swing.JLabel();
        jLabel9.setForeground(new Color(51, 102, 153));
        EmrField = new javax.swing.JTextField();
        DataFField = new javax.swing.JTextField();
        DataMField = new javax.swing.JTextField();
        TitulliComboBox = new javax.swing.JComboBox<>();
        CmimiRefField = new javax.swing.JTextField();
        CmimPromField = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        FshiBtn = new javax.swing.JButton();
        FshiBtn.setForeground(new Color(51, 102, 153));
        PastroTabeleBtn = new javax.swing.JButton();
        PastroTabeleBtn.setForeground(new Color(51, 102, 153));
        RuajBtn = new javax.swing.JButton();
        RuajBtn.setForeground(new Color(51, 102, 153));
        ShtoBtn = new javax.swing.JButton();
        ShtoBtn.setForeground(new Color(51, 102, 153));
        AnulloBtn = new javax.swing.JButton();
        AnulloBtn.setForeground(new Color(51, 102, 153));
        PastroBtn = new javax.swing.JButton();
        PastroBtn.setForeground(new Color(51, 102, 153));
        ZbritjeField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        EmrLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        EmrLabel.setText("Emri i Promocionit:");

        dataFLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        dataFLabel.setText("Data e Fillimit:");

        DataMLAbel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        DataMLAbel.setText("Data e Mbarimit");

        jLabel5.setFont(new java.awt.Font("Serif", 1, 22)); // NOI18N
        jLabel5.setText("➕ Shto Liber ne Promocion:");

        jLabel6.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel6.setText("Titulli i Librit:");

        jLabel7.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel7.setText("Cmimi Reference:");

        jLabel8.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel8.setText("Zbritja (ne %)");

        jLabel9.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel9.setText("Cmimi promocional");

        EmrField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        DataFField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        DataMField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        DataMField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DataMFieldActionPerformed(evt);
            }
        });

        TitulliComboBox.setEditable(true);
        TitulliComboBox.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        TitulliComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Titulli..." }));
        TitulliComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TitulliComboBoxActionPerformed(evt);
            }
        });

        CmimiRefField.setEditable(false);
        CmimiRefField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        CmimPromField.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        CmimPromField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CmimPromFieldActionPerformed(evt);
            }
        });

        jComboBox2.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Leke", "€" }));

        jComboBox3.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Leke", "€" }));

        jComboBox4.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "%" }));

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
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(410, 410, 410)
                        .addComponent(PastroTabeleBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(FshiBtn))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 466, Short.MAX_VALUE)))
                .addGap(20, 20, 20))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(PastroTabeleBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(FshiBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
                .addContainerGap())
        );

        RuajBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        RuajBtn.setText("💾 Ruaj");
        RuajBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuajBtnActionPerformed(evt);
            }
        });

        ShtoBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        ShtoBtn.setText("➕ Shto Liber");
        ShtoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShtoBtnActionPerformed(evt);
            }
        });

        AnulloBtn.setFont(new Font("Serif", Font.BOLD, 18)); // NOI18N
        AnulloBtn.setText("❌ Anullo");
        AnulloBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnulloBtnActionPerformed(evt);
            }
        });

        PastroBtn.setFont(new java.awt.Font("Serif", 1, 16)); // NOI18N
        PastroBtn.setText("🔄 Pastro");
        PastroBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PastroBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(jPanel2, GroupLayout.DEFAULT_SIZE, 1283, Short.MAX_VALUE)
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
        				.addGroup(layout.createSequentialGroup()
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
        								.addComponent(jLabel9))
        							.addGap(53)
        							.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
        								.addGroup(layout.createSequentialGroup()
        									.addComponent(PastroBtn, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
        									.addPreferredGap(ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
        									.addComponent(ShtoBtn))
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
        								.addComponent(TitulliComboBox, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        					.addGap(18)
        					.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE))
        				.addGroup(layout.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(AnulloBtn, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
        					.addGap(36)
        					.addComponent(RuajBtn, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
        					.addGap(11)))
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addGap(26)
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
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
        					.addGap(37)
        					.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
        						.addComponent(ShtoBtn, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        						.addComponent(PastroBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        				.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(RuajBtn, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
        				.addComponent(AnulloBtn, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap())
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>                        

    private void TitulliComboBoxActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
    }                                               

    private void CmimPromFieldActionPerformed(java.awt.event.ActionEvent evt) {                                              
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
    JOptionPane.showMessageDialog(this, "Shkruani emrin e promocionit.", "Gabim", JOptionPane.ERROR_MESSAGE);
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
    
        String kontrolloQuery = "SELECT COUNT(*) FROM promocione WHERE Emri_Promocionit = ?";
        try (PreparedStatement kontrolloStmt = conn.prepareStatement(kontrolloQuery)) {
            kontrolloStmt.setString(1, emriPromocionit);
            try (ResultSet rs = kontrolloStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "Ekziston tashme nje promocion me kete emer.", "Gabim", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

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
                    JOptionPane.showMessageDialog(this, "Promocioni u ruajt me sukses.");
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

    private void DataMFieldActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void AnulloBtnActionPerformed(java.awt.event.ActionEvent evt) {                                          
    dispose();
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
        JOptionPane.showMessageDialog(this, "Fushat e çmimeve duhet të jenë numerike.", "Gabim", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (zbritje > 100) {
        JOptionPane.showMessageDialog(this, "Zbritja nuk mund të jetë më e madhe se 100%.", "Gabim", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (cmimPromo > cmimRef) {
        JOptionPane.showMessageDialog(this, "Çmimi promocional nuk mund të jetë më i madh se çmimi reference.", "Gabim", JOptionPane.ERROR_MESSAGE);
        return;
    }
int libriId = -1;
double cmimBlerje = 0;

try (Connection conn = DBConnection.getConnection();
     PreparedStatement ps = conn.prepareStatement("SELECT ID_Libri, Cmim_Blerje FROM librat WHERE Titulli = ?")) {

    ps.setString(1, titulli);
    try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            libriId = rs.getInt("ID_Libri");
            cmimBlerje = rs.getDouble("Cmim_Blerje");
        } else {
            JOptionPane.showMessageDialog(this, "Nuk u gjet libri me kete titull ne databaze.", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(this, "Gabim gjate marrjes se ID-se se librit: " + ex.getMessage(), "Gabim", JOptionPane.ERROR_MESSAGE);
    return;
}


if (cmimPromo < cmimBlerje) {
    int option = JOptionPane.showConfirmDialog(this,
        "Çmimi promocional eshte me i vogel se çmimi i blerjes. Deshironi te vazhdoni?",
        "Kujdes", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    if (option != JOptionPane.YES_OPTION) return;
}


    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

    if (selectedRowIndex >= 0) {
        model.setValueAt(libriId, selectedRowIndex, 0);
        model.setValueAt(titulli, selectedRowIndex, 1);
        model.setValueAt(cmimRef, selectedRowIndex, 2);
        model.setValueAt(cmimPromo, selectedRowIndex, 3);
    } else {
       
        boolean exists = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object existingIdObj = model.getValueAt(i, 0);
            if (existingIdObj != null && Integer.parseInt(existingIdObj.toString()) == libriId) {
                exists = true;
                break;
            }
        }

        if (exists) {
            JOptionPane.showMessageDialog(this, "Libri tashme ekziston ne liste.", "Gabim", JOptionPane.ERROR_MESSAGE);
            return;
        }

        model.addRow(new Object[]{libriId, titulli, cmimRef, cmimPromo});
    }

    
    TitulliComboBox.setSelectedItem("");
    CmimiRefField.setText("");
    ZbritjeField.setText("");
    CmimPromField.setText("");

    TitulliComboBox.setEnabled(true);
    CmimiRefField.setEnabled(true);
    ZbritjeField.setEnabled(false);
    jTable1.clearSelection();
    selectedRowIndex = -1;
    TitulliComboBox.requestFocusInWindow();


    }                                       

    private void PastroBtnActionPerformed(java.awt.event.ActionEvent evt) {                                          
    TitulliComboBox.setSelectedItem("");
    CmimiRefField.setText("");
    ZbritjeField.setText("");
    CmimPromField.setText("");
    jTable1.clearSelection();         
    selectedRowIndex = -1;
    TitulliComboBox.requestFocusInWindow();
    }                                         

    private void FshiBtnActionPerformed(java.awt.event.ActionEvent evt) {                                        
     DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow != -1) {
        model.removeRow(selectedRow);
    } else {
        JOptionPane.showMessageDialog(this, "Selektoni nje rresht per te fshire.", "Gabim", JOptionPane.WARNING_MESSAGE);
    }
    }                                       

    private void FshiBtnComponentRemoved(java.awt.event.ContainerEvent evt) {                                         

    }                                        

    private void PastroTabeleBtnActionPerformed(java.awt.event.ActionEvent evt) {                                                
         DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0);
    }                                               

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {                                     

    }                                    

    public static void main(String args[]) {
    
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ShtoPromocion dialog = new ShtoPromocion(new javax.swing.JFrame(), true);
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
