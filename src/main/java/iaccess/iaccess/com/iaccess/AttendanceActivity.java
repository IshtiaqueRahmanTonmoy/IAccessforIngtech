package iaccess.iaccess.com.iaccess;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.google.android.gms.auth.api.Auth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import iaccess.iaccess.com.entity.GPSTracker;

public class AttendanceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String GETALL_URL = "http://i-access.ingtechbd.com/api/access-logs/add";

    Toolbar toolbar,toolbar1;
    private StringRequest stringRequest;
    private static final String TAG = "Test GPS";
    private LocationManager mLocationManager = null;
    private GPSTracker gps;
    private Button checkIn,checkOut,submitBtn;
    private double in_lat,in_long,out_lat,out_long;
    private EditText remarksEdt;
    private String remarks,in_location,timezone,time,out_location,hour,intime,format,format1,time1,roleval,intimevalue,hourin;
    boolean checkinclicked=false,checkoutclicked=false;
    Timestamp ts=null;
    TextView nameText,designationText;
    String yearout,mthout,dout,timeout,hsout,hourout,outtimevalue,access_token,Authorization;
    int hrsout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        initializeLocationManager();

        AttendanceActivity.this.setTitle("i-Attendance");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        roleval = getIntent().getStringExtra("userrole");
        access_token = getIntent().getStringExtra("accesstoken");
        //Toast.makeText(AttendanceActivity.this, ""+access_token, Toast.LENGTH_SHORT).show();
        Authorization = "Bearer"+" "+access_token;

        toolbar1 = (Toolbar) findViewById(R.id.page_toolbar);
        checkIn = (Button) findViewById(R.id.checkIn);
        checkOut = (Button) findViewById(R.id.checkOut);
        submitBtn = (Button) findViewById(R.id.submit);
        remarksEdt = (EditText) findViewById(R.id.remarksEdt);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar1.setTitle("");
        toolbar1.setSubtitle("");

        setSupportActionBar(toolbar1);

        getValue();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        nameText = (TextView) headerView.findViewById(R.id.nameNav);
        designationText = (TextView) headerView.findViewById(R.id.designationNav);

        nameText.setText("Nur A Alam");
        designationText.setText("Developer");

        Menu menu =navigationView.getMenu();


        if(roleval.equals("user")) {
            MenuItem target = menu.findItem(R.id.nav_employees);
            target.setVisible(false);

            //MenuItem target1 = menu.findItem(R.id.employee);
            //target1.setVisible(false);
        }


        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkinclicked = true;
                checkoutclicked = false;

                ColorDrawable buttonColor = (ColorDrawable) checkOut.getBackground();

                int colorId = buttonColor.getColor();
                if(colorId==R.color.red){
                    checkIn.setBackgroundColor(getResources().getColor(R.color.red));
                    checkOut.setBackgroundColor(getResources().getColor(R.color.pinkwhite));
                }
                else if(colorId==R.color.green){
                    checkIn.setBackgroundColor(getResources().getColor(R.color.green));
                    checkOut.setBackgroundColor(getResources().getColor(R.color.pinkwhite));
                }
                else {
                    checkIn.setBackgroundColor(getResources().getColor(R.color.pinkdeep));
                    checkOut.setBackgroundColor(getResources().getColor(R.color.pinkwhite));
                }


               //checkIn();
            }
        });

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkoutclicked = true;
                checkinclicked = false;

                int color = ((ColorDrawable)checkIn.getBackground()).getColor();

                //Log.d("color", String.valueOf(color));
                //Toast.makeText(AttendanceActivity.this, ""+color, Toast.LENGTH_SHORT).show();

                if(color == -973788){
                    checkIn.setBackgroundColor(getResources().getColor(R.color.red));
                    checkOut.setBackgroundColor(getResources().getColor(R.color.pinkdeep));
                }
                else if(color == -14421476){
                    checkIn.setBackgroundColor(getResources().getColor(R.color.green));
                    checkOut.setBackgroundColor(getResources().getColor(R.color.pinkdeep));
                }
                else {
                    checkIn.setBackgroundColor(getResources().getColor(R.color.pinkwhite));
                    checkOut.setBackgroundColor(getResources().getColor(R.color.pinkdeep));
                }


            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(checkinclicked){
                     remarks = remarksEdt.getText().toString();
                     checkIn();
                     remarksEdt.setText("");
                 }
                 else{

                 }
                 if(checkoutclicked){
                     remarks = remarksEdt.getText().toString();
                     checkOut();
                     remarksEdt.setText("");
                 }
                 else{

                 }
            }
        });
    }

    private void getValue() {
        final String intime,hoursh,minute;
        RequestQueue queue = Volley.newRequestQueue(AttendanceActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, GETALL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsevalue", response);
                        try{
                            JSONObject json = new JSONObject(response);
                            JSONObject obj = json.getJSONObject("data");

                            Log.d("obj",obj.toString());
                            String in_lats = obj.getString("in_lat");
                            String out_lats = obj.getString("out_lat");
                            //Log.d("intimevalue",in_lats);

                            String in_long = obj.getString("in_long");
                            String out_long = obj.getString("out_long");

                            final String in_loc = obj.getString("in_location");
                            final String out_loc = obj.getString("out_location");

                            JSONObject objtime = obj.getJSONObject("in_time");
                            String indates = objtime.getString("date");
                            String[] parts = indates.split("-");
                            String year = parts[0];
                            String mth = parts[1];
                            String d = parts[2];

                            Log.d("vals",year+mth+d);

                            String[] partsss = d.split(" ");
                            String times = partsss[1];
                            String[] timepartss = times.split(":");

                            String hs = timepartss[0];
                            int hrs = Integer.parseInt(hs);

                            if (hrs == 1) {
                                hourin = "1";
                            }
                            else if (hrs == 2) {
                                hourin = "2";
                            }
                            else if (hrs == 3) {
                                hourin = "3";
                            }
                            else if (hrs == 4) {
                                hourin = "4";
                            }
                            else if (hrs == 5) {
                                hourin = "5";
                            }
                            else if (hrs == 6) {
                                hourin = "1";
                            }
                            else if (hrs == 7) {
                                hourin = "7";
                            }
                            else if (hrs == 8) {
                                hourin = "8";
                            }
                            else if (hrs == 9) {
                                hourin = "9";
                            }
                            else if (hrs == 10) {
                                hourin = "10";
                            }
                            else if (hrs == 11) {
                                hourin = "11";
                            }
                            else if (hrs == 12) {
                                hourin = "12";
                            }
                            else if (hrs == 13) {
                                hourin = "1";
                            } else if (hrs == 14) {
                                hourin = "2";
                            } else if (hrs == 15) {
                                hourin = "3";
                            } else if (hrs == 16) {
                                hourin = "4";
                            } else if (hrs == 17) {
                                hourin = "5";
                            } else if (hrs == 18) {
                                hourin = "6";
                            } else if (hrs == 19) {
                                hourin = "7";
                            } else if (hrs == 20) {
                                hourin = "8";
                            } else if (hrs == 21) {
                                hourin = "9";
                            } else if (hrs == 22) {
                                hourin = "10";
                            } else if (hrs == 23) {
                                hourin = "11";
                            } else if(hrs == 24){
                                hourin = "12";
                            }

                            Log.d("hrs", String.valueOf(hrs));

                            String mn = timepartss[1];
                            int mttn = Integer.parseInt(mn);
                            Log.d("mnts", mn);

                            if (hrs <= 12) {
                                intimevalue = hourin + ":" + mttn + "AM";
                                checkIn.setText(intimevalue);
                                checkIn.setBackgroundColor(getResources().getColor(R.color.green));
                                checkIn.setTextColor(getResources().getColor(R.color.white));
                                submitBtn.setText("CHECK OUT");
                                checkIn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(  AttendanceActivity.this);

                                        // Setting Dialog Title
                                        alertDialog.setTitle("CheckIn Location...");

                                        // Setting Dialog Message
                                        alertDialog.setMessage(in_loc);

                                        // Setting Icon to Dialog
                                        alertDialog.setIcon(R.drawable.map);

                                        // Setting Positive "Yes" Button
                                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int which) {

                                                // Write your code here to invoke YES event
                                                // Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        // Setting Negative "NO" Button
                                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Write your code here to invoke NO event
                                                //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                                // dialog.cancel();
                                            }
                                        });

                                        // Showing Alert Message
                                        alertDialog.show();
                                    }
                                });
                            } else {
                                intimevalue = hourin + ":" + mttn + "PM";
                                checkIn.setText(intimevalue);
                                checkIn.setBackgroundColor(getResources().getColor(R.color.red));
                                checkIn.setTextColor(getResources().getColor(R.color.white));
                                submitBtn.setText("CHECK OUT");
                                checkIn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(  AttendanceActivity.this);

                                        // Setting Dialog Title
                                        alertDialog.setTitle("CheckIn Location...");

                                        // Setting Dialog Message
                                        alertDialog.setMessage(in_loc);

                                        // Setting Icon to Dialog
                                        alertDialog.setIcon(R.drawable.map);

                                        // Setting Positive "Yes" Button
                                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int which) {

                                                // Write your code here to invoke YES event
                                                // Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        // Setting Negative "NO" Button
                                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Write your code here to invoke NO event
                                                //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                                // dialog.cancel();
                                            }
                                        });

                                        // Showing Alert Message
                                        alertDialog.show();
                                    }
                                });
                            }


                            JSONObject objout = obj.getJSONObject("out_time");
                            String outdate = objout.getString("date");

                            String[] partsout = outdate.split("-");
                            String yearout = partsout[0];
                            String mthout = partsout[1];
                            String dout = partsout[2];

                            Log.d("vals",year+mth+d);

                            String[] partsssout = dout.split(" ");
                            String timeout = partsssout[1];
                            String[] timepartssout = timeout.split(":");

                            String hsout = timepartssout[0];
                            int hrsout = Integer.parseInt(hsout);

                            if (hrsout == 1) {
                                hourout = "1";
                            }
                            else if (hrsout == 2) {
                                hourout = "2";
                            }
                            else if (hrsout == 3) {
                                hourout = "3";
                            }
                            else if (hrsout == 4) {
                                hourout = "4";
                            }
                            else if (hrsout == 5) {
                                hourout = "5";
                            }
                            else if (hrsout == 6) {
                                hourout = "1";
                            }
                            else if (hrsout == 7) {
                                hourout = "7";
                            }
                            else if (hrsout == 8) {
                                hourout = "8";
                            }
                            else if (hrsout == 9) {
                                hourout = "9";
                            }
                            else if (hrsout == 10) {
                                hourout = "10";
                            }
                            else if (hrsout == 11) {
                                hourout = "11";
                            }
                            else if (hrsout == 12) {
                                hourout = "12";
                            }
                            else if (hrsout == 13) {
                                hourout = "1";
                            } else if (hrsout == 14) {
                                hourout = "2";
                            } else if (hrsout == 15) {
                                hourout = "3";
                            } else if (hrsout == 16) {
                                hourout = "4";
                            } else if (hrsout == 17) {
                                hourout = "5";
                            } else if (hrsout == 18) {
                                hourout = "6";
                            } else if (hrsout == 19) {
                                hourout = "7";
                            } else if (hrsout == 20) {
                                hourout = "8";
                            } else if (hrsout == 21) {
                                hourout = "9";
                            } else if (hrsout == 22) {
                                hourout = "10";
                            } else if (hrsout == 23) {
                                hourout = "11";
                            } else if(hrsout == 24){
                                hourout = "12";
                            }

                            Log.d("hrs", String.valueOf(hrs));

                            String mnout = timepartssout[1];
                            int mttnout = Integer.parseInt(mnout);
                            Log.d("mnts", mn);

                            if (hrsout <= 12) {
                                outtimevalue = hourout + ":" + mttnout + "AM";
                                checkOut.setText(outtimevalue);
                                checkOut.setBackgroundColor(getResources().getColor(R.color.green));
                                checkOut.setTextColor(getResources().getColor(R.color.white));
                                submitBtn.setText("Attendance Complete");
                                submitBtn.setEnabled(false);
                                checkOut.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(  AttendanceActivity.this);

                                        // Setting Dialog Title
                                        alertDialog.setTitle("CheckOut Location...");

                                        // Setting Dialog Message
                                        alertDialog.setMessage(out_loc);

                                        // Setting Icon to Dialog
                                        alertDialog.setIcon(R.drawable.map);

                                        // Setting Positive "Yes" Button
                                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int which) {

                                                // Write your code here to invoke YES event
                                                // Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        // Setting Negative "NO" Button
                                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Write your code here to invoke NO event
                                                //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                                // dialog.cancel();
                                            }
                                        });

                                        // Showing Alert Message
                                        alertDialog.show();
                                    }
                                });
                            } else {
                                outtimevalue = hourout + ":" + mttnout + "PM";
                                checkOut.setText(outtimevalue);
                                checkOut.setBackgroundColor(getResources().getColor(R.color.red));
                                checkOut.setTextColor(getResources().getColor(R.color.white));
                                submitBtn.setText("Attendance Complete");
                                submitBtn.setEnabled(false);
                                checkOut.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(  AttendanceActivity.this);

                                        // Setting Dialog Title
                                        alertDialog.setTitle("CheckOut Location...");

                                        // Setting Dialog Message
                                        alertDialog.setMessage(out_loc);

                                        // Setting Icon to Dialog
                                        alertDialog.setIcon(R.drawable.map);

                                        // Setting Positive "Yes" Button
                                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int which) {

                                                // Write your code here to invoke YES event
                                                // Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        // Setting Negative "NO" Button
                                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Write your code here to invoke NO event
                                                //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                                // dialog.cancel();
                                            }
                                        });

                                        // Showing Alert Message
                                        alertDialog.show();
                                    }
                                });
                            }


                           // Log.d("intime",in_lat);



                        }catch (Exception e) {e.printStackTrace();}
                        //Toast.makeText(AttendanceActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        // Display the response string.
                        //_response.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //_response.setText("That didn't work!");
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String,String>();
                headers.put("Accept","application/json");
                headers.put("Authorization", Authorization);
                //headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjViYjJlYjI2ZDhhNmEwZjA5ZTE5ZmQyZTk1ZmExNWIxZDhjZmQwYWRhZjYxYTJjZDRlMTA4NmY3OGZjZWJjNzlhYzNlOTRhYmQ1YmQzODUzIn0.eyJhdWQiOiIyIiwianRpIjoiNWJiMmViMjZkOGE2YTBmMDllMTlmZDJlOTVmYTE1YjFkOGNmZDBhZGFmNjFhMmNkNGUxMDg2Zjc4ZmNlYmM3OWFjM2U5NGFiZDViZDM4NTMiLCJpYXQiOjE1MTc4MTg5NjksIm5iZiI6MTUxNzgxODk2OSwiZXhwIjoxNTQ5MzU0OTY5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.IrunbaEHmpxGwJTnJ9tUuibBjYNfgWimN2hxHSoFS33r-bcVb8MMIVlYY72vbsfhxMKnEf8k1ds0IJ65uCg8IFO-sEsqA_bpomY1IhLradgoX7TwBKv_iqYLLQ3zMMyjaiUYEHcrFTIJxn4A80YTjtXfekGquDVUczFoVVhUMumhVWaJ23bZuqD2ujZDwg2CyZy3ABlg9VT30qmwVxOY_ThfVCIll69onrZyLVVzNC_rvJPTzzD0Hb827VnMBLRN6vv7cBme9wasBJzq6ab4Ys9IFn4j7JtVRoWHf_wVxgjeDPo2clggWt_KqAP2rU2ORBrQCYXk0TKwhzRtck2aczcOZcJLBnmOSaj3-1zw8gGXwNyLi-8a4h4A6aQzXZQpQTs9BNt-cnaP6LVr1Et-yMtjMpEMpzPcgt11vTxjFKVbWdbmV41445T9EtaaOLUMTM3m0STfsNJTOvl-bOtIoYNuTmXD5uNn69b6HcpiIdsqFwljffc_uPGvzXg9ddko286YVAVP5dSX7BC8WrX21sktrMreln7sbHQI4dzfs2y2k07nknlCO1AaXJjaqpFn4w3kfKSOcPhZ7ngf4WfZ8qGYXRRNbvs_Xs1HvPkIe2XxZ2u1L8b9mAWbEt5cC3YcKGPd4cVTK3qXFZLgc5yD-AZiFXSIiqjDZ3nfdSW0Hyw");
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void getLocation() {
        gps = new GPSTracker(this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            in_lat = gps.getLatitude();
            in_long = gps.getLongitude();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(in_lat, in_long, 1);
                Address obj = addresses.get(0);
                in_location = obj.getAddressLine(0);
                Log.d("location",in_location);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + in_lat + "\nLong: " + in_long, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    private void getLocationOut() {
        gps = new GPSTracker(this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            out_lat = gps.getLatitude();
            out_long = gps.getLongitude();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(out_lat, out_long, 1);
                Address obj = addresses.get(0);
                out_location = obj.getAddressLine(0);
                Log.d("location",out_location);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + out_lat + "\nLong: " + out_long, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
    private void checkIn() {

        getLocation();
        String t;
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("Asia/Dhaka"));

        int hour12 = cal.get(Calendar.HOUR); // 0..11
        int minutes = cal.get(Calendar.MINUTE); // 0..59
        boolean am = cal.get(Calendar.AM_PM) == Calendar.AM;
        if(am){
            timezone = "AM";
            time = hour12+":"+minutes+""+timezone;
            checkIn.setBackgroundColor(getResources().getColor(R.color.green));
            checkIn.setTextColor(getResources().getColor(R.color.white));
            checkIn.setText(time);
            submitBtn.setText("CHECK OUT");

            checkIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(  AttendanceActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("CheckIn Location...");

                    // Setting Dialog Message
                    alertDialog.setMessage(in_location);

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.map);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {

                            // Write your code here to invoke YES event
                            // Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            // dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
            });

            //Toast.makeText(gps, ""+t, Toast.LENGTH_SHORT).show();
            Log.d("timezone",hour12+":"+minutes+""+timezone);
        }
        else{
            timezone = "PM";
            time = hour12+":"+minutes+""+timezone;
            checkIn.setBackgroundColor(getResources().getColor(R.color.red));
            checkIn.setTextColor(getResources().getColor(R.color.white));
            checkIn.setText(time);
            submitBtn.setText("CHECK OUT");

            checkIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(  AttendanceActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("CheckIn Location...");

                    // Setting Dialog Message
                    alertDialog.setMessage(in_location);

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.map);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {

                            // Write your code here to invoke YES event
                            // Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            // dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
            });

            Log.d("timezone",hour12+":"+minutes+""+timezone);
        }



        RequestQueue queue = Volley.newRequestQueue(AttendanceActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        String url = "http://i-access.ingtechbd.com/api/access-logs/add";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsevalue", response);
                        //Toast.makeText(AttendanceActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        // Display the response string.
                        //_response.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //_response.setText("That didn't work!");
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("in_lat", String.valueOf(in_lat));
                params.put("in_long", String.valueOf(in_long));
                params.put("remarks", remarks);
                params.put("in_location",in_location);

                Log.d("paramsforattendance",params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String,String>();
                headers.put("Accept","application/json");
                headers.put("Authorization", Authorization);
                //headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjViYjJlYjI2ZDhhNmEwZjA5ZTE5ZmQyZTk1ZmExNWIxZDhjZmQwYWRhZjYxYTJjZDRlMTA4NmY3OGZjZWJjNzlhYzNlOTRhYmQ1YmQzODUzIn0.eyJhdWQiOiIyIiwianRpIjoiNWJiMmViMjZkOGE2YTBmMDllMTlmZDJlOTVmYTE1YjFkOGNmZDBhZGFmNjFhMmNkNGUxMDg2Zjc4ZmNlYmM3OWFjM2U5NGFiZDViZDM4NTMiLCJpYXQiOjE1MTc4MTg5NjksIm5iZiI6MTUxNzgxODk2OSwiZXhwIjoxNTQ5MzU0OTY5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.IrunbaEHmpxGwJTnJ9tUuibBjYNfgWimN2hxHSoFS33r-bcVb8MMIVlYY72vbsfhxMKnEf8k1ds0IJ65uCg8IFO-sEsqA_bpomY1IhLradgoX7TwBKv_iqYLLQ3zMMyjaiUYEHcrFTIJxn4A80YTjtXfekGquDVUczFoVVhUMumhVWaJ23bZuqD2ujZDwg2CyZy3ABlg9VT30qmwVxOY_ThfVCIll69onrZyLVVzNC_rvJPTzzD0Hb827VnMBLRN6vv7cBme9wasBJzq6ab4Ys9IFn4j7JtVRoWHf_wVxgjeDPo2clggWt_KqAP2rU2ORBrQCYXk0TKwhzRtck2aczcOZcJLBnmOSaj3-1zw8gGXwNyLi-8a4h4A6aQzXZQpQTs9BNt-cnaP6LVr1Et-yMtjMpEMpzPcgt11vTxjFKVbWdbmV41445T9EtaaOLUMTM3m0STfsNJTOvl-bOtIoYNuTmXD5uNn69b6HcpiIdsqFwljffc_uPGvzXg9ddko286YVAVP5dSX7BC8WrX21sktrMreln7sbHQI4dzfs2y2k07nknlCO1AaXJjaqpFn4w3kfKSOcPhZ7ngf4WfZ8qGYXRRNbvs_Xs1HvPkIe2XxZ2u1L8b9mAWbEt5cC3YcKGPd4cVTK3qXFZLgc5yD-AZiFXSIiqjDZ3nfdSW0Hyw");
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        //Toast.makeText(AttendanceActivity.this, "latitude"+latitude+"longitude"+longitude+"location"+location, Toast.LENGTH_SHORT).show();
    }


    private void checkOut() {
        getLocationOut();
        String t;
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("Asia/Dhaka"));

        int hour12 = cal.get(Calendar.HOUR); // 0..11
        int minutes = cal.get(Calendar.MINUTE); // 0..59
        boolean am = cal.get(Calendar.AM_PM) == Calendar.AM;
        if(am){
            timezone = "AM";
            time = hour12+":"+minutes+""+timezone;
            checkOut.setBackgroundColor(getResources().getColor(R.color.green));
            checkOut.setTextColor(getResources().getColor(R.color.white));
            checkOut.setText(time);
            submitBtn.setText("CHECK IN");
            remarksEdt.setEnabled(false);
            submitBtn.setEnabled(false);

            checkOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(  AttendanceActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("CheckOut Location...");

                    // Setting Dialog Message
                    alertDialog.setMessage(out_location);

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.map);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {

                            // Write your code here to invoke YES event
                            // Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            // dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
            });


            //Toast.makeText(gps, ""+t, Toast.LENGTH_SHORT).show();
            Log.d("timezone",hour12+":"+minutes+""+timezone);
        }
        else{
            timezone = "PM";
            time = hour12+":"+minutes+""+timezone;
            checkOut.setBackgroundColor(getResources().getColor(R.color.red));
            checkOut.setTextColor(getResources().getColor(R.color.white));
            checkOut.setText(time);
            submitBtn.setText("CHECK IN");
            remarksEdt.setEnabled(false);
            submitBtn.setEnabled(false);

            checkOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(  AttendanceActivity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("CheckOut Location...");

                    // Setting Dialog Message
                    alertDialog.setMessage(out_location);

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.map);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {

                            // Write your code here to invoke YES event
                            // Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            // dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }
            });
            Log.d("timezone",hour12+":"+minutes+""+timezone);
        }



        RequestQueue queue = Volley.newRequestQueue(AttendanceActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        String url = "http://i-access.ingtechbd.com/api/access-logs/add";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsevalue", response);
                        //Toast.makeText(AttendanceActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        // Display the response string.
                        //_response.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //_response.setText("That didn't work!");
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("out_lat", String.valueOf(out_lat));
                params.put("out_long", String.valueOf(out_long));
                params.put("remarks", remarks);
                params.put("out_location",out_location);

                Log.d("paramsforattendance",params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String,String>();
                headers.put("Accept","application/json");
                headers.put("Authorization", Authorization);
                //headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjViYjJlYjI2ZDhhNmEwZjA5ZTE5ZmQyZTk1ZmExNWIxZDhjZmQwYWRhZjYxYTJjZDRlMTA4NmY3OGZjZWJjNzlhYzNlOTRhYmQ1YmQzODUzIn0.eyJhdWQiOiIyIiwianRpIjoiNWJiMmViMjZkOGE2YTBmMDllMTlmZDJlOTVmYTE1YjFkOGNmZDBhZGFmNjFhMmNkNGUxMDg2Zjc4ZmNlYmM3OWFjM2U5NGFiZDViZDM4NTMiLCJpYXQiOjE1MTc4MTg5NjksIm5iZiI6MTUxNzgxODk2OSwiZXhwIjoxNTQ5MzU0OTY5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.IrunbaEHmpxGwJTnJ9tUuibBjYNfgWimN2hxHSoFS33r-bcVb8MMIVlYY72vbsfhxMKnEf8k1ds0IJ65uCg8IFO-sEsqA_bpomY1IhLradgoX7TwBKv_iqYLLQ3zMMyjaiUYEHcrFTIJxn4A80YTjtXfekGquDVUczFoVVhUMumhVWaJ23bZuqD2ujZDwg2CyZy3ABlg9VT30qmwVxOY_ThfVCIll69onrZyLVVzNC_rvJPTzzD0Hb827VnMBLRN6vv7cBme9wasBJzq6ab4Ys9IFn4j7JtVRoWHf_wVxgjeDPo2clggWt_KqAP2rU2ORBrQCYXk0TKwhzRtck2aczcOZcJLBnmOSaj3-1zw8gGXwNyLi-8a4h4A6aQzXZQpQTs9BNt-cnaP6LVr1Et-yMtjMpEMpzPcgt11vTxjFKVbWdbmV41445T9EtaaOLUMTM3m0STfsNJTOvl-bOtIoYNuTmXD5uNn69b6HcpiIdsqFwljffc_uPGvzXg9ddko286YVAVP5dSX7BC8WrX21sktrMreln7sbHQI4dzfs2y2k07nknlCO1AaXJjaqpFn4w3kfKSOcPhZ7ngf4WfZ8qGYXRRNbvs_Xs1HvPkIe2XxZ2u1L8b9mAWbEt5cC3YcKGPd4cVTK3qXFZLgc5yD-AZiFXSIiqjDZ3nfdSW0Hyw");
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null)
        {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            Intent intent = new Intent(AttendanceActivity.this,DashboardActivity.class);
            intent.putExtra("userrole",roleval);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(AttendanceActivity.this,AttendanceHistoryActivity.class);
            intent.putExtra("userrole",roleval);
            startActivity(intent);
        } else if (id == R.id.nav_supporthistory) {
            Intent intent = new Intent(AttendanceActivity.this,SupportHistory.class);
            intent.putExtra("userrole",roleval);
            startActivity(intent);
        } else if (id == R.id.nav_salarystatement) {

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(AttendanceActivity.this,ProfileActivity.class);
            intent.putExtra("userrole",roleval);
            startActivity(intent);
        } else if (id == R.id.nav_employees) {
            Intent intent = new Intent(AttendanceActivity.this,EmployeeListActivity.class);
            intent.putExtra("userrole",roleval);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;
        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            String lat = String.valueOf(location.getLatitude());
            String lng = String.valueOf(location.getLongitude());
            String accu = String.valueOf(location.getAccuracy());
            Log.e(TAG, lat);
            Log.e(TAG, lng);
            Log.e(TAG,accu);
            mLastLocation.set(location);
            Toast.makeText(getApplicationContext(), lat + " " + lng, Toast.LENGTH_LONG).show();


        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners =
            new LocationListener[]{new LocationListener(LocationManager.GPS_PROVIDER),
                    new LocationListener(LocationManager.NETWORK_PROVIDER)};
}


