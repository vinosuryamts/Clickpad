package classcubby.com.clickpad.Receptionist.Dashboard.Editpatientinformation;

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
import classcubby.com.clickpad.configfiles.loginconfig;
import classcubby.com.clickpad.configfiles.previousloginconfig;

/**
 * Created by Vino on 3/13/2018.
 */

public class Edit_PatientInformation_Activity extends AppCompatActivity {

    TextView searchdetailtitle;
    ImageView leftarrow;
    RecyclerView patientinformationrecycler;
    Snackbar snackbar;
    View view;
    EditPatientcustomadapter mAdapter;
    LinearLayoutManager mLayoutManager;


    int patid;
    String userid,hospitalid;
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
            arrpatientinsurancenumber,arrpatientidentifiers,arrpatientimage,arrnameofrelative,arrrelationshipofpatient,
            arrrelativemobilenumber;

    String patientid,patientmr,patientfirstname,patientlastname,patientfather,
            patientdob,patientbirthplace,patientgender,patientcivilstatus,
            patientbloodgroup,patientaddressline1,patientaddressline2,patientcity,
            patientcontactno,patientaltcontactno,patientemail,patientinsurancecompany,
            patientinsurancenumber,patientidentifiers,patientimage,nameofrelative,relationshipofpatient,relativemobilenumber;


    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editpatient_information_xml);


        searchdetailtitle = (TextView) findViewById(R.id.searchdetailtitle);
        leftarrow = (ImageView) findViewById(R.id.leftarrow);
        patientinformationrecycler = (RecyclerView) findViewById(R.id.patientinformationrecycler);
        view = (RelativeLayout) findViewById(R.id.editpatientinfocontainer);

        Typeface normaltypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansMedium.ttf");

        searchdetailtitle.setTypeface(boldtypeface);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        hospitalid = sharedPreferences.getString(previousloginconfig.key_hospitalid, "");

        mLayoutManager = new LinearLayoutManager(Edit_PatientInformation_Activity.this);
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

        if (AppStatus.getInstance(Edit_PatientInformation_Activity.this).isOnline()) {
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
        deleteCache(Edit_PatientInformation_Activity.this);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        userid = sharedPreferences.getString(loginconfig.key_editpatientid, "");

        userid = userid.replace("[", "");
        userid = userid.replace("]", "");
        userid = userid.replace("\"", "");

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_edit_dashboard_patient_information_url,
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
                                arrpatientfirstname = new ArrayList<String>(loginresult.length());
                                arrpatientlastname = new ArrayList<String>(loginresult.length());
                                arrpatientfather = new ArrayList<String>(loginresult.length());
                                arrpatientdob = new ArrayList<String>(loginresult.length());
                                arrpatientbirthplace = new ArrayList<String>(loginresult.length());
                                arrpatientgender = new ArrayList<String>(loginresult.length());
                                arrpatientcivilstatus = new ArrayList<String>(loginresult.length());
                                arrpatientbloodgroup = new ArrayList<String>(loginresult.length());
                                arrpatientaddressline1 = new ArrayList<String>(loginresult.length());
                                arrpatientaddressline2 = new ArrayList<String>(loginresult.length());
                                arrpatientcity = new ArrayList<String>(loginresult.length());
                                arrpatientcontactno = new ArrayList<String>(loginresult.length());
                                arrpatientaltcontactno = new ArrayList<String>(loginresult.length());
                                arrpatientemail = new ArrayList<String>(loginresult.length());
                                arrpatientinsurancecompany = new ArrayList<String>(loginresult.length());
                                arrpatientinsurancenumber = new ArrayList<String>(loginresult.length());
                                arrpatientidentifiers = new ArrayList<String>(loginresult.length());
                                arrpatientimage = new ArrayList<String>(loginresult.length());
                                arrnameofrelative = new ArrayList<String>(loginresult.length());
                                arrrelationshipofpatient = new ArrayList<String>(loginresult.length());
                                arrrelativemobilenumber = new ArrayList<String>(loginresult.length());


                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);

                                    patientid = json.optString("patientid");
                                    patientmr = json.optString("mrnumber");
                                    patientfirstname =json.optString("firstname");
                                    patientlastname =json.optString("lastname");
                                    patientfather =json.optString("fatherorhusbandname");
                                    patientdob =json.optString("dateofbirth");
                                    patientbirthplace =json.optString("birthplace");
                                    patientgender =json.optString("gender");
                                    patientcivilstatus =json.optString("civilstatus");
                                    patientbloodgroup =json.optString("bloodgroup");
                                    patientaddressline1 =json.optString("addressline1");
                                    patientaddressline2 =json.optString("addressline2");
                                    patientcity =json.optString("city");
                                    patientcontactno =json.optString("contactnumber");
                                    patientaltcontactno =json.optString("altcontactnumber");
                                    patientemail =json.optString("emailid");
                                    patientinsurancecompany =json.optString("insurancecompany");
                                    patientinsurancenumber =json.optString("insurancenumber");
                                    patientidentifiers =json.optString("patientidentifiers");
                                    patientimage =json.optString("patientimage");
                                    nameofrelative =json.optString("nameofrelative");
                                    relationshipofpatient =json.optString("relationshipofpatient");
                                    relativemobilenumber =json.optString("relativemobilenumber");


                                    arrpatientid.add(patientid);
                                    arrpatientmr.add(patientmr);
                                    arrpatientfirstname.add(patientfirstname);
                                    arrpatientlastname.add(patientlastname);
                                    arrpatientfather.add(patientfather);
                                    arrpatientdob.add(patientdob);
                                    arrpatientbirthplace.add(patientbirthplace);
                                    arrpatientgender.add(patientgender);
                                    arrpatientcivilstatus.add(patientcivilstatus);
                                    arrpatientbloodgroup.add(patientbloodgroup);
                                    arrpatientaddressline1.add(patientaddressline1);
                                    arrpatientaddressline2.add(patientaddressline2);
                                    arrpatientcity.add(patientcity);
                                    arrpatientcontactno.add(patientcontactno);
                                    arrpatientaltcontactno.add(patientaltcontactno);
                                    arrpatientemail.add(patientemail);
                                    arrpatientinsurancecompany.add(patientinsurancecompany);
                                    arrpatientinsurancenumber.add(patientinsurancenumber);
                                    arrpatientidentifiers.add(patientidentifiers);
                                    arrpatientimage.add(patientimage);
                                    arrnameofrelative.add(nameofrelative);
                                    arrrelationshipofpatient.add(relationshipofpatient);
                                    arrrelativemobilenumber.add(relativemobilenumber);

                                }





                                sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);

                                //creating editor to store values of shared preferences
                                editor = sharedPreferences.edit();

                                editor.putString(loginconfig.key_info_patientid, arrpatientid.toString());
                                editor.putString(loginconfig.key_info_mrnumber, arrpatientmr.toString());
                                editor.putString(loginconfig.key_info_firstname, arrpatientfirstname.toString());
                                editor.putString(loginconfig.key_info_lastname, arrpatientlastname.toString());
                                editor.putString(loginconfig.key_info_fatherorhusbandname, arrpatientfather.toString());
                                editor.putString(loginconfig.key_info_dateofbirth, arrpatientdob.toString());
                                editor.putString(loginconfig.key_info_birthplace, arrpatientbirthplace.toString());
                                editor.putString(loginconfig.key_info_gender, arrpatientgender.toString());
                                editor.putString(loginconfig.key_info_civilstatus, arrpatientcivilstatus.toString());
                                editor.putString(loginconfig.key_info_bloodgroup, arrpatientbloodgroup.toString());
                                editor.putString(loginconfig.key_info_addressline1, arrpatientaddressline1.toString());
                                editor.putString(loginconfig.key_info_addressline2, arrpatientaddressline2.toString());
                                editor.putString(loginconfig.key_info_city, arrpatientcity.toString());
                                editor.putString(loginconfig.key_info_contactnumber, arrpatientcontactno.toString());
                                editor.putString(loginconfig.key_info_altcontactnumber, arrpatientaltcontactno.toString());
                                editor.putString(loginconfig.key_info_emailid, arrpatientemail.toString());
                                editor.putString(loginconfig.key_info_insurancecompany, arrpatientinsurancecompany.toString());
                                editor.putString(loginconfig.key_info_insurancenumber, arrpatientinsurancenumber.toString());
                                editor.putString(loginconfig.key_info_patientidentifiers, arrpatientidentifiers.toString());
                                editor.putString(loginconfig.key_info_patientimage, arrpatientimage.toString());
                                editor.putString(loginconfig.key_info_patientnameofrelative, arrnameofrelative.toString());
                                editor.putString(loginconfig.key_info_patientrelationshipofpatient, arrrelationshipofpatient.toString());
                                editor.putString(loginconfig.key_info_patientrelativemobilenumber, arrrelativemobilenumber.toString());
                                editor.apply();


                                patientid =sharedPreferences.getString(loginconfig.key_info_patientid, "");
                                patientmr =sharedPreferences.getString(loginconfig.key_info_mrnumber, "");
                                patientfirstname =sharedPreferences.getString(loginconfig.key_info_firstname, "");
                                patientlastname =sharedPreferences.getString(loginconfig.key_info_lastname, "");
                                patientfather =sharedPreferences.getString(loginconfig.key_info_fatherorhusbandname, "");
                                patientdob =sharedPreferences.getString(loginconfig.key_info_dateofbirth, "");
                                patientbirthplace =sharedPreferences.getString(loginconfig.key_info_birthplace, "");
                                patientgender =sharedPreferences.getString(loginconfig.key_info_gender, "");
                                patientcivilstatus =sharedPreferences.getString(loginconfig.key_info_civilstatus, "");
                                patientbloodgroup =sharedPreferences.getString(loginconfig.key_info_bloodgroup, "");
                                patientaddressline1 =sharedPreferences.getString(loginconfig.key_info_addressline1, "");
                                patientaddressline2 =sharedPreferences.getString(loginconfig.key_info_addressline2, "");
                                patientcity =sharedPreferences.getString(loginconfig.key_info_city, "");
                                patientcontactno =sharedPreferences.getString(loginconfig.key_info_contactnumber, "");
                                patientaltcontactno =sharedPreferences.getString(loginconfig.key_info_altcontactnumber, "");
                                patientemail =sharedPreferences.getString(loginconfig.key_info_emailid, "");
                                patientinsurancecompany =sharedPreferences.getString(loginconfig.key_info_insurancecompany, "");
                                patientinsurancenumber =sharedPreferences.getString(loginconfig.key_info_insurancenumber, "");
                                patientidentifiers =sharedPreferences.getString(loginconfig.key_info_patientidentifiers, "");
                                patientimage =sharedPreferences.getString(loginconfig.key_info_patientimage, "");
                                nameofrelative =sharedPreferences.getString(loginconfig.key_info_patientnameofrelative, "");
                                relationshipofpatient =sharedPreferences.getString(loginconfig.key_info_patientrelationshipofpatient, "");
                                relativemobilenumber =sharedPreferences.getString(loginconfig.key_info_patientrelativemobilenumber, "");


                                patientid = patientid.replace("[", "");
                                patientid = patientid.replace("]", "");
                                patientid = patientid.replace("\"", "");

                                patientmr = patientmr.replace("[", "");
                                patientmr = patientmr.replace("]", "");
                                patientmr = patientmr.replace("\"", "");

                                patientfirstname = patientfirstname.replace("[", "");
                                patientfirstname = patientfirstname.replace("]", "");
                                patientfirstname = patientfirstname.replace("\"", "");

                                patientlastname = patientlastname.replace("[", "");
                                patientlastname = patientlastname.replace("]", "");
                                patientlastname = patientlastname.replace("\"", "");

                                patientfather = patientfather.replace("[", "");
                                patientfather = patientfather.replace("]", "");
                                patientfather = patientfather.replace("\"", "");

                                patientdob = patientdob.replace("[", "");
                                patientdob = patientdob.replace("]", "");
                                patientdob = patientdob.replace("\"", "");

                                patientbirthplace = patientbirthplace.replace("[", "");
                                patientbirthplace = patientbirthplace.replace("]", "");
                                patientbirthplace = patientbirthplace.replace("\"", "");


                                patientgender = patientgender.replace("[", "");
                                patientgender = patientgender.replace("]", "");
                                patientgender = patientgender.replace("\"", "");

                                patientcivilstatus = patientcivilstatus.replace("[", "");
                                patientcivilstatus = patientcivilstatus.replace("]", "");
                                patientcivilstatus = patientcivilstatus.replace("\"", "");

                                patientbloodgroup = patientbloodgroup.replace("[", "");
                                patientbloodgroup = patientbloodgroup.replace("]", "");
                                patientbloodgroup = patientbloodgroup.replace("\"", "");

                                patientaddressline1 = patientaddressline1.replace("[", "");
                                patientaddressline1 = patientaddressline1.replace("]", "");
                                patientaddressline1 = patientaddressline1.replace("\"", "");

                                patientaddressline2 = patientaddressline2.replace("[", "");
                                patientaddressline2 = patientaddressline2.replace("]", "");
                                patientaddressline2 = patientaddressline2.replace("\"", "");

                                patientcity = patientcity.replace("[", "");
                                patientcity = patientcity.replace("]", "");
                                patientcity = patientcity.replace("\"", "");

                                patientcontactno = patientcontactno.replace("[", "");
                                patientcontactno = patientcontactno.replace("]", "");
                                patientcontactno = patientcontactno.replace("\"", "");

                                patientaltcontactno = patientaltcontactno.replace("[", "");
                                patientaltcontactno = patientaltcontactno.replace("]", "");
                                patientaltcontactno = patientaltcontactno.replace("\"", "");

                                patientemail = patientemail.replace("[", "");
                                patientemail = patientemail.replace("]", "");
                                patientemail = patientemail.replace("\"", "");

                                patientinsurancecompany = patientinsurancecompany.replace("[", "");
                                patientinsurancecompany = patientinsurancecompany.replace("]", "");
                                patientinsurancecompany = patientinsurancecompany.replace("\"", "");

                                patientinsurancenumber = patientinsurancenumber.replace("[", "");
                                patientinsurancenumber = patientinsurancenumber.replace("]", "");
                                patientinsurancenumber = patientinsurancenumber.replace("\"", "");

                                patientidentifiers = patientidentifiers.replace("[", "");
                                patientidentifiers = patientidentifiers.replace("]", "");
                                patientidentifiers = patientidentifiers.replace("\"", "");

                                patientimage = patientimage.replace("[", "");
                                patientimage = patientimage.replace("]", "");
                                patientimage = patientimage.replace("\"", "");

                                nameofrelative = nameofrelative.replace("[", "");
                                nameofrelative = nameofrelative.replace("]", "");
                                nameofrelative = nameofrelative.replace("\"", "");

                                relationshipofpatient = relationshipofpatient.replace("[", "");
                                relationshipofpatient = relationshipofpatient.replace("]", "");
                                relationshipofpatient = relationshipofpatient.replace("\"", "");

                                relativemobilenumber = relativemobilenumber.replace("[", "");
                                relativemobilenumber = relativemobilenumber.replace("]", "");
                                relativemobilenumber = relativemobilenumber.replace("\"", "");





                                String[] values = {"0","1"};

                                newvalues = new String[values.length];

                                int k=0;
                                jo = new org.json.simple.JSONObject();
                                ja = new JSONArray();
                                mainObj = new org.json.simple.JSONObject();
                                jo.put("patientid", patientid);
                                jo.put("mrnumber", patientmr);
                                jo.put("firstname", patientfirstname);
                                jo.put("lastname", patientlastname);
                                jo.put("fatherorhusbandname", patientfather);
                                jo.put("dateofbirth", patientdob);
                                jo.put("birthplace", patientbirthplace);
                                jo.put("gender", patientgender);
                                jo.put("civilstatus", patientcivilstatus);
                                jo.put("bloodgroup", patientbloodgroup);
                                ja.put(jo);
                                mainObj.put("valueslist", ja);
                                newvalues[k]=mainObj.toString();


                                int i =1;
                                jo = new org.json.simple.JSONObject();
                                ja = new JSONArray();
                                mainObj = new org.json.simple.JSONObject();
                                jo.put("addressline1", patientaddressline1);
                                jo.put("addressline2", patientaddressline2);
                                jo.put("city", patientcity);
                                jo.put("contactnumber", patientcontactno);
                                jo.put("altcontactnumber", patientaltcontactno);
                                jo.put("emailid", patientemail);
                                jo.put("nameofrelative", nameofrelative);
                                jo.put("relationshipofpatient", relationshipofpatient);
                                jo.put("relativemobilenumber", relativemobilenumber);
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

                                mAdapter = new EditPatientcustomadapter(Edit_PatientInformation_Activity.this,dataSet,dataSetTypes);
                                patientinformationrecycler.setAdapter(mAdapter);


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

                params.put(loginconfig.key_editpatientid, userid);
                params.put(loginconfig.key_hospitalid, hospitalid);

                return params;
            }
        };
        RequestQueue studentlist = Volley.newRequestQueue(Edit_PatientInformation_Activity.this);
        studentlist.add(studentrequest1);

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
                return new DatePickerDialog(Edit_PatientInformation_Activity.this, dateListener, pYear, pMonth, pDay);

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
