package com.example.myapplication2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Intent intent;  // 음성인식 인텐트를 정의하는 변수
    SpeechRecognizer mRecognizer;  // 음성인식 객체를 생성하는 변수
    Button sttBtn;  // 음성인식 시작 버튼
    TextView textView;  // 음성인식 결과를 표시하는 텍스트뷰
    final int PERMISSION = 1;  // 권한 요청 코드

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            // 권한 체크 및 요청
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        textView = (TextView) findViewById(R.id.sttResult);  // 결과를 표시할 텍스트뷰
        sttBtn = (Button) findViewById(R.id.sttStart);  // 음성인식 시작 버튼

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");  // 한국어 인식 설정

        sttBtn.setOnClickListener(v -> {
            mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);  // 음성인식 객체 생성
            mRecognizer.setRecognitionListener(listener);  // 인식 리스너 설정
            mRecognizer.startListening(intent);  // 음성인식 시작
        });

    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(), "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {
            // 음성 입력이 시작되었을 때 호출됨
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            // 입력 음성의 RMS 값을 전달받음
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            // 음성 데이터를 전달받음
        }

        @Override
        public void onEndOfSpeech() {
            // 음성 입력이 종료되었을 때 호출됨
        }

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "인식기가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버 이상";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간 초과";
                    break;
                default:
                    message = "알 수 없는 오류";
                    break;
            }

            Toast.makeText(getApplicationContext(), "에러 발생: " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // 음성 인식 결과를 처리하는 부분
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            if (matches != null && !matches.isEmpty()) {
                textView.setText(matches.get(0));  // 첫 번째 인식 결과 표시
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            // 부분적인 음성 인식 결과를 처리하는 부분
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            // 이벤트 발생 시 호출됨
        }
    };
}
