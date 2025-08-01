package com.thiscat.stadiumamp.controller;

import com.thiscat.stadiumamp.dao.EventMusicDao;
import com.thiscat.stadiumamp.dao.TagDao;
import com.thiscat.stadiumamp.dto.*;
import com.thiscat.stadiumamp.entity.Cheertag;
import com.thiscat.stadiumamp.entity.Event;
import com.thiscat.stadiumamp.entity.EventMusic;
import com.thiscat.stadiumamp.entity.RunEvent;
import com.thiscat.stadiumamp.entity.repository.*;

import com.thiscat.stadiumamp.entity.value.TeamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class VoteController {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    RunEventRepository runEventRepository;
    @Autowired
    EventMusicRepository eventMusicRepository;
    @Autowired
    EventImageRepository eventImageRepository;
    @Autowired
    private CheertagRepository cheertagRepository;

    @GetMapping("/vote")
    public String vote(Model model,  @RequestParam Integer team, @RequestParam Long event_id,
        @RequestParam ( required = false) Integer move){
        if((team == 1 || team == 3) && (move == null)) {
            model.addAttribute("data", event_id);
            model.addAttribute("team", team);
            return "sso";
        }

        Event event = eventRepository.findById(event_id).orElseThrow(EntityNotFoundException::new);
        RunEvent runevent = runEventRepository.findFirstByEventOrderByIdDesc(event).orElseThrow(EntityNotFoundException::new);

        RunEventWebDto runEventDto = RunEventWebDto.builder()
                .id(runevent.getId())
                .eventId(runevent.getEvent().getId())
                .eventState(runevent.getEventState())
                .startDateTime(runevent.getStartDateTime())
                .triggerType(runevent.getEvent().getTriggerType())
                .triggerTime(runevent.getEvent().getTriggerTime())
                .triggerVote(runevent.getEvent().getTriggerVote())
                .homeCount(runevent.getHomeCount())
                .awayCount(runevent.getAwayCount())
                .webUrl(event.getWebUrl())
                .openchatUrl(event.getOpenchatUrl())
                .build();

//        List<EventMusic> eventMusicList =  eventMusicRepository.findAllByEventOrderBySequenceAsc(event);

        runEventDto = updateRunEventMusic(runEventDto, event.getId());

        String strTeam = "";
        String bgImage = "";
        if(team == 0 || team == 1)
        {
            bgImage = eventImageRepository.findTypeEventImage(event_id, "IMAGE_HOME");
            strTeam = "Home";
        }
        else
        {
            bgImage = eventImageRepository.findTypeEventImage(event_id, "IMAGE_AWAY");
            strTeam = "Away";
        }

        LocalDateTime startTime = runevent.getStartDateTime();
        int vTime = runEventDto.getTriggerTime();
        LocalDateTime endTime = startTime.plusSeconds(vTime);

        LocalDateTime nowTime = LocalDateTime.now();
        if(nowTime.isAfter(endTime))
        {
            model.addAttribute("state", "STOP");
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


        //bgImage = "https://lh3.googleusercontent.com/drive-viewer/AKGpihYYrUREeok3BOYgpR_kdlLX4MhYkeEVIjVM6UlDkhWlY86tCtknoo_2bgBWHnQ5DiyBGAnnuYdJN9uZ7LLCRw0rw06fsPWdwg=s2560";
        model.addAttribute("event", event);
        model.addAttribute("runevent", runEventDto);
        model.addAttribute("teamtype", strTeam);
        model.addAttribute("team", team);
        model.addAttribute("homeColor", "#"+event.getHomeColor());
        model.addAttribute("homeFont", "#"+event.getHomeFont());
        model.addAttribute("awayColor", "#"+event.getAwayColor());
        model.addAttribute("awayFont", "#"+event.getAwayFont());
        model.addAttribute("bgimg", bgImage);
        model.addAttribute("bgcolor", "#"+event.getEventBkcolor());
        model.addAttribute("openchatimg", event.getOpenchatImg());
        model.addAttribute("webimg", event.getWebImg());


        // Tag 추가
        List<Cheertag> cheertags = cheertagRepository.findAllByEventOrderById(event);
        List<TagDao> tags = cheertags.stream()
                .map(x ->  new TagDao(x.getTag_id(), x.getValue(), x.getLabel(), false))
                .collect(Collectors.toList());

        model.addAttribute("tags", tags);

        return "vote";
    }

    @GetMapping("/vote2")
    public String vote2(Model model,  @RequestParam Integer team, @RequestParam Long event_id,
                       @RequestParam ( required = false) Integer move){
        if((team == 1 || team == 3) && (move == null)) {
            model.addAttribute("data", event_id);
            model.addAttribute("team", team);
            return "sso";
        }

        Event event = eventRepository.findById(event_id).orElseThrow(EntityNotFoundException::new);
        RunEvent runevent = runEventRepository.findFirstByEventOrderByIdDesc(event).orElseThrow(EntityNotFoundException::new);

        RunEventWebDto runEventDto = RunEventWebDto.builder()
                .id(runevent.getId())
                .eventId(runevent.getEvent().getId())
                .eventState(runevent.getEventState())
                .startDateTime(runevent.getStartDateTime())
                .triggerType(runevent.getEvent().getTriggerType())
                .triggerTime(runevent.getEvent().getTriggerTime())
                .triggerVote(runevent.getEvent().getTriggerVote())
                .webUrl(event.getWebUrl())
                .openchatUrl(event.getOpenchatUrl())
                .build();

        runEventDto = updateRunEventMusic(runEventDto, event.getId());

        String strTeam = "";
        String bgImage = "";
        if(team == 0 || team == 1)
        {
            bgImage = eventImageRepository.findTypeEventImage(event_id, "IMAGE_HOME");
            strTeam = "Home";
        }
        else
        {
            bgImage = eventImageRepository.findTypeEventImage(event_id, "IMAGE_AWAY");
            strTeam = "Away";
        }

        LocalDateTime startTime = runevent.getStartDateTime();
        int vTime = runEventDto.getTriggerTime();
        LocalDateTime endTime = startTime.plusSeconds(vTime);

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


        model.addAttribute("event", event);
        model.addAttribute("runevent", runEventDto);
        model.addAttribute("teamtype", strTeam);
        model.addAttribute("team", team);
        model.addAttribute("homeColor", "#"+event.getHomeColor());
        model.addAttribute("awayColor", "#"+event.getAwayColor());
        model.addAttribute("bgimg", bgImage);

        return "vote2";
    }

    @GetMapping("/votep")
    public String vote_p(Model model,  @RequestParam Integer team, @RequestParam Long server_id ){
//        Stadiumserver stadiumserver = stadiumServerRepository.findById(server_id).orElseThrow(EntityNotFoundException::new);
//        Runevent runevent = runEventRepository.findFirstByStadiumserverOrderByIdDesc(stadiumserver).orElseThrow(EntityNotFoundException::new);
//
//        RunEventDto runEventDto = RunEventDto.builder()
//                .id(runevent.getId())
//                .stadiumServerId(runevent.getStadiumserver().getId())
//                .voteTime(runevent.getVoteTime())
//                .resultTime(runevent.getResultTime())
//                .homeCount(runevent.getHomeCount())
//                .awayCount(runevent.getAwayCount())
//                .eventState(runevent.getEventState())
//                .startDateTime(runevent.getStartDateTime())
//                .build();
//
//        String strTeam = "";
//        if(team == 0 || team == 1)
//            strTeam = "Home";
//        else
//            strTeam = "Away";
//
//
//        LocalDateTime startTime = runevent.getStartDateTime();
//        int vTime = runevent.getVoteTime();
//        LocalDateTime endTime = startTime.plusMinutes(vTime);
//
//        LocalDateTime nowTime = LocalDateTime.now();
//        if(nowTime.isAfter(endTime))
//        {
//            model.addAttribute("state", "이벤트 종료");
//        }
//        else {
//            Duration duration = Duration.between(nowTime, endTime);
//            long sec = duration.getSeconds();
//            long minV = sec / 60;
//            long secV = sec % 60;
//            String tVal = ""+minV+"분"+secV+"초";
//            model.addAttribute("state", tVal);
//            model.addAttribute("min", minV);
//            model.addAttribute("sec", secV);
//
//        }
//
////        model.addAttribute("min", 1);
////        model.addAttribute("sec", 10);
//        model.addAttribute("server", stadiumserver);
//        model.addAttribute("event", runEventDto);
//        model.addAttribute("teamtype", strTeam);
//        model.addAttribute("team", team);
        return "vote";
    }
    @GetMapping("/vote/save")
    public String voteSave(HttpServletRequest request, @RequestParam String teamType, @RequestParam Long eventId,
                           @RequestParam Long eventType){

//        RunEvent runevent = runEventRepository.findById(eventId).orElse(null);
//        LocalDateTime startTime = runevent.getStartDateTime();
//        int vTime = runevent.getEvent().getTriggerTime();
//        LocalDateTime endTime = startTime.plusMinutes(vTime);
//        LocalDateTime nowTime = LocalDateTime.now();
//        if(nowTime.isAfter(endTime))
//        {
//            String referer = request.getHeader("Referer");
//            return "redirect:"+ referer;
//        }
//
//        int voteCount = 1;
//        if(teamType.equalsIgnoreCase("1") || teamType.equalsIgnoreCase("3"))
//            voteCount = 3;
//
//        if(teamType.equalsIgnoreCase("0") || teamType.equalsIgnoreCase("1"))
//        {
//            Integer homeCount = runevent.getHomeCount();
//            int total = voteCount;
//            if(homeCount != null)
//                total += homeCount;
//            runevent.setHomeCount(total);
//
//            if(eventType == 1)
//            {
//                Integer home1 = runevent.getHome1Count();
//                int tot1 = voteCount;
//                if(home1 != null)
//                    tot1 += home1;
//                runevent.setHome1Count(tot1);
//            }
//            else if(eventType == 2)
//            {
//                Integer home2 = runevent.getHome2Count();
//                int tot2 = voteCount;
//                if(home2 != null)
//                    tot2 += home2;
//                runevent.setHome2Count(tot2);
//            }
//
//        }
//        else
//        {
//            Integer awayCount = runevent.getAwayCount();
//            int total = voteCount;
//            if(awayCount != null)
//                total += awayCount;
//            runevent.setAwayCount(total);
//
//            if(eventType == 1)
//            {
//                Integer away = runevent.getAway1Count();
//                int tot = voteCount;
//                if(away != null)
//                    tot += away;
//                runevent.setAway1Count(tot);
//            }
//            else if(eventType == 2)
//            {
//                Integer away = runevent.getAway2Count();
//                int tot = voteCount;
//                if(away != null)
//                    tot += away;
//                runevent.setAway2Count(tot);
//            }
//
//        }
//        runEventRepository.save(runevent);

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





    //////////////////////////
    private RunEventWebDto updateRunEventMusic(RunEventWebDto runEventDto, Long eventId)
    {
        List<Object[]> objects = eventMusicRepository.findAllEventMusic(eventId);
        List<EventMusicDto> eventMusicDtos = objects.stream()
                .map(x -> new EventMusicDto(((BigInteger)(x[0])).longValue(), ((BigInteger)(x[1])).longValue(),
                        (String)x[2], (Integer)x[3], (String)x[4],(String)x[5], (String)x[6]))
                .collect(Collectors.toList());

        ArrayList<String> homeList = new ArrayList<>();
        ArrayList<String> homeYoutube = new ArrayList<>();
        ArrayList<String> awayList = new ArrayList<>();
        ArrayList<String> awayYoutube = new ArrayList<>();
        for(EventMusicDto music : eventMusicDtos){
            if(music.getTeamType() == TeamType.TEAM_HOME)
            {
                homeList.add(removeExtension(music.getMusicName()));
                homeYoutube.add(music.getMusicYoutube());

            }
            else if(music.getTeamType() == TeamType.TEAM_AWAY)
            {
                awayList.add(removeExtension(music.getMusicName()));
                awayYoutube.add(music.getMusicYoutube());
            }
        }

        runEventDto.setHomeTitles(homeList);
        runEventDto.setHomeYoutube(homeYoutube);
        runEventDto.setAwayTitles(awayList);
        runEventDto.setAwayYoutube(awayYoutube);


//        for(EventMusicDto music : eventMusicDtos){
//            if(music.getTeamType() == TeamType.TEAM_HOME)
//            {
//                switch (music.getSequence())
//                {
//                    case 0 :
//                        runEventDto.setHome1Name(removeExtension(music.getMusicName()));
//                        break;
//                    case 1 :
//                        runEventDto.setHome2Name(removeExtension(music.getMusicName()));
//                        break;
//                    case 2 :
//                        runEventDto.setHome3Name(removeExtension(music.getMusicName()));
//                        break;
//                    case 3 :
//                        runEventDto.setHome4Name(removeExtension(music.getMusicName()));
//                        break;
//                    case 4 :
//                        runEventDto.setHome5Name(removeExtension(music.getMusicName()));
//                        break;
//                }
//            }
//            else if(music.getTeamType() == TeamType.TEAM_AWAY)
//            {
//                switch (music.getSequence())
//                {
//                    case 0 :
//                        runEventDto.setAway1Name(removeExtension(music.getMusicName()));
//                        break;
//                    case 1 :
//                        runEventDto.setAway2Name(removeExtension(music.getMusicName()));
//                        break;
//                    case 2 :
//                        runEventDto.setAway3Name(removeExtension(music.getMusicName()));
//                        break;
//                    case 3 :
//                        runEventDto.setAway4Name(removeExtension(music.getMusicName()));
//                        break;
//                    case 4 :
//                        runEventDto.setAway5Name(removeExtension(music.getMusicName()));
//                        break;
//                }
//            }
//        }
        return runEventDto;
    }


    private String removeExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex != -1) {
            fileName = fileName.substring(0, lastIndex);
        }
        return fileName;
    }




    @GetMapping("/voteresult")
    public String voteresult(Model model,  @RequestParam Long event_id,
                       @RequestParam ( required = false) Integer move){
        Event event = eventRepository.findById(event_id).orElseThrow(EntityNotFoundException::new);
        RunEvent runevent = runEventRepository.findFirstByEventOrderByIdDesc(event).orElseThrow(EntityNotFoundException::new);

        RunEventWebDto runEventDto = RunEventWebDto.builder()
                .id(runevent.getId())
                .eventId(runevent.getEvent().getId())
                .eventState(runevent.getEventState())
                .startDateTime(runevent.getStartDateTime())
                .triggerType(runevent.getEvent().getTriggerType())
                .triggerTime(runevent.getEvent().getTriggerTime())
                .triggerVote(runevent.getEvent().getTriggerVote())
                .homeCount(runevent.getHomeCount())
                .awayCount(runevent.getAwayCount())
                .webUrl(event.getWebUrl())
                .openchatUrl(event.getOpenchatUrl())
                .build();

//        List<EventMusic> eventMusicList =  eventMusicRepository.findAllByEventOrderBySequenceAsc(event);

        runEventDto = updateRunEventMusic(runEventDto, event.getId());

        String bgImage = "";
        List<Object[]> tops = null;
        String btColor;
        String fontColor;
        if(getIntValue(runevent.getHomeCount()) >= getIntValue(runevent.getAwayCount()))
        {
            bgImage = eventImageRepository.findTypeEventImage(event_id, "IMAGE_HOME");
            tops = runEventRepository.findHomeTopCounts(runevent.getId(), runevent.getEvent().getId());
            btColor = event.getHomeColor();
            fontColor = event.getHomeFont();
        }
        else
        {
            bgImage = eventImageRepository.findTypeEventImage(event_id, "IMAGE_AWAY");
            tops = runEventRepository.findAwayTopCounts(runevent.getId(), runevent.getEvent().getId());
            btColor = event.getAwayColor();
            fontColor = event.getAwayFont();
        }

        List<EventTopDto> eventTopDtos = tops.stream()
                .map(x -> new EventTopDto(((Integer)(x[0])).intValue(), (String)x[1], ((Integer)(x[2])).intValue()))
                .collect(Collectors.toList());


        LocalDateTime startTime = runevent.getStartDateTime();
        int vTime = runEventDto.getTriggerTime();
        LocalDateTime endTime = startTime.plusSeconds(vTime);

        LocalDateTime nowTime = LocalDateTime.now();
        if(nowTime.isAfter(endTime))
        {
            model.addAttribute("state", "STOP");
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


        //bgImage = "https://lh3.googleusercontent.com/drive-viewer/AKGpihYYrUREeok3BOYgpR_kdlLX4MhYkeEVIjVM6UlDkhWlY86tCtknoo_2bgBWHnQ5DiyBGAnnuYdJN9uZ7LLCRw0rw06fsPWdwg=s2560";
        model.addAttribute("event", event);
        model.addAttribute("runevent", runEventDto);
        model.addAttribute("homeColor", "#"+event.getHomeColor());
        model.addAttribute("homeFont", "#"+event.getHomeFont());
        model.addAttribute("awayColor", "#"+event.getAwayColor());
        model.addAttribute("awayFont", "#"+event.getAwayFont());

        model.addAttribute("btColor", "#"+btColor);
        model.addAttribute("fontColor", "#"+fontColor);
        model.addAttribute("bgimg", bgImage);
        model.addAttribute("bgcolor", "#"+event.getEventBkcolor());
        model.addAttribute("openchatimg", event.getOpenchatImg());
        model.addAttribute("webimg", event.getWebImg());
        model.addAttribute("eventTopDtos", eventTopDtos);


        return "voteresult";
    }

    private int getIntValue(Integer obj){
        if(obj == null)
            return 0;
        else
            return obj.intValue();
    }
}
