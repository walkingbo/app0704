package com.ssb.app0704;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    LinearLayout disp;
    EditText id;
    EditText pw;
    Button login;

    //disp 의 색상을 변경하는 핸들러 생성
    //일반 스레드에서는 뷰의 화면 변경을 하면 안됩니다.
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("true")) {
                disp.setBackgroundColor(Color.GREEN);
            } else {
                disp.setBackgroundColor(Color.RED);
            }
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(pw.getWindowToken(),0);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        disp = (LinearLayout) findViewById(R.id.background);

        id = (EditText) findViewById(R.id.idtext);
        pw = (EditText) findViewById(R.id.pwtext);

        login = (Button) findViewById(R.id.loginbtn);

        login.setOnClickListener((view) -> {
            Thread th = new Thread() {
                String json="";
                public void run() {
                    try {
                        String addr = "http://192.168.0.118:8090/login?id="+id.getText().toString().trim().toUpperCase()+
                                "&pw="+pw.getText().toString().trim();
                        URL url = new URL(addr);
                        HttpURLConnection con = (HttpURLConnection)url.openConnection();
                        StringBuilder sb = new StringBuilder();
                        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        while(true){
                        String line=br.readLine();
                        if(line==null){
                            break;
                        }
                        sb.append(line);
                        }
                        //Log.e("데이터",sb.toString());
                        json=sb.toString();
                        br.close();
                        con.disconnect();
                    } catch (Exception e) {
                        Log.e("다운로드예외",e.getMessage());
                    }

                    if(json!=null){
                        try {
                            JSONObject result = new JSONObject(json);
                            String msg = result.getString("result");
                            Message message = new Message();
                            message.obj = msg;
                            handler.sendMessage(message);
                        }catch (Exception e){
                            Log.e("파싱예외",e.getMessage());
                        }
                    }

                }
            };
            th.start();
        });

    }
}
