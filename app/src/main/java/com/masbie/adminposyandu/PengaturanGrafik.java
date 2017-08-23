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
import android.widget.TextView;

import com.masbie.adminposyandu.adapter.AdapterGrafik;
import com.masbie.adminposyandu.adapter.AdapterNotifikasi;
import com.masbie.adminposyandu.tools.ExpandableHeightGridView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PengaturanGrafik.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PengaturanGrafik#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PengaturanGrafik extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String url = "";
    public static final String JSON_ARRAY = "result";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PengaturanGrafik() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Akun.
     */
    // TODO: Rename and change types and number of parameters
    public static PengaturanGrafik newInstance(String param1, String param2) {
        PengaturanGrafik fragment = new PengaturanGrafik();
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
        return inflater.inflate(R.layout.fragment_akun, container, false);
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
        final SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("loginadmin", 0);
        if(pref.getBoolean("akses", true)){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            this.startActivity(intent);
            getActivity().finish();
        } else {
            if (pref.getBoolean("pertama", true)) {
//            Intent intent = new Intent(this, SplashActivity.class);
//            this.startActivity(intent);
            }
            ExpandableHeightGridView gridView = (ExpandableHeightGridView) view.findViewById(R.id.gridview);
            gridView.setAdapter(new AdapterGrafik(getActivity()));
            gridView.setExpanded(true);
        }
    }


}
