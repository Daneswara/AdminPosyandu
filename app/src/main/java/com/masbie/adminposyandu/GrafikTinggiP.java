package com.masbie.adminposyandu;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class GrafikTinggiP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_tinggi_p);
        setTitle("Grafik Tinggi (Perempuan)");
        String u = getIntent().getExtras().getString("umur");
        System.out.println(getIntent().getExtras().getString("tinggi"));
        System.out.println(u);
        float umur = getUmur(u);
        float tinggi = Float.parseFloat(getIntent().getExtras().getString("tinggi"));
        LineChart chart = (LineChart) findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<Entry>();
        entries.add(new Entry(0, 45));
        entries.add(new Entry(3, 56));
        entries.add(new Entry(6, 62.5f));
        entries.add(new Entry(9, 67));
        entries.add(new Entry(12, 70));
        entries.add(new Entry(15, 73.5f));
        entries.add(new Entry(18, 76));
        entries.add(new Entry(21, 78.5f));
        entries.add(new Entry(24, 81));
        entries.add(new Entry(27, 83));
        entries.add(new Entry(30, 85));
        entries.add(new Entry(33, 87));
        entries.add(new Entry(36, 89));

        List<Entry> entries2 = new ArrayList<Entry>();
        entries2.add(new Entry(0, 47));
        entries2.add(new Entry(3, 58));
        entries2.add(new Entry(6, 64));
        entries2.add(new Entry(9, 68));
        entries2.add(new Entry(12, 72));
        entries2.add(new Entry(15, 75));
        entries2.add(new Entry(18, 78));
        entries2.add(new Entry(21, 80.5f));
        entries2.add(new Entry(24, 83));
        entries2.add(new Entry(27, 85));
        entries2.add(new Entry(30, 87));
        entries2.add(new Entry(33, 89));
        entries2.add(new Entry(36, 91));

        List<Entry> entries3 = new ArrayList<Entry>();
        entries3.add(new Entry(0, 49));
        entries3.add(new Entry(3, 59));
        entries3.add(new Entry(6, 65.5f));
        entries3.add(new Entry(9, 70));
        entries3.add(new Entry(12, 73.5f));
        entries3.add(new Entry(15, 77));
        entries3.add(new Entry(18, 80));
        entries3.add(new Entry(21, 82.5f));
        entries3.add(new Entry(24, 85));
        entries3.add(new Entry(27, 87));
        entries3.add(new Entry(30, 89.5f));
        entries3.add(new Entry(33, 91.5f));
        entries3.add(new Entry(36, 93));

        List<Entry> entries4 = new ArrayList<Entry>();
        entries4.add(new Entry(0, 50.5f));
        entries4.add(new Entry(3, 61));
        entries4.add(new Entry(6, 67));
        entries4.add(new Entry(9, 71.5f));
        entries4.add(new Entry(12, 75.5f));
        entries4.add(new Entry(15, 79));
        entries4.add(new Entry(18, 82));
        entries4.add(new Entry(21, 85));
        entries4.add(new Entry(24, 87));
        entries4.add(new Entry(27, 90));
        entries4.add(new Entry(30, 92));
        entries4.add(new Entry(33, 94));
        entries4.add(new Entry(36, 96));

        List<Entry> entries5 = new ArrayList<Entry>();
        entries5.add(new Entry(0, 52));
        entries5.add(new Entry(3, 62.5f));
        entries5.add(new Entry(6, 68.5f));
        entries5.add(new Entry(9, 73.5f));
        entries5.add(new Entry(12, 77.5f));
        entries5.add(new Entry(15, 81));
        entries5.add(new Entry(18, 84));
        entries5.add(new Entry(21, 87));
        entries5.add(new Entry(24, 90));
        entries5.add(new Entry(27, 92));
        entries5.add(new Entry(30, 94.5f));
        entries5.add(new Entry(33, 96.5f));
        entries5.add(new Entry(36, 98.5f));

        List<Entry> entries6 = new ArrayList<Entry>();
        entries6.add(new Entry(0, 54));
        entries6.add(new Entry(3, 64));
        entries6.add(new Entry(6, 70.5f));
        entries6.add(new Entry(9, 75.5f));
        entries6.add(new Entry(12, 79.5f));
        entries6.add(new Entry(15, 83));
        entries6.add(new Entry(18, 86));
        entries6.add(new Entry(21, 89));
        entries6.add(new Entry(24, 92));
        entries6.add(new Entry(27, 94.5f));
        entries6.add(new Entry(30, 97));
        entries6.add(new Entry(33, 99));
        entries6.add(new Entry(36, 101));

        List<Entry> entries7 = new ArrayList<Entry>();
        entries7.add(new Entry(0, 55.5f));
        entries7.add(new Entry(3, 66));
        entries7.add(new Entry(6, 72));
        entries7.add(new Entry(9, 77));
        entries7.add(new Entry(12, 81.5f));
        entries7.add(new Entry(15, 85));
        entries7.add(new Entry(18, 88));
        entries7.add(new Entry(21, 91));
        entries7.add(new Entry(24, 94));
        entries7.add(new Entry(27, 96.5f));
        entries7.add(new Entry(30, 99));
        entries7.add(new Entry(33, 101.5f));
        entries7.add(new Entry(36, 103));

        List<Entry> entriesInput = new ArrayList<Entry>();
        entriesInput.add(new Entry(umur, tinggi));


        LineDataSet dataSet = new LineDataSet(entries, null);
        dataSet.setColor(Color.RED);
        dataSet.setDrawCircles(false);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawValues(false);
        LineDataSet dataSet2 = new LineDataSet(entries2, null);
        dataSet2.setColor(Color.YELLOW);
        dataSet2.setDrawCircles(false);
        dataSet2.setDrawCircleHole(false);
        dataSet2.setDrawValues(false);
        LineDataSet dataSet3 = new LineDataSet(entries3, null);
        dataSet3.setColor(Color.GREEN);
        dataSet3.setDrawCircles(false);
        dataSet3.setDrawCircleHole(false);
        dataSet3.setDrawValues(false);
        LineDataSet dataSet4 = new LineDataSet(entries4, null);
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
        LineDataSet dataSet8 = new LineDataSet(entriesInput, "Tinggi Sekarang");
        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet);
        dataSets.add(dataSet2);
        dataSets.add(dataSet3);
        dataSets.add(dataSet4);
        dataSets.add(dataSet5);
        dataSets.add(dataSet6);
        dataSets.add(dataSet7);
        dataSets.add(dataSet8);
        LineData lineData = new LineData(dataSets);

        chart.setData(lineData);
        chart.getDescription().setText("Grafik Gizi Berdasarkan Tinggi Badan Untuk Perempuan");
        LegendEntry a = new LegendEntry();

        chart.invalidate(); // refresh
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
}
