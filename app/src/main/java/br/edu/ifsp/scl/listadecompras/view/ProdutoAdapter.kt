package br.edu.ifsp.scl.listadecompras.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import br.edu.ifsp.scl.listadecompras.R
import br.edu.ifsp.scl.listadecompras.data.model.Produto
import br.edu.ifsp.scl.listadecompras.toBitmap
import java.text.NumberFormat
import java.util.*

class ProdutoAdapter(context: MainActivity): ArrayAdapter<Produto>(context, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v: View

        if (convertView != null) v = convertView
        else  v = LayoutInflater.from(context).inflate(R.layout.list_view_item, parent, false)

        val item = getItem(position)
        val nomeProduto = v.findViewById<TextView>(R.id.txt_item_produto)
        val qtdeProduto = v.findViewById<TextView>(R.id.txt_item_qtde)
        val valorProduto = v.findViewById<TextView>(R.id.txt_item_valor)
        val imgProduto = v.findViewById<ImageView>(R.id.img_item_foto)

        nomeProduto.text = item!!.nome
        qtdeProduto.text = item.qtde.toString()
        val f = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
        valorProduto.text = f.format(item.valor)

        if (item.foto != null){
            imgProduto.setImageBitmap(item.foto.toBitmap())
        }

        return v
    }
}