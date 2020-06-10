package br.edu.ifsp.scl.listadecompras

import androidx.room.*

@Dao
interface ListaComprasDAO {

    @Query("SELECT * FROM produtos")
    fun getAllProdutos(): List<Produto>

    @Insert
    fun insertProduto(produto: Produto)

    @Query("UPDATE produtos SET nome=:nome, qtde=:qtde, valor=:valor, foto=:foto WHERE id=:id")
    fun updateProduto(id: Int, nome: String, qtde: Int, valor: Double, foto: ByteArray?)

    @Delete
    fun deleteProduto(produto: Produto)
}