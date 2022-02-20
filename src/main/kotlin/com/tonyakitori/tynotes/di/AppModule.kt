package com.tonyakitori.tynotes.di

import com.tonyakitori.tynotes.data.repository.UsersRepository
import com.tonyakitori.tynotes.data.repository.impl.UsersRepositoryImpl
import com.tonyakitori.tynotes.data.sources.local.UsersLocalResource
import com.tonyakitori.tynotes.framework.data.local.UsersLocalResourceImpl
import com.tonyakitori.tynotes.framework.database.DatabaseManager
import com.tonyakitori.tynotes.services.UserService
import com.tonyakitori.tynotes.services.impl.UserServiceImpl
import org.koin.dsl.module

val appModule = module {

    single { DatabaseManager() }

    //Users dependencies
    single<UsersLocalResource> { UsersLocalResourceImpl(get()) }
    single<UsersRepository> { UsersRepositoryImpl(get()) }
    single<UserService> { UserServiceImpl(get()) }

}