package iaccess.iaccess.com.iaccess;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddEventActivity extends AppCompatActivity {

    private EditText nameEdt,titleEdt,timeEdt,descriptionEdt;
    private RadioGroup radioGroup;
    private RadioButton radioDisplayButton;

    private Button submitBtn;
    private String name,title,time,description,timevalue,status,access_token,Authorization,roleval,valuenot,idval;
    private ProgressDialog progressDialog;
    private ScrollView scrollView;
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        idval = getIntent().getStringExtra("idvalue");
        roleval = getIntent().getStringExtra("userrole");
        valuenot = getIntent().getStringExtra("valuenotlist");

        //Toast.makeText(this, ""+valuenot, Toast.LENGTH_SHORT).show();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add Event");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        access_token = getIntent().getStringExtra("access_token");
        //Toast.makeText(AttendanceActivity.this, ""+access_token, Toast.LENGTH_SHORT).show();
        Authorization = "Bearer"+" "+access_token;

        titleEdt = (EditText) findViewById(R.id.titleEdt);
        timeEdt = (EditText) findViewById(R.id.timeEdt);
        descriptionEdt = (EditText) findViewById(R.id.descriptionEdt);

        submitBtn = (Button) findViewById(R.id.saveBtn);

        scrollView = (ScrollView) findViewById(R.id.scrollview);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                hideKeyboard(view);
            }
        });


        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);

        timeEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //DatePickerFragment mTimePicker = new DatePickerFragment();
                //DatePickerFragment.show(getFragmentManager(), "Select time");


                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getFragmentManager(), "Select time");
            }
        });


        titleEdt.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (titleEdt.getText().toString().length() <= 0) {
                    titleEdt.setError("Enter Title");
                } else {
                    titleEdt.setError(null);
                }
            }
        });

        timeEdt.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (timeEdt.getText().toString().length() <= 0) {
                    timeEdt.setError("Enter Time");
                } else {
                    timeEdt.setError(null);
                }
            }
        });
        descriptionEdt.addTextChangedListener(new TextWatcher()  {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s)  {
                if (descriptionEdt.getText().toString().length() <= 0) {
                    descriptionEdt.setError("Enter Description");
                } else {
                    descriptionEdt.setError(null);
                }
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(titleEdt.getText().toString().length() <= 0){
                     titleEdt.setError("Enter Title");
                }
                else if(timeEdt.getText().toString().length() <= 0){
                    timeEdt.setError("Enter Time");
                }
                else if(descriptionEdt.getText().toString().length() <= 0){
                    descriptionEdt.setError("Enter Description");
                }
                else{
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radioDisplayButton = (RadioButton) findViewById(selectedId);

                    if(radioDisplayButton.getText().equals("Public")){
                        status = "0";
                    }
                    else {
                        status = "1";
                    }

                    title = titleEdt.getText().toString();
                    time = timeEdt.getText().toString();
                    description = descriptionEdt.getText().toString();
                    AddValue();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(valuenot.equals("1")){
            Intent intent = new Intent(AddEventActivity.this,DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("userrole",roleval);
            intent.putExtra("idvalue",idval);
            intent.putExtra("acces_token",access_token);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(AddEventActivity.this,EmployeeListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("userrole",roleval);
            intent.putExtra("idvalue",idval);
            intent.putExtra("access_token",access_token);
            startActivity(intent);
            finish();
        }

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
            Intent intent = new Intent(AddEventActivity.this,DashboardActivity.class);
            intent.putExtra("userrole",roleval);
            intent.putExtra("acces_token",access_token);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
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
                timevalue = dateFormat.format(new Date()); // Find todays date
                timeEdt.setText(timevalue);
                //Log.d("currentdate",currentDateTime);
            } catch (Exception e) {
                e.printStackTrace();}
        }
    }

    private void AddValue() {

        //Log.d("val",dateEdt.getText().toString()+""+timeEdt.getText().toString());
        RequestQueue requestQueue = Volley.newRequestQueue(AddEventActivity.this);
          /*Post data*/
        Map<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("time", time);
        params.put("description", description);
        params.put("status",status);


        Log.d("paramsforattendance",params.toString());


        JsonObjectRequest postRequest = new JsonObjectRequest( Request.Method.POST, "http://i-access.ingtechbd.com/api/events/add",

                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        titleEdt.setText("");
                        timeEdt.setText("");
                        descriptionEdt.setText("");

                        titleEdt.setError(null);
                        timeEdt.setError(null);
                        descriptionEdt.setError(null);

                        radioGroup.setEnabled(false);
                        progressDialog.dismiss();
                        Toast.makeText(AddEventActivity.this, "Successfully added event.", Toast.LENGTH_SHORT).show();
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
        progressDialog = new ProgressDialog(AddEventActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }
}
