package com.wallpapertools

import com.facebook.react.TurboReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider

class WallpaperToolsPackage : TurboReactPackage() {
  override fun getModule(
    name: String,
    reactContext: ReactApplicationContext
  ): NativeModule? {
    return if (name == WallpaperToolsModule.NAME) {
      WallpaperToolsModule(reactContext)
    } else {
      null
    }
  }

  override fun getReactModuleInfoProvider(): ReactModuleInfoProvider {
    return ReactModuleInfoProvider {
      val moduleInfos: MutableMap<String, ReactModuleInfo> = HashMap()

      moduleInfos[WallpaperToolsModule.NAME] = ReactModuleInfo(
        WallpaperToolsModule.NAME,
        WallpaperToolsModule.NAME,
        false,
        false,
        true,
        false,
        true
      )

      moduleInfos
    }
  }
}