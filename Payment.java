package tyitproject.booksonthego;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;

public class Payment extends AppCompatActivity implements View.OnClickListener {

    EditText txt_cno,txt_cvv,txt_name;
    TextView txt_amt;
    Spinner year,month;
    ArrayAdapter adapter1;
    ArrayAdapter adapter2;
    String finalamt="";
    String t="",address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent i=getIntent();
        finalamt=i.getStringExtra("amt");
        address=i.getStringExtra("address");

        txt_cno=(EditText)findViewById(R.id.txt_cno);

        txt_cvv=(EditText)findViewById(R.id.txt_cvv);
        txt_name=(EditText)findViewById(R.id.txt_title);
        txt_amt=(TextView)findViewById(R.id.txt_amt);
        txt_amt.setText("To pay: "+finalamt+"/-");

        year=(Spinner)findViewById(R.id.year);
        String c[] =new String[15];
        for(int j=0;j<=14;j++)
        {
            c[j]=(new java.util.GregorianCalendar().get(Calendar.YEAR)+j)+"";
        }

        adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, c);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        year.setAdapter(adapter1);


        month=(Spinner)findViewById(R.id.month);

        String c1[] =new String[12];
        for(int j=0;j<=11;j++)
        {
            c1[j]=(String.format("%02d",(j+1)));
        }

        adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, c1);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        month.setAdapter(adapter2);

        Button b1=(Button) findViewById(R.id.button21);
        b1.setOnClickListener(this);

        txt_cno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(txt_cno.getText().toString().trim().equals("")==false && txt_cno.getText().length()==16) {
                    String a = txt_cno.getText().toString();
                    int n[] = new int[16];
                    int sum = 0;
                    for (int i = 0; i < a.length(); i++) {
                        n[i] = Integer.parseInt(a.charAt(i) + "");
                    }
                    for (int i = 15; i >= 0; i--) {
                        if (i % 2 == 0) {
                            n[i] = n[i] + n[i];
                            if (n[i] >= 10) {
                                n[i] = (n[i] / 10) + (n[i] % 10);
                            }
                        }
                    }
                    for (int i = 0; i < 16; i++) {
                        sum = sum + n[i];
                    }
                    if (sum % 10 != 0) {
                        txt_cno.requestFocus();
                        txt_cno.setError("Invalid credit card no", null);

                    }
                    else if (a.substring(0, 1).equals("4")) {
                        Drawable img = getResources().getDrawable(R.drawable.visa1);
                        img.setBounds(0, 0, 50, 50);
                        txt_cno.setCompoundDrawables(null, null, img, null);
                    } else if (Integer.parseInt(a.substring(0, 2)) >= 51 && Integer.parseInt(a.substring(0, 2)) <= 55) {
                        Drawable img = getResources().getDrawable(R.drawable.m1);
                        img.setBounds(0, 0, 100, 80);
                        txt_cno.setCompoundDrawables(null, null, img, null);
                    } else if (a.substring(0, 4).equals("6011") || a.substring(0, 2).equals("65")) {
                        Drawable img = getResources().getDrawable(R.drawable.dis1);
                        img.setBounds(0, 0, 50, 50);
                        txt_cno.setCompoundDrawables(null, null, img, null);
                    } else if (a.substring(0, 2).equals("34") || a.substring(0, 2).equals("37")) {
                        Drawable img = getResources().getDrawable(R.drawable.am1);
                        img.setBounds(0, 0, 50, 50);
                        txt_cno.setCompoundDrawables(null, null, img, null);
                    }
                }
                else{

                    txt_cno.setCompoundDrawables(null, null, null, null);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {

        try {
            String name = txt_name.getText().toString().trim();
            String cno = txt_cno.getText().toString().trim();
            String cvv = txt_cvv.getText().toString().trim();


            String input = month.getSelectedItem().toString() + "/" + year.getSelectedItem().toString();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy");
            simpleDateFormat.setLenient(false);
            java.util.Date expiry = simpleDateFormat.parse(input);
            boolean expired = expiry.before(new java.util.Date());

            if(cno.equals("")==false ) {
                if (name.equals("") == false) {
                    if (expired == false) {
                        if (cvv.equals("") == false) {

                            DB_Conn1 obj1 = new DB_Conn1();
                            obj1.execute(name);
                        }
                        else {
                            txt_cvv.setError("Value is required");
                        }

                    }
                    else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(Payment.this);
                        alert.setTitle("Error");
                        alert.setMessage("Card Expired");
                        alert.setPositiveButton("OK", null);
                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                    }
                }
                else {
                    txt_name.setError("Value is required");
                }
            }
            else {
                txt_cno.setError("Value is required");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    class DB_Conn1 extends AsyncTask<String,Void,String>
    {


        @Override
        public String doInBackground(String...arg) //compulsory to implement
        {
            String r="",t="";
            try {
                SharedPreferences sp = getSharedPreferences("pf", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();

                String userid=sp.getString("userid", null);

                Calendar c=Calendar.getInstance();
                String order_no=c.get(Calendar.YEAR)+""+c.get(Calendar.MONTH)+""+c.get(Calendar.DAY_OF_MONTH)+""+c.get(Calendar.MILLISECOND);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String o_date = df.format(new java.util.Date()).toString();

                Connection con=DB_Connection.get_DBConnection();

                PreparedStatement pst = con.prepareStatement("insert into orders values(?,?,?,?,?,?)");
                pst.setString(1, order_no);
                pst.setString(2, o_date);
                pst.setString(3, address);
                pst.setInt(4, Integer.parseInt(finalamt));
                pst.setString(5, "Card");
                pst.setString(6, userid);

                //pst.executeUpdate();

                t=t+"<table border=1><tr><th>Book title</th><th>Price</th></tr>";
                SharedPreferences sp1 = getSharedPreferences("pf1", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed1 = sp1.edit();
                Map<String,?> keys = sp1.getAll();

                for(Map.Entry<String,?> entry : keys.entrySet()) {
                    PreparedStatement pst1 = con.prepareStatement("Select rp, sp,title from books where bno=?");
                    pst1.setString(1, entry.getKey());
                    ResultSet rs=pst1.executeQuery();
                    rs.next();
                    pst = con.prepareStatement("insert into orders_books values(?,?,?)");
                    pst.setString(1, order_no);
                    pst.setString(2, entry.getKey());
                    if(entry.getValue().toString().equals("R")){
                        pst.setInt(3, rs.getInt("rp"));
                    }
                    else{
                        pst.setInt(3, rs.getInt("sp"));
                    }

                    pst.executeUpdate();

                    PreparedStatement pst11 = con.prepareStatement("update books set status=? where bno=?");
                    pst11.setString(2, entry.getKey());
                    if(entry.getValue().toString().equals("R")){
                        pst11.setString(1,"rented");
                    }
                    else{
                        pst11.setString(1,"sold");
                    }
                    pst11.execute();

                    ed1.remove(entry.getKey());
                    ed1.commit();
                    t=t+"<tr><td>"+rs.getString("title");
                    t=t+"<td>"+rs.getString("sp");

                }
                t=t+"</table>";
                t="Order no.: "+order_no+"<br/>"+t+"<br/>Grand Total: "+finalamt+"/-";
                con.close();
                Properties p=new Properties();
                p.put("mail.smtp.starttls.enable", "true");//here smtp donot get start security gets started
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
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(userid));//here type recipient email id
                msg.setSubject("Order and Payment Details");
                String m = t;
                msg.setContent(m.replaceAll("\n","<br/>"), "text/html; charset=utf-8");
                Transport.send(msg);
                r = "success";

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

                AlertDialog.Builder alert = new AlertDialog.Builder(Payment.this);
                alert.setTitle("Success");
                alert.setMessage("Order placed and Payment processed successfully");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        SharedPreferences sp = getSharedPreferences("pf1", Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed = sp.edit();
                        ed.clear();
                        ed.commit();
                        Intent i=new Intent(Payment.this,ViewBooksType.class);
                        startActivity(i);

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
