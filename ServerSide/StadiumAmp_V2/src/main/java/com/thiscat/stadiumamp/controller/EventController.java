package com.thiscat.stadiumamp.controller;

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
        model.addAttribute("data", event);
        model.addAttribute("bgimg", bgImage);
        return "event";
    }


}
