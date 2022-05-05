package com.epam.gateway.gatewayapi.service.interfaces;

import com.epam.gateway.gatewayapi.model.SongDto;

import java.util.List;
import java.util.Map;

public interface SongService {
    List<SongDto> getSongsByNameRS(String name);

    Map.Entry<Integer, List<SongDto>> getPageSongsByNameRS(int numOfPage, int sizeOfPage, String name);

    String getHost();
}
