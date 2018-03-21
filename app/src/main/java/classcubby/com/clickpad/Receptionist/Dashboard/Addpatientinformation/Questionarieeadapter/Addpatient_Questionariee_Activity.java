package classcubby.com.clickpad.Receptionist.Dashboard.Addpatientinformation.Questionarieeadapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import classcubby.com.clickpad.AppStatus;
import classcubby.com.clickpad.MainActivity;
import classcubby.com.clickpad.R;
import classcubby.com.clickpad.Receptionist_Dashboard_Activity;
import classcubby.com.clickpad.configfiles.loginconfig;
import classcubby.com.clickpad.configfiles.previousloginconfig;

/**
 * Created by Vino on 3/14/2018.
 */

public class Addpatient_Questionariee_Activity extends AppCompatActivity {

    TextView searchdetailtitle;
    ImageView leftarrow;
    ListView questionarieelistview;
    TextView previouspage,submitbutton;
    View view;
    Snackbar snackbar;
    questionariee_listview_adapter adapter;
    ArrayList<String> questionarieevalues;
    Dialog dialog;


    String userid,hospitalid;
    private JSONArray loginresult;
    ArrayList<String> arrquestionid,arrquestion,arrquestionanswer;
    String questionid,question,questionanswer;
    String patientid,mrnumber,patientimage,firstname,lastname,father,cityid,dob,gender,civilstatus,bloodgroup;
    String addressline1,addressline2,altcontactnumber,contactnumber,emailid,city;
    String relativemobilenumber,relationshipofpatient,nameofrelative;


    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpatient_questionariee_xml);

        searchdetailtitle = (TextView) findViewById(R.id.searchdetailtitle);
        leftarrow = (ImageView) findViewById(R.id.leftarrow);
        questionarieelistview = (ListView) findViewById(R.id.questionarieelistview);
        previouspage = (TextView) findViewById(R.id.previouspage);
        submitbutton = (TextView) findViewById(R.id.submitbutton);
        view = (RelativeLayout) findViewById(R.id.addpatientquestioninfocontainer);

        Typeface normaltypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansMedium.ttf");

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        hospitalid = sharedPreferences.getString(previousloginconfig.key_hospitalid, "");

        searchdetailtitle.setTypeface(boldtypeface);
        previouspage.setTypeface(boldtypeface);
        submitbutton.setTypeface(boldtypeface);

        final Intent intent = getIntent();
        if (intent.hasExtra("patientid")) {
            patientid = intent.getExtras().getString("patientid");
            firstname = intent.getExtras().getString("firstname");
            lastname = intent.getExtras().getString("lastname");
            father = intent.getExtras().getString("father");
            dob = intent.getExtras().getString("dob");
            gender = intent.getExtras().getString("gender");
            civilstatus = intent.getExtras().getString("civilstatus");
            bloodgroup = intent.getExtras().getString("bloodgroup");
            addressline1 = intent.getExtras().getString("addressline1");
            addressline2 = intent.getExtras().getString("addressline2");
            cityid = intent.getExtras().getString("cityid");
            contactnumber = intent.getExtras().getString("contactnumber");
            altcontactnumber = intent.getExtras().getString("altcontactnumber");
            emailid = intent.getExtras().getString("emailid");
            nameofrelative = intent.getExtras().getString("nameofrelative");
            relationshipofpatient = intent.getExtras().getString("relationshipofpatient");
            relativemobilenumber = intent.getExtras().getString("relativemobilenumber");
        }


        if (AppStatus.getInstance(Addpatient_Questionariee_Activity.this).isOnline()) {
            getQuestionInformation();
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
        previouspage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(Addpatient_Questionariee_Activity.this);
                // Include dialog.xml file
                dialog.setContentView(R.layout.loading_dialog);

                TextView title = (TextView) dialog.findViewById(R.id.title);
                AVLoadingIndicatorView avi = (AVLoadingIndicatorView) dialog.findViewById(R.id.avi);

                Typeface normaltypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansRegular.ttf");
                Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansMedium.ttf");
                title.setTypeface(boldtypeface);

                title.setText("Saving Patient Information. Please wait!");

                dialog.show();
                dialog.setCancelable(false);

                questionarieevalues = questionariee_listview_adapter.getquestionarieqnswerslist();

                final JSONObject jObject = new JSONObject();

                try
                {
                    JSONArray jArry=new JSONArray();
                    for (int i=0;i<arrquestionid.size();i++)
                    {
                        JSONObject jObjd=new JSONObject();
                        jObjd.put("questionid", arrquestionid.get(i));
                        jObjd.put("questionanswer", questionarieevalues.get(i));
                        jArry.put(jObjd);
                    }
                    jObject.put("QuestionsList", jArry);


                }
                catch(JSONException ex)
                {
                    ex.printStackTrace();
                }

                StringRequest editinfo = new StringRequest(Request.Method.POST, loginconfig.key_addquestionariee_upload_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                dialog.setCancelable(true);
                                dialog.dismiss();
                                Intent i = new Intent(Addpatient_Questionariee_Activity.this, Receptionist_Dashboard_Activity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dialog.setCancelable(true);
                        dialog.dismiss();
                        volleyError.printStackTrace();
                    }
                })

                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.clear();

                        params.put(loginconfig.key_hospitalid, hospitalid);
                        params.put(loginconfig.key_editpatientid, patientid);
                        params.put(loginconfig.key_info_firstname, firstname);
                        params.put(loginconfig.key_info_lastname, lastname);
                        params.put(loginconfig.key_info_fatherorhusbandname, father);
                        params.put(loginconfig.key_info_gender, gender);
                        params.put(loginconfig.key_info_dateofbirth, dob);
                        params.put(loginconfig.key_info_civilstatus, civilstatus);
                        params.put(loginconfig.key_info_bloodgroup, bloodgroup);
                        params.put(loginconfig.key_info_addressline1, addressline1);
                        params.put(loginconfig.key_info_addressline2, addressline2);
                        params.put(loginconfig.key_info_city, cityid);
                        params.put(loginconfig.key_info_contactnumber, contactnumber);
                        params.put(loginconfig.key_info_altcontactnumber, altcontactnumber);
                        params.put(loginconfig.key_info_emailid, emailid);
                        params.put(loginconfig.key_info_patientnameofrelative, nameofrelative);
                        params.put(loginconfig.key_info_patientrelationshipofpatient, relationshipofpatient);
                        params.put(loginconfig.key_info_patientrelativemobilenumber, relativemobilenumber);
                        params.put(loginconfig.key_info_questionarieuload, jObject.toString());

                        return params;
                    }
                };
                RequestQueue editpatinfo = Volley.newRequestQueue(Addpatient_Questionariee_Activity.this);
                editpatinfo.add(editinfo);

            }
        });

    }



    private void getQuestionInformation() {
        deleteCache(Addpatient_Questionariee_Activity.this);
        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        userid = sharedPreferences.getString(loginconfig.key_userid, "");

        userid = userid.replace("[", "");
        userid = userid.replace("]", "");
        userid = userid.replace("\"", "");

        StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_addquestionariee_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        JSONObject j = null;
                        try {
                            j = new JSONObject(s);
                            loginresult = j.getJSONArray("result");

                            if (loginresult.length() > 0) {

                                arrquestionid = new ArrayList<String>(loginresult.length());
                                arrquestion = new ArrayList<String>(loginresult.length());
                                arrquestionanswer = new ArrayList<String>(loginresult.length());

                                for (int i = 0; i < loginresult.length(); i++) {
                                    JSONObject json = loginresult.getJSONObject(i);

                                    questionid = json.optString("questionid");
                                    question = json.optString("questions");
                                    questionanswer = json.optString("questionanswer");

                                    arrquestionid.add(questionid);
                                    arrquestion.add(question);
                                    arrquestionanswer.add(questionanswer);
                                }

                                adapter = new questionariee_listview_adapter(Addpatient_Questionariee_Activity.this, arrquestionid, arrquestion,arrquestionanswer);
                                questionarieelistview.setAdapter(adapter);


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
        RequestQueue studentlist = Volley.newRequestQueue(Addpatient_Questionariee_Activity.this);
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
