package com.epam.gateway.gatewayapi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CustomPage {

    private int size;
    private int pageNumber;
    private int totalPages;
    private List<SongDto> list;

    public CustomPage(int size, int pageNumber, int getTotalPages, List<SongDto> list) {
        this.size = size;
        this.pageNumber = pageNumber;
        this.totalPages = getTotalPages;
        this.list = list;
    }

    public CustomPage() {
    }
}
