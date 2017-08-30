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

public class KirimKeWA extends AppCompatActivity {
    EditText textSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kirim_ke_w);
        setTitle("Kirim Notifikasi ke WA");
        Button send = (Button) findViewById(R.id.buttonSend);
        textSMS = (EditText) findViewById(R.id.editTextSMS);
        SharedPreferences pref = this.getApplicationContext().getSharedPreferences("loginadmin", 0);
        final String petugas = pref.getString("nama", "Tidak ada");
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setAction(Intent.ACTION_SEND);
//                    sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
                    sendIntent.putExtra(Intent.EXTRA_TEXT, textSMS.getText().toString());
//                    sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net");
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
//                    new AttemptSubmit("WA", textSMS.getText().toString(), petugas).execute();
                }
            }
        });
    }
    public boolean validate() {
        boolean valid = true;

        String sms = textSMS.getText().toString();

        if (sms.isEmpty()) {
            textSMS.setError("Notifikasi harus di isi");
            valid = false;
        } else {
            textSMS.setError(null);
        }

        return valid;
    }
    class AttemptSubmit extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        String telp, sms, petugas;

        public AttemptSubmit(String telp, String sms, String petugas) {
            this.telp = telp;
            this.sms = sms;
            this.petugas = petugas;
        }

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("telp", telp));
            nameValuePairs.add(new BasicNameValuePair("sms", sms));
            nameValuePairs.add(new BasicNameValuePair("petugas", petugas));
            nameValuePairs.add(new BasicNameValuePair("tipe", "WA"));

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(
                        "http://posyanduanak.com/kenanga/insert_notifikasi.php");
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

//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            if (result != null) {
                Toast.makeText(KirimKeWA.this, result, Toast.LENGTH_LONG).show();
            }
        }

    }
}
