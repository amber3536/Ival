package com.hfad.ival_revisited;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.AsyncTask;
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
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class QuickstartActivity extends AppCompatActivity implements RecognitionListener {

    private BottomNavigationView bottomNavigationView;
    private AudioManager amanager;
    private NotificationManager notificationManager;
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
    private Boolean quickstart_position_mode = false;

    private String positionName;
    private static int makeCount = 0;
    private static int missCount = 0;
    private int totalMakeCount = 0;
    private int totalMissCount = 0;
    private int totalMakePrev = 0;
    private int totalMissPrev = 0;
    private int positionMakeCount = 0;
    private int positionMissCount = 0;
    private int positionMakePrev = 0;
    private int positionMissPrev = 0;
    private boolean turnOn = false;
    private long startTime;
    private Handler stopWatchHandler;
    private TextView timer;
    private DBHelper mydb;

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
        stopWatchHandler = new Handler();
        mydb = new DBHelper(this);
        displayPercentage();



        //recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        switch (getIntent().getStringExtra("EXTRA")) {
            case "positionFragment":
                Log.i("here", "onCreate: position intent");
                positionView();
                loadFragment(new PositionFragment(), "positionFragment");
                break;
            case "regularAccess":
                standardView();
                positionsPercentageOutput.setVisibility(View.INVISIBLE);
                quickstart_position_mode = false;
                break;
            case "statsFragment":
                loadFragment(new StatsFragment(), "statsFragment");
            default:
                positionsPercentageOutput.setVisibility(View.INVISIBLE);
                standardView();
                break;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivityForResult(intent, 55);
        }

        Log.i("here", "onCreate: " + amanager);
        if (amanager != null && notificationManager.isNotificationPolicyAccessGranted()) {
            Log.i("here", "onCreate: made it in");
            amanager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
            Log.i("here", "onCreate: " + amanager.getRingerMode());
        }

        if (isBluetoothHeadsetConnected()) {
            Log.i("bluetooth", "onCreate: YASSSSS");
            amanager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            amanager.startBluetoothSco();
            amanager.setBluetoothScoOn(true);
        }


        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!turnOn) {
                    requestAudioPermissions();
                    timer.setText(R.string.timer_txt);
                    makeOutput.setText("0");
                    missOutput.setText("0");
                    startTime = SystemClock.uptimeMillis();
                    stopWatchHandler.postDelayed(updateTimerThread, 0);
                    btnSwitch.setText(R.string.stop_txt);
                    turnOn = true;
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
                        loadFragment(new PositionFragment(), "positionFragment");
                    break;
                    case R.id.home:
                        finish();
                        break;

                    case R.id.stats:
                        loadFragment(new StatsFragment(), "statsFragment");
                        break;

                }
                return true;
            }
        });


    }

    public static boolean isBluetoothHeadsetConnected() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
                && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED;
    }

    public void clearDisplay() {
        timer.setText(R.string.timer_txt);
        makeOutput.setText("0");
        missOutput.setText("0");
        makeCount = 0;
        missCount = 0;
        stopWatchHandler.removeCallbacks(updateTimerThread);
        btnSwitch.setText(R.string.start_txt);
        positionMakePrev = 0;
        positionMissPrev = 0;
        turnOn = false;
    }

    public void firstTimeUserAdvice() {
        SharedPreferences sharedPreferences = getSharedPreferences("Ival", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("firstRun", true)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();
            Toast.makeText(QuickstartActivity.this, "Say \"make\" or \"miss\" when you make or miss a shot", Toast.LENGTH_LONG).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(QuickstartActivity.this, "Click start to begin recording", Toast.LENGTH_LONG).show();

                }
            }, 4000);

        }

    }

    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();

                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_PERMISSION);

            } else {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_PERMISSION);
            }
        }
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i("result", "requestAudioPermissions: ");
            speech = SpeechRecognizer.createSpeechRecognizer(this);
            Log.i("speech", "onCreate: " + SpeechRecognizer.isRecognitionAvailable(this));
            speech.setRecognitionListener(this);
            recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"US-en");
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speech.startListening(recognizerIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 55) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NotificationManager mNotificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
                assert mNotificationManager != null;
                if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
                    Intent intent = new Intent(
                            android.provider.Settings
                                    .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    // startActivity(intent);
                    startActivityForResult(intent, 55);
                }
                else {
                    firstTimeUserAdvice();
                }


            }
        }
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

    public void standardView() {
        timer.setVisibility(View.VISIBLE);
        btnSwitch.setEnabled(true);
        makeText.setVisibility(View.VISIBLE);
        missText.setVisibility(View.VISIBLE);
        makeOutput.setVisibility(View.VISIBLE);
        missOutput.setVisibility(View.VISIBLE);
        totalPercentageOutput.setVisibility(View.VISIBLE);
        positionsPercentageOutput.setVisibility(View.GONE);
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
                    speech = SpeechRecognizer.createSpeechRecognizer(this);
                    Log.i("speech", "onCreate: " + SpeechRecognizer.isRecognitionAvailable(this));
                    speech.setRecognitionListener(this);
                    recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"US-en");
                    recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
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
        if (amanager != null && notificationManager.isNotificationPolicyAccessGranted())
            amanager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0);

        if (isBluetoothHeadsetConnected()) {
            amanager.setMode(AudioManager.MODE_NORMAL);
            amanager.stopBluetoothSco();
            amanager.setBluetoothScoOn(false);
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.i("resume", "onResume: ");
        if (amanager != null && notificationManager.isNotificationPolicyAccessGranted())
            amanager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
       // speech.startListening(recognizerIntent);
    }


    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i("ready", "onReadyForSpeech");
        if (amanager != null && notificationManager.isNotificationPolicyAccessGranted())
            amanager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
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
//        if (turnOn)
//            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
        speech.startListening(recognizerIntent);
    }

    @Override
    public void onResults(Bundle results) {
        String make = "make";
        String miss = "miss";
        String text = "";

        Log.i("results", "onResults: ");

        ArrayList<String> matches = results .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Log.i("results", "onResults: " + matches);

        for (String result : matches)
            text = result + "\n";
        makeCount += StringUtils.countMatches(text, make);
        missCount += StringUtils.countMatches(text, miss);
        missCount += StringUtils.countMatches(text, "Miss");

        Log.i("results", "onResults: " + text + "make: " + makeCount);
        Log.i("results", "onResults: " + text + "miss: " + missCount);

        if (turnOn) {
            makeOutput.setText(String.valueOf(makeCount));
            missOutput.setText(String.valueOf(missCount));
            save();
            displayPercentage();

            if (quickstart_position_mode) {
                savePosition(positionName);
                displayPositionPercentage();
            }
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
//        SharedPreferences sharedPreferences = getSharedPreferences("num_shots", MODE_PRIVATE);
//        totalMakeCount = sharedPreferences.getInt("make_num", 0);
//        int makeNum = parseInt(makeOutput.getText().toString()) - totalMakePrev;
//        totalMakeCount += makeNum;
//        totalMissCount = sharedPreferences.getInt("miss_num", 0);
//        int missNum = parseInt(missOutput.getText().toString()) - totalMissPrev;
//        totalMissCount += missNum;
//
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("make_num", totalMakeCount);
//        editor.putInt("miss_num", totalMissCount);
//
//        Log.i("saved", "save make count: " + totalMakeCount);
//        editor.apply();
//        totalMakePrev = parseInt(makeOutput.getText().toString());
//        totalMissPrev = parseInt(missOutput.getText().toString());
        int makeNum = parseInt(makeOutput.getText().toString()) - totalMakePrev;
        int missNum = parseInt(missOutput.getText().toString()) - totalMissPrev;

        LocalDate localDate = LocalDate.now();
        mydb.insertContact("Total accuracy", makeNum, missNum, localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear(), localDate.getDayOfYear());

        totalMakePrev = parseInt(makeOutput.getText().toString());
        totalMissPrev = parseInt(missOutput.getText().toString());
    }

    public void savePosition(String posName) {
//        SharedPreferences sharedPreferences = getSharedPreferences(posName, MODE_PRIVATE);
//        positionMakeCount = sharedPreferences.getInt("pos_make_num", 0);
//        Log.i("saved", "savePosition: " + positionMakeCount + " " + makeOutput.getText().toString());
//        Log.i("saved", "savePosition: " + LocalDateTime.now());
//
//        int madeNum = parseInt(makeOutput.getText().toString()) - positionMakePrev;
//        positionMakeCount += madeNum;
//        positionMissCount = sharedPreferences.getInt("pos_miss_num", 0);
//        int missedNum = parseInt(missOutput.getText().toString()) - positionMissPrev;
//        positionMissCount += missedNum;

        int madeNum = parseInt(makeOutput.getText().toString()) - positionMakePrev;
        int missNum = parseInt(missOutput.getText().toString()) - positionMissPrev;

        LocalDate localDate = LocalDate.now();
        mydb.insertContact(posName, madeNum, missNum, localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear(), localDate.getDayOfYear());

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("pos_make_num", positionMakeCount);
//        editor.putInt("pos_miss_num", positionMissCount);

       // editor.putLong("timestamp", System.currentTimeMillis());

      //  Log.i("saved", "save pos make count: " + positionMakeCount);
//        editor.apply();
        positionMakePrev = parseInt(makeOutput.getText().toString());
        positionMissPrev = parseInt(missOutput.getText().toString());
    }

    public void displayPositionPercentage() {
//        SharedPreferences sharedPreferences = getSharedPreferences(positionName, MODE_PRIVATE);
//        positionMakeCount = sharedPreferences.getInt("pos_make_num", 0);
//        positionMissCount = sharedPreferences.getInt("pos_miss_num", 0);
        positionMakeCount = mydb.totalShotsMade(positionName);
        positionMissCount = mydb.totalShotsMissed(positionName);
        Log.i("percent", "displayPositionPercentage: makeCount" + positionMakeCount);
        Log.i("percent", "displayPositionPercentage: missCount" + positionMissCount);
        float accuracyCount = (float) positionMakeCount / (positionMissCount + positionMakeCount) * 100;
        int accuracyCountRounded = Math.round(accuracyCount);
        String str = getResources().getString(R.string.pos_accuracy_txt, accuracyCountRounded);
        positionsPercentageOutput.setText(str);
    }

    public int returnPositionAccuracy(String pos) {
//        SharedPreferences sharedPreferences = getSharedPreferences(pos, MODE_PRIVATE);
//        positionMakeCount = sharedPreferences.getInt("pos_make_num", 0);
//        positionMissCount = sharedPreferences.getInt("pos_miss_num", 0);
        positionMakeCount = mydb.totalShotsMade(pos);
        positionMissCount = mydb.totalShotsMissed(pos);
        Log.i("percent", "displayPositionPercentage: makeCount" + positionMakeCount);
        Log.i("percent", "displayPositionPercentage: missCount" + positionMissCount);
        float accuracyCount = (float) positionMakeCount / (positionMissCount + positionMakeCount) * 100;
        int accuracyCountRounded = Math.round(accuracyCount);
        return accuracyCountRounded;
    }

    public int getMakeCount() {
        return positionMakeCount;
    }

    public int getMissCount() {
        return positionMissCount;
    }

    public void displayPercentage() {
//        SharedPreferences sharedPreferences = getSharedPreferences("num_shots", MODE_PRIVATE);
//        totalMakeCount = sharedPreferences.getInt("make_num", 0);
//        totalMissCount = sharedPreferences.getInt("miss_num", 0);

        totalMakeCount = mydb.totalShotsMade("Total accuracy");
        totalMissCount = mydb.totalShotsMissed("Total accuracy");
        Log.i("percent", "displayPercentage: makeCount" + totalMakeCount);
        Log.i("percent", "displayPercentage: missCount" + totalMissCount);
        float accuracyCount = (float) totalMakeCount / (totalMakeCount + totalMissCount) * 100;
        int accuracyCountRounded = Math.round(accuracyCount);
        String str = getResources().getString(R.string.total_accuracy_txt, accuracyCountRounded);
        totalPercentageOutput.setText(str);
    }




    public void loadFragment(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void loadFragment(Fragment fragment, String key, String message) {
        FragmentManager fm = getSupportFragmentManager();
        Bundle result = new Bundle();
        result.putString(key, message);
        fragment.setArguments(result);

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setPositionName(String pos) {
        positionName = pos;
    }
    @Override
    public void onBackPressed() {
        PositionFragment positionFragment = (PositionFragment)getSupportFragmentManager().findFragmentByTag("positionFragment");
        StatsFragment statsFragment = (StatsFragment)getSupportFragmentManager().findFragmentByTag("statsFragment");

        if (positionFragment != null && positionFragment.isVisible()) {
            standardView();
            super.onBackPressed();
        }
        else if (statsFragment != null && statsFragment.isVisible()) {
            standardView();
            super.onBackPressed();
        }
        else
            super.onBackPressed();
    }

}



