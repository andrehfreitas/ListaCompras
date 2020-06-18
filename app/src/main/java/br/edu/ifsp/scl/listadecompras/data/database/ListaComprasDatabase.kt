package br.edu.ifsp.scl.listadecompras.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.edu.ifsp.scl.listadecompras.data.model.Produto

@Database(entities = [Produto::class], version = 1)
abstract class ListaComprasDatabase: RoomDatabase() {

    abstract fun DAO(): ListaComprasDAO

    // Singleton da classe
    companion object{
        private var INSTANCE: ListaComprasDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ListaComprasDatabase {
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    ListaComprasDatabase::class.java,
                    "listaCompras2.db").build()
            }
            return INSTANCE as ListaComprasDatabase
        }
    }

}