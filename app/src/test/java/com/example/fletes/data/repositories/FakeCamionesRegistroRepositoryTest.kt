package com.example.fletes.data.repositories

import com.example.fletes.data.model.CamionesRegistro
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class FakeCamionesRegistroRepositoryTest {

    private lateinit var camionesRegistroRepository: FakeCamionesRegistroRepository

    @Before
    fun setup() {
        camionesRegistroRepository = FakeCamionesRegistroRepository()
    }

    @Test // added run blocking
    fun `getAllCamionesRegistrosStream returns all records`() {
        // Check if getAllCamionesRegistrosStream returns a Flow emitting a list 
        // containing all CamionesRegistro objects in the data list.
        runBlocking {
            // Insert some initial records
            val camionesRegistro1 = CamionesRegistro(
                id = 1, createdAt = LocalDate.now(), kmCarga = 100, kmDescarga = 200, kmSurtidor = 0,
                litros = 100.0, precioGasOil = 1351.05
            )
            val camionesRegistro2 = CamionesRegistro(
                id = 2, createdAt = LocalDate.now(), kmCarga = 150, kmDescarga = 250, kmSurtidor = 0,
                litros = 150.0, precioGasOil = 1351.05
            )
            camionesRegistroRepository.insertCamionesRegistro(camionesRegistro1)
            camionesRegistroRepository.insertCamionesRegistro(camionesRegistro2)

            // Retrieve all records and check if they match the inserted records
            val allRecords = camionesRegistroRepository.getAllCamionesRegistrosStream().first()
            assertEquals(2, allRecords.size)
            assertEquals(camionesRegistro1, allRecords[0])
            assertEquals(camionesRegistro2, allRecords[1])
        }


    }

    @Test // add runBlocking
    fun `getAllCamionesRegistrosStream returns empty list`()= runBlocking {
        // Check if getAllCamionesRegistrosStream returns a Flow emitting an empty list
        // when the data list is empty.
        assertEquals(emptyList<CamionesRegistro>(),camionesRegistroRepository.getAllCamionesRegistrosStream().first())

    }

    @Test // add runBlocking
    fun `getCamionesRegistroStream returns correct record`()= runBlocking {
        // Check if getCamionesRegistroStream returns a Flow emitting the correct 
        // CamionesRegistro object when a matching ID is found.

         // Insert a record
        val camionesRegistro1 = CamionesRegistro(
            id = 1, createdAt = LocalDate.now(), kmCarga = 100, kmDescarga = 200, kmSurtidor = 0,
            litros = 100.0, precioGasOil = 1351.05
        )

        camionesRegistroRepository.insertCamionesRegistro(camionesRegistro1)

        // retrieve the record with id 1
        val retrievedRecord=camionesRegistroRepository.getCamionesRegistroStream(1).first()

        // verify if is the correct object
        assertEquals(camionesRegistro1,retrievedRecord)
    }

    @Test // Add runBlocking
    fun `getCamionesRegistroStream returns null`()= runBlocking {
        // Check if getCamionesRegistroStream returns a Flow emitting null when no 
        // CamionesRegistro object with the specified ID is found.

        // retrieve the record with id 999, that should not exists
        val retrievedRecord=camionesRegistroRepository.getCamionesRegistroStream(999).first()

        // verify if is null
        assertEquals(null,retrievedRecord)

    }

    @Test // add runBlocking
    fun `getCamionesRegistroStream invalid id`() = runBlocking {
        // Check if getCamionesRegistroStream returns a Flow emitting null when 
        // the ID is negative or zero, should not exists

        // try get with a negative id
        val retrievedRecord1=camionesRegistroRepository.getCamionesRegistroStream(-1).first()
        assertEquals(null,retrievedRecord1)

        // try get with a id zero
        val retrievedRecord2=camionesRegistroRepository.getCamionesRegistroStream(0).first()
        assertEquals(null,retrievedRecord2)

    }

    @Test
    fun `insertCamionesRegistro adds a new record`() = runBlocking {
        val initialCount = camionesRegistroRepository.getAllCamionesRegistrosStream().first().size
        val newCamionesRegistro = CamionesRegistro(
            id = 1,
            createdAt = LocalDate.now(),
            kmCarga = 100,
            kmDescarga = 200,
            kmSurtidor = 0,
            litros = 100.0,
            precioGasOil = 1351.05
        )

        camionesRegistroRepository.insertCamionesRegistro(newCamionesRegistro)

        val updatedCount = camionesRegistroRepository.getAllCamionesRegistrosStream().first().size

        assertEquals(initialCount + 1, updatedCount)

    }
    @Test
    fun `insertCamionesRegistro assigns a unique ID`() = runBlocking {
        // Check if insertCamionesRegistro assigns a unique ID to the newly inserted
        // CamionesRegistro object, incrementing from existing max id.

        // Insert some initial records
        val camionesRegistro1 = CamionesRegistro(id = 1, createdAt = LocalDate.now(), kmCarga = 100, kmDescarga = 200, kmSurtidor = 0, litros = 100.0, precioGasOil = 1351.05)
        val camionesRegistro2 = CamionesRegistro(id = 2, createdAt = LocalDate.now(), kmCarga = 150, kmDescarga = 250, kmSurtidor = 0, litros = 150.0, precioGasOil = 1351.05)
        camionesRegistroRepository.insertCamionesRegistro(camionesRegistro1)
        camionesRegistroRepository.insertCamionesRegistro(camionesRegistro2)

        // Insert a new record
        val newCamionesRegistro = CamionesRegistro(
            id = 3,
            createdAt = LocalDate.now(),
            kmCarga = 300,
            kmDescarga = 400,
            kmSurtidor = 0,
            litros = 200.0,
            precioGasOil = 1351.05
        )
        camionesRegistroRepository.insertCamionesRegistro(newCamionesRegistro)

        // Retrieve all records and check if the new record has a unique ID
        val allRecords = camionesRegistroRepository.getAllCamionesRegistrosStream().first()
        val insertedRecord = allRecords.last()
        assertEquals(3, insertedRecord.id)

    }

    @Test // Add runBlocking
    fun `insertCamionesRegistro with empty data`() = runBlocking {
        // check if insertCamionesRegistro add object when there was no previous data

        val initialCount = camionesRegistroRepository.getAllCamionesRegistrosStream().first().size
        assertEquals(0,initialCount)

        val newCamionesRegistro = CamionesRegistro(
            id = 1,
            createdAt = LocalDate.now(),
            kmCarga = 100,
            kmDescarga = 200,
            kmSurtidor = 0,
            litros = 100.0,
            precioGasOil = 1351.05
        )

        camionesRegistroRepository.insertCamionesRegistro(newCamionesRegistro)
        val updatedCount = camionesRegistroRepository.getAllCamionesRegistrosStream().first().size
        assertEquals(1, updatedCount)
    }

    @Test
    fun `deleteCamionesRegistro removes a record`() = runBlocking {
        // Check if deleteCamionesRegistro removes the specified CamionesRegistro object 
        // from the data list.
        // Insert a new record
        val newCamionesRegistro = CamionesRegistro(
            id = 1,
            createdAt = LocalDate.now(),
            kmCarga = 100,
            kmDescarga = 200,
            kmSurtidor = 0,
            litros = 100.0,
            precioGasOil = 1351.05
        )
        camionesRegistroRepository.insertCamionesRegistro(newCamionesRegistro)
        val initialCount = camionesRegistroRepository.getAllCamionesRegistrosStream().first().size
        assertEquals(1, initialCount)

        camionesRegistroRepository.deleteCamionesRegistro(newCamionesRegistro)

        val updatedCount = camionesRegistroRepository.getAllCamionesRegistrosStream().first().size
        assertEquals(0, updatedCount)
    }

    @Test // add runBlocking
    fun `deleteCamionesRegistro does nothing when not found`()= runBlocking {
        // Check if deleteCamionesRegistro does nothing when the specified 
        // CamionesRegistro object is not found in the data list.
        // Insert a new record
        val newCamionesRegistro = CamionesRegistro(
            id = 1,
            createdAt = LocalDate.now(),
            kmCarga = 100,
            kmDescarga = 200,
            kmSurtidor = 0,
            litros = 100.0,
            precioGasOil = 1351.05
        )
        val initialCount = camionesRegistroRepository.getAllCamionesRegistrosStream().first().size
        camionesRegistroRepository.deleteCamionesRegistro(newCamionesRegistro)
        val updatedCount = camionesRegistroRepository.getAllCamionesRegistrosStream().first().size
        assertEquals(initialCount, updatedCount)
    }

    @Test // add runBlocking
    fun `updateCamionesRegistro updates a record`()= runBlocking {
        // Check if updateCamionesRegistro updates the specified CamionesRegistro 
        // object in the data list with the new values.
        // Insert a new record
        val initialRecord = CamionesRegistro(
            id = 1,
            createdAt = LocalDate.now(),
            kmCarga = 100,
            kmDescarga = 200,
            kmSurtidor = 0,
            litros = 100.0,
            precioGasOil = 1351.05
        )
        camionesRegistroRepository.insertCamionesRegistro(initialRecord)
        val initialCount = camionesRegistroRepository.getAllCamionesRegistrosStream().first().size
        assertEquals(1, initialCount)

        // modify the record and update
        val updatedRecord = CamionesRegistro(
            id = 1,
            createdAt = LocalDate.now(),
            kmCarga = 300, // Update values
            kmDescarga = 400, // Update values
            kmSurtidor = 10, // Update values
            litros = 200.0, // Update values
            precioGasOil = 1400.05 // Update values
        )
        camionesRegistroRepository.updateCamionesRegistro(updatedRecord)
        val finalRecord=camionesRegistroRepository.getCamionesRegistroStream(1).first()
        assertEquals(updatedRecord,finalRecord)

    }


    @Test // add runBlocking
    fun `updateCamionesRegistro does nothing when not found`()= runBlocking {
        // Check if updateCamionesRegistro does nothing when the specified 
        // CamionesRegistro object is not found in the data list.
        // insert a new record
        val initialRecord = CamionesRegistro(
            id = 1,
            createdAt = LocalDate.now(),
            kmCarga = 100,
            kmDescarga = 200,
            kmSurtidor = 0,
            litros = 100.0,
            precioGasOil = 1351.05
        )
        camionesRegistroRepository.insertCamionesRegistro(initialRecord)
        camionesRegistroRepository.updateCamionesRegistro(CamionesRegistro(id = 999, createdAt = LocalDate.now(), kmCarga = 300, kmDescarga = 400, kmSurtidor = 10, litros = 200.0, precioGasOil = 1400.05))
        assertEquals(initialRecord,camionesRegistroRepository.getCamionesRegistroStream(1).first())
    }

    @Test
    fun `updateCamionesRegistro with invalid id`()= runBlocking {
        // Check if updateCamionesRegistro does nothing when the id is null or zero or negative

        val initialRecord = CamionesRegistro(
            id = 1,
            createdAt = LocalDate.now(),
            kmCarga = 100,
            kmDescarga = 200,
            kmSurtidor = 0,
            litros = 100.0,
            precioGasOil = 1351.05
        )
        camionesRegistroRepository.insertCamionesRegistro(initialRecord)

        // try update with a negative id
        camionesRegistroRepository.updateCamionesRegistro(CamionesRegistro(id = -1, createdAt = LocalDate.now(), kmCarga = 300, kmDescarga = 400, kmSurtidor = 10, litros = 200.0, precioGasOil = 1400.05))
        assertEquals(initialRecord,camionesRegistroRepository.getCamionesRegistroStream(1).first())
        // try update with a zero id
        camionesRegistroRepository.updateCamionesRegistro(CamionesRegistro(id = 0, createdAt = LocalDate.now(), kmCarga = 300, kmDescarga = 400, kmSurtidor = 10, litros = 200.0, precioGasOil = 1400.05))
        assertEquals(initialRecord,camionesRegistroRepository.getCamionesRegistroStream(1).first())
     }

    @Test // Add runBlocking
    fun `getCamionesRegistroByCamionIdStream returns filtered records`()= runBlocking {
        // check if getCamionesRegistroByCamionIdStream returns a Flow emitting a list
        // containing only CamionesRegistro objects with the specified camionId.
        // Insert some initial records
        val camionesRegistro1 = CamionesRegistro(
            id = 1, createdAt = LocalDate.now(), kmCarga = 100, kmDescarga = 200, kmSurtidor = 0,
            litros = 100.0, precioGasOil = 1351.05
        )
        val camionesRegistro2 = CamionesRegistro(
            id = 2, createdAt = LocalDate.now(), kmCarga = 150, kmDescarga = 250, kmSurtidor = 0,
            litros = 150.0, precioGasOil = 1351.05
        )
        val camionesRegistro3 = CamionesRegistro(
            id = 3, createdAt = LocalDate.now(), kmCarga = 150, kmDescarga = 250, kmSurtidor = 0,
            litros = 150.0, precioGasOil = 1351.05
        )
        camionesRegistroRepository.insertCamionesRegistro(camionesRegistro1)
        camionesRegistroRepository.insertCamionesRegistro(camionesRegistro2)
        camionesRegistroRepository.insertCamionesRegistro(camionesRegistro3)

        // retrieve the list of camionesRegistro with camionId=1
        val allRecords = camionesRegistroRepository.getCamionesRegistroByCamionIdStream(1).toList()[0]
        assertEquals(1, allRecords.size)
        assertEquals(listOf(camionesRegistro1), allRecords)
    }

    @Test
    fun `getCamionesRegistroByCamionIdStream returns empty list`() = runBlocking{
        // Check if getCamionesRegistroByCamionIdStream returns a Flow emitting an 
        // empty list when no CamionesRegistro objects with the specified camionId are 
        // found.
        val camionesRegistro1 = CamionesRegistro(
            id = 1, createdAt = LocalDate.now(), kmCarga = 100, kmDescarga = 200, kmSurtidor = 0,
            litros = 100.0, precioGasOil = 1351.05
        )

        camionesRegistroRepository.insertCamionesRegistro(camionesRegistro1)
        val retrievedRecord=camionesRegistroRepository.getCamionesRegistroByCamionIdStream(999).first()
        assertEquals(emptyList<CamionesRegistro>(),retrievedRecord)
     }

    @Test
    fun `getCamionesRegistroByCamionIdStream invalid id`()= runBlocking {
        // Check if getCamionesRegistroByCamionIdStream returns a Flow emitting an empty 
        // list when the id is invalid or negative.
        val camionesRegistro1 = CamionesRegistro(
            id = 1, createdAt = LocalDate.now(), kmCarga = 100, kmDescarga = 200, kmSurtidor = 0,
            litros = 100.0, precioGasOil = 1351.05
        )

        camionesRegistroRepository.insertCamionesRegistro(camionesRegistro1)
        // negative id
        val retrievedRecord1=camionesRegistroRepository.getCamionesRegistroByCamionIdStream(-1).first()
        assertEquals(emptyList<CamionesRegistro>(),retrievedRecord1)
        // zero id
        val retrievedRecord2=camionesRegistroRepository.getCamionesRegistroByCamionIdStream(0).first()
        assertEquals(emptyList<CamionesRegistro>(),retrievedRecord2)

    }

    @Test
    fun `multiple records with same ID`() = runBlocking {
        // Check if functions behave properly when multiple records have the same id.

        val camionesRegistro1 = CamionesRegistro(
            id = 1, createdAt = LocalDate.now(), kmCarga = 100, kmDescarga = 200, kmSurtidor = 0,
            litros = 100.0, precioGasOil = 1351.05
        )
        val camionesRegistro2 = CamionesRegistro(
            id = 2, createdAt = LocalDate.now(), kmCarga = 150, kmDescarga = 250, kmSurtidor = 0,
            litros = 150.0, precioGasOil = 1351.05
        )

        camionesRegistroRepository.insertCamionesRegistro(camionesRegistro1)
        camionesRegistroRepository.insertCamionesRegistro(camionesRegistro2)

        // Verify that both records are present
        val allRecords = camionesRegistroRepository.getAllCamionesRegistrosStream().first()
        assertEquals(2, allRecords.size)
        assertEquals(listOf(camionesRegistro1,camionesRegistro2),allRecords)
    }

    @Test
    fun `data is updated after update`() = runBlocking {
        // check if the value returned from getAllCamionesRegistrosStream is 
        // updated after insert delete and update
        // insert some records

        val camionesRegistro1 = CamionesRegistro(
            id = 1, createdAt = LocalDate.now(), kmCarga = 100, kmDescarga = 200, kmSurtidor = 0,
            litros = 100.0, precioGasOil = 1351.05
        )
        val camionesRegistro2 = CamionesRegistro(
            id = 2, createdAt = LocalDate.now(), kmCarga = 150, kmDescarga = 250, kmSurtidor = 0,
            litros = 150.0, precioGasOil = 1351.05
        )

        camionesRegistroRepository.insertCamionesRegistro(camionesRegistro1)
        camionesRegistroRepository.insertCamionesRegistro(camionesRegistro2)

        var allRecords = camionesRegistroRepository.getAllCamionesRegistrosStream().first()
        assertEquals(2, allRecords.size)

        // delete the second record
        camionesRegistroRepository.deleteCamionesRegistro(camionesRegistro2)

        // check if the list is updated after delete
        allRecords = camionesRegistroRepository.getAllCamionesRegistrosStream().first()
        assertEquals(1, allRecords.size)

        // insert a new record
        val camionesRegistro3 = CamionesRegistro(
            id = 3, createdAt = LocalDate.now(), kmCarga = 300, kmDescarga = 400, kmSurtidor = 0,
            litros = 200.0, precioGasOil = 1400.05
        )
        camionesRegistroRepository.insertCamionesRegistro(camionesRegistro3)

        // check if the list is updated after insert
        allRecords = camionesRegistroRepository.getAllCamionesRegistrosStream().first()
        assertEquals(2, allRecords.size)

        // update the first record
        val updatedRecord = camionesRegistro1.copy(litros=999.99)
        camionesRegistroRepository.updateCamionesRegistro(updatedRecord)

        // check if the list is updated after update
        allRecords = camionesRegistroRepository.getAllCamionesRegistrosStream().first()
        assertEquals(2, allRecords.size)
        assertEquals(updatedRecord, allRecords[0])
    }

}