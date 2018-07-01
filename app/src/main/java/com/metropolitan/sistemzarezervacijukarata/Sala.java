package com.metropolitan.sistemzarezervacijukarata;

import java.io.Serializable;

public class Sala implements Serializable {
    private int id;
    private int brojSedista;
    private String naziv;
    private String tehnologija;

    public Sala() {
    }

    public Sala(int id, int brojSedista, String naziv, String tehnologija) {
        this.id = id;
        this.brojSedista = brojSedista;
        this.naziv = naziv;
        this.tehnologija = tehnologija;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrojSedista() {
        return brojSedista;
    }

    public void setBrojSedista(int brojSedista) {
        this.brojSedista = brojSedista;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getTehnologija() {
        return tehnologija;
    }

    public void setTehnologija(String tehnologija) {
        this.tehnologija = tehnologija;
    }

    @Override
    public String toString() {
        return "Sala{" +
                "id=" + id +
                ", brojSedista=" + brojSedista +
                ", naziv='" + naziv + '\'' +
                ", tehnologija='" + tehnologija + '\'' +
                '}';
    }
}


