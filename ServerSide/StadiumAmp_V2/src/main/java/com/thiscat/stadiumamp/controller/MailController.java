package com.thiscat.stadiumamp.controller;

import com.thiscat.stadiumamp.entity.Event;
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
public class MailController {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    RunEventRepository runEventRepository;

    @GetMapping("/mail")
    public String home(Model model, @RequestParam Long id){
        Event event = eventRepository.findById(id).orElse(null);
//        Stadiumserver stadiumserver = stadiumServerRepository.findById(id).orElse(null);
        model.addAttribute("data", event);
        return "mail";
    }


}
