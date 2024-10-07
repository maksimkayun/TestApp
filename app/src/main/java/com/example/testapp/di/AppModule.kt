package com.example.testapp.di

import com.example.data.network.Api
import com.example.data.network.repository.NetworkRepository
import com.example.domain.repository.ListRepository
import com.example.domain.usecases.ListUseCase
import com.example.testapp.main.vm.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://mocki.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(Api::class.java)
    }
    single<ListRepository> { NetworkRepository(get()) }
    single { ListUseCase(get()) }
    viewModel { MainViewModel(get()) }
}