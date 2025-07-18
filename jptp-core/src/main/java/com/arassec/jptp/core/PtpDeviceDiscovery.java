package com.arassec.jptp.core;

import java.util.List;

public interface PtpDeviceDiscovery {

    void initialize();

    List<PtpDevice> discoverPtpDevices();

    void teardown();

}
