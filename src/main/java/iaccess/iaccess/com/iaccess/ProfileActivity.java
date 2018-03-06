package iaccess.iaccess.com.iaccess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
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

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextView idTxt,nameTxt,emailTxt,phoneTxt,designationTxt,genderTxt,addressTxt,upnameTxt;
    private StringRequest stringRequest;
    private String id,name,email,phone,designaton,gender,address,access_token,Authorization,userId,roleval;
    private Button buttonmenu;
    PopupMenu popup;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        roleval = getIntent().getStringExtra("userrole");
        userId = getIntent().getStringExtra("idval");

        //Toast.makeText(this, ""+userId, Toast.LENGTH_SHORT).show();
        access_token = getIntent().getStringExtra("access_token");
        //Toast.makeText(AttendanceActivity.this, ""+access_token, Toast.LENGTH_SHORT).show();
        Authorization = "Bearer"+" "+access_token;


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Profile Activity");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ProfileActivity.this.setTitle("Profile");
        upnameTxt = (TextView) findViewById(R.id.textViewName);
        idTxt = (TextView) findViewById(R.id.idText);
        nameTxt = (TextView) findViewById(R.id.nameText);
        emailTxt = (TextView) findViewById(R.id.emailText);
        phoneTxt = (TextView) findViewById(R.id.phoneText);
        designationTxt = (TextView) findViewById(R.id.designationText);
        genderTxt = (TextView) findViewById(R.id.genderText);
        addressTxt = (TextView) findViewById(R.id.address);
        upnameTxt = (TextView) findViewById(R.id.textViewName);

        buttonmenu = (Button) findViewById(R.id.button);

        buttonmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup = new PopupMenu(ProfileActivity.this,view);
                //popup.setOnMenuItemClickListener(ProfileActivity.this);// to implement on click event on items of menu
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.button_menus, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch(id) {
                            case R.id.editprof:
                                Intent intent = new Intent(ProfileActivity.this,EditProfile.class);
                                intent.putExtra("idval",userId);
                                intent.putExtra("userrole",roleval);
                                intent.putExtra("access_token",access_token);
                                startActivity(intent);
                                return true;
                            case R.id.resetpass:
                                Intent intent1 = new Intent(ProfileActivity.this,ResetPassword.class);
                                intent1.putExtra("idval",userId);
                                intent1.putExtra("userrole",roleval);
                                intent1.putExtra("access_token",access_token);
                                startActivity(intent1);
                                return true;
                            default:
                                return false;
                        }

                    }
                });
            }
        });

        getValue();
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileActivity.this,DashboardActivity.class);
        intent.putExtra("userrole",roleval);
        intent.putExtra("idvalue",id);
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
            Intent intent = new Intent(ProfileActivity.this,DashboardActivity.class);
            intent.putExtra("userrole",roleval);
            intent.putExtra("acces_token",access_token);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }



    private void getValue() {

        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, "http://i-access.ingtechbd.com/api/users/view/"+userId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsevalue", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject json = jsonObject.getJSONObject("data");

                            //Getting json object
                            //Toast.makeText(AttendanceHistoryActivity.this, ""+id, Toast.LENGTH_SHORT).show();

                            id = json.getString("id");
                            name = json.getString("name");
                            email = json.getString("email");
                            phone = json.getString("phone");
                            designaton = json.getString("designaton");
                            gender = json.getString("gender");
                            address = json.getString("address");

                            upnameTxt.setText(name);
                            idTxt.setText(id);
                            nameTxt.setText(name);
                            emailTxt.setText(email);
                            phoneTxt.setText(phone);
                            designationTxt.setText(designaton);
                            genderTxt.setText(gender);
                            addressTxt.setText(address);
                            Log.d("id", id + "name" + name + "email" + email);


                            //Toast.makeText(AttendanceHistoryActivity.this, ""+time, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
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
                //headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjViYjJlYjI2ZDhhNmEwZjA5ZTE5ZmQyZTk1ZmExNWIxZDhjZmQwYWRhZjYxYTJjZDRlMTA4NmY3OGZjZWJjNzlhYzNlOTRhYmQ1YmQzODUzIn0.eyJhdWQiOiIyIiwianRpIjoiNWJiMmViMjZkOGE2YTBmMDllMTlmZDJlOTVmYTE1YjFkOGNmZDBhZGFmNjFhMmNkNGUxMDg2Zjc4ZmNlYmM3OWFjM2U5NGFiZDViZDM4NTMiLCJpYXQiOjE1MTc4MTg5NjksIm5iZiI6MTUxNzgxODk2OSwiZXhwIjoxNTQ5MzU0OTY5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.IrunbaEHmpxGwJTnJ9tUuibBjYNfgWimN2hxHSoFS33r-bcVb8MMIVlYY72vbsfhxMKnEf8k1ds0IJ65uCg8IFO-sEsqA_bpomY1IhLradgoX7TwBKv_iqYLLQ3zMMyjaiUYEHcrFTIJxn4A80YTjtXfekGquDVUczFoVVhUMumhVWaJ23bZuqD2ujZDwg2CyZy3ABlg9VT30qmwVxOY_ThfVCIll69onrZyLVVzNC_rvJPTzzD0Hb827VnMBLRN6vv7cBme9wasBJzq6ab4Ys9IFn4j7JtVRoWHf_wVxgjeDPo2clggWt_KqAP2rU2ORBrQCYXk0TKwhzRtck2aczcOZcJLBnmOSaj3-1zw8gGXwNyLi-8a4h4A6aQzXZQpQTs9BNt-cnaP6LVr1Et-yMtjMpEMpzPcgt11vTxjFKVbWdbmV41445T9EtaaOLUMTM3m0STfsNJTOvl-bOtIoYNuTmXD5uNn69b6HcpiIdsqFwljffc_uPGvzXg9ddko286YVAVP5dSX7BC8WrX21sktrMreln7sbHQI4dzfs2y2k07nknlCO1AaXJjaqpFn4w3kfKSOcPhZ7ngf4WfZ8qGYXRRNbvs_Xs1HvPkIe2XxZ2u1L8b9mAWbEt5cC3YcKGPd4cVTK3qXFZLgc5yD-AZiFXSIiqjDZ3nfdSW0Hyw");
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }
}
