package br.edu.ifsp.scl.listadecompras.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.scl.listadecompras.R
import br.edu.ifsp.scl.listadecompras.data.database.ListaComprasDatabase
import br.edu.ifsp.scl.listadecompras.data.database.ListaComprasRepository
import br.edu.ifsp.scl.listadecompras.data.model.Produto
import br.edu.ifsp.scl.listadecompras.viewmodel.ListaComprasViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var database: ListaComprasDatabase
    private lateinit var listaComprasViewModel: ListaComprasViewModel
    private lateinit var listaComprasRepository: ListaComprasRepository
    private lateinit var listaProdutos: List<Produto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Implementação do adaptador do ListView
        val produtosAdapter = ProdutoAdapter(this)
        //Definindo o adaptador da lista
        list_view_produtos.adapter = produtosAdapter

        // Inicialização do módulo Banco de Dados
        database = ListaComprasDatabase.getInstance(this)
        listaComprasRepository = ListaComprasRepository(database.DAO())
        val factory = ListaComprasViewModel.ViewModelFactory(listaComprasRepository)
        listaComprasViewModel = ViewModelProvider(this, factory).get(ListaComprasViewModel::class.java)

        getProdutos(produtosAdapter)
        cadastrarProduto()
        removerProduto(produtosAdapter)
        editarProduto(produtosAdapter)
    }

    // Obtém a lista de produtos e exibe no ListView via adaptador
    fun getProdutos(adapter: ProdutoAdapter) {
        listaComprasViewModel.getAllProdutos()
        listaComprasViewModel.observeListaProdutos().observe(this, Observer {
            it?.let {
                adapter.clear()
                listaProdutos = it
                adapter.addAll(listaProdutos)
                atualizaTotal(listaProdutos)
            }
        })
    }


    // Busca a Intent de CadastroActivity para abri-lá e cadastrar um novo produto
    private fun cadastrarProduto(){
        btn_adicionar.setOnClickListener {
            val i = ""
            val intent = CadastroActivity.getStartIntent(this, i, i, i, i)
            this.startActivity(intent)
        }
    }


    // Busca a Intent de CadastroActivity para abri-lá e alterar os dados do produto selecionado na lista
    private fun editarProduto(adapter: ProdutoAdapter){
        list_view_produtos.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, position: Int, l: Long ->
            val item = adapter.getItem(position)
            val intent = CadastroActivity.getStartIntent(this, item!!.id.toString(), item.nome, item.qtde.toString(), item.valor.toString())
            this.startActivity(intent)
        }
    }


    // Apaga um produto da lista e do banco de dados após um clique longo sobre o mesmo
    private fun removerProduto(adapter: ProdutoAdapter){
        list_view_produtos.setOnItemLongClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->
            val item = adapter.getItem(position)
            listaComprasViewModel.deleteProduto(item!!)
            Toast.makeText(this, "Produto apagado com sucesso", Toast.LENGTH_LONG).show()

            // Retorno indicando que o click longo foi realizado com sucesso
            true
        }
    }


    // Atualiza o valor total dos produtos que estão na lista e exibe o valor no textview txt_total
    @SuppressLint("SetTextI18n")
    private fun atualizaTotal(listaAtualizada: List<Produto>){
        val soma = listaAtualizada.sumByDouble {
            it.valor * it.qtde
        }
        val f = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
        txt_total.text = "TOTAL: ${f.format(soma)}"
    }
}
