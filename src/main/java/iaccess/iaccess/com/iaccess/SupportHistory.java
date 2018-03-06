package iaccess.iaccess.com.iaccess;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
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
import iaccess.iaccess.com.entity.SimpleDividerItemDecoration;
import iaccess.iaccess.com.entity.Support;
import iaccess.iaccess.iaccess.com.adapter.EmployeeAdapter;
import iaccess.iaccess.iaccess.com.adapter.RecyclerItemClickListener;
import iaccess.iaccess.iaccess.com.adapter.SupportAdapter;

public class SupportHistory extends AppCompatActivity implements DateRangePickerFragment.OnDateRangeSelectedListener{

    private Spinner spinner;
    private RecyclerView recyclerView;
    private SupportAdapter mAdapter;
    Toolbar toolbar;
    private TextView txtview;
    private Button button;
    private ProgressDialog progressDialog;
    private static final String GETALL_URL = "http://i-access.ingtechbd.com/api/supports/";
    private StringRequest stringRequest,stringRequest1;
    private String user_id,organization,start_time,end_time,name,timestamp,month,day,fromto,access_token,Authorization,roleval;
    String vals;
    private List<Support> supportList = new ArrayList<Support>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_history);

        roleval = getIntent().getStringExtra("userrole");
        access_token = getIntent().getStringExtra("access_token");
        Authorization = "Bearer"+" "+access_token;
        //Toast.makeText(this, ""+ Authorization, Toast.LENGTH_SHORT).show();

        getValue();

        //Toast.makeText(SupportHistory.this, ""+Authorization, Toast.LENGTH_SHORT).show();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Support History");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        spinner = (Spinner) findViewById(R.id.Spinner);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        String[] list = new String[]{
                "Select...",
                "Search by id",
                "Search by date"
        };


        final List<String> plantsList = new ArrayList<>(Arrays.asList(list));
        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,plantsList){

            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
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

                if(selectedItemText.equals("Select...")){

                }
                else if(selectedItemText.equals("Search by id")){
                    supportList.clear();
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SupportHistory.this);
                    LayoutInflater inflater = SupportHistory.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                    dialogBuilder.setView(dialogView);

                    final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

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
                    supportList.clear();
                    DateRangePickerFragment dateRangePickerFragment= DateRangePickerFragment.newInstance(SupportHistory.this,false);
                    dateRangePickerFragment.show(getSupportFragmentManager(),"datePicker");
                }
                // Notify the selected item text
                //Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                //        .show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //getValue();

        mAdapter = new SupportAdapter(supportList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this, LinearLayout.VERTICAL,16));

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(SupportHistory.this, new   RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        // TODO Handle item click

                        txtview = (TextView) v.findViewById(R.id.id);
                        button = (Button) v.findViewById(R.id.view);


                        vals = txtview.getText().toString();
                        //Toast.makeText(SupportHistory.this, ""+txtview.getText().toString(), Toast.LENGTH_SHORT).show();
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(SupportHistory.this,SupportDetailsActivity.class);
                                intent.putExtra("id", vals);
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
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SupportHistory.this,DashboardActivity.class);
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
            Intent intent = new Intent(SupportHistory.this,DashboardActivity.class);
            intent.putExtra("userrole",roleval);
            intent.putExtra("acces_token",access_token);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }


    private void getValue() {
        RequestQueue queue = Volley.newRequestQueue(SupportHistory.this);
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
                            if(j.length() > 0) {
                                for (int i = 0; i < j.length(); i++) {
                                    try {
                                        //Getting json object
                                        JSONObject json = j.getJSONObject(i);
                                        user_id = json.getString("user_id");
                                        organization = json.getString("organization");
                                        start_time = json.getString("start_time");
                                        end_time = json.getString("end_time");
                                        JSONObject jsonob = json.getJSONObject("user");
                                        name = jsonob.getString("name");

                                        //this is start of intime
                                        JSONObject jsonin = json.getJSONObject("created_at");
                                        timestamp = jsonin.getString("date");
                                        String[] parts = timestamp.split("-");

                                        String year = parts[0];
                                        String mth = parts[1];
                                        String d = parts[2];

                                        String[] da = d.split(" ");
                                        String v = da[0];
                                        Log.d("vagfd", v);
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

                                        } else if(mth.equals("12")){
                                            month = "December";
                                        }
                                        else {}
                                        day = parts[2];
                                        //Log.d("day",pd);

                                        Log.d("ffsd", day);
                                        //this is end of intime
                                        //this is end of out time
                                        fromto = start_time + "-" + end_time;

                                        supportList.add(new Support(user_id, name, month, v, fromto, organization));
                                        mAdapter.notifyDataSetChanged();


                                        //Toast.makeText(SupportHistory.this, ""+v, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        //Toast.makeText(AllTransactionsActivity.this, "category"+category, Toast.LENGTH_SHORT).show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(SupportHistory.this, "Data not found", Toast.LENGTH_SHORT).show();
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
        progressDialog = new ProgressDialog(SupportHistory.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }


    private void showvaluebyid(final String userid) {
        RequestQueue queue1 = Volley.newRequestQueue(SupportHistory.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        // Request a string response from the provided URL.
        stringRequest1 = new StringRequest(Request.Method.GET, "http://i-access.ingtechbd.com/api/supports/view/"+userid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsevalue", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.length() > 0) {
                                //JSONArray j = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonObject.length(); i++) {
                                    try {
                                        //Getting json object
                                        JSONObject json = jsonObject.getJSONObject("data");
                                        user_id = json.getString("user_id");
                                        organization = json.getString("organization");
                                        start_time = json.getString("start_time");
                                        end_time = json.getString("end_time");
                                        JSONObject jsonob = json.getJSONObject("user");
                                        name = jsonob.getString("name");

                                        Log.d("name", name);

                                        //this is start of intime
                                        JSONObject jsonin = json.getJSONObject("created_at");
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

                                        } else if(mth.equals("12")){
                                            month = "December";
                                        }
                                        else {}

                                        day = parts[2];
                                        //Log.d("day",pd);

                                        //this is end of intime
                                        //this is end of out time
                                        fromto = start_time + "-" + end_time;

                                        //Log.d("values",user_id+name+month+day+fromto+organization);
                                        supportList.add(new Support(user_id, name, month, v, fromto, organization));
                                        mAdapter.notifyDataSetChanged();


                                        //Toast.makeText(AttendanceHistoryActivity.this, ""+time, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        //Toast.makeText(SupportHistory.this, "category"+supportList.get(i).getName(), Toast.LENGTH_SHORT).show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            else {
                                progressDialog.dismiss();
                                Toast.makeText(SupportHistory.this, "Data not found", Toast.LENGTH_SHORT).show();
                            }
                        }


                        catch (JSONException e) {
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
        queue1.add(stringRequest1);
        progressDialog = new ProgressDialog(SupportHistory.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();

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

        getValue();
    }

    private void getvalues() {
        //Log.d("datepicker",from+""+to);
        //Toast.makeText(this, ""+from+""+to, Toast.LENGTH_SHORT).show();
        RequestQueue queue = Volley.newRequestQueue(SupportHistory.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, "http://i-access.ingtechbd.com/api/supports/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsevalue", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //JSONArray j = jsonObject.getJSONArray("data");
                            if(jsonObject.length() > 0) {
                                for (int i = 0; i < jsonObject.length(); i++) {
                                    try {
                                        //Getting json object
                                        JSONObject json = jsonObject.getJSONObject("data");
                                        user_id = json.getString("user_id");
                                        organization = json.getString("organization");
                                        start_time = json.getString("start_time");
                                        end_time = json.getString("end_time");
                                        JSONObject jsonob = json.getJSONObject("user");
                                        name = jsonob.getString("name");

                                        Log.d("name", name);

                                        //this is start of intime
                                        JSONObject jsonin = json.getJSONObject("created_at");
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
                                        //Log.d("day",pd);

                                        //this is end of intime
                                        //this is end of out time
                                        fromto = start_time + "-" + end_time;

                                        //Log.d("values",user_id+name+month+day+fromto+organization);
                                        supportList.add(new Support(user_id, name, month, day, fromto, organization));
                                        mAdapter.notifyDataSetChanged();

                                        progressDialog.dismiss();

                                        //Toast.makeText(AttendanceHistoryActivity.this, ""+time, Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(SupportHistory.this, "category"+supportList.get(i).getName(), Toast.LENGTH_SHORT).show();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            else{
                                progressDialog.dismiss();
                                Toast.makeText(SupportHistory.this, "Data not found.", Toast.LENGTH_SHORT).show();
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
        //
        Log.d("string", String.valueOf(stringRequest));
        queue.add(stringRequest);
        progressDialog = new ProgressDialog(SupportHistory.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }
}
