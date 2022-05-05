package com.epam.gateway.gatewayapi.controllers;

import com.epam.gateway.gatewayapi.model.SongDto;
import com.epam.gateway.gatewayapi.service.interfaces.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AudioPlayerController {

    @Autowired
    private SongService songService;


    @RequestMapping(path = {"/search/{pageNO}"})
    public String search(Model model, @PathVariable int pageNO,
                         String nameForSearch, Long id, @AuthenticationPrincipal OAuth2User user) {
        int pageSize = 2;
        model.addAttribute("nameForSearch", nameForSearch);
        model.addAttribute("currentPage", pageNO);
        List<SongDto> realSongsList = new ArrayList<>();
        if (nameForSearch != null) {
            Map.Entry<Integer, List<SongDto>> page = songService.getPageSongsByNameRS(pageNO, pageSize, nameForSearch);
            realSongsList = page.getValue();
            model.addAttribute("totalPages", page.getKey());
        }

        if (id != null) {
            model.addAttribute("id", id);
        }
        if (nameForSearch != null) {
            model.addAttribute("songs", realSongsList);
            return "audio_v2";
        }

        model.addAttribute("songs", realSongsList);
        return "audio_v2";
    }

    @RequestMapping(path = {"/player"})
    public String songList(Model model) {
        List<SongDto> realSongsList = new ArrayList<>();
        model.addAttribute("songs", realSongsList);
        return "index";
    }

    @RequestMapping(path = {"/player2"})
    public String songListV2(Model model) {
        List<SongDto> realSongsList = new ArrayList<>();
        model.addAttribute("songs", realSongsList);
        return "audio_v2";
    }

}
