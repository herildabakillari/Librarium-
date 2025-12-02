import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

public class Fatura extends JFrame {
    private JPanel contentPane;
    private JTable table;
    private JButton btnMbyll, btnFiltro, btnArka;
    private AdminPage adminPage;
    private JSpinner spinnerNgaData, spinnerDeriData;

    double totaliArka = 0; 

    public Fatura(AdminPage adminPage) {
        this.adminPage = adminPage;
        setExtendedState(JFrame.MAXIMIZED_BOTH); 

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1300, 750);
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(240,240,240));
        setContentPane(contentPane);

        initUI();
        loadFaturat("", "");
        llogaritTotaliArka(); 
    }
    public void resetTotaliArka() {
        totaliArka = 0;
        JOptionPane.showMessageDialog(this, "Totali i arkave pas mbylljes: 0 €");
    }


    private void initUI() {
       
        JPanel panel = new JPanel();
        panel.setBackground(new Color(119, 136, 153));
        panel.setBounds(0,0,1283,50);
        panel.setLayout(null);
        contentPane.add(panel);

        JLabel lblTitle = new JLabel("📊 Raporti i Faturave");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 22));
        lblTitle.setBounds(20, 10, 400, 30);
        panel.add(lblTitle);

        
        JLabel lblNga = new JLabel("Nga data:");
        lblNga.setForeground(new Color(51, 102, 153));
        lblNga.setFont(new Font("Serif", Font.BOLD, 16));
        lblNga.setBounds(10, 61, 80, 25);
        contentPane.add(lblNga);

        spinnerNgaData = new JSpinner(new SpinnerDateModel());
        spinnerNgaData.setBounds(88, 61, 120, 25);
        spinnerNgaData.setEditor(new JSpinner.DateEditor(spinnerNgaData, "yyyy-MM-dd"));
        ((JFormattedTextField)spinnerNgaData.getEditor().getComponent(0)).setEditable(false);
        contentPane.add(spinnerNgaData);

        JLabel lblDeri = new JLabel("Deri data:");
        lblDeri.setForeground(new Color(51, 102, 153));
        lblDeri.setFont(new Font("Serif", Font.BOLD, 16));
        lblDeri.setBounds(250, 61, 80, 25);
        contentPane.add(lblDeri);

        spinnerDeriData = new JSpinner(new SpinnerDateModel());
        spinnerDeriData.setBounds(330, 61, 120, 25);
        spinnerDeriData.setEditor(new JSpinner.DateEditor(spinnerDeriData, "yyyy-MM-dd"));
        ((JFormattedTextField)spinnerDeriData.getEditor().getComponent(0)).setEditable(false);
        contentPane.add(spinnerDeriData);

     
        btnFiltro = new JButton("🔍 Filtro");
        btnFiltro.setForeground(new Color(51, 102, 153));
        btnFiltro.setFont(new Font("Serif", Font.BOLD, 16));
        btnFiltro.setBounds(470, 61, 113, 25);
        btnFiltro.setBackground(new Color(200,200,200));
        btnFiltro.setFocusPainted(false);
        contentPane.add(btnFiltro);

       
        table = new JTable();
        table.setFont(new Font("Serif", Font.PLAIN, 15));
        table.setRowHeight(28);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Serif", Font.BOLD, 16));
        header.setBackground(new Color(60,60,60));
        header.setForeground(Color.WHITE);

        table.setBackground(new Color(245,245,245));
        table.setGridColor(new Color(200,200,200));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(0,105,1283,472);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200,200,200), 1, true));
        contentPane.add(scrollPane);

       
        btnMbyll = new JButton(" ❌ Mbyll");
        btnMbyll.setFont(new Font("Serif", Font.BOLD, 18));
        btnMbyll.setBackground(new Color(200,200,200));
        btnMbyll.setForeground(new Color(51, 102, 153));
        btnMbyll.setFocusPainted(false);
        btnMbyll.setBounds(1123, 588, 150, 36);
        contentPane.add(btnMbyll);
        btnMbyll.addActionListener(e -> {
            this.dispose();
            adminPage.setVisible(true);
        });

        
        btnArka = new JButton("💰");
        btnArka.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        btnArka.setFocusPainted(false);
        btnArka.setBackground(new Color(255, 215, 0));
        btnArka.setForeground(Color.BLACK);
        btnArka.setBounds(1162, 61, 80, 36);
        contentPane.add(btnArka);

        btnArka.addActionListener(e -> {
            
            Mbylljet dialog = new Mbylljet(Fatura.this, true);
            dialog.setVisible(true);

            
            totaliArka = 0;
            

            
        });


        
        btnFiltro.addActionListener(e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String ngaData = sdf.format((Date)spinnerNgaData.getValue());
            String deriData = sdf.format((Date)spinnerDeriData.getValue());
            loadFaturat(ngaData, deriData);
        });

        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {

                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, col);
                String status = (String) tbl.getModel().getValueAt(row, 6);

                if (!isSelected) {
                    if ("Anulluar".equalsIgnoreCase(status)) {
                        c.setBackground(new Color(255, 102, 102));
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(row % 2 == 0 ? new Color(230,230,230) : new Color(250,250,250));
                        c.setForeground(Color.BLACK);
                    }
                } else {
                    c.setBackground(new Color(180,200,220));
                }
                return c;
            }
        });
    }

    private void loadFaturat(String ngaData, String deriData) {
        DefaultTableModel model = new DefaultTableModel(
            new Object[][] {},
            new String[] { "Produkt ID", "Titulli", "Sasia", "Çmimi", "Çmimi total", "Data", "Status", "ID Shitesi" }
        );
        table.setModel(model);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id_produkti, emer_produkti, sasia, cmimi, cmimi_total, data, status, id_shitesi " +
                         "FROM faturat " +
                         "WHERE data BETWEEN ? AND ? " +
                         "ORDER BY data DESC, id_fature DESC";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ngaData.isEmpty() ? "1900-01-01" : ngaData);
            ps.setString(2, deriData.isEmpty() ? "2100-12-31" : deriData);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("id_produkti"),
                    rs.getString("emer_produkti"),
                    rs.getInt("sasia"),
                    rs.getDouble("cmimi"),
                    rs.getDouble("cmimi_total"),
                    rs.getDate("data"),
                    rs.getString("status"),
                    rs.getInt("id_shitesi")
                });
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void shfaqTotali() {
        String[] options = {"Totali i gjithe faturave", "Totali per secilin shites"};
        int choice = JOptionPane.showOptionDialog(this, 
                "Zgjidhni menyren e llogaritjes se totalit:", 
                "Totali i Faturave", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.INFORMATION_MESSAGE, 
                null, options, options[0]);

        if (choice == 0) {
            double totalFaturat = 0;
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                totalFaturat += Double.parseDouble(model.getValueAt(i, 4).toString());
            }
            JOptionPane.showMessageDialog(this, "Totali i gjithe faturave: " + totalFaturat + " €");
        } else if (choice == 1) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            java.util.Map<Integer, Double> totalsPerShites = new java.util.HashMap<>();

            for (int i = 0; i < model.getRowCount(); i++) {
                int idShitesi = Integer.parseInt(model.getValueAt(i, 7).toString());
                double cmimiTotal = Double.parseDouble(model.getValueAt(i, 4).toString());
                totalsPerShites.put(idShitesi, totalsPerShites.getOrDefault(idShitesi, 0.0) + cmimiTotal);
            }

            StringBuilder sb = new StringBuilder("Totali per secilin shites:\n");
            for (Integer shites : totalsPerShites.keySet()) {
                sb.append("Shitesi ID ").append(shites).append(": ").append(totalsPerShites.get(shites)).append(" €\n");
            }

            JOptionPane.showMessageDialog(this, sb.toString());
        }
    }

    private void llogaritTotaliArka() {
        totaliArka = 0;
        Date dataMbylljes = getDataMbylljesSeFundit(); 
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT COALESCE(SUM(cmimi_total),0) as totali FROM faturat WHERE status <> 'Anulluar'";
            if (dataMbylljes != null) {
                sql += " AND data > ?";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            if (dataMbylljes != null) {
                ps.setDate(1, new java.sql.Date(dataMbylljes.getTime()));
            }

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                totaliArka = rs.getDouble("totali");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private Date getDataMbylljesSeFundit() {
		// TODO Auto-generated method stub
		return null;
	}
	public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Fatura frame = new Fatura(null); 
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}


