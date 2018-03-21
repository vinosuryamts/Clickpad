package classcubby.com.clickpad.Receptionist.Dashboard.Addpatientinformation.Questionarieeadapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import classcubby.com.clickpad.R;

/**
 * Created by Vino on 3/14/2018.
 */

public class questionariee_listview_adapter extends BaseAdapter {

    ArrayList<String> id,questionname;
    Activity context;
    Holder holder;
    static ArrayList<String> questionarieelistvalues;
    private static LayoutInflater inflater = null;
    EditText vk;

    TextView patid,patname,patientmrno,patientmobile,patimage;
    String idvlaue,namevalue,mrvalie,mobnumvalue,imagevalue;
    Dialog dialog;

    public questionariee_listview_adapter(Activity mainActivity, ArrayList<String> questionid, ArrayList<String> question
            , ArrayList<String> questionanswer) {
        // TODO Auto-generated constructor stub
        this.context = mainActivity;
        this.id = questionid;
        this.questionname = question;
        this.questionarieelistvalues = questionanswer;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return id.size();
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
        TextView questionid,question;
        EditText questionanswer;
    }


    public static ArrayList<String> getquestionarieqnswerslist(){
        return questionarieelistvalues;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView = convertView;

        Log.d("posiiton ImageAdapater:", "" + position);
        if (rowView == null) {
            rowView = inflater.inflate(R.layout.addpatient_questionariee_rowitems_xml, null);
            holder = new Holder();
            holder.questionid = (TextView) rowView.findViewById(R.id.questionid);
            holder.question = (TextView) rowView.findViewById(R.id.question);
            holder.questionanswer = (EditText) rowView.findViewById(R.id.questionanswer);
            holder.rowlistcontainer = (RelativeLayout) rowView.findViewById(R.id.rowlistcontainer);
            rowView.setTag(R.id.questionanswer, holder.questionanswer);
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }


        holder.questionanswer.setTag(position);


//        holder.image.setBackgroundResource(imageId[position]);

        Typeface normaltypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansRegular.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(context.getAssets(), "fonts/ClearSansMedium.ttf");
        holder.questionid.setTypeface(normaltypeface);
        holder.question.setTypeface(normaltypeface);
        holder.questionanswer.setTypeface(normaltypeface);

        holder.questionid.setText(id.get(position));
        holder.question.setText(questionname.get(position));

        if(questionarieelistvalues.get(position).equals("")){
            holder.questionanswer.setText("");
        }else{
            holder.questionanswer.setText(questionarieelistvalues.get(position));
        }


        final View finalRow1 = rowView;
        holder.questionanswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                vk = (EditText)finalRow1.findViewById(R.id.questionanswer);

                int pos = (Integer) vk.getTag();
                questionarieelistvalues.set(pos,s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });


        return rowView;
    }


}