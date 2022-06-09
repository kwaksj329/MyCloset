package com.example.mycloset.coordi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.example.mycloset.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoordiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoordiFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "EmailPassword";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CoordiGridListAdapter adapter;
    private GridView closetGridview;


    public CoordiFragment() {
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
    public static CoordiFragment newInstance(String param1, String param2) {
        CoordiFragment fragment = new CoordiFragment();
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
        View v = inflater.inflate(R.layout.fragment_coordi, container, false);
        closetGridview = v.findViewById(R.id.codyGridview);
        adapter = new CoordiGridListAdapter();


        View.OnClickListener onSeasonClick = view -> {
            adapter.clear();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference clothesRef = db.collection("coordinates");

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Query query = clothesRef.whereEqualTo("user", user.getUid());

            switch (view.getId()){
                case R.id.btn_spring:
                    query = query.whereEqualTo("spring", true);
                    break;
                case R.id.btn_summer:
                    query = query.whereEqualTo("summer", true);
                    break;
                case R.id.btn_fall:
                    query = query.whereEqualTo("fall", true);
                    break;
                case R.id.btn_winter:
                    query = query.whereEqualTo("winter", true);
                    break;
            }query
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                adapter.addItem(document);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
//                                closetGridview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        };
        v.findViewById(R.id.btn_spring).setOnClickListener(onSeasonClick);
        v.findViewById(R.id.btn_summer).setOnClickListener(onSeasonClick);
        v.findViewById(R.id.btn_fall).setOnClickListener(onSeasonClick);
        v.findViewById(R.id.btn_winter).setOnClickListener(onSeasonClick);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference clothesRef = db.collection("coordinates");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = clothesRef.whereEqualTo("user", user.getUid())
                .whereEqualTo("spring", true);
        query
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            adapter.addItem(document);
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                        closetGridview.setAdapter(adapter);

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
        
        Button newBtn = v.findViewById(R.id.newBtn);
        newBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), CoordiCreateActivity.class);
            startActivity(intent);
        });

        return v;
    }
}