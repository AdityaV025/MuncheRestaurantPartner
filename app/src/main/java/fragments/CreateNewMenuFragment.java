package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.muncherestaurantpartner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateNewMenuFragment extends Fragment implements View.OnClickListener{

    private View view;
    private MaterialSpinner mCategorySpinner;
    private EditText mMenuItemName, mMenuItemPrice, mMenuItemDesc;
    private String Ruid;
    private String mMenuCategory;
    private String mItemVegOrNot;
    private FirebaseFirestore db;

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

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        assert mCurrentUser != null;
        Ruid = mCurrentUser.getUid();

        mCategorySpinner = view.findViewById(R.id.chooseCategorySpinner);
        MaterialSpinner mFoodVegOrNotSpinner = view.findViewById(R.id.foodVegOrNotSpinner);
        Button mSaveMenuItemInfo = view.findViewById(R.id.saveItemInfoBtn);
        mMenuItemName = view.findViewById(R.id.newMenuItemEditText);
        mMenuItemPrice = view.findViewById(R.id.menuItemPrice);
        mMenuItemDesc = view.findViewById(R.id.menuItemDescription);
        mCategorySpinner.setItems("Select Category","Appetizers","Entrees","Starters","Salads",
                "Main Course","Desserts","Ice Cream","Biryani","Parathas","Pizzas","Burgers",
                "Sandwiches","Drinks","Beverages","Alcoholics","Sushi", "Pasta","Cakes","Pastries",
                "South Indian","North Indian","Thali","Dosas","Chinese", "Soups","Recommends");
        mCategorySpinner.setOnItemSelectedListener((view, position, id, item) -> {
            if (!item.toString().equals("Select Category")){
                mMenuCategory = item.toString();
            }
        });
        mFoodVegOrNotSpinner.setItems("Choose","Veg", "NonVeg");
        mFoodVegOrNotSpinner.setOnItemSelectedListener((view, position, id, item) -> {
            if (!item.toString().equals("Choose")){
                mItemVegOrNot = item.toString();
            }
        });
        mSaveMenuItemInfo.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.saveItemInfoBtn){

            String mItemName = mMenuItemName.getText().toString();
            String mItemPrice = mMenuItemPrice.getText().toString();
            String mItemDesc = mMenuItemDesc.getText().toString();

            DocumentReference mMenuRef = db.collection("Menu")
                    .document(Ruid)
                    .collection("MenuItems")
                    .document(mItemName);

            String menuItemId = String.valueOf(UUID.randomUUID()).replace("-","");
            Map<String, String> menuItemMap = new HashMap<>();
            menuItemMap.put("name", mItemName);
            menuItemMap.put("price", mItemPrice);
            menuItemMap.put("category", mMenuCategory);
            menuItemMap.put("specification", mItemVegOrNot);
            menuItemMap.put("description", mItemDesc);
            menuItemMap.put("is_active", "yes");
            menuItemMap.put("menuUid" , menuItemId);
            menuItemMap.put("category_index", String.valueOf(mCategorySpinner.getSelectedIndex()));

            mMenuRef.set(menuItemMap).addOnSuccessListener(aVoid -> {
                Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_LONG).show();
                Fragment fragment = new MenuFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            });

        }

    }
}