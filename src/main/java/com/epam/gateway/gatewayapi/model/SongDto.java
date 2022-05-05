package com.epam.gateway.gatewayapi.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SongDto {

    private Long id;

    private String name;

    private int year;

    private String notes;

    private String album;

    private Long resource;

    private String resourceObjId;

    public SongDto(Long id, String name, int year, String notes, String album, Long resource, String resourceObjId) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.notes = notes;
        this.album = album;
        this.resource = resource;
        this.resourceObjId = resourceObjId;
    }

    public SongDto() {
    }
}
