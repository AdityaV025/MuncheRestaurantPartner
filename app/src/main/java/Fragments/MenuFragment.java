package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.muncherestaurantpartner.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Models.MenuItemModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuFragment extends Fragment implements View.OnClickListener {

    private View view;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String Ruid,mItemName,mItemPrice,mItemIsActive,mItemSpecs;
    private FloatingActionButton mCreateNewMenuBtn;
    private DocumentReference mResMenuCategoryRef, mMenuItemRef;
    private ArrayList<String> categoryList;
    private FirestoreRecyclerAdapter<MenuItemModel, MenuItemHolder> adapter = null;
    LinearLayoutManager linearLayoutManager;
    private RecyclerView mMenuItemRecyclerView;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu,container,false);

        init();
        getMenuItems();
        mCreateNewMenuBtn.setOnClickListener(this);

        return view;
    }

    private void init() {
        mCreateNewMenuBtn = view.findViewById(R.id.createNewMenuBtn);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        assert mCurrentUser != null;
        Ruid = mCurrentUser.getUid();
        db = FirebaseFirestore.getInstance();
        mResMenuCategoryRef = db.collection("RestaurantList").document(Ruid);
        mMenuItemRecyclerView = view.findViewById(R.id.menuItemRecyclerView);
        linearLayoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        mMenuItemRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getMenuItems() {
        mResMenuCategoryRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult();
                assert documentSnapshot != null;
                if (documentSnapshot.exists()){
                    categoryList = (ArrayList<String>) documentSnapshot.get("Categories");
                    assert categoryList != null;
                    Log.d("CATEGORIES", categoryList.toString());

                    for(int i = 0 ; i < categoryList.size() ; i++){
                        String MenuItemSubCollection = categoryList.get(i);
                        CollectionReference menuItemReference = db.collection("Menu").document(Ruid).collection(MenuItemSubCollection);
                        menuItemReference.get().addOnCompleteListener(task1 -> {

                            if (task1.isSuccessful()){
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task1.getResult())) {

                                    DocumentReference mMenuItemRef = menuItemReference.document(document.getId());
                                    mMenuItemRef.get().addOnCompleteListener(task2 -> {

                                        if (task2.isSuccessful()){
                                            DocumentSnapshot mMenuItemSnapShot = task2.getResult();
                                            assert mMenuItemSnapShot != null;
                                            FirestoreRecyclerOptions<MenuItemModel> menuItemModel = new FirestoreRecyclerOptions.Builder<MenuItemModel>()
                                                    .setQuery(menuItemReference, MenuItemModel.class)
                                                    .build();
                                                adapter = new FirestoreRecyclerAdapter<MenuItemModel, MenuItemHolder>(menuItemModel) {
                                                @Override
                                                public void onBindViewHolder(MenuItemHolder holder, int position, MenuItemModel model) {
                                                    holder.mItemName.setText(model.getItemName());
                                                    String specImage = model.getItemSpecification();
                                                    if (specImage.equals("Veg")){
                                                        Glide.with(Objects.requireNonNull(requireActivity())).load(R.drawable.veg_symbol).into(holder.foodSpecification);
                                                    }else {
                                                        Glide.with(Objects.requireNonNull(requireActivity())).load(R.drawable.non_veg_symbol).into(holder.foodSpecification);
                                                    }
                                                    holder.mItemPrice.setText(model.getItemPrice());
                                                    holder.itemView.setOnClickListener(v -> {
                                                    });
                                                }
                                                @Override
                                                public MenuItemHolder onCreateViewHolder(ViewGroup group, int i) {
                                                    View view = LayoutInflater.from(group.getContext())
                                                            .inflate(R.layout.menu_item_details, group, false);
                                                    return new MenuItemHolder(view);
                                                }
                                                @Override
                                                public void onError(FirebaseFirestoreException e) {
                                                    Log.e("error", e.getMessage());
                                                }
                                            };
                                            adapter.notifyDataSetChanged();
                                            mMenuItemRecyclerView.setAdapter(adapter);
                                        }

                                    });

                                }
                            }

                        });

                    }
                }
            }

        });

    }

    public class MenuItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemName)
        TextView mItemName;
        @BindView(R.id.foodMark)
        ImageView foodSpecification;
        @BindView(R.id.itemPrice)
        TextView mItemPrice;
        @BindView(R.id.itemActiveSwitch)
        Switch isActiveSwitch;

        public MenuItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(requireActivity(), itemView);
        }
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
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}