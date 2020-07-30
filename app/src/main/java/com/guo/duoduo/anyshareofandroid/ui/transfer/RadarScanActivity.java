package com.guo.duoduo.anyshareofandroid.ui.transfer;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.guo.duoduo.anyshareofandroid.R;
import com.guo.duoduo.anyshareofandroid.sdk.accesspoint.AccessPointManager;
import com.guo.duoduo.anyshareofandroid.sdk.cache.Cache;
import com.guo.duoduo.anyshareofandroid.ui.common.BaseActivity;
import com.guo.duoduo.anyshareofandroid.utils.NetworkUtils;
import com.guo.duoduo.anyshareofandroid.utils.ToastUtils;
import com.guo.duoduo.p2pmanager.p2pconstant.P2PConstant;
import com.guo.duoduo.p2pmanager.p2pcore.P2PManager;
import com.guo.duoduo.p2pmanager.p2pentity.P2PFileInfo;
import com.guo.duoduo.p2pmanager.p2pentity.P2PNeighbor;
import com.guo.duoduo.p2pmanager.p2pinterface.Melon_Callback;
import com.guo.duoduo.p2pmanager.p2pinterface.SendFile_Callback;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class RadarScanActivity extends BaseActivity {
    private static final String tag = RadarScanActivity.class.getSimpleName();

    private P2PManager p2PManager;
    private String alias;//当前设备别名
    private RelativeLayout scanRelative;//圈圈
    private List<P2PNeighbor> neighbors = new ArrayList<>();//扫描到的设备
    private P2PNeighbor curNeighbor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radarscan);
        alias = Build.DEVICE;
        TextView radar_scan_name = findViewById(R.id.activity_radar_scan_name);
        radar_scan_name.setText(String.format("本机：%s", alias));

        FloatingActionButton fab = findViewById(R.id.activity_radar_scan_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,
                        getResources().getString(R.string.file_transfering_exit),
                        Snackbar.LENGTH_LONG)
                        .setAction(getResources().getString(R.string.ok),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        finish();
                                    }
                                }).show();
            }
        });

        scanRelative = findViewById(R.id.activity_radar_scan_relative);

//        String alias = ((RippleView) (view)).getText().toString();
//        for (int i = 0; i < neighbors.size(); i++) {
//            if (neighbors.get(i).alias.equals(alias)) {
//                curNeighbor = neighbors.get(i);
//                sendStrMsg(curNeighbor, "str msg from : " + alias);
//                break;
//            }
//        }
        initP2P();
    }

    private void sendStrMsg(P2PNeighbor neighbor, String msg) {
        Toast.makeText(this, "send { " + msg + " }", Toast.LENGTH_SHORT).show();
        p2PManager.sendStrMsg(neighbor, msg, new SendFile_Callback() {
            @Override
            public void BeforeSending() {

            }

            @Override
            public void OnSending(P2PFileInfo file, P2PNeighbor dest) {
                int index = -1;
                if (Cache.selectedList.contains(file)) {
                    index = Cache.selectedList.indexOf(file);
                }
                if (index != -1) {
                    P2PFileInfo fileInfo = Cache.selectedList.get(index);
                    fileInfo.percent = file.percent;
                } else {
                    Log.d(tag, "onSending index error");
                }
            }

            @Override
            public void AfterSending(P2PNeighbor dest) {
                ToastUtils.showTextToast(getApplicationContext(),
                        getString(R.string.file_send_complete));
            }

            @Override
            public void AfterAllSending() {
                ToastUtils.showTextToast(getApplicationContext(),
                        getString(R.string.file_send_complete));
            }

            @Override
            public void AbortSending(int error, P2PNeighbor dest) {
                String format = getString(R.string.send_abort_self);
                String toastMsg = "";
                switch (error) {
                    case P2PConstant.CommandNum.RECEIVE_ABORT_SELF:
                        toastMsg = String.format(format, dest.alias);
                        break;
                }
                ToastUtils.showTextToast(getApplicationContext(), toastMsg);
            }
        });
    }

    private void initP2P() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (p2PManager != null) {
            if (curNeighbor != null)
                p2PManager.cancelSend(curNeighbor);
            p2PManager.stop();
        }
        Cache.selectedList.clear();
    }

}
