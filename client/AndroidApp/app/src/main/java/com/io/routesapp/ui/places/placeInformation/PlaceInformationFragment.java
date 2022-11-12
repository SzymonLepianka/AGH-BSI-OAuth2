package com.io.routesapp.ui.places.placeInformation;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.io.routesapp.MainActivity;
import com.io.routesapp.R;
import com.io.routesapp.ui.places.model.Place;
import com.io.routesapp.ui.places.model.PlaceReview;
import com.io.routesapp.ui.places.model.PlaceReviewAdapter;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.Objects;

//Class displaying place details, location and reviews
public class PlaceInformationFragment extends Fragment {
    RecyclerView mRecyclerView;
    PlaceReviewAdapter placeReviewAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<PlaceReview> reviewsList;
    FloatingActionButton addFAB;
    View reviewField;
    TextView firstComment;
    int id;
    Place place;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng coords = new LatLng(place.getLatitude(), place.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(coords).title(place.getName()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 15));
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = requireArguments().getInt("id");
        try {
            place = MainActivity.HTTPClient.getPlace(id);
            reviewsList = MainActivity.HTTPClient.getPlaceReviews(id);
        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error while loading page...", Toast.LENGTH_SHORT).show();
        }
        
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_place_information, container, false);

        mRecyclerView = root.findViewById(R.id.review_list);
        layoutManager = new LinearLayoutManager(getActivity());

        firstComment = root.findViewById(R.id.firstComment);
        firstComment.setVisibility(View.INVISIBLE);

        if (reviewsList.isEmpty()){
            firstComment.setVisibility(View.VISIBLE);
        }

        placeReviewAdapter = new PlaceReviewAdapter(reviewsList);
        mRecyclerView.setAdapter(placeReviewAdapter);

        addFAB = root.findViewById(R.id.add_fab);
        reviewField = root.findViewById(R.id.review_field);

        addFAB.setOnClickListener(v -> {
            reviewField.setVisibility(View.VISIBLE);
            addFAB.setVisibility(View.GONE);
        });

        RatingBar placeRatingBar = root.findViewById(R.id.place_rating_bar);
        final TextInputEditText reviewText= root.findViewById(R.id.review_text);
        final FloatingActionButton sendFAB = root.findViewById(R.id.send_fab);

        reviewText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Objects.requireNonNull(reviewText.getText()).toString().isEmpty()){
                    sendFAB.setEnabled(true);
                }
            }
        });

        sendFAB.setOnClickListener(v -> {
            MainActivity.HTTPClient.addPlaceReview(
                    new PlaceReview(
                            String.valueOf(id),
                            MainActivity.getLoggedInUser().getUsername(),
                            Objects.requireNonNull(reviewText.getText()).toString()
                    )
            );
            reviewField.setVisibility(View.GONE);
            firstComment.setVisibility(View.INVISIBLE);
            sendFAB.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Your review will be added!", Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

}
