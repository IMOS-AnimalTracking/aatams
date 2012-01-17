databaseChangeLog = 
{
	changeSet(author: "jburgess (generated)", id: "1326330938494-1") {
		createTable(tableName: "sensor") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "sensor_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "intercept", type: "FLOAT4(8,8)")

			column(name: "ping_code", type: "int4") {
				constraints(nullable: "false")
			}

			column(name: "slope", type: "FLOAT4(8,8)")

			column(name: "tag_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "transmitter_type_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "unit", type: "VARCHAR(255)")

			column(name: "sensors_idx", type: "int4", defaultValueNumeric: 0)
			
			column(name: "transmitter_id", type: "VARCHAR(255)")
			{
				constraints(nullable: "false")
			}
		}
	}

	// Create all pinger sensors.
	changeSet(author: "jburgess (generated)", id: "1326330938494-1-1") 
	{
		sql('''insert into sensor (id, version, intercept, ping_code, slope, tag_id, transmitter_type_id, unit, transmitter_id) 
               select nextval('hibernate_sequence'), 0, intercept, ping_code, slope, device.id, transmitter_type_id, unit, code_map.code_map || '-' || ping_code 
			   from device 
			   join code_map on code_map.id = device.code_map_id
			   where class = 'au.org.emii.aatams.Tag';''')
	}

	// Move au.org.emii.Sensor in existing tag table to sensors.
	changeSet(author: "jburgess (generated)", id: "1326330938494-1-2")
	{
		sql('''insert into sensor (id, version, intercept, ping_code, slope, tag_id, transmitter_type_id, unit, sensors_idx, transmitter_id)
		       select nextval('hibernate_sequence'), 0, intercept, ping_code, slope, tag_id, transmitter_type_id, unit, (select max(sensors_idx) + 1 from sensor where tag_id = tag_id), code_map.code_map || '-' || ping_code
			   from device 
			   join code_map on code_map.id = device.code_map_id
               where class = 'au.org.emii.aatams.Sensor';''')

		//select max(sensors_idx) + 1 from sensor where tag_id = tag_id
		
		// Delete the records in device table.
		delete(tableName: "device")
		{
			where "class = 'au.org.emii.aatams.Sensor'"
		}
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-2") 
	{
		addColumn(tableName: "detection_surgery") 
		{
			column(name: "sensor_id", type: "int8") 
			{
				constraints(nullable: "true")
			}
		}

		// Populate the sensor_id column.
		sql('''update detection_surgery 
			   set sensor_id = sensor.id
			   from sensor
			   where sensor.tag_id = detection_surgery.tag_id;''')
		
		// Add not nullable constraint
		addNotNullConstraint(tableName: "detection_surgery", columnName: "sensor_id")
	}


	changeSet(author: "jburgess (generated)", id: "1326330938494-3") {
		addNotNullConstraint(columnDataType: "VARCHAR(255)", columnName: "serial_number", tableName: "device")
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-4") {
		modifyDataType(columnName: "initialisationdatetime_timestamp", newDataType: "TIMESTAMP WITH TIME ZONE", tableName: "receiver_deployment")
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-5") {
		dropForeignKeyConstraint(baseTableName: "detection_surgery", baseTableSchemaName: "public", constraintName: "fkdf258e3bceab1a01")
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-6") {
		dropForeignKeyConstraint(baseTableName: "device", baseTableSchemaName: "public", constraintName: "fkb06b1e56ceab1a01")
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-7") {
		dropForeignKeyConstraint(baseTableName: "device", baseTableSchemaName: "public", constraintName: "fkb06b1e564b0c3bc4")
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-8") {
		createIndex(indexName: "ping_code_idx", tableName: "sensor", unique: "false") {
			column(name: "ping_code")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-9") {
		addForeignKeyConstraint(baseColumnNames: "sensor_id", baseTableName: "detection_surgery", baseTableSchemaName: "public", constraintName: "fkdf258e3bd7d8b793", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sensor", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-10") {
		addForeignKeyConstraint(baseColumnNames: "tag_id", baseTableName: "sensor", baseTableSchemaName: "public", constraintName: "fkca0053baceab1a01", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-11") {
		addForeignKeyConstraint(baseColumnNames: "transmitter_type_id", baseTableName: "sensor", baseTableSchemaName: "public", constraintName: "fkca0053ba4b0c3bc4", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "transmitter_type", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-12") {
		dropIndex(indexName: "code_name_index", tableName: "device")
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-13") {
		dropColumn(columnName: "tag_id", tableName: "detection_surgery")
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-14") {
		dropColumn(columnName: "code_name", tableName: "device")
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-15") {
		dropColumn(columnName: "intercept", tableName: "device")
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-16") {
		dropColumn(columnName: "ping_code", tableName: "device")
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-17") {
		dropColumn(columnName: "slope", tableName: "device")
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-18") {
		dropColumn(columnName: "tag_id", tableName: "device")
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-19") {
		dropColumn(columnName: "transmitter_type_id", tableName: "device")
	}

	changeSet(author: "jburgess (generated)", id: "1326330938494-20") {
		dropColumn(columnName: "unit", tableName: "device")
	}
}
