package com.android.whichcollege.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.whichcollege.R;

public class PlusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plus_action);

        RelativeLayout createGroup = (RelativeLayout) findViewById(R.id.relativeLayout3);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent = new Intent(PlusActivity.this, AddGroupActivity.class);
                 startActivity(intent);
                 finish();
            }
        });

        View back = (View) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlusActivity.this,GroupFragment.class);
                finish();;
            }
        });





    }
}
