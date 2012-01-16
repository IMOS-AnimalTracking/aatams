package au.org.emii.aatams

import au.org.emii.aatams.test.AbstractControllerUnitTestCase
import grails.test.*

class SensorControllerTests extends AbstractControllerUnitTestCase
{
	CodeMap a69_1303
	Tag owningTag
	Project project
	
	TransmitterType pinger
	TransmitterType temp
	TransmitterType pressure

	TagDeviceModel model
	
    protected void setUp() 
	{
        super.setUp()
		
		mockLogging(TagFactoryService)
		controller.tagFactoryService = new TagFactoryService()
		
		a69_1303 = new CodeMap(codeMap: "A69-1303")
		mockDomain(CodeMap, [a69_1303])
		a69_1303.save()
		
		DeviceStatus newStatus = new DeviceStatus(status:"NEW")
		mockDomain(DeviceStatus, [newStatus])
		newStatus.save()
		
		model = new TagDeviceModel(modelName:"V16")
		mockDomain(TagDeviceModel, [model])
		
		owningTag = new Tag(codeMap:a69_1303, model:model, status:newStatus)
		mockDomain(Tag, [owningTag])
		owningTag.save()
		
		mockDomain(Sensor)
		
		project = new Project(name: "Hammerheads")
		mockDomain(Project, [project])
		
		pinger = new TransmitterType(transmitterTypeName: 'PINGER')
		temp = new TransmitterType(transmitterTypeName: 'TEMP')
		pressure = new TransmitterType(transmitterTypeName: 'PRESSURE')
		def transmitterTypeList = [pinger, temp, pressure]
		mockDomain(TransmitterType, transmitterTypeList)
		transmitterTypeList.each { it.save() }
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

	void testSavePingerNoMatchingSerialNumber()
	{
		assertSaveTag(1111, pinger)
	}	
	
	void testSaveSensorExistingSerialNumber()
	{
		createTag("12345")
		
		assertSaveTag(2222, pressure)		
	}

	private void createTag(serialNumber) 
	{
		Tag tag = new Tag(serialNumber:serialNumber,
				project:project,
				model:model,
				codeMap:a69_1303,
				expectedLifeTimeDays:100,
				status:new DeviceStatus())
		tag.save()
		assertFalse(tag.hasErrors())
	}
	
	private assertSaveTag(pingCode, transmitterType)
	{
		assertEquals(0, Sensor.count())
		
		def saveParams = [tag: [serialNumber:"12345",
								project:project,
								model:model,
								codeMap:a69_1303,
								expectedLifeTimeDays:100,
								status:new DeviceStatus()],
						  transmitterType:transmitterType,
						  pingCode:pingCode]
		
		controller.params.putAll(saveParams)
		
		controller.save()
	
		assertEquals("tag", controller.redirectArgs.controller)
		assertEquals("show", controller.redirectArgs.action)
		
		def tagId = controller.redirectArgs.id
		def tag = Tag.get(tagId)
		assertNotNull(tag)
		
		assertEquals("12345", tag.serialNumber)
		
		assertEquals(1, Sensor.count())
		Sensor sensor = Sensor.findByPingCode(pingCode)
		assertNotNull(sensor)
		assertEquals(tag, sensor.tag)
	}
	
	void testUpdateWithNullPingCode()
	{
		// should remove PINGER sensor from tag.
	}
	
}
