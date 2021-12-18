package com.example.hompimpa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ResultHompimpaActivity extends AppCompatActivity {

    private Socket mSocket;
    private TextView resultHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_hompimpa);

        SocketApp app = (SocketApp) getApplication();
        mSocket = app.getSocket();
        mSocket.emit("movetoresulthom");
        mSocket.on("gameresult", displayResult);

        resultHeader = (TextView) findViewById(R.id.resultHeader);
    }

    private Emitter.Listener displayResult = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ResultHompimpaActivity.this.runOnUiThread(new Runnable() {
                @SuppressLint("ResourceType")
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    boolean first = true;
                    int j = 0;
                    RelativeLayout resultsContainer = (RelativeLayout) findViewById(R.id.resultHompimpaContainer);
                    String choice;

                    try{
                        JSONArray resultsArray = data.getJSONArray("results");

                        for (int i = 0; i < resultsArray.length(); i++) {
                            JSONObject indexed = resultsArray.getJSONObject(i);
                            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                            RelativeLayout.LayoutParams p0 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            p0.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
                            linearLayout.setId(i + 1);
                            if(first){
                                p0.addRule(RelativeLayout.BELOW, resultHeader.getId());
                                first = false;
                            }else {
                                p0.addRule(RelativeLayout.BELOW, i);
                            }
                            j = i + 1;
                            p0.topMargin = 10;
                            p0.bottomMargin = 10;
                            linearLayout.setLayoutParams(p0);
                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                            TextView tv = new TextView(getApplicationContext());
                            LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            tv.setText(indexed.getString("username"));
                            p1.rightMargin = 10;
                            tv.setLayoutParams(p1);
                            linearLayout.addView(tv);


                            ImageView iv = new ImageView(getApplicationContext());
                            LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            iv.setId(i + 1);

                            choice = indexed.getString("choice");
                            if(choice.equals("black")){
                                Log.e("black", "asd");
                                iv.setImageResource(R.drawable.ic_black_hand);
                            }else if(choice.equals("white")){
                                Log.e("white", "dsa");
                                iv.setImageResource(R.drawable.ic_white_hand);
                            }
                            iv.setLayoutParams(p2);
                            linearLayout.addView(iv);
                            resultsContainer.addView(linearLayout);

                        }

                        Button bt = (Button)getLayoutInflater().inflate(R.layout.button_sample, null);
                        RelativeLayout.LayoutParams p3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        p3.addRule(RelativeLayout.BELOW, j);
                        p3.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
                        p3.topMargin = 20;
                        bt.setId(16);
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ResultHompimpaActivity.super.onBackPressed();
                            }
                        });
                        bt.setText("Return");
                        bt.setLayoutParams(p3);
                        resultsContainer.addView(bt);
                    }catch (JSONException e){
                        return;
                    }
                }
            });
        }
    };
}