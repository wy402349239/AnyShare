package com.dsj.tool.receiver.tool.p2p.p2pcore.send;


import android.util.Log;

import com.dsj.tool.receiver.tool.p2p.p2pconstant.P2PConstant;
import com.dsj.tool.receiver.tool.p2p.p2pcore.MelonHandler;
import com.dsj.tool.receiver.tool.p2p.p2pentity.P2PFileInfo;
import com.dsj.tool.receiver.tool.p2p.p2pentity.P2PNeighbor;
import com.dsj.tool.receiver.tool.p2p.p2pentity.param.ParamIPMsg;
import com.dsj.tool.receiver.tool.p2p.p2pentity.param.ParamStrEntity;
import com.dsj.tool.receiver.tool.p2p.p2pentity.param.ParamTCPNotify;

import java.util.HashMap;

/**
 * Created by xxx on 2015/9/20.
 */
public class SendManager {
    private static final String tag = SendManager.class.getSimpleName();

    private MelonHandler p2PHandler;
    private HashMap<String, Sender> mSenders;

    private SendServer sendServer;
    private SendServerHandler sendServerHandler;

    public SendManager(MelonHandler handler) {
        this.p2PHandler = handler;
        mSenders = new HashMap<>();
        init();
    }

    private void init() {
        mSenders.clear();
    }

    public void disPatchMsg(int what, Object obj, int src) {
        switch (src) {
            case P2PConstant.Src.COMMUNICATE: {
                String peerIP = ((ParamIPMsg) obj).peerIAddr.getHostAddress();
                Sender sender = getSender(peerIP);
                sender.dispatchCommMSG(what, (ParamIPMsg) obj);    //dispatch
                break;
            }
            case P2PConstant.Src.MANAGER: {
                if (what == P2PConstant.CommandNum.SEND_STR_REQ) {
                    if (!mSenders.isEmpty())
                        return;
                    ParamStrEntity entity = (ParamStrEntity) obj;
                    invoke(entity.getNeighbor(), entity.getContent());
                } else if (what == P2PConstant.CommandNum.SEND_ABORT_SELF) {
                    Sender sender = getSender(((P2PNeighbor) obj).ip);
                    sender.dispatchUIMSG(what);
                }
                break;
            }
            case P2PConstant.Src.SEND_TCP_THREAD: {
                String peerIP = ((ParamTCPNotify) obj).Neighbor.ip;
                Sender sender = getSender(peerIP);
                if (sender == null)
                    return;
                if (what == P2PConstant.CommandNum.SEND_PERCENTS) {
                    sender.flagPercents = false;
                }
                sender.dispatchTCPMsg(what, (ParamTCPNotify) obj);
                break;
            }

        }
    }

    private void invoke(P2PNeighbor neighbors, String msg) {
        P2PFileInfo[] files = new P2PFileInfo[0];
        P2PNeighbor melon = p2PHandler.getNeighborManager().getNeighbors()
                .get(neighbors.ip);
        Sender sender = null;
        if (melon != null) {
            sender = new Sender(p2PHandler, this, melon, files);
        }

        mSenders.put(neighbors.ip, sender);

        if (melon != null) {//通知对方，我要发送文件了
            if (p2PHandler != null)
                p2PHandler.send2Receiver(melon.inetAddress,
                        P2PConstant.CommandNum.SEND_STR_REQ, msg);
        }
    }

    public void startSend(String peerIP, Sender fileSender) {
        if (sendServer == null) {
            Log.d(tag, "SendManager start send");

            sendServerHandler = new SendServerHandler(this);
            sendServer = new SendServer(sendServerHandler, P2PConstant.PORT);
            sendServer.start();
            sendServer.isReady();
        }
        mSenders.put(fileSender.neighbor.ip, fileSender);
    }

    public void removeSender(String peerIP) {
        mSenders.remove(peerIP);
        checkAllOver();
    }

    public void checkAllOver() {
        if (mSenders.isEmpty()) {
            p2PHandler.releaseSend();
        }
    }

    public void quit() {
        mSenders.clear();
        if (sendServer != null) {
            sendServer.quit();
            sendServer = null;
        }
    }

    protected Sender getSender(String peerIP) {
        return mSenders.get(peerIP);
    }
}
