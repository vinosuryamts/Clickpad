package classcubby.com.clickpad.Receptionist.Dashboard.Appointments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

import classcubby.com.clickpad.R;
import classcubby.com.clickpad.Receptionist.Dashboard.Editpatientinformation.Edit_PatientInformation_Activity;
import classcubby.com.clickpad.Receptionist.Dashboard.Patientsearchinformation.PatientList;

/**
 * Created by Vino on 3/14/2018.
 */

public class appointmentsearch_patient_listview_adapter extends BaseAdapter {

    ArrayList<String> id,name,mrnumber,mobilenumber,image,overalllist;
    Activity context;
    Holder holder;
    private List<AppointmentsPatientList> patientlist;
    private ArrayList<AppointmentsPatientList> arraylist = null;
    private static LayoutInflater inflater = null;

    TextView patid,patname,patientmrno,patientmobile,patimage;
    String idvlaue,namevalue,mrvalie,mobnumvalue,imagevalue;
    Dialog dialog;

    public appointmentsearch_patient_listview_adapter(Activity mainActivity, ArrayList<String> patientid, ArrayList<String> patientname,
                                                      ArrayList<String> patientmrnumber, ArrayList<String> patientimagevalue,
                                                      ArrayList<String> patientmobilenumber, List<AppointmentsPatientList> patientlist) {
        // TODO Auto-generated constructor stub
        this.context = mainActivity;
        this.id = patientid;
        this.name = patientname;
        this.mrnumber = patientmrnumber;
        this.mobilenumber = patientmobilenumber;
        this.image = patientimagevalue;
        this.arraylist = new ArrayList<AppointmentsPatientList>();
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
        ImageView patientimage;
        TextView patientname,patientmrnumber,patientmobilenumber,patientid,patientimagevalue;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView = convertView;

        Log.d("posiiton ImageAdapater:", "" + position);
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.receptionist_dashboard_patientsearch_rowitems_xml, null);
            holder = new Holder();
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }

        holder.patientid = (TextView) rowView.findViewById(R.id.patientid);
        holder.patientname = (TextView) rowView.findViewById(R.id.patientname);
        holder.patientmrnumber = (TextView) rowView.findViewById(R.id.patientmrnumber);
        holder.patientmobilenumber = (TextView) rowView.findViewById(R.id.patientmobilenumber);
        holder.patientimage = (ImageView) rowView.findViewById(R.id.patientimage);
        holder.patientimagevalue = (TextView) rowView.findViewById(R.id.patientimagevalue);
        holder.rowlistcontainer = (RelativeLayout) rowView.findViewById(R.id.rowlistcontainer);


//        holder.image.setBackgroundResource(imageId[position]);

        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansMedium.ttf");
        holder.patientname.setTypeface(normaltypeface);
        holder.patientmrnumber.setTypeface(normaltypeface);
        holder.patientmobilenumber.setTypeface(normaltypeface);

        holder.patientid.setText(patientlist.get(position).getid());
        holder.patientname.setText(patientlist.get(position).getname());
        holder.patientmrnumber.setText(patientlist.get(position).getmrnumber());
        holder.patientmobilenumber.setText(patientlist.get(position).getMobilenumber());
        holder.patientimagevalue.setText(patientlist.get(position).getimage());

        Glide.with(context).load(patientlist.get(position).getimage()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                Log.i("GLIDE", "onException :", e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                Log.i("GLIDE", "onResourceReady");
                holder.patientimage.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(holder.patientimage);


        final View finalRow = rowView;
        holder.rowlistcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder = (Holder) finalRow.getTag();
                patid = (TextView) v.findViewById(R.id.patientid);
                patname = (TextView) v.findViewById(R.id.patientname);
                patientmrno = (TextView) v.findViewById(R.id.patientmrnumber);
                patientmobile = (TextView) v.findViewById(R.id.patientmobilenumber);
                patimage = (TextView) v.findViewById(R.id.patientimagevalue);

                idvlaue = patid.getText().toString().trim();
                namevalue = patname.getText().toString().trim();
                mrvalie = patientmrno.getText().toString().trim();
                mobnumvalue = patientmobile.getText().toString().trim();
                imagevalue = patimage.getText().toString().trim();


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
            for (AppointmentsPatientList wp : arraylist)
            {
                if (wp.getname().toLowerCase(Locale.getDefault()).contains(charText) || wp.getmrnumber().toLowerCase(Locale.getDefault()).contains(charText) || wp.getMobilenumber().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    patientlist.add(wp);
                }
            }
        }

        notifyDataSetChanged();
    }

}