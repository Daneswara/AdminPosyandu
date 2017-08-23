package com.masbie.adminposyandu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.masbie.adminposyandu.adapter.AdapterDetailAnak;
import com.masbie.adminposyandu.tools.ExpandableHeightGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RiwayatAnak extends AppCompatActivity {

    public String url;
    public static final String JSON_ARRAY = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_anak);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String id = getIntent().getExtras().getString("id");
        url = "http://posyanduanak.com/mawar/view.php?riwayatanak=" + id;
        String nama = getIntent().getExtras().getString("nama");
        setTitle("Profil " + nama);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), TambahRiwayat.class);
                in.putExtra("id", getIntent().getExtras().getString("id"));
                startActivity(in);
            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DetailProfilAnak.class);
                intent.putExtra("id", getIntent().getExtras().getString("id"));
                intent.putExtra("nama", getIntent().getExtras().getString("nama"));
                intent.putExtra("tanggalahir", getIntent().getExtras().getString("tanggalahir"));
                intent.putExtra("beratlahir", getIntent().getExtras().getString("beratlahir"));
                intent.putExtra("ayah", getIntent().getExtras().getString("ayah"));
                intent.putExtra("ibu", getIntent().getExtras().getString("ibu"));
                intent.putExtra("alamat", getIntent().getExtras().getString("alamat"));
                intent.putExtra("telp", getIntent().getExtras().getString("telp"));
                intent.putExtra("jeniskelamin", getIntent().getExtras().getString("jeniskelamin"));
                intent.putExtra("anakke", getIntent().getExtras().getString("anakke"));
                startActivity(intent);
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Load Data...");
        progressDialog.show();

        localAdminList();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    ProgressDialog progressDialog;

    public void localAdminList() {


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
                return Request.Priority.IMMEDIATE;
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
            data = new String[users.length()][14];
            for (int i = 0; i < users.length(); i++) {
                JSONObject jo = users.getJSONObject(i);
                String tanggal = formatTanggal(jo.getString("tanggal"));
                data[i][0] = tanggal;
                data[i][1] = jo.getString("nama");
                data[i][2] = jo.getString("tanggalahir");
                data[i][3] = jo.getString("alamat");
                data[i][4] = jo.getString("namaortu");
                data[i][5] = jo.getString("umur");
                data[i][6] = jo.getString("berat");
                data[i][7] = jo.getString("tinggi");
                data[i][8] = jo.getString("lkepala");
                data[i][9] = jo.getString("gizi");
                data[i][10] = jo.getString("keterangan");
                data[i][11] = jo.getString("id");
                data[i][12] = getIntent().getExtras().getString("jeniskelamin");
                data[i][13] = getIntent().getExtras().getString("beratlahir");
            }
            ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gridview);
            gridView.setAdapter(new AdapterDetailAnak(this, data));
            gridView.setExpanded(true);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    public String formatTanggal(String datetime) throws ParseException {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date tanggal = sdfDate.parse(datetime);

        SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        String hasil = fo.format(tanggal);
        return hasil;
    }


}
