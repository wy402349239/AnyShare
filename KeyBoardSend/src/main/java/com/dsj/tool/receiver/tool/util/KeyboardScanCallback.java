package com.dsj.tool.receiver.tool.util;

import com.dsj.tool.receiver.tool.p2p.p2pentity.P2PNeighbor;

public interface KeyboardScanCallback {

    void scanDevice(P2PNeighbor neighbor);

    void scanError();

    void removeDevice(P2PNeighbor neighbor);
}
