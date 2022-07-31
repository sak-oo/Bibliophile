package tyitproject.booksonthego;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


public class ViewEBooksAdapter extends RecyclerView.Adapter<ViewEBooksAdapter.MyViewHolder>  {

    private  ArrayList<HashMap<String,String>> arrayList;
    private static Context c;

    public ViewEBooksAdapter(Context c, ArrayList<HashMap<String, String>> arrayList) {
        this.c=c;
        this.arrayList = arrayList;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ebooklist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HashMap<String,String> m = arrayList.get(position);

        holder.txt_eno.setText(""+m.get("eno"));
        holder.txt_name.setText("Book Name: "+m.get("name"));
        holder.txt_userid.setText("UserID: "+m.get("userid"));

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_eno,txt_name,txt_userid;
        Button b1;

        public MyViewHolder(View view) {
            super(view);

            txt_eno = (TextView) view.findViewById(R.id.txt_eno);
            txt_name = (TextView) view.findViewById(R.id.txt_name);
            txt_userid = (TextView) view.findViewById(R.id.txt_userid);
            b1=(Button) view.findViewById(R.id.button24);
            b1.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v){
                    final Handler handler = new Handler(Looper.getMainLooper());
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            download(txt_eno.getText().toString());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(c, "File Downloaded", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    };
                    Thread t = new Thread(runnable);
                    t.start();



                }

            });

        }



    }
    public static void download(String r1)
    {
        try
        {
            Connection con = DB_Connection.get_DBConnection();
            PreparedStatement pst = con.prepareStatement("select * from ebooks where eno=?");
            pst.setString(1, r1);
            ResultSet rs = pst.executeQuery();
            rs.next();
            Blob blob = rs.getBlob("book");
            InputStream is = blob.getBinaryStream();
            File f = new File(c.getExternalFilesDir(null),rs.getString("name")+".pdf");
            FileOutputStream fos = new FileOutputStream(f);

            int b = 0;
            while ((b = is.read()) != -1)
            {
                fos.write(b);
            }
            fos.flush();
            fos.close();


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


}
