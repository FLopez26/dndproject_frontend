package com.fls.dndproject_frontend.di

import com.fls.dndproject_frontend.data.repository.AbilitiesRestRepository
import com.fls.dndproject_frontend.data.repository.BackgroundRestRepository
import com.fls.dndproject_frontend.data.repository.CharacterClassRestRepository
import com.fls.dndproject_frontend.data.repository.CharactersRestRepository
import com.fls.dndproject_frontend.data.repository.ChatRepositoryImpl
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
import com.fls.dndproject_frontend.data.source.remote.OllamaApiClient
import com.fls.dndproject_frontend.data.source.remote.RaceServiceClient
import com.fls.dndproject_frontend.data.source.remote.StatsChangeServiceClient
import com.fls.dndproject_frontend.data.source.remote.StatsServiceClient
import com.fls.dndproject_frontend.data.source.remote.UserServiceClient
import com.fls.dndproject_frontend.domain.repository.ChatRepository
import com.fls.dndproject_frontend.domain.usecase.background.GetAllBackgroundsUseCase
import com.fls.dndproject_frontend.domain.usecase.characterClass.GetAllCharacterClassesUseCase
import com.fls.dndproject_frontend.domain.usecase.characters.CharactersInfoUseCase
import com.fls.dndproject_frontend.domain.usecase.characters.CreateCharacterUseCase
import com.fls.dndproject_frontend.domain.usecase.characters.GetAllCharactersUseCase
import com.fls.dndproject_frontend.domain.usecase.characters.ListCharactersByUserUseCase
import com.fls.dndproject_frontend.domain.usecase.characters.UpdateCharacterUseCase
import com.fls.dndproject_frontend.domain.usecase.ollama.SendMessageUseCase
import com.fls.dndproject_frontend.domain.usecase.race.GetAllRacesUseCase
import com.fls.dndproject_frontend.domain.usecase.users.CreateAccountUseCase
import com.fls.dndproject_frontend.domain.usecase.users.GetUserByIdUseCase
import com.fls.dndproject_frontend.domain.usecase.users.ListUsersUseCase
import com.fls.dndproject_frontend.presentation.viewmodel.characterInfo.CharacterInfoViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.chat.ChatViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.createAccount.CreateAccountViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.forum.ForumViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.login.LoginViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.myCharacters.MyCharactersViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.profile.ProfileViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.wizard.Wizard1ViewModel
import com.fls.dndproject_frontend.presentation.viewmodel.wizard.Wizard2ViewModel
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

    single { OllamaApiClient() } // Instancia del cliente HTTP para Ollama
    single<ChatRepository> { ChatRepositoryImpl(get()) } // Implementación del repositorio de chat
    factory { SendMessageUseCase(get()) } // Caso de uso para enviar mensajes

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
    factory { GetAllBackgroundsUseCase(get()) }
    factory { GetAllCharacterClassesUseCase(get()) }
    factory { GetAllRacesUseCase(get()) }
    factory { CreateCharacterUseCase(get()) }

    viewModel { CreateAccountViewModel(get(),get())}
    viewModel { LoginViewModel(get()) }
    viewModel { MyCharactersViewModel(get(), get()) }
    viewModel { CharacterInfoViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { ForumViewModel(get()) }
    viewModel { Wizard1ViewModel() }
    viewModel { Wizard2ViewModel(get(), get(), get(), get(), get()) }
    viewModel { ChatViewModel(get()) }
}
