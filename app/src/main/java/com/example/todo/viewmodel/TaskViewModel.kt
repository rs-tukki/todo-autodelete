package com.example.todo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.AppDatabase
import com.example.todo.domain.model.Task
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.get(application).taskDao()

    init {
        val epochMillis = Instant.now()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        viewModelScope.launch {
            dao.deleteExpired(epochMillis)
        }
    }

    val tasks: StateFlow<List<Task>> =
        dao.getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addTask(content: String, deadline: Long) {
        viewModelScope.launch {
            dao.insert(Task(content = content, deadline = deadline))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            dao.delete(task)
        }
    }

    suspend fun deleteExpired() {
        dao.deleteExpired(System.currentTimeMillis())
    }
}
