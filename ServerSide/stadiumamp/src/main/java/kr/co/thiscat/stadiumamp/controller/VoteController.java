package kr.co.thiscat.stadiumamp.controller;

import kr.co.thiscat.stadiumamp.dto.RunEventDto;
import kr.co.thiscat.stadiumamp.entity.Runevent;
import kr.co.thiscat.stadiumamp.entity.Stadiumserver;
import kr.co.thiscat.stadiumamp.entity.repository.RunEventRepository;
import kr.co.thiscat.stadiumamp.entity.repository.StadiumServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/")
public class VoteController {
    @Autowired
    StadiumServerRepository stadiumServerRepository;
    @Autowired
    RunEventRepository runEventRepository;

    @GetMapping("/vote")
    public String home(Model model,  @RequestParam Integer team, @RequestParam Long server_id ){
        Stadiumserver stadiumserver = stadiumServerRepository.findById(server_id).orElseThrow(EntityNotFoundException::new);
        Runevent runevent = runEventRepository.findFirstByStadiumserverOrderByIdDesc(stadiumserver).orElseThrow(EntityNotFoundException::new);

        RunEventDto runEventDto = RunEventDto.builder()
                .id(runevent.getId())
                .stadiumServerId(runevent.getStadiumserver().getId())
                .voteTime(runevent.getVoteTime())
                .resultTime(runevent.getResultTime())
                .homeCount(runevent.getHomeCount())
                .awayCount(runevent.getAwayCount())
                .eventState(runevent.getEventState())
                .startDateTime(runevent.getStartDateTime())
                .build();

        String strTeam = "";
        if(team == 0 || team == 1)
            strTeam = "Home";
        else
            strTeam = "Away";


        LocalDateTime startTime = runevent.getStartDateTime();
        int vTime = runevent.getVoteTime();
        LocalDateTime endTime = startTime.plusMinutes(vTime);

        LocalDateTime nowTime = LocalDateTime.now();
        if(nowTime.isAfter(endTime))
        {
            model.addAttribute("state", "이벤트 종료");
        }
        else {
            Duration duration = Duration.between(nowTime, endTime);
            long sec = duration.getSeconds();
            long minV = sec / 60;
            long secV = sec % 60;
            String tVal = ""+minV+"분"+secV+"초";
            model.addAttribute("state", tVal);

        }

        model.addAttribute("server", stadiumserver);
        model.addAttribute("event", runEventDto);
        model.addAttribute("teamtype", strTeam);
        model.addAttribute("team", team);
        return "vote";
    }
    @GetMapping("/vote/save")
    public String voteSave(HttpServletRequest request, @RequestParam String teamType, @RequestParam Long eventId ){

        Runevent runevent = runEventRepository.findById(eventId).orElse(null);
        LocalDateTime startTime = runevent.getStartDateTime();
        int vTime = runevent.getVoteTime();
        LocalDateTime endTime = startTime.plusMinutes(vTime);
        LocalDateTime nowTime = LocalDateTime.now();
        if(nowTime.isAfter(endTime))
        {
            String referer = request.getHeader("Referer");
            return "redirect:"+ referer;
        }

        int voteCount = 1;
        if(teamType.equalsIgnoreCase("1") || teamType.equalsIgnoreCase("3"))
            voteCount = 3;

        if(teamType.equalsIgnoreCase("0") || teamType.equalsIgnoreCase("1"))
        {
            Integer homeCount = runevent.getHomeCount();
            int total = voteCount;
            if(homeCount != null)
                total += homeCount;

            runevent.setHomeCount(total);
        }
        else
        {
            Integer awayCount = runevent.getAwayCount();
            int total = voteCount;
            if(awayCount != null)
                total += awayCount;
            runevent.setAwayCount(total);
        }
        runEventRepository.save(runevent);

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;

       // return "redirect:/vote?team=2&server_id=-1";


    }
}
