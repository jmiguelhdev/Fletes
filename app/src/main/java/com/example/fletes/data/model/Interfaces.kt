

import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.CamionesRegistro
import com.example.fletes.data.room.Destino
import kotlinx.coroutines.flow.Flow

//Interfaces
interface CamionRepository {
    suspend fun insertCamion(camion: Camion)
    suspend fun deleteCamion(camion: Camion)
    suspend fun deleteAlllCamiones()
    suspend fun updateCamion(camion: Camion)
    suspend fun getCamionById(id: Int): Camion?
    fun getAllCamiones(): Flow<List<Camion>>
}

interface DestinoRepository {
    suspend fun insertDestino(destino: Destino)
    suspend fun deleteDestino(destino: Destino)
    suspend fun updateDestino(destino: Destino)
    fun getDestinoStream(id: Int): Flow<Destino?>
    fun getAllDestinosStream(): Flow<List<Destino>>
    fun searchComisionista(query: String): Flow<List<String>>
    fun searchLocalidad(query: String): Flow<List<String>>
}

interface CamionesRegistroRepository {
    suspend fun insertCamionesRegistro(camionesRegistro: CamionesRegistro)
    suspend fun deleteCamionesRegistro(camionesRegistro: CamionesRegistro)
    suspend fun updateCamionesRegistro(camionesRegistro: CamionesRegistro)
    fun getAllCamionesRegistrosStream(): Flow<List<CamionesRegistro>>
    fun getCamionesRegistroStream(id: Int): Flow<CamionesRegistro?>
    fun getLastTripByCamionId(camionId: Int): Flow<CamionesRegistro?>
    fun getCamionesRegistroByCamionIdStream(camionId: Int): Flow<List<CamionesRegistro>>
}