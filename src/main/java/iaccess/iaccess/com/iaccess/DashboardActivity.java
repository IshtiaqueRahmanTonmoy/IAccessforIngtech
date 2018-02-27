package iaccess.iaccess.com.iaccess;

import android.content.Intent;
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
    CardView cardview,eventcardview;
    TextView nameText,designationText;
    String roleval,acces_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        roleval = getIntent().getStringExtra("userrole");
        //acces_token = getIntent().getStringExtra("acces_token");
        //Toast.makeText(DashboardActivity.this, ""+roleval, Toast.LENGTH_SHORT).show();

       cardview = (CardView) findViewById(R.id.supports);
       eventcardview = (CardView) findViewById(R.id.events);

       cardview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(DashboardActivity.this,AddSupportActivity.class);
               startActivity(intent);
           }
       });

       eventcardview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(DashboardActivity.this,AddEventActivity.class);
               startActivity(intent);
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
                                intent.putExtra("rolevalue",roleval);
                                startActivity(intent);
                            } else if (item.getTitle().toString().equals("Notification")) {
                                //Intent intent = new Intent(DashboardActivity.this,AddNotification.class);
                                //startActivity(intent);
                            } else if (item.getTitle().toString().equals("Event")) {
                                Intent intent = new Intent(DashboardActivity.this, EventListActivity.class);
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
                                startActivity(intent);
                            } else if (item.getTitle().toString().equals("Notification")) {
                                //Intent intent = new Intent(DashboardActivity.this,AddNotification.class);
                                //startActivity(intent);
                            } else if (item.getTitle().toString().equals("Event")) {
                                Intent intent = new Intent(DashboardActivity.this, EventListActivity.class);
                                startActivity(intent);
                            } else if (item.getTitle().toString().equals("Employee")) {
                                Intent intent = new Intent(DashboardActivity.this, AddEmployeeActivity.class);
                                intent.putExtra("rolevalue",roleval);
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
            MenuItem target = menu.findItem(R.id.nav_employees);
            target.setVisible(false);

            //MenuItem target1 = menu.findItem(R.id.employee);
            //target1.setVisible(false);
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
        else if (id == R.id.action_logout) {
            Intent intent = new Intent(DashboardActivity.this,SignInActivity.class);
            startActivity(intent);
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
            Intent intent = new Intent(DashboardActivity.this,AttendanceHistoryActivity.class);
            intent.putExtra("rolevalue",roleval);
            startActivity(intent);
        } else if (id == R.id.nav_supporthistory) {
            Intent intent = new Intent(DashboardActivity.this,SupportHistory.class);
            startActivity(intent);
        } else if (id == R.id.nav_salarystatement) {

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(DashboardActivity.this,ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_employees) {
            Intent intent = new Intent(DashboardActivity.this,EmployeeListActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
