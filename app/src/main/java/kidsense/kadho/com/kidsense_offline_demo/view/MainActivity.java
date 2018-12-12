package kidsense.kadho.com.kidsense_offline_demo.view;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.kadho.android.media.KidsenseAudioRecorder;
import com.kadho.android.sdk.asr.offline.KidsenseOfflineManager;

import com.kadho.kidsense.kidsense_en_medium_v1.Kidsense_en_medium_v1;
import com.kadho.kidsense.kidsense_en_small_v1.Kidsense_en_small_v1;

import kidsense.kadho.com.kidsense_offline_demo.Configs;
import kidsense.kadho.com.kidsense_offline_demo.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_offline);

        Instance = this;
	
        _koManager = KidsenseOfflineManager.getInstance(MainActivity.this);

        // HERE YOU INITIALIZE A MODEL
        String configPath = Kidsense_en_medium_v1.autoSync(MainActivity.this);
//      String configPath = Kidsense_en_small_v1.autoSync(MainActivity.this);
//	String configPath = Kidsense_en_large_v1.autoSync(MainActivity.this);
        _koManager.initModel(configPath,"your-api-key-here");

        setButtonHandlers();
        enableButtons(false, Configs.IS_USE_LOCAL_VAD);

        _audioRecorder = KidsenseAudioRecorder.getInstance();

        VadSwitch = (Switch)findViewById(R.id.switchVAD);
        VadSwitch.setOnCheckedChangeListener(this);

        _tvBox = (TextView)findViewById(R.id.textView);
        _tvBox.setMovementMethod(new ScrollingMovementMethod());
        _tvBox2 = (TextView)findViewById(R.id.textView2);
        _tvBox2.setMovementMethod(new ScrollingMovementMethod());
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
                _tvBox.setText(result);
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
}
