package com.epam.gateway.gatewayapi.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Deprecated
public class SongIdList {

    private List<Long> songList;

    public SongIdList() {
        songList = new ArrayList<>();
    }

    public void addSong(Long songID) {
        songList.add(songID);
    }

}