import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;


public class LoginPage extends JFrame {
    private static final long serialVersionUID = 1L;
    private JLabel LoginLabel, UsernameJL, PassJL;
    private JPasswordField PasswordJF;
    private JButton ButtonOK;
    private JComboBox<String> UsernameCB;

    public LoginPage() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null); 
        setResizable(true);

      

        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 600);
        getContentPane().setLayout(null);
        getContentPane().setBackground(new Color(211, 211, 211));

        LoginLabel = new JLabel("📚 Login");
        LoginLabel.setFont(new Font("Serif", Font.BOLD, 80));
        LoginLabel.setForeground(new Color(51, 102, 153));
        LoginLabel.setBounds(416, 135, 329, 100);
        getContentPane().add(LoginLabel);

        UsernameJL = new JLabel("Username:");
        UsernameJL.setFont(new Font("Serif", Font.BOLD, 26));
        UsernameJL.setForeground(new Color(51, 102, 153));
        UsernameJL.setBounds(314, 303, 150, 35);
        getContentPane().add(UsernameJL);

        UsernameCB = new JComboBox<>();
        UsernameCB.setEditable(true);
        UsernameCB.setBackground(new Color(245, 245, 245));
        UsernameCB.setForeground(new Color(50, 50, 50));
        UsernameCB.setBounds(507, 303, 300, 35);
        getContentPane().add(UsernameCB);

        PassJL = new JLabel("Password:");
        PassJL.setFont(new Font("Serif", Font.BOLD, 26));
        PassJL.setForeground(new Color(51, 102, 153));
        PassJL.setBounds(314, 376, 150, 35);
        getContentPane().add(PassJL);

        PasswordJF = new JPasswordField();
        PasswordJF.setBounds(507, 376, 300, 35);
        getContentPane().add(PasswordJF);

        ButtonOK = new JButton("OK");
        ButtonOK.setFont(new Font("Serif", Font.BOLD, 24));
        ButtonOK.setBackground(new Color(255, 255, 255));
        ButtonOK.setForeground(new Color(51, 102, 153));
        ButtonOK.setFocusPainted(false);
        ButtonOK.setBounds(687, 449, 120, 40);
        getContentPane().add(ButtonOK);

        ButtonOK.addActionListener(e -> loginUser());

        loadUsernames();
        setLocationRelativeTo(null);
    }

    private void loadUsernames() {
        try (Connection conn = DBConnection.getConnection()) {

           
            PreparedStatement psShites = conn.prepareStatement("SELECT Username FROM Shites");
            ResultSet rsShites = psShites.executeQuery();
            while (rsShites.next()) {
                UsernameCB.addItem(rsShites.getString("Username"));
            }

            
            PreparedStatement psAdmin = conn.prepareStatement("SELECT Username FROM Admin");
            ResultSet rsAdmin = psAdmin.executeQuery();
            while (rsAdmin.next()) {
                UsernameCB.addItem(rsAdmin.getString("Username"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loginUser() {
        String username = (String) UsernameCB.getSelectedItem();
        String password = new String(PasswordJF.getPassword());

        try (Connection conn = DBConnection.getConnection()) {

           
            PreparedStatement psAdmin = conn.prepareStatement(
                    "SELECT ID_Admin, Emri, Mbiemri FROM Admin WHERE Username=? AND Password=?");
            psAdmin.setString(1, username);
            psAdmin.setString(2, password);
            ResultSet rsAdmin = psAdmin.executeQuery();

            if (rsAdmin.next()) {
               
                String emri = rsAdmin.getString("Emri");
                String mbiemri = rsAdmin.getString("Mbiemri");
                int idAdmin = rsAdmin.getInt("ID_Admin"); 
                String emriPlote = emri + " " + mbiemri;

               
                AdminPage adminFrame = new AdminPage(emriPlote, username, emri, mbiemri, idAdmin);
                adminFrame.setVisible(true);
                dispose(); 
                return;
            }

            
            PreparedStatement psShites = conn.prepareStatement(
                    "SELECT ID_Shitesi, Emri, Mbiemri FROM Shites WHERE Username=? AND Password=?");
            psShites.setString(1, username);
            psShites.setString(2, password);
            ResultSet rsShites = psShites.executeQuery();

            if (rsShites.next()) {
                String emri = rsShites.getString("Emri");
                String mbiemri = rsShites.getString("Mbiemri");
                int idShitesi = rsShites.getInt("ID_Shitesi");
                String emriPlote = emri + " " + mbiemri;

                PJesaPare pjesaPareFrame = new PJesaPare(emriPlote, username, emri, mbiemri, idShitesi);
                pjesaPareFrame.setVisible(true);
                dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Username ose password i gabuar!", "Gabim", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new LoginPage().setVisible(true));
    }
}


