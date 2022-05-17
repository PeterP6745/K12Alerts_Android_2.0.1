package messagelogix.com.k12campusalerts.activities.smartbuttonreports;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import messagelogix.com.k12campusalerts.R;


/**
 * Use the {@link SmartButtonMapActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SmartButtonMapActivity extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LONGITUDE = "longitude";

    private static final String ARG_LATITUDE = "latitude";

    private static final String LOG_TAG = SmartButtonMapActivity.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private Double mLongitude, mLatitude;

    //Google maps parameters
    static LatLng location = new LatLng(21, 57);

    public SmartButtonMapActivity() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param longitude Parameter 1.
     * @param latitude  Parameter 2.
     * @return A new instance of fragment SmartButtonMapActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static SmartButtonMapActivity newInstance(String longitude, String latitude) {
        SmartButtonMapActivity fragment = new SmartButtonMapActivity();
        Bundle args = new Bundle();
        args.putString(ARG_LONGITUDE, longitude);
        args.putString(ARG_LATITUDE, latitude);
        fragment.setArguments(args);
        //this.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bundle extras = getIntent().getExtras();
        if (getArguments() != null) {
            String longitude = getArguments().getString(ARG_LONGITUDE);
            String latitude = getArguments().getString(ARG_LATITUDE);

            Log.d("MapFragment", "Long is Empty --> " + longitude.trim().isEmpty() + " Lat is Empty --> " + latitude.trim().isEmpty());

            mLongitude = longitude.trim().isEmpty() ? 0 : Double.parseDouble(longitude);
            mLatitude = latitude.trim().isEmpty() ? 0 : Double.parseDouble(latitude);//getArguments().getString(ARG_LATITUDE);
            Log.d("MapFragment", "Long = " + mLongitude + " lat = " + mLatitude);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        try {
            MapFragment mapFragment = new MapFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();//getChildFragmentManager().beginTransaction();
            transaction.add(R.id.rl_map_container, mapFragment).commit();
            mapFragment.getMapAsync(this);
            Log.d("MapFragment","inside try-block after map fragment generated");
        } catch (Exception e) {
            Log.d("MapFragment","inside catch-block -> "+e);
        }

        Log.d("MapFragment","inside onCreateView just before return statement");
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(mLatitude, mLongitude), 17));
        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_red_48dp))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(mLatitude, mLongitude)));
        // MAP_TYPE_TERRAIN, MAP_TYPE_HYBRID and MAP_TYPE_NONE
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}
