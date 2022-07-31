package tyitproject.booksonthego;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPreferences sp = getSharedPreferences("pf", Context.MODE_PRIVATE);
                boolean l = sp.getBoolean("loggedin", false);
                String userid = sp.getString("userid", null);
                if (l == true) {

                        Intent i = new Intent(SplashScreen.this, MainPage.class);
                        startActivity(i);

                }
                else {
                    Intent i = new Intent(SplashScreen.this, Login.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        }, 3000);
    }
}
