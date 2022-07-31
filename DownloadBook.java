package tyitproject.booksonthego;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class DownloadBook extends AppCompatActivity {

    RecyclerView r;
    ViewEBooksAdapter bd;
    ArrayList<HashMap<String,String>> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_book);

        r=(RecyclerView)findViewById(R.id.rec111);
        r.setLayoutManager(new LinearLayoutManager(this));


        DB_Conn obj = new DB_Conn();
        obj.execute();
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


                pst = con.prepareStatement("select * from ebooks where userid!=?");
                pst.setString(1,userid);
                rs = pst.executeQuery();
                while(rs.next()) {

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("eno", rs.getString("eno"));
                    hashMap.put("name", rs.getString("name"));
                    hashMap.put("userid", rs.getString("userid"));

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

                bd=new ViewEBooksAdapter(DownloadBook.this,arrayList);

                r.setAdapter(bd);


            }
            else{
                AlertDialog.Builder alert = new AlertDialog.Builder(DownloadBook.this);
                alert.setTitle("Information");
                alert.setMessage("No Books to Display");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        DownloadBook.this.finish();
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
