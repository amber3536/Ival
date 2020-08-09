package com.hfad.ival_revisited;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

public class MainActivity extends AppCompatActivity implements RecognitionListener {

    private Button btnSwitch;
    private static final int REQUEST_RECORD_PERMISSION = 100;
    private TextView makeOutput;
    private TextView missOutput;
    private TextView percentageOutput;
    private BottomNavigationView bottomNavigationView;
    SpeechRecognizer speech;
    private Intent recognizerIntent;
    private AudioManager amanager;
    private Boolean activated = false;
    private String make = "make";
    private String miss = "miss";
    private static int makeCount = 0;
    private static int missCount = 0;
    private int totalMakeCount = 0;
    private int totalMissCount = 0;
    private boolean turnOn = false;
    private long startTime;
    private Handler stopWatchHandler;
    private TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSwitch = findViewById(R.id.startStopBtn);
        makeOutput= (TextView) findViewById(R.id.showMake);
        missOutput= (TextView) findViewById(R.id.showMiss);
        percentageOutput = findViewById(R.id.percentage);
        timer = (TextView) findViewById(R.id.simpleTimer);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
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

        //TODO Make Do Not Disturb access straightforward
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }

        if (amanager != null) {
            amanager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_TOGGLE_MUTE, 0);
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
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSION);
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
                        btnSwitch.setEnabled(false);
                        loadFragment(new PositionFragment());
                    break;

                }
                return true;
            }
        });


    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_tab0:
//
//                break;
//            case R.id.action_tab1:
//
//                break;
//        }
//        return true; // not false!
//    }

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
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast .LENGTH_SHORT).show();
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
        totalMakeCount += parseInt(makeOutput.getText().toString());
        totalMissCount = sharedPreferences.getInt("miss_num", 0);
        totalMissCount += parseInt(missOutput.getText().toString());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("make_num", totalMakeCount);
        editor.putInt("miss_num", totalMissCount);

        Log.i("saved", "save make count: " + totalMakeCount);
        editor.apply();
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
        percentageOutput.setText(str);
    }

    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

}



