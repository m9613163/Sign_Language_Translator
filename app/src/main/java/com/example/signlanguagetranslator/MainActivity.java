package com.example.signlanguagetranslator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Context cThis;//context 설정
    String LogTT="[STT]";//LOG타이틀
    //음성 인식용
    Intent SttIntent;
    SpeechRecognizer mRecognizer;

    // 화면 처리용
    Button btnSttStart;
    EditText txtInMsg;
    EditText txtSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cThis=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //음성인식
        SttIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        SttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getApplicationContext().getPackageName());
        SttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");//한국어 사용
        mRecognizer=SpeechRecognizer.createSpeechRecognizer(cThis);
        mRecognizer.setRecognitionListener(listener);

        //버튼설정
        btnSttStart=(Button)findViewById(R.id.btn_stt_start);
        btnSttStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("음성인식 시작!");
                if(ContextCompat.checkSelfPermission(cThis, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.RECORD_AUDIO},1);
                    //권한을 허용하지 않는 경우
                }else{
                    //권한을 허용한 경우
                    try {
                        mRecognizer.startListening(SttIntent);
                    }catch (SecurityException e){e.printStackTrace();}
                }
            }
        });
        txtInMsg=(EditText)findViewById(R.id.txtInMsg);
        txtSystem=(EditText)findViewById(R.id.txtSystem);

    }
    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            txtSystem.setText("음성인식 준비중 입니다"+"\r\n");
        }

        @Override
        public void onBeginningOfSpeech() {
            txtSystem.setText("지금부터 말을 해주세요"+"\r\n");
        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            txtSystem.setText("onBufferReceived..........."+"\r\n");
        }

        @Override
        public void onEndOfSpeech() {
            txtSystem.setText("음성인식 종료"+"\r\n");
        }

        @Override
        public void onError(int i) {
            txtSystem.setText("천천히 다시 말씀해 주세요"+"\r\n");
        }

        @Override
        public void onResults(Bundle results) {
            String key= "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            txtInMsg.setText(rs[0]+"\r\n");
            FuncVoiceOrderCheck(rs[0]);
            mRecognizer.startListening(SttIntent);

        }

        @Override
        public void onPartialResults(Bundle bundle) {
            txtSystem.setText("onPartialResults..........."+"\r\n"+txtSystem.getText());
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
            txtSystem.setText("onEvent..........."+"\r\n"+txtSystem.getText());
        }
    };
    //입력된 음성 메세지 확인 후 동작 처리
    private void FuncVoiceOrderCheck(String VoiceMsg){
        if(VoiceMsg.length()<1)return;

        VoiceMsg=VoiceMsg.replace(" ","");//공백제거

    }
}