package br.edu.ifsp.scl.listadecompras

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cadastro.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.update
import org.jetbrains.anko.toast

class CadastroActivity : AppCompatActivity() {

    val COD_IMAGE = 101
    var imageBitmap: Bitmap? = null

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        txt_produto.text.append(intent.getStringExtra(EXTRA_NOME))
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

        //Chamada da função do botão Inserir Produto
        inserirProduto(this)

        // Chamada da função para escolher imagem da galeria para foto do produto
        abrirGaleria()
    }


    private fun inserirProduto(context: Context){
        btn_inserir.setOnClickListener {
            // Pegando os valores digitados pelo usuário
            val produto = txt_produto.text.toString()
            val qtde = txt_qtd.text.toString()
            val valor = txt_valor.text.toString()

            // Verificando a digitação dos dados pelo usuário
            if (produto.isNotEmpty() && qtde.isNotEmpty() && valor.isNotEmpty()){

                // Inserção dos dados no banco de dados usando o Anko SQLite
                database.use {
                    val idProduto = insert("produtos",
                        "nome" to produto,
                        "quantidade" to qtde.toInt(),
                        "valor" to valor.toDouble(),
                        "foto" to (imageBitmap?.toByteArray())
                    )

                    if (idProduto != -1L) {
                        Toast.makeText(context, "Produto inserido com sucesso: ", Toast.LENGTH_LONG).show()
                        txt_produto.text.clear()
                        txt_qtd.text.clear()
                        txt_valor.text.clear()
                        img_foto_produto.setImageResource(android.R.drawable.ic_menu_camera)
                    }else{
                        Toast.makeText(context, "Erro ao salvar dados do produto", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                txt_produto.error = if (produto.isEmpty()) "Insira o nome de um produto"
                else null

                txt_qtd.error = if (qtde.isEmpty()) "Insira a quantidade do produto"
                else null

                txt_valor.error = if (valor.isEmpty()) "Insira o valor do produto"
                else null
            }
        }
    }


    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun atualizarProduto(context: Context, id: Int){
        btn_atualizar.setOnClickListener {
            // Pegando os valores digitados pelo usuário
            val produto = txt_produto.text.toString()
            val qtde = txt_qtd.text.toString()
            val valor = txt_valor.text.toString()

            // Verificando a digitação dos dados pelo usuário
            if (produto.isNotEmpty() && qtde.isNotEmpty() && valor.isNotEmpty()){

                // Atualizando os dados no banco de dados usando o Anko SQLite
                database.use {
                    update("produtos",
                        "nome" to produto,
                        "quantidade" to qtde.toInt(),
                        "valor" to valor.toDouble(),
                        "foto" to (imageBitmap?.toByteArray())
                    ).whereArgs("id = {id}", "id" to id).exec()

                        Toast.makeText(context, "Produto atualizado com sucesso", Toast.LENGTH_LONG).show()
                        txt_produto.text.clear()
                        txt_qtd.text.clear()
                        txt_valor.text.clear()
                        img_foto_produto.setImageResource(android.R.drawable.ic_menu_camera)
                }
            } else {
                txt_produto.error = if (produto.isEmpty()) "Insira o nome de um produto"
                else null

                txt_qtd.error = if (qtde.isEmpty()) "Insira a quantidade do produto"
                else null

                txt_valor.error = if (valor.isEmpty()) "Insira o valor do produto"
                else null
            }
        }
    }


    // Abre a galeria do dispositivo para inserção de uma imagem
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
