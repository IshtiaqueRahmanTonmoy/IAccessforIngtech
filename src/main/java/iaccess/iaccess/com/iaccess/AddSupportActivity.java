package iaccess.iaccess.com.iaccess;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddSupportActivity extends AppCompatActivity {

    private EditText organizationEdt,issueEdt,personEdt,descriptionEdt,remarksEdt,dateEdt,timeEdt;
    private Button submitBtn,cancelBtn;
    private String organization,support_issue,person,description,remarks,access_token,Authorization,roleval,idval,names,designations;
    String start_time,end_time;
    private ProgressDialog progressDialog;
    private ScrollView scrollView;
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_support);

        idval = getIntent().getStringExtra("idvalue");
        roleval = getIntent().getStringExtra("userrole");
        access_token = getIntent().getStringExtra("access_token");
        //Toast.makeText(AttendanceActivity.this, ""+access_token, Toast.LENGTH_SHORT).show();
        Authorization = "Bearer"+" "+access_token;

        names = getIntent().getStringExtra("namevalue");
        designations = getIntent().getStringExtra("designationvalue");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Support");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        organizationEdt = (EditText) findViewById(R.id.organizationeEdt);
        issueEdt = (EditText) findViewById(R.id.issueEdt);
        personEdt = (EditText) findViewById(R.id.personEdt);
        descriptionEdt = (EditText) findViewById(R.id.descriptionEdt);
        remarksEdt = (EditText) findViewById(R.id.remarksEdt);

        dateEdt = (EditText) findViewById(R.id.dateEdt);
        timeEdt = (EditText) findViewById(R.id.timeEdt);

        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getFragmentManager(), "Select time");
            }
        });

        timeEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EndtTimePicker mTimePicker1 = new EndtTimePicker();
                mTimePicker1.show(getFragmentManager(), "Select time");
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

        cancelBtn = (Button) findViewById(R.id.cancelBtn);

        submitBtn = (Button) findViewById(R.id.saveBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                organization = organizationEdt.getText().toString();
                support_issue = issueEdt.getText().toString();
                person = personEdt.getText().toString();
                description = descriptionEdt.getText().toString();
                remarks = remarksEdt.getText().toString();

                AddValue();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddSupportActivity.this,DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("userrole",roleval);
                intent.putExtra("idvalue",idval);
                intent.putExtra("acces_token",access_token);
                startActivity(intent);
                finish();
            }
        });
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
            Intent intent = new Intent(AddSupportActivity.this,DashboardActivity.class);
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
        Intent intent = new Intent(AddSupportActivity.this,DashboardActivity.class);
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

    @SuppressLint("ValidFragment")
    public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }
        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            //dateEdt.setText("Selected Time: " + String.valueOf(hourOfDay) + " : " + String.valueOf(minute));
            try {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                start_time = dateFormat.format(new Date()); // Find todays date
                dateEdt.setText(String.format("%02d",hourOfDay)+":"+String.format("%02d",minute));
                //Log.d("currentdate",currentDateTime);
            } catch (Exception e) {
                e.printStackTrace();}
        }
    }


    @SuppressLint("ValidFragment")
    public class EndtTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour1 = c.get(Calendar.HOUR_OF_DAY);
            int minute1 = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour1, minute1, DateFormat.is24HourFormat(getActivity()));
        }
        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay1, int minute1) {
            //dateEdt.setText("Selected Time: " + String.valueOf(hourOfDay) + " : " + String.valueOf(minute));
            try {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                end_time = dateFormat.format(new Date()); // Find todays date
                timeEdt.setText(String.format("%02d",hourOfDay1)+":"+String.format("%02d",minute1));
                //Log.d("currentdate",currentDateTime);
            } catch (Exception e) {
                e.printStackTrace();}
        }
    }

    private void AddValue() {

        Log.d("val",dateEdt.getText().toString()+""+timeEdt.getText().toString());
        RequestQueue requestQueue = Volley.newRequestQueue(AddSupportActivity.this);
          /*Post data*/
        Map<String, String> params = new HashMap<>();
        params.put("organization", organization);
        params.put("start_time", dateEdt.getText().toString());
        params.put("end_time", timeEdt.getText().toString());
        params.put("person",person);
        params.put("description",description);
        //params.put("avatar",avatar);
        params.put("remarks",remarks);

        Log.d("paramsforattendance",params.toString());


        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST, "http://i-access.ingtechbd.com/api/supports/add",

                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        organizationEdt.setText("");
                        issueEdt.setText("");
                        dateEdt.setText("");
                        timeEdt.setText("");
                        personEdt.setText("");
                        descriptionEdt.setText("");
                        remarksEdt.setText("");
                        progressDialog.dismiss();

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
                headers.put("Authorization",Authorization);
                //headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjViYjJlYjI2ZDhhNmEwZjA5ZTE5ZmQyZTk1ZmExNWIxZDhjZmQwYWRhZjYxYTJjZDRlMTA4NmY3OGZjZWJjNzlhYzNlOTRhYmQ1YmQzODUzIn0.eyJhdWQiOiIyIiwianRpIjoiNWJiMmViMjZkOGE2YTBmMDllMTlmZDJlOTVmYTE1YjFkOGNmZDBhZGFmNjFhMmNkNGUxMDg2Zjc4ZmNlYmM3OWFjM2U5NGFiZDViZDM4NTMiLCJpYXQiOjE1MTc4MTg5NjksIm5iZiI6MTUxNzgxODk2OSwiZXhwIjoxNTQ5MzU0OTY5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.IrunbaEHmpxGwJTnJ9tUuibBjYNfgWimN2hxHSoFS33r-bcVb8MMIVlYY72vbsfhxMKnEf8k1ds0IJ65uCg8IFO-sEsqA_bpomY1IhLradgoX7TwBKv_iqYLLQ3zMMyjaiUYEHcrFTIJxn4A80YTjtXfekGquDVUczFoVVhUMumhVWaJ23bZuqD2ujZDwg2CyZy3ABlg9VT30qmwVxOY_ThfVCIll69onrZyLVVzNC_rvJPTzzD0Hb827VnMBLRN6vv7cBme9wasBJzq6ab4Ys9IFn4j7JtVRoWHf_wVxgjeDPo2clggWt_KqAP2rU2ORBrQCYXk0TKwhzRtck2aczcOZcJLBnmOSaj3-1zw8gGXwNyLi-8a4h4A6aQzXZQpQTs9BNt-cnaP6LVr1Et-yMtjMpEMpzPcgt11vTxjFKVbWdbmV41445T9EtaaOLUMTM3m0STfsNJTOvl-bOtIoYNuTmXD5uNn69b6HcpiIdsqFwljffc_uPGvzXg9ddko286YVAVP5dSX7BC8WrX21sktrMreln7sbHQI4dzfs2y2k07nknlCO1AaXJjaqpFn4w3kfKSOcPhZ7ngf4WfZ8qGYXRRNbvs_Xs1HvPkIe2XxZ2u1L8b9mAWbEt5cC3YcKGPd4cVTK3qXFZLgc5yD-AZiFXSIiqjDZ3nfdSW0Hyw");
                return headers;
            }
        };
        requestQueue.add(postRequest);
        progressDialog = new ProgressDialog(AddSupportActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }
}
