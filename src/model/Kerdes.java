/* @author Zsolt Ollé */
package model;


public class Kerdes {
    private int id;
    private String kerdes;
    private String valasz0;
    private String valasz1;
    private String valasz2;
    private String valasz3;
    private int helyesValasz;
    
    
    public Kerdes() {
    }

    public Kerdes(String kerdes, String valasz0, String valasz1, String valasz2, String valasz3, int helyesValasz) {
        this.kerdes = kerdes;
        this.valasz0 = valasz0;
        this.valasz1 = valasz1;
        this.valasz2 = valasz2;
        this.valasz3 = valasz3;
        this.helyesValasz = helyesValasz;
    }

    public Kerdes(int id, String kerdes, String valasz0, String valasz1, String valasz2, String valasz3, int helyesValasz) {
        this.id = id;
        this.kerdes = kerdes;
        this.valasz0 = valasz0;
        this.valasz1 = valasz1;
        this.valasz2 = valasz2;
        this.valasz3 = valasz3;
        this.helyesValasz = helyesValasz;
    }
   
   
    public int getHelyesValasz() {
        return helyesValasz;
    }

    public void setHelyesValasz(int helyesValasz) {
        this.helyesValasz = helyesValasz;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKerdes() {
        return kerdes;
    }

    public void setKerdes(String kerdes) {
        this.kerdes = kerdes;
    }

    public String getValasz0() {
        return valasz0;
    }

    public void setValasz0(String valasz0) {
        this.valasz0 = valasz0;
    }

    public String getValasz1() {
        return valasz1;
    }

    public void setValasz1(String valasz1) {
        this.valasz1 = valasz1;
    }

    public String getValasz2() {
        return valasz2;
    }

    public void setValasz2(String valasz2) {
        this.valasz2 = valasz2;
    }

    public String getValasz3() {
        return valasz3;
    }

    public void setValasz3(String valasz3) {
        this.valasz3 = valasz3;
    }
    
    
}
