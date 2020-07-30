package com.dsj.tool.receiver.tool.p2p.p2pentity.param;

import com.dsj.tool.receiver.tool.p2p.p2pentity.P2PNeighbor;

/**
 * created by wangyu on 2020/7/29 5:51 PM
 * description:
 */
public class ParamStrEntity {
    String content;

    public P2PNeighbor neighbor;

    public ParamStrEntity(String content, P2PNeighbor neighbors) {
        this.content = content;
        this.neighbor = neighbors;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public P2PNeighbor getNeighbor() {
        return neighbor;
    }

    public void setNeighbor(P2PNeighbor neighbor) {
        this.neighbor = neighbor;
    }
}