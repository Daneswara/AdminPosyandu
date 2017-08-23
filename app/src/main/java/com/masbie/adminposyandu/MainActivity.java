package com.masbie.adminposyandu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity implements Anak.OnFragmentInteractionListener, Notifikasi.OnFragmentInteractionListener, PengaturanGrafik.OnFragmentInteractionListener, Riwayat.OnFragmentInteractionListener, Petugas.OnFragmentInteractionListener {
    TextView mTitle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar.setTitle(mTitle.getText().toString());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        BottomBar bottomBar = (BottomBar) findViewById(R.id.menubawah);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Fragment fragment = null;
                Class fragmentClass = null;
                if (tabId == R.id.tab_anak) {
                    fragmentClass = Anak.class;
                    mTitle.setText("Anak");
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                } else if (tabId == R.id.tab_riwayat) {
                    fragmentClass = Riwayat.class;
                    mTitle.setText("Riwayat");
                } else if (tabId == R.id.tab_statistik) {
                    fragmentClass = PengaturanGrafik.class;
                    mTitle.setText("Pengaturan Grafik");
                } else if (tabId == R.id.tab_akun) {
                    fragmentClass = Notifikasi.class;
                    mTitle.setText("Notifikasi");
                } else if (tabId == R.id.tab_petugas) {
                    fragmentClass = Petugas.class;
                    mTitle.setText("Petugas");
                } else {
                    fragmentClass = Anak.class;
                    mTitle.setText("Anak");

                }
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                toolbar.setTitle(mTitle.getText().toString());
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(this, Profil.class);
            this.startActivity(intent);
        }
// else if (id == R.id.bantuan) {
//            Intent intent = new Intent(this, Grafik.class);
//            this.startActivity(intent);
//        }
//        else if (id == R.id.keluar) {
//            SharedPreferences pref = getApplicationContext().getSharedPreferences("loginadmin", 0);
//            pref.edit().remove("akses").commit();
//            pref.edit().remove("notelp").commit();
//            pref.edit().remove("nama").commit();
//            Intent intent = new Intent(this, LoginActivity.class);
//            this.startActivity(intent);
//            finish();
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
