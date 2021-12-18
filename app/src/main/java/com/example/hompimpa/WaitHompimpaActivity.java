package com.example.hompimpa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class WaitHompimpaActivity extends AppCompatActivity {

    private Socket mSocket;
    private ProgressBar progressBar;
    private TextView totalket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_hompimpa);

        SocketApp app = (SocketApp) getApplication();
        mSocket = app.getSocket();
        mSocket.emit("movetowaitinghom");
        mSocket.on("finishchoosehom", moveToResult);
        mSocket.on("totalchoose", moveProgress);

        progressBar = (ProgressBar) findViewById(R.id.progressBarHompimpa);
        totalket = (TextView) findViewById(R.id.waitingHompimpa);

    }

    private Emitter.Listener moveToResult = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            WaitHompimpaActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WaitHompimpaActivity.this, ResultHompimpaActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }
    };

    private Emitter.Listener moveProgress = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            WaitHompimpaActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    int now = 0;
                    int total = 0;
                    try{
                        now = data.getInt("now");
                        total = data.getInt("total");
                    }catch (JSONException e){
                        return;
                    }

                    float progress = (float) now / (float) total * (float) 100;
                    progressBar.setProgress((int) progress);
                    totalket.setText("Waiting " + now + "/" + total);
                }
            });
        }
    };

}