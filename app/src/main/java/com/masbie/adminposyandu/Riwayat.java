package com.masbie.adminposyandu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Riwayat.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Riwayat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Riwayat extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String url = "http://posyanduanak.com/kenanga/view.php?riwayat=1";
    public static final String JSON_ARRAY = "result";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String[][] simpandata;

    public Riwayat() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Anak.
     */
    // TODO: Rename and change types and number of parameters
    public static Riwayat newInstance(String param1, String param2) {
        Riwayat fragment = new Riwayat();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

//        if(data != null){
//            tabelbuatan(data, tabel1);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(data == null){
//            SharedPreferences prefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
//            int panjang = prefs.getInt("panjang", 0);
//            int lebar = prefs.getInt("lebar", 0);
//            data = new String[panjang][lebar];
//            for(int i=0;i<panjang; i++) {
//                for (int j = 0; j < lebar; j++) {
//                    data[i][j] = prefs.getString("array["+i+"]["+j+"]", null);
//                }
//            }
//            System.out.println("data tidak ada, proses mengisi"+data[0][1]);
//        } else {
//            System.out.println("data ada");
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_riwayat, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    ProgressDialog progressDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("loginadmin", 0);
        if (pref.getBoolean("akses", true)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            this.startActivity(intent);
            getActivity().finish();
        } else {
            if (pref.getBoolean("pertama", true)) {
                //            Intent intent = new Intent(this, SplashActivity.class);
                //            this.startActivity(intent);
            }
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Load Data...");
            progressDialog.show();

            localAdminList();
            FloatingActionButton fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
            fab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        exportDataToExcel(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            FloatingActionButton fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);
            fab3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        exportDataToExcel(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String tanggal = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                    Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                    File cek = new File(Environment.getExternalStorageDirectory() + File.separator + "Posyandu" + File.separator + "Data Riwayat " + tanggal + ".xls");

                    if (cek.exists()) {
                        intentShareFile.setType("application/pdf");
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + cek.getAbsolutePath()));

                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                "Sharing File...");
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                        startActivity(Intent.createChooser(intentShareFile, "Share File"));
                    }
                }
            });
        }
    }

    public void exportDataToExcel(String[][] data) throws java.io.IOException {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Export Data...");
        progressDialog.show();
        File cek = new File(Environment.getExternalStorageDirectory() + File.separator + "Posyandu");
        if (!cek.isDirectory()) {
            cek.mkdirs();
        }
        String tanggal = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String csvFile = "Data Riwayat " + tanggal + ".xls";

        File directory = new File(cek.getAbsolutePath());
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        try {

            //file path
            File file = new File(directory, csvFile);
            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Data Riwayat", 0);
            // column and row
            sheet.addCell(new Label(0, 0, "No"));
            sheet.addCell(new Label(1, 0, "Tanggal"));
            sheet.addCell(new Label(2, 0, "Nama"));
            sheet.addCell(new Label(3, 0, "Tanggal Lahir"));
            sheet.addCell(new Label(4, 0, "Jenis Kelamin"));
            sheet.addCell(new Label(5, 0, "Anak Ke-"));
            sheet.addCell(new Label(6, 0, "Alamat"));
            sheet.addCell(new Label(7, 0, "Nama Orang Tua"));
            sheet.addCell(new Label(8, 0, "Umur"));
            sheet.addCell(new Label(9, 0, "Berat"));
            sheet.addCell(new Label(10, 0, "Tinggi"));
            sheet.addCell(new Label(11, 0, "Lingkar Kepala"));
            sheet.addCell(new Label(12, 0, "Gizi"));
            sheet.addCell(new Label(13, 0, "Keterangan"));

            for (int i = 0; i < data.length - 1; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    sheet.addCell(new Label(j, i + 1, data[i + 1][j]));
                }
            }

            workbook.write();
            workbook.close();
            progressDialog.dismiss();
            Toast.makeText(getActivity().getApplication(),
                    "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        } catch (RowsExceededException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        } catch (WriteException e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    public void localAdminList() {


        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                parseJSON(getView(), response);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    String[][] data = null;

    protected void parseJSON(View view, String json) {
        JSONArray users = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            data = new String[users.length() + 1][14];
            data[0][0] = "No";
            data[0][1] = "Tanggal";
            data[0][2] = "Nama";
            data[0][3] = "Tanggal Lahir";
            data[0][4] = "Jenis Kelamin";
            data[0][5] = "Anak Ke-";
            data[0][6] = "Alamat";
            data[0][7] = "Nama Orang Tua";
            data[0][8] = "Umur";
            data[0][9] = "Berat";
            data[0][10] = "Tinggi";
            data[0][11] = "Lingkar Kepala";
            data[0][12] = "Gizi";
            data[0][13] = "Keterangan";

            for (int i = 0; i < users.length(); i++) {
                JSONObject jo = users.getJSONObject(i);
                data[i + 1][0] = String.valueOf(i + 1);
                String tanggal = formatTanggal(jo.getString("tanggal"));
                data[i + 1][1] = tanggal;
                data[i + 1][2] = jo.getString("nama");
                data[i + 1][3] = jo.getString("tanggalahir");
                data[i + 1][4] = jo.getString("jeniskelamin");
                data[i + 1][5] = jo.getString("anakke");
                data[i + 1][6] = jo.getString("alamat");
                data[i + 1][7] = jo.getString("namaortu");
                data[i + 1][8] = jo.getString("umur");
                data[i + 1][9] = jo.getString("berat");
                data[i + 1][10] = jo.getString("tinggi");
                data[i + 1][11] = jo.getString("lkepala");
                data[i + 1][12] = jo.getString("gizi");
                data[i + 1][13] = jo.getString("keterangan");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tabel1 = (TableLayout) view.findViewById(R.id.tabelbody);

        tabelbuatan(data, tabel1);
        progressDialog.dismiss();

        //        SharedPreferences prefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        //        SharedPreferences.Editor edit = prefs.edit();
        //        edit.putInt("panjang", data.length);
        //        edit.putInt("lebar", data[0].length);
        //        for(int i=0;i<data.length; i++) {
        //            for (int j = 0; j < data[0].length; j++) {
        //                edit.putString("array["+i+"]["+j+"]", data[i][j]);
        //            }
        //        }
        //        edit.commit();
    }

    TableLayout tabel1 = null;

    public String formatTanggal(String datetime) throws ParseException {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date tanggal = sdfDate.parse(datetime);

        SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        String hasil = fo.format(tanggal);
        return hasil;
    }

    public void tabelbuatan(String[][] data, TableLayout tabel) {
        if (getActivity() != null) {
            // header
            TableRow.LayoutParams tableparam = new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f);

            TableRow rowheader = new TableRow(getActivity());
            rowheader.setLayoutParams(tableparam);
            for (int x = 0; x < data[0].length; x++) {
                TextView txt = new TextView(getActivity());
                txt.setText(data[0][x]);
                txt.setTextColor(Color.WHITE);
                txt.setGravity(Gravity.CENTER);
                txt.setPadding(10, 10, 10, 10);
                rowheader.addView(txt);
                rowheader.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            }
            tabel.addView(rowheader);
            // body
            for (int x = 1; x < data.length; x++) {
                TableRow row = new TableRow(getActivity());
                for (int y = 0; y < data[0].length; y++) {
                    TextView txt = new TextView(getActivity());
                    txt.setText(data[x][y]);
                    txt.setGravity(Gravity.CENTER);
                    txt.setPadding(10, 0, 0, 0);
                    row.addView(txt);
                }
                if (x % 2 == 1) {
                    row.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.warnabaris));
                }
                tabel.addView(row);
            }
        }
    }

}
