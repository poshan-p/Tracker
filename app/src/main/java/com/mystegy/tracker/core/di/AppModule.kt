package com.mystegy.tracker.core.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mystegy.tracker.feature_tracker.data.local.TrackerDatabase
import com.mystegy.tracker.feature_tracker.data.repository.TrackerRepositoryImpl
import com.mystegy.tracker.feature_tracker.domain.repository.TrackerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): TrackerDatabase =
        Room.databaseBuilder(app, TrackerDatabase::class.java, "tracker_database")
            .fallbackToDestructiveMigration()
            .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()

    @Provides
    fun providesTrackerRepository(
        db: TrackerDatabase
    ): TrackerRepository = TrackerRepositoryImpl(
        db = db
    )
}