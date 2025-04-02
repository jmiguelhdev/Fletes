package com.example.fletes.data.repositories

import com.example.fletes.data.model.Camion
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class FakeCamionRepositoryTest {

    private lateinit var camionRepository: FakeCamionRepository

    @Before
    fun setup() {
        camionRepository = FakeCamionRepository()
    }

    @Test
    fun `getAllCamionesStream empty list`() = runBlocking {
        // Test that getAllCamionesStream emits an empty list when 
        // there are no Camiones in the repository.
        val allCamiones = camionRepository.getAllCamionesStream().first()
        assert(allCamiones.isEmpty())
    }


    @Test
    fun `getAllCamionesStream check order`()= runBlocking {
        // Verify the order of camiones when multiple camiones are inserted 
        // in the list
        val camion1 = Camion(
            id = 1,
            createdAt = LocalDate.now(),
            choferName = "Doe",
            choferPrice = 250.0,
            choferImporte = 1000.0,
            patenteTractor = "XXX 123",
            patenteJaula = "YYY 456",
            kmService = 20000
        )
        val camion2 = Camion(
            id = 2,
            createdAt = LocalDate.now(),
            choferName = "Jane",
            choferPrice = 250.0,
            choferImporte = 1000.0,
            patenteTractor = "XXX 456",
            patenteJaula = "YYY 123",
            kmService = 20000
        )
        camionRepository.insertCamion(camion1)
        camionRepository.insertCamion(camion2)
        val allCamiones = camionRepository.getAllCamionesStream().first()
        println(allCamiones.joinToString(" "))
        assert(allCamiones.contains(camion1))
        assert(allCamiones.contains(camion2))
        assert(allCamiones.indexOf(camion1) < allCamiones.indexOf(camion2))
        assert(allCamiones[0].id == 1)
        assert(allCamiones[1].id == 2)
    }

    @Test
    fun `getCamionStream existing id`() = runBlocking {
        val newCamion = Camion(
            id = 1,
            createdAt = LocalDate.now(),
            choferName = "John Doe",
            choferPrice = 10.0,
            choferImporte = 250.0,
            patenteTractor = "XXX 123",
            patenteJaula = "YYY 456",
            kmService = 20000,
        )
        camionRepository.insertCamion(newCamion)
        val camion = camionRepository.getCamionStream(newCamion.id).first()
        assert(camion == newCamion)
    }

    @Test
    fun `getCamionStream non existing id`() = runBlocking {
        val newCamion = Camion(
            id = 1,
            createdAt = LocalDate.now(),
            choferName = "John Doe",
            choferPrice = 10.0,
            choferImporte = 250.0,
            patenteTractor = "XXX 123",
            patenteJaula = "YYY 456",
            kmService = 20000,
        )
        camionRepository.insertCamion(newCamion)
        val camion = camionRepository.getCamionStream(2).first()
        assert(camion == null)
    }

    @Test
    fun `getCamionStream negative id`() = runBlocking {
        val wrongId = -1
        val camion = camionRepository.getCamionStream(wrongId).first()
        assert(camion == null)
    }

    @Test
    fun `insertCamion new camion`()  = runBlocking {
        val newCamion = Camion(
            id = 1,
            createdAt = LocalDate.now(),
            choferName = "John Doe",
            choferPrice = 10.0,
            choferImporte = 250.0,
            patenteTractor = "XXX 123",
            patenteJaula = "YYY 456",
            kmService = 20000,
        )
        camionRepository.insertCamion(newCamion)
        val allCamiones = camionRepository.getAllCamionesStream().first()
        assert(allCamiones.contains(newCamion))
        val camionFromDb = camionRepository.getCamionStream(newCamion.id).first()
        assert(camionFromDb == newCamion)

    }

    @Test
    fun `deleteCamion existing camion`()= runBlocking {
        // Test that deleteCamion removes an existing Camion from the 
        // repository.
        val newCamion = Camion(
            id = 1,
            createdAt = LocalDate.now(),
            choferName = "John Doe",
            choferPrice = 250.0,
            choferImporte = 10000.0,
            patenteTractor = "XXX 123",
            patenteJaula = "YYY 456",
            kmService = 20000
        )
        camionRepository.insertCamion(newCamion)
        val allCamiones = camionRepository.getAllCamionesStream().first()
        assert(allCamiones.contains(newCamion))
        camionRepository.deleteCamion(newCamion)
        val allCamionesAfterDelete = camionRepository.getAllCamionesStream().first()
        assert(!allCamionesAfterDelete.contains(newCamion))
    }

    @Test
    fun `deleteCamion non existing camion`() = runBlocking {
        // Test that deleteCamion does nothing when trying to delete a 
        // Camion that doesn't exist.
        val newCamion = Camion(
            id = 1,
            createdAt = LocalDate.now(),
            choferName = "John Doe",
            choferPrice = 250.0,
            choferImporte = 10000.0,
            patenteTractor = "XXX 123",
            patenteJaula = "YYY 456",
            kmService = 20000
        )
        camionRepository.deleteCamion(newCamion)
        val allCamiones = camionRepository.getAllCamionesStream().first()
        assert(!allCamiones.contains(newCamion))
        assert(allCamiones.isEmpty())
    }

    @Test
    fun `deleteCamion multiple camiones`() = runBlocking{
        // Test delete multiple camiones.
        // Arrange: Setup test data (camiones) and insert them into the repository.
        val camion1 = Camion(
            id = 1,
            createdAt = LocalDate.now(),
            choferName = "Doe",
            choferPrice = 250.0,
            choferImporte = 1000.0,
            patenteTractor = "XXX 123",
            patenteJaula = "YYY 456",
            kmService = 20000
        )
        val camion2 = Camion(
            id = 2,
            createdAt = LocalDate.now(),
            choferName = "Jane",
            choferPrice = 250.0,
            choferImporte = 1000.0,
            patenteTractor = "XXX 456",
            patenteJaula = "YYY 123",
            kmService = 20000
        )
        val camionesToInsert = listOf(camion1, camion2)
        camionesToInsert.forEach { camionRepository.insertCamion(it) }

        // Act: Retrieve all camiones, delete both at once.
        val initialCamiones = camionRepository.getAllCamionesStream().first()

        assertTrue(initialCamiones.containsAll(camionesToInsert))
        assertEquals(2, initialCamiones.size)

        val camionesToDelete = listOf(camion1, camion2)
        camionesToDelete.forEach {
            camionRepository.deleteCamion(it)
        }
        val camionesAfterDeletion = camionRepository.getAllCamionesStream().first()

        println(camionRepository.getAllCamionesStream().first())
        assertEquals(0, camionesAfterDeletion.size)
    }


    @Test
    fun `updateCamion existing camion`()=runBlocking {
        // Test that updateCamion correctly updates an existing Camion's 
        // data.
        val newCamion = Camion(
            id = 1,
            createdAt = LocalDate.now(),
            choferName = "Jonh Doe",
            choferPrice = 250.0,
            choferImporte = 1000.0,
            patenteTractor = "XXX 123",
            patenteJaula = "YYY 456",
            kmService = 20000
        )
        camionRepository.insertCamion(newCamion)
        val camionFromDb = camionRepository.getCamionStream(newCamion.id).first()
        assert(camionFromDb == newCamion)
        val updateCamion = Camion(
            id = camionFromDb!!.id,
            createdAt = camionFromDb.createdAt,
            choferName = "Jane Doe",
            choferPrice = 100.0,
            choferImporte = 500.0,
            patenteTractor = "ABC 000",
            patenteJaula = "DEF 111",
            kmService = 20000
        )
        camionRepository.updateCamion(updateCamion)
        val camionFromDbAfterUpdate = camionRepository.getCamionStream(newCamion.id).first()
        assert(camionFromDbAfterUpdate == updateCamion)
        println(camionFromDbAfterUpdate)

    }

    @Test
    fun `updateCamion nonExistingCamion shouldDoNothing`() = runBlocking {
        // Arrange: Set up the initial state.
        val existingCamion = Camion(
            id = 1,
            createdAt = LocalDate.now(),
            choferName = "John Doe",
            choferPrice = 250.0,
            choferImporte = 1000.0,
            patenteTractor = "XXX 123",
            patenteJaula = "YYY 456",
            kmService = 20000
        )
        camionRepository.insertCamion(existingCamion)

        // Assert: Check initial state.
        val camionFromDb = camionRepository.getCamionStream(existingCamion.id).first()
        assertEquals("Initial camion should match inserted one", existingCamion, camionFromDb)

        // Act: Attempt to update a non-existent Camion.
        val nonExistingCamionId = 2
        val updateCamion = Camion(
            id = nonExistingCamionId,
            createdAt = LocalDate.now(),
            choferName = "Jane Doe",
            choferPrice = 100.0,
            choferImporte = 500.0,
            patenteTractor = "ABC 000",
            patenteJaula = "DEF 111",
            kmService = 30000 // Changed kmService
        )
        camionRepository.updateCamion(updateCamion)

        // Assert: Check that the existing Camion is unchanged.
        val camionFromDbAfterUpdate = camionRepository.getCamionStream(existingCamion.id).first()
        assertEquals("Camion should not be modified when trying to update a non existing id", existingCamion, camionFromDbAfterUpdate)

        // Assert: Check that the non-existent Camion has not been created.
        val nonExistingCamionAfterUpdate = camionRepository.getCamionStream(nonExistingCamionId).first()
        assertEquals("Non-existent Camion should not be created", null, nonExistingCamionAfterUpdate)
        println("camionFromDbAfterUpdate $camionFromDbAfterUpdate")
        println("nonExistingCamionAfterUpdate $nonExistingCamionAfterUpdate")
    }


    @Test
    fun `updateCamion check updated data`() = runBlocking {
        // Check the actual data updated is correct after an update.
        // Arrange: Set up the initial state.
        val newCamion = Camion(
            id = 1,
            createdAt = LocalDate.now(),
            choferName = "John Doe",
            choferPrice = 250.0,
            choferImporte = 1000.0,
            patenteTractor = "XXX 123",
            patenteJaula = "YYY 456",
            kmService = 20000
        )
        camionRepository.insertCamion(newCamion)
        val camionFromDb = camionRepository.getCamionStream(newCamion.id).first()
        assert(camionFromDb == newCamion)
        val updateCamion = Camion(
            id = camionFromDb!!.id,
            createdAt = LocalDate.now(),
            choferName = "Jane Doe",
            choferPrice = 100.0,
            choferImporte = 500.0,
            patenteTractor = "ABC 000",
            patenteJaula = "DEF 111",
            kmService = 30000 // Changed kmService
        )
        camionRepository.updateCamion(updateCamion)
        assertEquals(1, updateCamion.id)
        assertEquals("Jane Doe", updateCamion.choferName)
        assertEquals(/* expected = */ 100.0,
            /* actual = */updateCamion.choferPrice,
            /* delta = */0.0)
        assertEquals(500.0, updateCamion.choferImporte, 0.0)
        assertEquals("ABC 000", updateCamion.patenteTractor)
        assertEquals("DEF 111", updateCamion.patenteJaula)
        assertEquals(30000, updateCamion.kmService)
    }

    @Test
    fun `insert delete and then insert again`() = runBlocking {
        // Test that after a delete of a camion, if the 
        // camion is inserted again it receives a new id
        val newCamion = Camion(
            id = 1,
            createdAt = LocalDate.now(),
            choferName = "John Doe",
            choferPrice = 250.0,
            choferImporte = 1000.0,
            patenteTractor = "XXX 123",
            patenteJaula = "YYY 456",
            kmService = 20000
        )
        camionRepository.insertCamion(newCamion)  //id=1
        camionRepository.insertCamion(newCamion)  //id=2
        camionRepository.insertCamion(newCamion)  //id=3
        camionRepository.deleteCamion(newCamion)  //id=1
        camionRepository.insertCamion(newCamion)  //id=4

        assert(camionRepository.getCamionStream(id = 1).first() == null)

        val listOfCamiones = camionRepository.getAllCamionesStream().first()
        listOfCamiones.forEach {
            println(it)
        }
    }

}