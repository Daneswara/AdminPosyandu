package com.masbie.adminposyandu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

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
import java.util.Calendar;
import java.util.List;

public class DetailRiwayat extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    TextView tanggal, umur, berat, tinggi, lingkarKepala, gizi, keterangan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat);
        setTitle("Edit Riwayat");
        SharedPreferences pref = this.getApplicationContext().getSharedPreferences("loginadmin", 0);
        final String petugas = pref.getString("nama", "Tidak ada");
        final String id = getIntent().getExtras().getString("id_riwayat");
        tanggal = (TextView) findViewById(R.id.tanggal);
        umur = (TextView) findViewById(R.id.umur);
        berat = (TextView) findViewById(R.id.beratBadan);
        tinggi = (TextView) findViewById(R.id.tinggiBadan);
        lingkarKepala = (TextView) findViewById(R.id.lingkarKepala);
        gizi = (TextView) findViewById(R.id.gizi);
        keterangan = (TextView) findViewById(R.id.keterangan);

        tanggal.setText(getIntent().getExtras().getString("tanggal"));
        umur.setText(getIntent().getExtras().getString("umur"));
        berat.setText(getIntent().getExtras().getString("berat"));
        tinggi.setText(getIntent().getExtras().getString("tinggi"));
        lingkarKepala.setText(getIntent().getExtras().getString("lkepala"));
        gizi.setText(getIntent().getExtras().getString("gizi"));
        keterangan.setText(getIntent().getExtras().getString("keterangan"));
        final String jeniskelamin = getIntent().getExtras().getString("jeniskelamin");
        final String beratLahir = getIntent().getExtras().getString("beratlahir");

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        DetailRiwayat.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
                dpd.setMaxDate(now);
                dpd.setOkText("Pilih");
                dpd.setCancelText("Batal");
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        Button simpan = (Button) findViewById(R.id.simpan);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    new AttemptSubmit(id, tanggal.getText().toString(), umur.getText().toString(),
                            berat.getText().toString(), tinggi.getText().toString(), lingkarKepala.getText().toString(),
                            gizi.getText().toString(), keterangan.getText().toString(), petugas).execute();
                }
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jeniskelamin.equals("Laki-laki")) {
                    Intent intent = new Intent(getApplicationContext(), GrafikTinggi.class);
                    intent.putExtra("umur", getIntent().getExtras().getString("umur"));
                    intent.putExtra("tinggi", getIntent().getExtras().getString("tinggi"));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), GrafikTinggiP.class);
                    intent.putExtra("umur", getIntent().getExtras().getString("umur"));
                    intent.putExtra("tinggi", getIntent().getExtras().getString("tinggi"));
                    startActivity(intent);
                }

            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (jeniskelamin.equals("Laki-laki")) {
                    Intent intent = new Intent(getApplicationContext(), Grafik.class);
                    intent.putExtra("umur", getIntent().getExtras().getString("umur"));
                    intent.putExtra("berat", getIntent().getExtras().getString("berat"));
                    intent.putExtra("beratLahir", beratLahir);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), GrafikP.class);
                    intent.putExtra("umur", getIntent().getExtras().getString("umur"));
                    intent.putExtra("berat", getIntent().getExtras().getString("berat"));
                    intent.putExtra("beratLahir", beratLahir);
                    startActivity(intent);
                }
            }
        });
    }

    private ProgressDialog pDialog;
    public boolean validate() {
        boolean valid = true;

        String text_tanggal = tanggal.getText().toString();
        String text_umur = umur.getText().toString();
        String text_berat = berat.getText().toString();
        String text_tinggi = tinggi.getText().toString();
        String text_lingkarKepala = lingkarKepala.getText().toString();
        String text_gizi = gizi.getText().toString();
        String text_keterangan = keterangan.getText().toString();

        if (text_tanggal.isEmpty()) {
            tanggal.setError("Tanngal harus di isi");
            valid = false;
        } else {
            tanggal.setError(null);
        }

        if (text_umur.isEmpty()) {
            umur.setError("Umur ke berapa?");
            valid = false;
        } else {
            umur.setError(null);
        }

        if (text_berat.isEmpty()) {
            berat.setError("Berat lahir harus di isi");
            valid = false;
        } else {
            berat.setError(null);
        }

        if (text_tinggi.isEmpty()) {
            tinggi.setError("Tinggi harus di isi");
            valid = false;
        } else {
            tinggi.setError(null);
        }

        if (text_lingkarKepala.isEmpty()) {
            lingkarKepala.setError("Lingkar Kepala harus di isi");
            valid = false;
        } else {
            lingkarKepala.setError(null);
        }

        if (text_gizi.isEmpty()) {
            gizi.setError("Gizi harus di isi");
            valid = false;
        } else {
            gizi.setError(null);
        }
        if (text_keterangan.isEmpty()) {
            keterangan.setError("Keterangan harus di isi");
            valid = false;
        } else {
            keterangan.setError(null);
        }

        return valid;
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
        tanggal.setText(date);
    }

    class AttemptSubmit extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        String id, tanggal, umur, berat, tinggi, lingkarKepala, gizi, keterangan, petugas;

        public AttemptSubmit(String id, String tanggal, String umur, String berat, String tinggi, String lingkarKepala, String gizi, String keterangan, String petugas) {

            this.id = id;
            this.tanggal = tanggal;
            this.umur = umur;
            this.berat = berat;
            this.tinggi = tinggi;
            this.lingkarKepala = lingkarKepala;
            this.gizi = gizi;
            this.keterangan = keterangan;
            this.petugas = petugas;
        }

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetailRiwayat.this);
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
            nameValuePairs.add(new BasicNameValuePair("tanggal", tanggal));
            nameValuePairs.add(new BasicNameValuePair("umur", umur));
            nameValuePairs.add(new BasicNameValuePair("berat", berat));
            nameValuePairs.add(new BasicNameValuePair("tinggi", tinggi));
            nameValuePairs.add(new BasicNameValuePair("lingkarKepala", lingkarKepala));
            nameValuePairs.add(new BasicNameValuePair("gizi", gizi));
            nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));
            nameValuePairs.add(new BasicNameValuePair("petugas", petugas));

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(
                        "http://posyanduanak.com/mawar/edit_riwayat.php?id=" + id);
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
                Toast.makeText(DetailRiwayat.this, result, Toast.LENGTH_LONG).show();
            }
        }

    }
}
