package iaccess.iaccess.com.iaccess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class EmployeeListActivity extends AppCompatActivity {

    private String id,name,designation,role,idvalue;
    private RecyclerView recyclerView;
    private StringRequest stringRequest;
    private EmployeeAttendanceAdapter mAdapter;
    private List<EmployeeInfo> empList = new ArrayList<EmployeeInfo>();
    private static final String GETALL_URL = "http://i-access.ingtechbd.com/api/users/";
    private TextView textviewid;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);


        showvaluebyid();
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
                         intent.putExtra("idval",idvalue);
                         startActivity(intent);
                        //Toast.makeText(AttendanceHistoryActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                    }
                })
        );
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
                        Log.d("responsevalue", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray j = jsonObject.getJSONArray("data");
                            for(int i=0;i<j.length();i++){
                                try {
                                    //Getting json object
                                    JSONObject json = j.getJSONObject(i);

                                    id = json.getString("id");
                                    name = json.getString("name");
                                    designation = json.getString("designaton");
                                    role = json.getString("role");

                                    empList.add(new EmployeeInfo(id,name,designation,role));
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
                headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjViYjJlYjI2ZDhhNmEwZjA5ZTE5ZmQyZTk1ZmExNWIxZDhjZmQwYWRhZjYxYTJjZDRlMTA4NmY3OGZjZWJjNzlhYzNlOTRhYmQ1YmQzODUzIn0.eyJhdWQiOiIyIiwianRpIjoiNWJiMmViMjZkOGE2YTBmMDllMTlmZDJlOTVmYTE1YjFkOGNmZDBhZGFmNjFhMmNkNGUxMDg2Zjc4ZmNlYmM3OWFjM2U5NGFiZDViZDM4NTMiLCJpYXQiOjE1MTc4MTg5NjksIm5iZiI6MTUxNzgxODk2OSwiZXhwIjoxNTQ5MzU0OTY5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.IrunbaEHmpxGwJTnJ9tUuibBjYNfgWimN2hxHSoFS33r-bcVb8MMIVlYY72vbsfhxMKnEf8k1ds0IJ65uCg8IFO-sEsqA_bpomY1IhLradgoX7TwBKv_iqYLLQ3zMMyjaiUYEHcrFTIJxn4A80YTjtXfekGquDVUczFoVVhUMumhVWaJ23bZuqD2ujZDwg2CyZy3ABlg9VT30qmwVxOY_ThfVCIll69onrZyLVVzNC_rvJPTzzD0Hb827VnMBLRN6vv7cBme9wasBJzq6ab4Ys9IFn4j7JtVRoWHf_wVxgjeDPo2clggWt_KqAP2rU2ORBrQCYXk0TKwhzRtck2aczcOZcJLBnmOSaj3-1zw8gGXwNyLi-8a4h4A6aQzXZQpQTs9BNt-cnaP6LVr1Et-yMtjMpEMpzPcgt11vTxjFKVbWdbmV41445T9EtaaOLUMTM3m0STfsNJTOvl-bOtIoYNuTmXD5uNn69b6HcpiIdsqFwljffc_uPGvzXg9ddko286YVAVP5dSX7BC8WrX21sktrMreln7sbHQI4dzfs2y2k07nknlCO1AaXJjaqpFn4w3kfKSOcPhZ7ngf4WfZ8qGYXRRNbvs_Xs1HvPkIe2XxZ2u1L8b9mAWbEt5cC3YcKGPd4cVTK3qXFZLgc5yD-AZiFXSIiqjDZ3nfdSW0Hyw");
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
