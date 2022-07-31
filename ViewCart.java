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
import android.widget.Button;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewCart extends AppCompatActivity {

    RecyclerView r;
    ViewCartAdapter bd;
    TextView t1;
    Button b1;
    int total=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        r=(RecyclerView)findViewById(R.id.rec2);
        r.setLayoutManager(new LinearLayoutManager(this));
        t1=(TextView)findViewById(R.id.txt_total);

        DB_Conn obj = new DB_Conn();
        obj.execute();

        b1=(Button)findViewById(R.id.button9);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewCart.this, DeliveryAddress.class);
                i.putExtra("amt", total + "");
                startActivity(i);
            }
        });

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

        ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();

        @Override
        public String doInBackground(String...arg) //compulsory to implement
        {
            String r="";
            PreparedStatement pst;
            ResultSet rs;
            try {
                Connection con=DB_Connection.get_DBConnection();
                SharedPreferences sp = getSharedPreferences("pf1", Context.MODE_PRIVATE);

                Map<String,?> keys = sp.getAll();

                for(Map.Entry<String,?> entry : keys.entrySet()){
                    Log.i("bnoa", entry.getKey() );
                    pst = con.prepareStatement("select * from books where bno=?");
                    pst.setString(1, entry.getKey());
                    rs = pst.executeQuery();
                    rs.next();
                    HashMap<String,String> hashMap=new HashMap<>();
                    hashMap.put("bno", rs.getString("bno"));
                    hashMap.put("title", rs.getString("title"));

                    if(entry.getValue().equals("R")){
                        hashMap.put("v", "R");
                        hashMap.put("price", rs.getString("rp"));
                        total=total+(rs.getInt("rp"));
                    }
                    else{
                        hashMap.put("v", "");
                        hashMap.put("price", rs.getString("sp"));
                        total=total+(rs.getInt("sp"));
                    }
                    arrayList.add(hashMap);

                    pst.close();
                    rs.close();

                }
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

                bd=new ViewCartAdapter(ViewCart.this,arrayList);

                r.setAdapter(bd);
                t1.setText(total+"/-");

            }
            else{
                AlertDialog.Builder alert = new AlertDialog.Builder(ViewCart.this);
                alert.setTitle("Information");
                alert.setMessage("No Books to display in cart");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface obj, int x) {
                        ViewCart.this.finish();
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
