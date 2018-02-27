package iaccess.iaccess.com.entity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocodingLocation {

    private static final String TAG = "GeocodingLocation";

    public static void getAddressFromLocation(final String locationAddress,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                double result = 0.0,result1= 0.0;
                try {
                    List<Address> addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        StringBuilder sb1 = new StringBuilder();
                        sb.append(address.getLatitude());
                        sb1.append(address.getLongitude());
                        result = Double.parseDouble(sb.toString());
                        result1 = Double.parseDouble(sb1.toString());
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (!(result == 0) && !(result1 == 0)) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        //result = "\n\nLatitude and Longitude :\n" + result;
                        bundle.putDouble("address", result);
                        bundle.putDouble("address1", result1);

                        Log.d("values",result+""+result1);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        //result = "Address: " + locationAddress +
                        //        "\n Unable to get Latitude and Longitude for this address location.";
                        bundle.putDouble("address", result);
                        bundle.putDouble("address1", result1);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}