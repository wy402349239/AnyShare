package com.dsj.tool.receiver.tool.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import com.dsj.tool.receiver.tool.NetworkUtils;
import com.dsj.tool.receiver.tool.p2p.p2pcore.P2PManager;
import com.dsj.tool.receiver.tool.p2p.p2pentity.P2PNeighbor;
import com.dsj.tool.receiver.tool.p2p.p2pinterface.Melon_Callback;
import com.dsj.tool.receiver.tool.sdk.AccessPointManager;

import java.net.UnknownHostException;

/**
 * created by wangyu on 2020/7/30 5:08 PM
 * description:
 */
public class KeyBoardSendUtil {

    private static KeyBoardSendUtil mUtil = null;

    public static KeyBoardSendUtil getInstance() {
        if (mUtil == null) {
            synchronized (KeyBoardSendUtil.class) {
                if (mUtil == null) {
                    mUtil = new KeyBoardSendUtil();
                }
            }
        }
        return mUtil;
    }

    private KeyBoardSendUtil() {
    }

    private P2PManager p2PManager;

    public void scanDevices(final KeyboardScanCallback callback, Context context, String deviceName) {
        if (context == null) {
            if (callback != null) {
                callback.scanError();
            }
            return;
        }
        String alias = TextUtils.isEmpty(deviceName) ? Build.DEVICE : deviceName;
        p2PManager = new P2PManager(context);
        P2PNeighbor melonInfo = new P2PNeighbor();
        melonInfo.alias = alias;
        String ip = null;
        try {
            ip = AccessPointManager.getLocalIpAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(ip)) {
            ip = NetworkUtils.getLocalIp(context);
        }
        melonInfo.ip = ip;

        p2PManager.start(melonInfo, new Melon_Callback() {
            @Override
            public void Melon_Found(P2PNeighbor melon) {
                if (melon != null && callback != null) {
                    callback.scanDevice(melon);
                }
            }

            @Override
            public void Melon_Removed(P2PNeighbor melon) {
                if (melon != null && callback != null) {
                    callback.removeDevice(melon);
                }
            }
        });
    }

    public void sendMsg(Context context, String msg, P2PNeighbor neighbor) {
        if (TextUtils.isEmpty(msg)) {
            if (context != null) {
                Toast.makeText(context, "发送信息为空", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (p2PManager == null) {
            if (context != null) {
                Toast.makeText(context, "请先扫描设备", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (neighbor == null) {
            if (context != null) {
                Toast.makeText(context, "请选择目标设备", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        p2PManager.sendStrMsg(neighbor, msg, null);
    }

}