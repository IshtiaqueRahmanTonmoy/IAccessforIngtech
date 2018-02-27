package iaccess.iaccess.com.iaccess;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
    private String name,title,time,description,timevalue,status;
    private ProgressDialog progressDialog;
    private ScrollView scrollView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

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
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getFragmentManager(), "Select time");
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioDisplayButton = (RadioButton) findViewById(selectedId);

                if(radioDisplayButton.getText().equals("Public")){
                    status = "0";
                }
                else {
                    status = "1";
                }
                //Toast.makeText(AddEventActivity.this,
                //        radioDisplayButton.getText(), Toast.LENGTH_SHORT).show();


                title = titleEdt.getText().toString();
                time = timeEdt.getText().toString();
                description = descriptionEdt.getText().toString();
                //AddValue();
            }
        });
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
                headers.put("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjViYjJlYjI2ZDhhNmEwZjA5ZTE5ZmQyZTk1ZmExNWIxZDhjZmQwYWRhZjYxYTJjZDRlMTA4NmY3OGZjZWJjNzlhYzNlOTRhYmQ1YmQzODUzIn0.eyJhdWQiOiIyIiwianRpIjoiNWJiMmViMjZkOGE2YTBmMDllMTlmZDJlOTVmYTE1YjFkOGNmZDBhZGFmNjFhMmNkNGUxMDg2Zjc4ZmNlYmM3OWFjM2U5NGFiZDViZDM4NTMiLCJpYXQiOjE1MTc4MTg5NjksIm5iZiI6MTUxNzgxODk2OSwiZXhwIjoxNTQ5MzU0OTY5LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.IrunbaEHmpxGwJTnJ9tUuibBjYNfgWimN2hxHSoFS33r-bcVb8MMIVlYY72vbsfhxMKnEf8k1ds0IJ65uCg8IFO-sEsqA_bpomY1IhLradgoX7TwBKv_iqYLLQ3zMMyjaiUYEHcrFTIJxn4A80YTjtXfekGquDVUczFoVVhUMumhVWaJ23bZuqD2ujZDwg2CyZy3ABlg9VT30qmwVxOY_ThfVCIll69onrZyLVVzNC_rvJPTzzD0Hb827VnMBLRN6vv7cBme9wasBJzq6ab4Ys9IFn4j7JtVRoWHf_wVxgjeDPo2clggWt_KqAP2rU2ORBrQCYXk0TKwhzRtck2aczcOZcJLBnmOSaj3-1zw8gGXwNyLi-8a4h4A6aQzXZQpQTs9BNt-cnaP6LVr1Et-yMtjMpEMpzPcgt11vTxjFKVbWdbmV41445T9EtaaOLUMTM3m0STfsNJTOvl-bOtIoYNuTmXD5uNn69b6HcpiIdsqFwljffc_uPGvzXg9ddko286YVAVP5dSX7BC8WrX21sktrMreln7sbHQI4dzfs2y2k07nknlCO1AaXJjaqpFn4w3kfKSOcPhZ7ngf4WfZ8qGYXRRNbvs_Xs1HvPkIe2XxZ2u1L8b9mAWbEt5cC3YcKGPd4cVTK3qXFZLgc5yD-AZiFXSIiqjDZ3nfdSW0Hyw");
                return headers;
            }
        };

        requestQueue.add(postRequest);
        progressDialog = new ProgressDialog(AddEventActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }
}
