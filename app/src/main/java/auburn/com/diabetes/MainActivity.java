package auburn.com.diabetes;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import data.ReadingDatabaseHandler;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String page = null;
    private ReadingDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        decideFragment();
        db = new ReadingDatabaseHandler(getApplication());
    }

    private void decideFragment() {
        Intent intent = getIntent();
        Fragment f = new Overview();
        getSupportActionBar().setTitle("Overview");
        if (intent != null) {
            String s = intent.getStringExtra("fragment");
            if (s != null) {
                if (s.equals("education")) {
                    f = new Education();
                    getSupportActionBar().setTitle("Education");
                } else if (s.equals("reading")) {
                    f = new Reading();
                    getSupportActionBar().setTitle("Reading");
                } else if (s.equals("medication")) {
                    f = new Medication();
                    getSupportActionBar().setTitle("Medication");
                }else if (s.equals("setting")) {
                    f = new Setting();
                    getSupportActionBar().setTitle("Setting");
                }
            }
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.overview, f).commit();
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
        getMenuInflater().inflate(R.menu.overview, menu);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("fragment", this.page);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_overall) {
            fragment = new Overview();
            getSupportActionBar().setTitle("Overview");
        } else if (id == R.id.nav_readings) {
            fragment = new Reading();
            getSupportActionBar().setTitle("Reading");
        } else if (id == R.id.nav_medication){
            fragment = new Medication();
            getSupportActionBar().setTitle("Medication");
        }else if (id == R.id.nav_education) {
            fragment = new Education();
            getSupportActionBar().setTitle("Education");
            this.page = "reading";
        } else if (id == R.id.nav_quiz) {
            startActivity(new Intent(getApplicationContext(),Quiz.class));
            return true;
        } else if (id == R.id.nav_setting) {
            fragment = new Setting();
            getSupportActionBar().setTitle("Setting");
        } else if (id == R.id.nav_website) {
            fragment = new Website();
            getSupportActionBar().setTitle("Website");
        } else if (id == R.id.nav_email) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/html");
            intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "My Personal Glucose Information "+new java.util.Date().toString());
            intent.putExtra(Intent.EXTRA_TEXT, "My total Glucose average is "+ db.getAvg() +".");
            try{
                startActivity(intent);
                return true;
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (id == R.id.nav_message) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "send your diabetes data through");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "My total Glucose average is "+ db.getAvg() +".");
            startActivity(Intent.createChooser(shareIntent, "Diabetes overview"));

//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setType("vnd.android-dir/mms-sms");
//            intent.putExtra("address", "911");
//            intent.putExtra("sms_body", "My daily Glucose average is 100mg/dL. Three month average is 99mg/dl. From tester");
//            try{
//                startActivity(intent);
//            } catch (Exception ex) {
//                Toast.makeText(getApplicationContext(), "There are no message clients installed.", Toast.LENGTH_SHORT).show();
//                return false;
//            }
        }else {

        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.overview, fragment).commit();
        fragmentManager.popBackStackImmediate();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
