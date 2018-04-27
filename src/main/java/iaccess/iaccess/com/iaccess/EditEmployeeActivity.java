package iaccess.iaccess.com.iaccess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditEmployeeActivity extends AppCompatActivity {

    private EditText idEdt,nameEdt,emailEdt,phoneEdt,designationEdt,addressEdt;
    private Spinner genderSpinner;
    private StringRequest stringRequest;
    private String id,name,email,phone,designation,gender,address,idval,roleval,access_token,Authorization,names,designations,token;
    private Button update;
    String[] gnd = new String[]{
            "Male",
            "Female"
    };
    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Edit Employee");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        token = getIntent().getStringExtra("tokenval");
        roleval = getIntent().getStringExtra("userrole");
        idval = getIntent().getStringExtra("idval");
        access_token = getIntent().getStringExtra("access_token");
        Authorization = "Bearer"+" "+access_token;

        names = getIntent().getStringExtra("namevalue");
        designations = getIntent().getStringExtra("designationvalue");
        //Toast.makeText(EditEmployeeActivity.this, ""+Authorization, Toast.LENGTH_SHORT).show();

        // Toast.makeText(EditEmployeeActivity.this, ""+idval, Toast.LENGTH_SHORT).show();

        //idEdt = (EditText) findViewById(R.id.idEdt);
        nameEdt = (EditText) findViewById(R.id.nameEdt);
        emailEdt = (EditText) findViewById(R.id.emailEdt);
        phoneEdt = (EditText) findViewById(R.id.phoneEdt);
        designationEdt = (EditText) findViewById(R.id.designationEdt);
        addressEdt = (EditText) findViewById(R.id.addressEdt);

        genderSpinner = (Spinner) findViewById(R.id.gender);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                EditEmployeeActivity.this,R.layout.textview_for_spinner,gnd );

        spinnerArrayAdapter.setDropDownViewResource(R.layout.textview_for_spinner);

        genderSpinner.setAdapter(spinnerArrayAdapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender = genderSpinner.getSelectedItem().toString();
                //Toast.makeText(TransactionsActivity.this, ""+text2, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        update = (Button) findViewById(R.id.update);

        getValue();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameEdt.getText().toString();
                email = emailEdt.getText().toString();
                phone = phoneEdt.getText().toString();
                designation = designationEdt.getText().toString();
                address = addressEdt.getText().toString();

                update(name,email,phone,designation,address);
            }

    private void update(final String name, final String email, final String phone, final String designation, final String address) {
        //Log.d()
        RequestQueue queue = Volley.newRequestQueue(EditEmployeeActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        String url = "http://i-attendance.ingtechbd.com/api/users/edit/"+idval;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsevalue", response);
                        progressDialog.dismiss();
                        Toast.makeText(EditEmployeeActivity.this, "updated successfully", Toast.LENGTH_SHORT).show();
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
                //params.put("id", String.valueOf(1));
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("designation", designation);
                params.put("gender",gender);
                //params.put("avatar",null);
                params.put("address", address);

                Log.d("paramsforattendance",params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers = new HashMap<String,String>();
                headers.put("Accept","application/json");
                headers.put("Authorization",Authorization);
                //headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjQ5MzIwZjgyNzc1NzI5NThiMTFjMmU0ODA1YWE1YTc3MWRhMzM2N2QwNTllZjFkMGNkNTRjMThlYWQzYmI2NDc3OGU0Y2FlM2E3NTBkMzgwIn0.eyJhdWQiOiIyIiwianRpIjoiNDkzMjBmODI3NzU3Mjk1OGIxMWMyZTQ4MDVhYTVhNzcxZGEzMzY3ZDA1OWVmMWQwY2Q1NGMxOGVhZDNiYjY0Nzc4ZTRjYWUzYTc1MGQzODAiLCJpYXQiOjE1MTk3MjQ1MzAsIm5iZiI6MTUxOTcyNDUzMCwiZXhwIjoxNTUxMjYwNTMwLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.JmwLGPco1hAcKDD3z9pwguyIq2tjuxQg-korcwfDLgOCykv4hC20cE5MSe8ekVUiel2e9LWYkgq1jKz93-UawxKmd0vQ6VBLerIfVX8hbusgiOjQTcAVRCO5N40fc2GuhJ3IAXNA3sFeDfDU-sCMvdB1vKm7pMXH2AgIJLaUflDfA6KY67vy_-HhsDq6ZBp-PZsVNXGMNKiDxzuufUWZD6EOQ6Dyn8VHa3RkUHu7QL_e05NI3-tptLSNkzrtWt2Vycyy42zA8ZRdAGFsU_LNYd9oqaDguFmEihWGLjUwXGEsql1BulrtHdEAduzuM1_UIDAsU1cOrUktXMzJMnZ2rrOCnQfL1KaHHduGamQV1f7FvdLEkAQU0lUMN-NyqU-MnypJ9Z2QlZbIXYkdfZmOMWnISgW_8RD2v_Wwf0tPvmzEaeS0L6LPt4CPcIp-RNLHZPvbTz79yiZrl8J_D5L-KK4Es638InDZeSjPm5jIgoerWlt_8Z74K9XOkf0-YsndPL0E9CGbdfb2vOCqZoH-QSmDu61Q-ED5QFSsJa2eU__XMG-sbva3n5zgH1NVW09ccOkQ3BeCI_LE2C4WItljxCsNutla7ilaHlIS5SLKSWZu4swLAciiChWYi2eWcoaVMKTv9G6_DMSF0D5JAcIvCkklvctteLgyTqdG_Pl2edk");
                //headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjViYjJlYjI2ZDhhNmEwZjA5ZTE5ZmQyZTk1ZmExNWIxZDhjZmQwYWRhZjYxYTJjZDRlMTA4NmY3OGZjZWJjNzlhYzNlOTRhYmQ1YmQzODUzIn0.eyJhdWQiOiIyIiwianRpIjoiNWJiMmViMjZkOGE2YTBmMDllMTlmZDJlOTVmYTE1YjFkOGNmZDBhZGFmNjFhMmNkNGUxMDg2Zjc4ZmNlYmM3OWFjM2U5NGFiZDViZDM4NTMiLCJpYXQiOjE1MTc4MTg5NjksIm5iZiI6MTUxNzgxODk2OSwiZXhwIjoxNTQ5MzU0OTY5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.IrunbaEHmpxGwJTnJ9tUuibBjYNfgWimN2hxHSoFS33r-bcVb8MMIVlYY72vbsfhxMKnEf8k1ds0IJ65uCg8IFO-sEsqA_bpomY1IhLradgoX7TwBKv_iqYLLQ3zMMyjaiUYEHcrFTIJxn4A80YTjtXfekGquDVUczFoVVhUMumhVWaJ23bZuqD2ujZDwg2CyZy3ABlg9VT30qmwVxOY_ThfVCIll69onrZyLVVzNC_rvJPTzzD0Hb827VnMBLRN6vv7cBme9wasBJzq6ab4Ys9IFn4j7JtVRoWHf_wVxgjeDPo2clggWt_KqAP2rU2ORBrQCYXk0TKwhzRtck2aczcOZcJLBnmOSaj3-1zw8gGXwNyLi-8a4h4A6aQzXZQpQTs9BNt-cnaP6LVr1Et-yMtjMpEMpzPcgt11vTxjFKVbWdbmV41445T9EtaaOLUMTM3m0STfsNJTOvl-bOtIoYNuTmXD5uNn69b6HcpiIdsqFwljffc_uPGvzXg9ddko286YVAVP5dSX7BC8WrX21sktrMreln7sbHQI4dzfs2y2k07nknlCO1AaXJjaqpFn4w3kfKSOcPhZ7ngf4WfZ8qGYXRRNbvs_Xs1HvPkIe2XxZ2u1L8b9mAWbEt5cC3YcKGPd4cVTK3qXFZLgc5yD-AZiFXSIiqjDZ3nfdSW0Hyw");
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        progressDialog = new ProgressDialog(EditEmployeeActivity.this);
        progressDialog.setMessage("Updating Please wait....");
        progressDialog.show();
    }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditEmployeeActivity.this,EmployeeDetailsActivity.class);
        intent.putExtra("userrole",roleval);
        intent.putExtra("idval",idval);
        intent.putExtra("namevalue",names);
        intent.putExtra("tokenval","2");
        intent.putExtra("designationvalue",designations);
        //intent.putExtra("idval",idvalue);
        intent.putExtra("access_token",access_token);
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
            Intent intent = new Intent(EditEmployeeActivity.this,EmployeeDetailsActivity.class);
            intent.putExtra("userrole",roleval);
            intent.putExtra("idval",idval);
            intent.putExtra("tokenval","2");
            intent.putExtra("namevalue",names);
            intent.putExtra("designationvalue",designations);
            intent.putExtra("access_token",access_token);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }



    private void getValue() {
        RequestQueue queue = Volley.newRequestQueue(EditEmployeeActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, "http://i-attendance.ingtechbd.com/api/users/view/"+idval,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("responsevalue", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject json = jsonObject.getJSONObject("data");

                            //Getting json object
                            //Toast.makeText(AttendanceHistoryActivity.this, ""+id, Toast.LENGTH_SHORT).show();

                            id = json.getString("id");
                            name = json.getString("name");
                            email = json.getString("email");
                            phone = json.getString("phone");
                            designation = json.getString("designaton");
                            gender = json.getString("gender");
                            address = json.getString("address");

                            progressDialog.dismiss();

                            //idEdt.setText(id);
                            nameEdt.setText(name);
                            emailEdt.setText(email);
                            phoneEdt.setText(phone);
                            designationEdt.setText(designation);
                            addressEdt.setText(address);

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                    EditEmployeeActivity.this,R.layout.textview_for_spinner,gnd );

                            spinnerArrayAdapter.setDropDownViewResource(R.layout.textview_for_spinner);

                            genderSpinner.setAdapter(spinnerArrayAdapter);

                            Log.d("id", id + "name" + name + "email" + email);


                            //Toast.makeText(AttendanceHistoryActivity.this, ""+time, Toast.LENGTH_SHORT).show();

                            //Toast.makeText(AllTransactionsActivity.this, "category"+category, Toast.LENGTH_SHORT).show();




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
                //headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjQ5MzIwZjgyNzc1NzI5NThiMTFjMmU0ODA1YWE1YTc3MWRhMzM2N2QwNTllZjFkMGNkNTRjMThlYWQzYmI2NDc3OGU0Y2FlM2E3NTBkMzgwIn0.eyJhdWQiOiIyIiwianRpIjoiNDkzMjBmODI3NzU3Mjk1OGIxMWMyZTQ4MDVhYTVhNzcxZGEzMzY3ZDA1OWVmMWQwY2Q1NGMxOGVhZDNiYjY0Nzc4ZTRjYWUzYTc1MGQzODAiLCJpYXQiOjE1MTk3MjQ1MzAsIm5iZiI6MTUxOTcyNDUzMCwiZXhwIjoxNTUxMjYwNTMwLCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.JmwLGPco1hAcKDD3z9pwguyIq2tjuxQg-korcwfDLgOCykv4hC20cE5MSe8ekVUiel2e9LWYkgq1jKz93-UawxKmd0vQ6VBLerIfVX8hbusgiOjQTcAVRCO5N40fc2GuhJ3IAXNA3sFeDfDU-sCMvdB1vKm7pMXH2AgIJLaUflDfA6KY67vy_-HhsDq6ZBp-PZsVNXGMNKiDxzuufUWZD6EOQ6Dyn8VHa3RkUHu7QL_e05NI3-tptLSNkzrtWt2Vycyy42zA8ZRdAGFsU_LNYd9oqaDguFmEihWGLjUwXGEsql1BulrtHdEAduzuM1_UIDAsU1cOrUktXMzJMnZ2rrOCnQfL1KaHHduGamQV1f7FvdLEkAQU0lUMN-NyqU-MnypJ9Z2QlZbIXYkdfZmOMWnISgW_8RD2v_Wwf0tPvmzEaeS0L6LPt4CPcIp-RNLHZPvbTz79yiZrl8J_D5L-KK4Es638InDZeSjPm5jIgoerWlt_8Z74K9XOkf0-YsndPL0E9CGbdfb2vOCqZoH-QSmDu61Q-ED5QFSsJa2eU__XMG-sbva3n5zgH1NVW09ccOkQ3BeCI_LE2C4WItljxCsNutla7ilaHlIS5SLKSWZu4swLAciiChWYi2eWcoaVMKTv9G6_DMSF0D5JAcIvCkklvctteLgyTqdG_Pl2edk");
                //headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjViYjJlYjI2ZDhhNmEwZjA5ZTE5ZmQyZTk1ZmExNWIxZDhjZmQwYWRhZjYxYTJjZDRlMTA4NmY3OGZjZWJjNzlhYzNlOTRhYmQ1YmQzODUzIn0.eyJhdWQiOiIyIiwianRpIjoiNWJiMmViMjZkOGE2YTBmMDllMTlmZDJlOTVmYTE1YjFkOGNmZDBhZGFmNjFhMmNkNGUxMDg2Zjc4ZmNlYmM3OWFjM2U5NGFiZDViZDM4NTMiLCJpYXQiOjE1MTc4MTg5NjksIm5iZiI6MTUxNzgxODk2OSwiZXhwIjoxNTQ5MzU0OTY5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.IrunbaEHmpxGwJTnJ9tUuibBjYNfgWimN2hxHSoFS33r-bcVb8MMIVlYY72vbsfhxMKnEf8k1ds0IJ65uCg8IFO-sEsqA_bpomY1IhLradgoX7TwBKv_iqYLLQ3zMMyjaiUYEHcrFTIJxn4A80YTjtXfekGquDVUczFoVVhUMumhVWaJ23bZuqD2ujZDwg2CyZy3ABlg9VT30qmwVxOY_ThfVCIll69onrZyLVVzNC_rvJPTzzD0Hb827VnMBLRN6vv7cBme9wasBJzq6ab4Ys9IFn4j7JtVRoWHf_wVxgjeDPo2clggWt_KqAP2rU2ORBrQCYXk0TKwhzRtck2aczcOZcJLBnmOSaj3-1zw8gGXwNyLi-8a4h4A6aQzXZQpQTs9BNt-cnaP6LVr1Et-yMtjMpEMpzPcgt11vTxjFKVbWdbmV41445T9EtaaOLUMTM3m0STfsNJTOvl-bOtIoYNuTmXD5uNn69b6HcpiIdsqFwljffc_uPGvzXg9ddko286YVAVP5dSX7BC8WrX21sktrMreln7sbHQI4dzfs2y2k07nknlCO1AaXJjaqpFn4w3kfKSOcPhZ7ngf4WfZ8qGYXRRNbvs_Xs1HvPkIe2XxZ2u1L8b9mAWbEt5cC3YcKGPd4cVTK3qXFZLgc5yD-AZiFXSIiqjDZ3nfdSW0Hyw");
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        progressDialog = new ProgressDialog(EditEmployeeActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }

}
