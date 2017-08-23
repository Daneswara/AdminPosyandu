package com.masbie.adminposyandu;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class TambahTinggiP extends AppCompatActivity {
    public ProgressDialog pDialog;
    TextView input_bulan, input_b1, input_b2, input_b3, input_b4, input_b5, input_b6, input_b7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_tinggi_p);
        setTitle("Tambah Batas Tinggi Perempuan");
        input_bulan = (TextView) findViewById(R.id.bulan);
        input_b1 = (TextView) findViewById(R.id.b1);
        input_b2 = (TextView) findViewById(R.id.b2);
        input_b3 = (TextView) findViewById(R.id.b3);
        input_b4 = (TextView) findViewById(R.id.b4);
        input_b5 = (TextView) findViewById(R.id.b5);
        input_b6 = (TextView) findViewById(R.id.b6);
        input_b7 = (TextView) findViewById(R.id.b7);
        Button tambah = (Button) findViewById(R.id.tambah);
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()){
                    new AttemptSubmit(input_bulan.getText().toString(), input_b1.getText().toString(), input_b2.getText().toString(),
                            input_b3.getText().toString(), input_b4.getText().toString(), input_b5.getText().toString(),
                            input_b6.getText().toString(), input_b7.getText().toString()).execute();
                }
            }
        });
    }
    public boolean validate() {
        boolean valid = true;

        String bulan = input_bulan.getText().toString();
        String b1 = input_b1.getText().toString();
        String b2 = input_b2.getText().toString();
        String b3 = input_b3.getText().toString();
        String b4 = input_b4.getText().toString();
        String b5 = input_b5.getText().toString();
        String b6 = input_b6.getText().toString();
        String b7 = input_b7.getText().toString();

        if (bulan.isEmpty()) {
            input_bulan.setError("Bulan harus di isi");
            valid = false;
        } else {
            input_bulan.setError(null);
        }

        if (b1.isEmpty()) {
            input_b1.setError("Tinggi ke-1 berapa?");
            valid = false;
        } else {
            input_b1.setError(null);
        }

        if (b2.isEmpty()) {
            input_b2.setError("Tinggi ke-2 berapa?");
            valid = false;
        } else {
            input_b2.setError(null);
        }

        if (b3.isEmpty()) {
            input_b3.setError("Tinggi ke-3 berapa?");
            valid = false;
        } else {
            input_b3.setError(null);
        }

        if (b4.isEmpty()) {
            input_b4.setError("Tinggi ke-4 berapa?");
            valid = false;
        } else {
            input_b4.setError(null);
        }

        if (b5.isEmpty()) {
            input_b5.setError("Tinggi ke-5 berapa?");
            valid = false;
        } else {
            input_b5.setError(null);
        }

        if (b6.isEmpty()) {
            input_b6.setError("Tinggi ke-6 berapa?");
            valid = false;
        } else {
            input_b6.setError(null);
        }

        if (b7.isEmpty()) {
            input_b7.setError("Tinggi ke-7 berapa?");
            valid = false;
        } else {
            input_b7.setError(null);
        }

        return valid;
    }
    class AttemptSubmit extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        String bulan, b1, b2, b3, b4, b5, b6, b7;

        public AttemptSubmit(String bulan, String b1, String b2, String b3, String b4, String b5, String b6, String b7) {
            this.bulan = bulan;
            this.b1 = b1.replace(",",".");
            this.b2 = b2.replace(",",".");
            this.b3 = b3.replace(",",".");
            this.b4 = b4.replace(",",".");
            this.b5 = b5.replace(",",".");
            this.b6 = b6.replace(",",".");
            this.b7 = b7.replace(",",".");
        }

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TambahTinggiP.this);
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
            nameValuePairs.add(new BasicNameValuePair("bulan", bulan));
            nameValuePairs.add(new BasicNameValuePair("tinggi1", b1));
            nameValuePairs.add(new BasicNameValuePair("tinggi2", b2));
            nameValuePairs.add(new BasicNameValuePair("tinggi3", b3));
            nameValuePairs.add(new BasicNameValuePair("tinggi4", b4));
            nameValuePairs.add(new BasicNameValuePair("tinggi5", b5));
            nameValuePairs.add(new BasicNameValuePair("tinggi6", b6));
            nameValuePairs.add(new BasicNameValuePair("tinggi7", b7));
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(
                        "http://posyanduanak.com/mawar/insert_tinggi_p.php");
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
            pDialog.dismiss();
            if (result != null) {
                Toast.makeText(TambahTinggiP.this, result, Toast.LENGTH_LONG).show();
            }
            input_bulan.setText("");
            input_b1.setText("");
            input_b2.setText("");
            input_b3.setText("");
            input_b4.setText("");
            input_b5.setText("");
            input_b6.setText("");
            input_b7.setText("");
        }

    }
}
