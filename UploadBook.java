package tyitproject.booksonthego;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UploadBook extends AppCompatActivity implements View.OnClickListener{

    Button b1,b2;
    File source=null;
    EditText t1,t2,t3;
    String name,filename,eno;
    InputStream iStream=null;
    int n=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_book);

        b1=(Button)findViewById(R.id.button18);
        b1.setOnClickListener(this);
        b2=(Button)findViewById(R.id.button19);
        b2.setOnClickListener(this);

        t1=(EditText)findViewById(R.id.editText8);
        t2=(EditText)findViewById(R.id.editText7);
        t3=(EditText)findViewById(R.id.txt_eno);

        DB_Conn1 obj=new DB_Conn1();
        obj.execute();
    }
    class DB_Conn1 extends AsyncTask<Void,Void,String>
    {

        @Override
        public String doInBackground(Void...arg) //compulsory to implement
        {
            String r="";
            try {

                Connection con = DB_Connection.get_DBConnection();

                PreparedStatement pst = con.prepareStatement("select max(eno) from ebooks");

                ResultSet rs = pst.executeQuery();
                rs.next();
                String v = rs.getString(1);

                if (v != null) {
                    n = Integer.parseInt(v) + 1;
                }

                r = "success";


                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return r;
        }
        @Override
        public void onProgressUpdate(Void...arg0) //optional
        {

        }
        @Override
        public void onPostExecute(String result) //optional
        {
            //  do something after execution
            t3.setText("Book No: " + n);


        }

        @Override
        public void onPreExecute() //optional
        {
            // do something before start
        }

    }


    @Override
    public void onClick(View v) {
        if(v.getId()==b1.getId()){
            Intent chooseFile;
            Intent intent;
            chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("*/*");
            intent = Intent.createChooser(chooseFile, "Choose a PDF file");
            startActivityForResult(intent, 0);
        }
        else{
            eno=t3.getText().toString().substring(9);
            name=t1.getText().toString();
            filename=t2.getText().toString();

            if(!name.isEmpty()){
                if(!filename.isEmpty()){

                    DB_Conn obj=new DB_Conn();
                    obj.execute();
                }
                else{
                    t2.setError("Please select a file to upload");
                }
            }
            else{
                t1.setError("Please enter book name");
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Dataaa", "dataa");
        if (data != null) {
           Uri sel = data.getData();


            try {
                    iStream = getContentResolver().openInputStream(sel);
                    String src = sel.getPath();
                    source = new File(Environment.getExternalStorageDirectory()+"/"+src);
                    t2.setText(source.getName());
                    Log.i("Sourcee", src);

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    class DB_Conn extends AsyncTask<Void,Void,String>
    {

        @Override
        public String doInBackground(Void...arg) //compulsory to implement
        {
            String r="";
            try {
                SharedPreferences sp1 = getSharedPreferences("pf", Context.MODE_PRIVATE);


                String userid = sp1.getString("userid",null);

                Connection con = DB_Connection.get_DBConnection();

                PreparedStatement pst = con.prepareStatement("insert into ebooks values(?,?,?,?)");
                pst.setString(1, eno);
                pst.setString(2, name);
                pst.setString(3, userid);
                pst.setBlob(4, iStream);
                pst.execute();

                r = "success";


                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return r;
        }
        @Override
        public void onProgressUpdate(Void...arg0) //optional
        {

        }
        @Override
        public void onPostExecute(String result) //optional
        {
            //  do something after execution
            if(result.equals("success"))
            {

                AlertDialog.Builder alert = new AlertDialog.Builder(UploadBook.this);
                alert.setTitle("Success");
                alert.setMessage("Book uploaded successfully");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                       UploadBook.this.finish();

                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();

            }


        }

        @Override
        public void onPreExecute() //optional
        {
            // do something before start
        }

    }
}
