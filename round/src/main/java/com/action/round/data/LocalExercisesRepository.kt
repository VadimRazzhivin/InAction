package com.action.round.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.action.round.utills.findAndUpdate
import com.action.round.utills.swap
import java.util.concurrent.ExecutorService

class LocalExercisesRepository(
    private val es: ExecutorService,
) {

    private val _exercises = mutableListOf<Exercise>()
    val exercises: List<Exercise> = _exercises

    private val _exercisesLiveData = MutableLiveData<List<Exercise>>(_exercises)
    val exercisesLiveData: LiveData<List<Exercise>> = _exercisesLiveData

    fun updateExerciseById(id: Int, newDescription: String) {
        modifyExercises(notifyUpdates = false) { exercises ->
            exercises.findAndUpdate(
                predicate = { it.id == id },
                modify = { it.copy(description = newDescription) },
            )
        }
    }

    fun setExercises(newExercises: List<Exercise>) {
        modifyExercises { exercises ->
            exercises.clear()
            exercises.addAll(newExercises)
        }
    }

    fun move(from: Int, to: Int) {
        modifyExercises(notifyUpdates = false) { exercises ->
            exercises.swap(from, to)
        }
    }

    fun deleteByPosition(position: Int) {
        modifyExercises(notifyUpdates = false) { exercises ->
            exercises.removeAt(position)
        }
    }

    fun add() {
        modifyExercises { exercises ->
            exercises.add(Exercise())
        }
    }

    fun clear() {
        modifyExercises { exercises ->
            exercises.clear()
        }
    }

    private fun modifyExercises(
        notifyUpdates: Boolean = true,
        modify: (MutableList<Exercise>) -> Unit,
    ) {
        es.execute {
            modify(_exercises)
            if (notifyUpdates) {
                _exercisesLiveData.postValue(_exercises.toList())
            }
        }
    }
}
