package iaccess.iaccess.com.iaccess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iaccess.iaccess.com.entity.DateRangePickerFragment;
import iaccess.iaccess.com.entity.Employee;
import iaccess.iaccess.com.entity.Event;
import iaccess.iaccess.com.entity.SimpleDividerItemDecoration;
import iaccess.iaccess.iaccess.com.adapter.EmployeeAdapter;
import iaccess.iaccess.iaccess.com.adapter.EventAdapter;
import iaccess.iaccess.iaccess.com.adapter.RecyclerItemClickListener;

public class EventListActivity extends AppCompatActivity implements DateRangePickerFragment.OnDateRangeSelectedListener{

    Toolbar toolbar;
    private RecyclerView recyclerView;
    private Button button;
    private TextView txtview;
    private Spinner spinner;
    private StringRequest stringRequest;
    private String id,title,time,name,month,day,hour,ids,access_token,Authorization,roleval;
    private List<Event> eventList = new ArrayList<Event>();
    private ProgressDialog progressDialog;
    private EventAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        roleval = getIntent().getStringExtra("userrole");
        access_token = getIntent().getStringExtra("access_token");
        //Toast.makeText(AttendanceActivity.this, ""+access_token, Toast.LENGTH_SHORT).show();
        Authorization = "Bearer"+" "+access_token;
        //Toast.makeText(this, ""+Authorization, Toast.LENGTH_SHORT).show();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Event List");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        //getValue();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        spinner = (Spinner) findViewById(R.id.Spinner);
        String[] list = new String[]{
                "Search by date"
        };

        final List<String> List = new ArrayList<>(Arrays.asList(list));
        // Initializing an ArrayAdapter

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,List){

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

        mAdapter = new EventAdapter(eventList);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint

                Log.d("string",selectedItemText);
                if(selectedItemText.equals("Search by date")){
                    DateRangePickerFragment dateRangePickerFragment= DateRangePickerFragment.newInstance(EventListActivity.this,false);
                    dateRangePickerFragment.show(getSupportFragmentManager(),"datePicker");

                }
                else{
                }
                // Notify the selected item text
               // Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                //        .show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this, LinearLayout.VERTICAL,16));

// set the adapter
        //recyclerView.setAdapter(mAdapter);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(EventListActivity.this, new   RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        // TODO Handle item click

                        txtview = (TextView) v.findViewById(R.id.id);
                        button = (Button) v.findViewById(R.id.view);

                        ids = txtview.getText().toString();

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(EventListActivity.this,EventDetailsActivity.class);
                                intent.putExtra("userrole",roleval);
                                intent.putExtra("access_token",access_token);
                                intent.putExtra("id",ids);
                                startActivity(intent);
                                finish();
                            }
                        });

                        //Toast.makeText(AttendanceHistoryActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                    }
                })
        );

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EventListActivity.this,DashboardActivity.class);
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
            Intent intent = new Intent(EventListActivity.this,DashboardActivity.class);
            intent.putExtra("userrole",roleval);
            intent.putExtra("acces_token",access_token);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
        private void getValue() {
            RequestQueue queue = Volley.newRequestQueue(EventListActivity.this);
            //this is the url where you want to send the request
            //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

            // Request a string response from the provided URL.
            stringRequest = new StringRequest(Request.Method.GET, "http://i-access.ingtechbd.com/api/events/",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("responsevalue", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray j = jsonObject.getJSONArray("data");
                                if(j.length() > 0) {
                                    for (int i = 0; i < j.length(); i++) {
                                        try {
                                            //Getting json object
                                            JSONObject json = j.getJSONObject(i);
                                            id = json.getString("id");
                                            time = json.getString("time");
                                            title = json.getString("title");
                                            JSONObject jsonob = json.getJSONObject("user");
                                            name = jsonob.getString("name");


                                            String[] parts = time.split("-");

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

                                            String[] partss = time.split(" ");
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
                                                //intime = hour+":"+minute+"AM";
                                            } else {
                                                //intime = hour+":"+minute+"PM";
                                            }


                                            String t = v + "." + month + "." + year + " " + time;
                                            //Log.d("day",pd);


                                            //this is end of intime

                                            eventList.add(new Event(id, t, title, name));
                                            mAdapter.notifyDataSetChanged();

                                            progressDialog.dismiss();


                                            //Toast.makeText(AttendanceHistoryActivity.this, ""+time, Toast.LENGTH_SHORT).show();

                                            //Toast.makeText(AllTransactionsActivity.this, "category"+category, Toast.LENGTH_SHORT).show();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(EventListActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
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
            progressDialog = new ProgressDialog(EventListActivity.this);
            progressDialog.setMessage("Please wait....");
            progressDialog.show();
        }

    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
          eventList.clear();
          getValue();
    }
}
