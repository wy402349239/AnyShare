package com.dsj.tool.receiver.tool.p2p.p2pentity.param;


import com.dsj.tool.receiver.tool.p2p.p2pentity.P2PFileInfo;
import com.dsj.tool.receiver.tool.p2p.p2pentity.P2PNeighbor;

/**
 */
public class ParamSendFiles {
    public P2PNeighbor[] neighbors;
    public P2PFileInfo[] files;

    public ParamSendFiles(P2PNeighbor[] neighbors, P2PFileInfo[] files) {
        this.neighbors = neighbors;
        this.files = files;
    }
}
