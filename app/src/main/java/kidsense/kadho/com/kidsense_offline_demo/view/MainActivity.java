package kidsense.kadho.com.kidsense_offline_demo.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.kadho.android.media.KidsenseAudioRecorder;
import com.kadho.android.sdk.asr.offline.KidsenseOfflineManager;

import com.kadho.kidsense.kidsense_en_medium_v2.Kidsense_en_medium_v2;
import com.kadho.kidsense.kidsense_en_small_v2.Kidsense_en_small_v2;
import com.kadho.kidsense.kidsense_en_large_v2.Kidsense_en_large_v2;

import kidsense.kadho.com.kidsense_offline_demo.Configs;
import kidsense.kadho.com.kidsense_offline_demo.R;


import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MainActivity extends AppCompatActivity implements KidsenseAudioRecorder.KidsenseAudioRecorderListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, KidsenseOfflineManager.KidsenseOfflineEvent
{
    private static String TAG = "Kidsense";
    public static MainActivity Instance;
    public static boolean IS_SPEAKING = false;

    private KidsenseAudioRecorder _audioRecorder;
    private KidsenseOfflineManager _koManager;

    private Switch VadSwitch;
    private TextView _tvBox, _tvBox2;
    private ProgressDialog _dialog;

    private Filter filter;
    private String userID =  null;

    private boolean isAllPermissionsGranted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_offline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Instance = this;


        if (!checkPermission()) {
            requestPermission();
        }else {
            isAllPermissionsGranted = true;
        }
        if (isAllPermissionsGranted){
            initModel();
        }

        this.userID = getIntent().getStringExtra("userID");
    }

    public void initModel(){
        _koManager = KidsenseOfflineManager.getInstance(MainActivity.this);

        // HERE YOU INITIALIZE A MODEL
          String configPath = Kidsense_en_medium_v2.autoSync(MainActivity.this);
//          String configPath = Kidsense_en_small_v2.autoSync(MainActivity.this);
//        String configPath = Kidsense_en_large_v2.autoSync(MainActivity.this);
        _koManager.initModel(configPath,"5afk87h90kln05963vzpig9jvb");

        setButtonHandlers();
        enableButtons(false, Configs.IS_USE_LOCAL_VAD);

        _audioRecorder = KidsenseAudioRecorder.getInstance();

        VadSwitch = (Switch)findViewById(R.id.switchVAD);
        VadSwitch.setOnCheckedChangeListener(this);

        _tvBox = (TextView)findViewById(R.id.textView);
        _tvBox.setMovementMethod(new ScrollingMovementMethod());
        _tvBox2 = (TextView)findViewById(R.id.textView2);
        _tvBox2.setMovementMethod(new ScrollingMovementMethod());

        //filter
        filter = Filter.getFilter(this);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(),RECORD_AUDIO);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(),INTERNET);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        int permsRequestCode = 200;
        String[] perms = {READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE,INTERNET,RECORD_AUDIO};
        ActivityCompat.requestPermissions(this, perms, permsRequestCode);
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults){
        switch(permsRequestCode){
            case 200:
                if (grantResults.length > 0) {

                    boolean readAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    boolean recordAccepted = grantResults[2]==PackageManager.PERMISSION_GRANTED;
                    boolean internetccepted = grantResults[3]==PackageManager.PERMISSION_GRANTED;

                    if (readAccepted && writeAccepted && recordAccepted && internetccepted){
                        Log.e("KidsenseAssessment","All permissions are accepted");
                        isAllPermissionsGranted = true;
                        initModel();
                    }
                    else {
                        Log.e("KidsenseAssessment","Some permissions are not accepted");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to all the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{INTERNET, RECORD_AUDIO,READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE},
                                                            200);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    public void onStop()
    {
        super.onStop();
        enableButtons(false, Configs.IS_USE_LOCAL_VAD);

        cleanup();
    }

    private void setButtonHandlers()
    {
        (findViewById(R.id.btnStart)).setOnClickListener(this);

    }

    private void enableButton(int id, boolean isEnable)
    {
        (findViewById(id)).setEnabled(isEnable);
    }

    private void enableButtons(boolean isRecording, boolean isLocalVad)
    {
        if(isRecording)
        {
            if (isLocalVad)
            {
                enableButton(R.id.btnStart, false);

                int pressedBlue = Color.parseColor("#004966");
                findViewById(R.id.btnStart).setBackgroundColor(pressedBlue);
            }
            else
            {
                Button tb = findViewById(R.id.btnStart);
                tb.setText("Stop Recording");
            }
        }
        else
        {
            Button tb = findViewById(R.id.btnStart);
            tb.setText("Start Recording");
            if (isLocalVad)
            {
                enableButton(R.id.btnStart, true);

                int releasedBlue = Color.parseColor("#00adef");
                findViewById(R.id.btnStart).setBackgroundColor(releasedBlue);

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //This comes from our threaded audio recorder example.
    @Override
    public void onRecorderFrames(short[] short_data)
    {
        _koManager.processData(short_data);
    }
    private void cleanup()
    {
        if(_audioRecorder != null)
        {
            _audioRecorder.stopRecording();
            _audioRecorder.RemoveListener(this);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnStart: {

                //The if condition is only hit if LOCAL VAD IS OFF.
                if(KidsenseAudioRecorder.getInstance().isRecording)
                {
                    if(_koManager != null)
                    {
                        _koManager.decodeSession();
                        //_koManager.cleanup();
                    }

                    if (_audioRecorder != null)
                    {
                        _audioRecorder.RemoveListener(this);
                        _audioRecorder.stopRecording();
                    }

                    enableButtons(false,true);

                    IS_SPEAKING = false;
                    ImageView iv = findViewById(R.id.microphoneImage);
                    iv.setVisibility(View.INVISIBLE);
                }
                else
                {
                    _koManager.addListener(this);
                    _koManager.initializeSession(Configs.IS_USE_LOCAL_VAD);

                    if (Configs.IS_USE_LOCAL_VAD)
                        enableButtons(true, true);
                    else
                    {
                        enableButtons(true, false);
                        IS_SPEAKING = true;
                        ImageView iv = findViewById(R.id.microphoneImage);
                        iv.setVisibility(View.VISIBLE);
                    }

                    if (_audioRecorder != null)
                    {
                        _audioRecorder.AddListener(this);
                        _audioRecorder.startRecording();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        switch (buttonView.getId())
        {
            case R.id.switchVAD:
                stopServices();
                Configs.IS_USE_LOCAL_VAD = isChecked;
                enableButtons(false, isChecked);
                break;
        }
    }

    private void stopServices()
    {
        if(_audioRecorder != null && _audioRecorder.isRecording)
            _audioRecorder.stopRecording();
    }

    @Override
    public void onDestroy()
    {
        if(_koManager != null)
        {
            _koManager.removeListener(this);
            _koManager.cleanup();
            _koManager = null;
        }

        if(_audioRecorder != null)
            _audioRecorder.cleanup();

        super.onDestroy();
    }

    @Override
    public void onOfflineAsrPartialResult(final String result)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _tvBox.setText(result);
                _tvBox.setTextColor(Color.GRAY);
                _tvBox.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
        });
    }

    @Override
    public void onOfflineAsrSentence(final String result)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //tvBox.setText(result);
                _tvBox.setText(filter.filterText(result.replace(".", "")));
                _tvBox.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                _tvBox.setTextColor(Color.BLACK);
            }
        });
    }

    @Override
    public void onOfflineAsrFullResult(final String result)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _tvBox2.setText(result);
                _koManager.removeListener(MainActivity.this);
            }
        });
    }

    @Override
    public void onOfflineModelLoadingStart()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                _dialog = ProgressDialog.show(MainActivity.this, "",
                        "Loading. Please wait...", true);
            }
        });
    }

    @Override
    public void onOfflineModelLoadingComplete()
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if(_dialog != null)
                    _dialog.cancel();
            }
        });
    }

    //ONLY FIRES IF USING LOCAL PROVIDED VAD
    @Override
    public void onOfflineAsrSpeechStarted()
    {
        IS_SPEAKING = true;
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                ImageView iv = findViewById(R.id.microphoneImage);
                iv.setVisibility(View.VISIBLE);
            }
        });
    }

    //ONLY FIRES IF USING LOCAL PROVIDED VAD
    @Override
    public void onOfflineAsrSpeechEnded()
    {
        IS_SPEAKING = false;

        if(_audioRecorder != null && _audioRecorder.isRecording)
            _audioRecorder.stopRecording();

        _koManager.decodeSession();

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                ImageView iv = findViewById(R.id.microphoneImage);
                iv.setVisibility(View.INVISIBLE);
                enableButtons(false,Configs.IS_USE_LOCAL_VAD);
            }
        });
    }

    public void goToSettings(View view) {
        Intent openSettings = new Intent(this, settings.class);

        openSettings.putExtra("userID", this.userID);

        startActivity(openSettings);
        finish();
    }
}
