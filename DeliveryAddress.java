package tyitproject.booksonthego;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DeliveryAddress extends AppCompatActivity {

    Button b1;
    TextView t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        final String amt=getIntent().getStringExtra("amt");
        t1=(TextView)findViewById(R.id.txt_address);
        b1=(Button)findViewById(R.id.button11);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!t1.getText().toString().equals("")) {
                    Intent i = new Intent(DeliveryAddress.this, Payment.class);
                    i.putExtra("address",t1.getText().toString());
                    i.putExtra("amt", amt);
                    startActivity(i);
                }
                else{
                    t1.setError("Value is required");
                }
            }
        });
    }
}
