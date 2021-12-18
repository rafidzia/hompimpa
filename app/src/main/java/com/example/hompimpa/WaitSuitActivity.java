package com.example.hompimpa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class WaitSuitActivity extends AppCompatActivity {

    private Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_suit);

        SocketApp app = (SocketApp) getApplication();
        mSocket = app.getSocket();

        mSocket.on("finishchoosesuit", moveToResult);
    }

    private Emitter.Listener moveToResult = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            WaitSuitActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WaitSuitActivity.this, ResultSuitActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }
    };
}