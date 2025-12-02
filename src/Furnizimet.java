import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;


public class Furnizimet extends javax.swing.JFrame {
	private AdminPage adminPage;

   private List<String> furnitoret = new ArrayList<>();
   private List<String> librat = new ArrayList<>();

   public Furnizimet(AdminPage adminPage) {
       this.adminPage = adminPage;
       initComponents();
       this.setExtendedState(JFrame.MAXIMIZED_BOTH);
       shfaqTeDhenatNeTabele();

      
       this.addWindowListener(new WindowAdapter() {
           @Override
           public void windowClosing(WindowEvent e) {
               if (adminPage != null) {
                   adminPage.setVisible(true); 
               }
           }
       });

       this.setVisible(true);

       
        shfaqTeDhenatNeTabele(); 
        
       ngaDataSpinner.setModel(new SpinnerDateModel());
deriNeSpinner.setModel(new SpinnerDateModel());

JSpinner.DateEditor ngaEditor = new JSpinner.DateEditor(ngaDataSpinner, "dd/MM/yyyy");
ngaDataSpinner.setEditor(ngaEditor);

JSpinner.DateEditor deriEditor = new JSpinner.DateEditor(deriNeSpinner, "dd/MM/yyyy");
deriNeSpinner.setEditor(deriEditor);
        
    ChooseCB.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String selected = (String) ChooseCB.getSelectedItem();
                if ("Furnitor".equals(selected)) {
                    mbushComboBoxMeFurnitor(SearchCB);
                } else if ("Libri".equals(selected)) {
                    mbushComboBoxMeLibra(SearchCB);
                }
            }
        }
    });
 

    jTable1.addMouseListener(new MouseAdapter() {
    @Override
     public void mouseClicked(MouseEvent e) {
        int row = jTable1.rowAtPoint(e.getPoint());
        int column = jTable1.columnAtPoint(e.getPoint());

        if (column == 3) { 
            Object idObj = jTable1.getValueAt(row, 0); 

            if (idObj != null) {
                int idFurnizimi = Integer.parseInt(idObj.toString());

               FaturFurnizim fatur = new FaturFurnizim(null, true, idFurnizimi);
               fatur.setLocationRelativeTo(null);
               fatur.setVisible(true);
            }
        }
    }
});
 this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setVisible(true);
    }
   
    private void mbushComboBoxMeFurnitor(JComboBox<String> comboBox) {
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

    setupAutoComplete(comboBox, furnitoret);
}

private void mbushComboBoxMeLibra(JComboBox<String> comboBox) {
    librat.clear();
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT Titulli FROM librat ORDER BY Titulli");
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            librat.add(rs.getString("Titulli"));
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    setupAutoComplete(comboBox, librat);
}

private void setupAutoComplete(JComboBox<String> comboBox, List<String> dataList) {
    comboBox.setEditable(true);
    JTextField tf = (JTextField) comboBox.getEditor().getEditorComponent();

    tf.addKeyListener(new KeyAdapter() {
        public void keyReleased(KeyEvent e) {
            String text = tf.getText();
            int caret = tf.getCaretPosition();

            comboBox.hidePopup();
            comboBox.removeAllItems();

            for (String item : dataList) {
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

    comboBox.removeAllItems();
    for (String s : dataList) {
        comboBox.addItem(s);
    }

    comboBox.setSelectedItem(null);
}
private void shfaqTeDhenatNeTabeleMeFiltrim(java.util.Date ngaData, java.util.Date deriNeData) {
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(
             "SELECT f.ID_Furnizimi, fu.Emri_Biznesit AS Furnitori, f.Data_Furnizimit " +
             "FROM furnizimi f " +
             "JOIN furnitoret fu ON f.Furnitori_ID = fu.ID_Furnitori " +
             "WHERE f.Data_Furnizimit BETWEEN ? AND ?")) {

       
        ps.setDate(1, new java.sql.Date(ngaData.getTime()));
        ps.setDate(2, new java.sql.Date(deriNeData.getTime()));

        ResultSet rs = ps.executeQuery();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        while (rs.next()) {
            int id = rs.getInt("ID_Furnizimi");
            String furnitori = rs.getString("Furnitori");
            Date data = rs.getDate("Data_Furnizimit");

            Object[] row = {id, furnitori, data.toString(), "Shiko ketu"};
            model.addRow(row);
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

private void shfaqTeDhenatNeTabele() {
    
    try (Connection conn = DBConnection.getConnection();
     PreparedStatement ps = conn.prepareStatement(
         "SELECT f.ID_Furnizimi, fu.Emri_Biznesit AS Furnitori, f.Data_Furnizimit " +
    "FROM furnizimi f " +
    "JOIN furnitoret fu ON f.Furnitori_ID = fu.ID_Furnitori")) {

    ResultSet rs = ps.executeQuery();
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0); 

    while (rs.next()) {
        int id = rs.getInt("ID_Furnizimi");
        String furnitori = rs.getString("Furnitori");
        Date data = rs.getDate("Data_Furnizimit");

        Object[] row = {id, furnitori, data.toString(), "Shiko ketu"};
        model.addRow(row);
    }

} catch (SQLException ex) {
    ex.printStackTrace();
}
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        SearchCB = new javax.swing.JComboBox<>();
        ChooseCB = new javax.swing.JComboBox<>();
        SearchBtn = new javax.swing.JButton();
        SearchBtn.setForeground(new Color(51, 102, 153));
        ngaDataSpinner = new javax.swing.JSpinner();
        deriNeSpinner = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        RifreskoBtn1 = new javax.swing.JButton();
        RifreskoBtn1.setForeground(new Color(51, 102, 153));
        FiltroBtn2 = new javax.swing.JButton();
        FiltroBtn2.setForeground(new Color(51, 102, 153));
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton1.setForeground(new Color(51, 102, 153));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new Color(119, 136, 153));

        jLabel1.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Furnizimet");

        SearchCB.setEditable(true);
        SearchCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Kerko.." }));

        ChooseCB.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        ChooseCB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Furnitor", "Libri" }));

        SearchBtn.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        SearchBtn.setText("🔍");
        SearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchBtnActionPerformed(evt);
            }
        });

        ngaDataSpinner.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        deriNeSpinner.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nga Data:");

        jLabel3.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Deri ne:");

        RifreskoBtn1.setBackground(new java.awt.Color(255, 255, 255));
        RifreskoBtn1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        RifreskoBtn1.setText("🔄 Rifresko");
        RifreskoBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RifreskoBtn1ActionPerformed(evt);
            }
        });

        FiltroBtn2.setBackground(new java.awt.Color(255, 255, 255));
        FiltroBtn2.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        FiltroBtn2.setText("Filtro");
        FiltroBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FiltroBtn2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(SearchCB, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ChooseCB, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(SearchBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ngaDataSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(882, 882, 882)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(deriNeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addComponent(FiltroBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(RifreskoBtn1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(RifreskoBtn1)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SearchCB, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SearchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ChooseCB, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(deriNeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FiltroBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ngaDataSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        jTable1.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Furnitori", "Data", "Librat"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(55);
        jScrollPane1.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jButton1.setText("Shto Furnizim");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void SearchBtnActionPerformed(java.awt.event.ActionEvent evt) {                                          
  
    }                                         

    private void RifreskoBtn1ActionPerformed(java.awt.event.ActionEvent evt) {                                             
 shfaqTeDhenatNeTabele();    
    }                                            

    private void FiltroBtn2ActionPerformed(java.awt.event.ActionEvent evt) {                                           
        java.util.Date ngaData = (java.util.Date) ngaDataSpinner.getValue();
java.util.Date deriNeData = (java.util.Date) deriNeSpinner.getValue();
shfaqTeDhenatNeTabeleMeFiltrim(ngaData, deriNeData);
    }                                          

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
ShtoFurnizim dialog = new ShtoFurnizim(this, true);
dialog.setSize(this.getSize());
dialog.setLocationRelativeTo(null); 
dialog.setVisible(true);

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
            java.util.logging.Logger.getLogger(Furnizimet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Furnizimet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Furnizimet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Furnizimet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Furnizimet().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JComboBox<String> ChooseCB;
    private javax.swing.JButton FiltroBtn2;
    private javax.swing.JButton RifreskoBtn1;
    private javax.swing.JButton SearchBtn;
    private javax.swing.JComboBox<String> SearchCB;
    private javax.swing.JSpinner deriNeSpinner;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JSpinner ngaDataSpinner;
    // End of variables declaration                   
}