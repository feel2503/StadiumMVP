package com.thiscat.stadiumamp.controller;

import com.thiscat.stadiumamp.dao.TagDao;
import com.thiscat.stadiumamp.dto.EventMusicDto;
import com.thiscat.stadiumamp.dto.EventStatisticsDto;
import com.thiscat.stadiumamp.dto.EventTopDto;
import com.thiscat.stadiumamp.dto.RunEventWebDto;
import com.thiscat.stadiumamp.entity.Cheertag;
import com.thiscat.stadiumamp.entity.Event;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class VoteHistoryController {
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



    @GetMapping("/votehistory")
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

        String qrImage = eventImageRepository.findTypeEventImage(event_id, "IMAGE_QR");

        List<EventStatisticsDto> eventStatisticsDtos = runEventRepository.findTop5EventStatistics(event_id);

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

        model.addAttribute("qrImage", qrImage);
        model.addAttribute("eventStatisticsDtos", eventStatisticsDtos);

        return "votehistory";
    }

    private int getIntValue(Integer obj){
        if(obj == null)
            return 0;
        else
            return obj.intValue();
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


        return runEventDto;
    }

    private String removeExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex != -1) {
            fileName = fileName.substring(0, lastIndex);
        }
        return fileName;
    }
}
