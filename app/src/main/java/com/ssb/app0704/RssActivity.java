package com.ssb.app0704;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RssActivity extends AppCompatActivity {

    class Hani{
        public String title;
        public String list;
        //객체를 문자열로 표현하는 메소드
        //출력하는 곳에 객체 이름을 설정하면 이 메소드의 호출결과를 출력합니다.
        @Override
        public String toString(){
            return title;
        }
    }

    ListView listView;
    ArrayAdapter<Hani> adapter;
    ArrayList<Hani> list;

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            adapter.notifyDataSetChanged();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);

        listView = (ListView)findViewById(R.id.titlelist);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(RssActivity.this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);


    }

    //화면이 출력될 때마다 호출되는 메소드
    @Override
    public void onResume(){
        super.onResume();

        Thread th = new Thread(){
            String xml = "";
            public void run() {
                try {
                    String addr = "http://www.hani.co.kr/rss/economy/";
                    URL url = new URL(addr);
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while (true){
                        String line = br.readLine();
                        if(line==null){
                            break;
                        }
                        sb.append(line);
                    }
                    xml=sb.toString();
                    Log.e("읽어오기",xml);
                } catch (Exception e) {
                    Log.e("다운로드예외", e.getMessage());
                }


            }
        };
        th.start();
    }


}
