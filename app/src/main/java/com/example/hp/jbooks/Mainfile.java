package com.example.hp.jbooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Mainfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //TextView nameheader,emailheader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainfile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //nameheader=(TextView)findViewById(R.id.nameheader);
        //emailheader=(TextView)findViewById(R.id.emailheader);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        Toast.makeText(getApplicationContext(), name+" "+email,
                Toast.LENGTH_LONG).show();
        //nameheader.setText(name);
        //emailheader.setText(email);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainfile, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_physics) {
            Intent intent=new Intent(Mainfile.this,Category.class);
            intent.putExtra("id_subject",1);
            startActivity(intent);
        } else if (id == R.id.nav_chemistry) {
            Intent intent=new Intent(Mainfile.this,Category.class);
            intent.putExtra("id_subject",2);
            startActivity(intent);
        } else if (id == R.id.nav_maths) {
            Intent intent=new Intent(Mainfile.this,Category.class);
            intent.putExtra("id_subject",3);
            startActivity(intent);
        } else if (id == R.id.nav_aboutus) {
            Intent intent=new Intent(Mainfile.this,AboutUs.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            Intent intent=new Intent(Mainfile.this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            ShareCompat.IntentBuilder
                    .from(Mainfile.this)
                    .setType("text/plain")
                    .setChooserTitle("Share Via")
                    .setText("")
                    .startChooser();
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
