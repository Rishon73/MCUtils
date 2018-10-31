# MCUtils
This package contains some useful device and apps management methods we repeatably use in our LeanFT mobile scripts (iOS and Android).
It’ll work for Mobile Center and SromRunner Functional

It contains 2 classes:
* __com.mf.utils.MobileLabUtils__ – manage devices and apps to be used as well the lab that will execute the test
* __com.mf.utils.Logging__ - standardizing logging to console

## Deployment
The recommended way to deploy this jar file is to use the Maven install. 
This can be done either manually or from the Maven Project window in your IDE. 

### Manual
Download the latest _utils-\<version>\.jar_ file from https://github.com/Rishon73/MCUtils/releases and un the maven install command below:
```bash
mvn install:install-file -Dfile="<path>\jars\utils-<version>.jar" -DgroupId=com.mf -DartifactId=utils -Dversion=<version> -Dpackaging=jar
```

### Auto
Clone MCUtils to your working environment and open it in your IDE's, from the Maven Project window > Lifecycle > install

---

### com.mf.utils.MobileLabUtils
This class helps to streamline allocation of a device from a Mobile Center or StormRunner Functional lab.
It allows you to specify the the device type and application you wish to have installed on the device.

Key capabilities:
* Lock device by device description
* Lock device by device ID
* Select app by app identifier and version

### com.mf.utils.Logging
This class helps in providing a consistent logging capability to send messages to standard out and the UFT Pro (LeanFT) reporting class.

Example usage:<br>
***NOTE*** the below code snippet is utilizing Junit.
```java
...
import com.mf.utils;
...

    //Not required to be private but good practice for using as a global within a class
    private MobileLabUtils.LabType labType = MobileLabUtils.LabType.SRF;
    private MobileLabUtils labDevice = new MobileLabUtils();

    ...

    @Before
    public void setUp() throws Exception {
        logging.logMessages("Enter setUp() method ", LOG_LEVEL.INFO );

        //checking to see if any command line argument is passed to the maven using -Dlab=<labtype>
        //availalbe labtyps:
        // MC
        // SRF (this is the default)
        getLabType();

        //define the app to use
        labDevice.setInstallApp(true); // false if you don't want to install an app
        labDevice.setUninstallApp(false);  // true if you want to app to be removed after the test
        labDevice.setHighlight(true);  // false if you don't want to highlight the objects (see how to use in the actual test below)
        labDevice.setPackaged(true);  // false if you won't be using a packaged app
        labDevice.setAppIdentifier("com.mf.iShopping");  // the app string name
        labDevice.setAppVersion("1.1.4");  // version of app to use (not required)

        try {
            DeviceDescription deviceDescription = new DeviceDescription();
            deviceDescription.setOsType("IOS");
            deviceDescription.setOsVersion(">=9.0.0");
            deviceDescription.setModel("iPhone 5s (GSM)");
            deviceDescription.set("testName", "Mobile AOS");

            labDevice.lockDevice(deviceDescription, this.labType);

            if (labDevice.getDevice() != null) {
                appModel = new AppModelAOS_iOS(labDevice.getDevice());
                labDevice.setApp();
                logging.logMessages ("Allocated device: \"" + labDevice.getDevice().getName() + "\" (" + labDevice.getDevice().getId() + "), Model :"
                        + labDevice.getDevice().getModel() + ", OS: " + labDevice.getDevice().getOSType() + " version: " + labDevice.getDevice().getOSVersion()
                        + ", manufacturer: " + labDevice.getDevice().getManufacturer() + ". App in use: \"" + labDevice.getApp().getName()
                        + "\" v" + labDevice.getApp().getVersion(), LOG_LEVEL.INFO);
                if (labDevice.isInstallApp()){
                    logging.logMessages ("Installing app: " + labDevice.getApp().getName(), LOG_LEVEL.INFO);
                    labDevice.getApp().install();
                }else{
                    logging.logMessages ("Installing app: " + labDevice.getApp().getName(), LOG_LEVEL.INFO);
                    labDevice.getApp().restart();
                }
            } else {
                logging.logMessages ("Device couldn't be allocated, exiting script", LOG_LEVEL.ERROR);
            }
        } catch (Exception ex) {
            logging.logMessages ("Exception in setup(): " + ex.getMessage(), LOG_LEVEL.ERROR);
        }
    }

    private void getLabType (){
        if (System.getProperty("lab") != null){
            switch (System.getProperty("lab")){
                case "MC":
                    this.labType = MobileLabUtils.LabType.MC;
                    break;
                case "SRF":
                    this.labType = MobileLabUtils.LabType.SRF;
                    break;
                default:
                    this.labType = MobileLabUtils.LabType.UNKNOWN;
                    if (labType == MobileLabUtils.LabType.UNKNOWN){
                        logging.logMessages("Unknown mobile device lab.  Passed in: "+System.getProperty("lab"),LOG_LEVEL.ERROR);
                        Assert.fail();
                    }
                    break;
            }
        }
    }

    @Test
    public void test() throws GeneralLeanFtException, InterruptedException {
        if (labDevice.getDevice() == null) {
            Assert.fail();
            return;
        }

        try {
            logging.logMessages ("Tap 'Open Menu'", LOG_LEVEL.INFO);
            if (labDevice.isHighlight())
                appModel.AdvantageShoppingApplication().MenuButton().highlight();
            appModel.AdvantageShoppingApplication().MenuButton().tap();

            ...
    }
```

Example usage:<br>
After importing to your project<br>
import com.mf.utils.Logging.*;
```java
public class LeanFtTest extends UnitTestClassBase {
    // Logging class is now Static, which means no need to create a logging object
    // Logging logging = new Logging(); // Global for the test class

    ...

    Logging.logMessage ("Your message goes here", Logging.LOG_LEVEL.INFO);
    Logging.logMessage ("Your message goes here", Logging.LOG_LEVEL.ERROR);
```
