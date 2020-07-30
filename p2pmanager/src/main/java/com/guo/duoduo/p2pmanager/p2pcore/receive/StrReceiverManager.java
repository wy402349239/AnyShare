package com.guo.duoduo.p2pmanager.p2pcore.receive;

import com.guo.duoduo.p2pmanager.p2pconstant.P2PConstant;
import com.guo.duoduo.p2pmanager.p2pcore.MelonHandler;
import com.guo.duoduo.p2pmanager.p2pentity.P2PFileInfo;
import com.guo.duoduo.p2pmanager.p2pentity.P2PNeighbor;
import com.guo.duoduo.p2pmanager.p2pentity.param.ParamIPMsg;
import com.guo.duoduo.p2pmanager.p2pentity.param.ParamReceiveFiles;
import com.guo.duoduo.p2pmanager.p2pentity.param.ParamStrEntity;

/**
 * created by wangyu on 2020/7/29 6:04 PM
 * description:
 */
public class StrReceiverManager extends ReceiveManager{

    public StrReceiverManager(MelonHandler handler) {
        super(handler);
    }

    @Override
    protected void invoke(ParamIPMsg paramIPMsg) {
        String peerIP = paramIPMsg.peerIAddr.getHostAddress();
        P2PFileInfo[] files = new P2PFileInfo[0];
        P2PNeighbor neighbor = p2PHandler.getNeighborManager().getNeighbors().get(peerIP);
        String content = paramIPMsg.peerMSG.addition;
        ParamStrEntity entity = new ParamStrEntity(content, neighbor);
        receiver = new Receiver(this, neighbor, files);

        if (p2PHandler != null)
            p2PHandler.send2UI(P2PConstant.CommandNum.SEND_STR_REQ, entity);
    }
}