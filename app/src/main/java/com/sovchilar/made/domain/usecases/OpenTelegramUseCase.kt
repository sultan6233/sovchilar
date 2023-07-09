package com.sovchilar.made.domain.usecases

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.snackbar.Snackbar

class OpenTelegramUseCase {
    fun openUserPage(context: Context, userName: String) {
        val intent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/${userName.removePrefix("@")}"))

        try {
            intent.setPackage(provideOfficialTelegramPackageName())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(context, intent, null)
        } catch (e: Exception) {
            try {
                intent.setPackage(providePlusTelegramPackageName())
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(context, intent, null)
            } catch (e: Exception) {
                try {
                    intent.setPackage(provideTelegramXPackageName())
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(context, intent, null)
                } catch (e: Exception) {
                    Toast.makeText(context, "Telegram is not installed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun openTelegramHelp(context: Context) {
        openUserPage(context, "@sovchilar_help")
    }

    private fun provideOfficialTelegramPackageName(): String {
        return "org.telegram.messenger"
    }

    private fun providePlusTelegramPackageName(): String {
        return "org.telegram.plus"
    }

    private fun provideTelegramXPackageName(): String {
        return "org.thunderdog.challegram"
    }

    private fun provideTelegramPackageName(): String {
        return "org.telegram"
    }

    private fun provideTelegramPackageName2(): String {
        return "org.telegram.messenger"
    }

    private fun provideGraphMessengerPackageName(): String {
        return "ir.ilmili.telegraph"
    }
}