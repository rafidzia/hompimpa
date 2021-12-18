package com.example.hompimpa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ResultSuitActivity extends AppCompatActivity {

    private Socket mSocket;
    private ArrayList<String> resultsUsername = new ArrayList<>();
    private ArrayList<String> resultsChoice = new ArrayList<>();

    private String enemyUsername;
    private String yourResult = "";
    private String enemyResult = "";

    private ImageView yourChoiceImageView;
    private ImageView enemyChoiceImageView;

    private TextView yourTextUsername;
    private TextView enemyTextUsername;

    private TextView yourTextScore;
    private TextView enemyTextScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_suit);

        SocketApp app = (SocketApp) getApplication();
        mSocket = app.getSocket();
        mSocket.emit("movetoresultsuit");
        mSocket.on("gameresult", displayResult);
        mSocket.on("suitscore", displayScore);

        yourChoiceImageView = (ImageView) findViewById(R.id.yourChoice);
        enemyChoiceImageView = (ImageView) findViewById(R.id.enemyChoice);

        yourTextUsername = (TextView) findViewById(R.id.yourUsername);
        enemyTextUsername = (TextView) findViewById(R.id.enemyUsername);

        yourTextScore = (TextView) findViewById(R.id.yourScore);
        enemyTextScore = (TextView) findViewById(R.id.enemyScore);

    }

    private Emitter.Listener displayResult = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ResultSuitActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    try{
                        JSONArray resultsArray = data.getJSONArray("results");

                        for (int i = 0; i < resultsArray.length(); i++) {
                            JSONObject indexed = resultsArray.getJSONObject(i);
                                if(indexed.getString("username").equals(Constants.username)){
                                    yourResult = indexed.getString("choice");
                                    Log.e("asd", "asd");
                                }else{
                                    Log.e("dsa", "dsa");
                                    enemyUsername = indexed.getString("username");
                                    enemyResult = indexed.getString("choice");
                                }
                        }
                    }catch (JSONException e){
                        return;
                    }

                    Log.e("you", yourResult);
                    Log.e("you", enemyResult);
                    if(yourResult.equals("rock")){
                        yourChoiceImageView.setImageResource(R.drawable.ic_rock);
                    }else if(yourResult.equals("paper")){
                        yourChoiceImageView.setImageResource(R.drawable.ic_paper);
                    }else if(yourResult.equals("scissor")){
                        yourChoiceImageView.setImageResource(R.drawable.ic_scissor);
                    }

                    if(enemyResult.equals("rock")){
                        enemyChoiceImageView.setImageResource(R.drawable.ic_rock);
                    }else if(enemyResult.equals("paper")){
                        enemyChoiceImageView.setImageResource(R.drawable.ic_paper);
                    }else if(enemyResult.equals("scissor")){
                        enemyChoiceImageView.setImageResource(R.drawable.ic_scissor);
                    }

                    yourTextUsername.setText(Constants.username);
                    enemyTextUsername.setText(enemyUsername);

                }
            });
        }
    };
    private Emitter.Listener displayScore = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            ResultSuitActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try{
                        yourTextScore.setText(data.getString(Constants.username));
                        enemyTextScore.setText(data.getString(enemyUsername));
                    }catch (JSONException e){
                        return;
                    }
                }
            });
        }
    };


    public void btnReturn(View view) {
        ResultSuitActivity.super.onBackPressed();
    }
}