package iaccess.iaccess.com.iaccess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEdt,passwordEdt;
    private Button submitBtn;
    private String username,password;
    private SharedPreferences SM;
    private ProgressDialog progressDialog;
    public static final String url = "http://i-access.ingtechbd.com/oauth/token";
    private String token_type,expires_in,acces_token,refresh_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SM = getSharedPreferences("userrecord", 0);
        Boolean islogin = SM.getBoolean("userlogin", false);
        String emailvalue = SM.getString("email",null);
        if(islogin){
            Intent intent = new Intent(SignInActivity.this, DashboardActivity.class);
            intent.putExtra("email",emailvalue);
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

        submitBtn = (Button) findViewById(R.id.login_button);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = emailEdt.getText().toString();
                password = passwordEdt.getText().toString();

                RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
                //this is the url where you want to send the request
                //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response",response);
                                Toast.makeText(SignInActivity.this, ""+response, Toast.LENGTH_SHORT).show();

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray j = jsonObject.getJSONArray("result");
                                    for(int i=0;i<j.length();i++){
                                        try {
                                            //Getting json object
                                            JSONObject json = j.getJSONObject(i);
                                            token_type = json.getString("token_type");
                                            expires_in = json.getString("expires_in");
                                            acces_token = json.getString("access_token");
                                            refresh_token = json.getString("refresh_token");

                                            //progressDialog.dismiss();
                                            //Toast.makeText(AllTransactionsActivity.this, "category"+category, Toast.LENGTH_SHORT).show();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d("response",response);

                                if(response.equals("")){

                                    //progressDialog.dismiss();
                                    Toast.makeText(SignInActivity.this, "Please enter correct information..", Toast.LENGTH_SHORT).show();
                                }
                                else {

                                    SharedPreferences.Editor edit = SM.edit();
                                    edit.putString("username", username);
                                    edit.putBoolean("userlogin", true);
                                    edit.commit();

                                    Intent intent = new Intent(SignInActivity.this,DashboardActivity.class);
                                    intent.putExtra("username",username);
                                    startActivity(intent);
                                    //progressDialog.dismiss();
                                    finish();
                                }
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
                //progressDialog = new ProgressDialog(SignInActivity.this);
                //progressDialog.setMessage("Please wait....");
                //progressDialog.show();
            }
        });
    }
}


