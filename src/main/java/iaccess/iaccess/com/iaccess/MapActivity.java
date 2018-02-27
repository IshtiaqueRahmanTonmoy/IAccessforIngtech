package iaccess.iaccess.com.iaccess;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import iaccess.iaccess.com.entity.GeocodingLocation;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    String location;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    double latitude,longitute;
    Geocoder geocoder = null;
    MapView mapView = null;
    Context context;
    LatLng p1 = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        location = intent.getStringExtra("locationin");

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Geocoder coder = new Geocoder(MapActivity.this);
        List<Address> address;
        try {
            // May throw an IOException
            address = coder.getFromLocationName(location, 5);
            Address locations = address.get(0);
            latitude = locations.getLatitude();
            longitute = locations.getLongitude();
            p1 = new LatLng(latitude, longitute);

            //Toast.makeText(MapActivity.this, "latitude"+lat+"longitude"+longt, Toast.LENGTH_SHORT).show();


        } catch (IOException ex) {

            ex.printStackTrace();
        }


        //getLocationFromAddress(context,location);
    }

    private LatLng getLocationFromAddress(Context context, String location) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(location, 5);
            if (address == null) {
                return null;
            }
            Address locations = address.get(0);
            double lat = locations.getLatitude();
            double longt = locations.getLongitude();

            Toast.makeText(context, "latitude"+lat+"longitude"+longt, Toast.LENGTH_SHORT).show();
            p1 = new LatLng(locations.getLatitude(), locations.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    // GeocodingLocation locationAddress = new GeocodingLocation();
       // locationAddress.getAddressFromLocation(location,
        //        getApplicationContext(), new GeocoderHandler());



    /*
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {

            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    latitude = bundle.getDouble("address");
                    longitute = bundle.getDouble("address1");
                    Log.d("lat", String.valueOf(latitude));
                    Toast.makeText(MapActivity.this, ""+latitude, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    latitude = 0.0;
                    longitute = 0.0;
            }
            //Toast.makeText(MapActivity.this, ""+latitude+""+longitute, Toast.LENGTH_SHORT).show();
           // Log.i("locationaddress",latitude+","+""+longitute);
        }
    }

    */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng TutorialsPoint = new LatLng(latitude,longitute);
        mMap.addMarker(new
                MarkerOptions().position(TutorialsPoint).title("Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TutorialsPoint,16));
       // mMap.animateCamera(CameraUpdateFactory.zoomTo(mMap.getCameraPosition().zoom - 0.5f));
    }
}
