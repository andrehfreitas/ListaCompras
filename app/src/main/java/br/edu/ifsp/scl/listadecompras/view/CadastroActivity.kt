package br.edu.ifsp.scl.listadecompras.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.scl.listadecompras.viewmodel.ListaComprasViewModel
import br.edu.ifsp.scl.listadecompras.R
import br.edu.ifsp.scl.listadecompras.data.database.ListaComprasDatabase
import br.edu.ifsp.scl.listadecompras.data.database.ListaComprasRepository
import br.edu.ifsp.scl.listadecompras.data.model.Produto
import br.edu.ifsp.scl.listadecompras.toByteArray
import kotlinx.android.synthetic.main.activity_cadastro.*

class CadastroActivity : AppCompatActivity() {

    val COD_IMAGE = 101
    var imageBitmap: Bitmap? = null
    lateinit var database: ListaComprasDatabase
    private lateinit var listaComprasViewModel: ListaComprasViewModel
    private lateinit var listaComprasRepository: ListaComprasRepository


    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        // Inicialização do módulo Banco de Dados
        database = ListaComprasDatabase.getInstance(this)
        listaComprasRepository = ListaComprasRepository(database.DAO())
        val factory = ListaComprasViewModel.ViewModelFactory(listaComprasRepository)
        listaComprasViewModel = ViewModelProvider(this, factory).get(ListaComprasViewModel::class.java)


        txt_nome.text.append(intent.getStringExtra(EXTRA_NOME))
        txt_qtd.text.append(intent.getStringExtra(EXTRA_QUANTIDADE))
        txt_valor.text.append(intent.getStringExtra(EXTRA_VALOR))
        val idProdutoAtualizado = intent.getStringExtra(EXTRA_ID)

        if (idProdutoAtualizado != ""){
            btn_inserir.isEnabled = false
            // //Chamada da função do botão Salvar Alterações do Produto
            atualizarProduto(this, idProdutoAtualizado.toInt())
        } else {
            btn_atualizar.isEnabled = false
        }

        inserirProduto(this)
        abrirGaleria()
    }

    // Cadastra um produto no Banco de Dados
    private fun inserirProduto(context: Context){
        btn_inserir.setOnClickListener {
            // Pegando os valores digitados pelo usuário
            val nome = txt_nome.text.toString()
            val qtde = txt_qtd.text.toString()
            val valor = txt_valor.text.toString()
            val foto = imageBitmap

            // Verificando a digitação dos dados pelo usuário
            if (nome.isNotEmpty() && qtde.isNotEmpty() && valor.isNotEmpty()){
                val produto = Produto(
                    nome = nome,
                    qtde = qtde.toInt(),
                    valor = valor.toDouble(),
                    foto = foto?.toByteArray()
                )
                listaComprasViewModel.insertProduto(produto)
                Toast.makeText(context, "Produto inserido com sucesso: ", Toast.LENGTH_LONG).show()
                limpaCadastroActivity()
            } else {
                txt_nome.error = if (nome.isEmpty()) "Insira o nome de um produto"
                else null
                txt_qtd.error = if (qtde.isEmpty()) "Insira a quantidade do produto"
                else null
                txt_valor.error = if (valor.isEmpty()) "Insira o valor do produto"
                else null
            }
        }
    }


    // Altera os dados do Produto
    private fun atualizarProduto(context: Context, id: Int){
        btn_atualizar.setOnClickListener {
            // Pegando os valores digitados pelo usuário
            val nome = txt_nome.text.toString()
            val qtde = txt_qtd.text.toString()
            val valor = txt_valor.text.toString()
            val foto = imageBitmap

            // Verificando a digitação dos dados pelo usuário
            if (nome.isNotEmpty() && qtde.isNotEmpty() && valor.isNotEmpty()){
                val produto = Produto(
                    id,
                    nome = nome,
                    qtde = qtde.toInt(),
                    valor = valor.toDouble(),
                    foto = foto?.toByteArray()
                )
                listaComprasViewModel.updateProduto(produto)
                Toast.makeText(context, "Produto atualizado com sucesso", Toast.LENGTH_LONG).show()
                limpaCadastroActivity()
            } else {
                txt_nome.error = if (nome.isEmpty()) "Insira o nome de um produto"
                else null
                txt_qtd.error = if (qtde.isEmpty()) "Insira a quantidade do produto"
                else null
                txt_valor.error = if (valor.isEmpty()) "Insira o valor do produto"
                else null
            }
        }
    }


    // Limpa os campos da Activity de Cadastro
    private fun limpaCadastroActivity(){
        txt_nome.text.clear()
        txt_qtd.text.clear()
        txt_valor.text.clear()
        img_foto_produto.setImageResource(android.R.drawable.ic_menu_camera)
    }


    // Abre a galeria do dispositivo para escolha e inserção de uma imagem
    private fun abrirGaleria(){
        img_foto_produto.setOnClickListener {
            // Definindo a ação de conteúdo
            val intent = Intent(Intent.ACTION_GET_CONTENT)

            // Definindo filtros para as imagens
            intent.type = "image/*"

            // Inicializando a activity com o resultado
            startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), COD_IMAGE)
        }
    }


    // Retorno da imagem selecionada na galeria
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == COD_IMAGE && resultCode == Activity.RESULT_OK){
            if (data != null) {
                // Lendo a URI com a imagem
                val inputStream = contentResolver.openInputStream(data.data!!)

                // Transformado o resultado em Bitmap
                imageBitmap = BitmapFactory.decodeStream(inputStream)

                // Exibir a imagem no aplicativo
                img_foto_produto.setImageBitmap(imageBitmap)
            }
        }
    }


    // Retorno de Intent para a activity origem (pode ser qualquer activity do projeto) que chamar a CadastroActivity
    companion object {
        private const val EXTRA_ID = "EXTRA_ID"
        private const val EXTRA_NOME = "EXTRA_NOME"
        private const val EXTRA_QUANTIDADE = "EXTRA_QUANTIDADE"
        private const val EXTRA_VALOR = "EXTRA_VALOR"

        fun getStartIntent(context: Context, id: String, nome: String, quantidade: String, valor: String): Intent{
            return Intent(context, CadastroActivity::class.java).apply {
                putExtra(EXTRA_ID, id)
                putExtra(EXTRA_NOME, nome)
                putExtra(EXTRA_QUANTIDADE, quantidade)
                putExtra(EXTRA_VALOR, valor)
            }
        }
    }
}
