package com.sovchilar.made.domain.usecases

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity

class OpenTelegramUseCase {
    fun openUserPage(context: Context, userName: String) {
        val packageName = "org.telegram.messenger"
        val intent =
            Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/${userName.removePrefix("@")}"))
        intent.setPackage(packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, intent, null)
    }
}