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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditPetugas extends AppCompatActivity {
    EditText nama, username, password;
    public ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_petugas);
        setTitle("Edit Petugas");
        nama = (EditText) findViewById(R.id.nama);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        String getnama = getIntent().getExtras().getString("nama");
        String getusername = getIntent().getExtras().getString("username");
        final String getid = getIntent().getExtras().getString("id");
        nama.setText(getnama);
        username.setText(getusername);

        Button tambah = (Button) findViewById(R.id.tambah);
        Button hapus = (Button) findViewById(R.id.hapus);
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    new AttemptSubmit(nama.getText().toString(), username.getText().toString(), password.getText().toString(), getid).execute();
                }
            }
        });
        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(EditPetugas.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Apakah anda yakin?")
                        .setContentText("Anda tidak dapat mengembalikan data yang telah dihapus")
                        .setCancelText("Batal")
                        .setConfirmText("Iya")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                new HapusPetugas(getid).execute();
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }
    public boolean validate() {
        boolean valid = true;

        String input_username = username.getText().toString();
        String input_password = password.getText().toString();
        String input_nama = nama.getText().toString();

        if (input_username.isEmpty() || input_username.length() < 11 || input_username.length() > 12) {
            username.setError("Telp harus di isi, 11-12 digit");
            valid = false;
        } else {
            username.setError(null);
        }
        if (input_password.isEmpty()) {
            password.setError("Password harus di isi");
            valid = false;
        } else {
            password.setError(null);
        }
        if (input_nama.isEmpty()) {
            nama.setError("Nama harus di isi");
            valid = false;
        } else {
            nama.setError(null);
        }

        return valid;
    }

    class AttemptSubmit extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        String username, password, nama, id;

        public AttemptSubmit(String nama, String username, String password, String id) {
            this.username = username;
            this.password = password;
            this.nama = nama;
            this.id = id;
        }

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditPetugas.this);
            pDialog.setMessage("Proses Edit...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("nama", nama));
            nameValuePairs.add(new BasicNameValuePair("username", username));
            nameValuePairs.add(new BasicNameValuePair("password", password));

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(
                        "http://posyanduanak.com/mawar/edit_petugas.php?id=" + id);
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
                Toast.makeText(EditPetugas.this, result, Toast.LENGTH_LONG).show();
            }
        }

    }

    class HapusPetugas extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        String id;

        public HapusPetugas(String id) {
            this.id = id;
        }

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditPetugas.this);
            pDialog.setMessage("Proses Hapus...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(
                        "http://posyanduanak.com/mawar/delete_petugas.php?id=" + id);

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
                Toast.makeText(EditPetugas.this, result, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(EditPetugas.this, MainActivity.class);
                startActivity(intent);
            }
        }

    }
}
