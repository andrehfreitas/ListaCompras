package br.edu.ifsp.scl.listadecompras.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.scl.listadecompras.data.database.ListaComprasRepository
import br.edu.ifsp.scl.listadecompras.data.model.Produto
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Callable

class ListaComprasViewModel(private val repository: ListaComprasRepository): ViewModel() {

    private val listaProdutos: MutableLiveData<List<Produto>> = MutableLiveData()


    fun observeListaProdutos(): LiveData<List<Produto>> = listaProdutos


    fun insertProduto(produto: Produto){
        GlobalScope.launch {
            repository.insertProduto(produto)
        }
    }


    fun updateProduto(produto: Produto){
        GlobalScope.launch {
            repository.updateProduto(produto)
        }
    }


    fun deleteProduto(produto: Produto){
        GlobalScope.launch {
            repository.deleteProduto(produto)
            getAllProdutos()
        }
    }


    fun getAllProdutos() {
        GlobalScope.launch {
            listaProdutos.postValue(repository.getListaProdutos())
        }
    }


    class ViewModelFactory(val repository: ListaComprasRepository): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(ListaComprasRepository::class.java).newInstance(repository)
        }
    }

}

