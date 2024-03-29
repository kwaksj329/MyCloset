package com.example.mycloset.add;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mycloset.R;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

    /**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int MANUAL = 0;
    public static final int AUTO = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CropTouchView cropTouchView;

    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
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

        View v = inflater.inflate(R.layout.fragment_add, container, false);
        cropTouchView = v.findViewById(R.id.cropview);
        v.findViewById(R.id.complete_btn).setOnClickListener(view -> cropTouchView.completed());
        v.findViewById(R.id.clear_btn).setOnClickListener(view -> cropTouchView.clear());
        v.findViewById(R.id.result_btn).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), ResultActivity.class);
            Bitmap sendBitmap = cropTouchView.getToCrop();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            intent.putExtra("image",byteArray);
            intent.putExtra("mode", MANUAL);
            startActivity(intent);
        });

        v.findViewById(R.id.auto_btn).setOnClickListener(view -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), ResultActivity.class);
            Bitmap sendBitmap = cropTouchView.getToCrop();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            intent.putExtra("image",byteArray);
            intent.putExtra("mode", AUTO);
            startActivity(intent);
        });

        openSelectPhotoForResult();
        return v;
    }

    public void openSelectPhotoForResult() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        SelectPhotoResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> SelectPhotoResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();
                        Bitmap resizedImage = null;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver()
                                    , selectedImageUri);
                            int viewHeight = cropTouchView.getHeight();
                            int viewWidth = cropTouchView.getWidth();
                            int imageHeight = bitmap.getHeight();
                            int imageWidth = bitmap.getWidth();
                            if (imageHeight / (float)imageWidth > viewHeight / (float)viewWidth) {
                                resizedImage = Bitmap.createScaledBitmap(bitmap, (imageWidth * viewHeight) / imageHeight, viewHeight, true);
                            }
                            else {
                                resizedImage = Bitmap.createScaledBitmap(bitmap, viewWidth, (imageHeight * viewWidth) / imageWidth, true);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        cropTouchView.setToCrop(resizedImage);
                    }
                }
            });

}