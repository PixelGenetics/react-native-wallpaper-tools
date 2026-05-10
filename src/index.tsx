import { Platform } from 'react-native';
import WallpaperTools from './NativeWallpaperTools';

export type WallpaperScreen = 'home' | 'lock' | 'both';

export type SetWallpaperOptions = {
  uri: string;
  screen?: WallpaperScreen;
};

export async function setWallpaper(
  options: SetWallpaperOptions
): Promise<boolean> {
  if (Platform.OS !== 'android') {
    throw new Error('setWallpaper currently only supports Android.');
  }

  if (!options || !options.uri) {
    throw new Error('Image uri is required.');
  }

  const screen = options.screen ?? 'home';

  if (!['home', 'lock', 'both'].includes(screen)) {
    throw new Error("screen must be 'home', 'lock', or 'both'.");
  }

  return WallpaperTools.setWallpaper(options.uri, screen);
}

export default {
  setWallpaper,
};
