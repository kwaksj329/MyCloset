package com.example.mycloset;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClosetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClosetFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AppDatabase db;
    private ClothDao clothDao;
    private String[] categoryArray;
    private String selectedCategory;
    private int selectedSeason;

    public ClosetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClosetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClosetFragment newInstance(String param1, String param2) {
        ClosetFragment fragment = new ClosetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                AppDatabase.class, "closet").allowMainThreadQueries().build();
        clothDao = db.clothDao();

        categoryArray = getResources().getStringArray(R.array.category_array);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        selectedCategory = "상의";
        selectedSeason = 0;
        View v = inflater.inflate(R.layout.fragment_closet, container, false);
        GridView closetGridview = v.findViewById(R.id.closetGridview);
        GridListAdapter adapter = new GridListAdapter();

        Spinner season_spinner = (Spinner) v.findViewById(R.id.season_spinner);
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
                List<Cloth> temp = null;
                switch(selectedSeason){
                    case 0:
                        temp = clothDao.getSpringSelected(selectedCategory);
                        break;
                    case 1:
                        temp = clothDao.getSummerSelected(selectedCategory);
                        break;
                    case 2:
                        temp = clothDao.getFallSelected(selectedCategory);
                        break;
                    case 3:
                        temp = clothDao.getWinterSelected(selectedCategory);
                        break;
                }
                for(Cloth tempCloth : temp){
                    adapter.addItem(tempCloth);
                }
                closetGridview.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Spinner category_selector = (Spinner) v.findViewById(R.id.category_selector);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> category_selector_adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.category_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        category_selector_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        category_selector.setAdapter(category_selector_adapter);
        category_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.clear();
                selectedCategory = categoryArray[i];
                List<Cloth> temp = null;
                switch(selectedSeason){
                    case 0:
                        temp = clothDao.getSpringSelected(selectedCategory);
                        break;
                    case 1:
                        temp = clothDao.getSummerSelected(selectedCategory);
                        break;
                    case 2:
                        temp = clothDao.getFallSelected(selectedCategory);
                        break;
                    case 3:
                        temp = clothDao.getWinterSelected(selectedCategory);
                        break;
                }
                for(Cloth tempCloth : temp){
                    adapter.addItem(tempCloth);
                }
                closetGridview.setAdapter(adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<Cloth> temp = clothDao.getSpringSelected("상의");

        for(Cloth tempCloth : temp){
            adapter.addItem(tempCloth);
        }
        closetGridview.setAdapter(adapter);

        return v;
    }
}