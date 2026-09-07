package com.shohan.khatiyan.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

data class UserPreferences(
    val isOnboardingCompleted: Boolean = false,
    val userName: String = "ব্যবহারকারী",
    val currencySymbol: String = "৳",
    val isNotificationsEnabled: Boolean = true,
    val isAppLockEnabled: Boolean = false,
    val pinHash: String = "",
    val lastBackupTime: Long = 0L
)

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
        val USER_NAME = stringPreferencesKey("user_name")
        val CURRENCY_SYMBOL = stringPreferencesKey("currency_symbol")
        val IS_NOTIFICATIONS_ENABLED = booleanPreferencesKey("is_notifications_enabled")
        val IS_APP_LOCK_ENABLED = booleanPreferencesKey("is_app_lock_enabled")
        val PIN_HASH = stringPreferencesKey("pin_hash")
        val LAST_BACKUP_TIME = longPreferencesKey("last_backup_time")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .map { preferences ->
            UserPreferences(
                isOnboardingCompleted = preferences[PreferencesKeys.IS_ONBOARDING_COMPLETED] ?: false,
                userName = preferences[PreferencesKeys.USER_NAME] ?: "ব্যবহারকারী",
                currencySymbol = preferences[PreferencesKeys.CURRENCY_SYMBOL] ?: "৳",
                isNotificationsEnabled = preferences[PreferencesKeys.IS_NOTIFICATIONS_ENABLED] ?: true,
                isAppLockEnabled = preferences[PreferencesKeys.IS_APP_LOCK_ENABLED] ?: false,
                pinHash = preferences[PreferencesKeys.PIN_HASH] ?: "",
                lastBackupTime = preferences[PreferencesKeys.LAST_BACKUP_TIME] ?: 0L
            )
        }

    suspend fun setOnboardingCompleted(completed: Boolean, name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_ONBOARDING_COMPLETED] = completed
            if (name.isNotBlank()) {
                preferences[PreferencesKeys.USER_NAME] = name
            }
        }
    }

    suspend fun updateUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun setAppLock(enabled: Boolean, pinHash: String = "") {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_APP_LOCK_ENABLED] = enabled
            preferences[PreferencesKeys.PIN_HASH] = pinHash
        }
    }

    suspend fun updateLastBackupTime(time: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_BACKUP_TIME] = time
        }
    }
}
