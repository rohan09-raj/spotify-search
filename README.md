# Spotify Search

## Description
Welcome to Spotify Search, your ultimate companion for exploring the vast world of music! With our sleek and intuitive interface powered by Spotify APIs, discovering your favorite artists, albums, playlists, and tracks has never been easier.

## Features
- Explore Artists, Albums, Playlists and Tracks
- Recent Search enhances the search experience by displaying the most recent detail page visits using RoomDB
- Delete recent search

## Android APK
[Groww Assignment APK](https://drive.google.com/drive/folders/1bo4L81OgNJbd-128RQRwBs3gsyx7WNi7?usp=drive_link)

## Screen
 Splash Screen | Recent Searches | Search | Search Detail
:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------:
![Screenshot_2024-04-06-14-27-08-559_com example spotifysearch](https://github.com/rohan09-raj/spotify-search/assets/78433013/294853ec-b77c-4c39-a176-2a56df7c15d2) | ![Screenshot_2024-04-06-14-28-09-848_com example spotifysearch](https://github.com/rohan09-raj/spotify-search/assets/78433013/5f641309-e23f-46d6-9715-ffef2896fb84) | ![Screenshot_2024-04-06-14-28-36-286_com example spotifysearch](https://github.com/rohan09-raj/spotify-search/assets/78433013/c1f9fd69-d52e-497f-ac10-aaae7abaaf59) | ![Screenshot_2024-04-06-15-36-55-471_com example spotifysearch](https://github.com/rohan09-raj/spotify-search/assets/78433013/1e5f6ffa-d754-4583-b462-cdd9034ae781)


## Android App in Action
https://github.com/rohan09-raj/spotify-search/assets/78433013/3fc58952-5896-4e4e-a643-37e2ea86bcc6

## Dependencies Used
- Glide
- RoomDB
- Retrofit + GSON
- ViewModel and LiveData
- Dagger Hilt

## How to set up locally?
- Clone the repository to your local machine
- Create a Spotify app to get the client ID and client secret. Follow the steps here: https://developer.spotify.com/documentation/web-api/tutorials/getting-started
- Create an apikey.properties file in the root directory and add the following lines, a sample file is provided for the reference: https://github.com/rohan09-raj/spotify-search/blob/main/sample_apikey.properties
```
SPOTIFY_CLIENT_ID="<YOUR_CLIENT_ID>"
SPOTIFY_CLIENT_SECRET="<YOUR_CLIENT_SECRET>"
```
- Build and Run the app
