package com.dsj.tool.receiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.dsj.tool.receiver.tool.NetworkUtils;
import com.dsj.tool.receiver.tool.p2p.p2pcore.P2PManager;
import com.dsj.tool.receiver.tool.p2p.p2pentity.P2PFileInfo;
import com.dsj.tool.receiver.tool.p2p.p2pentity.P2PNeighbor;
import com.dsj.tool.receiver.tool.p2p.p2pinterface.Melon_Callback;
import com.dsj.tool.receiver.tool.p2p.p2pinterface.SendFile_Callback;
import com.dsj.tool.receiver.tool.sdk.AccessPointManager;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private P2PManager p2PManager;
    private String alias;//当前设备别名
    private List<P2PNeighbor> neighbors = new ArrayList<>();//扫描到的设备

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alias = Build.DEVICE;
        findViewById(R.id.main_load).setOnClickListener(v -> scan());
        findViewById(R.id.main_send).setOnClickListener(v -> send());
    }

    private void scan(){
        p2PManager = new P2PManager(getApplicationContext());
        P2PNeighbor melonInfo = new P2PNeighbor();
        melonInfo.alias = alias;
        String ip = null;
        try {
            ip = AccessPointManager.getLocalIpAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(ip))
            ip = NetworkUtils.getLocalIp(getApplicationContext());
        melonInfo.ip = ip;

        p2PManager.start(melonInfo, new Melon_Callback() {
            @Override
            public void Melon_Found(P2PNeighbor melon) {
                if (melon != null) {
                    if (!neighbors.contains(melon))
                        neighbors.add(melon);
                }
            }

            @Override
            public void Melon_Removed(P2PNeighbor melon) {
                if (melon != null) {
                    neighbors.remove(melon);
                }
            }
        });
    }

    private void send(){
        AppCompatEditText editText = findViewById(R.id.main_edit);
        final String msgStr = editText.getText().toString();
        if (TextUtils.isEmpty(msgStr)){
            Toast.makeText(this, "信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (neighbors.isEmpty()){
            Toast.makeText(this, "未扫描到设备", Toast.LENGTH_SHORT).show();
            return;
        }
        p2PManager.sendStrMsg(neighbors.get(0), msgStr, new SendFile_Callback() {
            @Override
            public void BeforeSending() {

            }

            @Override
            public void OnSending(P2PFileInfo file, P2PNeighbor dest) {

            }

            @Override
            public void AfterSending(P2PNeighbor dest) {

            }

            @Override
            public void AfterAllSending() {

            }

            @Override
            public void AbortSending(int error, P2PNeighbor dest) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (p2PManager != null) {
            if (!neighbors.isEmpty()){
                for (P2PNeighbor neighbor : neighbors){
                    p2PManager.cancelSend(neighbor);
                }
            }
            p2PManager.stop();
        }
    }
}