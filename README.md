# ScoresApp
This app is showing highlight vedio moments of soccer matches like Instagram "Stories".

## Screens:

| Matches list                        | Story page                          |
| ----------------------------------- | ----------------------------------- |
| ![image](https://github.com/abenartz/ScoresApp/assets/43881667/ff348211-040c-4d40-8eae-7129b69449eb) | ![image](https://github.com/abenartz/ScoresApp/assets/43881667/9f3b45ef-32f2-4514-9936-c950b159ce30) |


## Features and 3rd party libraries
- MVVM Jetpack Arcitecture with repository pattern
- Hilt for DI
- Navigation graph for hosting fragments
- Kotlin Coroutines + StateFlow for concurrency and reactive programing
- Dark/light modes
- ExoPlayer
- Moshi json adapter
- Timber for secure logging
- Glide for image rendering
- StoriesProgressView (lib for stories top progesses)

## Future steps
- Preloading the videos and cach them localy
- Save fetched data to Room db and show the user data only from there (Single source of truth)
- Pagination matches list from Room db with the minimum data required for list item.
  
