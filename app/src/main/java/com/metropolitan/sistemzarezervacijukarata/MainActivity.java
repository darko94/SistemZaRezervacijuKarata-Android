package com.metropolitan.sistemzarezervacijukarata;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GridView gridView;
    private ArrayList<Bitmap> bitmapList;
    private ProgressDialog dialog;
    List<Film> listaFilmova;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        this.gridView = (GridView)findViewById(R.id.gridView);
        listaFilmova = new ArrayList<>();
        /*listaFilmova.add(new Film("Test film 1", "https://img.cineplexx.at/media/rs/inc/movies_licences/223_8.png"));
        listaFilmova.add(new Film("Test film 2", "https://img.cineplexx.at/media/rs/inc/movies_licences/223_8.png"));
        listaFilmova.add(new Film("Deadpool 2", "https://img.cineplexx.at/media/rs/inc/movies_licences/223_8.png"));
        listaFilmova.add(new Film("Test film 4", "https://img.cineplexx.at/media/rs/inc/movies_licences/223_8.png"));
        listaFilmova.add(new Film("Test film 5", "https://img.cineplexx.at/media/rs/inc/movies_licences/223_8.png"));
        listaFilmova.add(new Film("Test film 1", "https://img.cineplexx.at/media/rs/inc/movies_licences/223_8.png"));
        listaFilmova.add(new Film("Test film 2", "https://img.cineplexx.at/media/rs/inc/movies_licences/223_8.png"));
        listaFilmova.add(new Film("Deadpool 2", "https://img.cineplexx.at/media/rs/inc/movies_licences/223_8.png"));
        listaFilmova.add(new Film("Test film 4", "https://img.cineplexx.at/media/rs/inc/movies_licences/223_8.png"));
        listaFilmova.add(new Film("Test film 5", "https://img.cineplexx.at/media/rs/inc/movies_licences/223_8.png"));*/

        try {
            new UcitajFilmove().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        FilmAdapter filmAdapter = new FilmAdapter(this, listaFilmova);
        gridView.setAdapter(filmAdapter);

        for(Film f : listaFilmova) {
            Log.i("FILM: ",f.toString());
            //f.ucitajSliku(filmAdapter);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "Kliknuto je na " + listaFilmova.get(position), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), FilmActivity.class);
                i.putExtra("film", new FilmBezSlike(listaFilmova.get(position)));
                startActivity(i);
            }
        });

    }



    @Override
    public void onBackPressed() {
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

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class UcitajFilmove extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getBaseContext(), "Preuzimanje JSON podataka", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... args) {
            HttpHandler sh = new HttpHandler();
            //String url = "https://my-json-server.typicode.com/darko94/cs330-dz10/db/";
            String url = "http://192.168.18.143:8080/SistemZaRezervacijuKarata-CS230/rest/filmovi/";


            try {
                WordDefinition("test");
            } catch (Exception e) {
                e.printStackTrace();
            }

//            String jsonStr = sh.makeServiceCall(url);
//
//            Log.e("ODGOVOR sa URL-a", "Odgovor sa url-a: " + jsonStr);
//
//            if(jsonStr != null) {
//                try {
//
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//                    JSONArray filmovi = jsonObj.getJSONArray("filmovi");
//
//                    for (int i = 0; i < filmovi.length(); i++) {
//                        JSONObject jsonObject = filmovi.getJSONObject(i);
//                        String naslov= jsonObject.getString("naslov");
//                        String slikaUrl = jsonObject.getString("slikaUrl");
//
//
//                        Film film = new Film(naslov, slikaUrl);
//
//                        film.setSlika(urlImageToBitmap(film.getSlikaUrl()));
//
//                        listaFilmova.add(film);
//                    }
//                } catch (final JSONException e) {
//                    Log.e("ODGOVOR sa URL-a", "Greska u parsiranju JSON-a: " + e.getMessage());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), "Greska u parsiranju JSON-a: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Log.e("ODGOVOR sa URL-a", "Nije moguce preuzeti JSON.");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(), "Nije moguce preuzeti JSON. Proverite Logcat za vise detalja o gresci.", Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
            return null;
        }
        private Bitmap urlImageToBitmap(String imageUrl) throws Exception {
            Bitmap result = null;
            URL url = new URL(imageUrl);
            if(url != null) {
                result = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            }
            return result;
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
                    "http://192.168.18.143:8080/SistemZaRezervacijuKarata-CS230/rest/filmovi/");
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
                    doc.getElementsByTagName("film");

            //---iteracija kroz svaki <Definition> element---
            for (int i = 0; i < definitionElements.getLength(); i++) {
                Node itemNode = definitionElements.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE)
                {

                    //---konverzija Definition čvora u Element---
                    Element definitionElement = (Element) itemNode;



                    String naslov = definitionElement.getElementsByTagName("naslov").item(0).getChildNodes().item(0).getNodeValue();
                    String originalniNaslov = definitionElement.getElementsByTagName("originalniNaslov").item(0).getChildNodes().item(0).getNodeValue();
                    String drzava = definitionElement.getElementsByTagName("drzava").item(0).getChildNodes().item(0).getNodeValue();
                    String godina = definitionElement.getElementsByTagName("godina").item(0).getChildNodes().item(0).getNodeValue();
                    String opis = definitionElement.getElementsByTagName("opis").item(0).getChildNodes().item(0).getNodeValue();
                    String pocetakPrikazivanjaStr = definitionElement.getElementsByTagName("pocetakPrikazivanja").item(0).getChildNodes().item(0).getNodeValue().substring(0, 10);
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date pocetakPrikazivanja = df.parse(pocetakPrikazivanjaStr);
                    String youtubeUrl = definitionElement.getElementsByTagName("youtubeUrl").item(0).getChildNodes().item(0).getNodeValue();
                    String slikaUrl = definitionElement.getElementsByTagName("slikaUrl").item(0).getChildNodes().item(0).getNodeValue();
                    String zanr = definitionElement.getElementsByTagName("zanr").item(0).getChildNodes().item(0).getNodeValue();
                    int duzinaTrajanja = Integer.parseInt(definitionElement.getElementsByTagName("duzinaTrajanja").item(0).getChildNodes().item(0).getNodeValue());
                    int id = Integer.parseInt(definitionElement.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue());

                    Film film = new Film();
                    film.setDrzava(drzava);
                    film.setDuzinaTrajanja(duzinaTrajanja);
                    film.setGodina(godina);
                    film.setId(id);
                    film.setNaslov(naslov);
                    film.setOriginalniNaslov(originalniNaslov);
                    film.setOpis(opis);
                    film.setPocetakPrikazivanja(pocetakPrikazivanja);
                    film.setSlikaUrl(slikaUrl);
                    film.setYoutubeUrl(youtubeUrl);
                    film.setZanr(zanr);
                    listaFilmova.add(film);

                    film.setSlika(urlImageToBitmap(film.getSlikaUrl()));

                    Log.i("FILM", film.toString());
                    Log.i("ORIGINALNI NASLOV", originalniNaslov);
                    //---preuzimanje svih <WordDefinition> elemenata iz
                    // <Definition> elementa---
                    NodeList wordDefinitionElements =
                            (definitionElement).getElementsByTagName(
                                    "WordDefinition");

                    strDefinition = "";
                    //---iteracija kroz svaki <WordDefinition> element---
                    for (int j = 0; j < wordDefinitionElements.getLength(); j++) {
                        //---konverzija <WordDefinition> Ävora u Element---
                        Element wordDefinitionElement =
                                (Element) wordDefinitionElements.item(j);

                        //---pruzimanje izvedenih čvorova iz
                        // <WordDefinition> elementa---
                        NodeList textNodes =
                                ((Node) wordDefinitionElement).getChildNodes();
                        strDefinition +=
                                ((Node) textNodes.item(0)).getNodeValue() + ". \n";
                    }
                }
            }
        } catch (IOException e1) {
            Log.d("NetworkingActivity", e1.getLocalizedMessage());
        }
        //---vraćanje definicije reči---
        return strDefinition;
    }

}
