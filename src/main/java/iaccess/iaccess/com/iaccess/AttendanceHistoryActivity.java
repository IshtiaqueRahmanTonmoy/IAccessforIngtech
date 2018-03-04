package iaccess.iaccess.com.iaccess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iaccess.iaccess.com.entity.DateRangePickerFragment;
import iaccess.iaccess.com.entity.Employee;
import iaccess.iaccess.com.entity.SimpleDividerItemDecoration;
import iaccess.iaccess.iaccess.com.adapter.EmployeeAdapter;
import iaccess.iaccess.iaccess.com.adapter.RecyclerItemClickListener;


public class AttendanceHistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,DateRangePickerFragment.OnDateRangeSelectedListener {

    private static final String GETALL_URL = "http://i-access.ingtechbd.com/api/access-logs/";
    private StringRequest stringRequest;
    Toolbar toolbar;
    private Spinner spinner;
    private Button button;
    private TextView txtview;
    private RecyclerView recyclerView;
    private String id,image,name,timestamp,timestampsout,hour,in_location,out_location,hourout,month,day,fromto,ids,roleval,access_token,Authorization;
    Timestamp timestamps = new Timestamp(System.currentTimeMillis());
    private String inlat,inlong,outlat,outlong;
    String intime,outtime;
    private EmployeeAdapter mAdapter;
    private ProgressDialog progressDialog;
    private List<Employee> empList = new ArrayList<Employee>();
    TextView nameText,designationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_history);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Attendance History");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        roleval = getIntent().getStringExtra("userrole");
        access_token = getIntent().getStringExtra("access_token");

        //Toast.makeText(this, ""+roleval, Toast.LENGTH_SHORT).show();

        Authorization = "Bearer"+" "+access_token;

        Log.d("Authorization",Authorization);
        //Toast.makeText(this, ""+Authorization, Toast.LENGTH_SHORT).show();
        spinner = (Spinner) findViewById(R.id.Spinner);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        String[] list = new String[]{
                "Search by id",
                "Search by date"
        };


        final List<String> plantsList = new ArrayList<>(Arrays.asList(list));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,plantsList){

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint

                      Log.d("string",selectedItemText);
                      if(selectedItemText.equals("Search by id")){
                          empList.clear();
                          AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AttendanceHistoryActivity.this);
                          LayoutInflater inflater = AttendanceHistoryActivity.this.getLayoutInflater();
                          final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                          dialogBuilder.setView(dialogView);

                          final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

                          dialogBuilder.setTitle("Custom dialog");
                          dialogBuilder.setMessage("Enter id below");
                          dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int whichButton) {
                                  //do something with edt.getText().toString();
                                  String user_id = edt.getText().toString();
                                  showvaluebyid(user_id);
                                  
                              }
                          });
                          dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int whichButton) {
                                  //pass
                              }
                          });
                          AlertDialog b = dialogBuilder.create();
                          b.show();
                      }
                      else{
                          empList.clear();
                          DateRangePickerFragment dateRangePickerFragment= DateRangePickerFragment.newInstance(AttendanceHistoryActivity.this,false);
                          dateRangePickerFragment.show(getSupportFragmentManager(),"datePicker");
                      }
                    // Notify the selected item text
                    Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //getValue();

        mAdapter = new EmployeeAdapter(getApplicationContext(),empList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this, LinearLayout.VERTICAL,16));

// set the adapter
        //recyclerView.setAdapter(mAdapter);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(AttendanceHistoryActivity.this, new   RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        // TODO Handle item click

                        txtview = (TextView) v.findViewById(R.id.belowTextsf);
                        button = (Button) v.findViewById(R.id.view);

                        ids = txtview.getText().toString();
                        //Intent intent = new Intent(AttendanceHistoryActivity.this,AttendanceDetails.class);
                        //intent.putExtra("id",ids);
                        //startActivity(intent);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(AttendanceHistoryActivity.this,AttendanceDetails.class);
                                intent.putExtra("id",ids);
                                intent.putExtra("userrole",roleval);
                                intent.putExtra("access_token",access_token);
                                startActivity(intent);
                                finish();
                            }
                        });

                        //Toast.makeText(AttendanceHistoryActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                    }
                })
        );

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
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AttendanceHistoryActivity.this,DashboardActivity.class);
        intent.putExtra("userrole",roleval);
        intent.putExtra("acces_token",access_token);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menusettings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent intent = new Intent(AttendanceHistoryActivity.this,DashboardActivity.class);
            intent.putExtra("userrole",roleval);
            intent.putExtra("acces_token",access_token);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }


    private void showvaluebyid(String user_id) {

        //Toast.makeText(this, ""+Authorization, Toast.LENGTH_SHORT).show();
        RequestQueue queue = Volley.newRequestQueue(AttendanceHistoryActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, "http://i-access.ingtechbd.com/api/access-logs?user_id="+user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsevaluebyid", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray j = jsonObject.getJSONArray("data");

                            if(j.length() > 0) {
                                for (int i = 0; i < j.length(); i++) {
                                    try {
                                        //Getting json object
                                        JSONObject json = j.getJSONObject(i);
                                        id = json.getString("id");

                                        inlat = json.getString("in_lat");
                                        inlong = json.getString("in_long");
                                        outlat = json.getString("out_lat");
                                        outlong = json.getString("out_long");

                                        //Toast.makeText(AttendanceHistoryActivity.this, ""+id, Toast.LENGTH_SHORT).show();
                                        in_location = json.getString("in_location");
                                        JSONObject jsonob = json.getJSONObject("user");
                                        name = jsonob.getString("name");
                                        image = jsonob.getString("avatar");

                                        //this is start of intime
                                        JSONObject jsonin = json.getJSONObject("in_time");
                                        timestamp = jsonin.getString("date");
                                        String[] parts = timestamp.split("-");

                                        String year = parts[0];
                                        String mth = parts[1];
                                        String d = parts[2];

                                        String[] da = d.split(" ");
                                        String v = da[0];
                                        Log.d("va", v);
                                        //int m = Integer.parseInt(mth);

                                        if (mth.equals("01")) {
                                            month = "January";
                                        } else if (mth.equals("02")) {
                                            month = "February";
                                        } else if (mth.equals("03")) {
                                            month = "March";
                                        } else if (mth.equals("04")) {
                                            month = "April";
                                        } else if (mth.equals("05")) {
                                            month = "May";
                                        } else if (mth.equals("06")) {
                                            month = "June";
                                        } else if (mth.equals("07")) {
                                            month = "July";
                                        } else if (mth.equals("08")) {
                                            month = "August";
                                        } else if (mth.equals("09")) {
                                            month = "September";
                                        } else if (mth.equals("10")) {
                                            month = "October";

                                        } else if (mth.equals("11")) {
                                            month = "November";

                                        } else {
                                            month = "December";
                                        }

                                        day = parts[2];

                                        String[] partss = timestamp.split(" ");
                                        String time = partss[1];

                                        String[] timeparts = time.split(":");

                                        String h = timeparts[0];
                                        int hr = Integer.parseInt(h);

                                        if (hr == 1) {
                                            hour = "1";
                                        } else if (hr == 2) {
                                            hour = "2";
                                        } else if (hr == 3) {
                                            hour = "3";
                                        } else if (hr == 4) {
                                            hour = "4";
                                        } else if (hr == 5) {
                                            hour = "5";
                                        } else if (hr == 6) {
                                            hour = "1";
                                        } else if (hr == 7) {
                                            hour = "7";
                                        } else if (hr == 8) {
                                            hour = "8";
                                        } else if (hr == 9) {
                                            hour = "9";
                                        } else if (hr == 10) {
                                            hour = "10";
                                        } else if (hr == 11) {
                                            hour = "11";
                                        } else if (hr == 12) {
                                            hour = "12";
                                        } else if (hr == 13) {
                                            hour = "1";
                                        } else if (hr == 14) {
                                            hour = "2";
                                        } else if (hr == 15) {
                                            hour = "3";
                                        } else if (hr == 16) {
                                            hour = "4";
                                        } else if (hr == 17) {
                                            hour = "5";
                                        } else if (hr == 18) {
                                            hour = "6";
                                        } else if (hr == 19) {
                                            hour = "7";
                                        } else if (hr == 20) {
                                            hour = "8";
                                        } else if (hr == 21) {
                                            hour = "9";
                                        } else if (hr == 22) {
                                            hour = "10";
                                        } else if (hr == 23) {
                                            hour = "11";
                                        } else {
                                            hour = "12";
                                        }


                                        String minute = timeparts[1];

                                        if (hr <= 12) {
                                            intime = hour + ":" + minute + "AM";
                                        } else {
                                            intime = hour + ":" + minute + "PM";
                                        }


                                        Log.d("intime", intime);

                                        //this is end of intime
                                        //this is begininig of out time
                                        JSONObject jsonout = json.getJSONObject("out_time");

                                        Log.d("jsonout", jsonout.toString());
                                        if (jsonout.isNull("null")) {
                                            timestampsout = jsonout.getString("date");
                                            String[] parts1 = timestampsout.split("-");

                                            String years = parts1[0];
                                            String months = parts1[1];
                                            String days = parts1[2];

                                            String[] partsss = timestampsout.split(" ");
                                            String times = partsss[1];

                                            Log.d("times", times);

                                            String[] timepartss = times.split(":");

                                            String hs = timepartss[0];
                                            int hrs = Integer.parseInt(hs);

                                            Log.d("hrs", String.valueOf(hrs));

                                            if (hrs == 1) {
                                                hourout = "1";
                                            } else if (hrs == 2) {
                                                hourout = "2";
                                            } else if (hrs == 3) {
                                                hourout = "3";
                                            } else if (hrs == 4) {
                                                hourout = "4";
                                            } else if (hrs == 5) {
                                                hourout = "5";
                                            } else if (hrs == 6) {
                                                hourout = "1";
                                            } else if (hrs == 7) {
                                                hourout = "7";
                                            } else if (hrs == 8) {
                                                hourout = "8";
                                            } else if (hrs == 9) {
                                                hourout = "9";
                                            } else if (hrs == 10) {
                                                hourout = "10";
                                            } else if (hrs == 11) {
                                                hourout = "11";
                                            } else if (hrs == 12) {
                                                hourout = "12";
                                            } else if (hrs == 13) {
                                                hourout = "1";
                                            } else if (hrs == 14) {
                                                hourout = "2";
                                            } else if (hrs == 15) {
                                                hourout = "3";
                                            } else if (hrs == 16) {
                                                hourout = "4";
                                            } else if (hrs == 17) {
                                                hourout = "5";
                                            } else if (hrs == 18) {
                                                hourout = "6";
                                            } else if (hrs == 19) {
                                                hourout = "7";
                                            } else if (hrs == 20) {
                                                hourout = "8";
                                            } else if (hrs == 21) {
                                                hourout = "9";
                                            } else if (hrs == 22) {
                                                hourout = "10";
                                            } else if (hrs == 23) {
                                                hourout = "11";
                                            } else {
                                                hourout = "12";
                                            }

                                            String minutes = timepartss[1];


                                            if (hrs <= 12) {
                                                outtime = hourout + ":" + minutes + "AM";
                                            } else {
                                                outtime = hourout + ":" + minutes + "PM";
                                            }
                                        } else {
                                            outtime = null;
                                        }
                                        Log.d("stringoutime", outtime);

                                        //this is end of out time
                                        fromto = intime + "-" + outtime;

                                        Log.d("fromto", fromto);

                                        Log.d("allv", id + "name" + name + "image" + image + "month" + month + "day" + v + "fromto" + fromto + "inlocation" + in_location);

                                        empList.add(new Employee(id, image, name, month, v, fromto, in_location));
                                        mAdapter.notifyDataSetChanged();


                                        //Toast.makeText(AttendanceHistoryActivity.this, ""+time, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        //Toast.makeText(AllTransactionsActivity.this, "category"+category, Toast.LENGTH_SHORT).show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(AttendanceHistoryActivity.this, "Data not found..", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                headers.put("Authorization",Authorization);
                //headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjViYjJlYjI2ZDhhNmEwZjA5ZTE5ZmQyZTk1ZmExNWIxZDhjZmQwYWRhZjYxYTJjZDRlMTA4NmY3OGZjZWJjNzlhYzNlOTRhYmQ1YmQzODUzIn0.eyJhdWQiOiIyIiwianRpIjoiNWJiMmViMjZkOGE2YTBmMDllMTlmZDJlOTVmYTE1YjFkOGNmZDBhZGFmNjFhMmNkNGUxMDg2Zjc4ZmNlYmM3OWFjM2U5NGFiZDViZDM4NTMiLCJpYXQiOjE1MTc4MTg5NjksIm5iZiI6MTUxNzgxODk2OSwiZXhwIjoxNTQ5MzU0OTY5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.IrunbaEHmpxGwJTnJ9tUuibBjYNfgWimN2hxHSoFS33r-bcVb8MMIVlYY72vbsfhxMKnEf8k1ds0IJ65uCg8IFO-sEsqA_bpomY1IhLradgoX7TwBKv_iqYLLQ3zMMyjaiUYEHcrFTIJxn4A80YTjtXfekGquDVUczFoVVhUMumhVWaJ23bZuqD2ujZDwg2CyZy3ABlg9VT30qmwVxOY_ThfVCIll69onrZyLVVzNC_rvJPTzzD0Hb827VnMBLRN6vv7cBme9wasBJzq6ab4Ys9IFn4j7JtVRoWHf_wVxgjeDPo2clggWt_KqAP2rU2ORBrQCYXk0TKwhzRtck2aczcOZcJLBnmOSaj3-1zw8gGXwNyLi-8a4h4A6aQzXZQpQTs9BNt-cnaP6LVr1Et-yMtjMpEMpzPcgt11vTxjFKVbWdbmV41445T9EtaaOLUMTM3m0STfsNJTOvl-bOtIoYNuTmXD5uNn69b6HcpiIdsqFwljffc_uPGvzXg9ddko286YVAVP5dSX7BC8WrX21sktrMreln7sbHQI4dzfs2y2k07nknlCO1AaXJjaqpFn4w3kfKSOcPhZ7ngf4WfZ8qGYXRRNbvs_Xs1HvPkIe2XxZ2u1L8b9mAWbEt5cC3YcKGPd4cVTK3qXFZLgc5yD-AZiFXSIiqjDZ3nfdSW0Hyw");
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        progressDialog = new ProgressDialog(AttendanceHistoryActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();

    }

    private void getValue() {
        RequestQueue queue = Volley.newRequestQueue(AttendanceHistoryActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, GETALL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsevalue", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray j = jsonObject.getJSONArray("data");
                            for(int i=0;i<j.length();i++){
                                try {
                                    //Getting json object
                                    JSONObject json = j.getJSONObject(i);
                                    id = json.getString("id");
                                    in_location = json.getString("in_location");
                                    JSONObject jsonob = json.getJSONObject("user");
                                    name = jsonob.getString("name");

                                 //this is start of intime
                                    JSONObject jsonin = json.getJSONObject("in_time");
                                    timestamp = jsonin.getString("date");
                                    String[] parts = timestamp.split("-");

                                    String year = parts[0];
                                    String mth = parts[1];
                                    String d = parts[2];

                                    String[] da = d.split(" ");
                                    String v = da[0];
                                    Log.d("va",v);
                                    //int m = Integer.parseInt(mth);

                                    if(mth.equals("01")){
                                        month = "January";
                                    }
                                    else if(mth.equals("02")){
                                        month = "February";
                                    }
                                    else if(mth.equals("03")){
                                        month = "March";
                                    }
                                    else if(mth.equals("04")){
                                        month = "April";
                                    }
                                    else if(mth.equals("05")){
                                        month = "May";
                                    }
                                    else if(mth.equals("06")){
                                        month = "June";
                                    }
                                    else if(mth.equals("07")){
                                        month = "July";
                                    }
                                    else if(mth.equals("08")){
                                        month = "August";
                                    }
                                    else if(mth.equals("09")){
                                        month = "September";
                                    }
                                    else if(mth.equals("10")){
                                        month = "October";

                                    }
                                    else if(mth.equals("11")){
                                        month = "November";

                                    }
                                    else{
                                       month = "December";
                                    }

                                    day = parts[2];

                                    String[] partss = timestamp.split(" ");
                                    String time = partss[1];

                                    String[] timeparts = time.split(":");

                                    String h = timeparts[0];
                                    int hr = Integer.parseInt(h);

                                    if (hr == 1) {
                                        hour = "1";
                                    }
                                    else if (hr == 2) {
                                        hour = "2";
                                    }
                                    else if (hr == 3) {
                                        hour = "3";
                                    }
                                    else if (hr == 4) {
                                        hour = "4";
                                    }
                                    else if (hr == 5) {
                                        hour = "5";
                                    }
                                    else if (hr == 6) {
                                        hour = "1";
                                    }
                                    else if (hr == 7) {
                                        hour = "7";
                                    }
                                    else if (hr == 8) {
                                        hour = "8";
                                    }
                                    else if (hr == 9) {
                                        hour = "9";
                                    }
                                    else if (hr == 10) {
                                        hour = "10";
                                    }
                                    else if (hr == 11) {
                                        hour = "11";
                                    }
                                    else if (hr == 12) {
                                        hour = "12";
                                    }
                                    else if (hr == 13) {
                                        hour = "1";
                                    } else if (hr == 14) {
                                        hour = "2";
                                    } else if (hr == 15) {
                                        hour = "3";
                                    } else if (hr == 16) {
                                        hour = "4";
                                    } else if (hr == 17) {
                                        hour = "5";
                                    } else if (hr == 18) {
                                        hour = "6";
                                    } else if (hr == 19) {
                                        hour = "7";
                                    } else if (hr == 20) {
                                        hour = "8";
                                    } else if (hr == 21) {
                                        hour = "9";
                                    } else if (hr == 22) {
                                        hour = "10";
                                    } else if (hr == 23) {
                                        hour = "11";
                                    } else {
                                        hour = "12";
                                    }


                                    String minute = timeparts[1];

                                    if(hr<=12){
                                        intime = hour+":"+minute+"AM";
                                    }
                                    else{
                                        intime = hour+":"+minute+"PM";
                                    }


                                    //Log.d("day",pd);

                                   //this is end of intime





                                    //this is begininig of out time
                                    JSONObject jsonout = json.getJSONObject("out_time");

                                    Log.d("jsonout",jsonout.toString());
                                    if(jsonout.isNull("null")) {
                                      timestampsout = jsonout.getString("date");
                                      String[] parts1 = timestampsout.split("-");

                                      String years = parts1[0];
                                      String months = parts1[1];
                                      String days = parts1[2];

                                      String[] partsss = timestampsout.split(" ");
                                      String times = partsss[1];

                                      Log.d("times", times);

                                      String[] timepartss = times.split(":");

                                      String hs = timepartss[0];
                                      int hrs = Integer.parseInt(hs);

                                      Log.d("hrs", String.valueOf(hrs));

                                         if (hrs == 1) {
                                            hourout = "1";
                                        }
                                         else if (hrs == 2) {
                                            hourout = "2";
                                        }
                                        else if (hrs == 3) {
                                            hourout = "3";
                                        }
                                         else if (hrs == 4) {
                                            hourout = "4";
                                        }
                                        else if (hrs == 5) {
                                            hourout = "5";
                                        }
                                          else if (hrs == 6) {
                                            hourout = "1";
                                        }
                                         else if (hrs == 7) {
                                            hourout = "7";
                                        }
                                        else if (hrs == 8) {
                                            hourout = "8";
                                        }
                                        else if (hrs == 9) {
                                            hourout = "9";
                                        }
                                        else if (hrs == 10) {
                                            hourout = "10";
                                        }
                                        else if (hrs == 11) {
                                            hourout = "11";
                                        }
                                        else if (hrs == 12) {
                                            hourout = "12";
                                        }
                                      else if (hrs == 13) {
                                          hourout = "1";
                                      } else if (hrs == 14) {
                                          hourout = "2";
                                      } else if (hrs == 15) {
                                          hourout = "3";
                                      } else if (hrs == 16) {
                                          hourout = "4";
                                      } else if (hrs == 17) {
                                          hourout = "5";
                                      } else if (hrs == 18) {
                                          hourout = "6";
                                      } else if (hrs == 19) {
                                          hourout = "7";
                                      } else if (hrs == 20) {
                                          hourout = "8";
                                      } else if (hrs == 21) {
                                          hourout = "9";
                                      } else if (hrs == 22) {
                                          hourout = "10";
                                      } else if (hrs == 23) {
                                          hourout = "11";
                                      } else {
                                          hourout = "12";
                                      }

                                      String minutes = timepartss[1];


                                      if (hrs <= 12) {
                                          outtime = hourout + ":" + minutes + "AM";
                                      } else {
                                          outtime = hourout + ":" + minutes + "PM";
                                      }
                                  }

                                  else{
                                      outtime = null;
                                  }
                                    Log.d("stringoutime",outtime);

                                  //this is end of out time
                                    fromto = intime+"-"+outtime;

                                    empList.add(new Employee(id,image,name,month,v,fromto,in_location));
                                    mAdapter.notifyDataSetChanged();



                                    //Toast.makeText(AttendanceHistoryActivity.this, ""+time, Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    //Toast.makeText(AllTransactionsActivity.this, "category"+category, Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                //headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjViYjJlYjI2ZDhhNmEwZjA5ZTE5ZmQyZTk1ZmExNWIxZDhjZmQwYWRhZjYxYTJjZDRlMTA4NmY3OGZjZWJjNzlhYzNlOTRhYmQ1YmQzODUzIn0.eyJhdWQiOiIyIiwianRpIjoiNWJiMmViMjZkOGE2YTBmMDllMTlmZDJlOTVmYTE1YjFkOGNmZDBhZGFmNjFhMmNkNGUxMDg2Zjc4ZmNlYmM3OWFjM2U5NGFiZDViZDM4NTMiLCJpYXQiOjE1MTc4MTg5NjksIm5iZiI6MTUxNzgxODk2OSwiZXhwIjoxNTQ5MzU0OTY5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.IrunbaEHmpxGwJTnJ9tUuibBjYNfgWimN2hxHSoFS33r-bcVb8MMIVlYY72vbsfhxMKnEf8k1ds0IJ65uCg8IFO-sEsqA_bpomY1IhLradgoX7TwBKv_iqYLLQ3zMMyjaiUYEHcrFTIJxn4A80YTjtXfekGquDVUczFoVVhUMumhVWaJ23bZuqD2ujZDwg2CyZy3ABlg9VT30qmwVxOY_ThfVCIll69onrZyLVVzNC_rvJPTzzD0Hb827VnMBLRN6vv7cBme9wasBJzq6ab4Ys9IFn4j7JtVRoWHf_wVxgjeDPo2clggWt_KqAP2rU2ORBrQCYXk0TKwhzRtck2aczcOZcJLBnmOSaj3-1zw8gGXwNyLi-8a4h4A6aQzXZQpQTs9BNt-cnaP6LVr1Et-yMtjMpEMpzPcgt11vTxjFKVbWdbmV41445T9EtaaOLUMTM3m0STfsNJTOvl-bOtIoYNuTmXD5uNn69b6HcpiIdsqFwljffc_uPGvzXg9ddko286YVAVP5dSX7BC8WrX21sktrMreln7sbHQI4dzfs2y2k07nknlCO1AaXJjaqpFn4w3kfKSOcPhZ7ngf4WfZ8qGYXRRNbvs_Xs1HvPkIe2XxZ2u1L8b9mAWbEt5cC3YcKGPd4cVTK3qXFZLgc5yD-AZiFXSIiqjDZ3nfdSW0Hyw");
                headers.put("Authorization",Authorization);
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        progressDialog = new ProgressDialog(AttendanceHistoryActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            Intent intent = new Intent(AttendanceHistoryActivity.this,DashboardActivity.class);
            intent.putExtra("userrole",roleval);
            intent.putExtra("access_token",access_token);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(AttendanceHistoryActivity.this,AttendanceHistoryActivity.class);
            intent.putExtra("userrole",roleval);
            intent.putExtra("access_token",access_token);
            startActivity(intent);
        } else if (id == R.id.nav_supporthistory) {
            Intent intent = new Intent(AttendanceHistoryActivity.this,SupportHistory.class);
            intent.putExtra("userrole",roleval);
            intent.putExtra("access_token",access_token);
            startActivity(intent);
        }  else if (id == R.id.nav_profile) {
            Intent intent = new Intent(AttendanceHistoryActivity.this,ProfileActivity.class);
            intent.putExtra("userrole",roleval);
            intent.putExtra("access_token",access_token);
            startActivity(intent);
        } else if (id == R.id.nav_employees) {
            Intent intent = new Intent(AttendanceHistoryActivity.this,EmployeeListActivity.class);
            intent.putExtra("userrole",roleval);
            intent.putExtra("access_token",access_token);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {

        String smonth=null,mmonth=null;
        if(startMonth<10){
            smonth = "0"+startMonth;
            Log.d("datepicker", String.valueOf(month));
            //String start = ""+startYear+"-"+month+"-"+startDay;
            //String end = ""+endYear+"-"+endMonth+"-"+endDay;
        }

        if(endMonth<10){
            mmonth = "0"+endMonth;
        }

        String from = startYear+"-"+smonth+"-"+startDay+" "+"00:00:00";
        String to = endYear+"-"+mmonth+"-"+endDay+" "+"23:59:5";

        //getvalue();
        getValuebydate(from,to);
    }

    private void getValuebydate(String from,String to) {

        //Log.d("datepicker",from+""+to);
        //Toast.makeText(this, ""+from+""+to, Toast.LENGTH_SHORT).show();
        RequestQueue queue = Volley.newRequestQueue(AttendanceHistoryActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, "http://i-access.ingtechbd.com/api/access-logs?from="+from+"&&to="+to,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsevalue", response);
                        //Toast.makeText(AttendanceHistoryActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray j = jsonObject.getJSONArray("data");
                            if(j.length() > 0) {
                                for (int i = 0; i < j.length(); i++) {
                                    try {
                                        //Getting json object
                                        JSONObject json = j.getJSONObject(i);
                                        id = json.getString("id");
                                        in_location = json.getString("in_location");
                                        JSONObject jsonob = json.getJSONObject("user");
                                        name = jsonob.getString("name");
                                        image = jsonob.getString("avatar");

                                        //this is start of intime
                                        JSONObject jsonin = json.getJSONObject("in_time");
                                        timestamp = jsonin.getString("date");
                                        String[] parts = timestamp.split("-");

                                        String year = parts[0];
                                        String mth = parts[1];
                                        String d = parts[2];

                                        String[] da = d.split(" ");
                                        String v = da[0];
                                        Log.d("va", v);
                                        //int m = Integer.parseInt(mth);

                                        if (mth.equals("01")) {
                                            month = "January";
                                        } else if (mth.equals("02")) {
                                            month = "February";
                                        } else if (mth.equals("03")) {
                                            month = "March";
                                        } else if (mth.equals("04")) {
                                            month = "April";
                                        } else if (mth.equals("05")) {
                                            month = "May";
                                        } else if (mth.equals("06")) {
                                            month = "June";
                                        } else if (mth.equals("07")) {
                                            month = "July";
                                        } else if (mth.equals("08")) {
                                            month = "August";
                                        } else if (mth.equals("09")) {
                                            month = "September";
                                        } else if (mth.equals("10")) {
                                            month = "October";

                                        } else if (mth.equals("11")) {
                                            month = "November";

                                        } else {
                                            month = "December";
                                        }

                                        day = parts[2];

                                        String[] partss = timestamp.split(" ");
                                        String time = partss[1];

                                        String[] timeparts = time.split(":");

                                        String h = timeparts[0];
                                        int hr = Integer.parseInt(h);

                                        if (hr == 1) {
                                            hour = "1";
                                        } else if (hr == 2) {
                                            hour = "2";
                                        } else if (hr == 3) {
                                            hour = "3";
                                        } else if (hr == 4) {
                                            hour = "4";
                                        } else if (hr == 5) {
                                            hour = "5";
                                        } else if (hr == 6) {
                                            hour = "1";
                                        } else if (hr == 7) {
                                            hour = "7";
                                        } else if (hr == 8) {
                                            hour = "8";
                                        } else if (hr == 9) {
                                            hour = "9";
                                        } else if (hr == 10) {
                                            hour = "10";
                                        } else if (hr == 11) {
                                            hour = "11";
                                        } else if (hr == 12) {
                                            hour = "12";
                                        } else if (hr == 13) {
                                            hour = "1";
                                        } else if (hr == 14) {
                                            hour = "2";
                                        } else if (hr == 15) {
                                            hour = "3";
                                        } else if (hr == 16) {
                                            hour = "4";
                                        } else if (hr == 17) {
                                            hour = "5";
                                        } else if (hr == 18) {
                                            hour = "6";
                                        } else if (hr == 19) {
                                            hour = "7";
                                        } else if (hr == 20) {
                                            hour = "8";
                                        } else if (hr == 21) {
                                            hour = "9";
                                        } else if (hr == 22) {
                                            hour = "10";
                                        } else if (hr == 23) {
                                            hour = "11";
                                        } else {
                                            hour = "12";
                                        }


                                        String minute = timeparts[1];

                                        if (hr <= 12) {
                                            intime = hour + ":" + minute + "AM";
                                        } else {
                                            intime = hour + ":" + minute + "PM";
                                        }


                                        Log.d("intime", intime);

                                        //this is end of intime
                                        //this is begininig of out time
                                        JSONObject jsonout = json.getJSONObject("out_time");

                                        Log.d("jsonout", jsonout.toString());
                                        if (jsonout.isNull("null")) {
                                            timestampsout = jsonout.getString("date");
                                            String[] parts1 = timestampsout.split("-");

                                            String years = parts1[0];
                                            String months = parts1[1];
                                            String days = parts1[2];

                                            String[] partsss = timestampsout.split(" ");
                                            String times = partsss[1];

                                            Log.d("times", times);

                                            String[] timepartss = times.split(":");

                                            String hs = timepartss[0];
                                            int hrs = Integer.parseInt(hs);

                                            Log.d("hrs", String.valueOf(hrs));

                                            if (hrs == 1) {
                                                hourout = "1";
                                            } else if (hrs == 2) {
                                                hourout = "2";
                                            } else if (hrs == 3) {
                                                hourout = "3";
                                            } else if (hrs == 4) {
                                                hourout = "4";
                                            } else if (hrs == 5) {
                                                hourout = "5";
                                            } else if (hrs == 6) {
                                                hourout = "1";
                                            } else if (hrs == 7) {
                                                hourout = "7";
                                            } else if (hrs == 8) {
                                                hourout = "8";
                                            } else if (hrs == 9) {
                                                hourout = "9";
                                            } else if (hrs == 10) {
                                                hourout = "10";
                                            } else if (hrs == 11) {
                                                hourout = "11";
                                            } else if (hrs == 12) {
                                                hourout = "12";
                                            } else if (hrs == 13) {
                                                hourout = "1";
                                            } else if (hrs == 14) {
                                                hourout = "2";
                                            } else if (hrs == 15) {
                                                hourout = "3";
                                            } else if (hrs == 16) {
                                                hourout = "4";
                                            } else if (hrs == 17) {
                                                hourout = "5";
                                            } else if (hrs == 18) {
                                                hourout = "6";
                                            } else if (hrs == 19) {
                                                hourout = "7";
                                            } else if (hrs == 20) {
                                                hourout = "8";
                                            } else if (hrs == 21) {
                                                hourout = "9";
                                            } else if (hrs == 22) {
                                                hourout = "10";
                                            } else if (hrs == 23) {
                                                hourout = "11";
                                            } else {
                                                hourout = "12";
                                            }

                                            String minutes = timepartss[1];


                                            if (hrs <= 12) {
                                                outtime = hourout + ":" + minutes + "AM";
                                            } else {
                                                outtime = hourout + ":" + minutes + "PM";
                                            }
                                        } else {
                                            outtime = null;
                                        }
                                        Log.d("stringoutime", outtime);

                                        //this is end of out time
                                        fromto = intime + "-" + outtime;

                                        Log.d("fromto", fromto);

                                        empList.add(new Employee(id, image, name, month, v, fromto, in_location));
                                        mAdapter.notifyDataSetChanged();


                                        //Toast.makeText(AttendanceHistoryActivity.this, ""+time, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        //Toast.makeText(AllTransactionsActivity.this, "category"+category, Toast.LENGTH_SHORT).show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(AttendanceHistoryActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                //headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjViYjJlYjI2ZDhhNmEwZjA5ZTE5ZmQyZTk1ZmExNWIxZDhjZmQwYWRhZjYxYTJjZDRlMTA4NmY3OGZjZWJjNzlhYzNlOTRhYmQ1YmQzODUzIn0.eyJhdWQiOiIyIiwianRpIjoiNWJiMmViMjZkOGE2YTBmMDllMTlmZDJlOTVmYTE1YjFkOGNmZDBhZGFmNjFhMmNkNGUxMDg2Zjc4ZmNlYmM3OWFjM2U5NGFiZDViZDM4NTMiLCJpYXQiOjE1MTc4MTg5NjksIm5iZiI6MTUxNzgxODk2OSwiZXhwIjoxNTQ5MzU0OTY5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.IrunbaEHmpxGwJTnJ9tUuibBjYNfgWimN2hxHSoFS33r-bcVb8MMIVlYY72vbsfhxMKnEf8k1ds0IJ65uCg8IFO-sEsqA_bpomY1IhLradgoX7TwBKv_iqYLLQ3zMMyjaiUYEHcrFTIJxn4A80YTjtXfekGquDVUczFoVVhUMumhVWaJ23bZuqD2ujZDwg2CyZy3ABlg9VT30qmwVxOY_ThfVCIll69onrZyLVVzNC_rvJPTzzD0Hb827VnMBLRN6vv7cBme9wasBJzq6ab4Ys9IFn4j7JtVRoWHf_wVxgjeDPo2clggWt_KqAP2rU2ORBrQCYXk0TKwhzRtck2aczcOZcJLBnmOSaj3-1zw8gGXwNyLi-8a4h4A6aQzXZQpQTs9BNt-cnaP6LVr1Et-yMtjMpEMpzPcgt11vTxjFKVbWdbmV41445T9EtaaOLUMTM3m0STfsNJTOvl-bOtIoYNuTmXD5uNn69b6HcpiIdsqFwljffc_uPGvzXg9ddko286YVAVP5dSX7BC8WrX21sktrMreln7sbHQI4dzfs2y2k07nknlCO1AaXJjaqpFn4w3kfKSOcPhZ7ngf4WfZ8qGYXRRNbvs_Xs1HvPkIe2XxZ2u1L8b9mAWbEt5cC3YcKGPd4cVTK3qXFZLgc5yD-AZiFXSIiqjDZ3nfdSW0Hyw");
                headers.put("Authorization",Authorization);
                return headers;
            }
        };
        //
        Log.d("string", String.valueOf(stringRequest));
        queue.add(stringRequest);
        progressDialog = new ProgressDialog(AttendanceHistoryActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }
}
