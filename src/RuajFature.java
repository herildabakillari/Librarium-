import java.awt.EventQueue;
import javax.swing.*;
import java.sql.ResultSet; 
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RuajFature extends JFrame {
    public static RuajFature instance; 
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private static int idShitesi;
    private JTextField txtEmri;         
    private JTextField txtMbiemri;      
    private JTextField txtIdKlienti;    
    private JTextField txtNumriKontakti;
    private JComboBox<String> cmbGjinia;
    private JTable table; 

    
    private JLabel lblTotali;
    private Runnable updateTotal;

   
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
            	RuajFature faturaFrame = new RuajFature(idShitesi); 
            	faturaFrame.setVisible(true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public RuajFature(int idShitesi) {
        this.idShitesi = idShitesi; 
        instance = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1365, 753);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(220, 220, 220));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 


        
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(119, 136, 153));
        panelHeader.setBounds(0, 0, 1283, 39);
        contentPane.add(panelHeader);
        panelHeader.setLayout(null);

        JLabel lblTitulli = new JLabel("Porosi");
        lblTitulli.setForeground(SystemColor.text);
        lblTitulli.setFont(new Font("Serif", Font.BOLD, 18));
        lblTitulli.setBounds(21, 11, 77, 25);
        panelHeader.add(lblTitulli);

        
        JPanel panelBaseData = new JPanel();
        panelBaseData.setBackground(Color.LIGHT_GRAY);
        panelBaseData.setBounds(0, 38, 1283, 48);
        contentPane.add(panelBaseData);
        panelBaseData.setLayout(null);

        JLabel lblBaseData = new JLabel("Te dhenat baze");
        lblBaseData.setForeground(new Color(0, 0, 0));
        lblBaseData.setFont(new Font("Serif", Font.BOLD, 18));
        lblBaseData.setBounds(10, 11, 156, 35);
        panelBaseData.add(lblBaseData);

        
        JLabel lblEmri = new JLabel("Emri :");
        lblEmri.setForeground(new Color(51, 102, 153));
        lblEmri.setFont(new Font("Serif", Font.BOLD, 18));
        lblEmri.setBounds(28, 181, 129, 32);
        contentPane.add(lblEmri);

        txtEmri = new JTextField();
        txtEmri.setBackground(new Color(255, 255, 255));
        txtEmri.setFont(new Font("Serif", Font.BOLD, 16));
        txtEmri.setBounds(28, 213, 170, 30);
        contentPane.add(txtEmri);
        txtEmri.setColumns(10);

        JLabel lblMbiemri = new JLabel("Mbiemri :");
        lblMbiemri.setForeground(new Color(51, 102, 153));
        lblMbiemri.setFont(new Font("Serif", Font.BOLD, 18));
        lblMbiemri.setBounds(250, 181, 129, 32);
        contentPane.add(lblMbiemri);

        txtMbiemri = new JTextField();
        txtMbiemri.setBackground(new Color(255, 255, 255));
        txtMbiemri.setFont(new Font("Serif", Font.BOLD, 16));
        txtMbiemri.setBounds(250, 213, 170, 30);
        contentPane.add(txtMbiemri);
        txtMbiemri.setColumns(10);

        JLabel lblIdKlienti = new JLabel("ID Klienti :");
        lblIdKlienti.setForeground(new Color(51, 102, 153));
        lblIdKlienti.setFont(new Font("Serif", Font.BOLD, 18));
        lblIdKlienti.setBounds(475, 181, 129, 32);
        contentPane.add(lblIdKlienti);

        txtIdKlienti = new JTextField();
        txtIdKlienti.setBackground(new Color(255, 255, 255));
        txtIdKlienti.setBounds(475, 213, 170, 30);
        contentPane.add(txtIdKlienti);
        txtIdKlienti.setColumns(10);
    
        txtIdKlienti.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                String idKlientiStr = txtIdKlienti.getText().trim();
                if (!idKlientiStr.isEmpty()) {
                    try {
                        int idKlienti = Integer.parseInt(idKlientiStr);
                        Connection conn = DBConnection.getConnection();
                        PreparedStatement ps = conn.prepareStatement(
                            "SELECT emri, mbiemri, gjinia, numerKontakti FROM klienti WHERE idKlienti = ?");
                        ps.setInt(1, idKlienti);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            txtEmri.setText(rs.getString("emri"));
                            txtMbiemri.setText(rs.getString("mbiemri"));
                            cmbGjinia.setSelectedItem(rs.getString("gjinia"));
                            txtNumriKontakti.setText(rs.getString("numerKontakti"));
                        } else {
                            
                            txtEmri.setText("");
                            txtMbiemri.setText("");
                            cmbGjinia.setSelectedIndex(0);
                            txtNumriKontakti.setText("");
                        }
                        rs.close();
                        ps.close();
                        conn.close();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "ID Klienti duhet te jete numer valid!");
                        txtIdKlienti.setText("");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                   
                    }
                }
            }
        });

        JLabel lblNumriKontakti = new JLabel("Numer Kontakti :");
        lblNumriKontakti.setForeground(new Color(51, 102, 153));
        lblNumriKontakti.setFont(new Font("Serif", Font.BOLD, 18));
        lblNumriKontakti.setBounds(28, 254, 177, 32);
        contentPane.add(lblNumriKontakti);

        txtNumriKontakti = new JTextField();
        txtNumriKontakti.setBackground(new Color(255, 255, 255));
        txtNumriKontakti.setBounds(28, 297, 170, 30);
        contentPane.add(txtNumriKontakti);
        txtNumriKontakti.setColumns(10);

        JLabel lblGjinia = new JLabel("Gjinia :");
        lblGjinia.setForeground(new Color(51, 102, 153));
        lblGjinia.setFont(new Font("Serif", Font.BOLD, 18));
        lblGjinia.setBounds(250, 254, 151, 32);
        contentPane.add(lblGjinia);

        cmbGjinia = new JComboBox<>();
        cmbGjinia.setBackground(SystemColor.control);
        cmbGjinia.setModel(new DefaultComboBoxModel<>(new String[] { "Femer", "Mashkull" }));
        cmbGjinia.setFont(new Font("Serif", Font.BOLD, 18));
        cmbGjinia.setBounds(250, 297, 170, 30);
        contentPane.add(cmbGjinia);

        
        JPanel panelButtons = new JPanel();
        panelButtons.setBounds(0, 97, 1283, 69);
        contentPane.add(panelButtons);
        panelButtons.setLayout(null);

        JButton btnRuaj = new JButton(" 💾 Ruaj");
        btnRuaj.setForeground(new Color(51, 102, 153));
        btnRuaj.setBounds(10, 14, 183, 41);
        panelButtons.add(btnRuaj);
        btnRuaj.setBackground(new Color(255, 255, 255));
        btnRuaj.setFont(new Font("Serif", Font.BOLD, 18));

        btnRuaj.addActionListener(e -> {
            String emri = txtEmri.getText();
            String mbiemri = txtMbiemri.getText();
            String idKlienti = txtIdKlienti.getText();
            String numriKontakti = txtNumriKontakti.getText();
            String gjinia = (String) cmbGjinia.getSelectedItem();

            if (emri.isEmpty() || mbiemri.isEmpty() || idKlienti.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ju lutem plotesoni te gjitha fushat e domosdoshme (Emri, Mbiemri, ID Klienti).", "Gabim", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ruajKlientNeDB(emri, mbiemri, idKlienti, numriKontakti, gjinia);

            KonfirmimRuajtje konfirmim = new KonfirmimRuajtje(emri, mbiemri, idKlienti, numriKontakti);
            konfirmim.setVisible(true);
        });

        JButton btnMbyll = new JButton(" ❌  Mbyll");
        btnMbyll.setForeground(new Color(51, 102, 153));
        btnMbyll.setBackground(new Color(255, 255, 255));
        btnMbyll.setFont(new Font("Serif", Font.BOLD, 18));
        btnMbyll.setBounds(214, 11, 183, 41);
        panelButtons.add(btnMbyll);
        btnMbyll.addActionListener(e -> dispose());

        JButton btnShtoProdukt = new JButton("✚  Shto Produkt");
        btnShtoProdukt.setForeground(new Color(51, 102, 153));
        btnShtoProdukt.setFont(new Font("Serif", Font.BOLD, 18));
        btnShtoProdukt.setBackground(new Color(255, 255, 255));
        btnShtoProdukt.setBounds(421, 14, 183, 41);
        panelButtons.add(btnShtoProdukt);
        btnShtoProdukt.addActionListener(e -> {
            ZgjidhProdukt zp = new ZgjidhProdukt();
            zp.setVisible(true);
        });

       
        JPanel panel_5 = new JPanel();
        panel_5.setBounds(10, 359, 1273, 221);
        contentPane.add(panel_5);
        panel_5.setLayout(null);

        JPanel panel_7 = new JPanel();
        panel_7.setBounds(0, 0, 1272, 40);
        panel_7.setBackground(new Color(119, 136, 153));
        panel_7.setLayout(null);
        panel_5.add(panel_7);

        JLabel lblArtikujtPorosise = new JLabel(" Artikujt e porosise");
        lblArtikujtPorosise.setForeground(new Color(245, 245, 245));
        lblArtikujtPorosise.setFont(new Font("Serif", Font.BOLD, 18));
        lblArtikujtPorosise.setBounds(10, 5, 253, 30);
        panel_7.add(lblArtikujtPorosise);

      
        table = new JTable();
        table.setFont(new Font("Serif", Font.PLAIN, 16));
        table.setRowHeight(25);

        table.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Produkti ID", "Titulli", "Sasia", "Cmimi", "Cmimi total", "Data"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; 
            }
        });

        JScrollPane scrollPaneArtikujt = new JScrollPane(table);
        scrollPaneArtikujt.setBounds(0, 40, 1272, 181);
        panel_5.add(scrollPaneArtikujt);

      
        lblTotali = new JLabel("Totali: 0.0 €");
        lblTotali.setForeground(new Color(51, 102, 153));
        lblTotali.setFont(new Font("Serif", Font.BOLD, 16));
        lblTotali.setBounds(350, 589, 200, 30);
        contentPane.add(lblTotali);

        JButton btnFshiProdukt = new JButton("Fshi Produkt");
        btnFshiProdukt.setForeground(new Color(51, 102, 153));
        btnFshiProdukt.setFont(new Font("Serif", Font.BOLD, 16));
        btnFshiProdukt.setBackground(new Color(248, 248, 255));
        btnFshiProdukt.setBounds(28, 591, 170, 41);
        contentPane.add(btnFshiProdukt);

       
        updateTotal = () -> {
            double totali = 0;
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                try {
                    totali += Double.parseDouble(model.getValueAt(i, 4).toString());
                } catch (Exception ex) { }
            }
            lblTotali.setText("Totali: " + totali + " €");
        };

     
        btnFshiProdukt.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.removeRow(selectedRow);
                updateTotal.run(); 
            } else {
                JOptionPane.showMessageDialog(this, "Zgjidhni nje produkt per te fshire!");
            }
        });

        
        table.getModel().addTableModelListener(e -> {
            if (e.getType() == javax.swing.event.TableModelEvent.UPDATE && e.getColumn() == 2) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                try {
                    int sasia = Integer.parseInt(model.getValueAt(e.getFirstRow(), 2).toString());
                    double cmimi = Double.parseDouble(model.getValueAt(e.getFirstRow(), 3).toString());
                    model.setValueAt(sasia * cmimi, e.getFirstRow(), 4);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Sasia duhet te jete numer valid!");
                    model.setValueAt(1, e.getFirstRow(), 2);
                }
                updateTotal.run();
            }
        });

       
        JButton btnHapFature = new JButton("  💾 Ruaj");
        btnHapFature.setForeground(new Color(51, 102, 153));
        btnHapFature.setFont(new Font("Serif", Font.BOLD, 16));
        btnHapFature.setBackground(new Color(248, 248, 255));
        btnHapFature.setBounds(1103, 591, 170, 41);
        contentPane.add(btnHapFature);
        btnHapFature.addActionListener(e -> hapFaturen());
    }

  
    public void shtoArtikullNeTabela(Object[] rresht) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.insertRow(0, rresht); 
        updateTotal.run();           
    }
    private void hapFaturen() {
    	String emri = txtEmri.getText().trim();
    	String mbiemri = txtMbiemri.getText().trim();
    	String idKlientiStr = txtIdKlienti.getText().trim();
    	String numriKontakti = txtNumriKontakti.getText().trim();
    	String gjinia = (String) cmbGjinia.getSelectedItem();

    	if (emri.isEmpty() || mbiemri.isEmpty() || idKlientiStr.isEmpty()) {
    	    JOptionPane.showMessageDialog(this, 
    	        "Ju lutem plotesoni te gjitha fushat e domosdoshme (Emri, Mbiemri, ID Klienti).", 
    	        "Gabim", JOptionPane.ERROR_MESSAGE);
    	    return;
    	}

    	int idKlienti;
    	try {
    	    idKlienti = Integer.parseInt(idKlientiStr);
    	} catch (NumberFormatException ex) {
    	    JOptionPane.showMessageDialog(this, "ID Klienti duhet te jete numer valid!");
    	    return;
    	}

    	
    	try (Connection conn = DBConnection.getConnection()) {
    	    PreparedStatement psCheck = conn.prepareStatement("SELECT COUNT(*) FROM klienti WHERE idKlienti = ?");
    	    psCheck.setInt(1, idKlienti);
    	    ResultSet rs = psCheck.executeQuery();
    	    if (rs.next() && rs.getInt(1) == 0) {
    	        PreparedStatement psInsert = conn.prepareStatement(
    	            "INSERT INTO klienti (idKlienti, emri, mbiemri, gjinia, numerKontakti) VALUES (?, ?, ?, ?, ?)");
    	        psInsert.setInt(1, idKlienti);
    	        psInsert.setString(2, emri);
    	        psInsert.setString(3, mbiemri);
    	        psInsert.setString(4, gjinia);
    	        psInsert.setString(5, numriKontakti);
    	        psInsert.executeUpdate();
    	        psInsert.close();
    	    }
    	    rs.close();
    	    psCheck.close();
    	} catch (Exception ex) {
    	    ex.printStackTrace();
    	    return;
    	}

    	try (Connection conn = DBConnection.getConnection()) {
    	    conn.setAutoCommit(false); 

    	    String sqlInsertFatura = "INSERT INTO faturat (id_klienti, data, sasia, cmimi, cmimi_total, id_produkti, emer_produkti, id_shitesi, status, anullime) " +
    	                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    	    PreparedStatement psFatura = conn.prepareStatement(sqlInsertFatura);

    	    String sqlUpdateGjendja = "UPDATE librat SET Gjendje_Dyqan = Gjendje_Dyqan - ? WHERE ID_Libri = ?";
    	    PreparedStatement psUpdateGjendja = conn.prepareStatement(sqlUpdateGjendja);

    	    for (int i = 0; i < table.getRowCount(); i++) {
    	        int idLibri = Integer.parseInt(table.getValueAt(i, 0).toString());
    	        String titulli = table.getValueAt(i, 1).toString();
    	        int sasia = Integer.parseInt(table.getValueAt(i, 2).toString());
    	        double cmimi = Double.parseDouble(table.getValueAt(i, 3).toString());
    	        double cmimiTotal = sasia * cmimi;

    	        
    	        psFatura.setInt(1, idKlienti);
    	        psFatura.setDate(2, new java.sql.Date(System.currentTimeMillis()));
    	        psFatura.setInt(3, sasia);
    	        psFatura.setDouble(4, cmimi);
    	        psFatura.setDouble(5, cmimiTotal);
    	        psFatura.setInt(6, idLibri);
    	        psFatura.setString(7, titulli);
    	        psFatura.setInt(8, idShitesi);
    	        psFatura.setString(9, "aktive");
    	        psFatura.setString(10, "aktive");
    	        psFatura.addBatch();

    
    	        psUpdateGjendja.setInt(1, sasia);
    	        psUpdateGjendja.setInt(2, idLibri);
    	        psUpdateGjendja.addBatch();
    	    }

    	    psFatura.executeBatch();
    	    psUpdateGjendja.executeBatch();
    	    conn.commit(); 

    	    psFatura.close();
    	    psUpdateGjendja.close();

    	} catch (Exception ex) {
    	    ex.printStackTrace();
    	    JOptionPane.showMessageDialog(this, "Gabim gjate ruajtjes se fatures : " + ex.getMessage());
    	    return;
    	}

    	try (Connection conn = DBConnection.getConnection()) {
    	    String sqlDeleteMbyllje = "DELETE FROM mbyllje_arka WHERE id_shitesi = ?";
    	    PreparedStatement psDelete = conn.prepareStatement(sqlDeleteMbyllje);
    	    psDelete.setInt(1, idShitesi);
    	    psDelete.executeUpdate();
    	    psDelete.close();
    	} catch (Exception ex) {
    	    ex.printStackTrace();
    	    JOptionPane.showMessageDialog(this, "Gabim gjate rifreskimit te arkes: " + ex.getMessage());
    	}

    	
    	if (Mbylljet.instance != null) {
    	    Mbylljet.instance.loadArkatIntoTable();
    	}

    	
    	double total = 0;
    	DefaultTableModel model = (DefaultTableModel) table.getModel();
    	for (int i = 0; i < model.getRowCount(); i++) {
    	    try {
    	        total += Double.parseDouble(model.getValueAt(i, 4).toString());
    	    } catch (Exception ex) {}
    	}

    	
    	JFrame faturaFrame = new JFrame("Fatura");
    	faturaFrame.setSize(600, 700);
    	faturaFrame.setLocationRelativeTo(null);
    	faturaFrame.getContentPane().setBackground(new Color(211, 211, 211));
    	faturaFrame.getContentPane().setLayout(null);

    	
    	JPanel klientPanel = new JPanel();
    	klientPanel.setLayout(null);
    	klientPanel.setBackground(new Color(119, 136, 153));
    	klientPanel.setBounds(10, 10, 560, 120);
    	faturaFrame.getContentPane().add(klientPanel);

    	klientPanel.add(new JLabel("Te dhenat e klientit:")).setBounds(10, 10, 200, 25);
    	klientPanel.add(new JLabel("Emri: " + emri)).setBounds(10, 40, 250, 20);
    	klientPanel.add(new JLabel("Mbiemri: " + mbiemri)).setBounds(10, 60, 250, 20);
    	klientPanel.add(new JLabel("ID Klienti: " + idKlienti)).setBounds(300, 40, 250, 20);
    	klientPanel.add(new JLabel("Numri Kontakti: " + numriKontakti)).setBounds(300, 60, 250, 20);

    	
    	JPanel artikujPanel = new JPanel(new BorderLayout());
    	artikujPanel.setBounds(10, 140, 560, 450);
    	artikujPanel.setBackground(new Color(211, 225, 245));
    	faturaFrame.getContentPane().add(artikujPanel);

    	DefaultTableModel fatureModel = new DefaultTableModel(
    	    new String[]{"ID_Libri", "Titulli", "Sasia", "Cmimi", "Cmimi Total"}, 0
    	);

    	for (int i = 0; i < table.getRowCount(); i++) {
    	    fatureModel.addRow(new Object[]{
    	        table.getValueAt(i, 0),
    	        table.getValueAt(i, 1),
    	        table.getValueAt(i, 2),
    	        table.getValueAt(i, 3),
    	        table.getValueAt(i, 4)
    	    });
    	}

    	JTable fatureTable = new JTable(fatureModel);
    	fatureTable.setFont(new Font("Serif", Font.PLAIN, 14));
    	fatureTable.setRowHeight(25);
    	fatureTable.getTableHeader().setBackground(new Color(119, 136, 153));
    	fatureTable.getTableHeader().setForeground(Color.WHITE);
    	fatureTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 15));
    	fatureTable.getTableHeader().setReorderingAllowed(false);

    	artikujPanel.add(new JScrollPane(fatureTable), BorderLayout.CENTER);

    	
    	JPanel totalPanel = new JPanel(null);
    	totalPanel.setBackground(new Color(211, 211, 211));
    	totalPanel.setBounds(10, 600, 560, 50);
    	faturaFrame.getContentPane().add(totalPanel);

    	JLabel lblTotal = new JLabel("Totali per te paguar: " + total + " €");
    	lblTotal.setFont(new Font("Serif", Font.BOLD, 16));
    	lblTotal.setBounds(10, 10, 300, 30);
    	totalPanel.add(lblTotal);

    	faturaFrame.setVisible(true);
    }
    private void ruajKlientNeDB(String emri, String mbiemri, String idKlienti, String numriKontakti, String gjinia) {
        String sql = "INSERT INTO klienti (idKlienti, emri, mbiemri, gjinia, numerKontakti) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idKlienti);
            ps.setString(2, emri);
            ps.setString(3, mbiemri);
            ps.setString(4, gjinia);
            ps.setString(5, numriKontakti);

            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
           
        }
    }
}

   