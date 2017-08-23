package com.masbie.adminposyandu;

import android.app.ProgressDialog;
import android.graphics.Color;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrafikP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_p);
        setTitle("Grafik Berat (Perempuan)");
        progressDialog = new ProgressDialog(GrafikP.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Load Data...");
        progressDialog.show();
        localAdminList();

    }


    public int getUmur(String umur) {
        int hasil = 0;
        String[] cek = umur.split(" ");
        if (cek.length == 2) {
            if (cek[1].equals("tahun")) {
                hasil = Integer.parseInt(cek[0]) * 12;
            } else if (cek[1].equals("bulan")) {
                hasil = Integer.parseInt(cek[0]);
            }
        } else if (cek.length == 4){
            hasil = Integer.parseInt(cek[0]) * 12;
            hasil += Integer.parseInt(cek[2]);
        }
        return hasil;
    }

    public static String url = "http://posyanduanak.com/mawar/view.php?listgrafik=2";
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

    float[][] data = null;

    protected void parseJSON(View view, String json) {
        JSONArray users = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            data = new float[7][users.length()/7];
            for (int i = 0; i < users.length(); i++) {
                JSONObject jo = users.getJSONObject(i);
                float berat = Float.parseFloat(jo.getString("berat"));
                int bulan = Integer.parseInt(jo.getString("bulan"));
                int urutan = Integer.parseInt(jo.getString("urutan"));
                data[urutan-1][bulan] = berat;
            }
            buatGrafik(view, data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }

    public void buatGrafik(View view, float[][] data){
        String u = getIntent().getExtras().getString("umur");
        float umur = getUmur(u);
        float berat = Float.parseFloat(getIntent().getExtras().getString("berat"));
        float beratLahir = Float.parseFloat(getIntent().getExtras().getString("beratLahir"));
        LineChart chart = (LineChart) view.findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i < data[0].length; i++) {
            entries.add(new Entry(i, data[6][i]));
        }
        List<Entry> entries2 = new ArrayList<Entry>();
        for (int i = 0; i < data[0].length; i++) {
            entries2.add(new Entry(i, data[5][i]));
        }
        List<Entry> entries3 = new ArrayList<Entry>();
        for (int i = 0; i < data[0].length; i++) {
            entries3.add(new Entry(i, data[4][i]));
        }
        List<Entry> entries4 = new ArrayList<Entry>();
        for (int i = 0; i < data[0].length; i++) {
            entries4.add(new Entry(i, data[3][i]));
        }
        List<Entry> entries5 = new ArrayList<Entry>();
        for (int i = 0; i < data[0].length; i++) {
            entries5.add(new Entry(i, data[2][i]));
        }
        List<Entry> entries6 = new ArrayList<Entry>();
        for (int i = 0; i < data[0].length; i++) {
            entries6.add(new Entry(i, data[1][i]));
        }
        List<Entry> entries7 = new ArrayList<Entry>();
        for (int i = 0; i < data[0].length; i++) {
            entries7.add(new Entry(i, data[0][i]));
        }

        List<Entry> entriesInput = new ArrayList<Entry>();
        entriesInput.add(new Entry(umur, berat));

        List<Entry> bobotlahir = new ArrayList<Entry>();
        bobotlahir.add(new Entry(0, beratLahir));

        LineDataSet dataSet = new LineDataSet(entries, "Buruk");
        dataSet.setColor(Color.RED);
        dataSet.setDrawCircles(false);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawValues(false);
        LineDataSet dataSet2 = new LineDataSet(entries2, "Cukup");
        dataSet2.setColor(Color.YELLOW);
        dataSet2.setDrawCircles(false);
        dataSet2.setDrawCircleHole(false);
        dataSet2.setDrawValues(false);
        LineDataSet dataSet3 = new LineDataSet(entries3, "Baik");
        dataSet3.setColor(Color.GREEN);
        dataSet3.setDrawCircles(false);
        dataSet3.setDrawCircleHole(false);
        dataSet3.setDrawValues(false);
        LineDataSet dataSet4 = new LineDataSet(entries4, "Baik");
        dataSet4.setColor(Color.GREEN);
        dataSet4.setDrawCircles(false);
        dataSet4.setDrawCircleHole(false);
        dataSet4.setDrawValues(false);
        LineDataSet dataSet5 = new LineDataSet(entries5, "Baik");
        dataSet5.setColor(Color.GREEN);
        dataSet5.setDrawCircles(false);
        dataSet5.setDrawCircleHole(false);
        dataSet5.setDrawValues(false);
        LineDataSet dataSet6 = new LineDataSet(entries6, "Cukup");
        dataSet6.setColor(Color.YELLOW);
        dataSet6.setDrawCircles(false);
        dataSet6.setDrawCircleHole(false);
        dataSet6.setDrawValues(false);
        LineDataSet dataSet7 = new LineDataSet(entries7, "Buruk");
        dataSet7.setColor(Color.RED);
        dataSet7.setDrawCircles(false);
        dataSet7.setDrawCircleHole(false);
        dataSet7.setDrawValues(false);
        LineDataSet dataSet8 = new LineDataSet(entriesInput, "Berat Sekarang");
        dataSet8.setColor(Color.BLUE);
        dataSet8.setCircleColor(Color.BLUE);
        LineDataSet dataSet9 = new LineDataSet(bobotlahir, "Berat Lahir");
        dataSet9.setColor(Color.CYAN);
        dataSet9.setCircleColor(Color.CYAN);
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet);
        dataSets.add(dataSet2);
        dataSets.add(dataSet3);
        dataSets.add(dataSet4);
        dataSets.add(dataSet5);
        dataSets.add(dataSet6);
        dataSets.add(dataSet7);
        dataSets.add(dataSet8);
        dataSets.add(dataSet9);
        LineData lineData = new LineData(dataSets);

        chart.setData(lineData);
        chart.getDescription().setText("Grafik Gizi Berdasarkan Berat Badan Untuk Perempuan");

        chart.invalidate(); // refresh
    }
}
