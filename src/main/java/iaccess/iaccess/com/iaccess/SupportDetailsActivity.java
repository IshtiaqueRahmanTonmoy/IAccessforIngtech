package iaccess.iaccess.com.iaccess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import iaccess.iaccess.com.entity.Support;

public class SupportDetailsActivity extends AppCompatActivity {

    Intent intent;
    String id;
    private TextView namevalues,datevalue,idvalues,organizationame,supportissue,personname,description,remarksvalue;
    private String idval,roleval,user_id,organization,start_time,end_time,name,timestamp,month,day,fromto,descrpt,remarks,access_token,Authorization;
    private StringRequest stringRequest;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_details);

        idval = getIntent().getStringExtra("idvalue");

        //Toast.makeText(this, ""+idval, Toast.LENGTH_SHORT).show();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Support Details");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        intent = getIntent();
        id = intent.getStringExtra("id");
        showDetails(id);

        roleval = getIntent().getStringExtra("userrole");
        access_token = getIntent().getStringExtra("access_token");
        //Toast.makeText(AttendanceActivity.this, ""+access_token, Toast.LENGTH_SHORT).show();
        Authorization = "Bearer"+" "+access_token;
        //Toast.makeText(this, ""+ Authorization, Toast.LENGTH_SHORT).show();

        namevalues = (TextView) findViewById(R.id.name);
        datevalue = (TextView) findViewById(R.id.date);
        idvalues = (TextView) findViewById(R.id.id);
        organizationame = (TextView) findViewById(R.id.tvorganizationname);
        supportissue = (TextView) findViewById(R.id.tvissue);
        personname = (TextView) findViewById(R.id.tvName);
        description = (TextView) findViewById(R.id.tvdescriptionvalue);
        remarksvalue = (TextView) findViewById(R.id.tvRemarksvalues);

        //Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SupportDetailsActivity.this,SupportHistory.class);
        intent.putExtra("userrole",roleval);
        intent.putExtra("idvalue",idval);
        intent.putExtra("access_token",access_token);
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
            Intent intent = new Intent(SupportDetailsActivity.this,SupportHistory.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("userrole",roleval);
            intent.putExtra("idvalue",idval);
            intent.putExtra("access_token",access_token);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    private void showDetails(String id) {
        RequestQueue queue1 = Volley.newRequestQueue(SupportDetailsActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, "http://i-access.ingtechbd.com/api/supports/view/"+idval,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsedetailsvalues", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //JSONArray j = jsonObject.getJSONArray("data");
                            for(int i=0;i<jsonObject.length();i++){
                                try {
                                    //Getting json object
                                    JSONObject json = jsonObject.getJSONObject("data");
                                    user_id = json.getString("user_id");
                                    remarks = json.getString("remarks");
                                    organization = json.getString("organization");
                                    start_time = json.getString("start_time");
                                    end_time = json.getString("end_time");
                                    descrpt = json.getString("description");

                                    JSONObject jsonob = json.getJSONObject("user");
                                    name = jsonob.getString("name");

                                    Log.d("name",name);

                                    //this is start of intime
                                    JSONObject jsonin = json.getJSONObject("created_at");
                                    timestamp = jsonin.getString("date");
                                    String[] parts = timestamp.split("-");

                                    String year = parts[0];
                                    String mth = parts[1];
                                    String d = parts[2];

                                    String[] da = d.split(" ");
                                    String v = da[0];
                                    Log.d("va",v);
                                    //int m = Integer.parseInt(mth);

                                    if(mth.equals("01")){
                                        month = "January";
                                    }
                                    else if(mth.equals("02")){
                                        month = "February";
                                    }
                                    else if(mth.equals("03")){
                                        month = "March";
                                    }
                                    else if(mth.equals("04")){
                                        month = "April";
                                    }
                                    else if(mth.equals("05")){
                                        month = "May";
                                    }
                                    else if(mth.equals("06")){
                                        month = "June";
                                    }
                                    else if(mth.equals("07")){
                                        month = "July";
                                    }
                                    else if(mth.equals("08")){
                                        month = "August";
                                    }
                                    else if(mth.equals("09")){
                                        month = "September";
                                    }
                                    else if(mth.equals("10")){
                                        month = "October";

                                    }
                                    else if(mth.equals("11")){
                                        month = "November";

                                    }
                                    else{
                                        month = "December";
                                    }

                                    day = parts[2];
                                    //Log.d("day",pd);

                                    //this is end of intime
                                    //this is end of out time
                                    fromto = start_time+"-"+end_time;

                                    //Log.d("values",user_id+name+month+day+fromto+organization);


                                    idvalues.setText(user_id);
                                    namevalues.setText(name);
                                    datevalue.setText(v+","+month+","+year);
                                    organizationame.setText(organization);
                                    supportissue.setText(descrpt);
                                    namevalues.setText(name);
                                    description.setText(descrpt);
                                    remarksvalue.setText(remarks);

                                    //Toast.makeText(AttendanceHistoryActivity.this, ""+time, Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    //Toast.makeText(SupportHistory.this, "category"+supportList.get(i).getName(), Toast.LENGTH_SHORT).show();

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
        queue1.add(stringRequest);
        progressDialog = new ProgressDialog(SupportDetailsActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }
}
