import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatistikaShitesit extends JFrame {

    private JTable tableShitesit, tableProdukte;
    private JButton btnRefresh;
    private Connection conn;
    private JSpinner startDateSpinner, endDateSpinner;
    private JLabel lblBestShitesi;
    private AdminPage adminPage; 


    public StatistikaShitesit(AdminPage adminPage) {
        this.adminPage = adminPage;
        setExtendedState(JFrame.MAXIMIZED_BOTH); 

        setTitle("Statistika Shitesish - Librarium");
        setSize(1000, 600);
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

        connectDB();
        initUI();
        loadShitesitData();
        loadBestShitesi();
    }

    
    private void connectDB() {
        try {
            String url = "jdbc:mysql://localhost:3306/librarium"; 
            String user = "root";
            String pass = "";
            conn = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lidhja me databazen deshtoi!");
            System.exit(1);
        }
    }

    private void initUI() {
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        topPanel.setBorder(new EmptyBorder(10,10,10,10));
        topPanel.setBackground(new Color(245,245,245)); 

        JLabel label = new JLabel("Nga data:");
        label.setForeground(new Color(51, 102, 153));
        label.setFont(new Font("Serif", Font.BOLD, 16));
        topPanel.add(label);
        startDateSpinner = new JSpinner(new SpinnerDateModel());
        startDateSpinner.setFont(new Font("Serif", Font.PLAIN, 16));
        startDateSpinner.setEditor(new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd"));
        topPanel.add(startDateSpinner);

        JLabel label_1 = new JLabel("Deri ne:");
        label_1.setForeground(new Color(51, 102, 153));
        label_1.setFont(new Font("Serif", Font.BOLD, 16));
        topPanel.add(label_1);
        endDateSpinner = new JSpinner(new SpinnerDateModel());
        endDateSpinner.setFont(new Font("Serif", Font.PLAIN, 16));
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd"));
        topPanel.add(endDateSpinner);

        btnRefresh = new JButton("Refresh");
        btnRefresh.setForeground(new Color(51, 102, 153));
        btnRefresh.setFont(new Font("Serif", Font.BOLD, 16));
        btnRefresh.setBackground(new Color(200,200,200));
        topPanel.add(btnRefresh);

        lblBestShitesi = new JLabel("Shitesi i javes: -");
        lblBestShitesi.setFont(new Font("Serif", Font.BOLD, 18));
        lblBestShitesi.setForeground(new Color(178, 34, 34));
        lblBestShitesi.setBorder(new EmptyBorder(0, 20, 0, 0));
        topPanel.add(lblBestShitesi);

        getContentPane().add(topPanel, BorderLayout.NORTH);

        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.5);

        tableShitesit = new JTable();
        tableShitesit.setFillsViewportHeight(true);
        tableShitesit.setBackground(new Color(230, 230, 230)); 
        tableShitesit.setGridColor(Color.DARK_GRAY);
        tableShitesit.setRowHeight(25);
        tableShitesit.getTableHeader().setBackground(new Color(200,200,200));
        tableShitesit.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane spShites = new JScrollPane(tableShitesit);
        splitPane.setTopComponent(spShites);

        tableProdukte = new JTable();
        tableProdukte.setFont(new Font("Serif", Font.PLAIN, 16));
        tableProdukte.setFillsViewportHeight(true);
        tableProdukte.setBackground(new Color(245,245,245));
        tableProdukte.setGridColor(Color.GRAY);
        tableProdukte.setRowHeight(25);
        tableProdukte.getTableHeader().setBackground(new Color(200,200,200));
        tableProdukte.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane spProdukte = new JScrollPane(tableProdukte);
        splitPane.setBottomComponent(spProdukte);

        getContentPane().add(splitPane, BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> {
            loadShitesitData();
            loadBestShitesi();
        });

        tableShitesit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tableShitesit.getSelectedRow();
                if (row != -1) {
                    int idShitesi = (int) tableShitesit.getValueAt(row, 0);
                    loadProduktePerShites(idShitesi);
                }
            }
        });
    }

    private String getStartDate() {
        Date d = (Date) startDateSpinner.getValue();
        return new SimpleDateFormat("yyyy-MM-dd").format(d);
    }

    private String getEndDate() {
        Date d = (Date) endDateSpinner.getValue();
        return new SimpleDateFormat("yyyy-MM-dd").format(d);
    }

    private void loadShitesitData() {
        try {
            String sql = "SELECT s.ID_Shitesi, s.Emri, s.Mbiemri, " +
                    "COUNT(f.id_fature) AS numShitjesh, " +
                    "SUM(CASE WHEN f.anullime=1 THEN -f.cmimi_total ELSE f.cmimi_total END) AS xhiroTotale, " +
                    "COUNT(DISTINCT f.id_produkti) AS numProdukte " +
                    "FROM shites s " +
                    "LEFT JOIN faturat f ON s.ID_Shitesi=f.id_shitesi AND f.data BETWEEN ? AND ? " +
                    "GROUP BY s.ID_Shitesi, s.Emri, s.Mbiemri " +
                    "ORDER BY xhiroTotale DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, getStartDate());
            ps.setString(2, getEndDate());
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"ID", "Emri", "Mbiemri", "Num Shitjesh", "Xhiro Totale", "Num Produkte"});

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ID_Shitesi"),
                        rs.getString("Emri"),
                        rs.getString("Mbiemri"),
                        rs.getInt("numShitjesh"),
                        rs.getDouble("xhiroTotale"),
                        rs.getInt("numProdukte")
                });
            }

            tableShitesit.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadProduktePerShites(int idShitesi) {
        try {
            String sql = "SELECT f.emer_produkti, SUM(f.sasia) AS sasia, " +
                    "SUM(CASE WHEN f.anullime=1 THEN -f.cmimi_total ELSE f.cmimi_total END) AS xhiro " +
                    "FROM faturat f WHERE f.id_shitesi=? AND f.data BETWEEN ? AND ? " +
                    "GROUP BY f.emer_produkti";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idShitesi);
            ps.setString(2, getStartDate());
            ps.setString(3, getEndDate());
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"Produkt", "Sasia e Shitur", "Xhiro"});

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("emer_produkti"),
                        rs.getInt("sasia"),
                        rs.getDouble("xhiro")
                });
            }

            tableProdukte.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBestShitesi() {
        try {
            String sql = "SELECT s.Emri, s.Mbiemri, SUM(CASE WHEN f.anullime=1 THEN -f.cmimi_total ELSE f.cmimi_total END) AS xhiro " +
                    "FROM shites s " +
                    "JOIN faturat f ON s.ID_Shitesi=f.id_shitesi " +
                    "WHERE YEARWEEK(f.data, 1) = YEARWEEK(CURDATE(), 1) " +
                    "GROUP BY s.ID_Shitesi ORDER BY xhiro DESC LIMIT 1";

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                lblBestShitesi.setText("<html><b>Shitesi i javes:</b> " + rs.getString("Emri") + " " + rs.getString("Mbiemri") +
                        " | Xhiro: " + rs.getDouble("xhiro") + "</html>");
                lblBestShitesi.setOpaque(true);
                lblBestShitesi.setBackground(new Color(0, 102, 204));
                lblBestShitesi.setForeground(Color.WHITE);
                lblBestShitesi.setBorder(new EmptyBorder(5,10,5,10));
                lblBestShitesi.setFont(new Font("Arial", Font.BOLD, 14));
            } else {
                lblBestShitesi.setText("Shitesi i javes: -");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
