package com.example.hp.jbooks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
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

import com.example.hp.jbooks.database.DatabaseHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Mainfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView nameheader, emailheader, no_recents;
    RecyclerView recentsView;
    List<Subject_Contents> recents;
    DatabaseHelper db;
    RecentsAdapter recentsAdapter;
    String email, name;
    SharedPreferences mpreferences;
    View ChildView;
    int GetItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainfile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mpreferences = this.getSharedPreferences(getResources().getString(R.string.sharedPreferenceKey), Context.MODE_PRIVATE);
        name = mpreferences.getString(getResources().getString(R.string.preferenceNameKey), "");
        email = mpreferences.getString(getResources().getString(R.string.preferenceEmailKey), "");
        Toast.makeText(getApplicationContext(), name + " " + email,
                Toast.LENGTH_LONG).show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        nameheader = (TextView) headerLayout.findViewById(R.id.nameheader);
        emailheader = (TextView) headerLayout.findViewById(R.id.emailheader);

        recentsView = (RecyclerView) findViewById(R.id.recentView);
        no_recents = (TextView) findViewById(R.id.no_recents_text);

        nameheader.setText(name);
        emailheader.setText(email);

        recentsView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(Mainfile.this, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    GetItemPosition = Recyclerview.getChildAdapterPosition(ChildView);

                    String link = recents.get(GetItemPosition).getLink();
                    String name = recents.get(GetItemPosition).getName();
                    if (!db.checkRecent(link))
                        db.insertRecent(name, link);
                    Intent intent = new Intent(Mainfile.this, WebViewpdf.class);
                    intent.putExtra("link", link);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        db = new DatabaseHelper(this);


        recents = new ArrayList<>();
        recents = db.getAllRecents();

        if (recents.size() != 0) {
            no_recents.setVisibility(View.GONE);
            recentsView.setVisibility(View.VISIBLE);
            recentsAdapter = new RecentsAdapter(recents);

            recentsView.setHasFixedSize(true);
            recentsView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recentsView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
            recentsView.setItemAnimator(new DefaultItemAnimator());
            recentsView.setAdapter(recentsAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            //Login.super.onBackPressed();
                            moveTaskToBack(true);
                            Mainfile.this.finish();
                        }
                    }).create().show();
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
            Intent intent = new Intent(Mainfile.this, Category.class);
            intent.putExtra("id_subject", 1);
            startActivity(intent);
        } else if (id == R.id.nav_chemistry) {
            Intent intent = new Intent(Mainfile.this, Category.class);
            intent.putExtra("id_subject", 2);
            startActivity(intent);
        } else if (id == R.id.nav_maths) {
            Intent intent = new Intent(Mainfile.this, Category.class);
            intent.putExtra("id_subject", 3);
            startActivity(intent);
        } else if (id == R.id.nav_aboutus) {
            Intent intent = new Intent(Mainfile.this, AboutUs.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            mpreferences = this.getSharedPreferences(getResources().getString(R.string.sharedPreferenceKey), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mpreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(Mainfile.this, Login.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            ShareCompat.IntentBuilder
                    .from(Mainfile.this)
                    .setType("text/plain")
                    .setChooserTitle("Share Via")
                    .setText(getResources().getString(R.string.share_text))
                    .startChooser();
        } else if (id == R.id.nav_feedback) {
            Intent intent = new Intent(Mainfile.this, Feedback.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
