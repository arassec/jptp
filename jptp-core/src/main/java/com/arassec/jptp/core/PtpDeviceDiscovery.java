package com.arassec.jptp.core;

import java.util.List;

/**
 * A discovery for PTP devices.
 */
public interface PtpDeviceDiscovery {

    /**
     * Initializes the discovery and has to be called before searching for devices.
     */
    void initialize();

    /**
     * Releases acquired resources and has to be called before shutting down.
     */
    void teardown();

    /**
     * Returns a list of all available PTP devices.
     *
     * @return A list of available devices.
     */
    List<PtpDevice> discoverPtpDevices();

}
