package br.edu.ifsp.scl.listadecompras.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.edu.ifsp.scl.listadecompras.data.model.Produto

@Dao
interface ListaComprasDAO {

    @Insert
    suspend fun insertProduto(produto: Produto)

    @Delete
    suspend fun deleteProduto(produto: Produto)

    @Query("UPDATE produtos SET nome=:nome, qtde=:qtde, valor=:valor, foto=:foto WHERE id=:id")
    suspend fun updateProduto(id: Int, nome: String, qtde: Int, valor: Double, foto: ByteArray?)

    @Query("SELECT * FROM produtos")
    suspend fun getAllProdutos(): List<Produto>
}