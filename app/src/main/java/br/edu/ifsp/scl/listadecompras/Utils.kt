package br.edu.ifsp.scl.listadecompras

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.text.NumberFormat
import java.util.*


// Função de extensão para converter um Bitmap em um ByteArray
fun Bitmap.toByteArray(): ByteArray{
    val stream = ByteArrayOutputStream()

    //Comprimindo a imagem
    this.compress(Bitmap.CompressFormat.PNG, 0, stream)

    // Transformando em um array de caracteres
    return stream.toByteArray()
}


// Função de extensão para decodificar um ByteArray para Bitmap
fun ByteArray.toBitmap(): Bitmap{
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}