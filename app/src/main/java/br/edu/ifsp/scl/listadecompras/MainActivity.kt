package br.edu.ifsp.scl.listadecompras

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select

class MainActivity : AppCompatActivity() {

    private lateinit var listaProdutos: List<Produto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Implementação do adaptador do ListView
        val produtosAdapter = ProdutoAdapter(this)

        //Definindo o adaptador da lista
        list_view_produtos.adapter = produtosAdapter
    }


    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        // Referência ao adapter criado em onCreate
        val adapter = list_view_produtos.adapter as ProdutoAdapter

        database.use {
            // Efetuando a consulta dos registros da tabela produtos no banco de dados
            select("produtos").exec {
                // Criando o parser que montará o objeto Produto
                val parser = rowParser {
                    //Colunas do banco de dados
                    id: Int, nome: String, quantidade: Int, valor: Double, foto: ByteArray? ->
                        // Montagem do objeto Produto com as colunas do banco de dados
                        Produto(id, nome, quantidade, valor, foto?.toBitmap())
                }

                // Criando a lista de produtos com os dados do banco
                listaProdutos = parseList(parser)

                // Limpa adapter para não duplicar itens na lista
                adapter.clear()

                // Adiciona itens cadastrados na lista
                adapter.addAll(listaProdutos)
            }
        }

        // Atualiza o valor total da lista, MAS NÃO ESTÁ ATUALIZANDO NO MOMENTO DA REMOÇÃO
        atualizaTotal(listaProdutos)

        // Função chamada quando botão Adicionar Itens é clicado
        inserirProduto()

        // Função ouvinte do click longo em um item da lista para remoção
        removerProduto(this, adapter)

        // Função ouvinte do click em um item da lista para edição
        editarProduto(adapter)
    }

    // Abre Activity CadastroActivity para cadastrar um novo produto
    private fun inserirProduto(){
        btn_adicionar.setOnClickListener {
            val i = ""
            val intent = CadastroActivity.getStartIntent(this, i, i, i, i)
            this.startActivity(intent)
        }
    }

    // Remove um produto após clique longo sobre o mesmo
    private fun removerProduto(context: Context, adapter: ProdutoAdapter){
        list_view_produtos.setOnItemLongClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->
            val item = adapter.getItem(position)
            adapter.remove(item)

            database.use {
                delete("produtos", "id = {id}", "id" to item!!.id)
            }

            Toast.makeText(context, "Produto apagado com sucesso", Toast.LENGTH_LONG).show()

            // Retorno indicando que o click longo foi realizado com sucesso
            true
        }
    }


    private fun editarProduto(adapter: ProdutoAdapter){
        list_view_produtos.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, position: Int, l: Long ->
            val item = adapter.getItem(position)
            val intent = CadastroActivity.getStartIntent(this, item!!.id.toString(), item.nome, item.qtde.toString(), item.valor.toString())
            this.startActivity(intent)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun atualizaTotal(listaProdutos:List<Produto>){
        val soma = listaProdutos.sumByDouble {
            it.valor * it.qtde
        }
        txt_total.text = "TOTAL: ${formatoNumeroBrasileiro(soma)}"
    }
}
