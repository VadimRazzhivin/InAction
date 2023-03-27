package com.action.round

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.action.round.data.Repository
import com.action.round.data.TrainingConverter
import com.action.round.data.db.InActionDatabase
import com.action.round.data.db.TrainingDao
import com.action.round.ui.vm.MainViewModel
import java.util.concurrent.Executors

class Dependencies(context: Context) {

    private companion object {
        const val DATABASE_NAME = "database.db"
    }

    val mainViewModelFactory: ViewModelProvider.Factory
        get() = viewModelFactory {
            initializer {
                MainViewModel(repository)
            }
        }

    private val db: InActionDatabase = Room.databaseBuilder(
        context,
        InActionDatabase::class.java,
        DATABASE_NAME,
    ).build()

    private val repository: Repository
        get() = Repository(
            trainingDao = dao,
            es = Executors.newSingleThreadExecutor(),
            converter = converter,
        )
    private val converter: TrainingConverter
        get() = TrainingConverter()

    private val dao: TrainingDao
        get() = db.trainingDao()
}