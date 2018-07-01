package com.metropolitan.sistemzarezervacijukarata;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class FilmActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView filmNaslov, filmOriginalniNaslov, filmPocetakPrikazivanja, filmDuzinaTrajanja, filmDrzavaGodina, filmZanr, filmOpis;
    private WebView youtubeVideo;
    private Spinner spinner;
    Button btnRezervisi;
    private FilmBezSlike film;
    private List<Projekcija> listaProjekcija;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.film_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        filmNaslov = (TextView)findViewById(R.id.film_naslov);
        filmOriginalniNaslov = (TextView)findViewById(R.id.film_originalni_naslov);
        filmPocetakPrikazivanja = (TextView)findViewById(R.id.film_pocetak_prikazivanja);
        filmDuzinaTrajanja = (TextView)findViewById(R.id.film_duzina_trajanja);
        filmDrzavaGodina = (TextView)findViewById(R.id.film_drzava_godina);
        filmZanr = (TextView)findViewById(R.id.film_zanr);
        filmOpis = (TextView)findViewById(R.id.film_opis);
        youtubeVideo = (WebView)findViewById(R.id.youtube_video);
        spinner = (Spinner)findViewById(R.id.lista_projekcija);
        btnRezervisi = (Button)findViewById(R.id.btnRezervisi);

        Intent i = getIntent();
        film = (FilmBezSlike)i.getSerializableExtra("film");
        filmNaslov.setText(film.getNaslov());
        filmOriginalniNaslov.setText(film.getOriginalniNaslov());
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy.");
        filmPocetakPrikazivanja.setText(df.format(film.getPocetakPrikazivanja()));
        filmDuzinaTrajanja.setText(film.getDuzinaTrajanja()+"");
        filmDrzavaGodina.setText(film.getDrzava() + "/" + film.getGodina());
        filmZanr.setText(film.getZanr());
        filmOpis.setText(film.getOpis());

        WebSettings webSettings = youtubeVideo.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);


        youtubeVideo.setWebViewClient(new Callback());
        youtubeVideo.loadUrl(film.getYoutubeUrl());

        listaProjekcija = new ArrayList<>();
        listaProjekcija.add(new Projekcija(-1, 0, null, "", null, new Sala()));

        try {
            new UcitajProjekcije().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ArrayAdapter<Projekcija> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaProjekcija);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        btnRezervisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Projekcija odabranaProjekcija = (Projekcija)spinner.getSelectedItem();
                if(odabranaProjekcija.toString().equals("Odaberite projekciju...")) {
                    Toast.makeText(getApplicationContext(), "Morate odabrati projekciju!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(getApplicationContext(), RezervacijaActivity.class);
                    i.putExtra("projekcija", odabranaProjekcija);
                    startActivity(i);
                }
            }
        });

    }



    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }



    @Override
    public void onBackPressed() {
        youtubeVideo.loadUrl("about:blank");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            youtubeVideo.loadUrl("about:blank");
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class UcitajProjekcije extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(getBaseContext(), "Preuzimanje JSON podataka", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... args) {

            try {
                WordDefinition("test");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    private InputStream OpenHttpConnection(String urlString)
            throws IOException
    {
        InputStream in = null;
        int response = -1;

        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Nema HTTP Konekcije");
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }
        catch (Exception ex)
        {
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Greška u povezivanju");
        }
        return in;
    }

    private String WordDefinition(String word) throws Exception {
        Log.i("WordDefinition", "Usao u word definition");
        InputStream in = null;
        String strDefinition = "";
        try {
            in = OpenHttpConnection(
                    "http://192.168.18.143:8080/SistemZaRezervacijuKarata-CS230/rest/filmovi/projekcijeZaFilm/" + film.getId());
            Document doc = null;
            DocumentBuilderFactory dbf =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
            try {
                db = dbf.newDocumentBuilder();
                doc = db.parse(in);
            } catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            doc.getDocumentElement().normalize();

            //---prihvatanje svih <Definition> elemenata---
            NodeList definitionElements =
                    doc.getElementsByTagName("projekcija");

            //---iteracija kroz svaki <Definition> element---
            for (int i = 0; i < definitionElements.getLength(); i++) {
                Node itemNode = definitionElements.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    //---konverzija Definition čvora u Element---
                    Element definitionElement = (Element) itemNode;

                    int id = Integer.parseInt(definitionElement.getElementsByTagName("id").item(1).getChildNodes().item(0).getNodeValue());
                    int slobodnoSedista = Integer.parseInt(definitionElement.getElementsByTagName("slobodnoSedista").item(0).getChildNodes().item(0).getNodeValue());
                    String datumStr = definitionElement.getElementsByTagName("datum").item(0).getChildNodes().item(0).getNodeValue().substring(0,10);
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date datum = df.parse(datumStr);
                    String vreme = definitionElement.getElementsByTagName("vreme").item(0).getChildNodes().item(0).getNodeValue();

                    int salaId = Integer.parseInt(definitionElement.getElementsByTagName("id").item(2).getChildNodes().item(0).getNodeValue());
                    int brojSedista = Integer.parseInt(definitionElement.getElementsByTagName("brojSedista").item(0).getChildNodes().item(0).getNodeValue());
                    String naziv = definitionElement.getElementsByTagName("naziv").item(0).getChildNodes().item(0).getNodeValue();
                    String tehnologija = definitionElement.getElementsByTagName("tehnologija").item(0).getChildNodes().item(0).getNodeValue();

                    Sala sala = new Sala(salaId, brojSedista, naziv, tehnologija);

                    Log.i("SALA", sala.toString());

                    Projekcija projekcija = new Projekcija();
                    projekcija.setId(id);
                    projekcija.setSlobodnoSedista(slobodnoSedista);
                    projekcija.setDatum(datum);
                    projekcija.setVreme(vreme);
                    projekcija.setFilmId(film);
                    projekcija.setSalaId(sala);

                    listaProjekcija.add(projekcija);
                }
            }
        } catch (IOException e1) {
            Log.d("NetworkingActivity", e1.getLocalizedMessage());
        }
        //---vraćanje definicije reči---
        return strDefinition;
    }
}
