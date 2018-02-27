package iaccess.iaccess.com.iaccess;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class EmployeePasswordReset extends AppCompatActivity {

    private EditText oldpass,newpass,retypepass;
    private Button submitBtn;
    private String old_password,new_password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_password_reset);

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
                RequestQueue queue = Volley.newRequestQueue(EmployeePasswordReset.this);
                StringRequest sr = new StringRequest(Request.Method.POST,"http://i-access.ingtechbd.com/api/users/password-reset/1", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsevalue",response);
                        //mPostCommentResponse.requestCompleted();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("erros", String.valueOf(error));
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
                        headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjViYjJlYjI2ZDhhNmEwZjA5ZTE5ZmQyZTk1ZmExNWIxZDhjZmQwYWRhZjYxYTJjZDRlMTA4NmY3OGZjZWJjNzlhYzNlOTRhYmQ1YmQzODUzIn0.eyJhdWQiOiIyIiwianRpIjoiNWJiMmViMjZkOGE2YTBmMDllMTlmZDJlOTVmYTE1YjFkOGNmZDBhZGFmNjFhMmNkNGUxMDg2Zjc4ZmNlYmM3OWFjM2U5NGFiZDViZDM4NTMiLCJpYXQiOjE1MTc4MTg5NjksIm5iZiI6MTUxNzgxODk2OSwiZXhwIjoxNTQ5MzU0OTY5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.IrunbaEHmpxGwJTnJ9tUuibBjYNfgWimN2hxHSoFS33r-bcVb8MMIVlYY72vbsfhxMKnEf8k1ds0IJ65uCg8IFO-sEsqA_bpomY1IhLradgoX7TwBKv_iqYLLQ3zMMyjaiUYEHcrFTIJxn4A80YTjtXfekGquDVUczFoVVhUMumhVWaJ23bZuqD2ujZDwg2CyZy3ABlg9VT30qmwVxOY_ThfVCIll69onrZyLVVzNC_rvJPTzzD0Hb827VnMBLRN6vv7cBme9wasBJzq6ab4Ys9IFn4j7JtVRoWHf_wVxgjeDPo2clggWt_KqAP2rU2ORBrQCYXk0TKwhzRtck2aczcOZcJLBnmOSaj3-1zw8gGXwNyLi-8a4h4A6aQzXZQpQTs9BNt-cnaP6LVr1Et-yMtjMpEMpzPcgt11vTxjFKVbWdbmV41445T9EtaaOLUMTM3m0STfsNJTOvl-bOtIoYNuTmXD5uNn69b6HcpiIdsqFwljffc_uPGvzXg9ddko286YVAVP5dSX7BC8WrX21sktrMreln7sbHQI4dzfs2y2k07nknlCO1AaXJjaqpFn4w3kfKSOcPhZ7ngf4WfZ8qGYXRRNbvs_Xs1HvPkIe2XxZ2u1L8b9mAWbEt5cC3YcKGPd4cVTK3qXFZLgc5yD-AZiFXSIiqjDZ3nfdSW0Hyw");
                        return headers;
                    }
                };
                queue.add(sr);
                progressDialog = new ProgressDialog(EmployeePasswordReset.this);
                progressDialog.setMessage("Please wait....");
                progressDialog.show();
            }
        });
    }
}
