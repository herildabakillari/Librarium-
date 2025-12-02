import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import javax.swing.table.JTableHeader;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;


public class Promocioni extends javax.swing.JFrame {
	private List<String> emratPromo = new ArrayList<>();
	protected AdminPage adminPage; 

	
	public Promocioni(AdminPage adminPage) {
	    this.adminPage = adminPage; 
	    this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
	    initComponents();
	    this.setVisible(true); 
	    setupAutoComplete();
	    shfaqTeGjithaPromocionet();
	}

 
 private void setupAutoComplete() {
    Vector<String> promocionItems = new Vector<>();

    String sql = "SELECT DISTINCT Emri_Promocionit FROM promocione ORDER BY Emri_Promocionit";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {
            String promocioni = rs.getString("Emri_Promocionit");
            if (promocioni != null && !promocioni.isEmpty()) {
                promocionItems.add(promocioni);
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Gabim gjate marrjes se promocioneve: " + ex.getMessage());
    }

    setupAutoComplete(PromocionComboBox, promocionItems);
}
 private void setupAutoComplete(JComboBox<String> comboBox, Vector<String> items) {
    comboBox.setEditable(true);
    JTextField tf = (JTextField) comboBox.getEditor().getEditorComponent();

    tf.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            SwingUtilities.invokeLater(() -> {
                String text = tf.getText();
                comboBox.removeAllItems();

                for (String item : items) {
                    if (item.toLowerCase().contains(text.toLowerCase())) {
                        comboBox.addItem(item);
                    }
                }
                tf.setText(text);
                comboBox.showPopup();
            });
        }
    });
}


private void filtroPromocione(String tipi) {
    DefaultTableModel model = (DefaultTableModel) TabelaPromocionit.getModel();
    model.setRowCount(0); 

    try {
        Connection con = DBConnection.getConnection();
        String query = "SELECT * FROM promocione";
        PreparedStatement pst = con.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        LocalDate sot = LocalDate.now();

        while (rs.next()) {
            LocalDate dataFillimit = rs.getDate("Data_Fillimit").toLocalDate();
            LocalDate dataMbarimit = rs.getDate("Data_Mbarimit").toLocalDate();

            boolean shto = false;

            switch (tipi) {
                case "aktive":
                    shto = (!sot.isBefore(dataFillimit) && !sot.isAfter(dataMbarimit));
                    break;
                case "ardhshme":
                    shto = sot.isBefore(dataFillimit);
                    break;
                case "kaluara":
                    shto = sot.isAfter(dataMbarimit);
                    break;
                case "gjitha":
                    shto = true;
                    break;
            }

            if (shto) {
              model.addRow(new Object[]{
    rs.getInt("ID_Promocioni"),
    rs.getString("Emri_Promocionit"),
    rs.getDate("Data_Fillimit"),
    rs.getDate("Data_Mbarimit"),
    "Shiko Librat"   
});
            }
        }

        con.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


private void resetRadioButtonsExcept(JRadioButton selectedBtn) {
    AktiveRadioBtn.setSelected(AktiveRadioBtn == selectedBtn);
    ArdhmeRadioBtn.setSelected(ArdhmeRadioBtn == selectedBtn);
    KaluaraRadioBtn.setSelected(KaluaraRadioBtn == selectedBtn);
    GjithaRadioBtn1.setSelected(GjithaRadioBtn1 == selectedBtn);
}

private void shfaqTeGjithaPromocionet() {
    filtroPromocione("gjitha");
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        LibratPromocion = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        IDpromocioniField1 = new javax.swing.JTextField();
        DataFField1 = new javax.swing.JTextField();
        DataMField1 = new javax.swing.JTextField();
        EmrPromField1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        EmrPromocioniLabel = new javax.swing.JLabel();
        EmriPromocionLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        AnulloBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelaPromocionit = new javax.swing.JTable();
        ShtoBtn = new javax.swing.JButton();
        ShtoBtn.setForeground(new Color(51, 102, 153));
        ModifikoBtn = new javax.swing.JButton();
        ModifikoBtn.setForeground(new Color(51, 102, 153));
        FshiBtn1 = new javax.swing.JButton();
        FshiBtn1.setForeground(new Color(51, 102, 153));

        LibratPromocion.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                LibratPromocionWindowOpened(evt);
            }
        });

        jPanel6.setBackground(new Color(119, 136, 153));
        jLabel15.setForeground(new Color(51, 102, 153));
        jLabel16.setForeground(new Color(51, 102, 153));
        jLabel17.setForeground(new Color(51, 102, 153));
        jLabel18.setForeground(new Color(51, 102, 153));
        
        jLabel15.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel15.setText("ID Promocioni:");

        jLabel16.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel16.setText("Emri Promocionit:");

        jLabel17.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel17.setText("Data Fillimit:");

        jLabel18.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel18.setText("Data Mbarimit:");

        IDpromocioniField1.setEditable(false);
        IDpromocioniField1.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        DataFField1.setEditable(false);
        DataFField1.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        DataMField1.setEditable(false);
        DataMField1.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        EmrPromField1.setEditable(false);
        EmrPromField1.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(52, 52, 52)
                        .addComponent(DataMField1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(DataFField1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(EmrPromField1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(52, 52, 52)
                        .addComponent(IDpromocioniField1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(IDpromocioniField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(EmrPromField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(DataFField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(DataMField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(119, 136, 153));

        EmrPromocioniLabel.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N

        EmriPromocionLabel.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        EmriPromocionLabel.setForeground(new java.awt.Color(255, 255, 255));
        EmriPromocionLabel.setText("Librat ne Promocion");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(EmriPromocionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EmrPromocioniLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EmriPromocionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(EmrPromocioniLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "ISBN", "Titulli", "Cmimi Reference", "Cmimi Promocional"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setRowHeight(35);
        jScrollPane2.setViewportView(jTable2);


        jLabel2.setFont(new java.awt.Font("Serif", 1, 18)); 
        jLabel2.setForeground(new java.awt.Color(51, 102, 153));
        jLabel2.setText("Librat ne Promocion:");

        javax.swing.GroupLayout LibratPromocionLayout = new javax.swing.GroupLayout(LibratPromocion.getContentPane());
        LibratPromocion.getContentPane().setLayout(LibratPromocionLayout);
        LibratPromocionLayout.setHorizontalGroup(
            LibratPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(LibratPromocionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(LibratPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 950, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LibratPromocionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(AnulloBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
        );
        LibratPromocionLayout.setVerticalGroup(
            LibratPromocionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LibratPromocionLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(AnulloBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      
           
                addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        adminPage.setVisible(true); 
                        dispose(); 
                    }
                });



        jPanel1.setBackground(new Color(119, 136, 153));
        jPanel1.setForeground(new Color(119, 136, 153));

        jLabel1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("✨ Promocionet");

        jLabel9.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel9.setText("🔍 Zgjidh promocionin:");

        jLabel14.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel14.setText("Promocionet:");

        TabelaPromocionit.setFont(new Font("Serif", Font.PLAIN, 12)); // NOI18N
        TabelaPromocionit.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Emri ", "Data e fillimit", "Data e mbarimit", "Librat ne Promocion"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TabelaPromocionit.setRowHeight(35);
        TabelaPromocionit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelaPromocionitMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TabelaPromocionit);

        ShtoBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        ShtoBtn.setText("➕ Shto promocion");
        ShtoBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ShtoBtnMouseClicked(evt);
            }
        });
        ShtoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShtoBtnActionPerformed(evt);
            }
        });

        ModifikoBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        ModifikoBtn.setText(" 🖉 Modifiko promocion");
        ModifikoBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ModifikoBtnMouseClicked(evt);
            }
        });
        ModifikoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifikoBtnActionPerformed(evt);
            }
        });

        FshiBtn1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        FshiBtn1.setText(" ❌ Fshi promocion");
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
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(211, 211, 211));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4Layout.setHorizontalGroup(
        	jPanel4Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel4Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 1273, Short.MAX_VALUE)
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addGap(18)
        					.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
        						.addGroup(jPanel4Layout.createSequentialGroup()
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jLabel14, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE))
        						.addComponent(jLabel9))
        					.addContainerGap(1077, Short.MAX_VALUE))))
        		.addGroup(Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
        			.addContainerGap(553, Short.MAX_VALUE)
        			.addComponent(FshiBtn1, GroupLayout.PREFERRED_SIZE, 231, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(ModifikoBtn, GroupLayout.PREFERRED_SIZE, 231, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(ShtoBtn, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE)
        			.addGap(20))
        		.addComponent(panel, GroupLayout.DEFAULT_SIZE, 1283, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
        	jPanel4Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel4Layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jLabel9)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(panel, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(jLabel14, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 305, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(ShtoBtn, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
        				.addComponent(ModifikoBtn, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
        				.addComponent(FshiBtn1, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE))
        			.addGap(69))
        );
        panel.setLayout(null);
        KerkoBtn = new javax.swing.JButton();
        KerkoBtn.setForeground(new Color(70, 130, 180));
        KerkoBtn.setBounds(486, 8, 128, 40);
        panel.add(KerkoBtn);
        
                KerkoBtn.setBackground(new java.awt.Color(255, 255, 255));
                KerkoBtn.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
                KerkoBtn.setText("🔍 Kerko");
                PromocionComboBox = new javax.swing.JComboBox<>();
                PromocionComboBox.setBounds(0, 8, 476, 40);
                panel.add(PromocionComboBox);
                
                        PromocionComboBox.setEditable(true);
                        PromocionComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Emri i promocionit..." }));
                        AktiveRadioBtn = new javax.swing.JRadioButton();
                        AktiveRadioBtn.setForeground(new Color(51, 102, 153));
                        AktiveRadioBtn.setBackground(new Color(211, 211, 211));
                        AktiveRadioBtn.setBounds(620, 11, 79, 35);
                        panel.add(AktiveRadioBtn);
                        
                                AktiveRadioBtn.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
                                AktiveRadioBtn.setText("Aktive");
                                ArdhmeRadioBtn = new javax.swing.JRadioButton();
                                ArdhmeRadioBtn.setForeground(new Color(51, 102, 153));
                                ArdhmeRadioBtn.setBackground(new Color(211, 211, 211));
                                ArdhmeRadioBtn.setBounds(713, 11, 135, 35);
                                panel.add(ArdhmeRadioBtn);
                                
                                        ArdhmeRadioBtn.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
                                        ArdhmeRadioBtn.setText("Te ardhshme");
                                        KaluaraRadioBtn = new javax.swing.JRadioButton();
                                        KaluaraRadioBtn.setForeground(new Color(51, 102, 153));
                                        KaluaraRadioBtn.setBackground(new Color(211, 211, 211));
                                        KaluaraRadioBtn.setBounds(852, 11, 121, 35);
                                        panel.add(KaluaraRadioBtn);
                                        
                                                KaluaraRadioBtn.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
                                                KaluaraRadioBtn.setText("Te Kaluara");
                                                GjithaRadioBtn1 = new javax.swing.JRadioButton();
                                                GjithaRadioBtn1.setForeground(new Color(51, 102, 153));
                                                GjithaRadioBtn1.setBackground(new Color(211, 211, 211));
                                                GjithaRadioBtn1.setBounds(974, 11, 107, 35);
                                                panel.add(GjithaRadioBtn1);
                                                
                                                        GjithaRadioBtn1.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
                                                        GjithaRadioBtn1.setText("Te Gjitha");
                                                        RifreskoBtn = new javax.swing.JButton();
                                                        RifreskoBtn.setForeground(new Color(51, 102, 153));
                                                        RifreskoBtn.setBounds(1131, 9, 142, 40);
                                                        panel.add(RifreskoBtn);
                                                        
                                                                RifreskoBtn.setBackground(new java.awt.Color(255, 255, 255));
                                                                RifreskoBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
                                                                RifreskoBtn.setText("🔄 Rifresko");
                                                                RifreskoBtn.addActionListener(new java.awt.event.ActionListener() {
                                                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                                        RifreskoBtnActionPerformed(evt);
                                                                    }
                                                                });
                                                        GjithaRadioBtn1.addActionListener(new java.awt.event.ActionListener() {
                                                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                                GjithaRadioBtn1ActionPerformed(evt);
                                                            }
                                                        });
                                                KaluaraRadioBtn.addActionListener(new java.awt.event.ActionListener() {
                                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                        KaluaraRadioBtnActionPerformed(evt);
                                                    }
                                                });
                                        ArdhmeRadioBtn.addActionListener(new java.awt.event.ActionListener() {
                                            public void actionPerformed(java.awt.event.ActionEvent evt) {
                                                ArdhmeRadioBtnActionPerformed(evt);
                                            }
                                        });
                                AktiveRadioBtn.addActionListener(new java.awt.event.ActionListener() {
                                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                                        AktiveRadioBtnActionPerformed(evt);
                                    }
                                });
                KerkoBtn.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        KerkoBtnActionPerformed(evt);
                    }
                });
        jPanel4.setLayout(jPanel4Layout);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, 1283, Short.MAX_VALUE)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addGap(30)
        			.addComponent(jLabel1)
        			.addContainerGap(1130, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jLabel1)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jPanel4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        jPanel1.setLayout(jPanel1Layout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void ArdhmeRadioBtnActionPerformed(java.awt.event.ActionEvent evt) {                                               

    filtroPromocione("ardhshme");
    resetRadioButtonsExcept(ArdhmeRadioBtn);
    }                                              

    private void AktiveRadioBtnActionPerformed(java.awt.event.ActionEvent evt) {                                               
      filtroPromocione("aktive");
    resetRadioButtonsExcept(AktiveRadioBtn);
    }                                              

    private void RifreskoBtnActionPerformed(java.awt.event.ActionEvent evt) {                                            
    filtroPromocione("gjitha");
    resetRadioButtonsExcept(null);
    PromocionComboBox.setSelectedIndex(-1);
    }                                           

    private void ShtoBtnActionPerformed(java.awt.event.ActionEvent evt) {                                        
    ShtoPromocion dialog = new ShtoPromocion(this, true); 
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
    }                                       

    private void ModifikoBtnActionPerformed(java.awt.event.ActionEvent evt) {                                            
                               
    int selectedRow = TabelaPromocionit.getSelectedRow();
    
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Zgjidhni nje promocion per te modifikuar.");
        return;
    }

  
    int idPromocioni = (int) TabelaPromocionit.getValueAt(selectedRow, 0);

   
    ModifikoPromocion dialog = new ModifikoPromocion(this, true, idPromocioni);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);

    
    filtroPromocione("gjitha");

    }                                           

    private void AnulloBtnActionPerformed(java.awt.event.ActionEvent evt) {                                          
        LibratPromocion.dispose();
    }                                         

    private void KerkoBtnActionPerformed(java.awt.event.ActionEvent evt) {                                         
    String emri = (String) PromocionComboBox.getSelectedItem();
    
    if (emri == null || emri.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ju lutem zgjidhni nje emer promocioni.");
        return;
    }

    DefaultTableModel model = (DefaultTableModel) TabelaPromocionit.getModel();
    model.setRowCount(0); 

    try (Connection con = DBConnection.getConnection()) {
        String query = "SELECT * FROM promocione WHERE Emri_Promocionit = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, emri);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("ID_Promocioni"),
                rs.getString("Emri_Promocionit"),
                rs.getDate("Data_Fillimit"),
                rs.getDate("Data_Mbarimit"),
                "Shiko Librat"
            });
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Promocioni nuk ekziston.");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gabim gjate kerkimit: " + e.getMessage());
    }

    }                                        

    private void KaluaraRadioBtnActionPerformed(java.awt.event.ActionEvent evt) {                                                
        
    filtroPromocione("kaluara");
    resetRadioButtonsExcept(KaluaraRadioBtn);

    }                                               

    private void GjithaRadioBtn1ActionPerformed(java.awt.event.ActionEvent evt) {                                                
     
    filtroPromocione("gjitha");
    resetRadioButtonsExcept(GjithaRadioBtn1);

    }                                               

    private void formWindowOpened(java.awt.event.WindowEvent evt) {                                  
           JTableHeader header = TabelaPromocionit.getTableHeader();
    header.setFont(new Font("Serif", Font.BOLD, 20));
    header.setBackground(new Color(60, 63, 65));
    header.setForeground(Color.BLACK);
    }                                 

    private void TabelaPromocionitMouseClicked(java.awt.event.MouseEvent evt) {                                               
    int row = TabelaPromocionit.rowAtPoint(evt.getPoint());
    int col = TabelaPromocionit.columnAtPoint(evt.getPoint());

    if (col == 4 && row != -1) {
        int idPromocioni = (int) TabelaPromocionit.getValueAt(row, 0);

        try (Connection con = DBConnection.getConnection()) {
            
            String queryPromo = "SELECT Emri_Promocionit, Data_Fillimit, Data_Mbarimit FROM promocione WHERE ID_Promocioni = ?";
            PreparedStatement pstPromo = con.prepareStatement(queryPromo);
            pstPromo.setInt(1, idPromocioni);
            ResultSet rsPromo = pstPromo.executeQuery();

            if (rsPromo.next()) {
                IDpromocioniField1.setText(String.valueOf(idPromocioni));
                EmrPromField1.setText(rsPromo.getString("Emri_Promocionit"));
                DataFField1.setText(rsPromo.getDate("Data_Fillimit").toString());
                DataMField1.setText(rsPromo.getDate("Data_Mbarimit").toString());
            }

           
            String queryLibrat = "SELECT l.ID_Libri, l.ISBN, l.Titulli, l.Cmim_Shitje, lp.Cmim_Promocion " +
                                 "FROM librat_promocion lp " +
                                 "JOIN librat l ON lp.ID_Libri = l.ID_Libri " +
                                 "WHERE lp.ID_Promocioni = ?";

            PreparedStatement pstLibra = con.prepareStatement(queryLibrat);
            pstLibra.setInt(1, idPromocioni);
            ResultSet rsLibrat = pstLibra.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jTable2.getModel(); 
            model.setRowCount(0);

            while (rsLibrat.next()) {
                model.addRow(new Object[]{
                    rsLibrat.getInt("ID_Libri"),
                    rsLibrat.getString("ISBN"),
                    rsLibrat.getString("Titulli"),
                    rsLibrat.getDouble("Cmim_Shitje"),
                    rsLibrat.getDouble("Cmim_Promocion")
                });
            }
         LibratPromocion.pack(); 
         LibratPromocion.setLocationRelativeTo(this);
         LibratPromocion.setVisible(true);
          

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gabim gjate ngarkimit: " + e.getMessage());
        }
    }

    }                                              

    private void LibratPromocionWindowOpened(java.awt.event.WindowEvent evt) {                                             
          jTable2.getTableHeader().setFont(new java.awt.Font("Serif", Font.BOLD, 18));
    }                                            

    private void FshiBtn1MouseClicked(java.awt.event.MouseEvent evt) {                                      
        // TODO add your handling code here:
    }                                     

    private void FshiBtn1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
    int selectedRow = TabelaPromocionit.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Zgjidhni nje rresht per te fshire.", "Gabim", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int idPromocioni = (int) TabelaPromocionit.getValueAt(selectedRow, 0); 
    int confirm = JOptionPane.showConfirmDialog(this,
        "A jeni i sigurt qe deshironi ta fshini kete promocion?", "Konfirmo fshirjen",
        JOptionPane.YES_NO_OPTION);

    if (confirm != JOptionPane.YES_OPTION) return;

   
    String sql = "DELETE FROM promocione WHERE ID_Promocioni = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idPromocioni);
        int affected = ps.executeUpdate();
        if (affected > 0) {
        
            DefaultTableModel model = (DefaultTableModel) TabelaPromocionit.getModel();
            model.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Promocioni u fshi me sukses.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Promocioni nuk ekziston.", "Gabim", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Gabim gjate fshirjes: " + ex.getMessage(), "Gabim", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }

    }                                        

    private void ModifikoBtnMouseClicked(java.awt.event.MouseEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    private void ShtoBtnMouseClicked(java.awt.event.MouseEvent evt) {                                     

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
            java.util.logging.Logger.getLogger(Promocioni.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Promocioni.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Promocioni.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Promocioni.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Promocioni().setVisible(true);
            }
        });
    }

    
    // Variables declaration - do not modify                     
    private javax.swing.JRadioButton AktiveRadioBtn;
    private javax.swing.JButton AnulloBtn;
    private javax.swing.JRadioButton ArdhmeRadioBtn;
    private javax.swing.JTextField DataFField1;
    private javax.swing.JTextField DataMField1;
    private javax.swing.JTextField EmrPromField1;
    private javax.swing.JLabel EmrPromocioniLabel;
    private javax.swing.JLabel EmriPromocionLabel;
    private javax.swing.JButton FshiBtn1;
    private javax.swing.JRadioButton GjithaRadioBtn1;
    private javax.swing.JTextField IDpromocioniField1;
    private javax.swing.JRadioButton KaluaraRadioBtn;
    private javax.swing.JButton KerkoBtn;
    private javax.swing.JDialog LibratPromocion;
    private javax.swing.JButton ModifikoBtn;
    private javax.swing.JComboBox<String> PromocionComboBox;
    private javax.swing.JButton RifreskoBtn;
    private javax.swing.JButton ShtoBtn;
    private javax.swing.JTable TabelaPromocionit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
}