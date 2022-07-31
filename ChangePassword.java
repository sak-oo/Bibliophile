package tyitproject.booksonthego;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {

    EditText txt_old,txt_new,txt_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Button b1=(Button)findViewById(R.id.button3);
        b1.setOnClickListener(this);

        txt_old = (EditText) findViewById(R.id.txt_oldpassword);
        txt_new = (EditText) findViewById(R.id.txt_newpassword);
        txt_confirm = (EditText) findViewById(R.id.txt_confirmpassword1);

        txt_confirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    if(!txt_confirm.getText().toString().equals(txt_new.getText().toString()))
                    {
                        txt_confirm.setError("Password and Confirm password do not match");
                        txt_confirm.setText("");
                    }
                }
            }
        });
    }

    public void onClick(View v)
    {
        String old = txt_old.getText().toString();
        String newp = txt_new.getText().toString();
        String confirm = txt_confirm.getText().toString();

        if(old.equals("")==false ) {
            if(newp.equals("")==false ){
                if (confirm.equals("") == false) {
                    if(confirm.equals(newp)){
                       // DB_Conn obj = new DB_Conn();
                        //obj.execute(old, newp);

                    }else {
                        txt_confirm.setError("Password and Confirm password do not match");
                    }

                } else {
                    txt_confirm.setError("Value is required");
                }
            } else {
                txt_new.setError("Valid is required");
            }
        }
        else {
            txt_old.setError("Value is required");
        }
    }

    class DB_Conn extends AsyncTask<String,Void,String>
    {


        @Override
        public String doInBackground(String...arg) //compulsory to implement
        {
            String r="";
            try {
                SharedPreferences sp = getSharedPreferences("pf", Context.MODE_PRIVATE);

                String userid=sp.getString("userid",null);

                Connection con=DB_Connection.get_DBConnection();

                PreparedStatement pst = con.prepareStatement("select * from login where userid=? and password=?");
                pst.setString(1, userid);
                pst.setString(2, arg[0]);
                ResultSet rs = pst.executeQuery();
                if (rs.next() == true) {

                    PreparedStatement pst1 = con.prepareStatement("update login set password=? where userid=?");
                    pst1.setString(1, arg[1]);
                    pst1.setString(2, userid);

                    pst1.executeUpdate();
                    con.close();
                    r="success";



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
               /* sp = getSharedPreferences("pf", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putString("userid", u);
                ed.putBoolean("loggedin", true);

                ed.commit();*/

                AlertDialog.Builder alert = new AlertDialog.Builder(ChangePassword.this);
                alert.setTitle("Success");
                alert.setMessage("Password changed successfully");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        Intent i=new Intent(ChangePassword.this,MainPage.class);
                        startActivity(i);

                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }

            else
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(ChangePassword.this);
                alert.setTitle("Error");
                alert.setMessage("Old password does not match");
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

