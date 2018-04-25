package iaccess.iaccess.com.iaccess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iaccess.iaccess.com.entity.Employee;
import iaccess.iaccess.com.entity.EmployeeInfo;
import iaccess.iaccess.com.entity.SimpleDividerItemDecoration;
import iaccess.iaccess.iaccess.com.adapter.EmployeeAdapter;
import iaccess.iaccess.iaccess.com.adapter.EmployeeAttendanceAdapter;
import iaccess.iaccess.iaccess.com.adapter.RecyclerItemClickListener;
import iaccess.iaccess.iaccess.com.adapter.RecyclerViewItemClickInterface;

public class EmployeeListActivity extends AppCompatActivity {

    private String Authorization,id,name,designation,role,idvalue,roleval,access_token,idval,names,designations,token;
    private RecyclerView recyclerView;
    private StringRequest stringRequest;
    private EmployeeAttendanceAdapter mAdapter;
    private List<EmployeeInfo> empList = new ArrayList<EmployeeInfo>();
    private static final String GETALL_URL = "http://i-attendance.ingtechbd.com/api/users/";
    private TextView textviewid;
    private ProgressDialog progressDialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        idvalue = getIntent().getStringExtra("idvalue");

        token = getIntent().getStringExtra("tokenval");

        //Toast.makeText(EmployeeListActivity.this, ""+token, Toast.LENGTH_SHORT).show();
        roleval = getIntent().getStringExtra("userrole");
        access_token = getIntent().getStringExtra("access_token");
        Authorization = "Bearer"+" "+access_token;

        names = getIntent().getStringExtra("namevalue");
        designations = getIntent().getStringExtra("designationvalue");

        Log.d("autho",Authorization);
        //Toast.makeText(this, ""+Authorization, Toast.LENGTH_SHORT).show();
        showvaluebyid();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Employee List");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new EmployeeAttendanceAdapter(empList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this, LinearLayout.VERTICAL,16));

// set the adapter
        //recyclerView.setAdapter(mAdapter);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(EmployeeListActivity.this, new   RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        // TODO Handle item click

                        textviewid = (TextView) v.findViewById(R.id.id);
                         idvalue = textviewid.getText().toString();


                         Intent intent = new Intent(EmployeeListActivity.this,EmployeeDetailsActivity.class);
                         intent.putExtra("userrole",roleval);
                         intent.putExtra("idval",idvalue);
                        intent.putExtra("namevalue",names);
                        intent.putExtra("designationvalue",designations);
                         intent.putExtra("access_token",access_token);
                        intent.putExtra("tokenval","2");
                         startActivity(intent);
                         finish();
                        //Toast.makeText(AttendanceHistoryActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                    }
                })
        );

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EmployeeListActivity.this,DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("userrole",roleval);
        intent.putExtra("idvalue",idvalue);
        intent.putExtra("namevalue",names);
        intent.putExtra("designationvalue",designations);
        intent.putExtra("acces_token",access_token);
        startActivity(intent);
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
            Intent intent = new Intent(EmployeeListActivity.this,DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("userrole",roleval);
            intent.putExtra("idvalue",idvalue);
            intent.putExtra("namevalue",names);
            intent.putExtra("designationvalue",designations);
            intent.putExtra("acces_token",access_token);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
    private void showvaluebyid() {
        RequestQueue queue = Volley.newRequestQueue(EmployeeListActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, GETALL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsevalueall", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray j = jsonObject.getJSONArray("data");
                            for(int i=0; i<j.length(); i++){
                                JSONObject json = j.getJSONObject(i);
                                id = json.getString("id");
                                name = json.getString("name");
                                designation = json.getString("designaton");
                                role = json.getString("role");

                                Log.d("adaptervalue", id + "name" + name + "desingation" + designation + "role");
                                empList.add(new EmployeeInfo(id,name,designation,role));
                                mAdapter.notifyDataSetChanged();

                                //Toast.makeText(AttendanceHistoryActivity.this, ""+time, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                //Toast.makeText(AllTransactionsActivity.this, "category"+category, Toast.LENGTH_SHORT).show();
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
                //headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjQ5MzIwZjgyNzc1NzI5NThiMTFjMmU0ODA1YWE1YTc3MWRhMzM2N2QwNTllZjFkMGNkNTRjMThlYWQzYmI2NDc3OGU0Y2FlM2E3NTBkMzgwIn0.eyJhdWQiOiIyIiwianRpIjoiNDkzMjBmODI3NzU3Mjk1OGIxMWMyZTQ4MDVhYTVhNzcxZGEzMzY3ZDA1OWVmMWQwY2Q1NGMxOGVhZDNiYjY0Nzc4ZTRjYWUzYTc1MGQzODAiLCJpYXQiOjE1MTk3MjQ1MzAsIm5iZiI6MTUxOTcyNDUzMCwiZXhwIjoxNTUxMjYwNTMwLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.JmwLGPco1hAcKDD3z9pwguyIq2tjuxQg-korcwfDLgOCykv4hC20cE5MSe8ekVUiel2e9LWYkgq1jKz93-UawxKmd0vQ6VBLerIfVX8hbusgiOjQTcAVRCO5N40fc2GuhJ3IAXNA3sFeDfDU-sCMvdB1vKm7pMXH2AgIJLaUflDfA6KY67vy_-HhsDq6ZBp-PZsVNXGMNKiDxzuufUWZD6EOQ6Dyn8VHa3RkUHu7QL_e05NI3-tptLSNkzrtWt2Vycyy42zA8ZRdAGFsU_LNYd9oqaDguFmEihWGLjUwXGEsql1BulrtHdEAduzuM1_UIDAsU1cOrUktXMzJMnZ2rrOCnQfL1KaHHduGamQV1f7FvdLEkAQU0lUMN-NyqU-MnypJ9Z2QlZbIXYkdfZmOMWnISgW_8RD2v_Wwf0tPvmzEaeS0L6LPt4CPcIp-RNLHZPvbTz79yiZrl8J_D5L-KK4Es638InDZeSjPm5jIgoerWlt_8Z74K9XOkf0-YsndPL0E9CGbdfb2vOCqZoH-QSmDu61Q-ED5QFSsJa2eU__XMG-sbva3n5zgH1NVW09ccOkQ3BeCI_LE2C4WItljxCsNutla7ilaHlIS5SLKSWZu4swLAciiChWYi2eWcoaVMKTv9G6_DMSF0D5JAcIvCkklvctteLgyTqdG_Pl2edk");
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        progressDialog = new ProgressDialog(EmployeeListActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }
}
