import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Eventet extends javax.swing.JFrame {
   private String roli;
    private boolean eventSaved = false;
    private ButtonGroup eventGroup;
    

    private AdminPage adminPage;

    public Eventet(String roli, AdminPage adminPage) {
        this.roli = roli;
        this.adminPage = adminPage;
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    


        if (!"admin".equalsIgnoreCase(roli)) {
            ShtoEvent.setVisible(false);
            ModifikoEvent.setVisible(false);
            FshiEventBtn.setVisible(false);
        }

        eventGroup = new ButtonGroup();
        eventGroup.add(ArdhmeRadioBtn);
        eventGroup.add(KaluaraRadioBtn);
        eventGroup.add(TeGjithaRadioBtn);

        
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = jTable1.rowAtPoint(evt.getPoint());
                    int col = jTable1.columnAtPoint(evt.getPoint());

                    if (row != -1 && col == 1) {
                        Object value = jTable1.getValueAt(row, col);
                        String pershkrimi = value != null ? value.toString() : "";
                        shfaqDialogPershkrimi(pershkrimi);
                    }
                }
            }
        });

        ngarkoEventet();
        setupAutoCompleteForLibriPromovuar();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        KerkoBtn.addActionListener(e -> kerkoEvent());
        RifreskoBtn.addActionListener(e -> {
            ngarkoEventet();
            ((JTextField) SearchComboBox.getEditor().getEditorComponent()).setText("");
            TeGjithaRadioBtn.setSelected(true);
        });

        ArdhmeRadioBtn.addActionListener(e -> filtroEventet(""));
        KaluaraRadioBtn.addActionListener(e -> filtroEventet(""));
        TeGjithaRadioBtn.addActionListener(e -> filtroEventet(""));
    }

     private void shfaqDialogPershkrimi(String pershkrimi) {
     JTextArea area = new JTextArea(pershkrimi);
     area.setWrapStyleWord(true);
     area.setLineWrap(true);
     area.setEditable(false);
     area.setFont(new Font("Serif", Font.PLAIN, 18));
     JScrollPane scroll = new JScrollPane(area);
     scroll.setPreferredSize(new Dimension(400, 200));

    JOptionPane.showMessageDialog(this, scroll, "Pershkrimi i Eventit", JOptionPane.INFORMATION_MESSAGE);
}
    private void ngarkoEventet() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        String sql = "SELECT ID_Eventi, Pershkrimi, Libri_Promovuar, Data_Ora, Vendndodhja FROM eventet ORDER BY Data_Ora";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("ID_Eventi"),
                    rs.getString("Pershkrimi"),
                    rs.getString("Libri_Promovuar"),
                    new SimpleDateFormat("dd/MM/yyyy HH:mm").format(rs.getTimestamp("Data_Ora")),
                    rs.getString("Vendndodhja")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gabim gjate marrjes se eventeve: " + ex.getMessage());
        }
    }
private void kerkoEvent() {
    String kerko = ((JTextField) SearchComboBox.getEditor().getEditorComponent()).getText().trim();

    if (kerko.isEmpty()) {
        ngarkoEventet();
        return;
    }
    

    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0);

    String sql = "SELECT ID_Eventi, Pershkrimi, Libri_Promovuar, Data_Ora, Vendndodhja FROM eventet WHERE Libri_Promovuar LIKE ? ORDER BY Data_Ora";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, "%" + kerko + "%");

        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("ID_Eventi"),
                    rs.getString("Pershkrimi"),
                    rs.getString("Libri_Promovuar"),
                    new SimpleDateFormat("dd/MM/yyyy HH:mm").format(rs.getTimestamp("Data_Ora")),
                    rs.getString("Vendndodhja")
                });
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Gabim gjate kerkimit te eventeve: " + ex.getMessage());
    }
    } 
    private void setupAutoCompleteForLibriPromovuar() {
    Vector<String> libriItems = new Vector<>();

    String sql = "SELECT DISTINCT Libri_Promovuar FROM eventet ORDER BY Libri_Promovuar";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {
            String libri = rs.getString("Libri_Promovuar");
            if (libri != null && !libri.isEmpty()) {
                libriItems.add(libri);
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Gabim gjate marrjes se librave: " + ex.getMessage());
    }

    setupAutoComplete(SearchComboBox, libriItems);

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

                    if (comboBox.getItemCount() > 0) {
                        comboBox.showPopup();
                    } else {
                        comboBox.hidePopup();
                    }

                   
                    filtroEventet(text);
                });
            }

            @Override
            public void keyTyped(KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    if (!comboBox.isPopupVisible() && comboBox.getItemCount() > 0) {
                        comboBox.showPopup();
                    }
                });
            }
        });
    }

    private void filtroEventet(String kerko) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        String baseSql = "SELECT ID_Eventi, Pershkrimi, Libri_Promovuar, Data_Ora, Vendndodhja FROM eventet ";
        String whereSql = "";
        Timestamp now = new Timestamp(System.currentTimeMillis());

        
        if (ArdhmeRadioBtn.isSelected()) {
            whereSql = "WHERE Data_Ora >= ? ";
        } else if (KaluaraRadioBtn.isSelected()) {
            whereSql = "WHERE Data_Ora < ? ";
        } else if (TeGjithaRadioBtn.isSelected()) {
            whereSql = "WHERE 1=1 ";
        }

      
        if (!kerko.isEmpty()) {
            whereSql += "AND Libri_Promovuar LIKE ? ";
        }

        String orderSql = "ORDER BY Data_Ora";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(baseSql + whereSql + orderSql)) {

            int paramIndex = 1;

            
            if (ArdhmeRadioBtn.isSelected() || KaluaraRadioBtn.isSelected()) {
                pst.setTimestamp(paramIndex++, now);
            }

           
            if (!kerko.isEmpty()) {
                pst.setString(paramIndex++, "%" + kerko + "%");
            }

            try (ResultSet rs = pst.executeQuery()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                while (rs.next()) {
                    Timestamp dataOra = rs.getTimestamp("Data_Ora");
                    String dataOraFormatted = (dataOra != null) ? sdf.format(dataOra) : "";

                    model.addRow(new Object[]{
                        rs.getInt("ID_Eventi"),
                        rs.getString("Pershkrimi"),
                        rs.getString("Libri_Promovuar"),
                        dataOraFormatted,
                        rs.getString("Vendndodhja")
                    });
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gabim gjate filtrimit live: " + ex.getMessage());
        }
    }


    private void fshiEvent(int eventId) {
        String sql = "DELETE FROM eventet WHERE ID_Eventi = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, eventId);
            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Eventi u fshi me sukses.");
            } else {
                JOptionPane.showMessageDialog(this, "Gabim: Eventi nuk u gjet ose nuk u fshi.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gabim gjate fshirjes: " + ex.getMessage());
        }
    }

   

    @SuppressWarnings("unchecked")
                            
    private void initComponents() {

        EventDialog = new javax.swing.JDialog();
        VendndodhjaLabel = new javax.swing.JLabel();
        LibriLabel = new javax.swing.JLabel();
        DataOraField = new javax.swing.JTextField();
        RuajBtn = new javax.swing.JButton();
        VendndodhjaField = new javax.swing.JTextField();
        AnulloBtn = new javax.swing.JButton();
        LibriField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        PershkrimiLabel = new javax.swing.JLabel();
        DataOraLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        PershkrimiArea = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel11.setForeground(new Color(255, 255, 255));
        jPanel3 = new javax.swing.JPanel();
        jPanel3.setForeground(new Color(211, 211, 211));
        jLabel12 = new javax.swing.JLabel();
        KerkoBtn = new javax.swing.JButton();
        KerkoBtn.setBackground(new Color(255, 255, 255));
        KerkoBtn.setForeground(new Color(51, 102, 153));
        SearchComboBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        FshiEventBtn = new javax.swing.JButton();
        FshiEventBtn.setBackground(new Color(255, 255, 255));
        FshiEventBtn.setForeground(new Color(51, 102, 153));
        ModifikoEvent = new javax.swing.JButton();
        ModifikoEvent.setBackground(new Color(255, 255, 255));
        ModifikoEvent.setForeground(new Color(51, 102, 153));
        ShtoEvent = new javax.swing.JButton();
        ShtoEvent.setBackground(new Color(255, 255, 255));
        ShtoEvent.setForeground(new Color(51, 102, 153));

        VendndodhjaLabel.setFont(new java.awt.Font("Serif", 1, 20)); 
        VendndodhjaLabel.setForeground(new java.awt.Color(51, 102, 153));
        VendndodhjaLabel.setText("Vendndodhja:");

        LibriLabel.setFont(new java.awt.Font("Serif", 1, 20)); 
        LibriLabel.setForeground(new java.awt.Color(51, 102, 153));
        LibriLabel.setText("Libri i Promovuar:");

        DataOraField.setFont(new java.awt.Font("Serif", 0, 18)); 
        DataOraField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DataOraFieldActionPerformed(evt);
            }
        });

        RuajBtn.setFont(new java.awt.Font("Serif", 1, 18));
        RuajBtn.setText("💾 Ruaj");
        RuajBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RuajBtnMouseClicked(evt);
            }
        });
        RuajBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuajBtnActionPerformed(evt);
            }
        });

        VendndodhjaField.setFont(new java.awt.Font("Serif", 0, 18)); 

        AnulloBtn.setFont(new java.awt.Font("Serif", 1, 18)); 
        AnulloBtn.setText("❌ Anullo");
        AnulloBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnulloBtnActionPerformed(evt);
            }
        });

        LibriField.setFont(new java.awt.Font("Serif", 0, 18)); 

        jPanel2.setBackground(new Color(119, 136, 153));
        
        jLabel1.setFont(new java.awt.Font("Serif", 1, 20)); 
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Event i Ri");

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
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PershkrimiLabel.setFont(new java.awt.Font("Serif", 1, 20)); 
        PershkrimiLabel.setForeground(new java.awt.Color(51, 102, 153));
        PershkrimiLabel.setText("Pershkrimi:");

        DataOraLabel.setFont(new java.awt.Font("Serif", 1, 20)); 
        DataOraLabel.setForeground(new java.awt.Color(51, 102, 153));
        DataOraLabel.setText("Data & Ora:");

        PershkrimiArea.setColumns(20);
        PershkrimiArea.setRows(5);
        jScrollPane2.setViewportView(PershkrimiArea);

        javax.swing.GroupLayout EventDialogLayout = new javax.swing.GroupLayout(EventDialog.getContentPane());
        EventDialog.getContentPane().setLayout(EventDialogLayout);
        EventDialogLayout.setHorizontalGroup(
            EventDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EventDialogLayout.createSequentialGroup()
                .addContainerGap(379, Short.MAX_VALUE)
                .addComponent(AnulloBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(RuajBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
            .addGroup(EventDialogLayout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(EventDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PershkrimiLabel)
                    .addComponent(DataOraLabel)
                    .addComponent(VendndodhjaLabel)
                    .addComponent(LibriLabel))
                .addGap(60, 60, 60)
                .addGroup(EventDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(LibriField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                    .addComponent(DataOraField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(VendndodhjaField, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        EventDialogLayout.setVerticalGroup(
            EventDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EventDialogLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84)
                .addGroup(EventDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PershkrimiLabel)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(EventDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EventDialogLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(LibriLabel))
                    .addComponent(LibriField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(EventDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(DataOraLabel)
                    .addComponent(DataOraField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(EventDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(VendndodhjaLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(VendndodhjaField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(EventDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RuajBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnulloBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (adminPage != null) {
                    adminPage.setVisible(true); 
                }
                dispose(); 
            }
        });



        jPanel1.setBackground(new Color(119, 136, 153));

        jLabel11.setFont(new java.awt.Font("Serif", 1, 18)); 
        jLabel11.setText("Eventet e Librit");

        jPanel3.setBackground(new Color(220, 220, 220));

        jLabel12.setFont(new java.awt.Font("Serif", 1, 22)); 
        jLabel12.setForeground(new Color(0, 0, 0));
        jLabel12.setText("Kerko Event");

        KerkoBtn.setFont(new java.awt.Font("Serif", 1, 23)); 
        KerkoBtn.setText("🔍");
        KerkoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KerkoBtnActionPerformed(evt);
            }
        });

        SearchComboBox.setEditable(true);
        SearchComboBox.setFont(new java.awt.Font("Serif", 0, 18));
        SearchComboBox.setToolTipText("Libri i Promovuar..");
       ComboBoxMeLibra(); 
        ArdhmeRadioBtn = new javax.swing.JRadioButton();
        
                ArdhmeRadioBtn.setBackground(new Color(220, 220, 220));
                ArdhmeRadioBtn.setFont(new Font("Serif", Font.BOLD, 18)); 
                ArdhmeRadioBtn.setForeground(new Color(51, 102, 153));
                ArdhmeRadioBtn.setText("Te Ardhshme");
                ArdhmeRadioBtn.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        ArdhmeRadioBtnActionPerformed(evt);
                    }
                });
        KaluaraRadioBtn = new javax.swing.JRadioButton();
        
                KaluaraRadioBtn.setBackground(new Color(220, 220, 220));
                KaluaraRadioBtn.setFont(new Font("Serif", Font.BOLD, 18)); 
                KaluaraRadioBtn.setForeground(new Color(51, 102, 153));
                KaluaraRadioBtn.setText("Te kaluara");
                KaluaraRadioBtn.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        KaluaraRadioBtnActionPerformed(evt);
                    }
                });
        TeGjithaRadioBtn = new javax.swing.JRadioButton();
        
                TeGjithaRadioBtn.setBackground(new Color(220, 220, 220));
                TeGjithaRadioBtn.setFont(new Font("Serif", Font.BOLD, 18)); 
                TeGjithaRadioBtn.setForeground(new Color(51, 102, 153));
                TeGjithaRadioBtn.setText("Te Gjitha");
        RifreskoBtn = new javax.swing.JButton();
        RifreskoBtn.setForeground(new Color(51, 102, 153));
        
                RifreskoBtn.setBackground(new Color(255, 255, 255));
                RifreskoBtn.setFont(new Font("Serif", Font.BOLD, 18));
                RifreskoBtn.setText("🔄 Rifresko");
                RifreskoBtn.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        RifreskoBtnActionPerformed(evt);
                    }
                });
  

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3Layout.setHorizontalGroup(
        	jPanel3Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel3Layout.createSequentialGroup()
        			.addGroup(jPanel3Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(jPanel3Layout.createSequentialGroup()
        					.addGap(20)
        					.addComponent(jLabel12)
        					.addGap(358)
        					.addComponent(ArdhmeRadioBtn)
        					.addGap(31)
        					.addComponent(KaluaraRadioBtn, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(TeGjithaRadioBtn, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
        					.addGap(18)
        					.addComponent(RifreskoBtn, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE))
        				.addGroup(jPanel3Layout.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(SearchComboBox, GroupLayout.PREFERRED_SIZE, 738, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addComponent(KerkoBtn, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap(216, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
        	jPanel3Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel3Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel3Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(ArdhmeRadioBtn, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel12)
        				.addComponent(KaluaraRadioBtn, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
        				.addComponent(TeGjithaRadioBtn)
        				.addComponent(RifreskoBtn, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(jPanel3Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(SearchComboBox, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
        				.addComponent(KerkoBtn, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
        			.addGap(42))
        );
        jPanel3.setLayout(jPanel3Layout);
        
        btnMbyll = new JButton("❌ ");
        

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1Layout.setHorizontalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addGroup(jPanel1Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(jPanel1Layout.createSequentialGroup()
        					.addGap(21)
        					.addComponent(jLabel11))
        				.addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, 1293, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
        	jPanel1Layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(jPanel1Layout.createSequentialGroup()
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addComponent(jLabel11)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap())
        );
        jPanel1.setLayout(jPanel1Layout);

        jTable1.setFont(new Font("Serif", Font.PLAIN, 19)); 
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Pershkrimi", "Libri i Promovuar", "Data & Ora", "Vendndodhja"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(35);
        jScrollPane1.setViewportView(jTable1);

        FshiEventBtn.setFont(new java.awt.Font("Serif", 1, 20)); 
        FshiEventBtn.setText("❌ Fshi Event");
        FshiEventBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FshiEventBtnActionPerformed(evt);
            }
        });

        ModifikoEvent.setFont(new java.awt.Font("Serif", 1, 20)); 
        ModifikoEvent.setText("🖉 Modifiko Event");
        ModifikoEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifikoEventActionPerformed(evt);
            }
        });

        ShtoEvent.setFont(new java.awt.Font("Serif", 1, 20)); 
        ShtoEvent.setText("➕ Shto event");
        ShtoEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShtoEventActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap(726, Short.MAX_VALUE)
        			.addComponent(ShtoEvent, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
        			.addGap(14)
        			.addComponent(FshiEventBtn, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(ModifikoEvent)
        			.addGap(28))
        		.addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        		.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 1303, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 390, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(ModifikoEvent, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
        				.addComponent(ShtoEvent, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
        				.addComponent(FshiEventBtn, GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
        			.addGap(64))
        );
        getContentPane().setLayout(layout);

        pack();
    }                       

    private void ArdhmeRadioBtnActionPerformed(java.awt.event.ActionEvent evt) {                                               
       
    }                                              

    private void ShtoEventActionPerformed(java.awt.event.ActionEvent evt) {                                          
   PershkrimiArea.setText("");
    LibriField.setText("");
    VendndodhjaField.setText("");

    String currentDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date());
    DataOraField.setText(currentDateTime);
    DataOraField.setToolTipText("Format: dd/MM/yyyy HH:mm");

 
    DataOraField.setCaretPosition(DataOraField.getText().length());

   
    EventDialog.setModal(true);
    EventDialog.pack();
    EventDialog.setLocationRelativeTo(this);
    EventDialog.setVisible(true);

    }                                         

    private void ModifikoEventActionPerformed(java.awt.event.ActionEvent evt) {                                              
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Zgjidhni nje event per ta modifikuar.");
        return;
    }

    try {
        int eventId = (int) jTable1.getValueAt(selectedRow, 0);
        String pershkrimi = (String) jTable1.getValueAt(selectedRow, 1);
        String libri = (String) jTable1.getValueAt(selectedRow, 2);
        String dataOraSet = (String) jTable1.getValueAt(selectedRow, 3);
        String vendndodhja = (String) jTable1.getValueAt(selectedRow, 4);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        java.util.Date dataOraDate = sdf.parse(dataOraSet);

        ModifikoEvent modifikoDialog = new ModifikoEvent(this, true);
       modifikoDialog.setEventData(eventId, pershkrimi, libri, new java.sql.Timestamp(dataOraDate.getTime()), vendndodhja);
        modifikoDialog.setLocationRelativeTo(this);
        modifikoDialog.setVisible(true);

        ngarkoEventet();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gabim gjate marrjes se dates: " + e.getMessage());
    }
    }                                             

    private void KaluaraRadioBtnActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
    }                                               

    private void RifreskoBtnActionPerformed(java.awt.event.ActionEvent evt) {                                            
        
        ((JTextField) SearchComboBox.getEditor().getEditorComponent()).setText("");
        
        
        TeGjithaRadioBtn.setSelected(true);

        
        filtroEventet(""); 
    }
                                           

    private void DataOraFieldActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }                                            

    private void RuajBtnMouseClicked(java.awt.event.MouseEvent evt) {                                     
  
    }                                    

    private void RuajBtnActionPerformed(java.awt.event.ActionEvent evt) {                                        
String pershkrimi = PershkrimiArea.getText().trim();
    String libri = LibriField.getText().trim();
    String dataOraStr = DataOraField.getText().trim();
    String vendndodhja = VendndodhjaField.getText().trim();

    if (pershkrimi.isEmpty() || libri.isEmpty() || dataOraStr.isEmpty() || vendndodhja.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ju lutem plotesoni te gjitha fushat.");
        return;
    }

    try {
        Timestamp dataOra = new Timestamp(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dataOraStr).getTime());

        
        String checkSql = "SELECT COUNT(*) FROM eventet WHERE Pershkrimi = ? AND Libri_Promovuar = ? AND Data_Ora = ?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, pershkrimi);
            checkStmt.setString(2, libri);
            checkStmt.setTimestamp(3, dataOra);

            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Ky event ekziston.");
                return;
            }
        }

        
        String insertSql = "INSERT INTO eventet (Pershkrimi, Libri_Promovuar, Data_Ora, Vendndodhja) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement pst = conn.prepareStatement(insertSql)) {

            pst.setString(1, pershkrimi);
            pst.setString(2, libri);
            pst.setTimestamp(3, dataOra);
            pst.setString(4, vendndodhja);

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Eventi u ruajt me sukses.");
                EventDialog.dispose(); 
                ngarkoEventet(); 
            }
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Gabim gjate ruajtjes: " + ex.getMessage());
    }
      
    }                                       

    private void FshiEventBtnActionPerformed(java.awt.event.ActionEvent evt) {                                             

    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Zgjidh nje event per te fshire.");
        return;
    }

    int response = JOptionPane.showConfirmDialog(
        this,
        "A jeni i sigurt qe doni ta fshini kete event?",
        "Konfirmim",
        JOptionPane.YES_NO_OPTION
    );

    if (response == JOptionPane.YES_OPTION) {
        int eventId = (int) jTable1.getValueAt(selectedRow, 0);
        fshiEvent(eventId); 
        ngarkoEventet();     
        
    }
    
    }                                            

    private void AnulloBtnActionPerformed(java.awt.event.ActionEvent evt) {                                          
    EventDialog.dispose();
    
    }                                         

    private void formWindowOpened(java.awt.event.WindowEvent evt) {                                  
     JTableHeader header = jTable1.getTableHeader();
    header.setFont(new Font("Serif", Font.BOLD, 20));
    header.setBackground(new Color(60, 63, 65));
    header.setForeground(Color.BLACK);
    }                                 

    private void ComboBoxMeLibra() {
        SearchComboBox.removeAllItems(); 

        String sql = "SELECT DISTINCT Libri_Promovuar FROM eventet ORDER BY Libri_Promovuar";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                SearchComboBox.addItem(rs.getString("Libri_Promovuar"));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gabim gjate ngarkimit te librave: " + ex.getMessage());
        }
    }
                                     

    private void KerkoBtnActionPerformed(java.awt.event.ActionEvent evt) {                                               
        String selected = (String) SearchComboBox.getSelectedItem();
        if (selected != null && !selected.equals("Libri i Promovuar..")) {
            kerkoEvent();  
        }
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
            java.util.logging.Logger.getLogger(Evente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Evente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Evente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Evente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
      

     
        
    }

                       
    private javax.swing.JButton AnulloBtn;
    private javax.swing.JRadioButton ArdhmeRadioBtn;
    private javax.swing.JTextField DataOraField;
    private javax.swing.JLabel DataOraLabel;
    private javax.swing.JDialog EventDialog;
    private javax.swing.JButton FshiEventBtn;
    private javax.swing.JRadioButton KaluaraRadioBtn;
    private javax.swing.JButton KerkoBtn;
    private javax.swing.JTextField LibriField;
    private javax.swing.JLabel LibriLabel;
    private javax.swing.JButton ModifikoEvent;
    private javax.swing.JTextArea PershkrimiArea;
    private javax.swing.JLabel PershkrimiLabel;
    private javax.swing.JButton RifreskoBtn;
    private javax.swing.JButton RuajBtn;
    private javax.swing.JComboBox<String> SearchComboBox;
    private javax.swing.JButton ShtoEvent;
    private javax.swing.JRadioButton TeGjithaRadioBtn;
    private javax.swing.JTextField VendndodhjaField;
    private javax.swing.JLabel VendndodhjaLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private JButton btnMbyll;
    
}