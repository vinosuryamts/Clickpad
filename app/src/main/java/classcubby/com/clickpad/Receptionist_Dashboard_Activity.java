package classcubby.com.clickpad;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import classcubby.com.clickpad.Receptionist.Dashboard.Dashboardcustomadapter.Dashboardcustomadapter;
import classcubby.com.clickpad.configfiles.loginconfig;
import classcubby.com.clickpad.configfiles.previousloginconfig;

/**
 * Created by Vino on 3/13/2018.
 */

public class Receptionist_Dashboard_Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView projecttitle,messageimage,counttext;
    private ImageView profileimage;
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    View view;


    private String loggedinuserimage,hospitalid;
    Timer t;
    boolean timervalue;
    Snackbar snackbar;
    Dashboardcustomadapter mAdapter;
    String[] dataSet,newvalues;
    int[] dataSetTypes;
    org.json.simple.JSONObject mainObj,jo;
    JSONArray ja;
    String userid;
    private JSONArray loginresult;
    ArrayList<String> arrpatientid,arrpatientmr,arrpatientimage;
    String patientid,patientimage,mrnumber;
    int previouscount = 0;
    int currentcount=0;
    Dialog dialog;


    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receptionist_dashboard_xml);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(false);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        projecttitle = (TextView)findViewById(R.id.projecttitle);
        profileimage = (ImageView) findViewById(R.id.profileimage);
        messageimage = (TextView) findViewById(R.id.messageimage);
        counttext = (TextView) findViewById(R.id.counttext);
        view = (RelativeLayout) findViewById(R.id.mainpageoverallcontainer);


        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        loggedinuserimage = sharedPreferences.getString(previousloginconfig.key_userimage, "");
        hospitalid = sharedPreferences.getString(previousloginconfig.key_hospitalid, "");

        Glide.with(Receptionist_Dashboard_Activity.this).load(loggedinuserimage).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                Log.i("GLIDE", "onException :", e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                Log.i("GLIDE", "onResourceReady");
                profileimage.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(profileimage);


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/appfont.ttf");
        Typeface normaltypeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSansSemibold.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSansBold.ttf");
        Typeface fontawesome = Typeface.createFromAsset(getAssets(), "fonts/fontawesome.ttf");

        projecttitle.setTypeface(typeface);
        messageimage.setTypeface(fontawesome);
        messageimage.setTextColor(getResources().getColor(R.color.darktextcolor));

        mLayoutManager = new LinearLayoutManager(Receptionist_Dashboard_Activity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(Receptionist_Dashboard_Activity.this, 0));

        if (AppStatus.getInstance(Receptionist_Dashboard_Activity.this).isOnline()) {
            getPatientInformation();
        }else {
            snackbar = Snackbar.make(view,"Please Connect to the Internet and Try Again",Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        t = new Timer();
        timervalue =true;
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                getPatientInformationcount();
            }

        },5000,5000);

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(Receptionist_Dashboard_Activity.this);
                // Include dialog.xml file
                dialog.setContentView(R.layout.close_dialog);

                TextView title = (TextView) dialog.findViewById(R.id.title);
                TextView messagetext = (TextView) dialog.findViewById(R.id.messagetext);
                TextView yes = (TextView) dialog.findViewById(R.id.yes);
                TextView cancel = (TextView) dialog.findViewById(R.id.cancel);

                Typeface normaltypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansRegular.ttf");
                Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansMedium.ttf");
                title.setTypeface(boldtypeface);
                messagetext.setTypeface(normaltypeface);
                yes.setTypeface(normaltypeface);
                cancel.setTypeface(normaltypeface);

                messagetext.setText("Do you want to logout?");
                dialog.show();

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();
                        Intent m = new Intent(Receptionist_Dashboard_Activity.this, LoginActivity.class);
                        m.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(m);
                        finish();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });

    }



    private void getPatientInformation() {
        deleteCache(Receptionist_Dashboard_Activity.this);
        userid = sharedPreferences.getString(loginconfig.key_userid, "");

        userid = userid.replace("[", "");
        userid = userid.replace("]", "");
        userid = userid.replace("\"", "");

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_dashboard_patient_information_url,
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

                                previouscount = loginresult.length();

                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);

                                    patientid = json.optString("patientid");
                                    mrnumber = json.optString("mrnumber");
                                    patientimage = json.optString("patientimage");

                                    arrpatientid.add(patientid);
                                    arrpatientmr.add(mrnumber);
                                    arrpatientimage.add(patientimage);
                                }


                                sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                //creating editor to store values of shared preferences
                                editor = sharedPreferences.edit();

                                editor.putString(loginconfig.key_dashpatientid, arrpatientid.toString());
                                editor.putString(loginconfig.key_dashpatientmr, arrpatientmr.toString());
                                editor.putString(loginconfig.key_dashpatientimage, arrpatientimage.toString());
                                editor.apply();

                                patientid = sharedPreferences.getString(loginconfig.key_dashpatientid, "");
                                mrnumber = sharedPreferences.getString(loginconfig.key_dashpatientmr, "");
                                patientimage = sharedPreferences.getString(loginconfig.key_dashpatientimage, "");


                                patientid = patientid.replace("[", "");
                                patientid = patientid.replace("]", "");
                                patientid = patientid.replace("\"", "");

                                mrnumber = mrnumber.replace("[", "");
                                mrnumber = mrnumber.replace("]", "");
                                mrnumber = mrnumber.replace("\"", "");

                                patientimage = patientimage.replace("[", "");
                                patientimage = patientimage.replace("]", "");
                                patientimage = patientimage.replace("\"", "");


                                String[] values = {"0","1","2"};

                                newvalues = new String[values.length];

                                int k=0;
                                jo = new org.json.simple.JSONObject();
                                ja = new JSONArray();
                                mainObj = new org.json.simple.JSONObject();
                                jo.put("patientid", patientid);
                                jo.put("mrnumber", mrnumber);
                                jo.put("patientimage", patientimage);
                                ja.put(jo);
                                mainObj.put("valueslist", ja);
                                newvalues[k]=mainObj.toString();

                                int i=1;
                                jo = new org.json.simple.JSONObject();
                                ja = new JSONArray();
                                mainObj = new org.json.simple.JSONObject();
                                jo.put("patientid", patientid);
                                jo.put("mrnumber", mrnumber);
                                jo.put("patientimage", patientimage);
                                ja.put(jo);
                                mainObj.put("valueslist", ja);
                                newvalues[i]=mainObj.toString();

                                dataSet = new String[newvalues.length];
                                dataSetTypes = new int[newvalues.length];


                                int m=2;
                                jo = new org.json.simple.JSONObject();
                                ja = new JSONArray();
                                mainObj = new org.json.simple.JSONObject();
                                jo.put("patientid", patientid);
                                jo.put("mrnumber", mrnumber);
                                jo.put("patientimage", patientimage);
                                ja.put(jo);
                                mainObj.put("valueslist", ja);
                                newvalues[m]=mainObj.toString();

                                dataSet = new String[newvalues.length];
                                dataSetTypes = new int[newvalues.length];

                                for(int t=0;t<newvalues.length;t++){
                                    if(t==0){
                                        dataSet[t]=newvalues[t];
                                        dataSetTypes[t]=0;
                                    }else if(t==1){
                                        dataSet[t]=newvalues[t];
                                        dataSetTypes[t]=1;
                                    }else if(t==2){
                                        dataSet[t]=newvalues[t];
                                        dataSetTypes[t]=2;
                                    }
                                }

                                mAdapter = new Dashboardcustomadapter(Receptionist_Dashboard_Activity.this,dataSet,dataSetTypes);
                                mRecyclerView.setAdapter(mAdapter);

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
        RequestQueue studentlist = Volley.newRequestQueue(Receptionist_Dashboard_Activity.this);
        studentlist.add(studentrequest1);

    }


    private void getPatientInformationcount() {
        deleteCache(Receptionist_Dashboard_Activity.this);
        userid = sharedPreferences.getString(loginconfig.key_userid, "");

        userid = userid.replace("[", "");
        userid = userid.replace("]", "");
        userid = userid.replace("\"", "");

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_count_dashboard_patient_information_url,
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


                                currentcount = loginresult.length();

                                if(currentcount>previouscount || currentcount<previouscount){

                                    for (int i = 0; i < loginresult.length(); i++) {
                                        JSONObject json = loginresult.getJSONObject(i);

                                        patientid = json.optString("patientid");
                                        mrnumber = json.optString("mrnumber");
                                        patientimage = json.optString("patientimage");

                                        arrpatientid.add(patientid);
                                        arrpatientmr.add(mrnumber);
                                        arrpatientimage.add(patientimage);
                                    }

                                    previouscount = loginresult.length();

                                    sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                    //creating editor to store values of shared preferences
                                    editor = sharedPreferences.edit();

                                    editor.putString(loginconfig.key_dashpatientid, arrpatientid.toString());
                                    editor.putString(loginconfig.key_dashpatientmr, arrpatientmr.toString());
                                    editor.putString(loginconfig.key_dashpatientimage, arrpatientimage.toString());
                                    editor.apply();

                                    patientid = sharedPreferences.getString(loginconfig.key_dashpatientid, "");
                                    mrnumber = sharedPreferences.getString(loginconfig.key_dashpatientmr, "");
                                    patientimage = sharedPreferences.getString(loginconfig.key_dashpatientimage, "");


                                    patientid = patientid.replace("[", "");
                                    patientid = patientid.replace("]", "");
                                    patientid = patientid.replace("\"", "");

                                    mrnumber = mrnumber.replace("[", "");
                                    mrnumber = mrnumber.replace("]", "");
                                    mrnumber = mrnumber.replace("\"", "");

                                    patientimage = patientimage.replace("[", "");
                                    patientimage = patientimage.replace("]", "");
                                    patientimage = patientimage.replace("\"", "");


                                    String[] values = {"0"};

                                    newvalues = new String[values.length];

                                    int k=0;
                                    jo = new org.json.simple.JSONObject();
                                    ja = new JSONArray();
                                    mainObj = new org.json.simple.JSONObject();
                                    jo.put("patientid", patientid);
                                    jo.put("mrnumber", mrnumber);
                                    jo.put("patientimage", patientimage);
                                    ja.put(jo);
                                    mainObj.put("valueslist", ja);
                                    newvalues[k]=mainObj.toString();

                                    dataSet = new String[newvalues.length];
                                    dataSetTypes = new int[newvalues.length];

                                    for(int t=0;t<newvalues.length;t++){
                                        if(t==0){
                                            dataSet[t]=newvalues[t];
                                            dataSetTypes[t]=0;
                                        }
                                    }

                                    mAdapter = new Dashboardcustomadapter(Receptionist_Dashboard_Activity.this,dataSet,dataSetTypes);
                                    mRecyclerView.setAdapter(mAdapter);

                                }



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
        RequestQueue studentlist = Volley.newRequestQueue(Receptionist_Dashboard_Activity.this);
        studentlist.add(studentrequest1);

    }


    @Override
    public void onDestroy() {
        if(timervalue==true){
            t.cancel();
            t = null;
            timervalue = false;
        }
        super.onDestroy();
    }

    @Override
    public  void onBackPressed(){
        dialog = new Dialog(Receptionist_Dashboard_Activity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.close_dialog);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        TextView messagetext = (TextView) dialog.findViewById(R.id.messagetext);
        TextView yes = (TextView) dialog.findViewById(R.id.yes);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);

        Typeface normaltypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansMedium.ttf");
        title.setTypeface(boldtypeface);
        messagetext.setTypeface(normaltypeface);
        yes.setTypeface(normaltypeface);
        cancel.setTypeface(normaltypeface);

        messagetext.setText("Do you want to exit the application");
        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
                System.exit(0);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.dismiss();
            }
        });


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