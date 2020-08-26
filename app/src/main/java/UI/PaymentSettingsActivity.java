package UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muncherestaurantpartner.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PaymentSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private CheckBox mCodCheckBox,mCreditCheckBox,mUPICheckBox;
    private TextView mUpiIdText;
    private EditText mUPIIdEditText;
    private FirebaseFirestore db;
    private String ruid;
    private static String RES_LIST = "RestaurantList";
    private Button mSaveInfoBtn;
    private Toolbar mPaymentToolBar;
    private ImageView mGoBackArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_settings);

        init();

    }

    private void init() {
        mPaymentToolBar = findViewById(R.id.paymentToolBar);
        mGoBackArrow = findViewById(R.id.goBackArrow);
        mGoBackArrow.setOnClickListener(view -> {
          this.onBackPressed();
        });
        db = FirebaseFirestore.getInstance();
        ruid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        mCodCheckBox = findViewById(R.id.codCheckBox);
        mCreditCheckBox = findViewById(R.id.creditCheckBox);
        mUPICheckBox = findViewById(R.id.upiCheckBox);
        mSaveInfoBtn = findViewById(R.id.savePaymentInfoBtn);
        mUpiIdText = findViewById(R.id.upiIdText);
        mUPIIdEditText = findViewById(R.id.upiIdEditText);
        mUPICheckBox.setOnClickListener(this);
        mSaveInfoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.upiCheckBox:
                if (mUPICheckBox.isChecked()){
                    mUpiIdText.setVisibility(View.VISIBLE);
                    mUPIIdEditText.setVisibility(View.VISIBLE);
                }else {
                    mUpiIdText.setVisibility(View.GONE);
                    mUPIIdEditText.setVisibility(View.GONE);
                }
            break;

            case R.id.savePaymentInfoBtn:
                selectionLogic();
                break;
        }
    }

    private void selectionLogic() {
        if (mCodCheckBox.isChecked() && mCreditCheckBox.isChecked() && mUPICheckBox.isChecked()){
            String cod = "YES";
            String card = "YES";
            String upi = mUPIIdEditText.getText().toString();
            updatePaymentSettings(cod,card,upi);
        }
        else if (mCodCheckBox.isChecked()){
            String cod = "YES";
            String card = "NO";
            String upi = "NO";
            updatePaymentSettings(cod,card,upi);
        }
        else if(mCreditCheckBox.isChecked()){
            String cod = "NO";
            String card = "YES";
            String upi = "NO";
            updatePaymentSettings(cod,card,upi);
        }
        else if(mUPICheckBox.isChecked()){
            String cod = "NO";
            String card = "NO";
            String upi = mUPIIdEditText.getText().toString();
            updatePaymentSettings(cod,card,upi);
        }
        else if(mCodCheckBox.isChecked() && mCreditCheckBox.isChecked() && !mUPICheckBox.isChecked()){
            String cod = "YES";
            String card = "YES";
            String upi = "NO";
            updatePaymentSettings(cod,card,upi);
        }
        else if(mCodCheckBox.isChecked() && mUPICheckBox.isChecked() && !mCreditCheckBox.isChecked()){
            String cod = "YES";
            String card = "NO";
            String upi =  mUPIIdEditText.getText().toString();
            updatePaymentSettings(cod,card,upi);
        }
        else if (mCreditCheckBox.isChecked() && mUPICheckBox.isChecked() && !mCodCheckBox.isChecked()){
            String cod = "NO";
            String card = "YES";
            String upi =  mUPIIdEditText.getText().toString();
            updatePaymentSettings(cod,card,upi);
        }
        else {
            String cod = "NO";
            String card = "NO";
            String upi =  "NO";
            Toast.makeText(this, "No Payment Method Selected", Toast.LENGTH_LONG).show();
            updatePaymentSettings(cod,card,upi);
        }
    }

    private void updatePaymentSettings(String cod, String card, String upi) {
        Map<String, Object> paymentMap = new HashMap<>();
        paymentMap.put("cod_payment", cod);
        paymentMap.put("card_payment", card);
        paymentMap.put("upi_payment", upi);
        db.collection(RES_LIST).document(ruid).update(paymentMap).addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Payment Info Saved", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MyProfileActivity.class);
            startActivity(intent);
            finish();

        });

    }

}