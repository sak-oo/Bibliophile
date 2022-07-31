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

public class ViewBooks extends AppCompatActivity {

    RecyclerView r;
    ViewBooksAdapter bd;
    public TextView t1;
    ArrayList<HashMap<String,String>> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);
        r=(RecyclerView)findViewById(R.id.rec);
        r.setLayoutManager(new LinearLayoutManager(this));


        DB_Conn obj = new DB_Conn();
        obj.execute();

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==1)
        {
            DB_Conn obj=new DB_Conn();
            obj.execute();
        }
    }
    public boolean onContextItemSelected(final MenuItem item){
       final HashMap<String,String> m = arrayList.get(item.getOrder());

        if(item.getTitle().toString().equalsIgnoreCase("Edit")){

            Intent i=new Intent(this,EditBook.class);
            i.putExtra("bno",m.get("bno"));
            i.putExtra("title",m.get("title"));
            i.putExtra("author",m.get("author"));
            i.putExtra("pub",m.get("publisher"));
            i.putExtra("desc",m.get("desc"));
            i.putExtra("sp",m.get("sp"));
            i.putExtra("rp",m.get("rp"));
            i.putExtra("type",m.get("type"));
            startActivityForResult(i,1);
            
        }
        else if(item.getTitle().toString().equalsIgnoreCase("Delete")){

            AlertDialog.Builder alert = new AlertDialog.Builder(ViewBooks.this);
            alert.setTitle("Delete book");
            alert.setMessage("Do you want to delete the selected book?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface obj, int x) {
                    arrayList.remove(item.getOrder());
                    bd.notifyDataSetChanged();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            delete(m.get("bno"));


                        }
                    };
                    Thread t = new Thread(runnable);
                    t.start();
                    Toast.makeText(ViewBooks.this, "Book deleted", Toast.LENGTH_LONG).show();
                }
            });
            alert.setNegativeButton("No", null);
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }

        else{
            return false;
        }

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


                    pst = con.prepareStatement("select * from books where userid=?");
                    pst.setString(1,userid);
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
                        hashMap.put("type", rs.getString("type"));
                        hashMap.put("status", rs.getString("status"));
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

                bd=new ViewBooksAdapter(ViewBooks.this,arrayList);

                r.setAdapter(bd);


            }
            else{
                AlertDialog.Builder alert = new AlertDialog.Builder(ViewBooks.this);
                alert.setTitle("Information");
                alert.setMessage("No Books to Display in Cart");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        ViewBooks.this.finish();
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
    public static void delete(String r1)
    {
        try
        {
            Connection con = DB_Connection.get_DBConnection();
            PreparedStatement pst = con.prepareStatement("delete from books where bno=?");
            pst.setString(1, r1);
            pst.executeUpdate();
            con.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}
