package com.metropolitan.sistemzarezervacijukarata;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

public class Film {
    private int id;
    private String drzava;
    private int duzinaTrajanja;
    private String godina;
    private String naslov;
    private String opis;
    private String originalniNaslov;
    private Date pocetakPrikazivanja;
    private String youtubeUrl;
    private String zanr;
    private String slikaUrl;

    private Bitmap slika;

    private FilmAdapter filmAdapter;

    public Film() {
    }

    public Film(String naslov, String slikaUrl) {
        this.naslov = naslov;
        this.slikaUrl = slikaUrl;
        this.slika = null;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getSlikaUrl() {
        return slikaUrl;
    }

    public void setSlikaUrl(String slikaUrl) {
        this.slikaUrl = slikaUrl;
    }

    public Bitmap getSlika() {
        return slika;
    }

    public void setSlika(Bitmap slika) {
        this.slika = slika;
    }

    public FilmAdapter getAdapter() {
        return filmAdapter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public int getDuzinaTrajanja() {
        return duzinaTrajanja;
    }

    public void setDuzinaTrajanja(int duzinaTrajanja) {
        this.duzinaTrajanja = duzinaTrajanja;
    }

    public String getGodina() {
        return godina;
    }

    public void setGodina(String godina) {
        this.godina = godina;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getOriginalniNaslov() {
        return originalniNaslov;
    }

    public void setOriginalniNaslov(String originalniNaslov) {
        this.originalniNaslov = originalniNaslov;
    }

    public Date getPocetakPrikazivanja() {
        return pocetakPrikazivanja;
    }

    public void setPocetakPrikazivanja(Date pocetakPrikazivanja) {
        this.pocetakPrikazivanja = pocetakPrikazivanja;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getZanr() {
        return zanr;
    }

    public void setZanr(String zanr) {
        this.zanr = zanr;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", drzava='" + drzava + '\'' +
                ", duzinaTrajanja=" + duzinaTrajanja +
                ", godina='" + godina + '\'' +
                ", naslov='" + naslov + '\'' +
                ", opis='" + opis + '\'' +
                ", originalniNaslov='" + originalniNaslov + '\'' +
                ", pocetakPrikazivanja=" + pocetakPrikazivanja +
                ", youtubeUrl='" + youtubeUrl + '\'' +
                ", zanr='" + zanr + '\'' +
                ", slikaUrl='" + slikaUrl + '\'' +
                ", slika=" + slika +
                '}';
    }

    public void setAdapter(FilmAdapter filmAdapter) {
        this.filmAdapter = filmAdapter;
    }
    public void ucitajSliku(FilmAdapter filmAdapter) {
        this.filmAdapter = filmAdapter;
        if(slikaUrl != null && !slikaUrl.equals("")) {
            new ImageLoadTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, slikaUrl);
        }
    }

    private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            Log.i("ImageLoadTask", "Loading image...");
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Log.i("ImageLoadTask", "Attempting to load image URL: " + strings[0]);
            try {
                Bitmap b = urlImageToBitmap(strings[0]);
                return  b;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        private Bitmap urlImageToBitmap(String imageUrl) throws Exception {
            Bitmap result = null;
            URL url = new URL(imageUrl);
            if(url != null) {
                result = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            }
            return result;
        }

        protected void onProgressUpdate(String... progress) {

        }

        protected void onPostExecute(Bitmap ret) {
            if(ret != null) {
                Log.i("ImageLoadTask", "Successfully loaded " + naslov + " image");
                slika = ret;
                if(filmAdapter != null) {
                    filmAdapter.notifyDataSetChanged();
                }
            } else {
                Log.e("ImageLoadTask", "Failed to load " + naslov + " image");
            }
        }
    }


}
