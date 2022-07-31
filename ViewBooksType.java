package tyitproject.booksonthego;

import android.content.Intent;
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
import java.util.ArrayList;


public class ViewBooksType extends AppCompatActivity implements View.OnClickListener{

    Spinner s;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books_type);
        s = (Spinner) findViewById(R.id.spinner2);
        s.setPrompt("Select Book Type");

        b1=(Button)findViewById(R.id.button8);
        b1.setOnClickListener(this);

        DB_Conn obj = new DB_Conn();
        obj.execute();


    }

    @Override
    public void onClick(View v) {
        String n=s.getSelectedItem().toString();
        Intent i=new Intent(this,ViewBooks_User.class);
        i.putExtra("type",n);
        startActivity(i);
    }

     public boolean onOptionsItemSelected(MenuItem item) {

         switch(item.getItemId()) {
             case R.id.viewcart:
                 Intent i=new Intent(this,ViewCart.class);
                 startActivity(i);
                 break;
             case R.id.home:
                 Intent i1=new Intent(this,MainPage.class);
                 startActivity(i1);
                 break;

             default:
                 return super.onOptionsItemSelected(item);
         }

         return true;
     }
     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the main_menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.menu, menu);
         return true;
     }
    class DB_Conn extends AsyncTask<String,Void,String>
    {

        ArrayList<String> arrayList=new ArrayList<>();

        @Override
        public String doInBackground(String...arg) //compulsory to implement
        {
            String r="";
            PreparedStatement pst;
            ResultSet rs;
            try {
                Connection con=DB_Connection.get_DBConnection();


                pst = con.prepareStatement("select distinct type from books");
                rs = pst.executeQuery();
                while(rs.next()) {

                    arrayList.add(rs.getString(1));

                }
                pst.close();
                rs.close();


                con.close();

                if(arrayList.size()>0){
                    r="success";
                }
                else{
                    r="failure";
                }
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

                ArrayAdapter adapter1 = new ArrayAdapter(ViewBooksType.this, android.R.layout.simple_spinner_item, arrayList);
                adapter1.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                s.setAdapter(adapter1);

            }

        }

        @Override
        public void onPreExecute() //optional
        {
            // do something before start
        }

    }


}
