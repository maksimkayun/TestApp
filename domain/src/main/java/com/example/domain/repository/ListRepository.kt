package com.example.domain.repository

import com.example.domain.entity.ListElement

interface ListRepository {
    suspend fun getList(): List<ListElement>
}