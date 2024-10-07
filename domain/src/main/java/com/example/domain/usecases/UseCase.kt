package com.example.domain.usecases

interface UseCase<D, R> {
    suspend fun execute(data: D): R
}