/* @author Zsolt Ollé */
package legyenonismilliomos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import model.DBModel;
import model.IModel;
import model.Kerdes;
import model.Labelek;
import model.Osszeg;


public class Foablak extends javax.swing.JFrame {

    private IModel model;
    private Osszeg osszeg;
    private Labelek lb;
    private Vector<Kerdes> kerdesek;
    private Kerdes kerdes;
    private int kerdesSzama = 14;
    private String nyeremeny;
    private String nyeremenyTxt = "Az Ön nyereménye: ";

    // Hibajelentések
    public static void hibaAblak(String uzenet) {
        JOptionPane.showMessageDialog(null, uzenet, "Hiba", JOptionPane.ERROR_MESSAGE);
    }

    public static void visszajelzesAblak(String uzenet) {
        JOptionPane.showMessageDialog(null, uzenet, "Kiesett a játékból", JOptionPane.INFORMATION_MESSAGE);
    }

    public static int folytatja(String uzenet) {
        int valasz = JOptionPane.showConfirmDialog(null, uzenet, "Folytatja ?", JOptionPane.YES_NO_OPTION);
        return valasz;
    }

    // kérdések     
    // public Kerdes kerdesKioszt(int kerdesSzama) {
    public void kerdesKioszt(int kerdesSzama) {
        System.out.println("kérdés száma = " + kerdesSzama);
        lb.labelAlapszin();
        kerdes = new Kerdes(
                kerdesek.get(kerdesSzama).getKerdes(),
                kerdesek.get(kerdesSzama).getValasz0(),
                kerdesek.get(kerdesSzama).getValasz1(),
                kerdesek.get(kerdesSzama).getValasz2(),
                kerdesek.get(kerdesSzama).getValasz3(),
                kerdesek.get(kerdesSzama).getHelyesValasz()
        );
        // return kerdes;
    }

//    public void foablakbolKiir(){
//        System.out.println("Főablakból íratom ki");
//    }
    public void kiertekel(int jatekosValasza) {

        lb.jatekosMegjelol(jatekosValasza);

// MyWorker meghivása
        MyWorker worker = new MyWorker() {
            @Override
            protected void done() {
                lbCoutDown.setText("");

                try {
                    lb.gepJelol(kerdes.getHelyesValasz());

                    
                    if (kerdes.getHelyesValasz() == jatekosValasza) {
                        // nyeremeny = osszeg.getOsszeg(kerdesSzama);
                        nyeremeny = osszeg.getErtek(kerdesSzama);
                        lbNyeremenyOsszege.setText(nyeremenyTxt + nyeremeny);

                       
                            int valasz = folytatja("Jöhet a következő kérdés " + osszeg.getErtek(kerdesSzama - 1) + " ért");

                            if (valasz == 0) {
                                // nyereménytábla ujrarajzolása    
                               //  kerdesSzama -= kerdesSzama;
                                kerdesSzama = kerdesSzama-1;
                                osszeg.OsszegLep(nyeremeny, kerdesSzama);
                                lstNyeremenyTabla.setSelectedIndex(kerdesSzama);

                                // label kérdések beállítása
                                kerdesKioszt(kerdesSzama);
                                lb.labelAlapszin();
                                lb.labelAlapBeallit();
                                lb.kerdesekKiiratasa(kerdes);
                                //                            
                            } else if (valasz == 1) {
                                visszajelzesAblak("Ön kilép a játékból ! Nyereménye " + nyeremeny);
                                //System.out.println("Itt a végeredményt kapom meg: " + get());
                                get(); // hogy ne dobjon hibát
                            }

                        

                    } else if (kerdes.getHelyesValasz() != jatekosValasza) {
                        if (nyeremeny.equals("100 000") || nyeremeny.equals("1 500 000") || nyeremeny.equals("40 000 000")) {
                            visszajelzesAblak("Sajnos Ön nem jó választ adott! Nyereménye " + nyeremeny);
                        } else {
                            nyeremeny = "0";
                            lbNyeremenyOsszege.setText(nyeremenyTxt + nyeremeny);
                            visszajelzesAblak("Sajnos Ön nem jó választ adott! Nyereménye " + nyeremeny);
                        }

                    }
                } catch (InterruptedException ex) {
                    hibaAblak(ex.toString());
                } catch (ExecutionException ex) {
                    hibaAblak(ex.toString());
                }

//                }else{
//                                    System.out.println("Elfogyott a kérdés Gratulálok Ön nyert");
//                                }
            }

            @Override
            protected void process(List<String> chunks) {
                String visszaSzamol = chunks.get(chunks.size() - 1);
                lbCoutDown.setText(visszaSzamol);
            }
        };

        worker.execute();
    }

    /**
     * Creates new form Foablak
     */
    public Foablak() {

        initComponents();
        lbKerdes.setText("Ide fog majd jönni a kérdés !");
        // Bellítja a Labeleket
        lb = new Labelek(lbKerdes, lbCoutDown, lbA, lbB, lbC, lbD);
        lb.labelAlapBeallit();
        osszeg = new Osszeg();
        // kerdesSzama = osszeg.getErtekek().length - 1;
//        kerdesSzama = 1;
        lstNyeremenyTabla.setListData(osszeg.getErtekToList());

        // egér klikkelésének letiltása a nyeremény listen
        // lehetséges nyeremények kiiratása a nyeremény táblába
        lstNyeremenyTabla.setSelectedIndex(kerdesSzama);

/////////////////////////////////////////////////////
        UIManager.put("OptionPane.yesButtonText", "Igen");
        UIManager.put("OptionPane.noButtonText", "Nem");
        setTitle("Legyen Ön is milliomos");
        setLocationRelativeTo(this);
        String connUrl = "jdbc:mysql://127.0.0.1:3306/loim";
        String dbUser = "root";
        String dbPass = "JavaOktatas12";

        try {
            Connection conn = DriverManager.getConnection(connUrl, dbUser, dbPass);
            model = new DBModel(conn);
            kerdesek = model.getAllKerdes();
        } catch (SQLException ex) {
            hibaAblak(ex.toString());
        }

//        kerdesSzama = kerdesek.size() - 1;
        // Összekeverem a kérdéseket
        Collections.shuffle(kerdesek);
        kerdesKioszt(kerdesSzama);
        lb.kerdesekKiiratasa(kerdes);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        lbCoutDown = new javax.swing.JLabel();
        lbNyeremenyOsszege = new javax.swing.JLabel();
        lbKerdes = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstNyeremenyTabla = new javax.swing.JList<>();
        JPbtn = new javax.swing.JPanel();
        lbA = new javax.swing.JLabel();
        lbB = new javax.swing.JLabel();
        lbC = new javax.swing.JLabel();
        lbD = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbCoutDown.setFont(new java.awt.Font("Tahoma", 1, 64)); // NOI18N
        lbCoutDown.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel2.add(lbCoutDown, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 140, 150));

        lbNyeremenyOsszege.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        lbNyeremenyOsszege.setForeground(new java.awt.Color(153, 204, 255));
        lbNyeremenyOsszege.setText("Az Ön nyereménye:  ");
        jPanel2.add(lbNyeremenyOsszege, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 290, 30));

        lbKerdes.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbKerdes.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(lbKerdes, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 484, 720, 30));

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 51));

        lstNyeremenyTabla.setBackground(new java.awt.Color(0, 0, 51));
        lstNyeremenyTabla.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 0)));
        lstNyeremenyTabla.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        lstNyeremenyTabla.setForeground(new java.awt.Color(255, 255, 255));
        lstNyeremenyTabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstNyeremenyTabla.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lstNyeremenyTabla.setFocusable(false);
        lstNyeremenyTabla.setRequestFocusEnabled(false);
        lstNyeremenyTabla.setVerifyInputWhenFocusTarget(false);
        lstNyeremenyTabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstNyeremenyTablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(lstNyeremenyTabla);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 70, 300, 390));

        JPbtn.setBackground(new java.awt.Color(0, 0, 0));
        JPbtn.setOpaque(false);

        lbA.setBackground(new java.awt.Color(0, 0, 0));
        lbA.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lbA.setForeground(new java.awt.Color(255, 255, 0));
        lbA.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbAMouseClicked(evt);
            }
        });

        lbB.setBackground(new java.awt.Color(0, 0, 0));
        lbB.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lbB.setForeground(new java.awt.Color(255, 255, 0));
        lbB.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbBMouseClicked(evt);
            }
        });

        lbC.setBackground(new java.awt.Color(0, 0, 0));
        lbC.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lbC.setForeground(new java.awt.Color(255, 255, 0));
        lbC.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbCMouseClicked(evt);
            }
        });

        lbD.setBackground(new java.awt.Color(0, 0, 0));
        lbD.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lbD.setForeground(new java.awt.Color(255, 255, 0));
        lbD.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbDMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout JPbtnLayout = new javax.swing.GroupLayout(JPbtn);
        JPbtn.setLayout(JPbtnLayout);
        JPbtnLayout.setHorizontalGroup(
            JPbtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPbtnLayout.createSequentialGroup()
                .addGroup(JPbtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbA, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                    .addComponent(lbC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(57, 57, 57)
                .addGroup(JPbtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbD, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                    .addComponent(lbB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        JPbtnLayout.setVerticalGroup(
            JPbtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPbtnLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(JPbtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbB, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbA, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPbtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbD, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                    .addComponent(lbC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel2.add(JPbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 520, 760, 70));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/LOIM.jpg"))); // NOI18N
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 600));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    private void lbAMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbAMouseClicked

        if (lbA.isEnabled()) {
            kiertekel(0);
        }
    }//GEN-LAST:event_lbAMouseClicked

    private void lbBMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbBMouseClicked
        if (lbB.isEnabled()) {
            kiertekel(1);
        }
    }//GEN-LAST:event_lbBMouseClicked

    private void lbCMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbCMouseClicked
        if (lbC.isEnabled()) {
            kiertekel(2);
        }
    }//GEN-LAST:event_lbCMouseClicked

    private void lbDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbDMouseClicked
        if (lbD.isEnabled()) {
            kiertekel(3);
        }
    }//GEN-LAST:event_lbDMouseClicked

    private void lstNyeremenyTablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstNyeremenyTablaMouseClicked
        // az egérrel kijelölt összeg kijelölését semmisé teszi és visszaadja az utolso jelölést
        if (evt.getButton() == 1) {
            lstNyeremenyTabla.clearSelection();
            lstNyeremenyTabla.setSelectedIndex(kerdesSzama);
        }

    }//GEN-LAST:event_lstNyeremenyTablaMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Foablak.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Foablak.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Foablak.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Foablak.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Foablak().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPbtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbA;
    private javax.swing.JLabel lbB;
    private javax.swing.JLabel lbC;
    private javax.swing.JLabel lbCoutDown;
    private javax.swing.JLabel lbD;
    private javax.swing.JLabel lbKerdes;
    private javax.swing.JLabel lbNyeremenyOsszege;
    private javax.swing.JList<String> lstNyeremenyTabla;
    // End of variables declaration//GEN-END:variables
    // private JLabel[] kerdLabelek = {lbA,lbB,lbC,lbD};
}
