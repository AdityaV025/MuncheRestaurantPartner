package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.muncherestaurantpartner.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MenuFragment extends Fragment implements View.OnClickListener {

    private View view;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String Ruid;
    private TextView mTempCreateMenuText;
    private Button mTempCreateMenuBtn, mTestBtn;
    private DocumentReference mResMenuRef;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu,container,false);

        init();

        mTempCreateMenuBtn.setOnClickListener(this);
        mTestBtn.setOnClickListener(this);

        return view;
    }

    private void init() {
        mTempCreateMenuText = view.findViewById(R.id.createMenuText);
        mTempCreateMenuBtn = view.findViewById(R.id.createNewMenuBtn);
        mTestBtn = view.findViewById(R.id.testBtn);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        assert mCurrentUser != null;
        Ruid = mCurrentUser.getUid();
        db = FirebaseFirestore.getInstance();
        mResMenuRef = db.collection("Menu").document(Ruid);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.createNewMenuBtn:
                Fragment fragment = new CreateNewMenuFragment();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case R.id.testBtn:
                DocumentReference documentReference = db.collection("Menu")
                        .document(Ruid)
                        .collection("Starters")
                        .document();

                Map<String, String> newMap = new HashMap<>();
                newMap.put("name", "Gol Gappe");
                newMap.put("price", "178");
                newMap.put("item_id", documentReference.getId());
                documentReference.set(newMap).addOnSuccessListener(aVoid -> {
                    Log.d("HOGAYA", "jkshdkahsdjhkajsjd");
                });
        }
    }
}