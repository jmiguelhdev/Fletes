package com.example.fletes.data.repositories

import com.example.fletes.data.model.Destino
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FakeDestinoRepositoryTest {
    private lateinit var destinoRepository: FakeDestinoRepository

    @Before
    fun setup() {
        destinoRepository = FakeDestinoRepository()
    }

    @Test
    fun `getAllDestinosStream empty list`() {
        // Test that getAllDestinosStream returns an empty list when no destinations have been inserted.
        runBlocking {
            val destinos = destinoRepository.getAllDestinosStream().first()
            assertTrue(destinos.isEmpty())
        }

    }

    @Test
    fun `getAllDestinosStream single item`() {
        // Test that getAllDestinosStream returns a list containing the single 
        // destination that has been inserted.
        runBlocking {
            val newDestino = Destino(
                id = 0,
                localidad = "Destino Prueba",
                distancia = 650.0
            )
            destinoRepository.insertDestino(newDestino)

            val destinos = destinoRepository.getAllDestinosStream().first()
            assertEquals(1, destinos.size)
            assertTrue(destinos.any { it.localidad == "Destino Prueba" })
            assertTrue(destinos.any { it.id == 1 })
            assert(destinos[0].distancia == 650.0)
        }

    }

    @Test
    fun `getAllDestinosStream multiple items`() {
        // Test that getAllDestinosStream returns a list containing all inserted destinations in the correct order.
        runBlocking {
            val newDestino1 = Destino(
                id = 0,
                localidad = "Destino 1",
                distancia = 100.0
            )
            val newDestino2 = Destino(
                id = 0,
                localidad = "Destino 2",
                distancia = 200.0
            )
            val newDestino3 = Destino(
                id = 0,
                localidad = "Destino 3",
                distancia = 300.0
            )
            destinoRepository.insertDestino(newDestino1)
            destinoRepository.insertDestino(newDestino2)
            destinoRepository.insertDestino(newDestino3)

            val destinos = destinoRepository.getAllDestinosStream().first()
            assertEquals(3, destinos.size)
            assertEquals("Destino 1", destinos[0].localidad)
            assertEquals("Destino 2", destinos[1].localidad)
            assertEquals("Destino 3", destinos[2].localidad)
        }

    }

    @Test
    fun `getAllDestinosStream data integrity`() {
        // Test that getAllDestinosStream returns a copy of the internal list, 
        // so modifications to the returned list do not affect the repository's data.
        runBlocking {
            val newDestino1 = Destino(
                id = 0,
                localidad = "Destino 1",
                distancia = 100.0
            )
            val newDestino2 = Destino(
                id = 0,
                localidad = "Destino 2",
                distancia = 200.0
            )
            destinoRepository.insertDestino(newDestino1)
            destinoRepository.insertDestino(newDestino2)

            val destinos = destinoRepository.getAllDestinosStream().first()
            val destinosMutable = destinos.toMutableList()

            // Modify the returned list.
            destinosMutable.removeAt(0)
            val modifiedDestino = destinos[0].copy(localidad = "Destino Modificado", distancia = 300.0)
            destinosMutable[0] = modifiedDestino

            // Check that the repository's data was not affected.
            val repoDestinos = destinoRepository.getAllDestinosStream().first()
            assertEquals(2, repoDestinos.size)
            assertEquals("Destino 1", repoDestinos[0].localidad)
        }

    }

    @Test
    fun `getDestinoStream existing ID`() {
        // Test that getDestinoStream returns the correct destination when given an existing ID.
        // TODO implement test
        runBlocking {
            val newDestino = Destino(
                id = 0,
                localidad = "Destino Prueba",
                distancia = 650.0
            )
            destinoRepository.insertDestino(newDestino)

            val destino = destinoRepository.getDestinoStream(1).first()!!
            assertEquals(1, destino.id)
            assertEquals("Destino Prueba", destino.localidad)
            assertEquals(650.0, destino.distancia, 0.001)



        }
    }

    @Test
    fun `getDestinoStream non existent ID`() {
        runBlocking {
            val newDestino = Destino(id = 0, localidad = "Destino Prueba", distancia = 650.0)
            destinoRepository.insertDestino(newDestino)
            val destino = destinoRepository.getDestinoStream(999).first()
            assert(destino == null)
        }
    }



    @Test
    fun `insertDestino successful insertion`() = runBlocking {
        // Test that insertDestino successfully adds a new destination to the repository.
        val newDestino = Destino(
            id = 1,
            localidad = "Destino Prueba",
            distancia = 650.0
        )
        destinoRepository.insertDestino(newDestino)

        val destinos = destinoRepository.getAllDestinosStream().first()
        assertEquals(1, destinos.size)
        assertTrue(destinos.any { it.localidad == "Destino Prueba" })
        assertTrue(destinos.any { it.id == 1 })
        assert(destinos[0].distancia == 650.0)
        destinos.forEach { println(it) }

    }
    @Test
    fun `insertDestino ID assignment`() = runBlocking {
        // Test that insertDestino automatically assigns a unique, increasing ID to the new destination.
        val newDestino1 = Destino(
            id = 0,
            localidad = "Destino Prueba 1",
            distancia = 650.0
        )
        val newDestino2 = Destino(
            id = 0,
            localidad = "Destino Prueba 2",
            distancia = 120.0
        )

        destinoRepository.insertDestino(newDestino1)
        destinoRepository.insertDestino(newDestino2)

        val destinos = destinoRepository.getAllDestinosStream().first()
        assertEquals(2, destinos.size)
        assertTrue(destinos.any { it.id == 1 })
        assertTrue(destinos.any { it.id == 2 })
        destinos.forEach { println(it) }

    }

    @Test
    fun `insertDestino multiple insertions`() = runBlocking {
        // Test that multiple calls to insertDestino result in multiple, correctly-added destinations.
        val newDestino1 = Destino(
            id = 0,
            localidad = "Destino Prueba 1",
            distancia = 650.0
        )
        val newDestino2 = Destino(
            id = 0,
            localidad = "Destino Prueba 2",
            distancia = 120.0
        )
        val newDestino3 = Destino(
            id = 0,
            localidad = "Destino Prueba 3",
            distancia = 30.0
        )
        destinoRepository.insertDestino(newDestino1)
        destinoRepository.insertDestino(newDestino2)
        destinoRepository.insertDestino(newDestino3)
        val destinos = destinoRepository.getAllDestinosStream().first()
        assertEquals(3, destinos.size)
        assertTrue(destinos.any { it.localidad == "Destino Prueba 3" })
        destinos.forEach { println(it) }
    }

    @Test
    fun `insertDestino data integrity`() = runBlocking {
        // Test that insertDestino creates a copy of the input destination to prevent
        // unwanted data modification.
        val originalDestino = Destino(
            id = 0,
            localidad = "Original Destino",
            distancia = 100.0
        )
        destinoRepository.insertDestino(originalDestino)

        val destinos = destinoRepository.getAllDestinosStream().first()
        val insertedDestino = destinos.first()
// no se puede modificar el objeto original, solo el copiado
//        originalDestino.localidad = "Modified Destino"
//        originalDestino.distancia = 200.0

        // Check that the changes to the original object did not affect the stored destination.
        assertEquals("Original Destino", insertedDestino.localidad)
        assertEquals(100.0, insertedDestino.distancia, 0.001)

    }

    @Test
    fun `deleteDestino existing destination`() = runBlocking {
        // Test that deleteDestino successfully removes an existing destination from the repository.
        val newDestino = Destino(
            id = 0,
            localidad = "Destino a eliminar",
            distancia = 650.0
        )
        destinoRepository.insertDestino(newDestino)
        val destinos = destinoRepository.getAllDestinosStream().first()
        assertEquals(1, destinos.size)
        assertTrue(destinos.any { it.localidad == "Destino a eliminar" })
        assertTrue(destinos.any { it.id == 1 })

        destinoRepository.deleteDestino(destinos[0])

        val destinos2 = destinoRepository.getAllDestinosStream().first()
        assertEquals(0, destinos2.size)
        assertTrue(destinos2.isEmpty())

    }

    @Test
    fun `deleteDestino non existent destination`() {
        // Test that deleteDestino does nothing when attempting to delete a non-existent destination.
        val nonExistentDestino = Destino(id = 999, localidad = "Non-Existent", distancia = 0.0)
        runBlocking {
            destinoRepository.deleteDestino(nonExistentDestino)
            assertTrue(destinoRepository.getAllDestinosStream().first().isEmpty())
        }
    }

    @Test
    fun `deleteDestino multiple deletions`() {
        // Test that multiple deletes on existing data will correctly delete data from repo
        runBlocking {
            val newDestino1 = Destino(
                id = 0,
                localidad = "Destino 1 a eliminar",
                distancia = 100.0
            )
            val newDestino2 = Destino(
                id = 0,
                localidad = "Destino 2 a eliminar",
                distancia = 200.0
            )
            val newDestino3 = Destino(
                id = 0,
                localidad = "Destino 3 a eliminar",
                distancia = 300.0
            )
            destinoRepository.insertDestino(newDestino1)
            destinoRepository.insertDestino(newDestino2)
            destinoRepository.insertDestino(newDestino3)

            var destinos = destinoRepository.getAllDestinosStream().first()
            assertEquals(3, destinos.size)
            destinoRepository.deleteDestino(destinos[0])
            destinoRepository.deleteDestino(destinos[1])
            destinoRepository.deleteDestino(destinos[2])
            destinos = destinoRepository.getAllDestinosStream().first()
            assertTrue(destinos.isEmpty())
        }
    }

    @Test
    fun `updateDestino existing destination`() {
        // Test that updateDestino successfully updates an existing destination in the repository.
        runBlocking {
            val newDestino = Destino(
                id = 0,
                localidad = "Destino a actualizar",
                distancia = 100.0
            )
            destinoRepository.insertDestino(newDestino)

            val destinoToUpdate = destinoRepository.getDestinoStream(1).first()!!
            val updatedDestino = destinoToUpdate.copy(localidad = "Destino Actualizado", distancia = 200.0)
            destinoRepository.updateDestino(updatedDestino)

            val destinos = destinoRepository.getAllDestinosStream().first()
            assertEquals(1, destinos.size)
            assertEquals("Destino Actualizado", destinos[0].localidad)
            assertEquals(200.0, destinos[0].distancia, 0.001)
        }
    }

    @Test
    fun `updateDestino non existent destination`() {
        val nonExistentDestino = Destino(id = 999, localidad = "Non-Existent", distancia = 0.0)
        runBlocking {
            destinoRepository.updateDestino(nonExistentDestino)
            assertTrue(destinoRepository.getAllDestinosStream().first().isEmpty())
        }
    }

    @Test
    fun `updateDestino data integrity`()= runBlocking{
        // Test that updateDestino creates a copy of the input destination to prevent
        // unwanted data modification.

        val newDestino = Destino(
            id = 0,
            localidad = "Destino a actualizar",
            distancia = 100.0
        )
        destinoRepository.insertDestino(newDestino)

        val destinoToUpdate = destinoRepository.getDestinoStream(1).first()!!

        val updatedDestino = destinoToUpdate.copy(
            id = destinoToUpdate.id,
            localidad = "Destino Actualizado",
            distancia = 200.0
        )

        destinoRepository.updateDestino(updatedDestino)

        val originalDestino = Destino(id=updatedDestino.id, localidad = "Modified Destino", distancia = 300.0)
//        updatedDestino.localidad = "Modified Destino"
//        updatedDestino.distancia = 300.0

        val destinos = destinoRepository.getAllDestinosStream().first()
        assertEquals(1, destinos.size)
        assertEquals("Destino Actualizado", destinos[0].localidad)
        assertEquals(200.0, destinos[0].distancia, 0.001)


    }

    @Test fun `updateDestino ID immutability`() {
        runBlocking {
            val newDestino = Destino(
                id = 0,
                localidad = "Destino a actualizar",
                distancia = 100.0
            )
            destinoRepository.insertDestino(newDestino)

            val destinoToUpdate = destinoRepository.getDestinoStream(1).first()!!
            val updatedDestino = destinoToUpdate.copy(id = 1, localidad = "Destino Actualizado", distancia = 200.0)
            destinoRepository.updateDestino(updatedDestino)

            val destinos = destinoRepository.getAllDestinosStream().first()
            assertEquals(1, destinos.size)
            assertEquals(1, destinos[0].id)
            assertEquals("Destino Actualizado", destinos[0].localidad)
            assertEquals(200.0, destinos[0].distancia, 0.001)
        }
    }

    @Test
    fun `clearData empty repository`() {
        // Test that clearData correctly clears the data when no destinos are in the repo
        runBlocking {
            destinoRepository.clearData()
            assertTrue(destinoRepository.getAllDestinosStream().first().isEmpty())
        }

    }

    @Test
    fun `clearData non empty repository`() {
        // Test that clearData successfully removes all destinations from the repository.
        runBlocking {
            val newDestino1 = Destino(
                id = 0,
                localidad = "Destino Prueba 1",
                distancia = 650.0
            )
            val newDestino2 = Destino(
                id = 0,
                localidad = "Destino Prueba 2",
                distancia = 120.0
            )
            destinoRepository.insertDestino(newDestino1)
            destinoRepository.insertDestino(newDestino2)

            destinoRepository.clearData()

            assertTrue(destinoRepository.getAllDestinosStream().first().isEmpty())
        }

    }

    @Test
    fun `clearData ID reset`() {
        // Test that clearData resets the ID counter to 1.
        runBlocking {
            val newDestino1 = Destino(
                id = 0,
                localidad = "Destino Prueba 1",
                distancia = 650.0
            )
            val newDestino2 = Destino(
                id = 0,
                localidad = "Destino Prueba 2",
                distancia = 120.0
            )
            destinoRepository.insertDestino(newDestino1)
            destinoRepository.insertDestino(newDestino2)
            destinoRepository.clearData()

            destinoRepository.insertDestino(newDestino1)

            assertEquals(1, destinoRepository.getAllDestinosStream().first().first().id)
        }

    }

    @Test
    fun `multiple inserts and clears`() {
        runBlocking {
            val newDestino1 = Destino(
                id = 0,
                localidad = "Destino Prueba 1",
                distancia = 650.0
            )
            val newDestino2 = Destino(
                id = 0,
                localidad = "Destino Prueba 2",
                distancia = 120.0
            )
            destinoRepository.insertDestino(newDestino1)
            destinoRepository.insertDestino(newDestino2)
            var destinos = destinoRepository.getAllDestinosStream().first()
            assertEquals(2, destinos.size)
            destinoRepository.clearData()
            destinos = destinoRepository.getAllDestinosStream().first()
            assertTrue(destinos.isEmpty())

            destinoRepository.insertDestino(newDestino1)
            destinoRepository.insertDestino(newDestino2)
            destinos = destinoRepository.getAllDestinosStream().first()
            assertEquals(2, destinos.size)
            assertEquals(1, destinos[0].id)
            assertEquals(2, destinos[1].id)

        }

    }

}