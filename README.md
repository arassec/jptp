# jPTP [![Build](https://github.com/arassec/jptp/actions/workflows/build.yml/badge.svg)](https://github.com/arassec/jptp/actions/workflows/build.yml) [![Quality Gate](https://img.shields.io/sonar/quality_gate/arassec_jptp?server=http%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=arassec_jptp) [![Code Coverage](https://img.shields.io/sonar/coverage/arassec_jptp?server=http%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/component_measures?id=arassec_jptp&metric=coverage&view=treemap)

## About
This is an attempt to implement the "Picture Transfer Protocol" (PTP), which is specified in ISO-15740 in Java.

The main goal is to provide an easy-to-use Java interface to USB connected still image devices for image capturing.

The network transport layer has not been implemented.

The project is still very much 'work in progress' and not everything specified in ISO-15740 is supported.

So far, the implementation has been tested with the following cameras:
* Nikon D3200
* Nikon Z30

## Usage

# Low-Level
The low-level interface can be used with the ``jptp-usb`` module:

```
<dependency>
    <groupId>com.arassec.jptp</groupId>
    <artifactId>jptp-usb</artifactId>
    <version>${revision}</version>
</dependency>
```

The main interfaces are ``PtpDeviceDiscovery`` and ``PtpDevice``.
They provide access to usb-connected cameras as follows:

```java
public void printPtpDeviceName() {
    PtpDeviceDiscovery deviceDiscovery = new UsbPtpDeviceDiscovery();
    deviceDiscovery.initialize();

    PtpDevice ptpDevice = deviceDiscovery.discoverPtpDevices().getFirst();
    ptpDevice.initialize();

    CommandResult<DeviceInfo> commandResult = ptpDevice.sendCommand(
            CommandContainer.newInstance(OperationCode.GET_DEVICE_INFO, ptpDevice.getSessionId(), ptpDevice.incrementTransactionId()),
            DeviceInfo.emptyInstance);

    System.out.println("Found PTP device: " + commandResult.dataContainer().payload().model().value().rawValue());

    ptpDevice.teardown();

    deviceDiscovery.teardown();
}
```

# High-Level
The high-level interface can be used with the ``jptp-main`` module:

```
<dependency>
    <groupId>com.arassec.jptp</groupId>
    <artifactId>jptp-main</artifactId>
    <version>${revision}</version>
</dependency>
```

It provides an even further abstraction from specification with the focus on image capturing:

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
