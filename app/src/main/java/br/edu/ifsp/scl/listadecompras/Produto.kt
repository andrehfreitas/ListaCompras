package br.edu.ifsp.scl.listadecompras

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "produtos")
data class Produto(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val qtde: Int,
    val valor: Double,
    val foto: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Produto

        if (foto != null) {
            if (other.foto == null) return false
            if (!foto.contentEquals(other.foto)) return false
        } else if (other.foto != null) return false

        return true
    }

    override fun hashCode(): Int {
        return foto?.contentHashCode() ?: 0
    }
}



