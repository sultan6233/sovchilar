package com.sovchilar.made.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import sovchilar.arch.featureremoteapi.RemoteAdvertisement
import sovchilar.uz.domain.IAdvertisement

@Module
@InstallIn(ViewModelComponent::class)
abstract class AdvertisementModule {
    @Binds
    abstract fun bindAdvertisementRepository(
        remoteAdvertisement: RemoteAdvertisement
    ): IAdvertisement
}