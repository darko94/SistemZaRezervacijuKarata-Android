package com.metropolitan.sistemzarezervacijukarata;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RezervacijaActivity  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Projekcija projekcija;
    private TextView rezFilmNaslov, rezDatumProjekcije, rezVremeProjekcije, rezSala, rezTehnologija;
    private EditText txtEmail;
    private Spinner brojeviKarata;
    private Button btnRezervisi;
    private int postStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rezervacija_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                sendEmail("Pitanje","");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent i = getIntent();
        projekcija = (Projekcija)i.getSerializableExtra("projekcija");

        rezFilmNaslov = (TextView)findViewById(R.id.rez_filmNaslov);
        rezDatumProjekcije = (TextView)findViewById(R.id.rez_datumProjekcije);
        rezVremeProjekcije = (TextView)findViewById(R.id.rez_vremeProjekcije);
        rezSala = (TextView)findViewById(R.id.rez_sala);
        rezTehnologija = (TextView)findViewById(R.id.rez_tehnologija);
        btnRezervisi = (Button)findViewById(R.id.btnRezervisi);
        txtEmail = (EditText)findViewById(R.id.email);
        brojeviKarata = (Spinner)findViewById(R.id.brojeviKarata);

        rezFilmNaslov.setText(projekcija.getFilmId().getNaslov());
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy.");
        rezDatumProjekcije.setText(df.format(projekcija.getDatum()));
        rezVremeProjekcije.setText(projekcija.getVreme());
        rezSala.setText(projekcija.getSalaId().getNaziv());
        rezTehnologija.setText(projekcija.getSalaId().getTehnologija());


        btnRezervisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean emailIBrojKarataOk = true;
                String email = txtEmail.getText().toString();
                Pattern VALID_EMAIL_ADDRESS_REGEX =
                        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

                Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
                if(!matcher.find()) {
                    Toast.makeText(getApplicationContext(), "Neispravan e-mail!", Toast.LENGTH_SHORT).show();
                    emailIBrojKarataOk = false;
                }

                int brojKarata = 0;
                try {
                    brojKarata = Integer.parseInt(brojeviKarata.getSelectedItem().toString());
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Morate odabrati broj karata!", Toast.LENGTH_SHORT).show();
                    emailIBrojKarataOk = false;
                }

                if(emailIBrojKarataOk) {
                    Korisnik korisnik = new Korisnik(0, null, email, "", "", "", "", "");
                    Rezervacija rezervacija = new Rezervacija(0, brojKarata, korisnik, projekcija);


                    JSONObject jsonObjectKorisnik = new JSONObject();
                    try {
                        jsonObjectKorisnik.put("datumRodjenja", korisnik.getDatumRodjenja());
                        jsonObjectKorisnik.put("email", korisnik.getEmail());
                        jsonObjectKorisnik.put("id", korisnik.getId());
                        jsonObjectKorisnik.put("ime", korisnik.getIme());
                        jsonObjectKorisnik.put("password", korisnik.getPassword());
                        jsonObjectKorisnik.put("pol", korisnik.getPol());
                        jsonObjectKorisnik.put("username", korisnik.getUsername());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONObject jsonObjectProjekcija = new JSONObject();
                    try {
                        jsonObjectProjekcija.put("id", projekcija.getId());
                        jsonObjectProjekcija.put("slobodnoSedista", projekcija.getSlobodnoSedista() - rezervacija.getBrojKarata());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JSONObject jsonObjectRezervacija = new JSONObject();
                    try {
                        jsonObjectRezervacija.put("id", rezervacija.getId());
                        jsonObjectRezervacija.put("brojKarata", rezervacija.getBrojKarata());
                        jsonObjectRezervacija.put("korisnikId", jsonObjectKorisnik);
                        jsonObjectRezervacija.put("projekcijaId", jsonObjectProjekcija);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        new SacuvajRezervaciju().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, jsonObjectRezervacija.toString()).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if(String.valueOf(postStatus).startsWith("2")) {
                        Toast.makeText(getApplicationContext(), "Uspešna rezervacija!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Rezervacija nije uspela!", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });
    }

    private class SacuvajRezervaciju extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(getBaseContext(), "Preuzimanje JSON podataka", Toast.LENGTH_LONG).show();
        }
        @Override
        protected Void doInBackground(String... args) {
            try {
                URL url = new URL("http://192.168.18.143:8080/SistemZaRezervacijuKarata-CS230/rest/sistemzarezervacijukarata.cs230.entities.rezervacija");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(args[0]);
                os.flush();
                os.close();
                postStatus = conn.getResponseCode();
                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), e.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
            }
            return null;
        }


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

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.pocetna) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        } else if (id == R.id.kontakt) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+381612098137"));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.CALL_PHONE},
                        1);
            }
            startActivity(intent);
        } else if (id == R.id.lokacija) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/place/Univerzitet+Metropolitan/@43.3069794,21.9451897,549m/data=!3m1!1e3!4m5!3m4!1s0x4755b06571362991:0x25d6cceab952b797!8m2!3d43.3070305!4d21.9472396"));
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //---slanje e-mail poruke na drugi uredjaj---
    private void sendEmail(String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        String[] to = {"darko.misic.2305@metropolitan.ac.rs"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email"));
    }
}
