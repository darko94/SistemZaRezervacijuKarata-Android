package com.metropolitan.sistemzarezervacijukarata;

import android.content.Intent;
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
import android.widget.TextView;

public class FilmActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView filmNaslov, filmOriginalniNaslov, filmPocetakPrikazivanja, filmDuzinaTrajanja, filmDrzavaGodina, filmZanr, filmOpis;
    private WebView youtubeVideo;
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

        Intent i = getIntent();
        FilmBezSlike film = (FilmBezSlike)i.getSerializableExtra("film");
        filmNaslov.setText(film.getNaslov());
        filmOriginalniNaslov.setText(film.getOriginalniNaslov());
        filmPocetakPrikazivanja.setText(film.getPocetakPrikazivanja().toString());
        filmDuzinaTrajanja.setText(film.getDuzinaTrajanja()+"");
        filmDrzavaGodina.setText(film.getDrzava() + "/" + film.getGodina());
        filmZanr.setText(film.getZanr());
        filmOpis.setText(film.getOpis());

        WebSettings webSettings = youtubeVideo.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);


        youtubeVideo.setWebViewClient(new Callback());
        youtubeVideo.loadUrl(film.getYoutubeUrl());


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
}
