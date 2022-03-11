package com.example.smartdoorlock2;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class LockOnActivity extends AppCompatActivity {
    private ImageButton lockon;
    Boolean check=true;
    LinearLayout l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockon);
        l=(LinearLayout)findViewById(R.id.mainlayout);
        lockon=findViewById(R.id.lockon);
        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            }
        });

    }
    public void onClick02(View v){
        if(check==true){
            lockon.setImageResource(R.drawable.lockoff);
            l.setBackgroundResource(R.drawable.lockoffgradient);
            check=false;
        }else{
            lockon.setImageResource(R.drawable.lock);
            l.setBackgroundResource(R.drawable.lockongradient);
            check=true;
        }
    }
}
