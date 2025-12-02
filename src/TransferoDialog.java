import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.awt.Color;

public class TransferoDialog extends javax.swing.JDialog {

  private int idLibri;

    
    public TransferoDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.idLibri = idLibri;
        setLocationRelativeTo(parent);
        ngarkoGjendjen(); 
    }
/**
 * @wbp.parser.constructor
 */
public TransferoDialog(java.awt.Frame parent, boolean modal, int idLibri) {
    super(parent, modal);
    initComponents();
    this.idLibri = idLibri; 
    setLocationRelativeTo(parent);
    ngarkoGjendjen();            
}
   private void ngarkoGjendjen() {
       try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT Titulli, Gjendje_Magazine, Gjendje_Dyqan FROM librat WHERE ID_Libri = ?")) {

            ps.setInt(1, idLibri);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Libri nuk u gjet.");
                    dispose();
                    return;
                }

                String titulli = rs.getString("Titulli");
                int gjMag = rs.getInt("Gjendje_Magazine");
                int gjDyq = rs.getInt("Gjendje_Dyqan");

                
                TitullLabel.setText(titulli);
                MagazinLabel.setText(String.valueOf(gjMag));
                DyqanLabel.setText(String.valueOf(gjDyq));

               
               if (gjMag > 0) {
    SasiaSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, gjMag, 1));
    RuajBtn.setEnabled(true);
} else {
    SasiaSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 0, 1));
    RuajBtn.setEnabled(false);
}

if (gjDyq > 0) {
    RuajBtn1.setEnabled(true);
} else {
    RuajBtn1.setEnabled(false);
}
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gabim" + ex.getMessage());
            dispose();
        }
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        titulliLabel = new javax.swing.JLabel();
        titulliLabel.setForeground(new Color(51, 102, 153));
        gjendjeMagazineLabel = new javax.swing.JLabel();
        gjendjeMagazineLabel.setForeground(new Color(51, 102, 153));
        gjendjeDyqanLabel = new javax.swing.JLabel();
        gjendjeDyqanLabel.setForeground(new Color(51, 102, 153));
        SasiaSpinner = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jLabel2.setForeground(new Color(51, 102, 153));
        AnulloBtn = new javax.swing.JButton();
        AnulloBtn.setForeground(new Color(51, 102, 153));
        RuajBtn = new javax.swing.JButton();
        RuajBtn.setForeground(new Color(51, 102, 153));
        TitullLabel = new javax.swing.JLabel();
        DyqanLabel = new javax.swing.JLabel();
        MagazinLabel = new javax.swing.JLabel();
        RuajBtn1 = new javax.swing.JButton();
        RuajBtn1.setForeground(new Color(51, 102, 153));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new Color(119, 136, 153));

        jLabel1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Transfero");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        titulliLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        titulliLabel.setText("Titulli:");

        gjendjeMagazineLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        gjendjeMagazineLabel.setText("Gjendje ne dyqan:");

        gjendjeDyqanLabel.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        gjendjeDyqanLabel.setText("Gjendje Magazine:");

        jLabel2.setFont(new java.awt.Font("Serif", 1, 20)); // NOI18N
        jLabel2.setText("Sasia per Transferim:");

        AnulloBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        AnulloBtn.setText("Anullo");
        AnulloBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnulloBtnActionPerformed(evt);
            }
        });

        RuajBtn.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        RuajBtn.setText("Transfero ne Dyqan");
        RuajBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuajBtnActionPerformed(evt);
            }
        });

        TitullLabel.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        DyqanLabel.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        MagazinLabel.setFont(new java.awt.Font("Serif", 0, 18)); // NOI18N

        RuajBtn1.setFont(new java.awt.Font("Serif", 1, 18)); // NOI18N
        RuajBtn1.setText("Transfero Ne Magazine");
        RuajBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RuajBtn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(AnulloBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(titulliLabel)
                            .addComponent(gjendjeMagazineLabel)
                            .addComponent(gjendjeDyqanLabel))
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(MagazinLabel)
                            .addComponent(DyqanLabel)
                            .addComponent(TitullLabel)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(SasiaSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(RuajBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(RuajBtn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGap(37, 37, 37))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(titulliLabel)
                            .addComponent(TitullLabel))
                        .addGap(54, 54, 54)
                        .addComponent(gjendjeMagazineLabel))
                    .addComponent(DyqanLabel))
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gjendjeDyqanLabel)
                    .addComponent(MagazinLabel))
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(SasiaSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RuajBtn1))
                .addGap(18, 18, 18)
                .addComponent(RuajBtn)
                .addGap(18, 18, 18)
                .addComponent(AnulloBtn)
                .addContainerGap(63, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void RuajBtnActionPerformed(java.awt.event.ActionEvent evt) {                                        
      
        int sasia = (int) SasiaSpinner.getValue();

        if (sasia <= 0) {
            JOptionPane.showMessageDialog(this, "Sasia nuk mund te jete 0!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
           
            String selectQuery = "SELECT Gjendje_Magazine, Gjendje_Dyqan FROM librat WHERE ID_Libri = ?";
            PreparedStatement ps = conn.prepareStatement(selectQuery);
            ps.setInt(1, idLibri);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int gjendjeMagazine = rs.getInt("Gjendje_Magazine");
                int gjendjeDyqan = rs.getInt("Gjendje_Dyqan");

                if (sasia > gjendjeMagazine) {
                    JOptionPane.showMessageDialog(this, "Stok i pamjaftueshem!");
                    return;
                }

             
                String updateQuery = "UPDATE librat SET Gjendje_Magazine = ?, Gjendje_Dyqan = ? WHERE ID_Libri = ?";
                PreparedStatement pst = conn.prepareStatement(updateQuery);
                pst.setInt(1, gjendjeMagazine - sasia);
                pst.setInt(2, gjendjeDyqan + sasia);
                pst.setInt(3, idLibri);

                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "✅ Librat u transferuan me sukses!");
                    dispose(); // Mbyll dialogun pas suksesit
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Gabim gjate transferimit!");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gabim " + ex.getMessage());
        }
    }                                       

    private void AnulloBtnActionPerformed(java.awt.event.ActionEvent evt) {                                          
        dispose();
    }                                         

    private void RuajBtn1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
          int sasia = (int) SasiaSpinner.getValue();

    if (sasia <= 0) {
        JOptionPane.showMessageDialog(this, "Sasia nuk mund te jete 0!");
        return;
    }

    try (Connection conn = DBConnection.getConnection()) {
        String selectQuery = "SELECT Gjendje_Magazine, Gjendje_Dyqan FROM librat WHERE ID_Libri = ?";
        PreparedStatement ps = conn.prepareStatement(selectQuery);
        ps.setInt(1, idLibri);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int gjendjeMagazine = rs.getInt("Gjendje_Magazine");
            int gjendjeDyqan = rs.getInt("Gjendje_Dyqan");

            if (sasia > gjendjeDyqan) {
                JOptionPane.showMessageDialog(this, "Stok i pamjaftueshem ne dyqan!");
                return;
            }

            String updateQuery = "UPDATE librat SET Gjendje_Magazine = ?, Gjendje_Dyqan = ? WHERE ID_Libri = ?";
            PreparedStatement pst = conn.prepareStatement(updateQuery);
            pst.setInt(1, gjendjeMagazine + sasia);  
            pst.setInt(2, gjendjeDyqan - sasia);     
            pst.setInt(3, idLibri);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "✅ Librat u transferuan ne magazine me sukses!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Gabim gjate transferimit!");
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gabim " + ex.getMessage());
    }
    }                                        


    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TransferoDialog dialog = new TransferoDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton AnulloBtn;
    private javax.swing.JLabel DyqanLabel;
    private javax.swing.JLabel MagazinLabel;
    private javax.swing.JButton RuajBtn;
    private javax.swing.JButton RuajBtn1;
    private javax.swing.JSpinner SasiaSpinner;
    private javax.swing.JLabel TitullLabel;
    private javax.swing.JLabel gjendjeDyqanLabel;
    private javax.swing.JLabel gjendjeMagazineLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel titulliLabel;
    // End of variables declaration                   
}