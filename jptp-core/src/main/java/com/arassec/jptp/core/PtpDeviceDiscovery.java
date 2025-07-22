package com.arassec.jptp.core;

import java.util.List;

public interface PtpDeviceDiscovery {

    void initialize();

    void teardown();

    List<PtpDevice> discoverPtpDevices();

}
