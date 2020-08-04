package UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Utils.GPSTracker;

public class CreateRestaurantActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mRestaurantName,mRestaurantPhoneNumber, mRestaurantCity, mRestaurantAddress, mRestaurantAveragePrice;
    private Button mSaveResInfoBtn;
    private RadioButton mOwnerRadioBtn, mManagerRadioBtn;
    private String resName,resNum,resCity,resAddress,checkedDesignation,RestaurantUid,Token,
            UserPhoneNumber,restaurantAveragePrice,city,state,country,postalCode,knownName,
            subLocality,subAdminArea,finalAddress,address;
    private Double latitude, longitude;
    private List<Address> addresses;
    private FirebaseFirestore db;
    private GPSTracker gpsTracker;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);

        init();
        checkPermission();
        getLocation();
        mSaveResInfoBtn.setOnClickListener(this);

    }

    private void init() {
        db = FirebaseFirestore.getInstance();
        mRestaurantName = findViewById(R.id.restaurantName);
        mRestaurantCity = findViewById(R.id.restaurantCity);
        mRestaurantPhoneNumber = findViewById(R.id.restaurantPhoneNumber);
        mRestaurantAddress = findViewById(R.id.restaurantAddress);
        mRestaurantAveragePrice = findViewById(R.id.restaurantAveragePriceEditText);
        mSaveResInfoBtn = findViewById(R.id.saveResInfoBtn);
        mOwnerRadioBtn = findViewById(R.id.ownerRadBtn);
        mManagerRadioBtn = findViewById(R.id.managerRadBtn);
        RestaurantUid = getIntent().getStringExtra("RUID");
        UserPhoneNumber = getIntent().getStringExtra("PHONENUMBER");
        Token = getIntent().getStringExtra("TOKEN");
    }

    @Override
    public void onClick(View view) {

        if (mRestaurantName.getText().toString().isEmpty()
                || mRestaurantCity.getText().toString().isEmpty()
                || mRestaurantPhoneNumber.getText().toString().isEmpty()
                || mRestaurantAddress.getText().toString().isEmpty()
                || mRestaurantAveragePrice.getText().toString().isEmpty()){

            Toast.makeText(this, "Please Enter Correct Information", Toast.LENGTH_LONG).show();

        }else {
            resName = mRestaurantName.getText().toString();
            resCity  = mRestaurantCity.getText().toString();
            resNum =  mRestaurantPhoneNumber.getText().toString();
            resAddress = mRestaurantAddress.getText().toString();
            restaurantAveragePrice = mRestaurantAveragePrice.getText().toString();

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
            RestaurantData.put("latitude", latitude);
            RestaurantData.put("longitude", longitude);
            RestaurantData.put("user_role", checkedDesignation);
            RestaurantData.put("average_price", restaurantAveragePrice);
            RestaurantData.put("restaurant_spotimage", "empty");
            RestaurantData.put("restaurant_open", "yes");

            db.collection("RestaurantList").document(RestaurantUid).set(RestaurantData)
                    .addOnSuccessListener(aVoid -> {
                sendUserToMain();
            }).addOnFailureListener(e -> {

                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

            });

        }

    }

    private void checkPermission() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getLocation() {
        gpsTracker = new GPSTracker(CreateRestaurantActivity.this);
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

            geocoder = new Geocoder(CreateRestaurantActivity.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName();
            subLocality = addresses.get(0).getSubLocality();
            subAdminArea = addresses.get(0).getSubAdminArea();

            finalAddress = knownName + ", " + subLocality +  ", " + city + ", " + postalCode;

            mRestaurantAddress.setText(finalAddress);

        }else{
            gpsTracker.showSettingsAlert();
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