# MCUtils
This package contains:
* com.mf.utils.LabDevice
* com.mf.utils.Logging

## Deployment
If you wish to just use the file and not compile it yourself, then use Maven to add the .\jars\utils-1.1.jar to your local .m2 cache using<br>
```
mvn install:install-file -Dfile="<path>\jars\utils-1.1jar" -DgroupId=com.mf -DartifactId=utils -Dversion=1.1 -Dpackaging=jar
```

### com.mf.utils.LabDevice
This class helps to streamline allocation of a device from a Mobile Center or StormRunner Functional lab.

Key capabilities:
* Lock device by device description
* Lock device by device ID

### com.mf.utils.Logging
This class helps in providing a consistent logging capability to send messages to standard out and the UFT Pro (LeanFT) reporting class.
This class allows you to specify the the device type and application you wish to have installed on the device.

Example usage:<br>
***NOTE*** the below code snippet is utilizing Junit.
```
...
import com.mf.LabDevice;
...

    //Not required to be private but good practice for using as a global within a class
    private LabDevice.LabType labType = LabDevice.LabType.SRF;
    private LabDevice labDevice = new LabDevice();

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

            labDevice.lockDevice(deviceDescription, LabDevice.LabType.SRF);

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
                    this.labType = LabDevice.LabType.MC;
                    break;
                case "SRF":
                    this.labType = LabDevice.LabType.SRF;
                    break;
                default:
                    this.labType = LabDevice.LabType.UNKNOWN;
                    if (labType == LabDevice.LabType.UNKNOWN){
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
```
public class LeanFtTest extends UnitTestClassBase {
    Logging logging = new Logging(); // Global for the test class

    ...

    logging.logMessages ("Your message goes here", LOG_LEVEL.INFO);
    logging.logMessages ("Your message goes here", LOG_LEVEL.ERROR);
```
