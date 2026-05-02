package com.example.nammaplatform.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import java.util.Locale

class LanguageManager(private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("language_pref", Context.MODE_PRIVATE)

    companion object {
        private const val LANGUAGE_KEY = "selected_language"
        private const val DEFAULT_LANGUAGE = "en"
        const val KANNADA = "kn"
        const val ENGLISH = "en"
    }

    fun setLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        // Save preference
        sharedPreferences.edit().putString(LANGUAGE_KEY, languageCode).apply()
    }

    fun getCurrentLanguage(): String {
        return sharedPreferences.getString(LANGUAGE_KEY, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    fun applyLanguage() {
        val languageCode = getCurrentLanguage()
        setLanguage(languageCode)
    }

    fun toggleLanguage() {
        val currentLanguage = getCurrentLanguage()
        val newLanguage = if (currentLanguage == ENGLISH) KANNADA else ENGLISH
        setLanguage(newLanguage)
    }

    fun isKannada(): Boolean {
        return getCurrentLanguage() == KANNADA
    }

    fun isEnglish(): Boolean {
        return getCurrentLanguage() == ENGLISH
    }
}
