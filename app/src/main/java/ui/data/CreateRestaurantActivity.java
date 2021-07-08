package ui.data;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.muncherestaurantpartner.MainActivity;
import com.example.muncherestaurantpartner.R;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hootsuite.nachos.NachoTextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import utils.GPSTracker;

public class CreateRestaurantActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mRestaurantName,mRestaurantPhoneNumber, mRestaurantCity, mRestaurantAddress, mRestaurantAveragePrice;
    private Button mSaveResInfoBtn;
    private RadioButton mOwnerRadioBtn, mManagerRadioBtn;
    private String checkedDesignation;
    private String RestaurantUid;
    private String Token;
    private String UserPhoneNumber;
    private String postalCode;
    private String knownName;
    private String subLocality;
    private Double latitude, longitude;
    private List<Address> addresses;
    private FirebaseFirestore db;
    private NachoTextView mResCuisineText;
    String[] suggestions = new String[]{"Appetizers","Entrees","Starters","Salads",
            "Main Course","Desserts","Ice Cream","Biryani","Parathas","Pizzas","Burgers",
            "Sandwiches","Drinks","Beverages","Alcoholics","Sushi", "Pasta","Cakes","Pastries",
            "South Indian","North Indian","Thali","Dosas","Chinese", "Soups","Bakery"
            ,"Thai","Italian","Fast Food","Rolls","Sweets","Mughlai"};

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
        mResCuisineText = findViewById(R.id.restaurantCuisine);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        mResCuisineText.setAdapter(adapter);
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
                || mRestaurantAveragePrice.getText().toString().isEmpty()
                || mResCuisineText.getAllChips().isEmpty()){

            Toast.makeText(this, "Please Enter Correct Information", Toast.LENGTH_LONG).show();

        }else {
            String resName = mRestaurantName.getText().toString();
            String resCity = mRestaurantCity.getText().toString();
            String resNum = mRestaurantPhoneNumber.getText().toString();
            String resAddress = mRestaurantAddress.getText().toString();
            String restaurantAveragePrice = mRestaurantAveragePrice.getText().toString();

            if (mOwnerRadioBtn.isChecked()){
                checkedDesignation = mOwnerRadioBtn.getText().toString();
            }else if(mManagerRadioBtn.isChecked()){
                checkedDesignation = mManagerRadioBtn.getText().toString();
            }else {
                Toast.makeText(this, "Please Select Correct Role", Toast.LENGTH_LONG).show();
            }

            HashMap<String,Object> RestaurantData = new HashMap<>();
            RestaurantData.put("restaurant_name", resName);
            RestaurantData.put("search_restaurant", resName.toLowerCase());
            RestaurantData.put("restaurant_cuisine", FieldValue.arrayUnion(mResCuisineText.getChipValues().toArray()));
            RestaurantData.put("cuisine_text", mResCuisineText.getChipValues().toString());
            RestaurantData.put("restaurant_phonenumber", resNum);
            RestaurantData.put("restaurant_city", resCity);
            RestaurantData.put("restaurant_address", resAddress);
            RestaurantData.put("user_phonenumber", UserPhoneNumber);
            RestaurantData.put("restaurant_uid", RestaurantUid);
            RestaurantData.put("device_token", Token);
            RestaurantData.put("latitude", latitude);
            RestaurantData.put("longitude", longitude);
            RestaurantData.put("knownname", knownName);
            RestaurantData.put("sublocality", subLocality);
            RestaurantData.put("postalcode",postalCode);
            RestaurantData.put("user_role", checkedDesignation);
            RestaurantData.put("average_price", restaurantAveragePrice);
            RestaurantData.put("restaurant_spotimage", "empty");
            RestaurantData.put("restaurant_open", "yes");
            RestaurantData.put("restaurant_prep_time", "10 Mins");

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
        GPSTracker gpsTracker = new GPSTracker(CreateRestaurantActivity.this);
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

            Geocoder geocoder = new Geocoder(CreateRestaurantActivity.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            String city = addresses.get(0).getLocality();
            postalCode = addresses.get(0).getPostalCode();
            knownName = addresses.get(0).getFeatureName();
            subLocality = addresses.get(0).getSubLocality();

            String finalAddress = knownName + ", " + subLocality + ", " + city + ", " + postalCode;

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