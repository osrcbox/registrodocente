package com.unl.lapc.registrodocente.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.unl.lapc.registrodocente.dao.AcreditableDao;
import com.unl.lapc.registrodocente.dao.ClaseDao;
import com.unl.lapc.registrodocente.dao.ParcialDao;
import com.unl.lapc.registrodocente.dao.QuimestreDao;
import com.unl.lapc.registrodocente.fragment.FragmentResumenNotas;
import com.unl.lapc.registrodocente.R;
import com.unl.lapc.registrodocente.modelo.Acreditable;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.Parcial;
import com.unl.lapc.registrodocente.modelo.Quimestre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainNotas extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ClaseDao dao;
    private AcreditableDao daoAcreditable;
    private QuimestreDao quimestreDao;
    private ParcialDao parcialDao;


    private Clase clase;
    private Map<MenuItem, Object> menuItems = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_notas_nav);
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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Custom init
        Menu menu = navigationView.getMenu();

        dao = new ClaseDao(this);
        daoAcreditable = new AcreditableDao(this);
        quimestreDao = new QuimestreDao(this);
        parcialDao = new ParcialDao(this);

        Bundle bundle = getIntent().getExtras();
        clase = bundle.getParcelable("clase");

        List<Quimestre> quimestres = quimestreDao.getAll(clase.getPeriodo());

        //group_id , item_id , order, nombre
        int gid = 0;
        int id = 0;
        int ord = 0;

        for(Quimestre q : quimestres){
            MenuItem s = menu.add(gid, 0, 0, q.getNombre());
            s.setIcon(R.drawable.ic_dashboard_black_18dp);
            menuItems.put(s, q);

            List<Parcial> parciales = parcialDao.getAll(q);
            for (Parcial p: parciales){
                MenuItem sp = menu.add(gid, 0, 0, p.getNombre());
                sp.setIcon(R.drawable.ic_toc_black_18dp);
                menuItems.put(sp, p);
            }

            gid++;
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
        getMenuInflater().inflate(R.menu.menu_main_notas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Main/Up button, so long
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

        boolean fragmentTransaction = false;
        Fragment fragment = null;

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Object tag = menuItems.get(item);

        //if (id == R.id.nav_camera) {
            // Handle the camera action
            fragment = new FragmentResumenNotas();
            fragmentTransaction = true;
        /*} else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        if(fragmentTransaction) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
