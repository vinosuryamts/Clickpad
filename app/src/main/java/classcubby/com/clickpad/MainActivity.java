package classcubby.com.clickpad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import classcubby.com.clickpad.configfiles.loginconfig;

public class MainActivity extends AppCompatActivity {

    TextView appname;
    ImageView splashlogo;
    private static int SPLASH_TIME_OUT = 2400;

    public static final String MY_PREFERENCES = "loginconfig.shared_pref_name";
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        splashlogo = (ImageView)findViewById(R.id.splashlogo);
        appname = (TextView)findViewById(R.id.appname);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/appfont.ttf");
        Typeface normaltypeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSansSemibold.ttf");
        Typeface boldtypeface = Typeface.createFromAsset(getAssets(), "fonts/OpenSansBold.ttf");

        appname.setTypeface(typeface);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, MainActivity.MODE_PRIVATE);
        final String nusertypeid = sharedPreferences.getString(loginconfig.key_usertypeid, "");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (nusertypeid.equals("")) {
                    Intent m = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(m);
                    finish();
                }else if (nusertypeid.equals("2")) {
                    Intent m = new Intent(MainActivity.this, Receptionist_Dashboard_Activity.class);
//                        m.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //It is use to finish current activity
                    startActivity(m);
                    finish();
                }
            }

        },SPLASH_TIME_OUT);

    }
}
