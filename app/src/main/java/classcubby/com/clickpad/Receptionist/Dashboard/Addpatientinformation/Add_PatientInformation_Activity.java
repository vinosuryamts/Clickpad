package classcubby.com.clickpad.Receptionist.Dashboard.Addpatientinformation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import classcubby.com.clickpad.AppStatus;
import classcubby.com.clickpad.MainActivity;
import classcubby.com.clickpad.R;
import classcubby.com.clickpad.Receptionist.Dashboard.Editpatientinformation.EditPatientcustomadapter;
import classcubby.com.clickpad.configfiles.loginconfig;
import classcubby.com.clickpad.configfiles.previousloginconfig;

/**
 * Created by Vino on 3/13/2018.
 */

public class Add_PatientInformation_Activity extends AppCompatActivity {

    TextView searchdetailtitle;
    ImageView leftarrow;
    RecyclerView patientinformationrecycler;
    Snackbar snackbar;
    View view;
    AddPatientcustomadapter mAdapter;
    LinearLayoutManager mLayoutManager;


    int patid;
    String hospitalid;
    private JSONArray loginresult;
    String[] dataSet,newvalues;
    int[] dataSetTypes;
    org.json.simple.JSONObject mainObj,jo;
    JSONArray ja;
    int pYear,pMonth,pDay;
    static final int DATE_DIALOG_ID = 0;

    ArrayList<String> arrpatientid,arrpatientmr,arrpatientfirstname,arrpatientlastname,arrpatientfather,
            arrpatientdob,arrpatientbirthplace,arrpatientgender,arrpatientcivilstatus,
            arrpatientbloodgroup,arrpatientaddressline1,arrpatientaddressline2,arrpatientcity,
            arrpatientcontactno,arrpatientaltcontactno,arrpatientemail,arrpatientinsurancecompany,
            arrpatientinsurancenumber,arrpatientidentifiers,arrpatientimage;

    String patientid,patientmr,patientfirstname,patientlastname,patientfather,
            patientdob,patientbirthplace,patientgender,patientcivilstatus,
            patientbloodgroup,patientaddressline1,patientaddressline2,patientcity,
            patientcontactno,patientaltcontactno,patientemail,patientinsurancecompany,
            patientinsurancenumber,patientidentifiers,patientimage;


    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpatient_information_xml);


        searchdetailtitle = (TextView) findViewById(R.id.searchdetailtitle);
        leftarrow = (ImageView) findViewById(R.id.leftarrow);
        patientinformationrecycler = (RecyclerView) findViewById(R.id.patientinformationrecycler);
        view = (RelativeLayout) findViewById(R.id.addnewpatinetcontainer);

        Typeface normaltypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansMedium.ttf");

        searchdetailtitle.setTypeface(boldtypeface);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        hospitalid = sharedPreferences.getString(previousloginconfig.key_hospitalid, "");

        mLayoutManager = new LinearLayoutManager(Add_PatientInformation_Activity.this);
        patientinformationrecycler.setLayoutManager(mLayoutManager);
        patientinformationrecycler.setHasFixedSize(true);

        final Intent intent = getIntent();
        if (intent.hasExtra("Patientid")) {
            String Patientid = intent.getExtras().getString("Patientid");
            Log.e("TabNumberFriendList",Patientid);
            patid = Integer.parseInt(Patientid);
            sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

            //creating editor to store values of shared preferences
            editor = sharedPreferences.edit();

            editor.putString(loginconfig.key_editpatientid, String.valueOf(patid));
            editor.apply();
        }

        if (AppStatus.getInstance(Add_PatientInformation_Activity.this).isOnline()) {
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

    }



    private void getPatientInformation() {
        deleteCache(Add_PatientInformation_Activity.this);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

        String[] values = {"0","1"};

        newvalues = new String[values.length];

        int k=0;
        jo = new org.json.simple.JSONObject();
        ja = new JSONArray();
        mainObj = new org.json.simple.JSONObject();
        jo.put("patientid", "");
        jo.put("mrnumber", "");
        jo.put("firstname", "");
        jo.put("lastname", "");
        jo.put("fatherorhusbandname", "");
        jo.put("dateofbirth", "");
        jo.put("birthplace", "");
        jo.put("gender", "");
        jo.put("civilstatus", "");
        jo.put("bloodgroup", "");
        ja.put(jo);
        mainObj.put("valueslist", ja);
        newvalues[k]=mainObj.toString();


        int i =1;
        jo = new org.json.simple.JSONObject();
        ja = new JSONArray();
        mainObj = new org.json.simple.JSONObject();
        jo.put("addressline1", "");
        jo.put("addressline2", "");
        jo.put("city", "");
        jo.put("contactnumber", "");
        jo.put("altcontactnumber", "");
        jo.put("emailid", "");
        jo.put("nameofrelative", "");
        jo.put("relationshipofpatient", "");
        jo.put("relativemobilenumber", "");
        ja.put(jo);
        mainObj.put("valueslist", ja);
        newvalues[i]=mainObj.toString();

        dataSet = new String[newvalues.length];
        dataSetTypes = new int[newvalues.length];

        for(int t=0;t<newvalues.length;t++){
            if(t==0){
                dataSet[t]=newvalues[t];
                dataSetTypes[t]=0;
            }else if(t==1){
                dataSet[t]=newvalues[t];
                dataSetTypes[t]=1;
            }
        }

        mAdapter = new AddPatientcustomadapter(Add_PatientInformation_Activity.this,dataSet,dataSetTypes);
        patientinformationrecycler.setAdapter(mAdapter);

    }

    DatePickerDialog.OnDateSetListener dateListener =  new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int yr, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            pYear = yr;
            pMonth = monthOfYear;
            pDay = dayOfMonth;
            String monthvalue;
            if(pMonth<10) {
                monthvalue = "0"+(pMonth+1);
            }else{
                monthvalue = String.valueOf(pMonth+1);
            }
            String date = pYear+"-"+monthvalue+"-"+pDay;

            mAdapter.setada(date);
        }


    };

    protected Dialog onCreateDialog(int id){

        switch(id) {
            case DATE_DIALOG_ID:
                Calendar cal = Calendar.getInstance();
                pYear = cal.get(Calendar.YEAR);
                pMonth = cal.get(Calendar.MONTH);
                pDay = cal.get(Calendar.DAY_OF_MONTH);
                return new DatePickerDialog(Add_PatientInformation_Activity.this, dateListener, pYear, pMonth, pDay);

        }
        return null;

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
