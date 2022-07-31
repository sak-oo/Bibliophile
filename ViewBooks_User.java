package tyitproject.booksonthego;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewBooks_User extends AppCompatActivity {

    RecyclerView r;
    ViewBooksAdapter_User bd;
    public TextView t1;
    ArrayList<HashMap<String,String>> arrayList;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books__user);
        r=(RecyclerView)findViewById(R.id.rec1);
        r.setLayoutManager(new LinearLayoutManager(this));

        type=getIntent().getStringExtra("type");

        setTitle(type.toUpperCase()+" Books");
        DB_Conn obj = new DB_Conn();
        obj.execute();

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
        @Override
        public String doInBackground(String...arg) //compulsory to implement
        {
            arrayList=new ArrayList<>();
            String r="";
            PreparedStatement pst;
            ResultSet rs;
            try {
                SharedPreferences sp = getSharedPreferences("pf", Context.MODE_PRIVATE);

                String userid = sp.getString("userid", null);
                Connection con=DB_Connection.get_DBConnection();


                pst = con.prepareStatement("select * from books where userid!=? and type=? and status=?");
                pst.setString(1,userid);//userid
                pst.setString(2,type);
                pst.setString(3,"available");
                rs = pst.executeQuery();
                while(rs.next()) {

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("bno", rs.getString("bno"));
                    hashMap.put("title", rs.getString("title"));
                    hashMap.put("author", rs.getString("author"));
                    hashMap.put("publisher", rs.getString("publisher"));
                    hashMap.put("desc", rs.getString("description"));
                    hashMap.put("sp", rs.getString("sp"));
                    hashMap.put("rp", rs.getString("rp"));
                    arrayList.add(hashMap);
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

                bd=new ViewBooksAdapter_User(ViewBooks_User.this,arrayList);

                r.setAdapter(bd);


            }
            else{
                AlertDialog.Builder alert = new AlertDialog.Builder(ViewBooks_User.this);
                alert.setTitle("Information");
                alert.setMessage("No Books to Display");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        ViewBooks_User.this.finish();
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
