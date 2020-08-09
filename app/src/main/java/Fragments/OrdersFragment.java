package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muncherestaurantpartner.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrdersFragment extends Fragment {

    private View view;
    private FirebaseFirestore db;

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_orders, container, false);

        init();

        return view;
    }

    private void init() {
        db = FirebaseFirestore.getInstance();
    }

    private void fetchLiveOrders() {



    }

}