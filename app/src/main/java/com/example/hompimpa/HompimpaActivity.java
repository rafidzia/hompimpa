package com.example.hompimpa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import io.socket.client.Socket;

public class HompimpaActivity extends AppCompatActivity {

    private Socket mSocket;
    private String choice = "";
    private boolean chosen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hompimpa);

        SocketApp app = (SocketApp) getApplication();
        mSocket = app.getSocket();
    }

    public void btnWhiteHand(View view) {
        choice = "white";
        chosen = true;
    }

    public void btnBlackHand(View view) {
        choice = "black";
        chosen = true;
    }

    public void submitChoice(View view) {
        if(!chosen){
            Toast.makeText(HompimpaActivity.this, "Choose one first", Toast.LENGTH_LONG).show();
        }else {
            mSocket.emit("choosehompimpa", choice);
            Intent intent = new Intent(HompimpaActivity.this, WaitHompimpaActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}