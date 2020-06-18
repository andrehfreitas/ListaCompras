package br.edu.ifsp.scl.listadecompras.data.database

import br.edu.ifsp.scl.listadecompras.data.model.Produto

class ListaComprasRepository (val dao: ListaComprasDAO) {

    suspend fun insertProduto(produto: Produto) {
        dao.insertProduto(produto)
    }


    suspend fun updateProduto(produto: Produto){
        dao.updateProduto(produto.id, produto.nome, produto.qtde, produto.valor, produto.foto)
    }


    suspend fun deleteProduto(produto: Produto){
        dao.deleteProduto(produto)
    }


    suspend fun getListaProdutos(): List<Produto> {
        return dao.getAllProdutos()
    }
}