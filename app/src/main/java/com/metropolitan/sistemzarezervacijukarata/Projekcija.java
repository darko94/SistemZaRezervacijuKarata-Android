package com.metropolitan.sistemzarezervacijukarata;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Projekcija implements Serializable {
    private int id;
    private int slobodnoSedista;
    private Date datum;
    private String vreme;
    private FilmBezSlike filmId;
    private Sala salaId;

    public Projekcija() {
    }

    public Projekcija(int id, int slobodnoSedista, Date datum, String vreme, FilmBezSlike filmId, Sala salaId) {
        this.id = id;
        this.slobodnoSedista = slobodnoSedista;
        this.datum = datum;
        this.vreme = vreme;
        this.filmId = filmId;
        this.salaId = salaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSlobodnoSedista() {
        return slobodnoSedista;
    }

    public void setSlobodnoSedista(int slobodnoSedista) {
        this.slobodnoSedista = slobodnoSedista;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getVreme() {
        return vreme;
    }

    public void setVreme(String vreme) {
        this.vreme = vreme;
    }

    public FilmBezSlike getFilmId() {
        return filmId;
    }

    public void setFilmId(FilmBezSlike filmId) {
        this.filmId = filmId;
    }

    public Sala getSalaId() {
        return salaId;
    }

    public void setSalaId(Sala salaId) {
        this.salaId = salaId;
    }

    DateFormat df = new SimpleDateFormat("dd.MM.yyyy.");

    @Override
    public String toString() {
        if(id == -1) {
            return "Odaberite projekciju...";
        } else {
            return df.format(datum) + " " + vreme + " - " + salaId.getNaziv() + " - " + salaId.getTehnologija();
        }
    }
}
