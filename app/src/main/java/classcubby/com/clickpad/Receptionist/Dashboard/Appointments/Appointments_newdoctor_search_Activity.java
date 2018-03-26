package classcubby.com.clickpad.Receptionist.Dashboard.Appointments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import classcubby.com.clickpad.AppStatus;
import classcubby.com.clickpad.MainActivity;
import classcubby.com.clickpad.R;
import classcubby.com.clickpad.configfiles.loginconfig;
import classcubby.com.clickpad.configfiles.previousloginconfig;

/**
 * Created by Vino on 3/14/2018.
 */

public class Appointments_newdoctor_search_Activity extends AppCompatActivity {

    TextView searchdetailtitle;
    ImageView leftarrow;
    EditText search;
    ListView patientlistview;
    View view;
    Snackbar snackbar;
    int patid;
    appointmentsearch_doctor_listview_adapter adapter;
    List<AppointmentsDoctorList> arraylist = new ArrayList<AppointmentsDoctorList>();
    List<AppointmentsDoctorList> newpatientlist;

    String userid,hospitalid;
    private JSONArray loginresult;
    ArrayList<String> arrdoctorid,arrdoctorname,arruserimage,arrdepartmentid,arrdepartmentname;
    String patientid,patientname,doctorid,doctorname,userimage,patientimage,mrnumber,departmentname,departmentid;
    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receptionist_dashboard_doctorsearch_xml);

        searchdetailtitle = (TextView) findViewById(R.id.searchdetailtitle);
        leftarrow = (ImageView) findViewById(R.id.leftarrow);
        search = (EditText) findViewById(R.id.search);
        patientlistview = (ListView) findViewById(R.id.patientlistview);
        view = (RelativeLayout) findViewById(R.id.patientlistsearchcontainer);

        Typeface normaltypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansMedium.ttf");

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        hospitalid = sharedPreferences.getString(previousloginconfig.key_hospitalid, "");

        searchdetailtitle.setText("Search for Doctor");
        searchdetailtitle.setTypeface(boldtypeface);
        search.setTypeface(normaltypeface);

        final Intent intent = getIntent();
        if (intent.hasExtra("Patientid")) {
            String Patientid = intent.getExtras().getString("Patientid");
            Log.e("Appointment Patient id",Patientid);
            patid = Integer.parseInt(Patientid);
            sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

            //creating editor to store values of shared preferences
            editor = sharedPreferences.edit();

            editor.putString(loginconfig.key_info_appointmentpatientid, String.valueOf(patid));
            editor.apply();
        }

        if (AppStatus.getInstance(Appointments_newdoctor_search_Activity.this).isOnline()) {
            getPatientInformation();
        }else {
            snackbar = Snackbar.make(view,"Please Connect to the Internet and Try Again",Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = search.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

    }



    private void getPatientInformation() {
        deleteCache(Appointments_newdoctor_search_Activity.this);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        userid = sharedPreferences.getString(loginconfig.key_userid, "");

        userid = userid.replace("[", "");
        userid = userid.replace("]", "");
        userid = userid.replace("\"", "");

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_search_dashboard_doctor_information_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
                            loginresult = j.getJSONArray("result");

                            if (loginresult.length() > 0) {

                                arrdoctorid = new ArrayList<String>(loginresult.length());
                                arrdepartmentid = new ArrayList<String>(loginresult.length());
                                arruserimage = new ArrayList<String>(loginresult.length());
                                arrdoctorname = new ArrayList<String>(loginresult.length());

                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);

                                    doctorid = json.optString("doctorid");
                                    doctorname = json.optString("name");
                                    departmentid = json.optString("departmentid");
                                    userimage = json.optString("userimage");

                                    arrdoctorid.add(doctorid);
                                    arrdoctorname.add(doctorname);
                                    arrdepartmentid.add(departmentid);
                                    arruserimage.add(userimage);
                                }


                                sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                //creating editor to store values of shared preferences
                                editor = sharedPreferences.edit();

                                editor.putString(loginconfig.key_info_doctorid, arrdoctorid.toString());
                                editor.putString(loginconfig.key_info_doctordepartment, arrdepartmentid.toString());
                                editor.putString(loginconfig.key_info_doctorimage, arruserimage.toString());
                                editor.putString(loginconfig.key_info_doctorname, arrdoctorname.toString());
                                editor.apply();


                                getDepartmentid();



                            }else{
                                snackbar = Snackbar.make(view,"Technical Error.",Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }

                        } catch (JSONException e) {
                            snackbar = Snackbar.make(view, "Unable to reach Server Please Try Again", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                snackbar = Snackbar.make(view, "Unable Connect to Internet. Please Try Again", Snackbar.LENGTH_LONG);
                snackbar.show();
                volleyError.printStackTrace();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_userid, userid);
                params.put(loginconfig.key_hospitalid, hospitalid);

                return params;
            }
        };
        RequestQueue studentlist = Volley.newRequestQueue(Appointments_newdoctor_search_Activity.this);
        studentlist.add(studentrequest1);

    }


    public  void getDepartmentid(){

        deleteCache(Appointments_newdoctor_search_Activity.this);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        userid = sharedPreferences.getString(loginconfig.key_userid, "");
        hospitalid = sharedPreferences.getString(loginconfig.key_hospitalid, "");

        userid = userid.replace("[", "");
        userid = userid.replace("]", "");
        userid = userid.replace("\"", "");

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_departmentlist_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
                            loginresult = j.getJSONArray("result");

                            if (loginresult.length() > 0) {

                                arrdepartmentid = new ArrayList<String>(loginresult.length());
                                arrdepartmentname = new ArrayList<String>(loginresult.length());

                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);

                                    departmentid = json.optString("departmentid");
                                    departmentname = json.optString("departmentname");

                                    arrdepartmentid.add(departmentid);
                                    arrdepartmentname.add(departmentname);
                                }


                                sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                //creating editor to store values of shared preferences
                                editor = sharedPreferences.edit();

                                editor.putString(loginconfig.key_info_doctorid, arrdoctorid.toString());
                                editor.putString(loginconfig.key_info_doctordepartmentid, arrdepartmentid.toString());
                                editor.apply();

                                for (int i = 0; i < arrdoctorid.size(); i++)
                                {
                                    AppointmentsDoctorList wp = new AppointmentsDoctorList(arrdoctorid.get(i),arrdoctorname.get(i),
                                            arruserimage.get(i),arrdepartmentid.get(i),arrdepartmentname.get(i));
                                    // Binds all strings into an array
                                    arraylist.add(wp);
                                }

                                adapter = new appointmentsearch_doctor_listview_adapter(Appointments_newdoctor_search_Activity.this, arrdoctorid, arrdoctorname,arrdepartmentname,arruserimage,arraylist);
                                patientlistview.setAdapter(adapter);

                            }else{
                                snackbar = Snackbar.make(view,"Technical Error.",Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }

                        } catch (JSONException e) {
                            snackbar = Snackbar.make(view, "Unable to reach Server Please Try Again", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                snackbar = Snackbar.make(view, "Unable Connect to Internet. Please Try Again", Snackbar.LENGTH_LONG);
                snackbar.show();
                volleyError.printStackTrace();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.clear();

                params.put(loginconfig.key_userid, userid);
                params.put(loginconfig.key_hospitalid, hospitalid);

                return params;
            }
        };
        RequestQueue studentlist = Volley.newRequestQueue(Appointments_newdoctor_search_Activity.this);
        studentlist.add(studentrequest1);

    }


    @Override
    public void onBackPressed() {
        //this is only needed if you have specific things
        //that you want to do when the user presses the back button.
        /* your specific things...*/
        super.onBackPressed();
        finish();
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

}
