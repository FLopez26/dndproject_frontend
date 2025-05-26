package com.fls.dndproject_frontend.di

import com.fls.dndproject_frontend.data.repository.AbilitiesRestRepository
import com.fls.dndproject_frontend.data.repository.BackgroundRestRepository
import com.fls.dndproject_frontend.data.repository.CharacterClassRestRepository
import com.fls.dndproject_frontend.data.repository.CharactersRestRepository
import com.fls.dndproject_frontend.data.repository.CompetenciesRestRepository
import com.fls.dndproject_frontend.data.repository.EquipmentRestRepository
import com.fls.dndproject_frontend.data.repository.RaceRestRepository
import com.fls.dndproject_frontend.data.repository.StatsChangeRestRepository
import com.fls.dndproject_frontend.data.repository.StatsRestRepository
import com.fls.dndproject_frontend.data.repository.UserRestRepository
import com.fls.dndproject_frontend.domain.usecase.users.CreateAccountUseCase
import com.fls.dndproject_frontend.domain.usecase.users.ListUsersUseCase
import com.fls.dndproject_frontend.presentation.viewmodel.createAccount.CreateAccountViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { AbilitiesRestRepository(get()) }
    single { BackgroundRestRepository(get()) }
    single { CharacterClassRestRepository(get()) }
    single { CharactersRestRepository(get()) }
    single { CompetenciesRestRepository(get()) }
    single { EquipmentRestRepository(get()) }
    single { RaceRestRepository(get()) }
    single { StatsRestRepository(get()) }
    single { StatsChangeRestRepository(get()) }
    single { UserRestRepository(get()) }

    factory { CreateAccountUseCase(get()) }
    factory { ListUsersUseCase(get()) }

    viewModel { CreateAccountViewModel(get(),get())}
    viewModel { LoginViewModel(get()) }
}
