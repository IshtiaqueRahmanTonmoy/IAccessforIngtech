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

import java.util.HashMap;
import java.util.Map;

public class ResetPassword extends AppCompatActivity {

    private EditText oldpass,newpass,retypepass;
    private Button submitBtn;
    private String old_password,new_password,idval,access_token,Authorization,roleval;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        access_token = getIntent().getStringExtra("access_token");
        //Toast.makeText(AttendanceActivity.this, ""+access_token, Toast.LENGTH_SHORT).show();
        Authorization = "Bearer"+" "+access_token;

        //Toast.makeText(this, ""+Authorization, Toast.LENGTH_SHORT).show();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Reset Password");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        roleval = getIntent().getStringExtra("userrole");
        idval = getIntent().getStringExtra("idval");
        //Toast.makeText(ResetPassword.this, ""+idval, Toast.LENGTH_SHORT).show();

        oldpass = (EditText) findViewById(R.id.oldpassword);
        newpass = (EditText) findViewById(R.id.newpassword);
        retypepass = (EditText) findViewById(R.id.retypepassword);

        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                old_password = oldpass.getText().toString();
                new_password = newpass.getText().toString();
                changepassword(old_password,new_password);

    }

    private void changepassword(final String old_password, final String new_password) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(ResetPassword.this);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://i-access.ingtechbd.com/api/users/password-reset/"+idval, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responsevalue",response);
                progressDialog.dismiss();
                Toast.makeText(ResetPassword.this, "Password successfully changed", Toast.LENGTH_SHORT).show();
                //mPostCommentResponse.requestCompleted();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("erros", String.valueOf(error));
                //progressDialog.dismiss();
                //mPostCommentResponse.requestEndedWithError(error);
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("old_password", old_password);
                params.put("new_password", new_password);
                Log.d("paramsforattendance",params.toString());
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
        queue.add(sr);
        progressDialog = new ProgressDialog(ResetPassword.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResetPassword.this,EmployeeDetailsActivity.class);
        intent.putExtra("userrole",roleval);
        intent.putExtra("idval",idval);
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
            Intent intent = new Intent(ResetPassword.this,EmployeeDetailsActivity.class);
            intent.putExtra("userrole",roleval);
            intent.putExtra("idval",idval);
            intent.putExtra("access_token",access_token);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

}
