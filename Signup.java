package tyitproject.booksonthego;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;


public class Signup extends AppCompatActivity {

    String fname, emailid, mobile, password, confirmpassword;
    int i=1;
    ProgressDialog pd;
    EditText txt_fname,txt_lname,txt_emailid,txt_mobile,txt_password,txt_confirmpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        txt_fname = (EditText) findViewById(R.id.txt_fname);
        txt_emailid = (EditText) findViewById(R.id.txt_emailid);
        txt_mobile = (EditText) findViewById(R.id.txt_title);
        txt_password = (EditText) findViewById(R.id.txt_password);
        txt_confirmpassword = (EditText) findViewById(R.id.txt_confirmpassword);


        txt_confirmpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    if(!txt_confirmpassword.getText().toString().equals(txt_password.getText().toString()))
                    {
                        txt_confirmpassword.setError("Password and Confirm password do not match");
                        txt_confirmpassword.setText("");
                    }
                }
            }
        });

        Button b1 = (Button) findViewById(R.id.btn_register);

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                fname = txt_fname.getText().toString();
                emailid = txt_emailid.getText().toString();
                mobile = txt_mobile.getText().toString();
                password = txt_password.getText().toString();
                confirmpassword = txt_confirmpassword.getText().toString();


                if (fname.equals("") == false) {
                    if (emailid.equals("") == false && android.util.Patterns.EMAIL_ADDRESS.matcher(emailid).matches()) {
                        if (mobile.equals("") == false && mobile.length()==10) {
                            if (password.equals("") == false) {
                                if (confirmpassword.equals("") == false ){
                                    if(confirmpassword.equals(password)==true) {

                                        DB_Conn obj = new DB_Conn();
                                        obj.execute();

                                        pd = new ProgressDialog(Signup.this);
                                        pd.setMessage("Sending OTP on email..Please wait.."); // Setting Message
                                        pd.setTitle("Registration"); // Setting Title
                                        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                                        pd.show(); // Display Progress Dialog
                                        pd.setCancelable(false);
                                    }

                                    else{
                                        txt_confirmpassword.setError("Confirm Password does not match");
                                    }}
                                else {
                                    txt_confirmpassword.setError("Please enter password");
                                }
                            } else {
                                txt_password.setError("Please enter password");
                            }
                        } else {
                            txt_mobile.setError("Please enter valid mobile no");
                        }
                    } else {
                        txt_emailid.setError("Please enter emailid in correct format");
                    }
                } else {
                    txt_fname.setError("Please enter name");
                }
            }
        });
    }

    class DB_Conn extends AsyncTask<Void, Void, String> {


        String otp = "";

        AlertDialog alertDialog;

        @Override
        public String doInBackground(Void... arg) //compulsory to implement
        {
            String result = "";


            try {
                Connection con = DB_Connection.get_DBConnection();
                PreparedStatement pst = con.prepareStatement("select * from login where userid=?");
                pst.setString(1, emailid);
                ResultSet rs = pst.executeQuery();

                if (rs.next() == false) {

                    Random r = new Random();
                    otp = r.nextInt(9) + "" + r.nextInt(9) + "" + r.nextInt(9) + "" + r.nextInt(9);
                    Properties p=new Properties();

                    p.put("mail.smtp.starttls.enable","true");//here smtp donot get start security gets started
                    p.put("mail.smtp.auth","true");
                    p.put("mail.smtp.host","smtp.gmail.com");
                    p.put("mail.smtp.port","587");

                    Session s= Session.getDefaultInstance(p,new Authenticator()
                    {
                        protected PasswordAuthentication getPasswordAuthentication()
                        {
                            return new PasswordAuthentication(DB_Connection.SENDERS_EMAILID,DB_Connection.SENDERS_PASSWORD);
                        }
                    });


                    MimeMessage msg=new MimeMessage(s);//multipurpose internet mail extension mime
                    msg.setFrom(new InternetAddress(DB_Connection.SENDERS_EMAILID));
                    msg.addRecipient(Message.RecipientType.TO,new InternetAddress(emailid));//here type recipient email id
                    msg.setSubject("OTP for registration");
                    String m="Greeting,\n Your OTP for account activation is "+otp;
                    //msg.setText(m,"UTF-8","html");
                    msg.setContent(m, "text/html; charset=utf-8");
                    Transport.send(msg);


                    result = "success";
                } else {
                    result = "failed";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        public void onProgressUpdate(Void... arg0) //optional
        {

        }

        @Override
        public void onPostExecute(String result) //optional
        {
            pd.dismiss();


            if (result.equals("success")) {
                Intent i=new Intent(Signup.this,Signup1.class);
                i.putExtra("otp",otp);
                i.putExtra("fname",fname);
                i.putExtra("emailid",emailid);
                i.putExtra("mobile",mobile);
                i.putExtra("password",password);

                startActivity(i);
            }
            else{
                AlertDialog.Builder alert = new AlertDialog.Builder(Signup.this);
                alert.setTitle("Error");
                alert.setMessage("This emailid is already registered. Try a different one");
                alert.setPositiveButton("OK", null);
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                txt_emailid.requestFocus();
            }


        }

        @Override
        public void onPreExecute() //optional
        {
            // do something before start
        }

        public void insert()
        {

        }

    }


}

