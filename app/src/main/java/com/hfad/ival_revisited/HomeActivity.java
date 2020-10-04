package com.hfad.ival_revisited;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private Button quickstartBtn;
    private Button positionBtn;
    private Button statsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (!notificationManager.isNotificationPolicyAccessGranted()) {
            Toast.makeText(HomeActivity.this, "Do Not Disturb access must be granted for the app to work", Toast.LENGTH_LONG).show();
        }
        notificationManager.cancelAll();

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
                intent.putExtra("EXTRA", "positionFragment");
                startActivity(intent);
            }
        });

        statsBtn = findViewById(R.id.stats);
        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, QuickstartActivity.class);
                intent.putExtra("EXTRA", "statsFragment");
                startActivity(intent);
            }
        });

        ImageView image = (ImageView)findViewById(R.id.basketball_home);
        Animation animation1 =
                AnimationUtils.loadAnimation(getBaseContext(), R.anim.move);
        image.startAnimation(animation1);
    }

}
