package kr.co.thiscat.stadiumamp.controller;

import kr.co.thiscat.stadiumamp.dto.RunEventDto;
import kr.co.thiscat.stadiumamp.entity.Runevent;
import kr.co.thiscat.stadiumamp.entity.Stadiumserver;
import kr.co.thiscat.stadiumamp.entity.repository.RunEventRepository;
import kr.co.thiscat.stadiumamp.entity.repository.StadiumServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class ResultController {
    @Autowired
    StadiumServerRepository stadiumServerRepository;
    @Autowired
    RunEventRepository runEventRepository;

    @GetMapping("/result")
    public String home(Model model){
        List<Stadiumserver> stadiumservers = stadiumServerRepository.findAll();
        List<Runevent> runevents = runEventRepository.findAll();
        List<RunEventDto> runEventDtos = runevents
                .stream()
                .map(x->RunEventDto.builder()
                        .id(x.getId())
                        .stadiumServerId(x.getStadiumserver().getId())
                        .startDateTime(x.getStartDateTime())
                        .endDateTime(x.getEndDateTime())
                        .voteTime(x.getVoteTime())
                        .resultTime(x.getResultTime())
                        .eventState(x.getEventState())
                        .homeCount(x.getHomeCount())
                        .awayCount(x.getAwayCount())
                        .build())
                .collect(Collectors.toList());

        model.addAttribute("server", stadiumservers);
        model.addAttribute("event", runEventDtos);
        return "result";
    }
}
