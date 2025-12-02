import javax.swing.*;
import java.awt.*;

public class KonfirmimRuajtje extends JFrame {

    private static final long serialVersionUID = 1L;

    public KonfirmimRuajtje(String emri, String mbiemri, String idKlienti, String numriKontakti) {
        setTitle("Konfirmim Ruajtjeje");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 320);
        setResizable(false);

        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(220, 220, 220));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        
        JPanel panelHeader = new JPanel();
        panelHeader.setForeground(new Color(119, 136, 153));
        panelHeader.setBackground(new Color(119, 136, 153));
        panelHeader.setBounds(0, 0, 434, 60);
        panelHeader.setLayout(null);
        contentPane.add(panelHeader);

        
        JLabel lblIcon = new JLabel("\u2705");
        lblIcon.setForeground(new Color(255, 255, 255));
        lblIcon.setFont(new Font("Serif", Font.BOLD, 40));
        lblIcon.setBounds(15, 10, 50, 40);
        panelHeader.add(lblIcon);

        
        JLabel lblTitulli = new JLabel("Klienti u ruajt me sukses");
        lblTitulli.setFont(new Font("Serif", Font.BOLD, 20));
        lblTitulli.setForeground(Color.WHITE);
        lblTitulli.setBounds(70, 10, 300, 40);
        panelHeader.add(lblTitulli);

        
        JPanel panelInfo = new JPanel();
        panelInfo.setBackground(new Color(245, 245, 245));
        panelInfo.setBounds(15, 75, 404, 150);
        panelInfo.setLayout(null);
        contentPane.add(panelInfo);

        Font labelBold = new Font("Serif", Font.BOLD, 16);
        Font labelNormal = new Font("Serif", Font.PLAIN, 16);
        Color textColor = new Color(50, 50, 50);

        JLabel lblEmri = new JLabel("Emri:");
        lblEmri.setFont(labelBold);
        lblEmri.setForeground(new Color(51, 102, 153));
        lblEmri.setBounds(20, 15, 100, 25);
        panelInfo.add(lblEmri);

        JLabel valEmri = new JLabel(emri);
        valEmri.setFont(labelNormal);
        valEmri.setForeground(textColor);
        valEmri.setBounds(130, 15, 250, 25);
        panelInfo.add(valEmri);

        JLabel lblMbiemri = new JLabel("Mbiemri:");
        lblMbiemri.setFont(labelBold);
        lblMbiemri.setForeground(new Color(51, 102, 153));
        lblMbiemri.setBounds(20, 50, 100, 25);
        panelInfo.add(lblMbiemri);

        JLabel valMbiemri = new JLabel(mbiemri);
        valMbiemri.setFont(labelNormal);
        valMbiemri.setForeground(textColor);
        valMbiemri.setBounds(130, 50, 250, 25);
        panelInfo.add(valMbiemri);

        JLabel lblId = new JLabel("ID Klienti:");
        lblId.setFont(labelBold);
        lblId.setForeground(new Color(51, 102, 153));
        lblId.setBounds(20, 85, 100, 25);
        panelInfo.add(lblId);

        JLabel valId = new JLabel(idKlienti);
        valId.setFont(labelNormal);
        valId.setForeground(textColor);
        valId.setBounds(130, 85, 250, 25);
        panelInfo.add(valId);

        JLabel lblKontakt = new JLabel("Kontakti:");
        lblKontakt.setFont(labelBold);
        lblKontakt.setForeground(new Color(51, 102, 153));
        lblKontakt.setBounds(20, 120, 100, 25);
        panelInfo.add(lblKontakt);

        JLabel valKontakt = new JLabel(numriKontakti);
        valKontakt.setFont(labelNormal);
        valKontakt.setForeground(textColor);
        valKontakt.setBounds(130, 120, 250, 25);
        panelInfo.add(valKontakt);

        
        JButton btnOK = new JButton("OK");
        btnOK.setForeground(new Color(51, 102, 153));
        btnOK.setFont(new Font("Serif", Font.BOLD, 18));
        btnOK.setBackground(new Color(211, 211, 211));
        btnOK.setFocusPainted(false);
        btnOK.setBounds(160, 240, 120, 40);
        contentPane.add(btnOK);

        btnOK.addActionListener(e -> dispose());
    }
}

   

