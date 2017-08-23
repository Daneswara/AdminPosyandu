package com.masbie.adminposyandu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.masbie.adminposyandu.adapter.AdapterAnak;
import com.masbie.adminposyandu.tools.ExpandableHeightGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Anak.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Anak#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Anak extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Anak() {
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
    public static Anak newInstance(String param1, String param2) {
        Anak fragment = new Anak();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_anak, container, false);
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
//        ExpandableHeightGridView gridView = (ExpandableHeightGridView) view.findViewById(R.id.gridview);
//        gridView.setAdapter(new AdapterAnak(getActivity(), data));
//        gridView.setExpanded(true);
            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(getActivity(), TambahAnak.class);
                    startActivity(in);
                }
            });
        }
    }

    public static final String url = "http://posyanduanak.com/mawar/view.php?anak=1";
    public static final String JSON_ARRAY = "result";

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
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
        requestQueue.add(stringRequest);
    }

    String[][] data = null;

    protected void parseJSON(View view, String json) {
        JSONArray users = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);
            data = new String[users.length()][10];
            for (int i = 0; i < users.length(); i++) {
                JSONObject jo = users.getJSONObject(i);
                data[i][0] = jo.getString("id");
                data[i][1] = jo.getString("nama");
                data[i][2] = jo.getString("tanggalahir");
                data[i][3] = jo.getString("beratlahir");
                data[i][4] = jo.getString("ayah");
                data[i][5] = jo.getString("ibu");
                data[i][6] = jo.getString("alamat");
                data[i][7] = jo.getString("notelp");
                data[i][8] = jo.getString("jeniskelamin");
                data[i][9] = jo.getString("anakke");
            }
            ExpandableHeightGridView gridView = (ExpandableHeightGridView) view.findViewById(R.id.gridview);
            gridView.setAdapter(new AdapterAnak(getActivity(), data));
            gridView.setExpanded(true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.dismiss();
    }


}
