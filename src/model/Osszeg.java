/* Zsolt Ollé */
package model;

import java.util.Vector;
import java.util.regex.Pattern;

/**
 *
 * @author Zsolt Ollé
 */
public class Osszeg {

    private Vector<String> ertekToList = new Vector<>();
    private String[] ertekek = {"40 000 000", "20 000 000", "10 000 000", "5 000 000", "3 000 000", "1 500 000",
        "800 000", "500 000", "300 000", "200 000", "100 000", "50 000", "25 000", "10 000", "5 000"};

    public Osszeg() {
        
        for (String ertek : ertekek) {
            if (ertek.equalsIgnoreCase("100 000") || ertek.equalsIgnoreCase("1 500 000") || ertek.equalsIgnoreCase("40 000 000")) {
                ertekToList.add("<html><b style=\"color:Yellow;\">" + ertek + "</b><html>");
            } else {
                ertekToList.add(ertek);
            }
        }
    }

    public void OsszegLep(String nyeremeny, int num) {
        if (nyeremeny.equalsIgnoreCase("100 000") || nyeremeny.equalsIgnoreCase("1 500 000") || nyeremeny.equalsIgnoreCase("40 000 000")) {
            String er = ertekToList.get(num);
            er = er.replaceAll(Pattern.quote("Yellow"), "Orange"); // felcserélem a 2 színt
            ertekToList.set(num, er); // vissza adom már az átformázott értéket
        } else {
           // ertekToList.set(num, "<html><b style=\"color:Gray;\">" + nyeremeny + "</b><html>");
             ertekToList.set(num+1, "<html><b style=\"color:Gray;\">" + nyeremeny + "</b><html>"); // itt van a hiba
        }
    }

// GET-te és SET-ter
    public Vector<String> getErtekToList() {
        return ertekToList;
    }

    public String getErtek(int num) {
        if(num > -1){
            return ertekek[num];
        }
        return ertekek[0];
    }

    public String[] getErtekek() {
        return ertekek;
    }

    public String getOsszeg(int num) {
        return ertekToList.get(num);
    }

}
