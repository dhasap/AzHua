package com.azhua.feature.player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.azhua.core.ui.theme.AzHuaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val donghuaId = intent.getLongExtra(EXTRA_DONGHUA_ID, 0L)
        val episodeId = intent.getLongExtra(EXTRA_EPISODE_ID, 0L)

        setContent {
            AzHuaTheme {
                PlayerScreen(
                    donghuaId = donghuaId,
                    episodeId = episodeId,
                    onBack = { finish() },
                )
            }
        }
    }

    companion object {
        const val EXTRA_DONGHUA_ID = "extra_donghua_id"
        const val EXTRA_EPISODE_ID = "extra_episode_id"

        fun createIntent(context: Context, donghuaId: Long, episodeId: Long): Intent {
            return Intent(context, PlayerActivity::class.java).apply {
                putExtra(EXTRA_DONGHUA_ID, donghuaId)
                putExtra(EXTRA_EPISODE_ID, episodeId)
            }
        }
    }
}
