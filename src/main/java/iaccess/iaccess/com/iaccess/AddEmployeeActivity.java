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
import android.util.Base64;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEmployeeActivity extends AppCompatActivity {

    private EditText idEdt,nameEdt,emailEdt,phoneEdt,designationEdt,addressEdt,passwordEdt;
    private ScrollView scrollView;
    private Spinner genderSpinner,roleSpinner;
    private StringRequest stringRequest;
    private List<String> listOfString;
    private Button submitBtn;
    private ProgressDialog progressDialog;
    private ImageView image;
    Bitmap bitmap;
    String val,gender,role,name,email,phone,designation,avatar,address,password;
    String[] gnd = new String[]{
            "Male",
            "Female"
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        getrole();
        idEdt = (EditText) findViewById(R.id.idEdt);
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

        scrollView = (ScrollView) findViewById(R.id.scrollview);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                hideKeyboard(view);
            }
        });

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                AddEmployeeActivity.this,R.layout.textview_for_spinner,gnd );

        spinnerArrayAdapter.setDropDownViewResource(R.layout.textview_for_spinner);

        genderSpinner.setAdapter(spinnerArrayAdapter);

        submitBtn = (Button) findViewById(R.id.buttonSubmit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = nameEdt.getText().toString();
                email = emailEdt.getText().toString();
                phone = phoneEdt.getText().toString();
                designation = designationEdt.getText().toString();
                address = addressEdt.getText().toString();
                avatar = getStringImage(bitmap);
                password = passwordEdt.getText().toString();
                addemployee();
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

    private void addemployee() {
        RequestQueue requestQueue = Volley.newRequestQueue(AddEmployeeActivity.this);
          /*Post data*/
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("phone", phone);
        params.put("designaton",designation);
        params.put("gender",gender);
        //params.put("avatar",avatar);
        params.put("address",address);
        params.put("role",role);
        params.put("password",password);

        Log.d("paramsforattendance",params.toString());


        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST, "http://i-access.ingtechbd.com/api/users/add",

                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Handle Error
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
        requestQueue.add(postRequest);
    }

    private void getrole() {
        RequestQueue queue = Volley.newRequestQueue(AddEmployeeActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, "http://i-access.ingtechbd.com/api/users/add",
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
                            progressDialog.dismiss();
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
                headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjViYjJlYjI2ZDhhNmEwZjA5ZTE5ZmQyZTk1ZmExNWIxZDhjZmQwYWRhZjYxYTJjZDRlMTA4NmY3OGZjZWJjNzlhYzNlOTRhYmQ1YmQzODUzIn0.eyJhdWQiOiIyIiwianRpIjoiNWJiMmViMjZkOGE2YTBmMDllMTlmZDJlOTVmYTE1YjFkOGNmZDBhZGFmNjFhMmNkNGUxMDg2Zjc4ZmNlYmM3OWFjM2U5NGFiZDViZDM4NTMiLCJpYXQiOjE1MTc4MTg5NjksIm5iZiI6MTUxNzgxODk2OSwiZXhwIjoxNTQ5MzU0OTY5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.IrunbaEHmpxGwJTnJ9tUuibBjYNfgWimN2hxHSoFS33r-bcVb8MMIVlYY72vbsfhxMKnEf8k1ds0IJ65uCg8IFO-sEsqA_bpomY1IhLradgoX7TwBKv_iqYLLQ3zMMyjaiUYEHcrFTIJxn4A80YTjtXfekGquDVUczFoVVhUMumhVWaJ23bZuqD2ujZDwg2CyZy3ABlg9VT30qmwVxOY_ThfVCIll69onrZyLVVzNC_rvJPTzzD0Hb827VnMBLRN6vv7cBme9wasBJzq6ab4Ys9IFn4j7JtVRoWHf_wVxgjeDPo2clggWt_KqAP2rU2ORBrQCYXk0TKwhzRtck2aczcOZcJLBnmOSaj3-1zw8gGXwNyLi-8a4h4A6aQzXZQpQTs9BNt-cnaP6LVr1Et-yMtjMpEMpzPcgt11vTxjFKVbWdbmV41445T9EtaaOLUMTM3m0STfsNJTOvl-bOtIoYNuTmXD5uNn69b6HcpiIdsqFwljffc_uPGvzXg9ddko286YVAVP5dSX7BC8WrX21sktrMreln7sbHQI4dzfs2y2k07nknlCO1AaXJjaqpFn4w3kfKSOcPhZ7ngf4WfZ8qGYXRRNbvs_Xs1HvPkIe2XxZ2u1L8b9mAWbEt5cC3YcKGPd4cVTK3qXFZLgc5yD-AZiFXSIiqjDZ3nfdSW0Hyw");
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        progressDialog = new ProgressDialog(AddEmployeeActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }
}
