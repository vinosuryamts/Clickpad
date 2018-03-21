package classcubby.com.clickpad.Receptionist.Dashboard.Addpatientinformation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import classcubby.com.clickpad.MainActivity;
import classcubby.com.clickpad.R;
import classcubby.com.clickpad.Receptionist.Dashboard.Addpatientinformation.Questionarieeadapter.Addpatient_Questionariee_Activity;
import classcubby.com.clickpad.configfiles.loginconfig;

/**
 * Created by Vino on 3/13/2018.
 */

public class AddPatientcustomadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "CustomAdapter";
    public String[] mDataSet;
    private int[] mDataSetTypes;
    public Activity context;
    String patientid,mrnumber,patientimage,firstname,lastname,fatherorhusbandname,patientdob,birthplace,gender,civilstatus,bloodgroup;
    String addressline1,addressline2,altcontactnumber,contactnumber,emailid,city;
    String relativemobilenumber,relationshipofpatient,nameofrelative;

    String genderarray[] = {"Male","Female","Transgender"};
    String civilstatusarray[] = {"Married","Single"};
    String userid,hospitalid;
    int pYear,pMonth,pDay;
    static final int DATE_DIALOG_ID = 0;

    private JSONArray loginresult;
    ArrayList<String> arrcitylistid,arrcityname;
    String citylistid,cityname;
    public static String[] strcitylistid,strcityname;
    ArrayAdapter<String> citylistadapter,cityidlistadapter;
    RecyclerView.ViewHolder holder;
    PrimaryinfoViewHolder vh1;
    ContactinfoViewHolder vh3;
    String patientuserid;


    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;


    public AddPatientcustomadapter(Activity mainActivity, String[]dataSet, int[] dataSetTypes) {
        context = mainActivity;
        mDataSet = dataSet;
        mDataSetTypes = dataSetTypes;
    }


    @Override
    public int getItemCount() {
        return this.mDataSet.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0: (position ==1 ? 1:2);
        //return mDataSetTypes[position];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());

        switch (viewType) {
            case 0:
                View v = inflater.inflate(R.layout.addpatient_information_primaryinfo_cardview_xml, null);
                viewHolder = new PrimaryinfoViewHolder(v);
                break;

            case 1:
                View v1 = inflater.inflate(R.layout.addpatient_information_addressinfo_cardview_xml, null);
                viewHolder = new ContactinfoViewHolder(v1);
                break;

            default:
                View v2 = inflater.inflate(R.layout.addpatient_information_primaryinfo_cardview_xml, null);
                viewHolder = new PrimaryinfoViewHolder(v2);
                break;

        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case 0:
                vh1 = (PrimaryinfoViewHolder) holder;
                configurenewpatientviewholder(vh1, position);
                break;

            case 1:
                vh3 = (ContactinfoViewHolder) holder;
                configuredefauldholder(vh3,vh1, position);
                break;

            default:
                PrimaryinfoViewHolder vh4 = (PrimaryinfoViewHolder) holder;
                configurenewpatientviewholder(vh4, position);
                break;
        }

    }


    public class PrimaryinfoViewHolder extends RecyclerView.ViewHolder {

        EditText firstname,lastname,fathername,bloodgroup;
        TextView dateofbirth,firstnametext,lastnametext,gendertext,dateofbirthtext,civilstatustext;
        ImageView calendarimage;
        Spinner gender,civilstatus;

        public PrimaryinfoViewHolder(View v) {
            super(v);
            this.firstname = (EditText) v.findViewById(R.id.firstname);
            this.lastname = (EditText) v.findViewById(R.id.lastname);
            this.fathername = (EditText) v.findViewById(R.id.fathername);
            this.bloodgroup = (EditText) v.findViewById(R.id.bloodgroup);
            this.civilstatus = (Spinner) v.findViewById(R.id.civilstatus);
            this.gender = (Spinner) v.findViewById(R.id.gender);
            this.dateofbirth = (TextView) v.findViewById(R.id.dateofbirth);
            this.firstnametext = (TextView) v.findViewById(R.id.firstnametext);
            this.lastnametext = (TextView) v.findViewById(R.id.lastnametext);
            this.gendertext = (TextView) v.findViewById(R.id.gendertext);
            this.dateofbirthtext = (TextView) v.findViewById(R.id.dateofbirthtext);
            this.civilstatustext = (TextView) v.findViewById(R.id.civilstatustext);
            this.calendarimage = (ImageView) v.findViewById(R.id.calendarimage);
        }
    }

    public class ContactinfoViewHolder extends RecyclerView.ViewHolder {

        EditText addressline1,addressline2,contactnumber,altcontactnumber,email,nameofrelative,relationshipofpatient,relativemobilenumber;
        TextView addressline1text,addressline2text,citytext,contactnumbertext,nameofrelativetext,relationshipofpatienttext,relativemobilenumbertext;
        Spinner city;
        Button submit;

        public ContactinfoViewHolder(View v) {
            super(v);
            this.addressline1 = (EditText) v.findViewById(R.id.addressline1);
            this.addressline2 = (EditText) v.findViewById(R.id.addressline2);
            this.contactnumber = (EditText) v.findViewById(R.id.contactnumber);
            this.altcontactnumber = (EditText) v.findViewById(R.id.altcontactnumber);
            this.nameofrelative = (EditText) v.findViewById(R.id.nameofrelative);
            this.relationshipofpatient = (EditText) v.findViewById(R.id.relationshipofpatient);
            this.relativemobilenumber = (EditText) v.findViewById(R.id.relativemobilenumber);
            this.email = (EditText) v.findViewById(R.id.email);
            this.city = (Spinner) v.findViewById(R.id.city);
            this.addressline1text = (TextView) v.findViewById(R.id.addressline1text);
            this.addressline2text = (TextView) v.findViewById(R.id.addressline2text);
            this.citytext = (TextView) v.findViewById(R.id.citytext);
            this.contactnumbertext = (TextView) v.findViewById(R.id.contactnumbertext);
            this.nameofrelativetext = (TextView) v.findViewById(R.id.nameofrelativetext);
            this.relationshipofpatienttext = (TextView) v.findViewById(R.id.relationshipofpatienttext);
            this.relativemobilenumbertext = (TextView) v.findViewById(R.id.relativemobilenumbertext);
            this.submit = (Button) v.findViewById(R.id.Next);
        }
    }


    private void configurenewpatientviewholder(final PrimaryinfoViewHolder vh, int position) {

        String newvalues = mDataSet[position];
        newvalues = newvalues.replace("\\r", "~`");
        newvalues = newvalues.replace("\\n", "~~");
        newvalues = newvalues.replace("\\'", "``");
        newvalues = newvalues.replace("[\"", "[");
        newvalues = newvalues.replace("\"]", "]");
        newvalues = newvalues.replace("\\", "");
        newvalues = newvalues.replace("\\", "");
        newvalues = newvalues.replace("~`", "\\r");
        newvalues = newvalues.replace("~~", "\\n");
        newvalues = newvalues.replace(", ", ",");
        newvalues = newvalues.replace("```", ",");

        try {

            JSONObject j = new JSONObject(newvalues);

            //JSONArray jsonArr = j.getJSONArray("");

            JSONArray jsonArr = j.getJSONArray("valueslist");

            if(jsonArr.length()>0) {
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject json = jsonArr.getJSONObject(i);
                    patientid = json.optString("patientid");
                    mrnumber = json.optString("mrnumber");
                    firstname = json.optString("firstname");
                    lastname = json.optString("lastname");
                    fatherorhusbandname = json.optString("fatherorhusbandname");
                    patientdob = json.optString("dateofbirth");
                    birthplace = json.optString("birthplace");
                    gender = json.optString("gender");
                    civilstatus = json.optString("civilstatus");
                    bloodgroup = json.optString("bloodgroup");
                }

            }

            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/appfont.ttf");
            Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansRegular.ttf");
            Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansMedium.ttf");
            Typeface fontawesome = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome.ttf");

            vh.firstname.setTypeface(boldtypeface);
            vh.lastname.setTypeface(normaltypeface);
            vh.fathername.setTypeface(normaltypeface);
            vh.dateofbirth.setTypeface(normaltypeface);
            vh.bloodgroup.setTypeface(normaltypeface);


            patientuserid = patientid;

            String firstnametext = "<font color=#2f3334>First Name </font> <font color=#e53935>*</font>";
            String lastnametext = "<font color=#2f3334>Last Name </font> <font color=#e53935>*</font>";
            String gendertext = "<font color=#2f3334>Gender </font> <font color=#e53935>*</font>";
            String dobtext = "<font color=#2f3334>Date of Birth </font> <font color=#e53935>*</font>";
            String civiltext = "<font color=#2f3334>Civil Status </font> <font color=#e53935>*</font>";
            vh.firstnametext.setText(Html.fromHtml(firstnametext));
            vh.lastnametext.setText(Html.fromHtml(lastnametext));
            vh.gendertext.setText(Html.fromHtml(gendertext));
            vh.dateofbirthtext.setText(Html.fromHtml(dobtext));
            vh.civilstatustext.setText(Html.fromHtml(civiltext));


            ArrayAdapter<String> genderarrayadapter = new ArrayAdapter<String>(context,   android.R.layout.simple_spinner_item, genderarray);
            genderarrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            vh.gender.setAdapter(genderarrayadapter);

            ArrayAdapter<String> civilstatusarrayadapter = new ArrayAdapter<String>(context,   android.R.layout.simple_spinner_item, civilstatusarray);
            civilstatusarrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            vh.civilstatus.setAdapter(civilstatusarrayadapter);

            vh.calendarimage.setEnabled(true);
            vh.calendarimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity)context).showDialog(DATE_DIALOG_ID);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void setada(String date){
        vh1.dateofbirth.setText(date);
    }

    private void configuredefauldholder(final ContactinfoViewHolder vh2, final PrimaryinfoViewHolder vh3, int position) {

        String newvalues = mDataSet[position];
        newvalues = newvalues.replace("\\r", "~`");
        newvalues = newvalues.replace("\\n", "~~");
        newvalues = newvalues.replace("\\'", "``");
        newvalues = newvalues.replace("[\"", "[");
        newvalues = newvalues.replace("\"]", "]");
        newvalues = newvalues.replace("\\", "");
        newvalues = newvalues.replace("\\", "");
        newvalues = newvalues.replace("~`", "\\r");
        newvalues = newvalues.replace("~~", "\\n");
        newvalues = newvalues.replace(", ", ",");
        newvalues = newvalues.replace("```", ",");

        try {

            JSONObject j = new JSONObject(newvalues);

            //JSONArray jsonArr = j.getJSONArray("");

            JSONArray jsonArr = j.getJSONArray("valueslist");

            if(jsonArr.length()>0) {
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject json = jsonArr.getJSONObject(i);
                    addressline1 = json.optString("addressline1");
                    addressline2 = json.optString("addressline2");
                    city = json.optString("city");
                    contactnumber = json.optString("contactnumber");
                    altcontactnumber = json.optString("altcontactnumber");
                    emailid = json.optString("emailid");
                    nameofrelative = json.optString("nameofrelative");
                    relationshipofpatient = json.optString("relationshipofpatient");
                    relativemobilenumber = json.optString("relativemobilenumber");
                }

            }



            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/appfont.ttf");
            Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansRegular.ttf");
            Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansMedium.ttf");
            Typeface fontawesome = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome.ttf");

            vh2.addressline1.setTypeface(boldtypeface);
            vh2.addressline2.setTypeface(normaltypeface);
            vh2.contactnumber.setTypeface(normaltypeface);
            vh2.altcontactnumber.setTypeface(normaltypeface);
            vh2.email.setTypeface(normaltypeface);
            vh2.nameofrelative.setTypeface(normaltypeface);
            vh2.relationshipofpatient.setTypeface(normaltypeface);
            vh2.relativemobilenumber.setTypeface(normaltypeface);
            vh2.submit.setTypeface(normaltypeface);


            String addline1text = "<font color=#2f3334>Address Line1 </font> <font color=#e53935>*</font>";
            String cityte = "<font color=#2f3334>City </font> <font color=#e53935>*</font>";
            String contnumtext = "<font color=#2f3334>Contact Number </font> <font color=#e53935>*</font>";
            vh2.addressline1text.setText(Html.fromHtml(addline1text));
            vh2.citytext.setText(Html.fromHtml(cityte));
            vh2.contactnumbertext.setText(Html.fromHtml(contnumtext));

            sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
            userid = sharedPreferences.getString(loginconfig.key_userid, "");
            hospitalid= sharedPreferences.getString(loginconfig.key_hospitalid, "");

            StringRequest studentrequest1 = new StringRequest(Request.Method.POST, loginconfig.key_citylist_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            JSONObject j = null;
                            try {
                                j = new JSONObject(s);
                                loginresult = j.getJSONArray("result");

                                if (loginresult.length() > 0) {

                                    arrcitylistid = new ArrayList<String>(loginresult.length());
                                    arrcityname = new ArrayList<String>(loginresult.length());

                                    for (int i = 0; i < loginresult.length(); i++) {
                                        JSONObject json = loginresult.getJSONObject(i);

                                        citylistid = json.optString("citylistid");
                                        cityname = json.optString("cityname");

                                        arrcitylistid.add(citylistid);
                                        arrcityname.add(cityname);
                                    }

                                    strcitylistid = new String[arrcitylistid.size()];
                                    strcitylistid = (String[]) arrcitylistid.toArray(strcitylistid);

                                    strcityname = new String[arrcityname.size()];
                                    strcityname = (String[]) arrcityname.toArray(strcityname);

                                    cityidlistadapter = new ArrayAdapter<String>(context,
                                            android.R.layout.simple_spinner_item, strcitylistid); // The drop down view

                                    citylistadapter = new ArrayAdapter<String>(context,
                                            android.R.layout.simple_spinner_item, strcityname);
                                    citylistadapter.setDropDownViewResource(R.layout.spinner_item); // The drop down view
                                    vh2.city.setAdapter(citylistadapter);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    volleyError.printStackTrace();
                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.clear();

                    params.put(loginconfig.key_userid, userid);

                    return params;
                }
            };
            RequestQueue studentlist = Volley.newRequestQueue(context);
            studentlist.add(studentrequest1);


            vh2.submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    vh2.submit.setVisibility(View.INVISIBLE);

                    if(!validate()){
                        vh2.submit.setVisibility(View.VISIBLE);
                        return;
                    }

                    vh2.submit.setVisibility(View.VISIBLE);

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    final String patientid = patientuserid;
                                    final String firstname = vh3.firstname.getText().toString().trim();
                                    final String lastname = vh3.lastname.getText().toString().trim();
                                    final String father = vh3.fathername.getText().toString().trim();
                                    final String gender = vh3.gender.getSelectedItem().toString().trim();
                                    final String dob = vh3.dateofbirth.getText().toString().trim();
                                    final String civilstatus = vh3.civilstatus.getSelectedItem().toString().trim();
                                    final String bloodgroup = vh3.bloodgroup.getText().toString().trim();
                                    final String addressline1 = vh2.addressline1.getText().toString().trim();
                                    final String addressline2 = vh2.addressline2.getText().toString().trim();
                                    String cityname = vh2.city.getSelectedItem().toString().trim();
                                    int id = citylistadapter.getPosition(cityname);
                                    final String cityid = cityidlistadapter.getItem(id);
                                    final String contactnumber = vh2.contactnumber.getText().toString().trim();
                                    final String altcontactnumber = vh2.altcontactnumber.getText().toString().trim();
                                    final String emailid = vh2.email.getText().toString().trim();
                                    final String nameofrelative = vh2.nameofrelative.getText().toString().trim();
                                    final String relationshipofpatient = vh2.relationshipofpatient.getText().toString().trim();
                                    final String relativemobilenumber = vh2.relativemobilenumber.getText().toString().trim();

                                    final Bundle bundle = new Bundle();
                                    Intent i = new Intent(context,Addpatient_Questionariee_Activity.class);
                                    bundle.putString("patientid", patientid);
                                    bundle.putString("firstname", firstname);
                                    bundle.putString("lastname", lastname);
                                    bundle.putString("father", father);
                                    bundle.putString("gender", gender);
                                    bundle.putString("dob", dob);
                                    bundle.putString("civilstatus", civilstatus);
                                    bundle.putString("bloodgroup", bloodgroup);
                                    bundle.putString("addressline1", addressline1);
                                    bundle.putString("addressline2", addressline2);
                                    bundle.putString("cityid", cityid);
                                    bundle.putString("contactnumber", contactnumber);
                                    bundle.putString("altcontactnumber", altcontactnumber);
                                    bundle.putString("emailid", emailid);
                                    bundle.putString("nameofrelative", nameofrelative);
                                    bundle.putString("relationshipofpatient", relationshipofpatient);
                                    bundle.putString("relativemobilenumber", relativemobilenumber);
                                    i.putExtras(bundle);
                                    context.startActivity(i);
                                }
                            }, 100);




                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private boolean validate() {
        boolean valid = true;

        final String firstname = vh1.firstname.getText().toString().trim();
        final String lastname = vh1.lastname.getText().toString().trim();
        final String addressline1 = vh3.addressline1.getText().toString().trim();
        final String contactnumber = vh3.contactnumber.getText().toString().trim();
        final String dob = vh1.dateofbirth.getText().toString().trim();

        if (firstname.isEmpty()) {
            vh1.firstname.setError("Please enter a valid firstname");
            valid = false;
        } else {
            vh1.firstname.setError(null);
        }

        if (dob.isEmpty()) {
            vh1.dateofbirth.setError("Please select your date of birth");
            valid = false;
        } else {
            vh1.dateofbirth.setError(null);
        }

        if (lastname.isEmpty()) {
            vh1.lastname.setError("Please enter a valid lastname");
            valid = false;
        } else {
            vh1.lastname.setError(null);
        }

        if (addressline1.isEmpty()) {
            vh3.addressline1.setError("Please enter a valid Address");
            valid = false;
        } else {
            vh3.addressline1.setError(null);
        }

        if (contactnumber.isEmpty() || contactnumber.length()<10) {
            vh3.contactnumber.setError("Please enter a valid Contact Number");
            valid = false;
        } else {
            vh3.contactnumber.setError(null);
        }

        return valid;

    }

}
