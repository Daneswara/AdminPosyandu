package com.masbie.adminposyandu.pengaturan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.masbie.adminposyandu.LoginActivity;
import com.masbie.adminposyandu.R;
import com.masbie.adminposyandu.TambahBeratP;
import com.masbie.adminposyandu.adapter.AdapterBeratL;
import com.masbie.adminposyandu.adapter.AdapterBeratP;
import com.masbie.adminposyandu.adapter.AdapterGrafik;
import com.masbie.adminposyandu.tools.ExpandableHeightGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PengaturanGrafikBeratP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_grafik_berat_p);
        setTitle("Pengaturan Grafik Berat (Perempuan)");
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("loginadmin", 0);
        if(pref.getBoolean("akses", true)){
            Intent intent = new Intent(PengaturanGrafikBeratP.this, LoginActivity.class);
            this.startActivity(intent);
            finish();
        } else {
            if (pref.getBoolean("pertama", true)) {
//            Intent intent = new Intent(this, SplashActivity.class);
//            this.startActivity(intent);
            }
            progressDialog = new ProgressDialog(PengaturanGrafikBeratP.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Load Data...");
            progressDialog.show();
            localAdminList();
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PengaturanGrafikBeratP.this, TambahBeratP.class);
                    startActivity(intent);
                }
            });
        }

    }
    public static final String url = "http://posyanduanak.com/kenanga/view.php?grafik=2";
    public static final String JSON_ARRAY = "result";
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
            data = new String[users.length()][4];
            for (int i = 0; i < users.length(); i++) {
                JSONObject jo = users.getJSONObject(i);
                data[i][0] = jo.getString("id");
                data[i][1] = jo.getString("bulan");
            }
            ExpandableHeightGridView gridView = (ExpandableHeightGridView) view.findViewById(R.id.gridview);
            gridView.setAdapter(new AdapterBeratP(this, data));
            gridView.setExpanded(true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }
}
