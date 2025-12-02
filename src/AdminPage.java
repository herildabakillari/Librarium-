import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.awt.event.*;

public class AdminPage extends JFrame {
    private String roli;

    public AdminPage(String roli) {
        this.roli = roli;
        initComponents(roli);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public JLabel UsernameLabel1; 
    private int idAdmin;
    private String username, emri, mbiemri;

    
    public AdminPage(String emriPlote, String username, String emri, String mbiemri, int idAdmin) {
        this.username = username;
        this.emri = emri;
        this.mbiemri = mbiemri;
        this.idAdmin = idAdmin;
        initComponents(emriPlote);
    }

    
    public AdminPage() {
        initComponents("Admin User");
    }

    private void initComponents(String emriPlote) {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                LoginPage login = new LoginPage();
                login.setVisible(true);
                dispose();
            }
        });

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(100, 100, 1349, 746);
        getContentPane().setBackground(new Color(220, 220, 220));
        getContentPane().setLayout(null);

        
        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(211, 211, 211));
        panelHeader.setBounds(0, 0, 1378, 88);
        panelHeader.setLayout(null);
        getContentPane().add(panelHeader);

        UsernameLabel1 = new JLabel(emriPlote);
        UsernameLabel1.setFont(new Font("Serif", Font.BOLD, 18));
        UsernameLabel1.setBounds(82, 55, 178, 35);
        panelHeader.add(UsernameLabel1);

        JLabel lblIconAdmin = new JLabel();
        lblIconAdmin.setIcon(safeIcon("C:\\Users\\User\\Downloads\\IkonShites.png"));
        lblIconAdmin.setBounds(22, 31, 50, 50);
        panelHeader.add(lblIconAdmin);

        JButton btnSettings = new JButton();
        btnSettings.setBackground(new Color(245, 245, 245));
        btnSettings.setIcon(safeIcon("C:\\Users\\User\\Downloads\\Setting.png"));
        btnSettings.setBounds(1280, 11, 50, 50);
        btnSettings.addActionListener(e -> JOptionPane.showMessageDialog(this, "Hapet faqja e cilesimeve per adminin"));
        panelHeader.add(btnSettings);
        
        JLabel lblIconShites = new JLabel();
        lblIconShites.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\IkonShites.png"));
        lblIconShites.setBounds(22, 31, 50, 50);
        panelHeader.add(lblIconShites);

       
        JPanel panelMenu = new JPanel();
        panelMenu.setForeground(new Color(255, 255, 255));
        panelMenu.setBackground(new Color(119, 136, 153));
        panelMenu.setBounds(0, 86, 1388, 195);
        panelMenu.setLayout(null);
        getContentPane().add(panelMenu);

        //Fatura
        JButton btnFatura = new JButton();
        btnFatura.addActionListener(e -> {
            Fatura faturaFrame = new Fatura(this); 
            faturaFrame.setVisible(true);
            this.setVisible(false);
        });


        btnFatura.setBackground(Color.WHITE);
        btnFatura.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\Fature.png"));
        btnFatura.setBounds(56, 24, 110, 110);
        panelMenu.add(btnFatura);
        JLabel lblFatura = new JLabel("Fatura");
        lblFatura.setForeground(new Color(255, 255, 255));
        lblFatura.setFont(new Font("Serif", Font.BOLD, 18));
        lblFatura.setBounds(87, 145, 70, 27);
        panelMenu.add(lblFatura);

        
        
        JButton btnMagazina = new JButton();
        btnMagazina.addActionListener(e -> {
            Iventari iventariFrame = new Iventari(this);
            iventariFrame.setVisible(true);
            this.setVisible(false);
        });


        btnMagazina.setBackground(Color.WHITE);
        btnMagazina.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\Magazin.jpg"));
        btnMagazina.setBounds(220, 24, 110, 110);
        panelMenu.add(btnMagazina);
        JLabel lblMagazina = new JLabel("Magazina");
        lblMagazina.setForeground(new Color(255, 255, 255));
        lblMagazina.setFont(new Font("Serif", Font.BOLD, 18));
        lblMagazina.setBounds(230, 145, 100, 27);
        panelMenu.add(lblMagazina);

      
        JButton btnShtoShites = new JButton();
        btnShtoShites.addActionListener(e -> {
            Shites shitesFrame = new Shites(this); 
            shitesFrame.setVisible(true);
            this.setVisible(false);
        });

        btnShtoShites.setBackground(Color.WHITE);
        btnShtoShites.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\Shtoo.jpg"));
        btnShtoShites.setBounds(383, 24, 110, 110);
        panelMenu.add(btnShtoShites);
        JLabel lblShtoShites = new JLabel("Shto shites");
        lblShtoShites.setForeground(new Color(255, 255, 255));
        lblShtoShites.setFont(new Font("Serif", Font.BOLD, 18));
        lblShtoShites.setBounds(390, 145, 120, 27);
        panelMenu.add(lblShtoShites);

        
        JButton btnEvente = new JButton();
        btnEvente.setBackground(Color.WHITE);
        btnEvente.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\Evente (1).png"));
        btnEvente.setBounds(549, 24, 110, 110);
        btnEvente.addActionListener(e -> {
            Eventet eventeFrame = new Eventet("admin", this);
            eventeFrame.setVisible(true);
            this.setVisible(false); 
        });

        panelMenu.add(btnEvente);
        JLabel lblEvente = new JLabel("Evente");
        lblEvente.setForeground(new Color(255, 255, 255));
        lblEvente.setFont(new Font("Serif", Font.BOLD, 18));
        lblEvente.setBounds(570, 145, 80, 27);
        panelMenu.add(lblEvente);

     
        JButton btnPromocione = new JButton();
        btnPromocione.setBackground(Color.WHITE);
        btnPromocione.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\Promocion.png"));
        btnPromocione.setBounds(712, 24, 110, 110);
        btnPromocione.addActionListener(e -> {
            Promocioni promocioniFrame = new Promocioni(this); 
            promocioniFrame.setVisible(true);
            this.setVisible(false); 
        });


        panelMenu.add(btnPromocione);
        JLabel lblPromocione = new JLabel("Promocione");
        lblPromocione.setForeground(new Color(255, 255, 255));
        lblPromocione.setFont(new Font("Serif", Font.BOLD, 18));
        lblPromocione.setBounds(722, 145, 121, 27);
        panelMenu.add(lblPromocione);

     
        JButton btnFurnizim = new JButton("Furnizimet");
        btnFurnizim.addActionListener(e -> {
            Furnizimet furnizimetFrame = new Furnizimet(this); 
            furnizimetFrame.setVisible(true);
            this.setVisible(false); 
        });
        btnFurnizim.setBackground(Color.WHITE);
        btnFurnizim.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\Furnizim.png"));
        btnFurnizim.setBounds(875, 24, 110, 110);
        panelMenu.add(btnFurnizim);
        JLabel lblFurnizim = new JLabel("Furnizim");
        lblFurnizim.setForeground(new Color(255, 255, 255));
        lblFurnizim.setFont(new Font("Serif", Font.BOLD, 18));
        lblFurnizim.setBounds(890, 145, 100, 27);
        panelMenu.add(lblFurnizim);

        // Statistika
        JButton btnStatistika = new JButton();
        btnStatistika.setBackground(Color.WHITE);
        btnStatistika.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\Statistika.png"));
        btnStatistika.setBounds(1035, 24, 110, 110);
        btnStatistika.addActionListener(e -> {
            StatistikaShitesit statFrame = new StatistikaShitesit(this); 
            statFrame.setVisible(true);
            this.setVisible(false); 
        });



        panelMenu.add(btnStatistika);
        JLabel lblStatistika = new JLabel("Statistika");
        lblStatistika.setForeground(new Color(255, 255, 255));
        lblStatistika.setFont(new Font("Serif", Font.BOLD, 18));
        lblStatistika.setBounds(1050, 145, 100, 27);
        panelMenu.add(lblStatistika);
    }

    private ImageIcon safeIcon(String path) {
        File file = new File(path);
        if (file.exists()) return new ImageIcon(path);
        else return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminPage("Admin User", "admin", "Admin", "User", 1).setVisible(true);
        });
    }
}
