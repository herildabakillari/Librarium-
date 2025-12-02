import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class AnulloFaturen extends JFrame {
    private static final long serialVersionUID = 1L;

    private PJesaPare pjesaPare;
    private int idShitesi;

    private JPanel contentPane;
    private JTextField textField;
    private JComboBox<Integer> comboBoxKlienti;
    private JComboBox<Integer> comboBoxFature;
    private JComboBox<Integer> comboBoxLibri;
    private JTable artikujTable;

    private JSpinner spinnerSasia; 

    private JLabel lblProdId;
    private JLabel lblTitulli;
    private JLabel lblStatusi;

    public AnulloFaturen(PJesaPare pjesaPare, int idShitesi) {
        this.pjesaPare = pjesaPare;
        this.idShitesi = idShitesi;
        setExtendedState(JFrame.MAXIMIZED_BOTH); 


        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1366, 791);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(119, 136, 153));
        panel.setBounds(0, 0, 1283, 49);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblTitle = new JLabel("  Anullim Fature");
        lblTitle.setForeground(new Color(245, 245, 245));
        lblTitle.setFont(new Font("Serif", Font.BOLD, 18));
        lblTitle.setBounds(10, 11, 292, 38);
        panel.add(lblTitle);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(220, 220, 220));
        panel_1.setBounds(0, 60, 1283, 662);
        contentPane.add(panel_1);
        panel_1.setLayout(null);

        JPanel panel_2 = new JPanel();
        panel_2.setBackground(new Color(211, 211, 211));
        panel_2.setBounds(0, 0, 1273, 73);
        panel_1.add(panel_2);
        panel_2.setLayout(null);

        JLabel lblIdKlienti = new JLabel("  ID Klienti :");
        lblIdKlienti.setForeground(new Color(51, 102, 153));
        lblIdKlienti.setBounds(239, 21, 107, 35);
        lblIdKlienti.setFont(new Font("Serif", Font.BOLD, 18));
        panel_2.add(lblIdKlienti);

        comboBoxKlienti = new JComboBox<>();
        comboBoxKlienti.setEditable(true);
        comboBoxKlienti.setBounds(356, 21, 179, 35);
        comboBoxKlienti.setBackground(new Color(245, 245, 245));
        panel_2.add(comboBoxKlienti);

        JLabel lblIdFature = new JLabel("  ID Fature :");
        lblIdFature.setForeground(new Color(51, 102, 153));
        lblIdFature.setBounds(559, 21, 107, 35);
        lblIdFature.setFont(new Font("Serif", Font.BOLD, 18));
        panel_2.add(lblIdFature);

        comboBoxFature = new JComboBox<>();
        comboBoxFature.setEditable(true);
        comboBoxFature.setBounds(670, 21, 189, 35);
        comboBoxFature.setBackground(new Color(245, 245, 245));
        panel_2.add(comboBoxFature);

        JLabel lblIdProdukti = new JLabel("  ID Produkti :");
        lblIdProdukti.setForeground(new Color(51, 102, 153));
        lblIdProdukti.setFont(new Font("Serif", Font.BOLD, 18));
        lblIdProdukti.setBounds(904, 21, 131, 35);
        panel_2.add(lblIdProdukti);

        comboBoxLibri = new JComboBox<>();
        comboBoxLibri.setEditable(true);
        comboBoxLibri.setBackground(new Color(245, 245, 245));
        comboBoxLibri.setBounds(1041, 21, 179, 35);
        panel_2.add(comboBoxLibri);

        JButton btnLoad = new JButton("🔍");
        btnLoad.setBounds(133, 21, 80, 35);
        panel_2.add(btnLoad);

        JPanel panel_2_1 = new JPanel();
        panel_2_1.setLayout(null);
        panel_2_1.setBackground(new Color(192, 192, 192));
        panel_2_1.setBounds(10, 80, 638, 498);
        panel_1.add(panel_2_1);

        JLabel lblSasiaAnullo = new JLabel("  Sasia per anullim");
        lblSasiaAnullo.setForeground(new Color(51, 102, 153));
        lblSasiaAnullo.setFont(new Font("Serif", Font.BOLD, 18));
        lblSasiaAnullo.setBounds(17, 380, 151, 37);
        panel_2_1.add(lblSasiaAnullo);

        spinnerSasia = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)); 
        spinnerSasia.setBounds(177, 383, 60, 35);
        panel_2_1.add(spinnerSasia);

        lblProdId = new JLabel("  Produkti ID");
        lblProdId.setForeground(new Color(51, 102, 153));
        lblProdId.setFont(new Font("Serif", Font.BOLD, 18));
        lblProdId.setBounds(17, 44, 220, 37);
        panel_2_1.add(lblProdId);

        lblTitulli = new JLabel("  Titulli");
        lblTitulli.setForeground(new Color(51, 102, 153));
        lblTitulli.setFont(new Font("Serif", Font.BOLD, 18));
        lblTitulli.setBounds(17, 121, 525, 37);
        panel_2_1.add(lblTitulli);

        lblStatusi = new JLabel("  Statusi");
        lblStatusi.setForeground(new Color(51, 102, 153));
        lblStatusi.setFont(new Font("Serif", Font.BOLD, 18));
        lblStatusi.setBounds(17, 201, 84, 37);
        panel_2_1.add(lblStatusi);

        JComboBox<String> comboBoxStatus = new JComboBox<>();
        comboBoxStatus.setFont(new Font("Serif", Font.BOLD, 16));
        comboBoxStatus.setModel(new DefaultComboBoxModel<>(new String[] {"  aktive", "  anulluar"}));
        comboBoxStatus.setBackground(new Color(245, 245, 245));
        comboBoxStatus.setBounds(172, 201, 113, 35);
        panel_2_1.add(comboBoxStatus);

        JLabel lblArsye = new JLabel("  Arsye anullimit");
        lblArsye.setForeground(new Color(51, 102, 153));
        lblArsye.setFont(new Font("Serif", Font.BOLD, 18));
        lblArsye.setBounds(17, 281, 141, 37);
        panel_2_1.add(lblArsye);

        textField = new JTextField();
        textField.setBackground(new Color(245, 245, 245));
        textField.setBounds(178, 281, 422, 35);
        panel_2_1.add(textField);
        textField.setColumns(10);

        JButton btnKonfirmo = new JButton(" ✅ Konfirmo ");
        btnKonfirmo.setForeground(new Color(51, 102, 153));
        btnKonfirmo.setFont(new Font("Serif", Font.BOLD, 16));
        btnKonfirmo.setBackground(new Color(248, 248, 255));
        btnKonfirmo.setBounds(385, 446, 211, 35);
        panel_2_1.add(btnKonfirmo);

        JPanel panel_4 = new JPanel();
        panel_4.setBackground(new Color(176, 196, 222));
        panel_4.setBounds(648, 80, 612, 431);
        panel_1.add(panel_4);
        panel_4.setLayout(null);


        artikujTable = new JTable();
        artikujTable.setFont(new Font("Serif", Font.PLAIN, 14)); 
        artikujTable.setRowHeight(24); 
        artikujTable.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] { "Produkt ID", "Titulli", "Sasia", "Çmimi", "Çmimi total", "Data", "Status" }
        ));

     
        artikujTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                
                c.setBackground(row % 2 == 0 ? new Color(245,245,245) : new Color(230,230,230));

                
                if(column == 2 || column == 3 || column == 4) { 
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                return c;
            }
        });

     
        artikujTable.getColumnModel().getColumn(1).setPreferredWidth(250);

        JScrollPane scrollPaneArtikujt = new JScrollPane(artikujTable);
        scrollPaneArtikujt.setBounds(0, 0, 612, 431);
        panel_4.add(scrollPaneArtikujt);


        JButton btnDil = new JButton("Dil");
        btnDil.setForeground(new Color(51, 102, 153));
        btnDil.setFont(new Font("Serif", Font.BOLD, 14));
        btnDil.setBackground(new Color(245, 245, 245));
        btnDil.setBounds(1144, 534, 114, 44);
        panel_1.add(btnDil);
        btnDil.addActionListener(e -> {
            this.dispose();
            if (pjesaPare != null) {
                pjesaPare.setVisible(true);
            }
        });

        loadComboBoxKlienti();
        loadComboBoxFature();
        loadComboBoxLibri();

        btnLoad.addActionListener(e -> loadProduktPanel());

        btnKonfirmo.addActionListener(e -> anulloFature(comboBoxStatus.getSelectedItem().toString().trim()));
    }

    private void loadComboBoxKlienti() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT DISTINCT id_klienti FROM faturat";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                comboBoxKlienti.addItem(rs.getInt("id_klienti"));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadComboBoxFature() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT DISTINCT id_fature FROM faturat";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                comboBoxFature.addItem(rs.getInt("id_fature"));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadComboBoxLibri() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT DISTINCT id_produkti FROM faturat";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                comboBoxLibri.addItem(rs.getInt("id_produkti"));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadProduktPanel() {
        try (Connection conn = DBConnection.getConnection()) {
            int idFature = Integer.parseInt(comboBoxFature.getSelectedItem().toString().trim());
            int idKlienti = Integer.parseInt(comboBoxKlienti.getSelectedItem().toString().trim());
            int idProdukti = Integer.parseInt(comboBoxLibri.getSelectedItem().toString().trim());

            String sql = "SELECT id_produkti, emer_produkti, sasia, cmimi, cmimi_total, data, status, anullime " +
                         "FROM faturat " +
                         "WHERE id_fature = ? AND id_klienti = ? AND id_produkti = ? AND id_shitesi = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idFature);
            ps.setInt(2, idKlienti);
            ps.setInt(3, idProdukti);
            ps.setInt(4, idShitesi);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                lblProdId.setText("Produkt ID: " + rs.getInt("id_produkti"));
                lblTitulli.setText("Titulli: " + rs.getString("emer_produkti"));
                lblStatusi.setText("Statusi: " + rs.getString("status"));
                textField.setText(rs.getString("anullime") != null ? rs.getString("anullime") : "");
            } else {
                JOptionPane.showMessageDialog(this, "Nuk u gjet produkti!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gabim gjate ngarkimit te produktit!");
        }
    }
    private void anulloFature(String status) {
        try (Connection conn = DBConnection.getConnection()) {
            int idFature = Integer.parseInt(comboBoxFature.getSelectedItem().toString().trim());
            int idKlienti = Integer.parseInt(comboBoxKlienti.getSelectedItem().toString().trim());
            int idProdukti = Integer.parseInt(comboBoxLibri.getSelectedItem().toString().trim());
            int sasiaAnull = (int) spinnerSasia.getValue(); 

            String arsye = textField.getText().trim();

            
            String sqlSelect = "SELECT sasia, cmimi, cmimi_total, emer_produkti, data " +
                               "FROM faturat WHERE id_fature = ? AND id_produkti = ? AND id_klienti = ?";
            PreparedStatement psSelect = conn.prepareStatement(sqlSelect);
            psSelect.setInt(1, idFature);
            psSelect.setInt(2, idProdukti);
            psSelect.setInt(3, idKlienti);

            ResultSet rs = psSelect.executeQuery();

            if(rs.next()) {
                int sasiaOrigjinale = rs.getInt("sasia");
                double cmimi = rs.getDouble("cmimi");
                double cmimiTotalAnull = cmimi * sasiaAnull;

                if(sasiaAnull > sasiaOrigjinale) {
                    JOptionPane.showMessageDialog(this, "Sasia per anullim nuk mund te jete me e madhe se sasia ekzistuese!");
                    return;
                }

                
                String sqlInsertAnull = "INSERT INTO faturat (id_klienti, data, sasia, cmimi, cmimi_total, id_produkti, emer_produkti, id_shitesi, status, anullime) " +
                                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement psInsert = conn.prepareStatement(sqlInsertAnull)) {
                    psInsert.setInt(1, idKlienti);
                    psInsert.setDate(2, new java.sql.Date(System.currentTimeMillis())); 
                    psInsert.setInt(3, -sasiaAnull); 
                    psInsert.setDouble(4, cmimi);
                    psInsert.setDouble(5, -cmimiTotalAnull); 
                    psInsert.setInt(6, idProdukti);
                    psInsert.setString(7, rs.getString("emer_produkti"));
                    psInsert.setInt(8, idShitesi);
                    psInsert.setString(9, "Anulluar");
                    psInsert.setString(10, arsye);
                    psInsert.executeUpdate();
                }

               
                DefaultTableModel model = (DefaultTableModel) artikujTable.getModel();
                model.addRow(new Object[] {
                    idProdukti,
                    rs.getString("emer_produkti"),
                    -sasiaAnull,
                    cmimi,
                    -cmimiTotalAnull,
                    new java.util.Date(),
                    "Anulluar"
                });

                JOptionPane.showMessageDialog(this, "Produkti u anullua me sukses!");

            } else {
                JOptionPane.showMessageDialog(this, "Nuk u gjet produkti per te anulluar!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gabim gjate anullimit te produktit!");
        }
    }
}