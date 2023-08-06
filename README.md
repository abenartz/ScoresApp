# ScoresApp
This app is showing highlight vedio moments of soccer matches like Instagram "Stories".

## Screens:

| Matches list                        | Story page                          |
| ----------------------------------- | ----------------------------------- |
| ![Matches list](https://github.com/abenartz/ScoresApp/assets/43881667/d38a6737-1f4f-4b8c-a6db-447fa4832089) | ![Story page](https://github.com/abenartz/ScoresApp/assets/43881667/43a31723-e58e-4c16-b5b6-125637abc891)
 |


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
  
