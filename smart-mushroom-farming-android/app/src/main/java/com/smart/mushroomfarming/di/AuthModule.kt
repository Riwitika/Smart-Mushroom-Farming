package com.smart.mushroomfarming.di

import android.content.Context
import com.google.firebase.FirebaseApp
import com.smart.mushroomfarming.data.repository.DemoAuthRepository
import com.smart.mushroomfarming.data.repository.FirebaseAuthRepository
import com.smart.mushroomfarming.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        @ApplicationContext context: Context,
        firebaseAuthRepository: Provider<FirebaseAuthRepository>,
        demoAuthRepository: Provider<DemoAuthRepository>
    ): AuthRepository {
        return if (isFirebaseAvailable(context)) {
            firebaseAuthRepository.get()
        } else {
            demoAuthRepository.get()
        }
    }

    private fun isFirebaseAvailable(context: Context): Boolean {
        return try {
            val resourceId = context.resources.getIdentifier("google_app_id", "string", context.packageName)
            if (resourceId != 0) {
                FirebaseApp.initializeApp(context)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}
