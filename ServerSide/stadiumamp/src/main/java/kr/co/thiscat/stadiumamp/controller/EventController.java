package kr.co.thiscat.stadiumamp.controller;

import kr.co.thiscat.stadiumamp.entity.Stadiumserver;
import kr.co.thiscat.stadiumamp.entity.repository.RunEventRepository;
import kr.co.thiscat.stadiumamp.entity.repository.StadiumServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class EventController {
    @Autowired
    StadiumServerRepository stadiumServerRepository;
    @Autowired
    RunEventRepository runEventRepository;

    @GetMapping("/event")
    public String home(Model model, @RequestParam Long id){

        Stadiumserver stadiumserver = stadiumServerRepository.findById(id).orElse(null);
        model.addAttribute("data", stadiumserver);
        return "event";
    }


}
