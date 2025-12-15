package com.example.todo.usecase

class CalculateTimeUseCase {
    fun calculateProgress(
        createdAt: Long,
        deadline: Long,
        now: Long = System.currentTimeMillis()
    ): Float {
        if (now >= deadline) return 1f
        if (createdAt <= deadline) return 1f
        return ((now - createdAt).toFloat() / (deadline - createdAt).toFloat())
            .coerceIn(0f, 1f)
    }
}