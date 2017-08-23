package com.masbie.adminposyandu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Profil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        setTitle("Akun");
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("loginadmin", 0);
        if(pref.getBoolean("akses", true)){
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
            finish();
        } else {
            if (pref.getBoolean("pertama", true)) {
//            Intent intent = new Intent(this, SplashActivity.class);
//            this.startActivity(intent);
            }
            final TextView telp = (TextView) findViewById(R.id.telp);
            final TextView nama = (TextView) findViewById(R.id.nama);
            final String id = pref.getString("telp", "Tidak ada");
            final String nama1 = pref.getString("nama", "Tidak ada");
            telp.setText(id);
            nama.setText(nama1);
            TextView edit = (TextView) findViewById(R.id.teksEdit);
            TextView logout = (TextView) findViewById(R.id.logout);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Profil.this, EditAkun.class);
                    intent.putExtra("id", id);
                    intent.putExtra("nama", nama1);
                    finish();
                    startActivity(intent);
                }
            });
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    Intent intent = new Intent(Profil.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}
