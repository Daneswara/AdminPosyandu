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

public class EditAkun extends AppCompatActivity {

    public EditText input_username, input_password, input_nama;
    public ProgressDialog pDialog;
    public String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_akun);
        SharedPreferences pref = this.getApplicationContext().getSharedPreferences("loginadmin", 0);
        if (pref.getBoolean("akses", true)) {
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
        } else {
            setTitle("Edit Akun");
            id = getIntent().getExtras().getString("id");
            String nama = getIntent().getExtras().getString("nama");
            input_username = (EditText) findViewById(R.id.username);
            input_password = (EditText) findViewById(R.id.password);
            input_nama = (EditText) findViewById(R.id.nama);

            input_username.setText(id);
            input_nama.setText(nama);

            Button tambah = (Button) findViewById(R.id.tambah);
            tambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(validate()) {
                        new AttemptSubmit(input_nama.getText().toString(), input_username.getText().toString(), input_password.getText().toString()).execute();
                    }
                }
            });
        }
    }

    public boolean validate() {
        boolean valid = true;

        String user = input_username.getText().toString();
        String pass = input_password.getText().toString();
        String nama = input_nama.getText().toString();

        if (user.isEmpty()) {
            input_username.setError("Username harus di isi");
            valid = false;
        } else {
            input_username.setError(null);
        }
        if (pass.isEmpty()) {
            input_password.setError("Password harus di isi");
            valid = false;
        } else {
            input_password.setError(null);
        }
        if (nama.isEmpty()) {
            input_nama.setError("Nama harus di isi");
            valid = false;
        } else {
            input_nama.setError(null);
        }

        return valid;
    }

    class AttemptSubmit extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        String username, password, nama;

        public AttemptSubmit(String nama, String username, String password) {
            this.username = username;
            this.password = password;
            this.nama = nama;
        }

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditAkun.this);
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
                        "http://posyanduanak.com/mawar/edit_akun_admin.php?id=" + id);
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
            if (result.equals("data akun berhasil dirubah")) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("loginadmin", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("akses", false);
                editor.putString("nama", input_nama.getText().toString());
                editor.putString("telp", input_username.getText().toString());
                editor.commit();
            }
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            pDialog.dismiss();
            if (result != null) {
                Toast.makeText(EditAkun.this, result, Toast.LENGTH_LONG).show();
            }
        }

    }
}
