package com.dsj.tool.receiver.tool.p2p.p2pentity.param;


import com.dsj.tool.receiver.tool.p2p.p2pentity.P2PNeighbor;

public class ParamTCPNotify {
    public P2PNeighbor Neighbor;
    public Object Obj;

    public ParamTCPNotify(P2PNeighbor dest, Object obj) {
        Neighbor = dest;
        Obj = obj;
    }
}
