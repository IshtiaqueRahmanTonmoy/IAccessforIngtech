package iaccess.iaccess.com.iaccess;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    CardView cardview,eventcardview,employeecardview,attendanceardview,notificationcardview,helpcardview;
    TextView nameText,designationText;
    String roleval,acces_token,idval;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        */

        idval = getIntent().getStringExtra("idvalue");

        //Toast.makeText(this, ""+idval, Toast.LENGTH_SHORT).show();
        roleval = getIntent().getStringExtra("userrole");
        acces_token = getIntent().getStringExtra("acces_token");
        //Toast.makeText(DashboardActivity.this, ""+acces_token, Toast.LENGTH_SHORT).show();

       cardview = (CardView) findViewById(R.id.supports);
       eventcardview = (CardView) findViewById(R.id.events);
       employeecardview = (CardView) findViewById(R.id.employees);
       attendanceardview = (CardView) findViewById(R.id.attendance);

        notificationcardview = (CardView) findViewById(R.id.notification);
        helpcardview = (CardView) findViewById(R.id.help);

        cardview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(DashboardActivity.this,SupportHistory.class);
               intent.putExtra("idvalue",idval);
               intent.putExtra("userrole",roleval);
               intent.putExtra("access_token",acces_token);
               startActivity(intent);

           }
       });

       eventcardview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(DashboardActivity.this,EventListActivity.class);
               intent.putExtra("userrole",roleval);
               intent.putExtra("idvalue",idval);
               intent.putExtra("access_token",acces_token);
               startActivity(intent);
               //finish();
           }
       });

        employeecardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this,EmployeeListActivity.class);
                intent.putExtra("userrole",roleval);
                intent.putExtra("idvalue",idval);
                intent.putExtra("access_token",acces_token);
                startActivity(intent);
                //finish();
            }
        });

        attendanceardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this,AttendanceHistoryActivity.class);
                intent.putExtra("access_token",acces_token);
                intent.putExtra("idvalue",idval);
                intent.putExtra("userrole",roleval);
                startActivity(intent);
                //finish();
            }
        });

        notificationcardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "On build process", Toast.LENGTH_SHORT).show();
            }
        });

        helpcardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "On build process", Toast.LENGTH_SHORT).show();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(DashboardActivity.this, view);
                //Inflating the Popup using xml file

                if(roleval.equals("user")) {
                    popup.getMenuInflater().inflate(R.menu.button_menu1, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            Log.d("attendance", item.getTitle().toString());

                            if (item.getTitle().toString().equals("Attendance")) {
                                Intent intent = new Intent(DashboardActivity.this, AttendanceActivity.class);
                                intent.putExtra("idvalue",idval);
                                intent.putExtra("access_token",acces_token);
                                intent.putExtra("userrole",roleval);
                                startActivity(intent);
                            }
                            else if (item.getTitle().toString().equals("Event")) {
                                Intent intent = new Intent(DashboardActivity.this, AddEventActivity.class);
                                intent.putExtra("idvalue",idval);
                                intent.putExtra("access_token",acces_token);
                                intent.putExtra("userrole",roleval);
                                startActivity(intent);
                            }
                            else if (item.getTitle().toString().equals("Support")) {
                                Intent intent = new Intent(DashboardActivity.this, AddSupportActivity.class);
                                intent.putExtra("access_token",acces_token);
                                intent.putExtra("idvalue",idval);
                                intent.putExtra("userrole",roleval);
                                startActivity(intent);
                            }
                            return true;
                        }
                    });
                }
                else{
                    popup.getMenuInflater().inflate(R.menu.button_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            Log.d("attendance", item.getTitle().toString());

                            if (item.getTitle().toString().equals("Attendance")) {
                                Intent intent = new Intent(DashboardActivity.this, AttendanceActivity.class);
                                intent.putExtra("idvalue",idval);
                                intent.putExtra("access_token",acces_token);
                                intent.putExtra("userrole",roleval);
                                startActivity(intent);
                            }  else if (item.getTitle().toString().equals("Event")) {
                                Intent intent = new Intent(DashboardActivity.this, AddEventActivity.class);
                                intent.putExtra("idvalue",idval);
                                intent.putExtra("access_token",acces_token);
                                intent.putExtra("userrole",roleval);
                                intent.putExtra("valuenotlist","1");
                                startActivity(intent);
                                finish();
                            } else if (item.getTitle().toString().equals("Employee")) {
                                Intent intent = new Intent(DashboardActivity.this, AddEmployeeActivity.class);
                                intent.putExtra("userrole",roleval);
                                intent.putExtra("idvalue",idval);
                                intent.putExtra("access_token",acces_token);
                                startActivity(intent);
                            }
                            return true;
                        }
                    });

                }
                popup.show();//showing popup menu
                //return super.onOptionsItemSelected(item);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        nameText = (TextView) headerView.findViewById(R.id.nameNav);
        designationText = (TextView) headerView.findViewById(R.id.designationNav);

        nameText.setText("Nur A Alam");
        designationText.setText("Developer");

        Menu menu =navigationView.getMenu();

        if(roleval.equals("user")) {

            MenuItem target1 = menu.findItem(R.id.nav_employees);
            target1.setVisible(false);
        }


        if(roleval.equals("admin")) {
            MenuItem target = menu.findItem(R.id.nav_employees);
            target.setVisible(true);

            MenuItem target1 = menu.findItem(R.id.nav_profile);
            target1.setVisible(true);
        }

        else if(roleval.equals("user")){
            MenuItem target = menu.findItem(R.id.nav_employees);
            target.setVisible(false);

            MenuItem target1 = menu.findItem(R.id.nav_profile);
            target1.setVisible(true);
        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_event) {
            Intent intent = new Intent(DashboardActivity.this, EventListActivity.class);
            intent.putExtra("accesstoken",acces_token);
            intent.putExtra("userrole",roleval);
            startActivity(intent);
            finish();
            return true;
        }
        else if (id == R.id.action_logout) {

            SharedPreferences SM = getSharedPreferences("userrecord", 0);
            SharedPreferences.Editor edit = SM.edit();
            edit.putBoolean("userlogin", false);
            edit.commit();

            Intent intent = new Intent(DashboardActivity.this,SignInActivity.class);
            startActivity(intent);
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            // Handle the camera action
        } else if (id == R.id.nav_history) {
            //Toast.makeText(this, ""+acces_token, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DashboardActivity.this,AttendanceHistoryActivity.class);
            intent.putExtra("userrole",roleval);
            intent.putExtra("access_token",acces_token);
            startActivity(intent);
        } else if (id == R.id.nav_supporthistory) {
            Intent intent = new Intent(DashboardActivity.this,SupportHistory.class);
            intent.putExtra("userrole",roleval);
            intent.putExtra("access_token",acces_token);
            startActivity(intent);
        }  else if (id == R.id.nav_profile) {
            Intent intent = new Intent(DashboardActivity.this,ProfileActivity.class);
            intent.putExtra("idval",idval);
            intent.putExtra("access_token",acces_token);
            intent.putExtra("userrole",roleval);
            startActivity(intent);
        } else if (id == R.id.nav_employees) {
            Intent intent = new Intent(DashboardActivity.this,EmployeeListActivity.class);
            intent.putExtra("userrole",roleval);
            intent.putExtra("access_token",acces_token);
            startActivity(intent);
        }
        else if (id == R.id.nav_logout) {
            SharedPreferences SM = getSharedPreferences("userrecord", 0);
            SharedPreferences.Editor edit = SM.edit();
            edit.putBoolean("userlogin", false);
            edit.commit();

            Intent intent = new Intent(DashboardActivity.this,SignInActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
