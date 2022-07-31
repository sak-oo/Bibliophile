package tyitproject.booksonthego;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shruti on 9/1/2018.
 */
public class ViewBooksAdapter extends RecyclerView.Adapter<ViewBooksAdapter.MyViewHolder>  {

    private  ArrayList<HashMap<String,String>> arrayList;
    private Context c;

    public ViewBooksAdapter(Context c,ArrayList<HashMap<String,String>> arrayList) {
        this.c=c;
        this.arrayList = arrayList;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookslist, parent, false);

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
        holder.txt_type.setText("Type: "+m.get("type"));
        holder.txt_author.setText("Author: "+m.get("author"));
        holder.txt_pub.setText("Pub: " + m.get("publisher"));
        holder.txt_status.setText("Status: " + m.get("status"));
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView txt_bno,txt_rp,txt_sp,txt_title,txt_author,txt_pub,txt_type,txt_desc,txt_status;


        public MyViewHolder(View view) {
            super(view);
            view.setOnCreateContextMenuListener(this);
            txt_bno = (TextView) view.findViewById(R.id.txt_bno);
            txt_title = (TextView) view.findViewById(R.id.txt_title);
            txt_desc = (TextView) view.findViewById(R.id.txt_desc);
            txt_rp = (TextView) view.findViewById(R.id.txt_rp);
            txt_sp = (TextView) view.findViewById(R.id.txt_sp);
            txt_author = (TextView) view.findViewById(R.id.txt_author);
            txt_pub = (TextView) view.findViewById(R.id.txt_pub);
            txt_type = (TextView) view.findViewById(R.id.txt_type);
            txt_status = (TextView) view.findViewById(R.id.txt_status);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select an Action");
            menu.add(2, v.getId(), getAdapterPosition(), "Edit");//groupId, itemId, order, title
            menu.add(3, v.getId(), getAdapterPosition(), "Delete");

        }

    }


}
