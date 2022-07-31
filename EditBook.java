package tyitproject.booksonthego;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditBook extends AppCompatActivity implements View.OnClickListener {

    int n = 1;
    EditText txt_bno, txt_title, txt_sp,txt_rp, txt_author,txt_pub,txt_desc;
    String title, sp, desc,bno,type,author,publisher,rp;
    Button b1;
    Spinner s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        String c[] = {"Comedy","Action","Adventure","Sci-Fi","Fantasy","Music","Drama","Romance","Horror","Mystery","Thriller","Animation","Family","Sport","Crime","Biography","Western","Musical","History"};
        s = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, c);
        s.setAdapter(adapter1);


        txt_bno = (EditText) findViewById(R.id.txt_bno);
        txt_title = (EditText) findViewById(R.id.txt_title);
        txt_sp = (EditText) findViewById(R.id.txt_sp);
        txt_pub = (EditText) findViewById(R.id.txt_publisher);
        txt_author = (EditText) findViewById(R.id.txt_author);
        txt_desc = (EditText) findViewById(R.id.txt_desc);
        txt_rp = (EditText) findViewById(R.id.txt_rp);
        b1 = (Button) findViewById(R.id.button2);
        b1.setOnClickListener(this);

        Intent i=getIntent();
        txt_bno.setText("Book No: "+i.getStringExtra("bno"));
        txt_title.setText(i.getStringExtra("title"));
        txt_sp.setText(i.getStringExtra("sp"));
        txt_pub.setText(i.getStringExtra("pub"));
        txt_author.setText(i.getStringExtra("author"));
        txt_desc.setText(i.getStringExtra("desc"));
        txt_rp.setText(i.getStringExtra("rp"));
        s.setSelection(adapter1.getPosition(i.getStringExtra("type")));


    }
    @Override
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
                    if (sp.equals("") == false) {
                        if (rp.equals("") == false) {
                            if (desc.equals("") == false) {

                                DB_Conn obj = new DB_Conn();
                                obj.execute();
                            }
                            else {
                                txt_desc.setError("Value is required");
                            }
                        }
                        else {
                            txt_rp.setError("Value is required");
                        }
                    }
                    else {
                        txt_sp.setError("Value is required");
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

    class DB_Conn extends AsyncTask<Void,Void,String>
    {

        @Override
        public String doInBackground(Void...arg) //compulsory to implement
        {
            String r="";
            try {


                Connection con = DB_Connection.get_DBConnection();

                PreparedStatement pst = con.prepareStatement("update books set title=?,author=?,publisher=?,description=?,sp=?,rp=?,type=? where bno=?");
                pst.setString(1, title);
                pst.setString(2, author);
                pst.setString(3, publisher);
                pst.setString(4, desc);
                pst.setInt(5, Integer.parseInt(sp));
                pst.setInt(6, Integer.parseInt(rp));
                pst.setString(7, type);
                pst.setString(8, bno);
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

                AlertDialog.Builder alert = new AlertDialog.Builder(EditBook.this);
                alert.setTitle("Success");
                alert.setMessage("Book updated successfully");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        Intent intent=new Intent();
                        setResult(1,intent);
                        EditBook.this.finish();

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
