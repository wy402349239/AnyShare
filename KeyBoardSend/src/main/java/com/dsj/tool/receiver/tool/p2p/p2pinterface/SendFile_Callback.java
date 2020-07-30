package com.dsj.tool.receiver.tool.p2p.p2pinterface;


import com.dsj.tool.receiver.tool.p2p.p2pentity.P2PFileInfo;
import com.dsj.tool.receiver.tool.p2p.p2pentity.P2PNeighbor;

/**
 * 我要发送实现的发送回调
 */
public interface SendFile_Callback {
    public void BeforeSending();

    public void OnSending(P2PFileInfo file, P2PNeighbor dest);

    public void AfterSending(P2PNeighbor dest);

    public void AfterAllSending();

    public void AbortSending(int error, P2PNeighbor dest);
}
