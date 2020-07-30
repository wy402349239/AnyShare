package com.dsj.tool.receiver.tool.p2p.p2pentity.param;


import com.dsj.tool.receiver.tool.p2p.p2pentity.SigMessage;

import java.net.InetAddress;

public class ParamIPMsg {
    public SigMessage peerMSG;
    public InetAddress peerIAddr;
    public int peerPort;

    public ParamIPMsg(String msg, InetAddress addr, int port) {
        peerMSG = new SigMessage(msg);
        peerIAddr = addr;
        peerPort = port;
    }
}
