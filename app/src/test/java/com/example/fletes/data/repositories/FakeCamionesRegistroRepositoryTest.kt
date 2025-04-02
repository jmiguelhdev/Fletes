package com.example.fletes.data.repositories

import com.example.fletes.data.model.CamionesRegistro
import kotlinx.coroutines.flow.first
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

    @Test
    fun `getAllCamionesRegistrosStream returns all records`() {
        // Check if getAllCamionesRegistrosStream returns a Flow emitting a list 
        // containing all CamionesRegistro objects in the data list.
        // TODO implement test

    }

    @Test
    fun `getAllCamionesRegistrosStream returns empty list`() {
        // Check if getAllCamionesRegistrosStream returns a Flow emitting an empty list 
        // when the data list is empty.
        // TODO implement test
    }

    @Test
    fun `getCamionesRegistroStream returns correct record`() {
        // Check if getCamionesRegistroStream returns a Flow emitting the correct 
        // CamionesRegistro object when a matching ID is found.
        // TODO implement test
    }

    @Test
    fun `getCamionesRegistroStream returns null`() {
        // Check if getCamionesRegistroStream returns a Flow emitting null when no 
        // CamionesRegistro object with the specified ID is found.
        // TODO implement test
    }

    @Test
    fun `getCamionesRegistroStream invalid id`() {
        // Check if getCamionesRegistroStream returns a Flow emitting null when 
        // the ID is negative or zero, should not exists
        // TODO implement test
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
    fun `insertCamionesRegistro assigns a unique ID`() {
        // Check if insertCamionesRegistro assigns a unique ID to the newly inserted 
        // CamionesRegistro object, incrementing from existing max id.
        // TODO implement test
    }

    @Test
    fun `insertCamionesRegistro with empty data`() {
        // check if insertCamionesRegistro add object when there was no previous data
        // TODO implement test
    }

    @Test
    fun `deleteCamionesRegistro removes a record`() {
        // Check if deleteCamionesRegistro removes the specified CamionesRegistro object 
        // from the data list.
        // TODO implement test
    }

    @Test
    fun `deleteCamionesRegistro does nothing when not found`() {
        // Check if deleteCamionesRegistro does nothing when the specified 
        // CamionesRegistro object is not found in the data list.
        // TODO implement test
    }

    @Test
    fun `updateCamionesRegistro updates a record`() {
        // Check if updateCamionesRegistro updates the specified CamionesRegistro 
        // object in the data list with the new values.
        // TODO implement test
    }

    @Test
    fun `updateCamionesRegistro does nothing when not found`() {
        // Check if updateCamionesRegistro does nothing when the specified 
        // CamionesRegistro object is not found in the data list.
        // TODO implement test
    }

    @Test
    fun `updateCamionesRegistro with invalid id`() {
        // Check if updateCamionesRegistro does nothing when the id is null or zero or negative
        // TODO implement test
    }

    @Test
    fun `getCamionesRegistroByCamionIdStream returns filtered records`() {
        // Check if getCamionesRegistroByCamionIdStream returns a Flow emitting a list 
        // containing only CamionesRegistro objects with the specified camionId.
        // TODO implement test
    }

    @Test
    fun `getCamionesRegistroByCamionIdStream returns empty list`() {
        // Check if getCamionesRegistroByCamionIdStream returns a Flow emitting an 
        // empty list when no CamionesRegistro objects with the specified camionId are 
        // found.
        // TODO implement test
    }

    @Test
    fun `getCamionesRegistroByCamionIdStream invalid id`() {
        // Check if getCamionesRegistroByCamionIdStream returns a Flow emitting an empty 
        // list when the id is invalid or negative.
        // TODO implement test
    }

    @Test
    fun `multiple records with same ID`() {
        // check if function behave properly when multiple records have the same id.
        // TODO implement test
    }

    @Test
    fun `data is updated after update`() {
        // check if the value returned from getAllCamionesRegistrosStream is 
        // updated after insert delete and update
        // TODO implement test
    }

}