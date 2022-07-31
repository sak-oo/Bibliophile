package tyitproject.booksonthego;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shruti on 9/1/2018.
 */
public class ViewBooksAdapter_User extends RecyclerView.Adapter<ViewBooksAdapter_User.MyViewHolder>  {

    private  ArrayList<HashMap<String,String>> arrayList;
    private Context c;

    public ViewBooksAdapter_User(Context c, ArrayList<HashMap<String, String>> arrayList) {
        this.c=c;
        this.arrayList = arrayList;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookslist_user, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HashMap<String,String> m = arrayList.get(position);

        holder.txt_title.setText("Title: "+m.get("title"));
        holder.txt_sp.setText("Selling Price: "+m.get("sp")+"/-");
        holder.txt_rp.setText("Rent Price: "+m.get("rp")+"/-");
        holder.txt_bno.setText("Book No.: "+m.get("bno"));
        holder.txt_desc.setText("Description: "+m.get("desc"));
        holder.txt_author.setText("Author: "+m.get("author"));
        holder.txt_pub.setText("Pub: " + m.get("publisher"));
        if(m.get("rp").equals("0")){
            holder.b2.setEnabled(false);
        }
        if(m.get("sp").equals("0")){
            holder.b1.setEnabled(false);
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView txt_bno,txt_rp,txt_sp,txt_title,txt_author,txt_pub,txt_type,txt_desc;
        public Button b1,b2;

        public MyViewHolder(View view) {
            super(view);

            txt_bno = (TextView) view.findViewById(R.id.txt_bno);
            txt_title = (TextView) view.findViewById(R.id.txt_title);
            txt_desc = (TextView) view.findViewById(R.id.txt_desc);
            txt_rp = (TextView) view.findViewById(R.id.txt_rp);
            txt_sp = (TextView) view.findViewById(R.id.txt_sp);
            txt_author = (TextView) view.findViewById(R.id.txt_author);
            txt_pub = (TextView) view.findViewById(R.id.txt_pub);
            b1=(Button)view.findViewById(R.id.button5);
            b2=(Button)view.findViewById(R.id.button14);

            b1.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v){
                    boolean f=false;
                    SharedPreferences sp = c.getSharedPreferences("pf1", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    Map<String,?> keys = sp.getAll();

                    for(Map.Entry<String,?> entry : keys.entrySet()){
                        if(entry.getKey().toString().equals(txt_bno.getText().toString().substring(10)))
                        {

                            Toast.makeText(c, "Book already present in the cart", Toast.LENGTH_SHORT).show();
                            f=true;
                            break;
                        }
                    }
                    if(f==false){
                        Log.i("bno", txt_bno.getText().toString().substring(10));
                        ed.putString(txt_bno.getText().toString().substring(10), "S");
                        ed.commit();
                        Toast.makeText(c,"Book added to cart",Toast.LENGTH_SHORT).show();
                    }



                }

            });
            b2.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v){
                    boolean f=false;
                    SharedPreferences sp = c.getSharedPreferences("pf1", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    Map<String,?> keys = sp.getAll();

                    for(Map.Entry<String,?> entry : keys.entrySet()){
                        if(entry.getKey().toString().equals(txt_bno.getText().toString().substring(10)))
                        {
                            Toast.makeText(c, "Book already present in the cart", Toast.LENGTH_SHORT).show();
                            f=true;
                            break;
                        }
                    }
                    if(f==false){
                        ed.putString(txt_bno.getText().toString().substring(10), "R");
                        ed.commit();
                        Toast.makeText(c,"Book added to cart",Toast.LENGTH_SHORT).show();
                    }



                }

            });
        }



    }


}
