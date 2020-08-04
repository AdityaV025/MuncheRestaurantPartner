package UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.muncherestaurantpartner.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.suke.widget.SwitchButton;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mRestaurantNameText;
    private FirebaseAuth mAuth;
    private ImageView mRestaurantSpotImage;
    private ImageButton mChangeRestaurantSpotImageBtn;
    Uri mImageUri;
    private StorageReference mRestaurantImageRef;
    private StorageReference filePath;
    private FirebaseUser mCurrentUser;
    String ruid, mRestaurantImageUrl;
    private FirebaseFirestore restaurantDB;
    private DocumentReference mRestRef;
    private AVLoadingIndicatorView mLoadingView;
    private ProgressDialog progressDialog;
    private SwitchButton mSwitchBtn;
    private LinearLayout mLogOut, mPaymentSettings;

    //TODO: Implement ChiliPhotoPicker in MyProfileFragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        init();
        mLoadingView.show();
        loadResSpotImage();
        mLogOut.setOnClickListener(this);
        mPaymentSettings.setOnClickListener(this);
        mChangeRestaurantSpotImageBtn.setOnClickListener(this);

    }

    private void init() {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        mLoadingView = findViewById(R.id.loadingView);
        mLogOut = findViewById(R.id.logOutContainer);
        mPaymentSettings = findViewById(R.id.paymentSettingContainer);
        mRestaurantNameText = findViewById(R.id.restaurantNameText);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        assert mCurrentUser != null;
        ruid = mCurrentUser.getUid();
        mChangeRestaurantSpotImageBtn = findViewById(R.id.changeResSpotImageBtn);
        mRestaurantSpotImage = findViewById(R.id.restaurant_spotImage);
        mRestaurantImageRef = FirebaseStorage.getInstance().getReference();
        restaurantDB  = FirebaseFirestore.getInstance();
        mRestRef = restaurantDB.collection("RestaurantList").document(ruid);
        mSwitchBtn = findViewById(R.id.acceptOrderSwitch);
        progressDialog = new ProgressDialog(this);
    }

    private void loadResSpotImage() {
        mRestRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();
                assert documentSnapshot != null;
                String validateImage = String.valueOf(documentSnapshot.get("restaurant_spotimage"));
                String resName = String.valueOf(documentSnapshot.get("restaurant_name"));
                String res_status = String.valueOf(documentSnapshot.get("restaurant_open"));

                if (!res_status.equals("yes")){
                    mSwitchBtn.setChecked(false);
                }
                mRestaurantNameText.setText(resName);
                if (validateImage.equals("empty")){
                    Glide.with(this)
                            .load(R.drawable.restaurant_image_placeholder)
                            .into(mRestaurantSpotImage);
                    mLoadingView.hide();
                }else {
                    Glide.with(this)
                            .load(validateImage)
                            .placeholder(R.drawable.restaurant_image_placeholder)
                            .into(mRestaurantSpotImage);
                    mLoadingView.hide();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.changeResSpotImageBtn:
                setImage();
                break;

            case R.id.logOutContainer:
                new AlertDialog.Builder(Objects.requireNonNull(this))
                        .setMessage("Are you sure you want to log out ?")
                        .setPositiveButton("Log Out", (dialog, which) -> {
                            mAuth.signOut();
                            sendUserToLogin();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                break;

            case R.id.paymentSettingContainer:
                Intent intent = new Intent(this, PaymentSettingsActivity.class);
                startActivity(intent);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        progressDialog.setMessage("Uploading, Please Wait...");
        progressDialog.show();

        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {

                mLoadingView.show();
                assert data != null;
                mImageUri = data.getData();
                filePath = mRestaurantImageRef.child("restaurant_spot_image").child(ruid + ".jpg");
                filePath.putFile(mImageUri).addOnCompleteListener(task -> {

                    if (task.isSuccessful()){
                        mRestaurantImageRef.child("restaurant_spot_image").child(ruid + ".jpg").getDownloadUrl().addOnSuccessListener(uri -> {

                            final String downloadUrl = uri.toString();

                            UploadTask uploadTask = filePath.putFile(mImageUri);

                            uploadTask.addOnSuccessListener(taskSnapshot -> {

                                if (task.isSuccessful()){
                                    Map updateHashmap = new HashMap<>();
                                    updateHashmap.put("restaurant_spotimage", downloadUrl);

                                    mRestRef.update(updateHashmap).addOnSuccessListener(o -> {

                                        mRestRef.get().addOnCompleteListener(task1 -> {

                                            if (task1.isSuccessful()){
                                                DocumentSnapshot documentSnapshot = task1.getResult();
                                                assert documentSnapshot != null;
                                                mRestaurantImageUrl = String.valueOf(documentSnapshot.get("restaurant_spotimage"));
                                                Glide.with(this)
                                                        .load(mRestaurantImageUrl)
                                                        .into(mRestaurantSpotImage);
                                                progressDialog.dismiss();
                                            }

                                        });

                                    });

                                }

                            });

                        });
                    }

                });

            }

        }
    }

    private void sendUserToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}