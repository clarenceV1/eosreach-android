package com.memtrip.eosreach.app.welcome.importkey

import androidx.lifecycle.ViewModel
import com.memtrip.eosreach.app.ViewModelKey

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WelcomeImportKeyActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeImportKeyViewModel::class)
    internal abstract fun contributesWelcomeImportKeyViewModel(viewModel: WelcomeImportKeyViewModel): ViewModel
}