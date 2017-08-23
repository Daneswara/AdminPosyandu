package com.masbie.adminposyandu.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.masbie.adminposyandu.DetailRiwayat;
import com.masbie.adminposyandu.R;

public class AdapterDetailAnak extends BaseAdapter {
    private Context mContext;
    private String[][] data;
    // Constructor
    public AdapterDetailAnak(Context c, String[][] data) {
        mContext = c;
        this.data = data;
        pelajaran = new String[data.length];
        mThumbIds = new String[data.length];
        for (int i = 0; i < data.length; i++){
            pelajaran[i] = data[i][0];
            mThumbIds[i] = data[i][6];
        }
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView imageView;
        TextView teks;
        LinearLayout lin;
        if (convertView == null) {
            imageView = new TextView(mContext);
            imageView.setText("A");
            imageView.setTextColor(Color.WHITE);
            imageView.setTextSize(20);
            imageView.setBackgroundResource(R.drawable.bg_red);
            imageView.setGravity(Gravity.CENTER);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            teks = new TextView(mContext);
            teks.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            teks.setGravity(Gravity.CENTER_VERTICAL);
            teks.setPadding(20, 0, 0, 0);

            lin = new LinearLayout(mContext);
            lin.setPadding(20, 8, 8, 8);
            lin.addView(imageView);
            lin.addView(teks);
            lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DetailRiwayat.class);
                    intent.putExtra("tanggal", data[position][0]);
                    intent.putExtra("nama", data[position][1]);
                    intent.putExtra("tanggalahir", data[position][2]);
                    intent.putExtra("alamat", data[position][3]);
                    intent.putExtra("namaortu", data[position][4]);
                    intent.putExtra("umur", data[position][5]);
                    intent.putExtra("berat", data[position][6]);
                    intent.putExtra("tinggi", data[position][7]);
                    intent.putExtra("lkepala", data[position][8]);
                    intent.putExtra("gizi", data[position][9]);
                    intent.putExtra("keterangan", data[position][10]);
                    intent.putExtra("id_riwayat", data[position][11]);
                    intent.putExtra("jeniskelamin", data[position][12]);
                    intent.putExtra("beratlahir", data[position][13]);
                    mContext.startActivity(intent);
                }
            });
        } else {
            lin = (LinearLayout) convertView;
            imageView = (TextView) lin.getChildAt(0);
            teks = (TextView) lin.getChildAt(1);
        }
        imageView.setText(mThumbIds[position]);
        teks.setText(pelajaran[position]);
        lin.setOrientation(LinearLayout.HORIZONTAL);
        return lin;
    }

    // Keep all Images in array
    public String[] mThumbIds;

    public String[] pelajaran;
}