package com.metropolitan.sistemzarezervacijukarata;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

public class Film {
    private String naslov;
    private String slikaUrl;
    private Bitmap slika;

    private FilmAdapter filmAdapter;

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

    @Override
    public String toString() {
        return "Film{" +
                "naslov='" + naslov + '\'' +
                ", slikaUrl='" + slikaUrl + '\'' +
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
