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
import com.fls.dndproject_frontend.data.source.remote.AbilitiesServiceClient
import com.fls.dndproject_frontend.data.source.remote.BackgroundServiceClient
import com.fls.dndproject_frontend.data.source.remote.CharacterClassServiceClient
import com.fls.dndproject_frontend.data.source.remote.CharactersServiceClient
import com.fls.dndproject_frontend.data.source.remote.CompetenciesServiceClient
import com.fls.dndproject_frontend.data.source.remote.EquipmentServiceClient
import com.fls.dndproject_frontend.data.source.remote.RaceServiceClient
import com.fls.dndproject_frontend.data.source.remote.StatsChangeServiceClient
import com.fls.dndproject_frontend.data.source.remote.StatsServiceClient
import com.fls.dndproject_frontend.data.source.remote.UserServiceClient
import com.fls.dndproject_frontend.domain.usecase.characters.CharactersInfoUseCase
import com.fls.dndproject_frontend.domain.usecase.characters.GetAllCharactersUseCase
import com.fls.dndproject_frontend.domain.usecase.characters.ListCharactersByUserUseCase
import com.fls.dndproject_frontend.domain.usecase.characters.UpdateCharacterUseCase
import com.fls.dndproject_frontend.domain.usecase.users.CreateAccountUseCase
import com.fls.dndproject_frontend.domain.usecase.users.GetUserByIdUseCase
import com.fls.dndproject_frontend.domain.usecase.users.ListUsersUseCase
import com.fls.dndproject_frontend.presentation.viewmodel.characterInfo.CharacterInfoViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.createAccount.CreateAccountViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.forum.ForumViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.login.LoginViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.myCharacters.MyCharactersViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {

    single { get<Retrofit>().create(AbilitiesServiceClient::class.java) }
    single { get<Retrofit>().create(BackgroundServiceClient::class.java) }
    single { get<Retrofit>().create(CharacterClassServiceClient::class.java) }
    single { get<Retrofit>().create(CharactersServiceClient::class.java) }
    single { get<Retrofit>().create(CompetenciesServiceClient::class.java) }
    single { get<Retrofit>().create(EquipmentServiceClient::class.java) }
    single { get<Retrofit>().create(RaceServiceClient::class.java) }
    single { get<Retrofit>().create(StatsServiceClient::class.java) }
    single { get<Retrofit>().create(StatsChangeServiceClient::class.java) }
    single { get<Retrofit>().create(UserServiceClient::class.java) }

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
    factory { ListCharactersByUserUseCase(get()) }
    factory { CharactersInfoUseCase(get()) }
    factory { UpdateCharacterUseCase(get()) }
    factory { GetUserByIdUseCase(get()) }
    factory { GetAllCharactersUseCase(get()) }

    viewModel { CreateAccountViewModel(get(),get())}
    viewModel { LoginViewModel(get()) }
    viewModel { MyCharactersViewModel(get(), get()) }
    viewModel { CharacterInfoViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { ForumViewModel(get()) }
}
