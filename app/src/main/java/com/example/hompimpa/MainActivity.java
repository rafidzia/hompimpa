package com.example.hompimpa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_CODE_0 = 99;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SocketApp app = (SocketApp) getApplication();
        mSocket = app.getSocket();

        mSocket.connect();
    }

    public void btnSuitClick(View view) {
        Constants.mode = "suit";
        nextActivity();
    }

    public void btnHomClick(View view) {
        Constants.mode = "hompimpa";
        nextActivity();
    }

    private void nextActivity(){
        Intent intent = new Intent(MainActivity.this, RoomActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}