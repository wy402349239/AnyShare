package com.dsj.tool.receiver.tool.p2p.p2pentity.param;


import com.dsj.tool.receiver.tool.p2p.p2pentity.P2PFileInfo;
import com.dsj.tool.receiver.tool.p2p.p2pentity.P2PNeighbor;

public class ParamReceiveFiles {
    public P2PNeighbor Neighbor;
    public P2PFileInfo[] Files;

    public ParamReceiveFiles(P2PNeighbor dest, P2PFileInfo[] files) {
        Neighbor = dest;
        Files = files;
    }
}
