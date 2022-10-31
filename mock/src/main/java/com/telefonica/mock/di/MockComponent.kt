package com.telefonica.mock.di

import com.telefonica.mock.MockHelper
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [MockApiModule::class]
)
interface MockComponent {

    fun inject(mockHelper: MockHelper)
}
