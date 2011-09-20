package au.org.emii.aatams

import grails.test.*

class ReceiverTests extends GrailsUnitTestCase 
{
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() 
    {
        super.tearDown()
    }

    void testNonUniqueCodeName() 
    {
        Receiver rx1 = new Receiver(codeName: "VRW2-1111",
                                    serialNumber:"1111",
                                    status:new DeviceStatus(),
                                    model:new DeviceModel(),
                                    organisation:new Organisation())
        Receiver rx2 = new Receiver(codeName: "VRW2-1111",
                                    serialNumber:"2222",
                                    status:new DeviceStatus(),
                                    model:new DeviceModel(),
                                    organisation:new Organisation())
                                
        mockDomain(Receiver, [rx1, rx2])
        mockForConstraintsTests(Receiver, [rx1, rx2])
        
        assertFalse(rx2.validate())
        println(rx2.errors)
    }
}
