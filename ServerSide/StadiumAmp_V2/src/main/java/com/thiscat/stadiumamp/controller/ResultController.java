package com.thiscat.stadiumamp.controller;


import com.thiscat.stadiumamp.dto.RunEventDto;
import com.thiscat.stadiumamp.entity.Event;
import com.thiscat.stadiumamp.entity.RunEvent;
import com.thiscat.stadiumamp.entity.repository.EventRepository;
import com.thiscat.stadiumamp.entity.repository.RunEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class ResultController {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    RunEventRepository runEventRepository;

    @GetMapping("/result")
    public String home(Model model){
        List<Event> stadiumservers = eventRepository.findAllByOrderByIdAsc();
        //List<RunEvent> runevents = runEventRepository.findAllByOrderByIdAsc();
        List<RunEvent> runevents = runEventRepository.findTop30ByOrderByIdDesc();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<RunEventDto> runEventDtos = runevents
                .stream()
                .map(x->RunEventDto.builder()
                        .id(x.getId())
                        .eventId(x.getEvent().getId())
                        .startDateTime(x.getStartDateTime())
                        .endDateTime(x.getEndDateTime())

                        .eventState(x.getEventState())
                        .homeCount(x.getHomeCount())
                        .home1Count(x.getHome1Count())
                        .home2Count(x.getHome2Count())
                        .home3Count(x.getHome3Count())
                        .home4Count(x.getHome4Count())
                        .home5Count(x.getHome5Count())
                        .awayCount(x.getAwayCount())
                        .away1Count(x.getAway1Count())
                        .away2Count(x.getAway2Count())
                        .away3Count(x.getAway3Count())
                        .away4Count(x.getAway4Count())
                        .away5Count(x.getAway5Count())
                        .tag0(x.getTag0())
                        .tag1(x.getTag1())
                        .tag2(x.getTag2())
                        .tag3(x.getTag3())
                        .tag4(x.getTag4())
                        .tag5(x.getTag5())
                        .tag6(x.getTag6())
                        .tag7(x.getTag7())
                        .tag8(x.getTag8())
                        .tag9(x.getTag9())
                        .result(getResultTeam(x))
                        .build())
                .collect(Collectors.toList());

        model.addAttribute("server", stadiumservers);
        model.addAttribute("event", runEventDtos);
        return "result";
    }

    public String getResultTeam(RunEvent x)
    {

        int home1 = x.getHome1Count() == null ? 0 : x.getHome1Count();
        int home2 = x.getHome2Count() == null ? 0 : x.getHome2Count();
        int home3 = x.getHome3Count() == null ? 0 : x.getHome3Count();
        int home4 = x.getHome4Count() == null ? 0 : x.getHome4Count();
        int home5 = x.getHome5Count() == null ? 0 : x.getHome5Count();
        int away1 = x.getAway1Count() == null ? 0 : x.getAway1Count();
        int away2 = x.getAway2Count() == null ? 0 : x.getAway2Count();
        int away3 = x.getAway3Count() == null ? 0 : x.getAway3Count();
        int away4 = x.getAway4Count() == null ? 0 : x.getAway4Count();
        int away5 = x.getAway5Count() == null ? 0 : x.getAway5Count();
        int[] values = {home1, home2, home3, home4, home5, away1, away2, away3, away4, away5};
        int maxPos = 0;
        int max = 0;
        for(int i = 0; i < values.length; i++)
        {
            if(values[i] > max)
            {
                max = values[i];
                maxPos = i;
            }
        }
        String result = "Home1";
        switch (maxPos)
        {
            case 0:
                result = "Home1";
                break;
            case 1:
                result = "Home2";
                break;
            case 2:
                result = "Home3";
                break;
            case 3:
                result = "Home4";
                break;
            case 4:
                result = "Home5";
                break;
            case 5:
                result = "Away1";
                break;
            case 6:
                result = "Away2";
                break;
            case 7:
                result = "Away3";
                break;
            case 8:
                result = "Away4";
                break;
            case 9:
                result = "Away5";
                break;
        }
        return result;
    }
}
