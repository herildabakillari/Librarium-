import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;

public class Mbylljet extends javax.swing.JDialog {

    public static Mbylljet instance;
    public Mbylljet(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        instance = this;
        initComponents();
        setLocationRelativeTo(parent);
        loadArkatIntoTable(); 
    }

  
    public List<Object[]> getArkat() {
        List<Object[]> data = new ArrayList<>();
        String query = 
            "SELECT s.ID_Shitesi, s.Emri, " +
            "       COALESCE(SUM(f.cmimi_total), 0) AS totali_arka, " +
            "       (SELECT COUNT(*) FROM mbyllje_arka ma WHERE ma.id_shitesi = s.ID_Shitesi) AS ka_mbyllje " +
            "FROM shites s " +
            "LEFT JOIN faturat f ON s.ID_Shitesi = f.id_shitesi AND f.status <> 'Anulluar' " +
            "GROUP BY s.ID_Shitesi, s.Emri " +
            "ORDER BY s.ID_Shitesi";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                double totali = rs.getDouble("totali_arka");
                boolean mbyllur = rs.getInt("ka_mbyllje") > 0;

                if(mbyllur) totali = 0.0; 

                data.add(new Object[]{
                    rs.getInt("ID_Shitesi"),
                    rs.getString("Emri"),
                    totali
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gabim gjate ngarkimit te arkave:\n" + e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

    
    private void shenoDataMbylljes(int idShitesi, java.util.Date data) {
        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = DBConnection.getConnection();

            
            PreparedStatement pstCheck = conn.prepareStatement(
                "SELECT COUNT(*) FROM mbyllje_arka WHERE id_shitesi = ?"
            );
            pstCheck.setInt(1, idShitesi);
            ResultSet rsCheck = pstCheck.executeQuery();
            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                
                rsCheck.close();
                pstCheck.close();
                return;
            }
            rsCheck.close();
            pstCheck.close();

            String sql = "INSERT INTO mbyllje_arka (id_shitesi, data_mbylljes) VALUES (?, ?)";
            pst = conn.prepareStatement(sql);

            pst.setInt(1, idShitesi);
            pst.setDate(2, new java.sql.Date(data.getTime()));

            pst.executeUpdate();

           
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gabim gjate ruajtjes: " + e.getMessage());
        } finally {
            try {
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    
    public void loadArkatIntoTable() {
        List<Object[]> arkat = getArkat();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); 

        for(Object[] row : arkat) {
            model.addRow(row);
        }
    }

    
    private void mbyllArkat() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "A deshironi te mbyllni arken? ",
            "Konfirmo Mbylljen",
            JOptionPane.YES_NO_OPTION
        );

        if(confirm != JOptionPane.YES_OPTION) return;

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        java.util.Date sot = new java.util.Date();

        for(int i = 0; i < model.getRowCount(); i++) {
            int idShitesi = (int) model.getValueAt(i, 0);
            shenoDataMbylljes(idShitesi, sot);
        }

        loadArkatIntoTable(); 
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        jButton1 = new JButton();
        jButton1.setForeground(new Color(51, 102, 153));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new Color(119, 136, 153));

        jLabel1.setFont(new java.awt.Font("Serif", 1, 20));
        jLabel1.setForeground(new Color(255, 255, 255));
        jLabel1.setText("Mbyllja e Arkave");

        jTable1.setModel(new DefaultTableModel(
            new Object [][] {},
            new String [] { "ID Shitesi", "Emri", "Totali i Arkes" }
        ) {
            boolean[] canEdit = new boolean [] { false, false, false };
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(30);
        jScrollPane1.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("Serif", 1, 18));
        jButton1.setText("Mbyll Arkat");
        jButton1.addActionListener(evt -> mbyllArkat());

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 648, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10,10,10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10,10,10))
        );

        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            Mbylljet dialog = new Mbylljet(new JFrame(), true);
            dialog.setVisible(true);
        });
    }

    // Variables declaration
    private JButton jButton1;
    private JLabel jLabel1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTable jTable1;
}
