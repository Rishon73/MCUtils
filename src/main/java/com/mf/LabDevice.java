package com.mf;

import com.hp.lft.sdk.GeneralLeanFtException;
import com.hp.lft.sdk.SrfLab;
import com.hp.lft.sdk.mobile.*;
import com.mf.utils.*;

public class LabDevice {
    public enum LabType {MC, SRF, UNKNOWN}

    private boolean installApp = false;
    private boolean uninstallApp = false;
    private boolean highlight = false;
    private boolean packaged = false;
    private String appIdentifier = "";
    private String appVersion = "";
    private Application app = null;
    private Device device = null;

    private ApplicationDescription[] appDescription = new ApplicationDescription[1];

    private Logging log = new Logging();

    /*
    Lock device by Device Description, and device source (MC/ADF)
    */
//    public void lockDevice(DeviceDescription deviceDescription, DeviceSource deviceSource, LabType labType)  throws GeneralLeanFtException{
//        String msg="unknown";
//
//        log.logMessages ("Init device capabilities...", Logging.LOG_LEVEL.INFO);
//        switch (labType){
//            case MC:
//                this.device = MobileLab.lockDevice(deviceDescription, this.appDescription, deviceSource);
//                msg = "MC";
//                break;
//            case SRF:
//                this.device = SrfLab.lockDevice(deviceDescription, this.appDescription, deviceSource);
//                msg = "SRF";
//                break;
//        }
//        log.logMessages("Locking "+msg+" device name: "+ this.getDevice().getName()+ " ID: "+this.getDevice().getId(), Logging.LOG_LEVEL.INFO);
//        log.logMessages ("Exit lockDevice(DeviceDescription, DeviceSource)", Logging.LOG_LEVEL.INFO);
//    }

    /*
    Lock device by Device Description
    */
    public void lockDevice(DeviceDescription deviceDescription, LabType labType)  throws GeneralLeanFtException{
        String msg="unknown";
        log.logMessages ("Init device capabilities...", Logging.LOG_LEVEL.INFO);
        switch (labType){
            case MC:
                this.device = MobileLab.lockDevice(deviceDescription);
                msg = "MC";
                break;
            case SRF:
                this.device = SrfLab.lockDevice(deviceDescription);
                msg = "SRF";
                break;
        }
        log.logMessages("Locking "+msg+" device name: "+ this.getDevice().getName()+ " ID: "+this.getDevice().getId(), Logging.LOG_LEVEL.INFO);
        log.logMessages ("Exit lockDevice(DeviceDescription)", Logging.LOG_LEVEL.INFO);
    }

    /*
    Lock device by Device ID
    */
    public void lockDeviceById (String deviceId, LabType labType) throws GeneralLeanFtException
    {
        String msg = "unknown";
        log.logMessages ("Init device capabilities...", Logging.LOG_LEVEL.INFO);
        switch (labType){
            case MC:
                this.device = MobileLab.lockDeviceById(deviceId);
                msg = "MC";
                break;
            case SRF:
                this.device = SrfLab.lockDeviceById(deviceId);
                msg = "SRF";
                break;
        }
        log.logMessages("Locking "+msg+" device name: "+ this.getDevice().getName()+ " ID: "+this.getDevice().getId(), Logging.LOG_LEVEL.INFO);
        log.logMessages ("Exit lockDeviceById(deviceId)", Logging.LOG_LEVEL.INFO);
    }

    /*
    Setting up the app for the test.
    This method uses the public IS_PACKAGED, APP_VERSION and APP_IDENTIFIER members to identify the app
    */
    public void setApp() throws GeneralLeanFtException{
        ApplicationDescription localAppDescription = new ApplicationDescription.Builder().identifier(appIdentifier).
                packaged(isPackaged()).version(appVersion).build();

        app = device.describe(Application.class, localAppDescription);
    }

    /*
    Setting up the app for the test.
    This method uses accept the 3 parameters to identify the app
    */
    public void setApp(String appIdentifier, String appVersion, Boolean isPackaged) throws GeneralLeanFtException{
        ApplicationDescription localAppDescription = new ApplicationDescription.Builder().identifier(appIdentifier).
                packaged(isPackaged).version(appVersion).build();

        app = device.describe(Application.class, localAppDescription);
    }

    public Application getApp() {
        return this.app;
    }

    public void setInstallApp(Boolean install_app){
        this.installApp = install_app;
    }
    public boolean isInstallApp(){
        return this.installApp;
    }

    public void setUninstallApp(boolean uninstallApp) {
        this.uninstallApp = uninstallApp;
    }

    public boolean isUninstallApp() {
        return uninstallApp;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setPackaged(boolean packaged){
        this.packaged = packaged;
    }

    public boolean isPackaged() {
        return packaged;
    }

    public void setAppIdentifier(String appIdentifier) {
        this.appIdentifier = appIdentifier;
    }

    public String getAppIdentifier() {
        return this.appIdentifier;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
    public Device getDevice(){
        return this.device;
    }

}
