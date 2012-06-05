package au.org.emii.aatams.bulk

import au.org.emii.aatams.*
import grails.test.*
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils

class ReceiverLoaderTests extends GrailsUnitTestCase 
{
	ReceiverLoader processor
	private static String HEADER = '''"RCV_ID","RCV_SERIAL_NO","RCV_MODEL_CODE","RCV_OWNER","RCV_COMMENTS","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"''' + '\n'
	
	def currentDateTime = new DateTime("2012-01-01T12:00:00")
	
    protected void setUp() 
	{
        super.setUp()
		
		processor = new ReceiverLoader()
		
		mockDomain(BulkImport)
		mockDomain(BulkImportRecord)
		
		def vr2 = new ReceiverDeviceModel(modelName: "VR2")
		mockDomain(ReceiverDeviceModel, [vr2])
		vr2.save()
		
		DateTimeUtils.setCurrentMillisFixed(currentDateTime.toInstant().getMillis())
		
		mockDomain(Receiver)
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

	void testEmpty()
	{
		assertFailsWithError(null, "Invalid receivers data: data is empty.")
	}
	
	void testInvalidFormat()
	{
		def receiversText = '''"RCV_SERIAL_NO","RCV_MODEL_CODE","RCV_OWNER","RCV_COMMENTS","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
		1661,"VR2",,,15/5/2008 16:01:55,"TAG",15/5/2008 16:01:55,"TAG"
		1663,"VR2",,,15/5/2008 16:01:56,"TAG",15/5/2008 16:01:56,"TAG"
'''
		assertFailsWithError(receiversText, "Invalid receivers data format.")
	}
		
	void testUnknownModelCode() 
	{
		def receiversText = HEADER + '''1,1661,"VR8",,,15/5/2008 16:01:55,"TAG",15/5/2008 16:01:55,"TAG"'''
		
		assertFailsWithError(receiversText, "Unknown model code: VR8")
	}
	
	void testNewOne()
	{
		def receiversText = HEADER + '''1,1661,"VR2",,,15/5/2008 16:01:55,"TAG",15/5/2008 16:01:55,"TAG"'''
		
		assertSuccess(
			receiversText, 
			[["type": BulkImportRecordType.NEW, 
			 "srcPk": 1, 
			 "srcTable": "RECEIVERS", 
			 "srcModifiedDate": ReceiverLoader.DATE_TIME_FORMATTER.parseDateTime("15/5/2008 16:01:55"),
			 "dstClass": "au.org.emii.aatams.Receiver",
			 "serialNumber": "1661"]])
	}
	
	void testDuplicateSerialNumber()
	{
		Receiver receiver = new Receiver(serialNumber: "1661", model: new ReceiverDeviceModel(), organisation: new Organisation())
		receiver.save()
		
		def receiversText = HEADER + '''1,1661,"VR2",,,15/5/2008 16:01:55,"TAG",15/5/2008 16:01:55,"TAG"'''
		
		assertSuccess(
			receiversText, 
			[["type": BulkImportRecordType.DUPLICATE, 
			  "srcPk": 1, 
			  "srcTable": "RECEIVERS", 
			  "srcModifiedDate": ReceiverLoader.DATE_TIME_FORMATTER.parseDateTime("15/5/2008 16:01:55"),
			  "dstClass": "au.org.emii.aatams.Receiver"]])
	}
	
	void testNewTwo()
	{
		def receiversText = HEADER + '''1,1661,"VR2",,,15/5/2008 16:01:55,"TAG",15/5/2008 16:01:55,"TAG"
2,1662,"VR2",,,15/5/2008 16:01:55,"TAG",15/5/2008 16:01:55,"TAG"'''
		
		assertSuccess(
			receiversText, 
			[["type": BulkImportRecordType.NEW, 
			 "srcPk": 1, 
			 "srcTable": "RECEIVERS", 
			 "srcModifiedDate": ReceiverLoader.DATE_TIME_FORMATTER.parseDateTime("15/5/2008 16:01:55"),
			 "dstClass": "au.org.emii.aatams.Receiver",
			 "serialNumber": "1661"],
			 ["type": BulkImportRecordType.NEW, 
			 "srcPk": 2, 
			 "srcTable": "RECEIVERS", 
			 "srcModifiedDate": ReceiverLoader.DATE_TIME_FORMATTER.parseDateTime("15/5/2008 16:01:55"),
			 "dstClass": "au.org.emii.aatams.Receiver",
			 "serialNumber": "1662"]])
	}
	
	void testUpdated()
	{
		Receiver receiver = new Receiver(serialNumber: "1661", model: new ReceiverDeviceModel(), organisation: new Organisation())
		receiver.save()
		
		BulkImport bulkImport = new BulkImport(organisation: new Organisation(), importStartDate: new DateTime(), status: BulkImportStatus.IN_PROGRESS, filename: "some/path")
		bulkImport.save(failOnError: true)
		
		BulkImportRecord importRecord = 
			new BulkImportRecord(
				bulkImport:bulkImport,
				srcTable: "RECEIVERS", 
				srcPk: 1,
				srcModifiedDate: ReceiverLoader.DATE_TIME_FORMATTER.parseDateTime("15/5/2007 16:01:55"),
				dstClass: "au.org.emii.aatams.Receiver",
				dstPk: receiver.id,
				type: BulkImportRecordType.NEW)
		importRecord.save(failOnError:true)
			
		assertEquals(1, Receiver.count())
		assertEquals(1, BulkImport.count())
		assertEquals(1, BulkImportRecord.count())
		
		def receiversText = HEADER + '''1,1662,"VR2",,,15/5/2008 16:01:55,"TAG",15/5/2008 16:01:55,"TAG"'''
		
		assertSuccess(
			receiversText, 
			[["type": BulkImportRecordType.UPDATED, 
			 "srcPk": 1, 
			 "srcTable": "RECEIVERS", 
			 "srcModifiedDate": ReceiverLoader.DATE_TIME_FORMATTER.parseDateTime("15/5/2008 16:01:55"),
			 "dstClass": "au.org.emii.aatams.Receiver",
			 "serialNumber": "1662"]])

		assertEquals(1, Receiver.count())
		assertEquals(2, BulkImport.count())
		assertEquals(2, BulkImportRecord.count())
		assertEquals("1662", receiver.serialNumber)
	}
	
	void testIgnored()
	{
		Receiver receiver = new Receiver(serialNumber: "1661", model: new ReceiverDeviceModel(), organisation: new Organisation())
		receiver.save()
		
		BulkImport bulkImport = new BulkImport(organisation: new Organisation(), importStartDate: new DateTime(), status: BulkImportStatus.IN_PROGRESS, filename: "some/path")
		bulkImport.save(failOnError: true)
		
		BulkImportRecord importRecord = 
			new BulkImportRecord(
				bulkImport:bulkImport,
				srcTable: "RECEIVERS", 
				srcPk: 1,
				srcModifiedDate: ReceiverLoader.DATE_TIME_FORMATTER.parseDateTime("15/5/2009 16:01:55"),
				dstClass: "au.org.emii.aatams.Receiver",
				dstPk: receiver.id,
				type: BulkImportRecordType.NEW)
		importRecord.save(failOnError:true)
			
		assertEquals(1, Receiver.count())
		assertEquals(1, BulkImport.count())
		assertEquals(1, BulkImportRecord.count())
		
		def receiversText = HEADER + '''1,1662,"VR2",,,15/5/2008 16:01:55,"TAG",15/5/2008 16:01:55,"TAG"'''
		
		assertSuccess(
			receiversText, 
			[["type": BulkImportRecordType.IGNORED, 
			 "srcPk": 1, 
			 "srcTable": "RECEIVERS", 
			 "srcModifiedDate": ReceiverLoader.DATE_TIME_FORMATTER.parseDateTime("15/5/2008 16:01:55"),
			 "dstClass": "au.org.emii.aatams.Receiver",
			 "serialNumber": "1661"]])

		assertEquals(1, Receiver.count())
		assertEquals(2, BulkImport.count())
		assertEquals(2, BulkImportRecord.count())
		
		assertEquals("1661", receiver.serialNumber)
	}
	
	/**
	"RCV_ID","RCV_SERIAL_NO","RCV_MODEL_CODE","RCV_OWNER","RCV_COMMENTS","ENTRY_DATETIME","ENTRY_BY","MODIFIED_DATETIME","MODIFIED_BY"
	1,1661,"VR2",,,15/5/2008 16:01:55,"TAG",15/5/2008 16:01:55,"TAG"
	2,1663,"VR2",,,15/5/2008 16:01:56,"TAG",15/5/2008 16:01:56,"TAG"
*/
	private void assertFailsWithError(receiversText, expectedError)
	{
		BulkImport bulkImport = new BulkImport()
		try
		{
			processor.load([bulkImport: bulkImport], receiversText ? new ByteArrayInputStream(receiversText.bytes) : null)
			fail()
		}
		catch (BulkImportException e)
		{
			assertEquals(expectedError, e.message)
			assertEquals(BulkImportStatus.ERROR, bulkImport.status)
		}	
	}

	private def assertSuccess(receiversText, expectedImportRecords)
	{
		Organisation csiro = new Organisation()
		BulkImport bulkImport = new BulkImport(organisation: csiro, importStartDate: new DateTime(), status: BulkImportStatus.IN_PROGRESS, filename: "some/path")
		
		processor.load([bulkImport: bulkImport, organisation: csiro], new ByteArrayInputStream(receiversText.bytes))
		
		bulkImport.save(failOnError: true)
		def importRecords = BulkImportRecord.findAllByBulkImport(bulkImport)
		assertEquals(expectedImportRecords.size(), importRecords.size())
		
		expectedImportRecords.eachWithIndex
		{
			it, i ->
			
			def importRecord = importRecords[i]
			assertNotNull(importRecord)
			assertEquals(bulkImport, importRecord.bulkImport)
			
			assertEquals(it.type, importRecord.type)
			assertEquals(it.srcPk, importRecord.srcPk)
			assertEquals(it.srcTable, importRecord.srcTable)
			assertEquals(it.srcModifiedDate, importRecord.srcModifiedDate)
			assertEquals(it.dstClass, importRecord.dstClass)
			
			if (importRecord.dstPk)
			{
				def receiver = Receiver.get(importRecord.dstPk)
				assertNotNull(receiver)
				assertEquals(it.serialNumber, receiver.serialNumber)
			}
		}
	}
}
