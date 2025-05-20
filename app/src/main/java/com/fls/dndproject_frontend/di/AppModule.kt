package com.fls.dndproject_frontend.di

import com.fls.dndproject_frontend.data.repository.UserRestRepository
import com.fls.dndproject_frontend.domain.usecase.users.CreateAccountUseCase
import com.fls.dndproject_frontend.domain.usecase.users.ListUsersUseCase
import com.fls.dndproject_frontend.presentation.viewmodel.createAccount.CreateAccountViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { UserRestRepository(get()) }

    factory { CreateAccountUseCase(get()) }
    factory { ListUsersUseCase(get()) }

    viewModel { CreateAccountViewModel(get(),get())}
    viewModel { LoginViewModel(get()) }
}
