package classcubby.com.clickpad.Receptionist.Dashboard.Dashboardcustomadapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import classcubby.com.clickpad.R;
import classcubby.com.clickpad.Receptionist.Dashboard.Addpatientinformation.Add_PatientInformation_Activity;
import classcubby.com.clickpad.Receptionist.Dashboard.Appointments.Appointments_newpatient_search_Activity;
import classcubby.com.clickpad.Receptionist.Dashboard.Dashboard_newpatient_gallery_adapter;
import classcubby.com.clickpad.Receptionist.Dashboard.Patientsearchinformation.Dashboard_newpatient_search_Activity;
import classcubby.com.clickpad.TextConversionjava.NumberToWordsConverter;
import classcubby.com.clickpad.TextConversionjava.TypeWriter;

/**
 * Created by Vino on 3/13/2018.
 */

public class Dashboardcustomadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "CustomAdapter";
    public String[] mDataSet;
    private int[] mDataSetTypes;
    public Context context;
    String patientid,mrnumber,patientimage;

    ArrayList<String> arrpatientid,arrmrnumber,arrpatientimage;


    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;


    public Dashboardcustomadapter(Context mainActivity, String[]dataSet, int[] dataSetTypes) {
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
        return position;
        //return mDataSetTypes[position];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());

        switch (viewType) {
            case 0:
                View v = inflater.inflate(R.layout.receptionist_dashboard_addpatient_xml, null);
                viewHolder = new AddPatientviewholder(v);
                break;

            case 1:
                View v1 = inflater.inflate(R.layout.receptionist_dashboard_editpatient_xml, null);
                viewHolder = new EditPatientviewholder(v1);
                break;

            case 2:
                View v2 = inflater.inflate(R.layout.receptionist_dashboard_appointment_xml, null);
                viewHolder = new Addappointmentviewholder(v2);
                break;

            default:
                View v3 = inflater.inflate(R.layout.receptionist_dashboard_addpatient_xml, null);
                viewHolder = new AddPatientviewholder(v3);
                break;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case 0:
                AddPatientviewholder vh1 = (AddPatientviewholder) holder;
                configureaddnewpatient(vh1, position);
                break;

            case 1:
                EditPatientviewholder vh2 = (EditPatientviewholder) holder;
                configureeditnewpatient(vh2, position);
                break;

            case 2:
                Addappointmentviewholder vh3 = (Addappointmentviewholder) holder;
                configureAddappointmentviewholder(vh3, position);
                break;

            default:
                AddPatientviewholder vh4 = (AddPatientviewholder) holder;
                configureaddnewpatient(vh4, position);
                break;
        }

    }

    public class AddPatientviewholder extends RecyclerView.ViewHolder {

        RelativeLayout addcontainer;
        TextView newpatienttitle,addpatientbutton,morebutton;

        public AddPatientviewholder(View v) {
            super(v);
            this.newpatienttitle = (TextView) v.findViewById(R.id.newpatienttitle);
            this.addpatientbutton = (TextView) v.findViewById(R.id.addpatientbutton);
            this.morebutton = (TextView) v.findViewById(R.id.morebutton);
            this.addcontainer = (RelativeLayout) v.findViewById(R.id.addcontainer);
        }
    }


    private void configureaddnewpatient(final AddPatientviewholder vh, int position) {

        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansMedium.ttf");
        Typeface fontawesome = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome.ttf");

        vh.newpatienttitle.setTypeface(boldtypeface);
        vh.addpatientbutton.setTypeface(boldtypeface);
        vh.morebutton.setTypeface(boldtypeface);


        vh.addcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,Add_PatientInformation_Activity.class);
                context.startActivity(i);
            }
        });

    }



    public class EditPatientviewholder extends RecyclerView.ViewHolder {

        RelativeLayout editcontainer;
        TextView newpatienttitle,addpatientbutton,morebutton;

        public EditPatientviewholder(View v) {
            super(v);
            this.newpatienttitle = (TextView) v.findViewById(R.id.newpatienttitle);
            this.addpatientbutton = (TextView) v.findViewById(R.id.addpatientbutton);
            this.morebutton = (TextView) v.findViewById(R.id.morebutton);
            this.editcontainer = (RelativeLayout) v.findViewById(R.id.editcontainer);
        }
    }


    private void configureeditnewpatient(final EditPatientviewholder vh, int position) {

        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansMedium.ttf");
        Typeface fontawesome = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome.ttf");

        vh.newpatienttitle.setTypeface(boldtypeface);
        vh.addpatientbutton.setTypeface(boldtypeface);
        vh.morebutton.setTypeface(boldtypeface);


        vh.editcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,Dashboard_newpatient_search_Activity.class);
                context.startActivity(i);
            }
        });

    }




    public class Addappointmentviewholder extends RecyclerView.ViewHolder {

        RelativeLayout appointmentcontainer;
        TextView appointmentttitle,appointmentbutton,morebutton;

        public Addappointmentviewholder(View v) {
            super(v);
            this.appointmentttitle = (TextView) v.findViewById(R.id.appointmentttitle);
            this.appointmentbutton = (TextView) v.findViewById(R.id.appointmentbutton);
            this.morebutton = (TextView) v.findViewById(R.id.morebutton);
            this.appointmentcontainer = (RelativeLayout) v.findViewById(R.id.appointmentcontainer);
        }
    }


    private void configureAddappointmentviewholder(final Addappointmentviewholder vh, int position) {

        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansMedium.ttf");
        Typeface fontawesome = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome.ttf");

        vh.appointmentttitle.setTypeface(boldtypeface);
        vh.appointmentbutton.setTypeface(boldtypeface);
        vh.morebutton.setTypeface(boldtypeface);


        vh.appointmentcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,Appointments_newpatient_search_Activity.class);
                context.startActivity(i);
            }
        });

    }





    public class NewPatientViewHolder extends RecyclerView.ViewHolder {

        TextView newpatienttitle,addnewpatient,patientnextpage,addnewtext;
        TypeWriter patientcount;
        RecyclerView patientlist;

        public NewPatientViewHolder(View v) {
            super(v);
            this.newpatienttitle = (TextView) v.findViewById(R.id.newpatienttitle);
            this.patientcount = (TypeWriter) v.findViewById(R.id.patientcount);
            this.addnewpatient = (TextView) v.findViewById(R.id.addnewpatient);
            this.patientnextpage = (TextView) v.findViewById(R.id.patientnextpage);
            this.addnewtext = (TextView) v.findViewById(R.id.addnewtext);
            this.patientlist = (RecyclerView) v.findViewById(R.id.patientlist);
        }
    }

    public class NewPatientViewHolder1 extends RecyclerView.ViewHolder {

        TextView newpatienttitle,patientcount,addnewpatient;
        RecyclerView patientlist;

        public NewPatientViewHolder1(View v) {
            super(v);
            this.newpatienttitle = (TextView) v.findViewById(R.id.newpatienttitle);
            this.patientcount = (TextView) v.findViewById(R.id.patientcount);
            this.addnewpatient = (TextView) v.findViewById(R.id.addnewpatient);
            this.patientlist = (RecyclerView) v.findViewById(R.id.patientlist);
        }
    }


    private void configurenewpatientviewholder(final NewPatientViewHolder vh, int position) {

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
                    patientimage = json.optString("patientimage");
                }

            }

            arrpatientid = new ArrayList<String>(Arrays.asList(patientid.split(",")));
            arrmrnumber = new ArrayList<String>(Arrays.asList(mrnumber.split(",")));
            arrpatientimage = new ArrayList<String>(Arrays.asList(patientimage.split(",")));

            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/appfont.ttf");
            Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansRegular.ttf");
            Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansMedium.ttf");
            Typeface fontawesome = Typeface.createFromAsset(context.getAssets(), "fonts/fontawesome.ttf");

            vh.newpatienttitle.setTypeface(boldtypeface);
            vh.patientcount.setTypeface(normaltypeface);
            vh.addnewtext.setTypeface(normaltypeface);


            NumberToWordsConverter ntw = new NumberToWordsConverter();
            //vh.patientcount.setText(ntw.convert(arrmrnumber.size()));
            vh.patientcount.setCharacterDelay(150);
            vh.patientcount.animateText(ntw.convert(arrmrnumber.size()));


            /*ValueAnimator animator = ValueAnimator.ofInt(0, Integer.parseInt(size));
            animator.setDuration(1000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    vh.patientcount.setText(animation.getAnimatedValue().toString());
                }
            });
            animator.start();*/



            vh.patientlist.setHasFixedSize(true);
            LinearLayoutManager  mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            vh.patientlist.setLayoutManager(mLayoutManager);
            vh.patientlist.setAdapter(new Dashboard_newpatient_gallery_adapter(context, arrpatientid, arrmrnumber,arrpatientimage));

            vh.patientnextpage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context,Dashboard_newpatient_search_Activity.class);
                    context.startActivity(i);
                }
            });

            vh.addnewpatient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,Add_PatientInformation_Activity.class);
                    context.startActivity(i);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }




}
