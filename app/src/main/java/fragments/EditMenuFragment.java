package fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.muncherestaurantpartner.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class EditMenuFragment extends Fragment {

    private View view;
    private MaterialSpinner mCategorySpinnerEdit, mFoodVegOrNotSpinnerEdit;
    private EditText mMenuItemNameEdit, mMenuItemPriceEdit, mMenuItemDescEdit;
    private Button mUpdateMenuItemBtn, mDeleteMenuItemBtn;

    public EditMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_menu, container, false);

        init();

        return view;
    }

    private void init() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mCategorySpinnerEdit = view.findViewById(R.id.chooseCategorySpinnerEdit);
        mFoodVegOrNotSpinnerEdit = view.findViewById(R.id.foodVegOrNotSpinnerEdit);
        mMenuItemNameEdit = view.findViewById(R.id.newMenuItemEditTextEdit);
        mMenuItemPriceEdit = view.findViewById(R.id.menuItemPriceEdit);
        mMenuItemDescEdit = view.findViewById(R.id.menuItemDescriptionEdit);
        mUpdateMenuItemBtn = view.findViewById(R.id.updateItemInfoBtnEdit);
        mDeleteMenuItemBtn = view.findViewById(R.id.deleteItemInfoBtnEdit);
    }

}