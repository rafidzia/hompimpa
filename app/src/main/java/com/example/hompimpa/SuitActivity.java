package com.example.hompimpa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import io.socket.client.Socket;

public class SuitActivity extends AppCompatActivity {

    private Socket mSocket;
    private String choice = "";
    private boolean chosen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suit);

        SocketApp app = (SocketApp) getApplication();
        mSocket = app.getSocket();

    }

    public void btnStone(View view) {
        choice = "rock";
        chosen = true;
    }

    public void btnPaper(View view) {
        choice = "paper";
        chosen = true;
    }

    public void btnScissor(View view) {
        choice = "scissor";
        chosen = true;
    }

    public void submitChoice(View view) {
        if(!chosen){
            Toast.makeText(SuitActivity.this, "Choose one first", Toast.LENGTH_LONG).show();
        }else{
            mSocket.emit("choosesuit", choice);
            Intent intent = new Intent(SuitActivity.this, WaitSuitActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }
}