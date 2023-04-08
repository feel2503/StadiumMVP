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
        List<Stadiumserver> stadiumservers = stadiumServerRepository.findAllByOrderByIdAsc();
        List<Runevent> runevents = runEventRepository.findAllByOrderByIdAsc();
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
                        .home1Count(x.getHome1Count())
                        .home2Count(x.getHome2Count())
                        .awayCount(x.getAwayCount())
                        .away1Count(x.getAway1Count())
                        .away2Count(x.getAway2Count())
                        .result(getResultTeam(x))
                        .build())
                .collect(Collectors.toList());

        model.addAttribute("server", stadiumservers);
        model.addAttribute("event", runEventDtos);
        return "result";
    }

    public String getResultTeam(Runevent x)
    {
        int home1 = x.getHome1Count() == null ? 0 : x.getHome1Count();
        int home2 = x.getHome2Count() == null ? 0 : x.getHome2Count();
        int away1 = x.getAway1Count() == null ? 0 : x.getAway1Count();
        int away2 = x.getAway2Count() == null ? 0 : x.getAway2Count();
        int[] values = {home1, home2, away1, away2};
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
                result = "Away1";
                break;
            case 3:
                result = "Away2";
                break;
        }
        return result;
    }
}
