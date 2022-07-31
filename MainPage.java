package tyitproject.booksonthego;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity implements View.OnClickListener {

    Button b1,b2,b3,b4,b5,b6,b7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        b1=(Button)findViewById(R.id.button4);
        b2=(Button)findViewById(R.id.button10);
        b3=(Button)findViewById(R.id.button15);
        b4=(Button)findViewById(R.id.button16);
        b5=(Button)findViewById(R.id.button20);
        b6=(Button)findViewById(R.id.button22);
        b7=(Button)findViewById(R.id.button23);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        checkAndRequestPermissions();
    }
    private  boolean checkAndRequestPermissions() {


        List<String> listPermissionsNeeded = new ArrayList<>();
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 1);
            return false;
        }
        return true;
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button4) {
            Intent i = new Intent(MainPage.this, AddBook.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.button10) {
            Intent i = new Intent(MainPage.this, ViewBooks.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.button15) {
            Intent i = new Intent(MainPage.this, UploadBook.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.button16) {
            Intent i = new Intent(MainPage.this, DownloadBook.class);
            startActivity(i);
        }
 	    else if(v.getId()==R.id.button20) {
            Intent i = new Intent(MainPage.this, ViewBooksType.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.button22) {
            Intent i = new Intent(MainPage.this, ChangePassword.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.button23) {
            SharedPreferences sp = getSharedPreferences("pf", Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = sp.edit();
            ed.clear();
            ed.commit();

            Intent i=new Intent(this,Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }
}
