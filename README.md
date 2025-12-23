# jPTP [![Build](https://github.com/arassec/jptp/actions/workflows/build.yml/badge.svg)](https://github.com/arassec/jptp/actions/workflows/build.yml) [![Quality Gate](https://img.shields.io/sonar/quality_gate/arassec_jptp?server=http%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=arassec_jptp) [![Code Coverage](https://img.shields.io/sonar/coverage/arassec_jptp?server=http%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/component_measures?id=arassec_jptp&metric=coverage&view=treemap)

## About
This is a rudimentary Java implementation of the "Picture Transfer Protocol" (PTP), which is specified in ISO-15740.

The main goal is to provide an easy-to-use Java interface to USB connected cameras for image capturing.

Not everything specified in ISO-15740 might be supported e.g., the network transport layer has not been implemented.
Main focus so far has been capturing images and downloading them.

So far, the implementation has been tested with the following cameras:
* Canon EOS 500D
* Nikon D3200
* Nikon Z30

## Releases

The released versions are available
at: [![Maven Central](https://img.shields.io/maven-central/v/com.arassec.jptp/jptp?color=green)](https://central.sonatype.com/search?q=jptp)

## Modules

### jptp-core
Contains the classes of the ISO-15740 specification and the core interfaces ``PtpDeviceDiscovery`` and ``PtpDevice``.

### jptp-usb
Provides a low-level interface to control PTP devices attached via USB.
It provides implementations of the core interfaces: ``UsbPtpDeviceDiscovery`` and ``UsbPtpDevice`` respectively.

#### Usage Example

```java
public void printPtpDeviceName() {
    PtpDeviceDiscovery deviceDiscovery = new UsbPtpDeviceDiscovery();
    deviceDiscovery.initialize();

    PtpDevice ptpDevice = deviceDiscovery.discoverPtpDevices().getFirst();
    ptpDevice.initialize();

    CommandResult<DeviceInfo> commandResult = ptpDevice.sendCommand(
            CommandContainer.newInstance(OperationCode.GET_DEVICE_INFO, ptpDevice.getSessionId(), ptpDevice.incrementTransactionId()),
            DeviceInfo.DESERIALIZER);

    System.out.println("Found PTP device: " + commandResult.dataContainer().payload().model().value().rawValue());

    ptpDevice.teardown();

    deviceDiscovery.teardown();
}
```

### jptp-main
Provides a high-level interface to capture images with a PTP device: ``ImageCaptureDevice`` with its implementation ``PtpImageCaptureDevice``.

#### Usage Example
```java
void captureImage() throws IOException {
    ImageCaptureDevice imageCaptureDevice = new PtpImageCaptureDevice(new UsbPtpDeviceDiscovery());

    if (imageCaptureDevice.initialize()) {
        System.out.println("Found PTP device: " + imageCaptureDevice.getDeviceInfo().orElseThrow());
        
        DataObject dataObject = imageCaptureDevice.captureImage();
        
        Files.write(Path.of("captured-image.jpg"), dataObject.data());
        
        imageCaptureDevice.teardown();
    }
}
```