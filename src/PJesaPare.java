// PJesaPare.java
import java.awt.*;
import javax.swing.*;

public class PJesaPare extends JFrame {
    private static final long serialVersionUID = 1L;
    private JLabel lblUserName;
    private String usernameLogin, emri, mbiemri;
    private int idShitesi;
    

    public PJesaPare(String emriPlote, String usernameLogin, String emri, String mbiemri, int idShitesi) {
        this.usernameLogin = usernameLogin;
        this.emri = emri;
        this.mbiemri = mbiemri;
        this.idShitesi = idShitesi;
        setExtendedState(JFrame.MAXIMIZED_BOTH); 


        getContentPane().setBackground(new Color(220, 220, 220));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1349, 746);
        getContentPane().setLayout(null);

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(220, 220, 220));
        panelHeader.setBounds(0, 0, 1283, 88);
        getContentPane().add(panelHeader);
        panelHeader.setLayout(null);

        lblUserName = new JLabel(emriPlote);
        lblUserName.setFont(new Font("Serif", Font.BOLD, 18));
        lblUserName.setBounds(82, 55, 178, 35);
        panelHeader.add(lblUserName);

        JLabel lblIconShites = new JLabel();
        lblIconShites.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\IkonShites.png"));
        lblIconShites.setBounds(22, 31, 50, 50);
        panelHeader.add(lblIconShites);

        JButton btnCilesimet = new JButton();
        btnCilesimet.setBackground(new Color(255, 255, 255));
        btnCilesimet.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\Setting.png"));
        btnCilesimet.addActionListener(e -> {
            Cilesimet cilesimetFrame = new Cilesimet(idShitesi, usernameLogin, emri, mbiemri);
            cilesimetFrame.setVisible(true);
        });
        btnCilesimet.setBounds(908, 11, 50, 50);
        panelHeader.add(btnCilesimet);

        JPanel panelMenu = new JPanel();
        panelMenu.setBackground(new Color(119, 136, 153));
        panelMenu.setBounds(0, 89, 1378, 195);
        getContentPane().add(panelMenu);
        panelMenu.setLayout(null);

        JButton btnFatura = new JButton("Fatura");
        btnFatura.setBackground(new Color(255, 255, 255));
        btnFatura.setForeground(UIManager.getColor("Button.background"));
        btnFatura.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\Faturat.png"));
        btnFatura.setBounds(52, 34, 110, 110);
        btnFatura.addActionListener(e -> {
        	RuajFature faturaFrame = new RuajFature(idShitesi); 
        	faturaFrame.setVisible(true);

        });
        panelMenu.add(btnFatura);
        JLabel lblFatura = new JLabel("Fatura");
        lblFatura.setForeground(new Color(255, 255, 255));
        lblFatura.setFont(new Font("Serif", Font.BOLD, 18));
        lblFatura.setBounds(75, 155, 70, 27);
        panelMenu.add(lblFatura);

        JButton btnAnulloFature = new JButton("Anullo Faturen");
        btnAnulloFature.setBackground(new Color(255, 255, 255));
        btnAnulloFature.setForeground(new Color(160, 160, 160));
        btnAnulloFature.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\AnullimiFatures.png"));
        btnAnulloFature.setBounds(214, 34, 110, 110);
        btnAnulloFature.addActionListener(e -> {
        	AnulloFaturen anulloFaturenFrame = new AnulloFaturen(this, idShitesi);
        	anulloFaturenFrame.setVisible(true);

        });
        panelMenu.add(btnAnulloFature);
        JLabel lblAnulloFature = new JLabel("Anullo Faturen");
        lblAnulloFature.setForeground(new Color(255, 255, 255));
        lblAnulloFature.setFont(new Font("Serif", Font.BOLD, 18));
        lblAnulloFature.setBounds(215, 155, 144, 27);
        panelMenu.add(lblAnulloFature);

        JButton btnMagazina = new JButton("Magazina");
        btnMagazina.setBackground(new Color(255, 255, 255));
        btnMagazina.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\Magazine.png"));
        btnMagazina.setBounds(377, 34, 110, 110);
        btnMagazina.addActionListener(e -> {
            Magazina magazinaFrame = new Magazina(this); 
            magazinaFrame.setVisible(true);
            setVisible(false);
        });


        panelMenu.add(btnMagazina);
        JLabel lblMagazina = new JLabel("Magazina");
        lblMagazina.setForeground(new Color(255, 255, 255));
        lblMagazina.setFont(new Font("Serif", Font.BOLD, 18));
        lblMagazina.setBounds(393, 156, 94, 25);
        panelMenu.add(lblMagazina);

        JButton btnEvente = new JButton("Evente");
        btnEvente.setBackground(new Color(255, 255, 255));
        btnEvente.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\Untitled_design__11_-removebg-preview.png"));
        btnEvente.setBounds(534, 34, 110, 110);
        btnEvente.addActionListener(e -> {
            Evente eventeFrame = new Evente();
            eventeFrame.setVisible(true);
        });
        panelMenu.add(btnEvente);
        JLabel lblEvente = new JLabel("Evente");
        lblEvente.setForeground(new Color(255, 255, 255));
        lblEvente.setFont(new Font("Serif", Font.BOLD, 18));
        lblEvente.setBounds(560, 155, 84, 21);
        panelMenu.add(lblEvente);

        JButton btnPromocione = new JButton("Promocione");
        btnPromocione.setBackground(new Color(255, 255, 255));
        btnPromocione.setIcon(new ImageIcon("C:\\Users\\User\\Downloads\\Promocion.png"));
        btnPromocione.setBounds(691, 34, 110, 110);
        btnPromocione.addActionListener(e -> {
            Promocione promocioneFrame = new Promocione(this); 
        });

        panelMenu.add(btnPromocione);
        JLabel lblPromocione = new JLabel("Promocione");
        lblPromocione.setForeground(new Color(255, 255, 255));
        lblPromocione.setFont(new Font("Serif", Font.BOLD, 18));
        lblPromocione.setBounds(701, 155, 121, 27);
        panelMenu.add(lblPromocione);
    }
}
