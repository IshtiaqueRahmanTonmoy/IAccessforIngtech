package iaccess.iaccess.com.iaccess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import iaccess.iaccess.com.entity.Employee;

public class AttendanceDetails extends AppCompatActivity {

    private TextView nameTxt,dateTxt,idTxt,descriptionTxt,outTxt,workingHourTxt,overtimeTxt,remarksTxt,inmore,outmore;
    private Button inBtn,outBtn;
    Intent intent;
    private StringRequest stringRequest;
    String years,months,days,mnth;
    private ProgressDialog progressDialog;
    String id,ids,name,date,intime,outtime,in_location,out_location,working_hour,overtime,remarks,timestamp,month,day,hour,timestampsout,hourout,fromto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_details);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        //Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();

        showDetails(id);

        nameTxt = (TextView) findViewById(R.id.name);
        dateTxt = (TextView) findViewById(R.id.date);
        idTxt = (TextView) findViewById(R.id.id);
        descriptionTxt = (TextView) findViewById(R.id.desciption);
        outTxt = (TextView) findViewById(R.id.tvouLoc);
        workingHourTxt = (TextView) findViewById(R.id.tvworkingvalue);
        overtimeTxt = (TextView) findViewById(R.id.tvOverValue);
        remarksTxt = (TextView) findViewById(R.id.tvRemarksvalue);

        inBtn = (Button) findViewById(R.id.View);
        outBtn = (Button) findViewById(R.id.View2);
        inmore = (TextView) findViewById(R.id.checkedinmore);
        outmore = (TextView) findViewById(R.id.checkedout);

        inmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(AttendanceDetails.this,MapActivity.class);
               intent.putExtra("locationin",descriptionTxt.getText().toString());
               startActivity(intent);
            }
        });

        outmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttendanceDetails.this,MapActivity.class);
                intent.putExtra("locationin",descriptionTxt.getText().toString());
                startActivity(intent);
            }
        });


    }

    private void showDetails(String id) {
        RequestQueue queue = Volley.newRequestQueue(AttendanceDetails.this);
        //this is the url where you want to send the request
        //TODO: replace with your own url to send request, as I am using my own localhost for this tutorial

        // Request a string response from the provided URL.
        stringRequest = new StringRequest(Request.Method.GET, "http://i-access.ingtechbd.com/api/access-logs/view/"+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("responsevalue", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                           // JSONArray j = jsonObject.getJSONArray("data");

                                try {
                                    //Getting json object
                                    JSONObject json = jsonObject.getJSONObject("data");
                                    ids = json.getString("id");

                                    //Toast.makeText(AttendanceHistoryActivity.this, ""+id, Toast.LENGTH_SHORT).show();
                                    in_location = json.getString("in_location");
                                    out_location = json.getString("out_location");

                                    working_hour = json.getString("working_hour");
                                    overtime = json.getString("overtime");
                                    remarks = json.getString("remarks");

                                    JSONObject jsonob = json.getJSONObject("user");
                                    name = jsonob.getString("name");

                                    //this is start of intime
                                    JSONObject jsonin = json.getJSONObject("in_time");
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

                                    String[] partss = timestamp.split(" ");
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

                                    //this is end of intime
                                    //this is begininig of out time
                                    JSONObject jsonout = json.getJSONObject("out_time");

                                    Log.d("jsonout",jsonout.toString());
                                    if(jsonout.isNull("null")) {
                                        timestampsout = jsonout.getString("date");
                                        String[] parts1 = timestampsout.split("-");

                                        years = parts1[0];
                                        months = parts1[1];
                                        days = parts1[2];

                                        Log.d("years",years+"months"+months+"days"+days);



                                        String[] partsss = timestampsout.split(" ");
                                        String times = partsss[1];

                                        Log.d("times", times);

                                        String[] timepartss = times.split(":");

                                        String hs = timepartss[0];
                                        int hrs = Integer.parseInt(hs);

                                        Log.d("hrs", String.valueOf(hrs));

                                        if (hrs == 1) {
                                            hourout = "1";
                                        }
                                        else if (hrs == 2) {
                                            hourout = "2";
                                        }
                                        else if (hrs == 3) {
                                            hourout = "3";
                                        }
                                        else if (hrs == 4) {
                                            hourout = "4";
                                        }
                                        else if (hrs == 5) {
                                            hourout = "5";
                                        }
                                        else if (hrs == 6) {
                                            hourout = "1";
                                        }
                                        else if (hrs == 7) {
                                            hourout = "7";
                                        }
                                        else if (hrs == 8) {
                                            hourout = "8";
                                        }
                                        else if (hrs == 9) {
                                            hourout = "9";
                                        }
                                        else if (hrs == 10) {
                                            hourout = "10";
                                        }
                                        else if (hrs == 11) {
                                            hourout = "11";
                                        }
                                        else if (hrs == 12) {
                                            hourout = "12";
                                        }
                                        else if (hrs == 13) {
                                            hourout = "1";
                                        } else if (hrs == 14) {
                                            hourout = "2";
                                        } else if (hrs == 15) {
                                            hourout = "3";
                                        } else if (hrs == 16) {
                                            hourout = "4";
                                        } else if (hrs == 17) {
                                            hourout = "5";
                                        } else if (hrs == 18) {
                                            hourout = "6";
                                        } else if (hrs == 19) {
                                            hourout = "7";
                                        } else if (hrs == 20) {
                                            hourout = "8";
                                        } else if (hrs == 21) {
                                            hourout = "9";
                                        } else if (hrs == 22) {
                                            hourout = "10";
                                        } else if (hrs == 23) {
                                            hourout = "11";
                                        } else {
                                            hourout = "12";
                                        }

                                        String minutes = timepartss[1];


                                        if (hrs <= 12) {
                                            outtime = hourout + ":" + minutes + "AM";
                                        } else {
                                            outtime = hourout + ":" + minutes + "PM";
                                        }
                                    }

                                    else{
                                        outtime = null;
                                    }
                                    Log.d("stringoutime",outtime);

                                    //this is end of out time
                                    fromto = intime+"-"+outtime;
                                    String y = days+","+months+","+years;

                                    nameTxt.setText(name);
                                    dateTxt.setText(v+","+month+","+years);
                                    idTxt.setText(ids);

                                    inBtn.setText(intime);
                                    outBtn.setText(outtime);

                                    descriptionTxt.setText(in_location);
                                    outTxt.setText(out_location);

                                    workingHourTxt.setText(working_hour);
                                    overtimeTxt.setText(overtime);
                                    remarksTxt.setText(remarks);
                                    //Log.d("fromto",fromto);




                                    //Toast.makeText(AttendanceHistoryActivity.this, ""+time, Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    //Toast.makeText(AllTransactionsActivity.this, "category"+category, Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
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
        queue.add(stringRequest);
        progressDialog = new ProgressDialog(AttendanceDetails.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
    }
}
