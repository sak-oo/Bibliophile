package tyitproject.booksonthego;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.sql.*;

public class Login extends AppCompatActivity implements View.OnClickListener {


    String u,p1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_login=(Button)findViewById(R.id.button);
        btn_login.setOnClickListener(this);
        Button btn_signup=(Button)findViewById(R.id.button6);
        btn_signup.setOnClickListener(this);
        Button btn_fp=(Button)findViewById(R.id.button7);
        btn_fp.setOnClickListener(this);
    }
    public void onClick(View v)
    {
        EditText txt_userid = (EditText) findViewById(R.id.editText);
        EditText txt_password = (EditText) findViewById(R.id.editText2);

        u = txt_userid.getText().toString();
        p1 = txt_password.getText().toString();

        if(v.getId()==R.id.button)
        {
            if(u.equals("") == false) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(u).matches()) {
                    if (p1.equals("") == false) {


                        DB_Conn obj = new DB_Conn();
                        obj.execute(u, p1);

                    } else {
                        txt_password.setError("Please enter password");
                    }
                } else {
                    txt_userid.setError("Invalid Emailid Format");
                }
            } else {
                txt_userid.setError("Please enter userid");
            }
        }
        else if(v.getId()==R.id.button6) {
            Intent i=new Intent(this,Signup.class);
            startActivity(i);
        }
        else{
            Intent i=new Intent(this,ForgotPassword.class);
            startActivity(i);
        }


    }
    class DB_Conn extends AsyncTask<String,Void,String>
    {


        @Override
        public String doInBackground(String...arg) //compulsory to implement
        {
            String r="";
            try {


                Connection con = DB_Connection.get_DBConnection();

                PreparedStatement pst = con.prepareStatement("select * from login where userid=? and password=?");
                pst.setString(1, arg[0]);
                pst.setString(2, arg[1]);
                ResultSet rs = pst.executeQuery();
                if (rs.next() == true) {

                    r = "success";

                }
                else
                {
                    r="failure";
                }
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
                SharedPreferences sp = getSharedPreferences("pf", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putString("userid", u);
                ed.putBoolean("loggedin", true);

                ed.commit();

                Intent i=new Intent(Login.this,MainPage.class);
                startActivity(i);
                Login.this.finish();
            }

            else
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
                alert.setTitle("Error");
                alert.setMessage("Invalid credentials");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {


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
