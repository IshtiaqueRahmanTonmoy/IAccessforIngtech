package iaccess.iaccess.com.iaccess;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iaccess.iaccess.com.entity.EmployeeInfo;

public class AddEmployeeActivity extends AppCompatActivity {

    private EditText idEdt,nameEdt,emailEdt,phoneEdt,designationEdt,addressEdt,passwordEdt;
    private ScrollView scrollView;
    private Spinner genderSpinner,roleSpinner;
    private StringRequest stringRequest;
    private List<String> listOfString,emaillist,phonelist;
    private Button submitBtn;
    private ProgressDialog progressDialog;
    private ImageView image;
    Bitmap bitmap;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String names,designations,val,gender,role,name,email,phone,designation,avatar,address,password,access_token,Authorization,roleval,idval,emailval,phoneval;
    String[] gnd = new String[]{
            "Male",
            "Female"
    };
    Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        emaillist = new ArrayList<String>();
        phonelist = new ArrayList<String>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Employee");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getvalue();

        idval = getIntent().getStringExtra("idvalue");
        roleval = getIntent().getStringExtra("userrole");
        access_token = getIntent().getStringExtra("access_token");

        names = getIntent().getStringExtra("namevalue");
        designations = getIntent().getStringExtra("designationvalue");

        //Toast.makeText(AttendanceActivity.this, ""+access_token, Toast.LENGTH_SHORT).show();
        Authorization = "Bearer"+" "+access_token;


        Log.d("Auth", Authorization);
        // Toast.makeText(this, ""+Authorization, Toast.LENGTH_SHORT).show();

        getrole();
        //idEdt = (EditText) findViewById(R.id.idEdt);
        nameEdt = (EditText) findViewById(R.id.nameEdt);
        emailEdt = (EditText) findViewById(R.id.emailEdt);
        phoneEdt = (EditText) findViewById(R.id.phoneEdt);
        designationEdt = (EditText) findViewById(R.id.designationEdt);
        addressEdt = (EditText) findViewById(R.id.addressEdt);
        passwordEdt = (EditText) findViewById(R.id.passwordEdt);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        roleSpinner = (Spinner) findViewById(R.id.roleSpinner);
        image = (ImageView) findViewById(R.id.imageViewLogo);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),999);
            }
        });

        nameEdt.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (nameEdt.getText().toString().length() <= 0) {
                    nameEdt.setError("Enter Name");
                } else {
                    nameEdt.setError(null);
                }
            }
        });

        emailEdt.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (emailEdt.getText().toString().length() <= 0) {
                    emailEdt.setError("Enter Email");
                }

                else if(!emailEdt.getText().toString().matches(emailPattern)){

                    emailEdt.setError("Email not valid");
                }

                else if(emailEdt.getText().toString().length()>0){
                    for(String str : emaillist){
                        if(str.trim().contains(emailEdt.getText().toString())){
                            emailEdt.setError("Email already in use");
                        }
                    }
                }
                else {
                    emailEdt.setError(null);
                }
            }
        });
        phoneEdt.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (phoneEdt.getText().toString().length() <= 0) {
                    phoneEdt.setError("Enter Phone");
                }
                else if(phoneEdt.getText().toString().length()>0){
                    for(String str1 : phonelist){
                        if(str1.trim().contains(phoneEdt.getText().toString())){
                            phoneEdt.setError("Phone already in use");
                        }
                    }
                }
                else {
                    phoneEdt.setError(null);
                }
            }
        });
        designationEdt.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (designationEdt.getText().toString().length() <= 0) {
                    designationEdt.setError("Enter Designation");
                } else {
                    designationEdt.setError(null);
                }
            }
        });

        addressEdt.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (addressEdt.getText().toString().length() <= 0) {
                    addressEdt.setError("Enter Address");
                } else {
                    addressEdt.setError(null);
                }
            }
        });
        passwordEdt.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (passwordEdt.getText().toString().length() <= 0) {
                    passwordEdt.setError("Enter Password");
                }
                else if(passwordEdt.getText().toString().length() < 6){
                    passwordEdt.setError("Password should be atleast 6 character");
                }
                else {
                    passwordEdt.setError(null);
                }
            }
        });


        /*
        scrollView = (ScrollView) findViewById(R.id.scrollview);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                hideKeyboard(view);
            }
        });
        */

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                AddEmployeeActivity.this,R.layout.textview_for_spinner,gnd );

        spinnerArrayAdapter.setDropDownViewResource(R.layout.textview_for_spinner);

        genderSpinner.setAdapter(spinnerArrayAdapter);

        submitBtn = (Button) findViewById(R.id.buttonSubmit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nameEdt.getText().toString().length() <= 0) {
                    nameEdt.setError("Enter Name");
                }

                else if (emailEdt.getText().toString().length() <= 0) {
                    emailEdt.setError("Enter Email");
                }

                else if (phoneEdt.getText().toString().length() <= 0) {
                    phoneEdt.setError("Enter Phone");
                }

                else if (designationEdt.getText().toString().length() <= 0) {
                    designationEdt.setError("Enter Designation");
                }

                else if (addressEdt.getText().toString().length() <= 0) {
                    addressEdt.setError("Enter Address");
                }

                else if (passwordEdt.getText().toString().length() <= 0) {
                    passwordEdt.setError("Enter Password");
                }

                else if(emailEdt.getText().toString().equals(emailPattern)){
                    emailEdt.setError("Email address not valid");
                }
                else {
                    name = nameEdt.getText().toString();
                    email = emailEdt.getText().toString();
                    phone = phoneEdt.getText().toString();
                    designation = designationEdt.getText().toString();
                    address = addressEdt.getText().toString();
                    password = passwordEdt.getText().toString();
                    avatar = null;
                    //avatar = getStringImage(bitmap);

                    addemployee(name,email,phone,designation,address,password,avatar);
                }
            }
        });


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

        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                role = roleSpinner.getSelectedItem().toString();
                //Toast.makeText(TransactionsActivity.this, ""+text2, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void getvalue() {
        RequestQueue queue = Volley.newRequestQueue(AddEmployeeActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, "http://i-attendance.ingtechbd.com/api/users/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsevalueall", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray j = jsonObject.getJSONArray("data");
                            for(int i=0; i<j.length(); i++){
                                JSONObject json = j.getJSONObject(i);
                                emailval = json.getString("email");
                                phoneval = json.getString("phone");

                                emaillist.add(emailval);
                                phonelist.add(phoneval);

                                //Toast.makeText(AddEmployeeActivity.this, ""+emailval+""+phoneval, Toast.LENGTH_SHORT).show();
                                //progressDialog.dismiss();
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
            Intent intent = new Intent(AddEmployeeActivity.this,DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("userrole",roleval);
            intent.putExtra("idvalue",idval);
            intent.putExtra("namevalue",names);
            intent.putExtra("designationvalue",designations);
            intent.putExtra("acces_token",access_token);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddEmployeeActivity.this,DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("userrole",roleval);
        intent.putExtra("idvalue",idval);
        intent.putExtra("namevalue",names);
        intent.putExtra("designationvalue",designations);
        intent.putExtra("acces_token",access_token);
        startActivity(intent);
        finish();
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private String getStringImage(Bitmap bm) {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,ba);
        byte[] imagebyte = ba.toByteArray();
        String encode = Base64.encodeToString(imagebyte, Base64.DEFAULT);
        return encode;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 999 && resultCode== RESULT_OK && data !=null){
            Uri filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void addemployee(final String name,final String email,final String phone, final String designation,final String address,final String password, final String avatar) {
        RequestQueue queue = Volley.newRequestQueue(AddEmployeeActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        String url = "http://i-attendance.ingtechbd.com/api/users/add";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("respforaddemp", response);
                        //Log.d("respo", String.valueOf(response));

                        nameEdt.setText("");
                        emailEdt.setText("");
                        phoneEdt.setText("");
                        designationEdt.setText("");
                        addressEdt.setText("");
                        passwordEdt.setText("");

                        progressDialog.dismiss();
                        Toast.makeText(AddEmployeeActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                        //Toast.makeText(AddEmployeeActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                        // Display the response string.
                        //_response.setText(response);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                nameEdt.setText("");
                emailEdt.setText("");
                phoneEdt.setText("");
                designationEdt.setText("");
                addressEdt.setText("");
                passwordEdt.setText("");

                progressDialog.dismiss();
                Toast.makeText(AddEmployeeActivity.this, "Employee added successfully", Toast.LENGTH_SHORT).show();

                nameEdt.setError(null);
                emailEdt.setError(null);
                phoneEdt.setError(null);
                designationEdt.setError(null);
                addressEdt.setError(null);
                passwordEdt.setError(null);

                //_response.setText("That didn't work!");
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("designaton",designation);
                params.put("gender",gender);
                //params.put("avatar","images/users/default.jpeg");
                params.put("address",address);
                params.put("role",role);
                params.put("password",password);


                Log.d("attendanceaddemployee",params.toString());
                return params;
            }

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
        progressDialog = new ProgressDialog(AddEmployeeActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }

    private void getrole() {
        RequestQueue queue = Volley.newRequestQueue(AddEmployeeActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, "http://i-attendance.ingtechbd.com/api/users/add",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsevalue", response);
                        try {
                            JSONArray valarray = new JSONArray(response);
                            for (int i = 0; i < valarray.length(); i++) {

                                 val = valarray.get(i).toString();

                                listOfString = new ArrayList<String>();
                                listOfString.add("user");
                                listOfString.add("admin");

                                Log.d("str", val);
                                //Toast.makeText(AddEmployeeActivity.this, ""+str, Toast.LENGTH_SHORT).show();
                            }


                            Log.d("listofstring", String.valueOf(listOfString.size()));
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                    AddEmployeeActivity.this,R.layout.textview_for_spinner,listOfString);
                            spinnerArrayAdapter.setDropDownViewResource(R.layout.textview_for_spinner);
                            roleSpinner.setAdapter(spinnerArrayAdapter);


                        } catch (JSONException e) {
                            Log.e("JSON", "There was an error parsing the JSON", e);
                        }


                            //Toast.makeText(AttendanceHistoryActivity.this, ""+time, Toast.LENGTH_SHORT).show();
                            //progressDialog.dismiss();
                            //Toast.makeText(AllTransactionsActivity.this, "category"+category, Toast.LENGTH_SHORT).show();

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
        //progressDialog = new ProgressDialog(AddEmployeeActivity.this);
        //progressDialog.setMessage("Please wait....");
        //progressDialog.show();
    }
}
