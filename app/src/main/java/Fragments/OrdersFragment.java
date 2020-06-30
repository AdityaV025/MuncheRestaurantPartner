package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muncherestaurantpartner.R;
import com.facebook.shimmer.ShimmerFrameLayout;

public class OrdersFragment extends Fragment {

    private ShimmerFrameLayout mShimmerFrameLayout;
    private View view;

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
//        mShimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
//        mShimmerFrameLayout.startShimmerAnimation();
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        mShimmerFrameLayout.stopShimmerAnimation();
//        mShimmerFrameLayout.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mShimmerFrameLayout.stopShimmerAnimation();
//        mShimmerFrameLayout.setVisibility(View.GONE);
//    }
}