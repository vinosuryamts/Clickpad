package classcubby.com.clickpad.Receptionist.Dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import classcubby.com.clickpad.R;
import classcubby.com.clickpad.Receptionist.Dashboard.Addpatientinformation.Add_PatientInformation_Activity;
import classcubby.com.clickpad.TextConversionjava.NumberToWordsConverter;
import classcubby.com.clickpad.TextConversionjava.TypeWriter;


/**
 * Created by Vino on 10-02-2016.
 */
public class Dashboard_newpatient_gallery_adapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> id,mrnumber,image;
    Context context;

    public Dashboard_newpatient_gallery_adapter(Context mainActivity, ArrayList<String> patientid,ArrayList<String> patientmrnumber, ArrayList<String> patientimage) {
        super();
        this.context = mainActivity;
        this.id = patientid;
        this.mrnumber = patientmrnumber;
        this.image = patientimage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.receptionist_newpatient_galleryview_xml, viewGroup, false);
        viewHolder = new PatienListHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PatienListHolder vh1 = (PatienListHolder) holder;
        configurenewpatientviewholder(vh1, position);

    }


    public class PatienListHolder extends RecyclerView.ViewHolder {

        public ImageView patientimage;
        public TextView patientmrnumber;

        public PatienListHolder(View v) {
            super(v);
            patientimage = (ImageView) itemView.findViewById(R.id.patientimage);
            patientmrnumber = (TextView) itemView.findViewById(R.id.patientmrnumber);
        }
    }

    @Override
    public int getItemCount() {
        return id.size();
    }


    private void configurenewpatientviewholder(final PatienListHolder vh, int position) {

        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansMedium.ttf");

        vh.patientmrnumber.setTypeface(normaltypeface);

        vh.patientmrnumber.setText(mrnumber.get(position));

        Glide.with(context).load(image.get(position)).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                Log.i("GLIDE", "onException :", e);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                Log.i("GLIDE", "onResourceReady");
                vh.patientimage.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(vh.patientimage);
    }

}

