# SimpleExoPlayerFragment [![](https://jitpack.io/v/zeeshanmansoori/SimpleExoPlayerFragment.svg)](https://jitpack.io/#zeeshanmansoori/SimpleExoPlayerFragment)

1. make use of exo player with fragment.
2. handle ativity/fragment life cycle events.
 

## ScreenShots
in portrait mode
![portrait](https://user-images.githubusercontent.com/43025057/146138526-e7e30b43-92e5-49e4-8ac2-0500690b0289.jpg)

fullscreen mode
![full_screen](https://user-images.githubusercontent.com/43025057/146138733-69ec22a1-9c19-4d73-aea8-c5b3c03fc1a7.jpg)

## Gradle Dependency

### Repository
Add this in your root build.gradle file (not your module build.gradle file):


```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}

```

Dependency
Add this to your module's build.gradle file:
```gradle

dependencies {
	...
	implementation 'com.github.zeeshanmansoori.SimpleExoPlayerFragment:ExoPlayerFragment-Lib::1.7' 
	}
}
 
```
## Basic Usage
###### In activity.xml
To use this add the fragment inside your activity.xml

```xml

<androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        
        ...                                         
        >


<fragment
            android:id="@+id/player_fragment"
            android:name="com.zee.exoplayerfragment_lib.ExoPlayerFragment"
            android:layout_width="match_parent"
            android:layout_height="250dp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/appbar" />
  
      ...
/>
```

activity.kt
``` java 

  private var isFullScreen = false
  private var playerFragment: ExoPlayerFragment? = null
  private val playBackListener: ExoPlayerFragment.PlayBackListener = object : ExoPlayerFragment.PlayBackListener {
        
        override fun isVideoPlaying(boolean: Boolean) {}
        override fun onFullScreenChanged(isFullScreen: Boolean) {}
        override fun isControllerVisible(visible: Boolean) {}
        override fun onVideoStarted() {}
        override fun onError(error: String) {}
        override fun onVideoEnded() {}
        override fun onSeekTo(seconds: Long) {}
        override fun isLoading(isLoading: Boolean) {}
        override fun isBuffering(isBuffering: Boolean) {}
    }

  override fun onCreate(savedInstanceState: Bundle?) {
        
        ...
        playerFragment =
            supportFragmentManager.findFragmentById(R.id.player_fragment) as ExoPlayerFragment
        playerFragment?.setListener(playBackListener)
        playerFragment?.playVideo(
            title = "video",
            "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4".toUri()
        )

    }
    
    ...

```

callbacks
```java
    
    
        //call when video start playing
        fun isVideoPlaying(boolean: Boolean)

        //call when video player exist or enter fullscreen mode
        fun onFullScreenChanged(isFullScreen: Boolean)

        //call when player controller visiblity changes
        fun isControllerVisible(visible: Boolean)

        //call when video started
        fun onVideoStarted()

        //call when error occured while playing
        fun onError(error: String)

        //call when video completed
        fun onVideoEnded()

        //call to seek 
        fun onSeekTo(seconds: Long)

        //call return loading state of a video
        fun isLoading(isLoading: Boolean)

      //call return buffering state
        fun isBuffering(isBuffering: Boolean)

    
```


