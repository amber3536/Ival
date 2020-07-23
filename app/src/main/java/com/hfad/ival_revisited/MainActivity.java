package com.hfad.ival_revisited;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecognitionListener {

    private Button btnSwitch;
    private static final int REQUEST_RECORD_PERMISSION = 100;
    private TextView makeOutput;
    private TextView missOutput;
    SpeechRecognizer speech;
    private Intent recognizerIntent;
    private Boolean activated = false;
    private String make = "make";
    private String miss = "miss";
    private static int makeCount = 0;
    private static int missCount = 0;
    private boolean turnOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSwitch = findViewById(R.id.startStopBtn);
        makeOutput= (TextView) findViewById(R.id.showMake);
        missOutput= (TextView) findViewById(R.id.showMiss);
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        Log.i("speech", "onCreate: " + SpeechRecognizer.isRecognitionAvailable(this));
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,"US-en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        AudioManager amanager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        amanager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (turnOn == false) {
                    btnSwitch.setText("STOP");
                    turnOn = true;
                    if (activated == false) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_PERMISSION);
                        activated = true;
                    }
                }
                else {
                    makeCount = 0;
                    missCount = 0;
                    btnSwitch.setText("START");
                    turnOn = false;
                }
            }
        });


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
        makeOutput.setText(errorMessage);
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
        Log.i("results", "onResults: " + text + "make: " + makeCount);
        Log.i("results", "onResults: " + text + "miss: " + missCount);
        makeOutput.setText(String.valueOf(makeCount));
        missOutput.setText(String.valueOf(missCount));
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

}



