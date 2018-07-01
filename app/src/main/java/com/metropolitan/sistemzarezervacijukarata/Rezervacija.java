package com.metropolitan.sistemzarezervacijukarata;

public class Rezervacija {
    private int id;
    private int brojKarata;
    private Korisnik korisnikId;
    private Projekcija projekcijaId;

    public Rezervacija(int id, int brojKarata, Korisnik korisnikId, Projekcija projekcijaId) {

        this.id = id;
        this.brojKarata = brojKarata;
        this.korisnikId = korisnikId;
        this.projekcijaId = projekcijaId;
    }

    public Rezervacija() {

    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getBrojKarata() {
        return brojKarata;
    }

    public void setBrojKarata(int brojKarata) {
        this.brojKarata = brojKarata;
    }

    public Korisnik getKorisnikId() {
        return korisnikId;
    }

    public void setKorisnikId(Korisnik korisnikId) {
        this.korisnikId = korisnikId;
    }

    public Projekcija getProjekcijaId() {
        return projekcijaId;
    }

    public void setProjekcijaId(Projekcija projekcijaId) {
        this.projekcijaId = projekcijaId;
    }


    @Override
    public String toString() {
        return "Rezervacija{" +
                "id=" + id +
                ", brojKarata=" + brojKarata +
                ", korisnikId=" + korisnikId +
                ", projekcijaId=" + projekcijaId +
                '}';
    }
}
