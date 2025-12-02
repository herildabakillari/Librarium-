import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class Pershkrimi extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final JPanel panel_1 = new JPanel();
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Pershkrimi frame = new Pershkrimi();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Pershkrimi() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 760, 633);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(245, 245, 245));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(220, 220, 220));
		panel.setBounds(0, 0, 746, 596);
		contentPane.add(panel);
		panel.setLayout(null);
		panel_1.setBackground(new Color(119, 136, 153));
		panel_1.setBounds(0, 0, 756, 59);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("  Pershkrimi i Librit");
		lblNewLabel_2.setForeground(UIManager.getColor("Button.disabledShadow"));
		lblNewLabel_2.setFont(new Font("Serif", Font.BOLD, 18));
		lblNewLabel_2.setBackground(new Color(119, 136, 153));
		lblNewLabel_2.setBounds(0, 0, 746, 56);
		panel_1.add(lblNewLabel_2);
		
		JLabel lblNewLabel_1 = new JLabel(" Titulli:");
		lblNewLabel_1.setForeground(new Color(51, 102, 153));
		lblNewLabel_1.setFont(new Font("Serif", Font.BOLD, 18));
		lblNewLabel_1.setBounds(47, 84, 77, 40);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel(" Autori:");
		lblNewLabel_1_1.setForeground(new Color(51, 102, 153));
		lblNewLabel_1_1.setFont(new Font("Serif", Font.BOLD, 18));
		lblNewLabel_1_1.setBounds(47, 161, 77, 40);
		panel.add(lblNewLabel_1_1);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(171, 164, 394, 40);
		panel.add(textField_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Kategoria:");
		lblNewLabel_1_1_1.setForeground(new Color(51, 102, 153));
		lblNewLabel_1_1_1.setFont(new Font("Serif", Font.BOLD, 18));
		lblNewLabel_1_1_1.setBounds(27, 245, 97, 40);
		panel.add(lblNewLabel_1_1_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(171, 248, 394, 40);
		panel.add(textField_2);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("Pershkrimi:");
		lblNewLabel_1_1_1_1.setForeground(new Color(51, 102, 153));
		lblNewLabel_1_1_1_1.setFont(new Font("Serif", Font.BOLD, 18));
		lblNewLabel_1_1_1_1.setBounds(27, 351, 97, 40);
		panel.add(lblNewLabel_1_1_1_1);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(171, 340, 422, 218);
		panel.add(textField_3);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(171, 84, 394, 40);
		panel.add(textField);

	}

}
