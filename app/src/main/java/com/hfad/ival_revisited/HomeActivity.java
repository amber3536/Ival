package com.hfad.ival_revisited;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //builder.setMessage("hey")
                builder.setMessage("Do Not Disturb access must be granted for the app to work")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                
                            }
                        })
                        .show();
                //builder.create();
           // Toast.makeText(HomeActivity.this, "Do Not Disturb access must be granted for the app to work", Toast.LENGTH_LONG).show();
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

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

}
