package ru.komarov.musicplayer.presentation

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.komarov.api.CurrentMusicsList
import ru.komarov.musicplayer.App
import ru.komarov.musicplayer.R
import ru.komarov.musicplayer.databinding.ActivityMainBinding
import ru.komarov.musicplayer.domain.MusicController
import ru.komarov.player.PlayerService
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var musicController: MusicController


    override fun onStart() {
        super.onStart()

        (application as App).appComponent.inject(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.POST_NOTIFICATIONS
                ), PackageManager.PERMISSION_GRANTED
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()

    }

    private fun initNavigation() {
        val navView: BottomNavigationView = binding.activityMainBottomNavView
        val navController =
            (supportFragmentManager.findFragmentById(R.id.activity_main__nav_host) as NavHostFragment).navController

        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == ru.komarov.player.R.id.playerFragment) {
                if (CurrentMusicsList.musicsList == null)
                    return@addOnDestinationChangedListener

                if(CurrentMusicsList.currentMusicId == -1){

                }

                musicController.setMusicList(
                    CurrentMusicsList.musicsList!!,
                    CurrentMusicsList.currentMusicId!!
                )

                runOnUiThread {
                    musicController.startService()
                }
                CurrentMusicsList.musicsList = null
                CurrentMusicsList.currentMusicId = null
            }

        }
    }


    override fun onDestroy() {
        musicController.stopService()
        super.onDestroy()
    }


}