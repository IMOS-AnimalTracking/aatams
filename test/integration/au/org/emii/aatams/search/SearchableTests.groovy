package au.org.emii.aatams.search

import au.org.emii.aatams.*

import org.joda.time.*
import grails.test.*

import java.text.SimpleDateFormat

class SearchableTests extends GrailsUnitTestCase  {
    protected void setUp()  {
        super.setUp()
    }

    protected void tearDown()  {
        super.tearDown()
    }

    void testSearchOrganisationByName()  {
        def csiro = Organisation.findByName("CSIRO")
        def imos = Organisation.findByName("IMOS")

        def searchResult = Organisation.search("CSIRO")

        assertTrue(searchResult.results*.name.contains(csiro.name))
        assertFalse(searchResult.results*.name.contains(imos.name))
    }

    void testSearchProjects() {
        def sealCount = Project.findByName('Seal Count')
        def searchResult = Project.search(sealCount.name)

        assertTrue(searchResult.results*.id.contains(sealCount.id))
    }

    void testSearchDeploymentsByReceiver() {
        def rx1 = Receiver.findBySerialNumber('101336')
        def rx2 = Receiver.findBySerialNumber('101337')

        def rx1Bondi = ReceiverDeployment.findByReceiver(rx1)
        def rx2Bondi = ReceiverDeployment.findByReceiver(rx2)

        def searchResult = ReceiverDeployment.search(rx1.name)

        assertTrue(searchResult.results*.id.contains(rx1Bondi.id))
        assertFalse(searchResult.results*.id.contains(rx2Bondi.id))
    }

    void testSearchDeploymentsByStations() {
        def bondiSW1 = InstallationStation.findByName('Bondi SW1')
        def bondiSW2 = InstallationStation.findByName('Bondi SW2')

        def rx1Bondi = ReceiverDeployment.findByStation(bondiSW1)
        def rx2Bondi = ReceiverDeployment.findByStation(bondiSW2)

        def searchResult = ReceiverDeployment.search(bondiSW1.name)

        assertTrue(searchResult.results*.id.contains(rx1Bondi.id))
        assertFalse(searchResult.results*.id.contains(rx2Bondi.id))
    }

    void testSearchRecoveriesByReceiver() {
        def rx1 = Receiver.findBySerialNumber('101336')

        Calendar cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 12)
        cal.set(Calendar.MINUTE, 34)
        cal.set(Calendar.SECOND, 56)
        cal.set(Calendar.MILLISECOND, 0)
        cal.add(Calendar.DATE, 2)

        def rx1BondiRecovery = ReceiverRecovery.findByRecoveryDateTime(new DateTime(cal.getTime().getTime()))

        cal.set(Calendar.MINUTE, 54)
        def rx2BondiRecovery = ReceiverRecovery.findByRecoveryDateTime(new DateTime(cal.getTime().getTime()))

        def searchResult = ReceiverRecovery.search(rx1.name)

        assertTrue(searchResult.results*.id.contains(rx1BondiRecovery.id))
        assertFalse(searchResult.results*.id.contains(rx2BondiRecovery.id))
    }

//    void testSearchExportsByReceiverViaDetection()
//    {
//        def rx1 = Receiver.findByCodeName('VR2W-101336')
//
//        def export1 = ReceiverDownloadFile.findByName("export1.csv")
//        def export2 = ReceiverDownloadFile.findByName("export2.csv")
//
//        def searchResult = ReceiverDownloadFile.search(rx1.codeName)
//
//        assertTrue(searchResult.results*.id.contains(export1.id))
//        assertFalse(searchResult.results*.id.contains(export2.id))
//    }

//    void testSearchExportsByTag()
//    {
//        def tag1 = Tag.findByCodeName('A69-1303-62339')
//
//        def export1 = ReceiverDownloadFile.findByName("export1.csv")
//        def export2 = ReceiverDownloadFile.findByName("export2.csv")
//
//        def searchResult = ReceiverDownloadFile.search(tag1.serialNumber)
//
//        assertTrue(searchResult.results*.id.contains(export1.id))
//        assertFalse(searchResult.results*.id.contains(export2.id))
//    }

//    void testSearchExportsByReceiverViaEvent()
//    {
//        def rx2 = Receiver.findByCodeName('VR2W-101337')
//
//        def export1 = ReceiverDownloadFile.findByName("export1.csv")
//        def export2 = ReceiverDownloadFile.findByName("export2.csv")
//
//        def searchResult = ReceiverDownloadFile.search(rx2.codeName)
//
//        assertFalse(searchResult.results*.id.contains(export1.id))
//        assertTrue(searchResult.results*.id.contains(export2.id))
//    }
}
