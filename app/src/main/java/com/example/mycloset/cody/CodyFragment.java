package com.example.mycloset.cody;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Spinner;

import com.example.mycloset.add.GridListAdapter;
import com.example.mycloset.add.ResultActivity;
import com.example.mycloset.dao.CodyDao;
import com.example.mycloset.database.AppDatabase;
import com.example.mycloset.R;
import com.example.mycloset.entity.Cody;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CodyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CodyFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AppDatabase db;
    private CodyDao codyDao;
    private int selectedSeason;
    private CodyGridListAdapter adapter;
    private GridView closetGridview;


    public CodyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CodyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CodyFragment newInstance(String param1, String param2) {
        CodyFragment fragment = new CodyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = AppDatabase.getInstance(getActivity().getApplicationContext());
        codyDao = db.codyDao();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        selectedSeason = 0;
        View v = inflater.inflate(R.layout.fragment_cody, container, false);
        closetGridview = v.findViewById(R.id.codyGridview);
        adapter = new CodyGridListAdapter();

        /*Spinner season_spinner = (Spinner) v.findViewById(R.id.codySeasonSpinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> season_spinner_adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.season_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        season_spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        season_spinner.setAdapter(season_spinner_adapter);
        season_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.clear();
                selectedSeason = i;
                List<Cody> temp = null;
                switch (selectedSeason) {
                    case 0:
                        temp = codyDao.getSpringSelected();
                        break;
                    case 1:
                        temp = codyDao.getSummerSelected();
                        break;
                    case 2:
                        temp = codyDao.getFallSelected();
                        break;
                    case 3:
                        temp = codyDao.getWinterSelected();
                        break;
                }
                for (Cody tempCloth : temp) {
                    adapter.addItem(tempCloth);
                }
                closetGridview.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        View.OnClickListener onSeasonClick = view -> {
            adapter.clear();
            List<Cody> temp = null;
            switch (view.getId()){
                case R.id.btn_spring:
                    temp = codyDao.getSpringSelected();
                    break;
                case R.id.btn_summer:
                    temp = codyDao.getSummerSelected();
                    break;
                case R.id.btn_fall:
                    temp = codyDao.getFallSelected();
                    break;
                case R.id.btn_winter:
                    temp = codyDao.getWinterSelected();
                    break;
            }
            for (Cody tempCloth : temp) {
                adapter.addItem(tempCloth);
            }
            closetGridview.setAdapter(adapter);
        };
        v.findViewById(R.id.btn_spring).setOnClickListener(onSeasonClick);
        v.findViewById(R.id.btn_summer).setOnClickListener(onSeasonClick);
        v.findViewById(R.id.btn_fall).setOnClickListener(onSeasonClick);
        v.findViewById(R.id.btn_winter).setOnClickListener(onSeasonClick);

        List<Cody> temp = codyDao.getSpringSelected();

        for (Cody tempCloth : temp) {
            adapter.addItem(tempCloth);
        }
        closetGridview.setAdapter(adapter);

        Button newBtn = v.findViewById(R.id.newBtn);
        newBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), CodyCreateActivity.class);
            startActivity(intent);
        });

        return v;
    }
}