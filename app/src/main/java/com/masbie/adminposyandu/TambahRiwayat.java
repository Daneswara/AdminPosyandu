package com.masbie.adminposyandu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TambahRiwayat extends AppCompatActivity {
    public EditText input_berat, input_tinggi, input_lkepala;
    public ProgressDialog pDialog;
    public String input_gizi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_riwayat);
        SharedPreferences pref = this.getApplicationContext().getSharedPreferences("loginadmin", 0);
        final String petugas = pref.getString("nama", "Tidak ada");
        if (pref.getBoolean("akses", true)) {
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
        }
        final String id = getIntent().getExtras().getString("id");
        setTitle("Tambah Riwayat");
        input_berat = (EditText) findViewById(R.id.beratBadan);
        input_tinggi = (EditText) findViewById(R.id.tinggiBadan);
        input_lkepala = (EditText) findViewById(R.id.lingkarKepala);
//        input_keterangan = (EditText) findViewById(R.id.keterangan);



        Button tambah = (Button) findViewById(R.id.tambah);
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    new AttemptSubmit(id, input_berat.getText().toString(), input_tinggi.getText().toString(),
                            input_lkepala.getText().toString(), input_gizi, petugas).execute();
                }
            }
        });

    }
    public boolean validate() {
        boolean valid = true;

        String berat = input_berat.getText().toString();
        String tinggi = input_tinggi.getText().toString();
        String lkepala = input_lkepala.getText().toString();

        if (berat.isEmpty()) {
            input_berat.setError("Berat harus di isi");
            valid = false;
        } else {
            input_berat.setError(null);
        }

        if (tinggi.isEmpty()) {
            input_tinggi.setError("Tinggi ke berapa?");
            valid = false;
        } else {
            input_tinggi.setError(null);
        }

        if (lkepala.isEmpty()) {
            input_lkepala.setError("Lingkar Kepala harus di isi");
            valid = false;
        } else {
            input_lkepala.setError(null);
        }

        return valid;
    }
    class AttemptSubmit extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        String berat, tinggi, lkepala, gizi, keterangan, idAnak, petugas;

        public AttemptSubmit(String idAnak, String berat, String tinggi, String lkepala, String gizi, String petugas) {
            this.idAnak = idAnak;
            this.berat = berat;
            this.tinggi = tinggi;
            this.lkepala = lkepala;
            this.gizi = gizi;
            this.keterangan = "";
            this.petugas = petugas;
        }

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TambahRiwayat.this);
            pDialog.setMessage("Proses Input...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("idAnak", idAnak));
            nameValuePairs.add(new BasicNameValuePair("berat", berat));
            nameValuePairs.add(new BasicNameValuePair("tinggi", tinggi));
            nameValuePairs.add(new BasicNameValuePair("lkepala", lkepala));
            nameValuePairs.add(new BasicNameValuePair("gizi", gizi));
            nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));
            nameValuePairs.add(new BasicNameValuePair("petugas", petugas));

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(
                        "http://posyanduanak.com/mawar/insert_riwayat.php");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = httpClient.execute(httpPost);

                HttpEntity entity = response.getEntity();
                String responseAsString = EntityUtils.toString(response.getEntity());
                return responseAsString;

            } catch (ClientProtocolException e) {
                return e.toString();
            } catch (IOException e) {
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            pDialog.dismiss();
            if (result != null) {
                Toast.makeText(TambahRiwayat.this, result, Toast.LENGTH_LONG).show();
            }
            input_berat.setText("");
            input_tinggi.setText("");
            input_lkepala.setText("");
        }

    }


}
