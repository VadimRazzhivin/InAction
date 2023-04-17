package com.action.round.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.action.round.R
import com.action.round.data.Exercise
import com.action.round.ui.adapter.item.touch.ItemTouchHelperAdapter
import com.action.round.ui.adapter.item.touch.ItemTouchHelperViewHolder

class ExercisesRecycleAdapter(
    private val onSwipe: (position: Int) -> Unit,
    private val onMove: (from: Int, to: Int) -> Unit,
    private val onExerciseChange: (id: Int, newText: String) -> Unit,
) : ListAdapter<Exercise, ExercisesRecycleAdapter.RoundViewHolder>(ExercisesDiffUtilCallback()),
    ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoundViewHolder {
        return RoundViewHolder(
            view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.round_and_exercises, parent, false),
            onExerciseChange = onExerciseChange,
        )
    }

    override fun onBindViewHolder(holder: RoundViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onViewRecycled(holder: RoundViewHolder) {
        super.onViewRecycled(holder)
        holder.onUnbind()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        onMove(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        onSwipe(position)
    }

    override fun onListUpdate() {
        submitList(currentList)
    }

    class RoundViewHolder(
        private val view: View,
        private val onExerciseChange: (id: Int, newText: String) -> Unit,
    ) : RecyclerView.ViewHolder(view), ItemTouchHelperViewHolder {

        override fun onItemSelected() {
            view.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            view.setBackgroundColor(Color.TRANSPARENT)
        }

        fun onBind(exercise: Exercise) {
            view.apply {
                val text = "Round [ ${adapterPosition + 1} ]" // todo: fix adapter position
                findViewById<TextView>(R.id.tvRoundOfTraining).text = text
                findViewById<EditText>(R.id.etExerciseOfTraining).apply {
                    setText(exercise.description)
                    setSelection(exercise.description.length)
                    doAfterTextChanged {
                        onExerciseChange(exercise.id, it?.toString().orEmpty())
                    }
                }
            }
        }

        fun onUnbind() {
            view.setOnClickListener(null)
        }
    }

    class ExercisesDiffUtilCallback : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.description == newItem.description
        }
    }
}
