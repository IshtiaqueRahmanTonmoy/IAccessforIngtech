package iaccess.iaccess.com.iaccess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
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

public class EventDetailsActivity extends AppCompatActivity {

    private String idval;
    private TextView nameTxt,createdonTxt,idTxt,dateTxt,descriptionTxt;
    private Button timeBtn;
    private StringRequest stringRequest;
    private String idvalues,name,createdon,date,description,times,month,day,month1,hour,intime,roleval,access_token,Authorization,names,designations;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        //EventDetailsActivity.this.setTitle("Event Details");

        idval = getIntent().getStringExtra("idvalue");
        roleval = getIntent().getStringExtra("userrole");

        access_token = getIntent().getStringExtra("access_token");
        //Toast.makeText(AttendanceActivity.this, ""+access_token, Toast.LENGTH_SHORT).show();
        Authorization = "Bearer"+" "+access_token;

        names = getIntent().getStringExtra("namevalue");
        designations = getIntent().getStringExtra("designationvalue");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Event Details");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //idval = getIntent().getStringExtra("id");
        //Toast.makeText(EventDetailsActivity.this, ""+idval, Toast.LENGTH_SHORT).show();

        nameTxt= (TextView) findViewById(R.id.name);
        idTxt= (TextView) findViewById(R.id.id);
        createdonTxt= (TextView) findViewById(R.id.createdon);

        dateTxt= (TextView) findViewById(R.id.tvDate);
        descriptionTxt= (TextView) findViewById(R.id.tvDescription);

        timeBtn = (Button) findViewById(R.id.tvTime);

        getValue(idval);

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
            Intent intent = new Intent(EventDetailsActivity.this,EventListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("idvalue",idval);
            intent.putExtra("userrole",roleval);
            intent.putExtra("namevalue",names);
            intent.putExtra("designationvalue",designations);
            intent.putExtra("access_token",access_token);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EventDetailsActivity.this,EventListActivity.class);
        intent.putExtra("userrole",roleval);
        intent.putExtra("namevalue",names);
        intent.putExtra("designationvalue",designations);
        intent.putExtra("access_token",access_token);
        startActivity(intent);
        finish();
    }

    private void getValue(String id) {
        RequestQueue queue = Volley.newRequestQueue(EventDetailsActivity.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, "http://i-access.ingtechbd.com/api/events/view/"+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responseeventdetails", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject json = jsonObject.getJSONObject("data");

                            //Getting json object
                            //Toast.makeText(AttendanceHistoryActivity.this, ""+id, Toast.LENGTH_SHORT).show();

                            idvalues = json.getString("id");
                            description = json.getString("description");
                            times = json.getString("time");

                            String[] parts1 = times.split("-");

                            String year1 = parts1[0];
                            String mth1 = parts1[1];
                            String d1 = parts1[2];

                            String[] da1 = d1.split(" ");
                            String v1 = da1[0];
                            Log.d("va",v1);
                            //int m = Integer.parseInt(mth);

                            if(mth1.equals("01")){
                                month1 = "January";
                            }
                            else if(mth1.equals("02")){
                                month1 = "February";
                            }
                            else if(mth1.equals("03")){
                                month1 = "March";
                            }
                            else if(mth1.equals("04")){
                                month1 = "April";
                            }
                            else if(mth1.equals("05")){
                                month1 = "May";
                            }
                            else if(mth1.equals("06")){
                                month1 = "June";
                            }
                            else if(mth1.equals("07")){
                                month1 = "July";
                            }
                            else if(mth1.equals("08")){
                                month1 = "August";
                            }
                            else if(mth1.equals("09")){
                                month1 = "September";
                            }
                            else if(mth1.equals("10")){
                                month1 = "October";
                            }
                            else if(mth1.equals("11")){
                                month1 = "November";
                            }
                            else{
                                month1 = "December";
                            }

                            //day1 = parts[2];
                            String date =v1+" , " + month1 + " , " + year1;
                            Log.d("time",v1+""+month1+""+year1);

                            String[] partss = times.split(" ");
                            String time = partss[1];

                            String[] timeparts = time.split(":");

                            String h = timeparts[0];
                            int hr = Integer.parseInt(h);

                            if (hr == 1) {
                                hour = "1";
                            }
                            else if (hr == 2) {
                                hour = "2";
                            }
                            else if (hr == 3) {
                                hour = "3";
                            }
                            else if (hr == 4) {
                                hour = "4";
                            }
                            else if (hr == 5) {
                                hour = "5";
                            }
                            else if (hr == 6) {
                                hour = "1";
                            }
                            else if (hr == 7) {
                                hour = "7";
                            }
                            else if (hr == 8) {
                                hour = "8";
                            }
                            else if (hr == 9) {
                                hour = "9";
                            }
                            else if (hr == 10) {
                                hour = "10";
                            }
                            else if (hr == 11) {
                                hour = "11";
                            }
                            else if (hr == 12) {
                                hour = "12";
                            }
                            else if (hr == 13) {
                                hour = "1";
                            } else if (hr == 14) {
                                hour = "2";
                            } else if (hr == 15) {
                                hour = "3";
                            } else if (hr == 16) {
                                hour = "4";
                            } else if (hr == 17) {
                                hour = "5";
                            } else if (hr == 18) {
                                hour = "6";
                            } else if (hr == 19) {
                                hour = "7";
                            } else if (hr == 20) {
                                hour = "8";
                            } else if (hr == 21) {
                                hour = "9";
                            } else if (hr == 22) {
                                hour = "10";
                            } else if (hr == 23) {
                                hour = "11";
                            } else {
                                hour = "12";
                            }


                            String minute = timeparts[1];

                            if(hr<=12){
                                intime = hour+":"+minute+"AM";
                            }
                            else{
                                intime = hour+":"+minute+"PM";
                            }

                            Log.d("intime",intime);




                            Log.d("valuevalues",idvalues+""+description+""+times);

                            JSONObject jsonob = json.getJSONObject("user");
                            name = jsonob.getString("name");

                            Log.d("name",name);

                            JSONObject jsonobs = json.getJSONObject("created_at");
                            createdon = jsonobs.getString("date");

                            Log.d("createdon",createdon);

                            String[] parts = createdon.split("-");

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

                            String crton = v+"," + month + "," + year;
                            Log.d("vlaues",v+" , " + month+" , " + year);

                            progressDialog.dismiss();

                            idTxt.setText(idvalues);
                            timeBtn.setText(intime);
                            nameTxt.setText(name);
                            createdonTxt.setText(crton);
                            dateTxt.setText(date);
                            descriptionTxt.setText(description);

                            /*
                            emailTxt.setText(email);
                            phoneTxt.setText(phone);
                            designationTxt.setText(designaton);
                            genderTxt.setText(gender);
                            addressTxt.setText(address);
                            */
                            //Log.d("id", id + "name" + name + "email" + email);


                            //Toast.makeText(AttendanceHistoryActivity.this, ""+time, Toast.LENGTH_SHORT).show();
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
                // headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjViYjJlYjI2ZDhhNmEwZjA5ZTE5ZmQyZTk1ZmExNWIxZDhjZmQwYWRhZjYxYTJjZDRlMTA4NmY3OGZjZWJjNzlhYzNlOTRhYmQ1YmQzODUzIn0.eyJhdWQiOiIyIiwianRpIjoiNWJiMmViMjZkOGE2YTBmMDllMTlmZDJlOTVmYTE1YjFkOGNmZDBhZGFmNjFhMmNkNGUxMDg2Zjc4ZmNlYmM3OWFjM2U5NGFiZDViZDM4NTMiLCJpYXQiOjE1MTc4MTg5NjksIm5iZiI6MTUxNzgxODk2OSwiZXhwIjoxNTQ5MzU0OTY5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.IrunbaEHmpxGwJTnJ9tUuibBjYNfgWimN2hxHSoFS33r-bcVb8MMIVlYY72vbsfhxMKnEf8k1ds0IJ65uCg8IFO-sEsqA_bpomY1IhLradgoX7TwBKv_iqYLLQ3zMMyjaiUYEHcrFTIJxn4A80YTjtXfekGquDVUczFoVVhUMumhVWaJ23bZuqD2ujZDwg2CyZy3ABlg9VT30qmwVxOY_ThfVCIll69onrZyLVVzNC_rvJPTzzD0Hb827VnMBLRN6vv7cBme9wasBJzq6ab4Ys9IFn4j7JtVRoWHf_wVxgjeDPo2clggWt_KqAP2rU2ORBrQCYXk0TKwhzRtck2aczcOZcJLBnmOSaj3-1zw8gGXwNyLi-8a4h4A6aQzXZQpQTs9BNt-cnaP6LVr1Et-yMtjMpEMpzPcgt11vTxjFKVbWdbmV41445T9EtaaOLUMTM3m0STfsNJTOvl-bOtIoYNuTmXD5uNn69b6HcpiIdsqFwljffc_uPGvzXg9ddko286YVAVP5dSX7BC8WrX21sktrMreln7sbHQI4dzfs2y2k07nknlCO1AaXJjaqpFn4w3kfKSOcPhZ7ngf4WfZ8qGYXRRNbvs_Xs1HvPkIe2XxZ2u1L8b9mAWbEt5cC3YcKGPd4cVTK3qXFZLgc5yD-AZiFXSIiqjDZ3nfdSW0Hyw");
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        progressDialog = new ProgressDialog(EventDetailsActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }
}
