package ru.komarov.api

data class RemoteMusic(
    val title: String,
    val artist: RemoteArtist,
    val album: RemoteAlbum,
    val preview: String
)


data class RemoteArtist(
    val name: String
)

data class RemoteAlbum(
    val cover: String,
    val title: String
)


data class RemoteListMusic(
    val tracks: RemoteTracks
)

data class RemoteTracks(
    val data: List<RemoteMusic>
)