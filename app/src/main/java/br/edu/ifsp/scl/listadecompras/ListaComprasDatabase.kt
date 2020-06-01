package br.edu.ifsp.scl.listadecompras

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class ListaComprasDatabase(context: Context): ManagedSQLiteOpenHelper(ctx = context, name = "listaCompras.db", version = 1) {

    // Singleton da classe
    companion object{
        private var instance: ListaComprasDatabase? = null

        fun getInstance(context: Context): ListaComprasDatabase{
            if (instance == null) instance = ListaComprasDatabase(context.applicationContext)
            return instance!!
        }
    }


    override fun onCreate(db: SQLiteDatabase?) {
        // Criação da tabela de Produtos
        db?.createTable("produtos", true,
              "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                       "nome" to TEXT,
                       "quantidade" to INTEGER,
                       "valor" to REAL,
                       "foto" to BLOB)

    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

}


//Acesso à propriedade pelo contexto
val Context.database: ListaComprasDatabase
    get() = ListaComprasDatabase.getInstance(applicationContext)