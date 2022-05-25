package com.apextalos.cvitfusion.common.mqtt.connection;

import java.util.EventListener;

public interface ConnectionListener extends EventListener {

    public void connectionChange(ConnectionEvent e);
}
