package com.epam.gateway.gatewayapi.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Deprecated
public class SongList {

    private List<SongDto> songList;

    public SongList() {
        songList = new ArrayList<>();
    }

    public void addSong(SongDto song) {
        songList.add(song);
    }

    public SongList(List<SongDto> songList) {
        this.songList = songList;
    }
}
