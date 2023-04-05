/* Zsolt Ollé */
package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;


/**
 *
 * @author Zsolt Ollé
 */
public class Labelek {

    private String[] kerdesJel = {"A: ", "B: ", "C: ", "D: "};
    private JLabel lbKerdes, lbCoutDown;
    private int playerValasza;
    private List<JLabel> kerdLabelek = new ArrayList<>();

    
    public Labelek() {
    }
    
    public Labelek(JLabel lbKerdes, JLabel lbCoutDown, JLabel lbA, JLabel lbB, JLabel lbC, JLabel lbD) {
        this.lbKerdes = lbKerdes;
        this.lbCoutDown = lbCoutDown;
        kerdLabelek.add(lbA);
        kerdLabelek.add(lbB);
        kerdLabelek.add(lbC);
        kerdLabelek.add(lbD);
    }

// labelek megjelolése betükkel abcd
    public void labelAlapBeallit() {
        for (int i = 0; i < kerdLabelek.size(); i++) {
            kerdLabelek.get(i).setText(kerdesJel[i]);
        }
    }

// vissza állít minden az eredetire
    public void labelAlapszin() {
        for (JLabel kerdLabel : kerdLabelek) {
            kerdLabel.setEnabled(true);
            kerdLabel.setOpaque(false);
            kerdLabel.setBackground(Color.BLACK);
            kerdLabel.setForeground(Color.YELLOW);
        }
    }

// kérdés megjelenítése Labelekben
    public void kerdesekKiiratasa(Kerdes kerdes) {
        lbKerdes.setText(kerdes.getKerdes());
        kerdLabelek.get(0).setText(kerdLabelek.get(0).getText() + kerdes.getValasz0());
        kerdLabelek.get(1).setText(kerdLabelek.get(1).getText() + kerdes.getValasz1());
        kerdLabelek.get(2).setText(kerdLabelek.get(2).getText() + kerdes.getValasz2());
        kerdLabelek.get(3).setText(kerdLabelek.get(3).getText() + kerdes.getValasz3());
        System.out.println(kerdes.getHelyesValasz());
    }

// játékos válasza megjelöl
    public void jatekosMegjelol(int num) {
        // tiltás hogy töbször ne lehessen ráklikkelni
        for (JLabel kerdLabel : kerdLabelek) {
            kerdLabel.setEnabled(false);
        }
        playerValasza = num;        
        kerdLabelek.get(num).setOpaque(true);
        kerdLabelek.get(num).setBackground(Color.ORANGE);
        kerdLabelek.get(num).setForeground(Color.GRAY);

    }

// gép kijelöli a jo választ
    public void gepJelol(int joValaszNum) {
        kerdLabelek.get(joValaszNum).setOpaque(true);
        kerdLabelek.get(joValaszNum).setBackground(Color.GREEN);
        kerdLabelek.get(joValaszNum).setForeground(Color.GRAY);
    }
   
// visszaszámlálás megjelenítése
    public void countdown(int num) {
        lbCoutDown.setText(num + "");
    }

    // GET-TEREK  
    public int getPlayerValasza() {
        return playerValasza;
    }

}
