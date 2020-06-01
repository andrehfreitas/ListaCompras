package br.edu.ifsp.scl.listadecompras

import android.graphics.Bitmap

data class Produto(
    val id: Int,
    val nome: String,
    val qtde: Int,
    val valor: Double,
    val foto: Bitmap? = null
)
