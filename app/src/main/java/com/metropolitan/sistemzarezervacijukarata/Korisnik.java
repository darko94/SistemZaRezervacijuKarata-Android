package com.metropolitan.sistemzarezervacijukarata;

import java.util.Date;

public class Korisnik {
    private int id;
    private Date datumRodjenja;
    private String email;
    private String ime;
    private String prezime;
    private String password;
    private String pol;
    private String username;

    public Korisnik() {
    }

    public Korisnik(int id, Date datumRodjenja, String email, String ime, String prezime, String password, String pol, String username) {
        this.id = id;
        this.datumRodjenja = datumRodjenja;
        this.email = email;
        this.ime = ime;
        this.prezime = prezime;
        this.password = password;
        this.pol = pol;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(Date datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPol() {
        return pol;
    }

    public void setPol(String pol) {
        this.pol = pol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Korisnik{" +
                "id=" + id +
                ", datumRodjenja=" + datumRodjenja +
                ", email='" + email + '\'' +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", password='" + password + '\'' +
                ", pol='" + pol + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
