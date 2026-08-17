package com.lcars.demo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
        setContent {
            DemoHubScreen(
                onOpenCatalog = {
                    startActivity(Intent(this, ComponentCatalogActivity::class.java))
                },
                onOpenWeather = {
                    startActivity(Intent(this, WeatherSystemActivity::class.java))
                },
                onOpenPadd = {
                    startActivity(Intent(this, PaddVariantActivity::class.java))
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
