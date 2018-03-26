package classcubby.com.clickpad.Receptionist.Dashboard.Appointments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import classcubby.com.clickpad.MainActivity;
import classcubby.com.clickpad.R;
import classcubby.com.clickpad.configfiles.loginconfig;
import classcubby.com.clickpad.configfiles.previousloginconfig;

/**
 * Created by Vino on 3/14/2018.
 */

public class appointmentsearch_doctor_listview_adapter extends BaseAdapter {

    ArrayList<String> id,name,departmentname,mobilenumber,image,overalllist;
    Activity context;
    Holder holder;
    private List<AppointmentsDoctorList> patientlist;
    private ArrayList<AppointmentsDoctorList> arraylist = null;
    private static LayoutInflater inflater = null;

    TextView patid,patname,patientmrno,patientmobile,patimage,docid;
    String idvlaue,namevalue,mrvalie,mobnumvalue,imagevalue,appointpatientid;
    Dialog dialog;

    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;

    public appointmentsearch_doctor_listview_adapter(Activity mainActivity, ArrayList<String> doctorid, ArrayList<String> doctorname,
                                                     ArrayList<String> departmentid, ArrayList<String> patientimagevalue,
                                                     List<AppointmentsDoctorList> patientlist) {
        // TODO Auto-generated constructor stub
        this.context = mainActivity;
        this.id = doctorid;
        this.name = doctorname;
        this.departmentname = departmentid;
        this.image = patientimagevalue;
        this.arraylist = new ArrayList<AppointmentsDoctorList>();
        this.arraylist.addAll(patientlist);
        this.patientlist = patientlist;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return patientlist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        RelativeLayout rowlistcontainer;
        ImageView doctorimage;
        TextView doctorname,doctorid,doctordepartment,doctortimings,doctorimagevalue;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView = convertView;

        Log.d("posiiton ImageAdapater:", "" + position);
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.receptionist_dashboard_doctorsearch_rowitems_xml, null);
            holder = new Holder();
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.doctorid = (TextView) rowView.findViewById(R.id.doctorid);
        holder.doctorname = (TextView) rowView.findViewById(R.id.doctorname);
        holder.doctordepartment = (TextView) rowView.findViewById(R.id.doctordepartment);
        holder.doctortimings = (TextView) rowView.findViewById(R.id.doctortimings);
        holder.doctorimage = (ImageView) rowView.findViewById(R.id.doctorimage);
        holder.doctorimagevalue = (TextView) rowView.findViewById(R.id.doctorimagevalue);
        holder.rowlistcontainer = (RelativeLayout) rowView.findViewById(R.id.rowlistcontainer);


//        holder.image.setBackgroundResource(imageId[position]);

        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansMedium.ttf");
        holder.doctorname.setTypeface(normaltypeface);
        holder.doctordepartment.setTypeface(normaltypeface);
        holder.doctortimings.setTypeface(normaltypeface);

        holder.doctorid.setText(patientlist.get(position).getid());
        holder.doctorname.setText(patientlist.get(position).getname());
        holder.doctordepartment.setText(patientlist.get(position).getdepartmentname());
        holder.doctorimagevalue.setText(patientlist.get(position).getimage());

        Glide.with(context).load(patientlist.get(position).getimage()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                Log.i("GLIDE", "onException :", e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                Log.i("GLIDE", "onResourceReady");
                holder.doctorimage.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(holder.doctorimage);


        final View finalRow = rowView;
        holder.rowlistcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder = (Holder) finalRow.getTag();
                docid = (TextView) v.findViewById(R.id.doctorid);

                idvlaue = docid.getText().toString().trim();

                sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
                appointpatientid = sharedPreferences.getString(loginconfig.key_info_appointmentpatientid, "");

            }
        });



        return rowView;
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        patientlist.clear();
        if (charText.length() == 0) {
            patientlist.addAll(arraylist);
        }
        else
        {
            for (AppointmentsDoctorList wp : arraylist)
            {
                if (wp.getname().toLowerCase(Locale.getDefault()).contains(charText) || wp.getdepartmentname().toLowerCase(Locale.getDefault()).contains(charText) )
                {
                    patientlist.add(wp);
                }
            }
        }

        notifyDataSetChanged();
    }

}