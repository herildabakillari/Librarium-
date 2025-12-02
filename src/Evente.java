import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class Evente extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private JRadioButton rdbtnArdhshme, rdbtnKaluara, rdbtnGjitha;
    private JComboBox<String> SearchComboBox;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Evente frame = new Evente();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Evente() {
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1361, 875);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(220, 220, 220));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(119, 136, 153));
        panel.setBounds(0, 0, 1283, 43);
        contentPane.add(panel);

        JLabel lblEvente = new JLabel("    Eventet e Librit");
        lblEvente.setBounds(10, 0, 300, 43);
        panel.add(lblEvente);
        lblEvente.setForeground(Color.WHITE);
        lblEvente.setFont(new Font("Serif", Font.BOLD, 20));

       
        JPanel panel_3 = new JPanel();
        panel_3.setLayout(null);
        panel_3.setBackground(new Color(220, 220, 220));
        panel_3.setBounds(0, 43, 1283, 117);
        contentPane.add(panel_3);

        JLabel lblKerko = new JLabel("  Kerko Event");
        lblKerko.setForeground(Color.BLACK);
        lblKerko.setFont(new Font("Serif", Font.BOLD, 18));
        lblKerko.setBounds(23, 4, 169, 33);
        panel_3.add(lblKerko);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(220, 220, 220));
        panel_1.setBounds(10, 44, 1262, 62);
        panel_3.add(panel_1);
        panel_1.setLayout(null);

        SearchComboBox = new JComboBox<>();
        SearchComboBox.setToolTipText("Libri i Promovuar..");
        SearchComboBox.setFont(new Font("Serif", Font.PLAIN, 18));
        SearchComboBox.setEditable(true);
        SearchComboBox.setBounds(0, 11, 758, 48);
        panel_1.add(SearchComboBox);

        JButton KerkoBtn = new JButton("🔍");
        KerkoBtn.setBackground(Color.WHITE);
        KerkoBtn.setForeground(new Color(51, 102, 153));
        KerkoBtn.setFont(new Font("Serif", Font.BOLD, 23));
        KerkoBtn.setBounds(783, 14, 83, 39);
        panel_1.add(KerkoBtn);

        
        ButtonGroup group = new ButtonGroup();

        rdbtnArdhshme = new JRadioButton("  Te ardhshme");
        rdbtnArdhshme.setForeground(new Color(51, 102, 153));
        rdbtnArdhshme.setBounds(510, 0, 131, 43);
        panel_3.add(rdbtnArdhshme);
        rdbtnArdhshme.setFont(new Font("Serif", Font.BOLD, 16));
        rdbtnArdhshme.setBackground(new Color(220, 220, 220));
        group.add(rdbtnArdhshme);

        rdbtnKaluara = new JRadioButton("  Te kaluara");
        rdbtnKaluara.setForeground(new Color(51, 102, 153));
        rdbtnKaluara.setBounds(659, 0, 131, 43);
        panel_3.add(rdbtnKaluara);
        rdbtnKaluara.setFont(new Font("Serif", Font.BOLD, 16));
        rdbtnKaluara.setBackground(new Color(220, 220, 220));
        group.add(rdbtnKaluara);

        rdbtnGjitha = new JRadioButton("  Te gjitha");
        rdbtnGjitha.setForeground(new Color(51, 102, 153));
        rdbtnGjitha.setBounds(809, 0, 131, 43);
        panel_3.add(rdbtnGjitha);
        rdbtnGjitha.setFont(new Font("Serif", Font.BOLD, 16));
        rdbtnGjitha.setBackground(new Color(220, 220, 220));
        group.add(rdbtnGjitha);
        rdbtnGjitha.setSelected(true);

        JButton btnRifresko = new JButton("🔄 Rifresko");
        btnRifresko.setForeground(new Color(51, 102, 153));
        btnRifresko.setBounds(1012, 4, 150, 35);
        panel_3.add(btnRifresko);
        btnRifresko.setBackground(Color.WHITE);
        btnRifresko.setFont(new Font("Serif", Font.BOLD, 16));

        
        JPanel panel_2 = new JPanel();
        panel_2.setBounds(-10, 171, 1283, 548);
        contentPane.add(panel_2);
        panel_2.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 0, 1263, 417);
        panel_2.add(scrollPane);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Pershkrimi", "Data & Ora", "Vendndodhja", "Libri i promovuar"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Serif", Font.PLAIN, 18));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Serif", Font.BOLD, 18));
        scrollPane.setViewportView(table);

        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());
                    if (row != -1 && col == 1) { 
                        int modelRow = table.convertRowIndexToModel(row);
                        Object pershkrimiObj = tableModel.getValueAt(modelRow, 1);
                        if (pershkrimiObj != null) {
                            String pershkrimi = pershkrimiObj.toString();
                            shfaqDialogPershkrimi(pershkrimi);
                        }
                    }
                }
            }
        });


        loadBooks(); 
        loadEvents(""); 

       
        KerkoBtn.addActionListener(e -> {
            String searchText = ((String) SearchComboBox.getEditor().getItem()).trim();
            loadEvents(searchText);
        });

        
        btnRifresko.addActionListener(e -> {
            SearchComboBox.setSelectedItem("");
            rdbtnGjitha.setSelected(true);
            loadEvents("");
        });

        
        ActionListener filterListener = e -> loadEvents(((String) SearchComboBox.getEditor().getItem()).trim());
        rdbtnGjitha.addActionListener(filterListener);
        rdbtnKaluara.addActionListener(filterListener);
        rdbtnArdhshme.addActionListener(filterListener);

        
        JButton btnMbyll = new JButton("Mbyll");
        btnMbyll.setForeground(new Color(51, 102, 153));
        btnMbyll.addActionListener(e -> dispose());
        btnMbyll.setFont(new Font("Serif", Font.BOLD, 18));
        btnMbyll.setBackground(new Color(245, 245, 245));
        btnMbyll.setBounds(1031, 428, 150, 42);
        panel_2.add(btnMbyll);
    }

    private void loadBooks() {
        try (Connection conn = DBConnection.getConnection()) {
            SearchComboBox.removeAllItems();
            PreparedStatement pst = conn.prepareStatement("SELECT DISTINCT Libri_Promovuar FROM eventet ORDER BY Libri_Promovuar");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                SearchComboBox.addItem(rs.getString("Libri_Promovuar"));
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gabim ne marrjen e librave nga DB: " + ex.getMessage());
        }
    }

    private void loadEvents(String searchText) {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ID_Eventi, Pershkrimi, Data_Ora, Vendndodhja, Libri_Promovuar FROM eventet WHERE 1=1";

            if (rdbtnArdhshme.isSelected()) {
                sql += " AND Data_Ora >= NOW()";
            } else if (rdbtnKaluara.isSelected()) {
                sql += " AND Data_Ora < NOW()";
            }

            if (searchText != null && !searchText.isEmpty()) {
                sql += " AND Libri_Promovuar LIKE ?";
            }

            PreparedStatement pst = conn.prepareStatement(sql);
            if (searchText != null && !searchText.isEmpty()) {
                pst.setString(1, "%" + searchText + "%");
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("ID_Eventi"));
                row.add(rs.getString("Pershkrimi"));
                row.add(rs.getTimestamp("Data_Ora"));
                row.add(rs.getString("Vendndodhja"));
                row.add(rs.getString("Libri_Promovuar"));
                tableModel.addRow(row);
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gabim ne marrjen e te dhenave nga DB: " + ex.getMessage());
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
        JOptionPane.showMessageDialog(this, scroll, "Pershkrimi i Eventit", JOptionPane.INFORMATION_MESSAGE);
    }
}



