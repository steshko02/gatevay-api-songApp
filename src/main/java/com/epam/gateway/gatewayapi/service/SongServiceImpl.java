package com.epam.gateway.gatewayapi.service;

import com.epam.gateway.gatewayapi.model.SongDto;
import com.epam.gateway.gatewayapi.service.interfaces.SongService;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SongServiceImpl implements SongService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder builder;

    @Autowired
    private EurekaClient eurekaClient;

    @Override
    public List<SongDto> getSongsByNameRS(String name) {

        ResponseEntity<List<SongDto>> response = restTemplate.exchange(
                "http://upload-app/songs/search?name=" + name, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<SongDto>>() {
                });

        List<SongDto> result = response.getBody();
        return result;
    }


    @Override
    public Map.Entry<Integer, List<SongDto>> getPageSongsByNameRS(int numOfPage, int sizeOfPage, String name) {
//        InstanceInfo service = eurekaClient
//                .getApplication("upload-app")
//                .getInstances()
//                .get(0);
//
//        String hostName = service.getHostName();
//        int port = service.getPort();

        ResponseEntity<Map.Entry<Integer, List<SongDto>>> response = restTemplate.
                exchange(getHost() + "/songs/page/" + numOfPage + "/?name=" + name + "&size=" + sizeOfPage, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }

    @Override
    public String getHost() {
        InstanceInfo service = eurekaClient
                .getApplication("upload-app")
                .getInstances()
                .get(0);
        return "http://" + service.getHostName() + ":" + service.getPort();
    }

//    @Override
//    public List<SongDto> getPageSongsByNameRSTest(int numOfPage, int sizeOfPage, String name) {
//
//        InstanceInfo service = eurekaClient
//                .getApplication("upload-app")
//                .getInstances()
//                .get(0);
//
//        String hostName = service.getHostName();
//        int port = service.getPort();
//
////        Mono<List<SongDto>> songDtoFlux = builder.build().get().uri("http://upload-app/songsList/page/" + numOfPage + "/?name=" + name + "&size=" + sizeOfPage)
////                .retrieve().bodyToMono(new ParameterizedTypeReference<List<SongDto>>() {});
//        ResponseEntity<List<SongDto>> response = restTemplate.
//                exchange("http://"+hostName+":"+port+"/songsList/page/" + numOfPage + "/?name=" + name + "&size=" + sizeOfPage, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
//                });
//        List<SongDto> songDtos =  response.getBody();
//        return  songDtos;
//    }


}