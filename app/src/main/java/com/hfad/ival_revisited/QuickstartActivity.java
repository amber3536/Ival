package com.hfad.ival_revisited;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
//import android.app.Fragment;
import androidx.fragment.app.Fragment;
//import android.app.FragmentManager;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
//import android.app.FragmentTransaction;
//import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class QuickstartActivity extends AppCompatActivity implements RecognitionListener {

    private BottomNavigationView bottomNavigationView;
    private AudioManager amanager;
    private Button btnSwitch;
    private static final int REQUEST_RECORD_PERMISSION = 100;
    SpeechRecognizer speech;
    private Intent recognizerIntent;
    private TextView makeOutput;
    private TextView missOutput;
    private TextView makeText;
    private TextView missText;
    private TextView totalPercentageOutput;
    private TextView positionsPercentageOutput;

    private Boolean activated = false;
    private Boolean quickstart_position_mode = false;

    private String make = "make";
    private String miss = "miss";
    private static int makeCount = 0;
    private static int missCount = 0;
    private int totalMakeCount = 0;
    private int totalMissCount = 0;
    private int totalMakePrev = 0;
    private int totalMissPrev = 0;
    private int positionMakeCount = 0;
    private int positionMissCount = 0;
    private boolean turnOn = false;
    private long startTime;
    private Handler stopWatchHandler;
    private TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        btnSwitch = findViewById(R.id.startStopBtn);
        makeOutput= (TextView) findViewById(R.id.showMake);
        missOutput= (TextView) findViewById(R.id.showMiss);
        makeText = findViewById(R.id.textMake);
        missText = findViewById(R.id.textMiss);
        totalPercentageOutput = findViewById(R.id.total_percentage);
        positionsPercentageOutput = findViewById(R.id.position_percentage);
        timer = (TextView) findViewById(R.id.simpleTimer);
        displayPercentage();
        stopWatchHandler = new Handler();

        speech = SpeechRecognizer.createSpeechRecognizer(this);
        Log.i("speech", "onCreate: " + SpeechRecognizer.isRecognitionAvailable(this));
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"US-en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        switch (getIntent().getStringExtra("EXTRA")) {
            case "positionFragment":
                Log.i("here", "onCreate: position intent");
                positionView();
                loadFragment(new PositionFragment());
                break;
            case "regularAccess":
                quickstart_position_mode = false;
                loadFragment(new QuickstartFragment());
                //Log.i("here", "onCreate: "+ amanager.getRingerMode());
                break;
            default:
//                positionsPercentageOutput.setVisibility(View.VISIBLE);
//                positionsPercentageOutput.setText("bottom shot");
                loadFragment(new QuickstartFragment());
                break;
        }


        //TODO Make Do Not Disturb access straightforward
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }

        Log.i("here", "onCreate: " + amanager);
        if (amanager != null) {
            Log.i("here", "onCreate: made it in");
            amanager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
        }




        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!turnOn) {
                    timer.setText(R.string.timer_txt);
                    makeOutput.setText("0");
                    missOutput.setText("0");
                    startTime = SystemClock.uptimeMillis();
                    stopWatchHandler.postDelayed(updateTimerThread, 0);
                    btnSwitch.setText(R.string.stop_txt);
                    turnOn = true;
                    if (!activated) {
                        ActivityCompat.requestPermissions(QuickstartActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSION);
                        activated = true;
                    }
                }
                else {
                    stopWatchHandler.removeCallbacks(updateTimerThread);
                    makeCount = 0;
                    missCount = 0;
                    btnSwitch.setText(R.string.start_txt);
                    turnOn = false;
                }
            }
        });



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.position:
                        //btnSwitch.setEnabled(false);
                        //timer.setVisibility(View.GONE);
                        positionView();
                        loadFragment(new PositionFragment());
                    break;
                    case R.id.home:
                        finish();
                        break;

                }
                return true;
            }
        });


    }

    public void positionView() {
        timer.setVisibility(View.GONE);
        btnSwitch.setEnabled(false);
        makeText.setVisibility(View.GONE);
        missText.setVisibility(View.GONE);
        makeOutput.setVisibility(View.GONE);
        missOutput.setVisibility(View.GONE);
        totalPercentageOutput.setVisibility(View.GONE);
        positionsPercentageOutput.setVisibility(View.GONE);
    }

    public void showView() {
        quickstart_position_mode = true;
        timer.setVisibility(View.VISIBLE);
        btnSwitch.setEnabled(true);
        makeText.setVisibility(View.VISIBLE);
        missText.setVisibility(View.VISIBLE);
        makeOutput.setVisibility(View.VISIBLE);
        missOutput.setVisibility(View.VISIBLE);
        totalPercentageOutput.setVisibility(View.VISIBLE);
        positionsPercentageOutput.setVisibility(View.VISIBLE);
        displayPositionPercentage();
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            long elapsedMiliseconds = (SystemClock.uptimeMillis() - startTime);
            updateTime(elapsedMiliseconds);
            stopWatchHandler.postDelayed(this, 0);
        }
    };

    private void updateTime(long updatedTime) {
        DateFormat format = new SimpleDateFormat("mm:ss.SS");
        String displayTime = format.format(updatedTime);
        timer.setText(displayTime);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_PERMISSION:
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    Log.i("permissions", "onRequestPermissionsResult: ");
                    speech.startListening(recognizerIntent);
                } else {
                    Toast.makeText(QuickstartActivity.this, "Permission Denied!", Toast .LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (speech != null) {
            speech.destroy();
            Log.i("destroy", "destroy");
        }
        amanager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0);
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.i("resume", "onResume: ");
        amanager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
    }


    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i("ready", "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        Log.i("end", "onEndOfSpeech: ");
    }

    @Override
    public void onError(int error) {
        String errorMessage = getErrorText(error);
        Log.i("error", "onError: " + errorMessage);
        //makeOutput.setText(errorMessage);
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onResults(Bundle results) {
        Log.i("results", "onResults: ");
        ArrayList<String> matches = results .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.i("results", "onResults: " + matches);
        String text = "";
        for (String result : matches)
            text = result + "\n";
        makeCount += StringUtils.countMatches(text, make);
        missCount += StringUtils.countMatches(text, miss);
        missCount += StringUtils.countMatches(text, "Miss");
        Log.i("results", "onResults: " + text + "make: " + makeCount);
        Log.i("results", "onResults: " + text + "miss: " + missCount);
        makeOutput.setText(String.valueOf(makeCount));
        missOutput.setText(String.valueOf(missCount));
        save();
        displayPercentage();
        if (quickstart_position_mode) {
            savePosition();
            displayPositionPercentage();
        }
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }


    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        Log.i("error", "getErrorText: " + message);
        return message;
    }

    public void save() {
        SharedPreferences sharedPreferences = getSharedPreferences("num_shots", MODE_PRIVATE);
        totalMakeCount = sharedPreferences.getInt("make_num", 0);
        int num = parseInt(makeOutput.getText().toString()) - totalMakePrev;
        totalMakeCount += num;
        totalMissCount = sharedPreferences.getInt("miss_num", 0);
        num = parseInt(missOutput.getText().toString()) - totalMissPrev;
        totalMissCount += num;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("make_num", totalMakeCount);
        editor.putInt("miss_num", totalMissCount);

        Log.i("saved", "save make count: " + totalMakeCount);
        editor.apply();
        totalMakePrev = parseInt(makeOutput.getText().toString());
        totalMissPrev = parseInt(missOutput.getText().toString());
    }

    public void savePosition() {
        SharedPreferences sharedPreferences = getSharedPreferences("num_position_shots", MODE_PRIVATE);
        positionMakeCount = sharedPreferences.getInt("pos_make_num", 0);
        Log.i("saved", "savePosition: " + positionMakeCount + " " + makeOutput.getText().toString());
        positionMakeCount += parseInt(makeOutput.getText().toString());
        positionMissCount = sharedPreferences.getInt("pos_miss_num", 0);
        positionMissCount += parseInt(missOutput.getText().toString());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("pos_make_num", positionMakeCount);
        editor.putInt("pos_miss_num", positionMissCount);

        Log.i("saved", "save pos make count: " + positionMakeCount);
        editor.apply();
    }

    public void displayPositionPercentage() {
        SharedPreferences sharedPreferences = getSharedPreferences("num_position_shots", MODE_PRIVATE);
        positionMakeCount = sharedPreferences.getInt("pos_make_num", 0);
        positionMissCount = sharedPreferences.getInt("pos_miss_num", 0);
        Log.i("percent", "displayPositionPercentage: makeCount" + positionMakeCount);
        Log.i("percent", "displayPositionPercentage: missCount" + positionMissCount);
        float accuracyCount = (float) positionMakeCount / (positionMissCount + positionMakeCount) * 100;
        int accuracyCountRounded = Math.round(accuracyCount);
        String str = getResources().getString(R.string.pos_accuracy_txt, accuracyCountRounded);
        positionsPercentageOutput.setText(str);
    }

    public void displayPercentage() {
        SharedPreferences sharedPreferences = getSharedPreferences("num_shots", MODE_PRIVATE);
        totalMakeCount = sharedPreferences.getInt("make_num", 0);
        totalMissCount = sharedPreferences.getInt("miss_num", 0);
        Log.i("percent", "displayPercentage: makeCount" + totalMakeCount);
        Log.i("percent", "displayPercentage: missCount" + totalMissCount);
        float accuracyCount = (float) totalMakeCount / (totalMakeCount + totalMissCount) * 100;
        int accuracyCountRounded = Math.round(accuracyCount);
        String str = getResources().getString(R.string.accuracy_txt, accuracyCountRounded);
        totalPercentageOutput.setText(str);
    }




    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

}



