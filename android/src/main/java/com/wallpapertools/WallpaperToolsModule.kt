package com.wallpapertools

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import java.net.URL

class WallpaperToolsModule(reactContext: ReactApplicationContext) :
  NativeWallpaperToolsSpec(reactContext) {

  override fun getName(): String {
    return NAME
  }

  override fun setWallpaper(uri: String, screen: String, promise: Promise) {
    Thread {
      try {
        if (uri.isBlank()) {
          promise.reject("INVALID_URI", "Image uri is empty.")
          return@Thread
        }

        val bitmap = loadBitmapFromUri(uri)
        val wallpaperManager = WallpaperManager.getInstance(reactApplicationContext)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          when (screen.lowercase()) {
            "home" -> {
              wallpaperManager.setBitmap(
                bitmap,
                null,
                true,
                WallpaperManager.FLAG_SYSTEM
              )
            }

            "lock" -> {
              wallpaperManager.setBitmap(
                bitmap,
                null,
                true,
                WallpaperManager.FLAG_LOCK
              )
            }

            "both" -> {
              wallpaperManager.setBitmap(
                bitmap,
                null,
                true,
                WallpaperManager.FLAG_SYSTEM
              )

              wallpaperManager.setBitmap(
                bitmap,
                null,
                true,
                WallpaperManager.FLAG_LOCK
              )
            }

            else -> {
              promise.reject(
                "INVALID_SCREEN",
                "screen must be 'home', 'lock', or 'both'."
              )
              return@Thread
            }
          }
        } else {
          wallpaperManager.setBitmap(bitmap)
        }

        promise.resolve(true)
      } catch (error: Exception) {
        promise.reject(
          "SET_WALLPAPER_ERROR",
          error.message ?: "Failed to set wallpaper.",
          error
        )
      }
    }.start()
  }

  private fun loadBitmapFromUri(uriString: String): Bitmap {
    val inputStream = if (
      uriString.startsWith("http://") ||
      uriString.startsWith("https://")
    ) {
      URL(uriString).openStream()
    } else {
      val uri = Uri.parse(uriString)

      reactApplicationContext.contentResolver.openInputStream(uri)
        ?: throw Exception("Could not open image uri: $uriString")
    }

    inputStream.use { stream ->
      return BitmapFactory.decodeStream(stream)
        ?: throw Exception("Could not decode image from uri: $uriString")
    }
  }

  companion object {
    const val NAME = "WallpaperTools"
  }
}