package com.action.round.ui.screens.training

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.action.round.data.Exercise
import com.action.round.data.LocalExercisesRepository
import com.action.round.data.Repository
import com.action.round.data.Training

class TrainingViewModel(
    private val repository: Repository,
    private val localExercisesRepository: LocalExercisesRepository,
) : ViewModel() {

    val exercisesLiveData: LiveData<List<Exercise>> = localExercisesRepository.exercisesLiveData
    val exercisesSize: Int get() = localExercisesRepository.exercises.size

    fun setTraining(training: Training?) {
        localExercisesRepository.setExercises(training?.exercises.orEmpty())
    }

    fun deleteTraining(training: Training?) {
        training?.let { repository.delete(it) {} }
    }

    fun saveTraining(
        title: String,
    ) {
        repository.save(
            title = title,
            exercises = filledExercises(),
        )
    }

    fun updateTraining(
        id: Int,
        title: String,
    ) {
        repository.update(
            id = id,
            title = title,
            exercises = filledExercises(),
        )
    }

    fun clearExercises() {
        localExercisesRepository.clear()
    }

    fun addNewExercise() {
        localExercisesRepository.add()
    }

    fun moveExercise(from: Int, to: Int) {
        localExercisesRepository.move(from, to)
    }

    fun deleteExercise(position: Int) {
        localExercisesRepository.deleteByPosition(position)
    }

    fun updateExerciseById(id: Int, newDescription: String) {
        localExercisesRepository.updateExerciseById(id, newDescription)
    }

    private fun filledExercises(): List<String> {
        return localExercisesRepository.exercises.mapNotNull { ex ->
            ex.takeIf { it.description.isNotEmpty() }?.description
        }
    }
}