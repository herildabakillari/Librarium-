import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;

public class Cilesimet extends JFrame {
    private JPanel panelContentPane;
    private JLabel lblEmer, lblMbiemer, lblUsername;
    private JButton btnShikoRaportin, btnDil;
    private int idShitesi; 
    private String usernameLogin, emri, mbiemri;

    public Cilesimet(int idShitesi, String usernameLogin, String emri, String mbiemri) {
        this.idShitesi = idShitesi;
        this.usernameLogin = usernameLogin;
        this.emri = emri;
        this.mbiemri = mbiemri;

        setTitle("Cilesimet");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 632, 543);
        panelContentPane = new JPanel();
        panelContentPane.setBackground(new Color(230, 230, 230)); 
        panelContentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(panelContentPane);
        panelContentPane.setLayout(null);

        JPanel panelHeader = new JPanel();
        panelHeader.setBackground(new Color(119, 136, 153)); 
        panelHeader.setBounds(0, 0, 618, 58);
        panelHeader.setLayout(null);
        panelContentPane.add(panelHeader);

        JLabel lblTitulli = new JLabel("⚙️  Cilesimet");
        lblTitulli.setFont(new Font("Serif", Font.BOLD, 22));
        lblTitulli.setForeground(Color.WHITE);
        lblTitulli.setBounds(10, 11, 220, 35);
        panelHeader.add(lblTitulli);

        JPanel panelForm = new JPanel();
        panelForm.setBackground(new Color(200, 200, 200)); 
        panelForm.setBounds(10, 80, 608, 400);
        panelForm.setLayout(null);
        panelForm.setBorder(new LineBorder(new Color(160, 160, 160), 2, true));
        panelContentPane.add(panelForm);

        JLabel lblEmerLabel = new JLabel("Emer :");
        lblEmerLabel.setForeground(new Color(51, 102, 153));
        lblEmerLabel.setFont(new Font("Serif", Font.BOLD, 18));
        lblEmerLabel.setBounds(20, 20, 90, 35);
        panelForm.add(lblEmerLabel);

        JLabel lblMbiemerLabel = new JLabel("Mbiemer :");
        lblMbiemerLabel.setForeground(new Color(51, 102, 153));
        lblMbiemerLabel.setFont(new Font("Serif", Font.BOLD, 18));
        lblMbiemerLabel.setBounds(20, 80, 100, 35);
        panelForm.add(lblMbiemerLabel);

        JLabel lblUsernameLabel = new JLabel("Username :");
        lblUsernameLabel.setForeground(new Color(51, 102, 153));
        lblUsernameLabel.setFont(new Font("Serif", Font.BOLD, 18));
        lblUsernameLabel.setBounds(20, 140, 100, 35);
        panelForm.add(lblUsernameLabel);

        
        lblEmer = new JLabel(emri);
        lblEmer.setFont(new Font("Serif", Font.PLAIN, 18));
        lblEmer.setBounds(130, 20, 220, 35);
        lblEmer.setBorder(new LineBorder(new Color(150, 150, 150), 1, true));
        lblEmer.setOpaque(true);
        lblEmer.setBackground(Color.WHITE);
        panelForm.add(lblEmer);

        lblMbiemer = new JLabel(mbiemri);
        lblMbiemer.setFont(new Font("Serif", Font.PLAIN, 18));
        lblMbiemer.setBounds(130, 80, 220, 35);
        lblMbiemer.setBorder(new LineBorder(new Color(150, 150, 150), 1, true));
        lblMbiemer.setOpaque(true);
        lblMbiemer.setBackground(Color.WHITE);
        panelForm.add(lblMbiemer);

        lblUsername = new JLabel(usernameLogin);
        lblUsername.setFont(new Font("Serif", Font.PLAIN, 18));
        lblUsername.setBounds(130, 140, 220, 35);
        lblUsername.setBorder(new LineBorder(new Color(150, 150, 150), 1, true));
        lblUsername.setOpaque(true);
        lblUsername.setBackground(Color.WHITE);
        panelForm.add(lblUsername);

        btnShikoRaportin = new JButton("Shiko Raportin");
        btnShikoRaportin.setFont(new Font("Serif", Font.BOLD, 18));
        btnShikoRaportin.setBounds(49, 255, 200, 50);
        btnShikoRaportin.setBackground(new Color(180, 180, 180));
        btnShikoRaportin.setFocusPainted(false);
        btnShikoRaportin.setForeground(new Color(51, 102, 153));
        btnShikoRaportin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnShikoRaportin.setBackground(new Color(140, 140, 140));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnShikoRaportin.setBackground(new Color(180, 180, 180));
            }
        });
        panelForm.add(btnShikoRaportin);

        btnDil = new JButton("Dil");
        btnDil.setFont(new Font("Serif", Font.BOLD, 18));
        btnDil.setBounds(308, 255, 200, 50);
        btnDil.setBackground(new Color(180, 180, 180));
        btnDil.setFocusPainted(false);
        btnDil.setForeground(new Color(51, 102, 153));
        btnDil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDil.setBackground(new Color(140, 140, 140));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDil.setBackground(new Color(180, 180, 180));
            }
        });
        panelForm.add(btnDil);

        btnShikoRaportin.addActionListener(e -> {
            Raporti raportiFrame = new Raporti(idShitesi); 
            raportiFrame.setVisible(true);
        });

        btnDil.addActionListener(e -> {
            dispose();               
            LoginPage login = new LoginPage();  
            login.setVisible(true);            
        });
    }
}
