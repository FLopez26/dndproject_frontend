package com.fls.dndproject_frontend.di

import com.fls.dndproject_frontend.data.repository.UserRestRepository
import com.fls.dndproject_frontend.domain.usecase.createAccount.CreateAccountUseCase
import com.fls.dndproject_frontend.presentation.viewmodel.createAccount.CreateAccountViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { UserRestRepository(get()) }

    factory { CreateAccountUseCase(get()) }

    viewModel { CreateAccountViewModel(get())}
}
