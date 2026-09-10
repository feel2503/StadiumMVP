package com.thiscat.stadiumamp.controller;

import static com.thiscat.stadiumamp.system.common.ColorUtils.getColorValue;

import com.thiscat.stadiumamp.entity.Event;
import com.thiscat.stadiumamp.entity.repository.EventImageRepository;
import com.thiscat.stadiumamp.entity.repository.EventRepository;
import com.thiscat.stadiumamp.entity.repository.RunEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class EventController {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    RunEventRepository runEventRepository;
    @Autowired
    EventImageRepository eventImageRepository;

    @GetMapping("/event")
    public String home(Model model, @RequestParam Long id){
        Event event = eventRepository.findById(id).orElse(null);
//        Stadiumserver stadiumserver = stadiumServerRepository.findById(id).orElse(null);

        String bgImage = eventImageRepository.findTypeEventImage(id, "IMAGE_DEFAULT");
        //bgImage = "https://pmdm-update.s3.ap-northeast-2.amazonaws.com/portvid.mp4";
        model.addAttribute("data", event);
        model.addAttribute("bgimg", bgImage);
        model.addAttribute("bgcolor", getColorValue(event.getEventBkcolor()));

        model.addAttribute("logoImg", getDropboxImage(event.getLogoImg()));
        model.addAttribute("bottomAd", getDropboxImage(event.getBottomAd()));
        model.addAttribute("homeTitleImg", getDropboxImage(event.getHomeTitleImg()));
        model.addAttribute("awayTitleImg", getDropboxImage(event.getAwayTitleImg()));
        model.addAttribute("awayShowSate", event.getAwayShowState());
        model.addAttribute("homeColor", getColorValue(event.getHomeColor()));
        model.addAttribute("homeFont", getColorValue(event.getHomeFont()));
        model.addAttribute("awayColor", getColorValue(event.getAwayColor()));
        model.addAttribute("awayFont", getColorValue(event.getAwayFont()));
        model.addAttribute("bottomAdUrl", event.getBottomAdUrl());
        return "event";
    }



    private String getDropboxImage(String baseUrl){

        if (baseUrl != null && baseUrl.contains("dropbox.com")) {
            // www.dropbox.com을 dl.dropboxusercontent.com으로 치환
            baseUrl = baseUrl.replace("www.dropbox.com", "dl.dropboxusercontent.com");
            // 맨 뒤의 dl=0 또는 dl=1 파라미터를 제거하거나 변경 (선택사항이지만 치환하면 더 안전합니다)
            baseUrl = baseUrl.replaceAll("\\?dl=\\d", "");
            baseUrl = baseUrl.replaceAll("&dl=\\d", "");
        }
        return baseUrl;
    }


}
