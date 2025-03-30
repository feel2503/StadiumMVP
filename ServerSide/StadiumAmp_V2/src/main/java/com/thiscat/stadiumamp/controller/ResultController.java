package com.thiscat.stadiumamp.controller;


import com.thiscat.stadiumamp.dto.RunEventDto;
import com.thiscat.stadiumamp.entity.Event;
import com.thiscat.stadiumamp.entity.RunEvent;
import com.thiscat.stadiumamp.entity.repository.EventRepository;
import com.thiscat.stadiumamp.entity.repository.RunEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class ResultController {
    @Autowired
    EventRepository eventRepository;
    @Autowired
    RunEventRepository runEventRepository;

    @GetMapping("/result")
    public String boardList(Model model, @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        // 데이터를 담아 페이지로 보내기 위해 Model 자료형을 인자로

        List<Event> stadiumservers = eventRepository.findAllByOrderByIdAsc();

        Page<RunEvent> runEvents = runEventRepository.findAll(pageable);

        int nowPage = runEvents.getPageable().getPageNumber() + 1; // 현재 페이지를 가져옴 , 0에서 시작하기에 처리를 위해 + 1
        int startPage = Math.max(nowPage - 4, 1); // Math.max(a, b) -- a 와 b 중 큰 값을 반환 --> 그냥 nowPAge - 4만 하면 nowpage가 1인 경우 -3도 가능하기에 이를 방지하기 위함
        int endPage = Math.min(nowPage + 5, runEvents.getTotalPages()); // totalPage보다 크면 안되기에 두개 중 최소값 반환하는 Math.min을 사용


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<RunEventDto> runEventDtos = runEvents
                .stream()
                .map(x->RunEventDto.builder()
                        .id(x.getId())
                        .eventId(x.getEvent().getId())
                        .strStartDateTime(x.getStartDateTime() != null ? x.getStartDateTime().format(formatter) : "")
                        .strEndDateTime(x.getEndDateTime() != null ? x.getEndDateTime().format(formatter): "")
                        .duration(duration(x.getStartDateTime(), x.getEndDateTime()))
                        .eventState(x.getEventState())
                        .homeCount(x.getHomeCount())
                        .home1Count(x.getHome1Count())
                        .home2Count(x.getHome2Count())
                        .home3Count(x.getHome3Count())
                        .home4Count(x.getHome4Count())
                        .home5Count(x.getHome5Count())
                        .home6Count(x.getHome6Count())
                        .home7Count(x.getHome7Count())
                        .home8Count(x.getHome8Count())
                        .home9Count(x.getHome9Count())
                        .home10Count(x.getHome10Count())
                        .home11Count(x.getHome11Count())
                        .home12Count(x.getHome12Count())
                        .home13Count(x.getHome13Count())
                        .home14Count(x.getHome14Count())
                        .home15Count(x.getHome15Count())
                        .home16Count(x.getHome16Count())
                        .home17Count(x.getHome17Count())
                        .home18Count(x.getHome18Count())
                        .home19Count(x.getHome19Count())
                        .home20Count(x.getHome20Count())
                        .awayCount(x.getAwayCount())
                        .away1Count(x.getAway1Count())
                        .away2Count(x.getAway2Count())
                        .away3Count(x.getAway3Count())
                        .away4Count(x.getAway4Count())
                        .away5Count(x.getAway5Count())
                        .away6Count(x.getAway6Count())
                        .away7Count(x.getAway7Count())
                        .away8Count(x.getAway8Count())
                        .away9Count(x.getAway9Count())
                        .away10Count(x.getAway10Count())
                        .away11Count(x.getAway11Count())
                        .away12Count(x.getAway12Count())
                        .away13Count(x.getAway13Count())
                        .away14Count(x.getAway14Count())
                        .away15Count(x.getAway15Count())
                        .away16Count(x.getAway16Count())
                        .away17Count(x.getAway17Count())
                        .away18Count(x.getAway18Count())
                        .away19Count(x.getAway19Count())
                        .away20Count(x.getAway20Count())
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

        PageImpl<RunEventDto> pageImpl = new PageImpl<RunEventDto>(runEventDtos, runEvents.getPageable(), runEvents.getTotalElements());

        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("server", stadiumservers);
        model.addAttribute("event", pageImpl);


        return "result";
    }

    @GetMapping("/result_old")
    public String home(Model model){
        List<Event> stadiumservers = eventRepository.findAllByOrderByIdAsc();
        List<RunEvent> runevents = runEventRepository.findAllByOrderByIdAsc();
        //List<RunEvent> runevents = runEventRepository.findTop30ByOrderByIdDesc();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<RunEventDto> runEventDtos = runevents
                .stream()
                .map(x->RunEventDto.builder()
                        .id(x.getId())
                        .eventId(x.getEvent().getId())
                        .startDateTime(x.getStartDateTime())
                        .endDateTime(x.getEndDateTime())
                        .duration(duration(x.getStartDateTime(), x.getEndDateTime()))
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

    @GetMapping("/result/view")
    public String resultView(Model model, Long id) throws Exception{

        List<Event> stadiumservers = eventRepository.findAllByOrderByIdAsc();
        //Optional<RunEvent> runevent = runEventRepository.findById(id);
        RunEvent x = runEventRepository.findById(id).orElseThrow(() -> new Exception("res-target-notfoune"));

        RunEventDto runEventDto = RunEventDto.builder()
                .id(x.getId())
                .homeName(x.getEvent().getHomeName())
                .awayName(x.getEvent().getAwayName())
                .eventId(x.getEvent().getId())
                .startDateTime(x.getStartDateTime())
                .endDateTime(x.getEndDateTime())
                .duration(duration(x.getStartDateTime(), x.getEndDateTime()))
                .eventState(x.getEventState())
                .homeCount(x.getHomeCount())
                .home1Count(x.getHome1Count())
                .home2Count(x.getHome2Count())
                .home3Count(x.getHome3Count())
                .home4Count(x.getHome4Count())
                .home5Count(x.getHome5Count())
                .home6Count(x.getHome6Count())
                .home7Count(x.getHome7Count())
                .home8Count(x.getHome8Count())
                .home9Count(x.getHome9Count())
                .home10Count(x.getHome10Count())
                .home11Count(x.getHome11Count())
                .home12Count(x.getHome12Count())
                .home13Count(x.getHome13Count())
                .home14Count(x.getHome14Count())
                .home15Count(x.getHome15Count())
                .home16Count(x.getHome16Count())
                .home17Count(x.getHome17Count())
                .home18Count(x.getHome18Count())
                .home19Count(x.getHome19Count())
                .home20Count(x.getHome20Count())
                .awayCount(x.getAwayCount())
                .away1Count(x.getAway1Count())
                .away2Count(x.getAway2Count())
                .away3Count(x.getAway3Count())
                .away4Count(x.getAway4Count())
                .away5Count(x.getAway5Count())
                .away6Count(x.getAway6Count())
                .away7Count(x.getAway7Count())
                .away8Count(x.getAway8Count())
                .away9Count(x.getAway9Count())
                .away10Count(x.getAway10Count())
                .away11Count(x.getAway11Count())
                .away12Count(x.getAway12Count())
                .away13Count(x.getAway13Count())
                .away14Count(x.getAway14Count())
                .away15Count(x.getAway15Count())
                .away16Count(x.getAway16Count())
                .away17Count(x.getAway17Count())
                .away18Count(x.getAway18Count())
                .away19Count(x.getAway19Count())
                .away20Count(x.getAway20Count())
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
                .build();

        Map<String, Object> eventMap = new HashMap<>();
        for (int i = 1; i <= 20; i++) {
            try {
                // "homeXCount" 필드 값을 가져오기 (Reflection 사용)
                Object value = RunEventDto.class.getDeclaredMethod("getHome" + i + "Count").invoke(runEventDto);
                eventMap.put("home" + i + "Count", value != null ? value : 0); // null이면 0으로 설정
            } catch (Exception e) {
                eventMap.put("home" + i + "Count", 0); // 예외 발생 시 기본값 0
            }
        }
        // Away 1~20 Count 동적 추가
        for (int i = 1; i <= 20; i++) {
            try {
                Object value = RunEventDto.class.getDeclaredMethod("getAway" + i + "Count").invoke(runEventDto);
                eventMap.put("away" + i + "Count", value != null ? value : 0);
            } catch (Exception e) {
                eventMap.put("away" + i + "Count", 0);
            }
        }

        // 태그 값 추가
        for (int i = 0; i <= 9; i++) {
            try {
                Object value = RunEventDto.class.getDeclaredMethod("getTag" + i).invoke(runEventDto);
                eventMap.put("tag" + i, value != null ? value : 0);
            } catch (Exception e) {
                eventMap.put("tag" + i, 0);
            }
        }

        model.addAttribute("server", stadiumservers);
        model.addAttribute("event", runEventDto);
        model.addAttribute("eventMap", eventMap);

        return "resultview";
    }


    private String duration(LocalDateTime startTime, LocalDateTime endTime)
    {
        if(startTime == null  || endTime == null)
            return "";
        Duration duration = Duration.between(startTime, endTime);

        // 결과 출력 (진행 시간을 시, 분, 초로 표시)
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        String result = "" + hours + "h " + minutes + "m " + seconds + "s";
        return result;
    }
}
