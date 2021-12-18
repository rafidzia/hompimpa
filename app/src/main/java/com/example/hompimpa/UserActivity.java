package com.example.hompimpa;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class UserActivity extends AppCompatActivity {

    private Socket mSocket;

    private TextView roomText;
    private ListView listUser;
    private EditText usernameEdit;
    private RelativeLayout userContainer;
    private boolean startable = false;

    private ArrayList<String> users = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        SocketApp app = (SocketApp) getApplication();
        mSocket = app.getSocket();

        roomText = (TextView) findViewById(R.id.textView4);
        roomText.setText(Constants.room);
        listUser = (ListView) findViewById(R.id.listUser);
        usernameEdit = (EditText) findViewById(R.id.username);
        userContainer = (RelativeLayout) findViewById(R.id.userContainer);

        mSocket.emit("userready");

        mSocket.on("bcuser", listingUser);
        mSocket.on("startinit", startinit);
        mSocket.on("userexist", userfail);

        adapter = new ArrayAdapter<String>(UserActivity.this, R.layout.list_item, R.id.username, users);
        listUser.setAdapter(adapter);
    }

    @SuppressLint("ResourceType")
    private void removeStartBtn(){
        if(startable){
            ((ViewGroup) findViewById(16).getParent()).removeView(findViewById(16));
        }
        startable = false;
    }

    private Emitter.Listener listingUser = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            UserActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try{
                        JSONArray userArray = data.getJSONArray("users");
                        users.clear();
                        for (int i = 0; i < userArray.length(); i++) {
                            JSONObject usernames = userArray.getJSONObject(i);
                            users.add(usernames.getString("username"));
                        }
                    } catch (JSONException e) {
                        return;
                    }
                    adapter.notifyDataSetChanged();
                    removeStartBtn();
                }
            });
        }
    };


    public void btnCreateUser(View view) {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        mSocket.emit("createuser", usernameEdit.getText().toString().trim());
    }

    private Emitter.Listener startinit = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            UserActivity.this.runOnUiThread(new Runnable() {
                @SuppressLint("ResourceType")
                @Override
                public void run() {
                    Constants.username = usernameEdit.getText().toString().trim();
                    if(!startable){
                        Button bt = (Button)getLayoutInflater().inflate(R.layout.button_sample, null);
                        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        p.addRule(RelativeLayout.BELOW, listUser.getId());
                        p.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
                        bt.setId(16);
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                enterGame();
                            }
                        });
                        bt.setText("Start");
                        bt.setLayoutParams(p);
                        userContainer.addView(bt);
                    }
                    startable = true;
                }
            });
        }
    };

    private Emitter.Listener userfail = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            UserActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(UserActivity.this, "Username already exist", Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Leave Room?")
                .setNegativeButton("No", null)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSocket.emit("leaveroom");
                        UserActivity.super.onBackPressed();
                    }
                }).create().show();
    }

    private void enterGame(){
        if(Constants.mode == "suit"){
            Intent intent = new Intent(UserActivity.this, SuitActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else if(Constants.mode == "hompimpa"){
            Intent intent = new Intent(UserActivity.this, HompimpaActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}