package com.example.hompimpa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class RoomActivity extends AppCompatActivity {

    private Socket mSocket;
    private Button btnJoin;
    private boolean isthisjoin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        SocketApp app = (SocketApp) getApplication();
        mSocket = app.getSocket();

        btnJoin = (Button) findViewById(R.id.btnJoin);

        mSocket.on("joined", onJoin);
        mSocket.on("failjoin", failJoin);

    }

    public void btnCreateRoom(View view) {
        try {
            JSONObject dataJoin = new JSONObject();
            dataJoin.put("mode", Constants.mode);
            mSocket.emit("createroom", dataJoin);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @SuppressLint("ResourceType")
    public void btnJoinRoom(View view) {
        if(!isthisjoin){
            RelativeLayout roomContainer = (RelativeLayout) findViewById(R.id.roomContainer);
            EditText et = new EditText(this);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.addRule(RelativeLayout.BELOW, btnJoin.getId());
            p.leftMargin = 100;
            p.rightMargin = 100;
            p.topMargin = 100;
            et.setInputType(InputType.TYPE_CLASS_TEXT);
            et.setSingleLine();
            et.setGravity(Gravity.CENTER_HORIZONTAL);
            et.setHint("Room ID");
            et.setId(14);
            et.setLayoutParams(p);
            roomContainer.addView(et);

            Button bt = (Button)getLayoutInflater().inflate(R.layout.button_sample, null);
            RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p1.addRule(RelativeLayout.BELOW, 14);
            p1.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
            bt.setId(15);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinRoom();
                }
            });
            bt.setText("Submit");
            bt.setLayoutParams(p1);
            roomContainer.addView(bt);
        }
        isthisjoin = true;

    }

    public void joinRoom() {
        @SuppressLint("ResourceType") EditText asd = (EditText) findViewById(14);
        final String roomID = asd.getText().toString().trim();
        try {
            JSONObject dataJoin = new JSONObject();
            dataJoin.put("room", roomID);
            dataJoin.put("mode", Constants.mode);
            mSocket.emit("joinroom", dataJoin);
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    @SuppressLint("ResourceType")
    private void removeForm(){
        if(isthisjoin){
            ((ViewGroup) findViewById(14).getParent()).removeView(findViewById(14));
            ((ViewGroup) findViewById(15).getParent()).removeView(findViewById(15));
        }
        isthisjoin = false;
    }

    private Emitter.Listener onJoin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            RoomActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    removeForm();
                    JSONObject data = (JSONObject) args[0];
                    String roomID;
                    try{
                        roomID = data.getString("room");

                    } catch (JSONException e){
                        return;
                    }
                    Intent intent = new Intent(RoomActivity.this, UserActivity.class);
                    Constants.room = roomID;
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
        }
    };

    private Emitter.Listener failJoin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            RoomActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RoomActivity.this, "Failed to join", Toast.LENGTH_LONG).show();
                }
            });
        }
    };
}