package com.example.testc2.videochat1;

import android.media.projection.MediaProjection;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SocketLive {
    private static final String TAG = "David";
    private WebSocket webSocket;
    int port;
    CodecLiveH265 codecLiveH265;
    public SocketLive(int port) {
        this.port = port;
        webSocketServer = new WebSocketServer(new InetSocketAddress(this.port)) {
            @Override
            public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
                SocketLive.this.webSocket = webSocket;
                Log.i(TAG, "onOpen ");
            }

            @Override
            public void onClose(WebSocket webSocket, int i, String s, boolean b) {
                Log.i(TAG, "onClose: 关闭 socket ");
            }

            @Override
            public void onMessage(WebSocket webSocket, String s) {
            }

            @Override
            public void onError(WebSocket webSocket, Exception e) {
                Log.i(TAG, "onError:  " + e.toString());
            }

            @Override
            public void onStart() {
                Log.i(TAG, "onStart ");
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void start(MediaProjection mediaProjection) {
        webSocketServer.start();
        codecLiveH265 = new CodecLiveH265(this,mediaProjection);
        codecLiveH265.startLive();
    }
    public void close() {
        try {
            webSocket.close();
            webSocketServer.stop();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private WebSocketServer webSocketServer ;

    public void sendData(byte[] bytes) {
        Log.i(TAG, "sendData    null:"+(webSocket==null)+"  isOpen:"+ (webSocket==null?"null":""+webSocket.isOpen()));
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.send(bytes);
        }
    }
}
