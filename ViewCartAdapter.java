package tyitproject.booksonthego;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewCartAdapter extends RecyclerView.Adapter<ViewCartAdapter.MyViewHolder>  {

    private  ArrayList<HashMap<String,String>> arrayList;
    private Context c;

    public ViewCartAdapter(Context c,ArrayList<HashMap<String,String>> arrayList) {
        this.c=c;
        this.arrayList = arrayList;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cartlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        HashMap<String,String> m = arrayList.get(position);
        holder.txt_bno.setText(m.get("bno"));
        holder.txt_title.setText(m.get("title"));
        holder.txt_price.setText(m.get("price") + "/-");
        holder.txt_status.setText(m.get("v"));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_bno,txt_title,txt_price,txt_status;
        ImageButton b1;
        public ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            final ViewCart vc=(ViewCart)c;

            txt_bno = (TextView) view.findViewById(R.id.txt_bno);
            txt_title = (TextView) view.findViewById(R.id.txt_title);
            txt_price = (TextView) view.findViewById(R.id.txt_price);
            txt_status = (TextView) view.findViewById(R.id.txt_status);
            b1=(ImageButton)view.findViewById(R.id.imageButton);

            b1.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v){

                    SharedPreferences sp = c.getSharedPreferences("pf1", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed = sp.edit();
                    ed.remove(txt_bno.getText().toString());
                    ed.commit();
                    arrayList.remove(getAdapterPosition());
                    //int a=getAdapterPosition();
                    notifyDataSetChanged();

                    int total=0;
                    for(HashMap<String,String> m:arrayList)
                    {
                        total=total+Integer.parseInt(m.get("price"));
                    }

                    vc.t1.setText(total+"");

                }

            });


        }

    }


}
