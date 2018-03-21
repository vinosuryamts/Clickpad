package classcubby.com.clickpad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

import classcubby.com.clickpad.configfiles.loginconfig;
import classcubby.com.clickpad.configfiles.previousloginconfig;

public class LoginActivity extends AppCompatActivity {

    TextView welcomemessage,welcomehopemessage,signinmessage,showorhidetext;
    EditText usernameedittext,passwordedittext;
    FloatingActionButton signinbutton;
    RelativeLayout loadingcontainer;
    View view;


    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static final String Previous_PREFERENCES = "previousloginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences,previoussharedPreferences;
    public static SharedPreferences.Editor editor,previouseditor;
    private JSONArray loginresult;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        welcomemessage = (TextView) findViewById(R.id.welcomemessage);
        welcomehopemessage = (TextView) findViewById(R.id.welcomehopemessage);
        signinmessage = (TextView) findViewById(R.id.signinmessage);
        showorhidetext = (TextView) findViewById(R.id.showorhidetext);
        usernameedittext = (EditText) findViewById(R.id.usernameedittext);
        passwordedittext = (EditText) findViewById(R.id.passwordedittext);
        signinbutton = (FloatingActionButton) findViewById(R.id.loginbutton);
        loadingcontainer = (RelativeLayout) findViewById(R.id.loadingcontainer);

        view = (RelativeLayout) findViewById(R.id.loginwholecontainer);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/appfont.ttf");
        Typeface normaltypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansRegular.ttf");
        Typeface semiboldtypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansLight.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/ClearSansBold.ttf");

        welcomemessage.setTypeface(boldtypeface);
        welcomehopemessage.setTypeface(semiboldtypeface);
        signinmessage.setTypeface(semiboldtypeface);
        usernameedittext.setTypeface(normaltypeface);
        showorhidetext.setTypeface(normaltypeface);
        passwordedittext.setTypeface(normaltypeface);


        usernameedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (usernameedittext.getText().length()>=10 && passwordedittext.getText().length()>5){
                    signinbutton.setEnabled(true);
                }else{
                    signinbutton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        passwordedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (usernameedittext.getText().length()>5 && passwordedittext.getText().length()>5){
                    signinbutton.setEnabled(true);
                }else{
                    signinbutton.setEnabled(false);
                }

                if(showorhidetext.getVisibility()== View.INVISIBLE && passwordedittext.getText().length()>1){
                    showorhidetext.setVisibility(View.VISIBLE);
                    Selection.setSelection(passwordedittext.getText(), passwordedittext.getText().length());
                }if(showorhidetext.getVisibility()== View.VISIBLE && passwordedittext.getText().length()>1){
                    showorhidetext.setVisibility(View.VISIBLE);
                    Selection.setSelection(passwordedittext.getText(), passwordedittext.getText().length());
                }else{
                    showorhidetext.setVisibility(View.INVISIBLE);
                    showorhidetext.setText("Show");
                    Selection.setSelection(passwordedittext.getText(), passwordedittext.getText().length());
                    passwordedittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        showorhidetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showorhidetext.getText().toString().trim().equals("Show")){
                    passwordedittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showorhidetext.setText("Hide");
                }else{
                    passwordedittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showorhidetext.setText("Show");
                }
            }
        });


        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AppStatus.getInstance(LoginActivity.this).isOnline()) {

                    if(!validate()){
                        signinbutton.setVisibility(View.VISIBLE);
                        usernameedittext.setEnabled(true);
                        passwordedittext.setEnabled(true);
                        loadingcontainer.setVisibility(View.INVISIBLE);
                        return;
                    }

                    signinbutton.setVisibility(View.INVISIBLE);
                    usernameedittext.setEnabled(false);
                    passwordedittext.setEnabled(false);
                    loadingcontainer.setVisibility(View.VISIBLE);

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    onLoginSuccess();
                                }
                            }, 100);

                }else{
                    snackbar = Snackbar.make(view,"Unable to connect to Internet! Please try again.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }


            }
        });

    }


    private boolean validate() {
        boolean valid = true;

        String usernamevalue = usernameedittext.getText().toString().trim();
        String passwordvalue = passwordedittext.getText().toString().trim();

        if (usernamevalue.isEmpty() || usernamevalue.length() <= 9 || passwordedittext.length() < 5) {
            usernameedittext.setError("Username should be your mobile number of 10 or more digits");
            valid = false;
        } else {
            usernameedittext.setError(null);
        }

        if (passwordvalue.isEmpty() || passwordvalue.length() < 5 || passwordvalue.length() > 25) {
            passwordedittext.setError("Password should be between 5 to 25 Alphanumeric Characters");
            valid = false;
        } else {
            passwordedittext.setError(null);
        }

        return valid;

    }

    public void onLoginSuccess() {

        deleteCache(this);

        final String edittextusername = usernameedittext.getText().toString().trim();
        final String edittextpassword = passwordedittext.getText().toString().trim();

        if (!(edittextusername.equals("")) && !(edittextpassword.equals("")))
        {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, loginconfig.key_login_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            JSONObject j = null;
                            try {
                                j = new JSONObject(s);
                                loginresult = j.optJSONArray("result");


                                if(loginresult.length()>0)
                                {
                                    JSONObject json = loginresult.getJSONObject(0);

                                    String id = json.optString("id");
                                    String arrusername = json.optString("username");
                                    String arrusertypeid = json.optString("usertypeid");
                                    String arruserimage = json.optString("userimage");
                                    String arrloginname = json.optString("loginname");
                                    String arrpassword = json.optString("password");
                                    String arrhospitalid = json.optString("hospitalid");
                                    String arrname = json.optString("name");

                                    sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
                                    previoussharedPreferences = getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);

                                    //creating editor to store values of shared preferences
                                    editor = sharedPreferences.edit();
                                    previouseditor = previoussharedPreferences.edit();

                                    editor.putBoolean(loginconfig.login_success, true);
                                    editor.putString(loginconfig.key_userid, id);
                                    editor.putString(loginconfig.key_username, arrusername);
                                    editor.putString(loginconfig.key_username, arrusername);
                                    editor.putString(loginconfig.key_usertypeid, arrusertypeid);
                                    editor.putString(loginconfig.key_userimage, arruserimage);
                                    editor.putString(loginconfig.key_loginname, arrloginname);
                                    editor.putString(loginconfig.key_password, arrpassword);
                                    editor.putString(loginconfig.key_hospitalid, arrhospitalid);
                                    editor.putString(loginconfig.key_name, arrname);
                                    editor.apply();


                                    previoussharedPreferences = getSharedPreferences(Previous_PREFERENCES, MainActivity.MODE_PRIVATE);

                                    //creating editor to store values of shared preferences
                                    previouseditor = previoussharedPreferences.edit();

                                    // Clearing all data from Shared Preferences
                                    previouseditor.clear();

                                    //Saving the sharedpreferences
                                    previouseditor.commit();

                                    previouseditor.putBoolean(previousloginconfig.login_success, true);
                                    previouseditor.putString(previousloginconfig.key_userid, id);
                                    previouseditor.putString(previousloginconfig.key_username, arrusername);
                                    previouseditor.putString(previousloginconfig.key_usertypeid, arrusertypeid);
                                    previouseditor.putString(previousloginconfig.key_userimage, arruserimage);
                                    previouseditor.putString(previousloginconfig.key_loginname, arrloginname);
                                    previouseditor.putString(previousloginconfig.key_name, arrname);
                                    previouseditor.putString(previousloginconfig.key_password, arrpassword);
                                    previouseditor.putString(previousloginconfig.key_hospitalid, arrhospitalid);
                                    previouseditor.putString(previousloginconfig.isloggedinalready, "true");
                                    previouseditor.apply();

                                    signinbutton.setVisibility(View.VISIBLE);
                                    usernameedittext.setEnabled(true);
                                    passwordedittext.setEnabled(true);
                                    loadingcontainer.setVisibility(View.INVISIBLE);

                                    sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
                                    final String nusertypeid = sharedPreferences.getString(loginconfig.key_usertypeid, "");

                                    if(nusertypeid.equals("2")) {
                                        Intent i = new Intent(LoginActivity.this, Receptionist_Dashboard_Activity.class);
                                        startActivity(i);
                                        finish();
                                    }else if(nusertypeid.equals("3")||nusertypeid.equals("4")){
                                        finish();
                                    }

                                    /*if(nusertypeid.equals("2")) {
                                        getschoolname();
                                    }else if(nusertypeid.equals("3")||nusertypeid.equals("4")){
                                        getdashboarditems();
                                    }*/

                                }
                                else
                                {
                                    signinbutton.setVisibility(View.VISIBLE);
                                    usernameedittext.setEnabled(true);
                                    passwordedittext.setEnabled(true);
                                    loadingcontainer.setVisibility(View.INVISIBLE);
                                    snackbar = Snackbar.make(view,"Invalid Username or Password",Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                            } catch (JSONException e) {
                                signinbutton.setVisibility(View.VISIBLE);
                                usernameedittext.setEnabled(true);
                                passwordedittext.setEnabled(true);
                                loadingcontainer.setVisibility(View.INVISIBLE);
                                snackbar = Snackbar.make(view,"Unable to recieve data from server due to Technical issues.",Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    signinbutton.setVisibility(View.VISIBLE);
                    usernameedittext.setEnabled(true);
                    passwordedittext.setEnabled(true);
                    loadingcontainer.setVisibility(View.INVISIBLE);
                    snackbar = Snackbar.make(view,"Unable to Connect with server due to Technical Issues. Kindly Contact Administrator for more details.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            })

            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.clear();

                    params.put(loginconfig.key_username, edittextusername);
                    params.put(loginconfig.key_password, edittextpassword);

                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

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
