package com.example.hompimpa;


import android.app.Application;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketApp extends Application {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.SOCKET_SERVER_URL);
        } catch(URISyntaxException e){
            throw new RuntimeException(e);
        }
    }
    public Socket getSocket() {
        return mSocket;
    }
}

