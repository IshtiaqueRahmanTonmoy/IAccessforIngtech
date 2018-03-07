package iaccess.iaccess.com.iaccess;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.wallet.LineItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import iaccess.iaccess.com.entity.EmployeeInfo;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEdt,passwordEdt;
    private Button submitBtn;
    private String username,password,id,Authorization;
    private SharedPreferences SM;
    private ProgressDialog progressDialog;
    public static final String url = "http://i-access.ingtechbd.com/oauth/token";
    private String token_type,expires_in,acces_token,refresh_token,rolevalue,role;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SM = getSharedPreferences("userrecord", 0);
        Boolean islogin = SM.getBoolean("userlogin", false);
        String id = SM.getString("userid",null);
        String emailvalue = SM.getString("email",null);
        String accesstoken = SM.getString("acces_token",null);
        String rolevals = SM.getString("userrole",null);

        //Log.d("sharepref",accesstoken);
        if(islogin){
            Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
            intent.putExtra("acces_token",accesstoken);
            intent.putExtra("idvalue",id);
            intent.putExtra("email",emailvalue);
            intent.putExtra("userrole",rolevals);
            startActivity(intent);
            finish();
            return;
        }

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        emailEdt = (EditText) findViewById(R.id.email);
        passwordEdt = (EditText) findViewById(R.id.password);

        emailEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        passwordEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        submitBtn = (Button) findViewById(R.id.login_button);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = emailEdt.getText().toString();
                password = passwordEdt.getText().toString();

                if(username.isEmpty() || username.length() == 0 || username.equals("") || username == null)
                {
                    emailEdt.setError("username cannot be blank");
                    //EditText is empty
                }

                else if(password.isEmpty() || password.length() == 0 || password.equals("") || password == null)
                {
                    passwordEdt.setError("password cannot be blank");
                    //EditText is empty
                }
                else
                {
                    getogin(username,password);
                    //EditText is not empty
                }

                /*
                else
                {
                    getogin(username, password);
                    //EditText is not empty
                }
                */
            }

    private void getogin(final String username,final String password) {
        RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response",response);
                        //Toast.makeText(SignInActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        //progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.length() > 0) {
                                token_type = jsonObject.getString("token_type");
                                expires_in = jsonObject.getString("expires_in");
                                acces_token = jsonObject.getString("access_token");
                                refresh_token = jsonObject.getString("refresh_token");

                                Authorization = "Bearer" + " " + acces_token;



                                //getId(Authorization);
                                 Log.d("accesstoken",Authorization);
                            }
                            else{
                                //progressDialog.dismiss();
                                Toast.makeText(SignInActivity.this, "Please enter correct information..", Toast.LENGTH_SHORT).show();

                            }

                            progressDialog.dismiss();
                            //Toast.makeText(AllTransactionsActivity.this, "category"+category, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        Log.d("response",response);



                        if(response.equals("")){

                            progressDialog.dismiss();
                            //progressDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "Please enter correct information..", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //name.setText(sharedpreferences.getString(Name, ""));
                            //}

                            progressDialog.dismiss();
                            getId(new VolleyCallback(){
                                @Override
                                public void onSuccess(String result, String id) {
                                    Intent intent = new Intent(SignInActivity.this,DashboardActivity.class);
                                    intent.putExtra("userrole",result);
                                    intent.putExtra("idvalue",id);
                                    intent.putExtra("acces_token",acces_token);
                                    intent.putExtra("username",username);


                                    SharedPreferences.Editor edit = SM.edit();
                                    edit.putString("userrole", result);
                                    edit.putString("userid", id);
                                    edit.putString("username", username);
                                    edit.putString("acces_token",acces_token);
                                    edit.putBoolean("userlogin", true);
                                    edit.commit();

                                    startActivity(intent);



                                    finish();

                                    //rolevalue = result;
                                    Log.d("rolevalue",result);
                                }


                            });


                            progressDialog.dismiss();
                            //Toast.makeText(SignInActivity.this, "the value is"+rolevalue, Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //_response.setText("That didn't work!");
                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, "Please enter correct information", Toast.LENGTH_SHORT).show();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("grant_type", "password");
                params.put("client_id","2");
                params.put("client_secret","tX83nzCm61VasMQSguln17lSvvItsRhSmM5w7HpK");
                params.put("username",username);
                params.put("password",password);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }
        });
    }


    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void getId(final VolleyCallback callback) {

            //Toast.makeText(SignInActivity.this, ""+Auth, Toast.LENGTH_SHORT).show();
            //Log.d("Auth",Auth);

            RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
            //this is the url where you want to send the request
            //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

            // Request a string response from the provided URL.
            stringRequest = new StringRequest(Request.Method.GET, "http://i-access.ingtechbd.com/api/users?current_user=true",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("responrolevalue", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject j = jsonObject.getJSONObject("data");
                                                                     //Getting json object
                                        //JSONObject json = j.getJSONObject(i);

                                        role = j.getString("role");
                                        id = j.getString("id");

                                        callback.onSuccess(role,id);


                                //Log.d("idrole",role);
                                        //Toast.makeText(SignInActivity.this, ""+role, Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(SignInActivity.this, ""+id, Toast.LENGTH_SHORT).show();
                                        //progressDialog.dismiss();
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
                    //headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImE5ZGU3M2U2NmM4MTM4ZDQ3OWQ0ZDg3MDRiNWNjNWYwZWU4MzhjODliYjNiYjUyNzg3NWM3OTRlNTdhNzI5NTA3MWJjMGJjN2QyNGQxMGFjIn0.eyJhdWQiOiIyIiwianRpIjoiYTlkZTczZTY2YzgxMzhkNDc5ZDRkODcwNGI1Y2M1ZjBlZTgzOGM4OWJiM2JiNTI3ODc1Yzc5NGU1N2E3Mjk1MDcxYmMwYmM3ZDI0ZDEwYWMiLCJpYXQiOjE1MTk3MTg2MDksIm5iZiI6MTUxOTcxODYwOSwiZXhwIjoxNTUxMjU0NjA5LCJzdWIiOiIzIiwic2NvcGVzIjpbXX0.hV11Mk0S9Q06hopUrwNxVXCzwKAsnZ_xp3xyuMPxxYI6n12M44r7rzr6OPYpzGOFehWyTjn6ywSi-RsQDgzIhKqt5mr90uUXMLoYzNi2VQXJ-59liBBi8KhQD4jPjdEhmab0kXPHMhFG6b3ciVb2ksLMSoMB8WNUE8oU6KO_qQtE6uUnW5HPBG8NI4AzeXXIRJr43jIHKtYGJarR_fJNVAJn8hi5CGe4CFzv_oi9dQDtRz3AR6WoGhVSYrGHubF--aYNlWgQFqKkWvSGzbPkun2lU0RfGksGPTZzf8P47NVKn2aEhdthxlnhNKQOMOar4FY1dZwtwlHc0zPG6JpbYNgNA63eLlKwt81PT35e3ollzS_kYjJkNywCNziDwkPrnHglOn1MxQLVFPIzSkXZY8EM42yRHkqlgc6FpOjuO_rwS71HRs0DdKL296kCma3T2O7gmVkVOZV7MqySo9y9r5jiw7il3ms4AKwWwaTB767CbmwlZ751K1vVNMteVQCpb52umphOPUiVKebQIR0sfCKCcOiu5JoT7VJ8-cl1coCSbTsR04bsVElxGJJA7dvNkZnhljkcgk1-N9J83KK1O2LwKUWlvcfvXSOsRSWuNdpqnxMNg21TugyMDSkO9SssPPYVH3qSLB3-XbY0iNNRUSUlRR37DS3vIpvD8z_ediU");
                    return headers;
                }
            };
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
            //progressDialog = new ProgressDialog(EmployeeListActivity.this);
            //progressDialog.setMessage("Please wait....");
            //progressDialog.show();
    }
}


