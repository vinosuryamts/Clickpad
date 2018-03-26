package classcubby.com.clickpad.Receptionist.Dashboard.Appointments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import classcubby.com.clickpad.Receptionist.Dashboard.Patientsearchinformation.search_patient_listview_adapter;
import classcubby.com.clickpad.configfiles.loginconfig;
import classcubby.com.clickpad.configfiles.previousloginconfig;

/**
 * Created by Vino on 3/14/2018.
 */

public class Appointments_newpatient_search_Activity extends AppCompatActivity {

    TextView searchdetailtitle;
    ImageView leftarrow;
    EditText search;
    ListView patientlistview;
    View view;
    Snackbar snackbar;
    appointmentsearch_patient_listview_adapter adapter;
    List<AppointmentsPatientList> arraylist = new ArrayList<AppointmentsPatientList>();
    List<AppointmentsPatientList> newpatientlist;

    String userid,hospitalid;
    private JSONArray loginresult;
    ArrayList<String> arrpatientid,arrpatientname,arrpatientmobilenumber,arrpatientmr,arrpatientimage;
    String patientid,patientname,patientmobilenumber,patientimage,mrnumber;
    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receptionist_dashboard_patientsearch_xml);

        searchdetailtitle = (TextView) findViewById(R.id.searchdetailtitle);
        leftarrow = (ImageView) findViewById(R.id.leftarrow);
        search = (EditText) findViewById(R.id.search);
        patientlistview = (ListView) findViewById(R.id.patientlistview);
        view = (RelativeLayout) findViewById(R.id.patientlistsearchcontainer);

        Typeface normaltypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansMedium.ttf");

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        hospitalid = sharedPreferences.getString(previousloginconfig.key_hospitalid, "");

        searchdetailtitle.setText("Book Appointment");
        searchdetailtitle.setTypeface(boldtypeface);
        search.setTypeface(normaltypeface);

        if (AppStatus.getInstance(Appointments_newpatient_search_Activity.this).isOnline()) {
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
        deleteCache(Appointments_newpatient_search_Activity.this);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        userid = sharedPreferences.getString(loginconfig.key_userid, "");

        userid = userid.replace("[", "");
        userid = userid.replace("]", "");
        userid = userid.replace("\"", "");

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_search_dashboard_patient_information_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
                            loginresult = j.getJSONArray("result");

                            if (loginresult.length() > 0) {

                                arrpatientid = new ArrayList<String>(loginresult.length());
                                arrpatientmr = new ArrayList<String>(loginresult.length());
                                arrpatientimage = new ArrayList<String>(loginresult.length());
                                arrpatientname = new ArrayList<String>(loginresult.length());
                                arrpatientmobilenumber = new ArrayList<String>(loginresult.length());

                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);

                                    patientid = json.optString("patientid");
                                    patientname = json.optString("name");
                                    mrnumber = json.optString("mrnumber");
                                    patientmobilenumber = json.optString("mobilenumber");
                                    patientimage = json.optString("patientimage");

                                    arrpatientid.add(patientid);
                                    arrpatientname.add(patientname);
                                    arrpatientmr.add(mrnumber);
                                    arrpatientmobilenumber.add(patientmobilenumber);
                                    arrpatientimage.add(patientimage);
                                }


                                sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                //creating editor to store values of shared preferences
                                editor = sharedPreferences.edit();

                                editor.putString(loginconfig.key_dashpatientid, arrpatientid.toString());
                                editor.putString(loginconfig.key_dashpatientmr, arrpatientmr.toString());
                                editor.putString(loginconfig.key_dashpatientimage, arrpatientimage.toString());
                                editor.putString(loginconfig.key_dashpatientname, arrpatientname.toString());
                                editor.putString(loginconfig.key_dashpatientmobile, arrpatientmobilenumber.toString());
                                editor.apply();

                                for (int i = 0; i < arrpatientid.size(); i++)
                                {
                                    AppointmentsPatientList wp = new AppointmentsPatientList(arrpatientid.get(i),arrpatientname.get(i),
                                            arrpatientmr.get(i),arrpatientmobilenumber.get(i),arrpatientimage.get(i));
                                    // Binds all strings into an array
                                    arraylist.add(wp);
                                }

                                adapter = new appointmentsearch_patient_listview_adapter(Appointments_newpatient_search_Activity.this, arrpatientid, arrpatientname,arrpatientmr,arrpatientimage,arrpatientmobilenumber,arraylist);
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
        RequestQueue studentlist = Volley.newRequestQueue(Appointments_newpatient_search_Activity.this);
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
