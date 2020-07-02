package UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.muncherestaurantpartner.MainActivity;
import com.example.muncherestaurantpartner.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateRestaurantActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mRestaurantName,mRestaurantPhoneNumber, mRestaurantCity, mRestaurantAddress;
    private Button mSaveResInfoBtn;
    private RadioButton mOwnerRadioBtn, mManagerRadioBtn;
    private String resName,resNum,resCity,resAddress,checkedDesignation,RestaurantUid,Token,UserPhoneNumber;
    private Double mLatitude, mLongitude;
    private List<Address> address;
    private Geocoder geocoder;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        init();
        mSaveResInfoBtn.setOnClickListener(this);

    }

    private void init() {
        db = FirebaseFirestore.getInstance();
        mRestaurantName = findViewById(R.id.restaurantName);
        mRestaurantCity = findViewById(R.id.restaurantCity);
        mRestaurantPhoneNumber = findViewById(R.id.restaurantPhoneNumber);
        mRestaurantAddress = findViewById(R.id.restaurantAddress);
        mSaveResInfoBtn = findViewById(R.id.saveResInfoBtn);
        mOwnerRadioBtn = findViewById(R.id.ownerRadBtn);
        mManagerRadioBtn = findViewById(R.id.managerRadBtn);
        RestaurantUid = getIntent().getStringExtra("RUID");
        UserPhoneNumber = getIntent().getStringExtra("PHONENUMBER");
        Token = getIntent().getStringExtra("TOKEN");
        geocoder = new Geocoder(this);
    }

    @Override
    public void onClick(View view) {

        if (mRestaurantName.getText().toString().isEmpty()
                || mRestaurantCity.getText().toString().isEmpty()
                || mRestaurantPhoneNumber.getText().toString().isEmpty()
                || mRestaurantAddress.getText().toString().isEmpty()){

            Toast.makeText(this, "Please Enter Correct Information", Toast.LENGTH_LONG).show();
        }else {
            resName = mRestaurantName.getText().toString();
            resCity  = mRestaurantCity.getText().toString();
            resNum =  mRestaurantPhoneNumber.getText().toString();
            resAddress = mRestaurantAddress.getText().toString();

            try {
                address = geocoder.getFromLocationName(resAddress,5);
                if (address == null) {
                    Log.d("NULL_ADDRESS", "Address is NUll");
                }

                Address location = address.get(0);
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (mOwnerRadioBtn.isChecked()){
                checkedDesignation = mOwnerRadioBtn.getText().toString();
            }else if(mManagerRadioBtn.isChecked()){
                checkedDesignation = mManagerRadioBtn.getText().toString();
            }else {
                Toast.makeText(this, "Please Select Correct Role", Toast.LENGTH_LONG).show();
            }

            Map RestaurantData = new HashMap<>();
            RestaurantData.put("restaurant_name",resName);
            RestaurantData.put("restaurant_phonenumber", resNum);
            RestaurantData.put("restaurant_city", resCity);
            RestaurantData.put("restaurant_address", resAddress);
            RestaurantData.put("user_phonenumber", UserPhoneNumber);
            RestaurantData.put("restaurant_uid", RestaurantUid);
            RestaurantData.put("device_token", Token);
            RestaurantData.put("latitude", mLatitude);
            RestaurantData.put("longitude", mLongitude);
            RestaurantData.put("user_role", checkedDesignation);
            RestaurantData.put("restaurant_spotimage", "empty");

            db.collection("RestaurantList").document(RestaurantUid).set(RestaurantData)
                    .addOnSuccessListener(aVoid -> {
                sendUserToMain();
            }).addOnFailureListener(e -> {

                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

            });

        }

    }

    private void sendUserToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}