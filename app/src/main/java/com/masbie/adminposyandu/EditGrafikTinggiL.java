package com.masbie.adminposyandu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.masbie.adminposyandu.pengaturan.PengaturanGrafikTinggiL;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditGrafikTinggiL extends AppCompatActivity {
    EditText bulan1, bulan2, bulan3, bulan4, bulan5, bulan6, bulan7;
    public ProgressDialog pDialog;
    String bulan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_grafik_tinggi_l);
        bulan = getIntent().getExtras().getString("bulan");
        setTitle("Edit Grafik Tinggi L Bulan ke-" + bulan);
        bulan1 = (EditText) findViewById(R.id.b1);
        bulan2 = (EditText) findViewById(R.id.b2);
        bulan3 = (EditText) findViewById(R.id.b3);
        bulan4 = (EditText) findViewById(R.id.b4);
        bulan5 = (EditText) findViewById(R.id.b5);
        bulan6 = (EditText) findViewById(R.id.b6);
        bulan7 = (EditText) findViewById(R.id.b7);
        progressDialog = new ProgressDialog(EditGrafikTinggiL.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Load Data...");
        progressDialog.show();
        localAdminList();
        Button tambah = (Button) findViewById(R.id.tambah);
        Button hapus = (Button) findViewById(R.id.hapus);
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    new AttemptSubmit(bulan1.getText().toString(), bulan2.getText().toString(), bulan3.getText().toString(), bulan4.getText().toString(), bulan5.getText().toString(), bulan6.getText().toString(), bulan7.getText().toString()).execute();
                }
            }
        });
        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(EditGrafikTinggiL.this, SweetAlertDialog.WARNING_TYPE)
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
                                new HapusPetugas(bulan).execute();
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }
    public boolean validate() {
        boolean valid = true;

        String b1 = bulan1.getText().toString();
        String b2 = bulan2.getText().toString();
        String b3 = bulan3.getText().toString();
        String b4 = bulan4.getText().toString();
        String b5 = bulan5.getText().toString();
        String b6 = bulan6.getText().toString();
        String b7 = bulan7.getText().toString();

        if (b1.isEmpty()) {
            bulan1.setError("Bulan harus di isi");
            valid = false;
        } else {
            bulan1.setError(null);
        }

        if (b2.isEmpty()) {
            bulan2.setError("Tinggi ke-2 berapa?");
            valid = false;
        } else {
            bulan2.setError(null);
        }

        if (b3.isEmpty()) {
            bulan3.setError("Tinggi ke-3 berapa?");
            valid = false;
        } else {
            bulan3.setError(null);
        }

        if (b4.isEmpty()) {
            bulan4.setError("Tinggi ke-4 berapa?");
            valid = false;
        } else {
            bulan4.setError(null);
        }

        if (b5.isEmpty()) {
            bulan5.setError("Tinggi ke-5 berapa?");
            valid = false;
        } else {
            bulan5.setError(null);
        }

        if (b6.isEmpty()) {
            bulan6.setError("Tinggi ke-6 berapa?");
            valid = false;
        } else {
            bulan6.setError(null);
        }

        if (b7.isEmpty()) {
            bulan7.setError("Tinggi ke-7 berapa?");
            valid = false;
        } else {
            bulan7.setError(null);
        }

        return valid;
    }
    class AttemptSubmit extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         */
        String bulan1, bulan2, bulan3, bulan4, bulan5, bulan6, bulan7;

        public AttemptSubmit(String bulan1, String bulan2, String bulan3, String bulan4, String bulan5, String bulan6, String bulan7) {
            this.bulan1 = bulan1;
            this.bulan2 = bulan2;
            this.bulan3 = bulan3;
            this.bulan4 = bulan4;
            this.bulan5 = bulan5;
            this.bulan6 = bulan6;
            this.bulan7 = bulan7;
        }

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditGrafikTinggiL.this);
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
            nameValuePairs.add(new BasicNameValuePair("tinggi1", bulan1));
            nameValuePairs.add(new BasicNameValuePair("tinggi2", bulan2));
            nameValuePairs.add(new BasicNameValuePair("tinggi3", bulan3));
            nameValuePairs.add(new BasicNameValuePair("tinggi4", bulan4));
            nameValuePairs.add(new BasicNameValuePair("tinggi5", bulan5));
            nameValuePairs.add(new BasicNameValuePair("tinggi6", bulan6));
            nameValuePairs.add(new BasicNameValuePair("tinggi7", bulan7));

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(
                        "http://posyanduanak.com/mawar/edit_tinggi_l.php?bulan=" + bulan);
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
                Toast.makeText(EditGrafikTinggiL.this, result, Toast.LENGTH_LONG).show();
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
            pDialog = new ProgressDialog(EditGrafikTinggiL.this);
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
                        "http://posyanduanak.com/mawar/delete_tinggi_l.php?bulan=" + id);

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
                Toast.makeText(EditGrafikTinggiL.this, result, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(EditGrafikTinggiL.this, PengaturanGrafikTinggiL.class);
                startActivity(intent);
            }
        }

    }

    public static String url;
    public static final String JSON_ARRAY = "result";
    ProgressDialog progressDialog;

    public void localAdminList() {
        url = "http://posyanduanak.com/mawar/view.php?detailgrafik=3&bulan=" + bulan;

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                parseJSON(getWindow().getDecorView().getRootView(), response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError) {
                }
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                return headers;
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    String[][] data = null;

    protected void parseJSON(View view, String json) {
        JSONArray users = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            data = new String[users.length()][4];
            for (int i = 0; i < users.length(); i++) {
                JSONObject jo = users.getJSONObject(i);
                int urutan = Integer.parseInt(jo.getString("urutan"));
                switch (urutan) {
                    case 1:
                        bulan1.setText(jo.getString("tinggi"));
                        break;
                    case 2:
                        bulan2.setText(jo.getString("tinggi"));
                        break;
                    case 3:
                        bulan3.setText(jo.getString("tinggi"));
                        break;
                    case 4:
                        bulan4.setText(jo.getString("tinggi"));
                        break;
                    case 5:
                        bulan5.setText(jo.getString("tinggi"));
                        break;
                    case 6:
                        bulan6.setText(jo.getString("tinggi"));
                        break;
                    case 7:
                        bulan7.setText(jo.getString("tinggi"));
                        break;
                    default:
                        System.out.println("tidak ada");
                        break;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }
}
