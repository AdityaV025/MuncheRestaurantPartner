package Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.muncherestaurantpartner.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import UI.LoginActivity;

public class MyProfileFragment extends Fragment implements View.OnClickListener {

    private TextView mLogOutText;
    private View view;
    private FirebaseAuth mAuth;
    private ImageView mRestaurantSpotImage;
    private ImageButton mChangeRestaurantSpotImageBtn;
    Uri mImageUri;
    private StorageReference mRestaurantImageRef;
    private StorageReference filePath;
    private FirebaseUser mCurrentUser;
    String ruid;
    private FirebaseFirestore restaurantDB;
    private DocumentReference mRestRef;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        init();
        mLogOutText.setOnClickListener(this);
        mChangeRestaurantSpotImageBtn.setOnClickListener(this);

        return view;
    }

    private void init() {
        mLogOutText = view.findViewById(R.id.logOutText);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        assert mCurrentUser != null;
        ruid = mCurrentUser.getUid();
        mChangeRestaurantSpotImageBtn = view.findViewById(R.id.changeResSpotImageBtn);
        mRestaurantSpotImage = view.findViewById(R.id.restaurant_spotImage);
        mRestaurantImageRef = FirebaseStorage.getInstance().getReference();
        restaurantDB  = FirebaseFirestore.getInstance();
        mRestRef = restaurantDB.collection("RestaurantList").document(ruid);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.logOutText:
                new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                        .setMessage("Are you sure you want to log out ?")
                        .setPositiveButton("Log Out", (dialog, which) -> {
                            mAuth.signOut();
                            sendUserToLogin();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                break;

            case R.id.changeResSpotImageBtn:
                setImage();
                break;

        }
    }

    private void setImage() {
        ImagePicker.Companion.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start(123);

    }

    private void sendUserToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

}