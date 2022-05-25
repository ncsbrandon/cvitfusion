package com.apextalos.cvitfusion.common.mqtt;

import java.util.EventListener;

public interface ConnectionListener extends EventListener {

    public void connectionChange(ConnectionEvent e);
}
