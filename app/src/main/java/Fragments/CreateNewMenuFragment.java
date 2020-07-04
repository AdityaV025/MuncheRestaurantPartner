package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.muncherestaurantpartner.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class CreateNewMenuFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private View view;
    private Spinner mCategorySpinner;

    public CreateNewMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_new_menu, container, false);

        init();

        return view;
    }

    private void init() {
        mCategorySpinner = view.findViewById(R.id.chooseCategorySpinner);
        String[] mCategoryArray = new String[] {
          "Select Category","Appetizers","Entrees","Starters","Salads","Main Course","Desserts","Ice Cream","Biryani",
                "Parathas","Pizzas","Burgers","Sandwiches","Drinks","Beverages","Alcoholics","Sushi",
                "Pasta","Cakes","Pastries","South Indian","North Indian","Thali","Dosas","Chinese",
                "Soups","Recommends"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_spinner_item, mCategoryArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(adapter);
        mCategorySpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (!mCategorySpinner.getSelectedItem().toString().equals("Select Category")){
            Toast.makeText(requireActivity(), mCategorySpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}