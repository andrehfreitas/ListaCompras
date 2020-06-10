package br.edu.ifsp.scl.listadecompras

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var database: ListaComprasDatabase
    private lateinit var listaProdutos: List<Produto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Implementação do adaptador do ListView
        val produtosAdapter = ProdutoAdapter(this)
        //Definindo o adaptador da lista
        list_view_produtos.adapter = produtosAdapter

        database = ListaComprasDatabase.getInstance(this)
    }


    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        val adapter = list_view_produtos.adapter as ProdutoAdapter

        getProdutos(adapter)
        atualizaTotal(listaProdutos)
        cadastrarProduto()
        removerProduto(adapter)
        editarProduto(adapter)
    }


    fun getProdutos(adapter: ProdutoAdapter) {
        val result = database.DAO().getAllProdutos()
        listaProdutos = result
        adapter.clear()
        adapter.addAll(listaProdutos)
    }


    // Abre Activity CadastroActivity para cadastrar um novo produto
    private fun cadastrarProduto(){
        btn_adicionar.setOnClickListener {
            val i = ""
            val intent = CadastroActivity.getStartIntent(this, i, i, i, i)
            this.startActivity(intent)
        }
    }


    // Remove um produto após clique longo sobre o mesmo
    private fun removerProduto(adapter: ProdutoAdapter){
        list_view_produtos.setOnItemLongClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->
            val item = adapter.getItem(position)
            adapter.remove(item)
            database.DAO().deleteProduto(item!!)
            Toast.makeText(this, "Produto apagado com sucesso", Toast.LENGTH_LONG).show()

            // Retorno indicando que o click longo foi realizado com sucesso
            true
        }
    }


    // Ao clicar em um item da lista abre o formulário para alteração dos dados
    private fun editarProduto(adapter: ProdutoAdapter){
        list_view_produtos.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, position: Int, l: Long ->
            val item = adapter.getItem(position)
            val intent = CadastroActivity.getStartIntent(this, item!!.id.toString(), item.nome, item.qtde.toString(), item.valor.toString())
            this.startActivity(intent)
        }
    }


    // Atualiza o valor total da lista, MAS NÃO ESTÁ ATUALIZANDO NO MOMENTO DA REMOÇÃO
    @SuppressLint("SetTextI18n")
    private fun atualizaTotal(listaProdutos:List<Produto>){
        val soma = listaProdutos.sumByDouble {
            it.valor * it.qtde
        }
        txt_total.text = "TOTAL: ${formatoNumeroBrasileiro(soma)}"
    }
}
