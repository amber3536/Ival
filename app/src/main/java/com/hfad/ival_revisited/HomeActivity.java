package com.hfad.ival_revisited;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button quickstartBtn;
    private Button positionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        quickstartBtn = findViewById(R.id.quickstart);
        quickstartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, QuickstartActivity.class);
                intent.putExtra("EXTRA", "regularAccess");
                startActivity(intent);
            }
        });

        positionBtn = findViewById(R.id.position);
        positionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, QuickstartActivity.class);
                intent.putExtra("EXTRA", "openFragment");
                startActivity(intent);
            }
        });
    }
}
