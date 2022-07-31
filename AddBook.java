package tyitproject.booksonthego;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.*;

public class AddBook extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    int n = 1;
    EditText txt_bno, txt_title, txt_sp,txt_rp, txt_author,txt_pub,txt_desc;
    String title, sp, desc,bno,type,author,publisher,rp;
    Button b1;
    Spinner s;
    CheckBox c1,c2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        String c[] = {"Computer","Comedy","Action","Adventure","Sci-Fi","Fantasy","Music","Drama","Romance","Horror","Mystery","Thriller","Animation","Family","Sport","Crime","Biography","Western","Musical","History"};
        s = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, c);
       // adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        s.setAdapter(adapter1);

        c1=(CheckBox)findViewById(R.id.checkBox);
        c2=(CheckBox)findViewById(R.id.checkBox2);
        c1.setOnCheckedChangeListener(this);
        c2.setOnCheckedChangeListener(this);

        txt_bno = (EditText) findViewById(R.id.txt_bno);
        txt_title = (EditText) findViewById(R.id.txt_title);
        txt_sp = (EditText) findViewById(R.id.txt_sp);
        txt_pub = (EditText) findViewById(R.id.txt_publisher);
        txt_author = (EditText) findViewById(R.id.txt_author);
        txt_desc = (EditText) findViewById(R.id.txt_desc);
        txt_rp = (EditText) findViewById(R.id.txt_rp);
        b1 = (Button) findViewById(R.id.button17);
        b1.setOnClickListener(this);

        DB_Conn1 obj=new DB_Conn1();
        obj.execute();


    }

    public void onClick(View v) {

        title = txt_title.getText().toString();
        sp = txt_sp.getText().toString();
        rp = txt_rp.getText().toString();
        author = txt_author.getText().toString();
        publisher = txt_pub.getText().toString();
        desc = txt_desc.getText().toString();
        bno=txt_bno.getText().toString().substring(9);
        type=s.getSelectedItem().toString();

        if (title.equals("") == false) {
            if (author.equals("") == false) {
                if (publisher.equals("") == false) {
                    if (desc.equals("") == false) {
                                if(c1.isChecked()==true || c2.isChecked()==true ){
                                    if((c1.isChecked()==true && sp.equals("")==false && Integer.parseInt(sp)>0)||c1.isChecked()==false){
                                        if((c2.isChecked()==true && rp.equals("")==false && Integer.parseInt(rp)>0)||c2.isChecked()==false){
                                            if(c1.isChecked()==false){
                                                sp="0";
                                            }
                                            if(c2.isChecked()==false){
                                                rp="0";
                                            }
                                            DB_Conn obj = new DB_Conn();
                                            obj.execute();
                                        }
                                        else{
                                            txt_rp.setError("Valid value is required");
                                        }

                                    }
                                    else{
                                        txt_sp.setError("Valid value is required");
                                    }
                                }
                                else {
                                    Toast.makeText(this,"Please select an option",Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                txt_desc.setError("Value is required");
                            }
                        }
                    else {
                        txt_pub.setError("Value is required");
                    }

                }
            else {
                txt_author.setError("Value is required");
            }
        }
        else {
            txt_title.setError("Value is required");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(c1.isChecked()){
            txt_sp.setEnabled(true);
        }
        else{
            txt_sp.setEnabled(false);
        }

        if(c2.isChecked()){
            txt_rp.setEnabled(true);
        }
        else{
            txt_rp.setEnabled(false);
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


                String userid = sp1.getString("userid", null);

                Connection con = DB_Connection.get_DBConnection();

                PreparedStatement pst = con.prepareStatement("insert into books values(?,?,?,?,?,?,?,?,?,?)");
                pst.setString(1, bno);
                pst.setString(2, title);
                pst.setString(3, author);
                pst.setString(4,publisher);
                pst.setString(5, desc);
                pst.setInt(6, Integer.parseInt(sp));
                pst.setInt(7, Integer.parseInt(rp));
                pst.setString(8, type);
                pst.setString(9,userid);
                pst.setString(10,"available");
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

                AlertDialog.Builder alert = new AlertDialog.Builder(AddBook.this);
                alert.setTitle("Success");
                alert.setMessage("Book added successfully");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        AddBook.this.finish();

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
    class DB_Conn1 extends AsyncTask<Void,Void,String>
    {

        @Override
        public String doInBackground(Void...arg) //compulsory to implement
        {
            String r="";
            try {

                Connection con = DB_Connection.get_DBConnection();

                PreparedStatement pst = con.prepareStatement("select max(bno) from books");

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
            txt_bno.setText("Book No: " + n);


        }

        @Override
        public void onPreExecute() //optional
        {
            // do something before start
        }

    }
}
