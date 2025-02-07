package com.charr0max.flashcards.presentation.ui.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat

object PermissionHelper {
    const val RECORD_AUDIO_PERMISSION_CODE = 1001

    fun hasAudioPermission(@SuppressLint("RestrictedApi") activity: ComponentActivity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestAudioPermission(@SuppressLint("RestrictedApi") activity: ComponentActivity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            RECORD_AUDIO_PERMISSION_CODE
        )
    }
}