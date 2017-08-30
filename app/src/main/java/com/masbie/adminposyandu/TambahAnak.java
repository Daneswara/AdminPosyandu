package com.masbie.adminposyandu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Calendar;
import java.util.List;

public class TambahAnak extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    public EditText input_telp, input_nama, input_ttl, input_berat, input_ayah, input_ibu, input_alamat, input_anakke;
    public RadioGroup input_radio;
    public RadioButton input_jeniskelamin;
    public ProgressDialog pDialog;
    public boolean cek = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_anak);
        SharedPreferences pref = this.getApplicationContext().getSharedPreferences("loginadmin", 0);
        final String petugas = pref.getString("nama", "Tidak ada");
        if (pref.getBoolean("akses", true)) {
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
        } else {
            setTitle("Tambah Anak");

            input_telp = (EditText) findViewById(R.id.telp);
            input_nama = (EditText) findViewById(R.id.nama);
            input_ttl = (EditText) findViewById(R.id.tanggalahir);
            input_berat = (EditText) findViewById(R.id.berat);
            input_ayah = (EditText) findViewById(R.id.ayah);
            input_ibu = (EditText) findViewById(R.id.ibu);
            input_alamat = (EditText) findViewById(R.id.alamat);
            input_anakke = (EditText) findViewById(R.id.anakke);

            input_radio = (RadioGroup) findViewById(R.id.jeniskelamin);
            input_radio.check(R.id.laki);

            Button tambah = (Button) findViewById(R.id.tambah);
            tambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(validate()) {
                        int selectedId = input_radio.getCheckedRadioButtonId();
                        input_jeniskelamin = (RadioButton) findViewById(selectedId);
                        new AttemptSubmit(input_telp.getText().toString(), input_nama.getText().toString(),
                                input_ttl.getText().toString(), input_berat.getText().toString(), input_ayah.getText().toString(), input_ibu.getText().toString(),
                                input_alamat.getText().toString(), petugas, input_jeniskelamin.getText().toString(), input_anakke.getText().toString()).execute();
                    }
                }
            });

            input_ttl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (cek) {
                        Calendar now = Calendar.getInstance();
                        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                                TambahAnak.this,
                                now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH)
                        );
                        dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
                        dpd.setMaxDate(now);
                        dpd.setOkText("Pilih");
                        dpd.setCancelText("Batal");
                        dpd.show(getFragmentManager(), "Datepickerdialog");
                        cek = false;
                    } else {
                        cek = true;
                    }
                }
            });
            input_ttl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar now = Calendar.getInstance();
                    com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                            TambahAnak.this,
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
        }
    }
    public boolean validate() {
        boolean valid = true;

        String alamat = input_alamat.getText().toString();
        String anakke = input_anakke.getText().toString();
        String ayah = input_ayah.getText().toString();
        String berat = input_berat.getText().toString();
        String ibu = input_ibu.getText().toString();
        String ttl = input_ttl.getText().toString();
        String nama = input_nama.getText().toString();
        String telp = input_telp.getText().toString();

        if (alamat.isEmpty()) {
            input_alamat.setError("Alamat harus di isi");
            valid = false;
        } else {
            input_alamat.setError(null);
        }

        if (anakke.isEmpty()) {
            input_anakke.setError("Anak ke berapa?");
            valid = false;
        } else {
            input_anakke.setError(null);
        }

        if (ayah.isEmpty()) {
            input_ayah.setError("Nama Ayah harus di isi");
            valid = false;
        } else {
            input_ayah.setError(null);
        }

        if (berat.isEmpty()) {
            input_berat.setError("Berat lahir harus di isi");
            valid = false;
        } else {
            input_berat.setError(null);
        }

        if (ibu.isEmpty()) {
            input_ibu.setError("Nama Ibu harus di isi");
            valid = false;
        } else {
            input_ibu.setError(null);
        }

        if (ttl.isEmpty()) {
            input_ttl.setError("Tanggal lahir harus di isi");
            valid = false;
        } else {
            input_ttl.setError(null);
        }
        if (nama.isEmpty()) {
            input_nama.setError("Nama anak harus di isi");
            valid = false;
        } else {
            input_nama.setError(null);
        }
        if (telp.isEmpty() || telp.length() < 11 || telp.length() > 12) {
            input_telp.setError("Isikan nomer telp 11-12 digit");
            valid = false;
        } else {
            input_telp.setError(null);
        }


        return valid;
    }
    class AttemptSubmit extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        String telp, nama, ttl, berat, ayah, ibu, alamat, petugas, jeniskelamin, anakke;

        public AttemptSubmit(String telp, String nama, String ttl, String berat, String ayah, String ibu, String alamat, String petugas, String jk, String anakke) {
            this.telp = telp;
            this.nama = nama;
            this.ttl = ttl;
            this.berat = berat;
            this.ayah = ayah;
            this.ibu = ibu;
            this.alamat = alamat;
            this.petugas = petugas;
            this.jeniskelamin = jk;
            this.anakke = anakke;
        }

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TambahAnak.this);
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
            nameValuePairs.add(new BasicNameValuePair("telp", telp));
            nameValuePairs.add(new BasicNameValuePair("password", "123456"));
            nameValuePairs.add(new BasicNameValuePair("nama", nama));
            nameValuePairs.add(new BasicNameValuePair("ttl", ttl));
            nameValuePairs.add(new BasicNameValuePair("berat", berat));
            nameValuePairs.add(new BasicNameValuePair("ayah", ayah));
            nameValuePairs.add(new BasicNameValuePair("ibu", ibu));
            nameValuePairs.add(new BasicNameValuePair("alamat", alamat));
            nameValuePairs.add(new BasicNameValuePair("petugas", petugas));
            nameValuePairs.add(new BasicNameValuePair("jeniskelamin", jeniskelamin));
            nameValuePairs.add(new BasicNameValuePair("anakke", anakke));

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(
                        "http://posyanduanak.com/kenanga/insert_anak.php");
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
                Toast.makeText(TambahAnak.this, result, Toast.LENGTH_LONG).show();
            }
            input_telp.setText("");
            input_nama.setText("");
            input_ttl.setText("");
            input_berat.setText("");
            input_ayah.setText("");
            input_ibu.setText("");
            input_alamat.setText("");
            input_anakke.setText("");
        }

    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
        input_ttl.setText(date);
    }
}
