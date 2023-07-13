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
import java.net.URLDecoder;
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
    public String home(Model model,  @RequestParam Integer team, @RequestParam Long server_id,
        @RequestParam ( required = false) Integer move){
        if((team == 1 || team == 3) && (move == null)) {
            model.addAttribute("data", server_id);
            model.addAttribute("team", team);
            return "sso";
        }
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
            model.addAttribute("min", minV);
            model.addAttribute("sec", secV);

        }

        stadiumserver.setHomeMusic1(getFileName(stadiumserver.getHomeMusic1()));
        stadiumserver.setHomeMusic2(getFileName(stadiumserver.getHomeMusic2()));
        stadiumserver.setAwayMusic1(getFileName(stadiumserver.getAwayMusic1()));
        stadiumserver.setAwayMusic2(getFileName(stadiumserver.getAwayMusic2()));
//        model.addAttribute("min", 1);
//        model.addAttribute("sec", 10);
        model.addAttribute("server", stadiumserver);
        model.addAttribute("event", runEventDto);
        model.addAttribute("teamtype", strTeam);
        model.addAttribute("team", team);

//        if(move != null && move == false)
//            return "redirect:vote";
        return "vote";
    }

    @GetMapping("/votep")
    public String vote_p(Model model,  @RequestParam Integer team, @RequestParam Long server_id ){
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
            model.addAttribute("min", minV);
            model.addAttribute("sec", secV);

        }

//        model.addAttribute("min", 1);
//        model.addAttribute("sec", 10);
        model.addAttribute("server", stadiumserver);
        model.addAttribute("event", runEventDto);
        model.addAttribute("teamtype", strTeam);
        model.addAttribute("team", team);
        return "vote";
    }
    @GetMapping("/vote/save")
    public String voteSave(HttpServletRequest request, @RequestParam String teamType, @RequestParam Long eventId,
                           @RequestParam Long eventType){

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

            if(eventType == 1)
            {
                Integer home1 = runevent.getHome1Count();
                int tot1 = voteCount;
                if(home1 != null)
                    tot1 += home1;
                runevent.setHome1Count(tot1);
            }
            else if(eventType == 2)
            {
                Integer home2 = runevent.getHome2Count();
                int tot2 = voteCount;
                if(home2 != null)
                    tot2 += home2;
                runevent.setHome2Count(tot2);
            }

        }
        else
        {
            Integer awayCount = runevent.getAwayCount();
            int total = voteCount;
            if(awayCount != null)
                total += awayCount;
            runevent.setAwayCount(total);

            if(eventType == 1)
            {
                Integer away = runevent.getAway1Count();
                int tot = voteCount;
                if(away != null)
                    tot += away;
                runevent.setAway1Count(tot);
            }
            else if(eventType == 2)
            {
                Integer away = runevent.getAway2Count();
                int tot = voteCount;
                if(away != null)
                    tot += away;
                runevent.setAway2Count(tot);
            }

        }
        runEventRepository.save(runevent);

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;

       // return "redirect:/vote?team=2&server_id=-1";


    }

    @GetMapping("/sso")
    public String home_sso(Model model,  @RequestParam Integer team, @RequestParam Long server_id ){
        return "sso";
    }

    //////////////////
    public String getFileName(String url)
    {
        if(url == null)
            return url;
        String strDec = URLDecoder.decode(url);
        String name = strDec.substring(strDec.lastIndexOf('/')+1, strDec.length());
        return name;
    }
}
