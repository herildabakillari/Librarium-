import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class ZgjidhProdukt extends JFrame {

    private JPanel contentPane;
    private JTable table;
    private JComboBox<String> comboBoxTitulli;
    private JComboBox<String> comboBoxID;

    private Vector<String> titujList = new Vector<>();
    private Vector<String> idList = new Vector<>();

    public ZgjidhProdukt() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1000, 600);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.control);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 


        JLabel lblTitle = new JLabel("Zgjidh artikuj per ne fature");
        lblTitle.setFont(new Font("Serif", Font.BOLD, 16));
        lblTitle.setBounds(10, 11, 308, 33);
        contentPane.add(lblTitle);

        comboBoxTitulli = new JComboBox<>();
        comboBoxTitulli.setEditable(true);
        comboBoxTitulli.setBackground(new Color(255, 255, 255));
        comboBoxTitulli.setBounds(10, 50, 469, 40);
        contentPane.add(comboBoxTitulli);

        comboBoxID = new JComboBox<>();
        comboBoxID.setBackground(new Color(255, 255, 255));
        comboBoxID.setEditable(true);
        comboBoxID.setBounds(500, 50, 200, 40);
        contentPane.add(comboBoxID);

        JButton btnLoad = new JButton("🔍");
        btnLoad.setForeground(new Color(51, 102, 153));
        btnLoad.setBounds(720, 50, 80, 40);
        contentPane.add(btnLoad);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 110, 960, 350);
        contentPane.add(scrollPane);
     
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setRowSelectionAllowed(true);
        table.setRowHeight(28);
        table.setFont(new Font("Serif", Font.PLAIN, 14));
        table.setForeground(Color.DARK_GRAY);
        table.setBackground(new Color(245, 245, 245));
        table.setGridColor(Color.LIGHT_GRAY);

        table.getTableHeader().setBackground(new Color(119, 136, 153)); 
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Serif", Font.BOLD, 15));
        table.getTableHeader().setReorderingAllowed(false);

      
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    setBackground(new Color(176, 196, 222)); 
                    setForeground(Color.DARK_GRAY);
                } else {
                    setBackground(row % 2 == 0 ? new Color(230, 245, 255) : new Color(200, 225, 255));
                    setForeground(Color.DARK_GRAY);
                }
                return this;
            }
        });


        table.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Artikulli", "Sasia", "Çmimi", "Vendndodhja", "Kategoria", "Pershkrimi"}
        ));
        scrollPane.setViewportView(table);

        JButton btnShtoProdukt = new JButton("✚  Shto Produkt");
        btnShtoProdukt.setForeground(new Color(51, 102, 153));
        btnShtoProdukt.setFont(new Font("Serif", Font.BOLD, 18));
        btnShtoProdukt.setBounds(20, 471, 187, 40);
        contentPane.add(btnShtoProdukt);

        JButton btnMbyll = new JButton("❌  Mbyll");
        btnMbyll.setForeground(new Color(51, 102, 153));
        btnMbyll.setFont(new Font("Serif", Font.BOLD, 18));
        btnMbyll.setBounds(819, 473, 151, 40);
        contentPane.add(btnMbyll);
        btnMbyll.addActionListener(e -> dispose());

        loadLibrat();
        setupAutoComplete(comboBoxTitulli, titujList);
        setupAutoComplete(comboBoxID, idList);

        btnLoad.addActionListener(e -> loadSelectedProductToTable());

        btnShtoProdukt.addActionListener(e -> {
            int[] selectedRows = table.getSelectedRows();
            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(this, "Zgjidh te pakten nje rresht nga tabela!");
                return;
            }

            if (RuajFature.instance != null) {
                for (int row : selectedRows) {
                    Object produktID = table.getValueAt(row, 0);
                    Object titulli = table.getValueAt(row, 1);
                    Object cmimi = table.getValueAt(row, 3);

                    RuajFature.instance.shtoArtikullNeTabela(new Object[]{
                            produktID,
                            titulli,
                            1,
                            cmimi,
                            cmimi,
                            java.time.LocalDate.now().toString()
                    });
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "RuajFature nuk eshte hapur!");
            }
        });
    }

    
    	private void loadLibrat() {
    	    try (Connection conn = DBConnection.getConnection();
    	         Statement stmt = conn.createStatement();
    	         
    	         ResultSet rs = stmt.executeQuery("SELECT ID_Libri, Titulli FROM librat WHERE Gjendje_Dyqan > 0")) {

    	        comboBoxTitulli.removeAllItems();
    	        comboBoxID.removeAllItems();
    	        titujList.clear();
    	        idList.clear();

    	        while (rs.next()) {
    	            String id = String.valueOf(rs.getInt("ID_Libri"));
    	            String titulli = rs.getString("Titulli");

    	            idList.add(id);
    	            titujList.add(titulli);

    	            comboBoxID.addItem(id);
    	            comboBoxTitulli.addItem(titulli);
    	        }
    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	        JOptionPane.showMessageDialog(this, "Gabim gjate marrjes se librave: " + e.getMessage());
    	    }
    	}

    	private void loadSelectedProductToTable() {
    	    String selectedID = (String) comboBoxID.getSelectedItem();
    	    String selectedTitle = (String) comboBoxTitulli.getSelectedItem();

    	    if ((selectedID == null || selectedID.isEmpty()) && (selectedTitle == null || selectedTitle.isEmpty())) {
    	        JOptionPane.showMessageDialog(this, "Zgjidh ID ose Titull per produktin!");
    	        return;
    	    }

    	    try (Connection conn = DBConnection.getConnection();
    	         PreparedStatement ps = conn.prepareStatement(
    	                 "SELECT * FROM librat WHERE (ID_Libri = ? OR Titulli = ?) AND Gjendje_Dyqan > 0"
    	         )) {

    	        ps.setString(1, selectedID != null ? selectedID : "");
    	        ps.setString(2, selectedTitle != null ? selectedTitle : "");
    	        ResultSet rs = ps.executeQuery();
    	        DefaultTableModel model = (DefaultTableModel) table.getModel();
    	        while (rs.next()) {
    	            model.addRow(new Object[]{
    	                    rs.getInt("ID_Libri"),
    	                    rs.getString("Titulli"),
    	                    1,
    	                    rs.getDouble("Cmim_Shitje"),
    	                    rs.getString("Vendndodhja_raft"),
    	                    rs.getString("Kategoria"),
    	                    rs.getString("Pershkrimi")
    	            });
    	        }

    	    } catch (SQLException e) {
    	        e.printStackTrace();
    	        JOptionPane.showMessageDialog(this, "Gabim gjate marrjes se produktit: " + e.getMessage());
    	    }
    	}


    
    private void setupAutoComplete(JComboBox<String> comboBox, Vector<String> items) {
        comboBox.setEditable(true);
        JTextField tf = (JTextField) comboBox.getEditor().getEditorComponent();
        tf.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String text = tf.getText();
                comboBox.removeAllItems();
                for (String item : items) {
                    if (item.toLowerCase().contains(text.toLowerCase())) {
                        comboBox.addItem(item);
                    }
                }
                tf.setText(text);
                comboBox.showPopup();
            }
        });
    }
}

