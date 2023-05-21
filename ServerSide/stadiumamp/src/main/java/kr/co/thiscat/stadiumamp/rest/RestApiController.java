package kr.co.thiscat.stadiumamp.rest;

import io.swagger.annotations.ApiOperation;
import kr.co.thiscat.stadiumamp.dto.*;
import kr.co.thiscat.stadiumamp.entity.Entertainment;
import kr.co.thiscat.stadiumamp.entity.Runevent;
import kr.co.thiscat.stadiumamp.entity.Stadiumserver;
import kr.co.thiscat.stadiumamp.entity.repository.EntertainmentRepository;
import kr.co.thiscat.stadiumamp.entity.repository.RunEventRepository;
import kr.co.thiscat.stadiumamp.entity.repository.StadiumServerRepository;
import kr.co.thiscat.stadiumamp.system.common.ApiResultWithValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class RestApiController extends BaseController{
    @Autowired
    StadiumServerRepository stadiumServerRepository;
    @Autowired
    RunEventRepository runEventRepository;
    @Autowired
    EntertainmentRepository entertainmentRepository;

    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> saveEvent(@RequestBody Stadiumserver eventServer) throws Exception {
        stadiumServerRepository.save(Stadiumserver.builder()
                .name(eventServer.getName())
                .build());
        return getResponseEntity("success", "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> updateEvent(@RequestBody EntertainmentDto entertainmentDto) throws Exception {
        Stadiumserver stadiumserver = stadiumServerRepository.findById(entertainmentDto.getServerId()).orElseThrow(EntityNotFoundException::new);
        Entertainment entertainment = entertainmentRepository.findByServerAndSsaid(entertainmentDto.getSsaid(), entertainmentDto.getServerId()).orElse(new Entertainment());

        entertainment.setStadiumserver(stadiumserver);
        entertainment.setSsaid(entertainmentDto.getSsaid());
        entertainment.setAwayImage(entertainmentDto.getAwayImage());
        entertainment.setAwayMusic1(entertainmentDto.getAwayMusic1());
        entertainment.setAwayMusic2(entertainmentDto.getAwayMusic2());
        entertainment.setHomeImage(entertainmentDto.getHomeImage());
        entertainment.setHomeMusic1(entertainmentDto.getHomeMusic1());
        entertainment.setHomeMusic2(entertainmentDto.getHomeMusic2());
        entertainment.setDefaultImage(entertainmentDto.getDefaultImage());
        entertainment.setDefaultMusic(entertainmentDto.getDefaultMusic());
        entertainment.setWebUrl(entertainmentDto.getWebUrl());

        entertainmentRepository.save(entertainment);
        return getResponseEntity("success", "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Last Event")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/event-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getEventInfo(@RequestParam Long serverId, String ssaid) throws Exception {
        Stadiumserver stadiumserver = stadiumServerRepository.findById(serverId).orElseThrow(EntityNotFoundException::new);
        Runevent runevent = runEventRepository.findFirstByStadiumserverOrderByIdDesc(stadiumserver).orElseThrow(EntityNotFoundException::new);
        Entertainment entertainment = entertainmentRepository.findByServerAndSsaid(ssaid, serverId).orElse(null);

        if(runevent != null)
        {
            EventInfoDto eventInfoDto = EventInfoDto.builder()
                    .stadiumServerId(serverId)
                    .voteTime(runevent.getVoteTime())
                    .resultTime(runevent.getResultTime())
                    .homeCount(runevent.getHomeCount())
                    .awayCount(runevent.getAwayCount())
                    .eventState(runevent.getEventState())
                    .startDateTime(runevent.getStartDateTime())
                    .defaultMusic(entertainment.getDefaultMusic())
                    .homeMusic1(entertainment.getHomeMusic1())
                    .homeMusic2(entertainment.getHomeMusic2())
                    .awayMusic1(entertainment.getAwayMusic1())
                    .awayMusic2(entertainment.getAwayMusic2())
                    .defaultImage(entertainment.getDefaultImage())
                    .homeImage(entertainment.getHomeImage())
                    .awayImage(entertainment.getAwayImage())
                    .webUrl(entertainment.getWebUrl())
                    .build();
            return getResponseEntity(eventInfoDto, "success", HttpStatus.OK);
        }
        return getResponseEntity(null, "success", HttpStatus.OK);
    }


    @ApiOperation(value = "Server info")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/server/server", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getServr(@RequestParam Long serverId) throws Exception {
        Stadiumserver stadiumserver = stadiumServerRepository.findById(serverId).orElseThrow(EntityNotFoundException::new);
        StadiumServerDto stadiumServerDto = StadiumServerDto.builder()
                .awayImage(stadiumserver.getAwayImage())
                .awayMusic1(stadiumserver.getAwayMusic1())
                .awayMusic2(stadiumserver.getAwayMusic2())
                .defaultImage(stadiumserver.getDefaultImage())
                .defaultMusic(stadiumserver.getDefaultMusic())
                .homeImage(stadiumserver.getHomeImage())
                .homeMusic1(stadiumserver.getHomeMusic1())
                .homeMusic2(stadiumserver.getHomeMusic2())
                .build();
        ArrayList<Stadiumserver> result = new ArrayList<>();
        result.add(stadiumserver);
        return getResponseEntity(result, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Server List")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/server/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getEventList() throws Exception {
        List<Stadiumserver> result = stadiumServerRepository.findAllByOrderByIdAsc();
        return getResponseEntity(result, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Event List")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/nowstate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getNowState(@RequestParam Long eventId) throws Exception {
        Runevent runevent = runEventRepository.findById(eventId).orElseThrow(EntityNotFoundException::new);
        runevent.getStadiumserver().getName();
        EventNowResultDto result = EventNowResultDto.builder()
                .serverName(runevent.getStadiumserver().getName())
                .eventState(runevent.getEventState())
                .homeCount(runevent.getHomeCount())
                .awayCount(runevent.getAwayCount())
                .build();
        return getResponseEntity(result, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Last Event")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/last-event", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getLastEvent(@RequestParam Long serverId) throws Exception {
        Stadiumserver stadiumserver = stadiumServerRepository.findById(serverId).orElseThrow(EntityNotFoundException::new);
        Runevent runevent = runEventRepository.findFirstByStadiumserverOrderByIdDesc(stadiumserver).orElseThrow(EntityNotFoundException::new);

        if(runevent != null)
        {
            RunEventDto runEventDto = RunEventDto.builder()
                    .id(runevent.getId())
                    .stadiumServerId(runevent.getStadiumserver().getId())
                    .voteTime(runevent.getVoteTime())
                    .resultTime(runevent.getResultTime())
                    .homeCount(runevent.getHomeCount())
                    .home1Count(runevent.getHome1Count())
                    .home2Count(runevent.getHome2Count())
                    .awayCount(runevent.getAwayCount())
                    .away1Count(runevent.getAway1Count())
                    .away2Count(runevent.getAway2Count())
                    .eventState(runevent.getEventState())
                    .startDateTime(runevent.getStartDateTime())
                    .build();
            return getResponseEntity(runEventDto, "success", HttpStatus.OK);
        }
        return getResponseEntity(null, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Last Event")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/eventdto", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getLastEventDto(@RequestParam Long serverId) throws Exception {
        Stadiumserver stadiumserver = stadiumServerRepository.findById(serverId).orElseThrow(EntityNotFoundException::new);
        Runevent runevent = runEventRepository.findFirstByStadiumserverOrderByIdDesc(stadiumserver).orElseThrow(EntityNotFoundException::new);

        VoteResultDto voteResultDto =getVoteResult(runevent);
        //VoteResultDto voteResultDto = new VoteResultDto("95%", "5%");
        return getResponseEntity(voteResultDto, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Event State")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/state", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getEventState(@RequestParam Long eventId) throws Exception {
        Runevent runevent = runEventRepository.findById(eventId).orElseThrow(EntityNotFoundException::new);
        Stadiumserver stadiumserver = stadiumServerRepository.findById(runevent.getStadiumserver().getId()).orElseThrow(EntityNotFoundException::new);

        RunEventDto runEventDto = RunEventDto.builder()
                .id(runevent.getId())
                .stadiumServerId(runevent.getStadiumserver().getId())
                .voteTime(runevent.getVoteTime())
                .resultTime(runevent.getResultTime())
                .homeCount(runevent.getHomeCount())
                .home1Count(runevent.getHome1Count())
                .home2Count(runevent.getHome2Count())
                .awayCount(runevent.getAwayCount())
                .away1Count(runevent.getAway1Count())
                .away2Count(runevent.getAway2Count())
                .eventState(runevent.getEventState())
                .defaultMusic(stadiumserver.getDefaultMusic())
                .homeImg(stadiumserver.getHomeImage())
                .awayImg(stadiumserver.getAwayImage())
                .defaultImg(stadiumserver.getDefaultImage())
                .startDateTime(runevent.getStartDateTime())
                .endDateTime(runevent.getEndDateTime())
                .build();

        return getResponseEntity(runEventDto, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Event List")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/event/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> startEvent(@RequestBody RunEventStartDto runEvent) throws Exception {
        Stadiumserver stadiumserver = stadiumServerRepository.findById(runEvent.getStadiumServerId())
                .orElseThrow(() -> new Exception("Event Server Not found"));

        Entertainment entertainment = entertainmentRepository.findByServerAndSsaid(runEvent.getSsaid(), runEvent.getStadiumServerId()).orElse(new Entertainment());

        entertainment.setStadiumserver(stadiumserver);
        entertainment.setSsaid(runEvent.getSsaid());
        entertainment.setAwayImage(runEvent.getAwayImage());
        entertainment.setAwayMusic1(runEvent.getAwayMusic1());
        entertainment.setAwayMusic2(runEvent.getAwayMusic2());
        entertainment.setHomeImage(runEvent.getHomeImage());
        entertainment.setHomeMusic1(runEvent.getHomeMusic1());
        entertainment.setHomeMusic2(runEvent.getHomeMusic2());
        entertainment.setDefaultImage(runEvent.getDefaultImage());
        entertainment.setDefaultMusic(runEvent.getDefaultMusic());
        entertainment.setWebUrl(runEvent.getWebUrl());

        entertainmentRepository.save(entertainment);

//        stadiumserver.setDefaultMusic(runEvent.getDefaultMusic());
//        stadiumserver.setHomeMusic1(runEvent.getHomeMusic1());
//        stadiumserver.setHomeMusic2(runEvent.getHomeMusic2());
//        stadiumserver.setAwayMusic1(runEvent.getAwayMusic1());
//        stadiumserver.setAwayMusic2(runEvent.getAwayMusic2());
//        stadiumserver.setDefaultImage(runEvent.getDefaultImage());
//        stadiumserver.setHomeImage(runEvent.getHomeImage());
//        stadiumserver.setAwayImage(runEvent.getAwayImage());
//        stadiumServerRepository.save(stadiumserver);

        LocalDateTime startDateTime = LocalDateTime.now();
        Runevent runevent = Runevent.builder()
                .stadiumserver(stadiumserver)
                .voteTime(runEvent.getVoteTime())
                .resultTime(runEvent.getResultTime())
                .eventState("START")
                .startDateTime(startDateTime)
                .build();

        Runevent saveResult = runEventRepository.save(runevent);
        runEventRepository.flush();

        long stopTime = 60 * 1000 * runEvent.getVoteTime();
        EventStateTimer eventStateTimer = new EventStateTimer(saveResult.getId());
        Timer timer = new Timer();
        timer.schedule(eventStateTimer, stopTime );

        RunEventDto runEventDto = RunEventDto.builder()
                .id(saveResult.getId())
                .stadiumServerId(saveResult.getStadiumserver().getId())
                .voteTime(saveResult.getVoteTime())
                .resultTime(saveResult.getResultTime())
                .homeCount(saveResult.getHomeCount())
                .awayCount(saveResult.getAwayCount())
                .eventState(saveResult.getEventState())
                .startDateTime(saveResult.getStartDateTime())
                .build();

        return getResponseEntity(runEventDto, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Event Stop")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/stop", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> stopEvent(@RequestParam Long eventId) throws Exception {
        Runevent runevent = runEventRepository.findById(eventId).orElse(null);
        if(runevent != null)
        {
            LocalDateTime endDateTime = LocalDateTime.now();
            runevent.setEndDateTime(endDateTime);
            runevent.setEventState("STOP");
        }
        Runevent saveResult = runEventRepository.save(runevent);
        RunEventDto runEventDto = RunEventDto.builder()
                .id(saveResult.getId())
                .stadiumServerId(saveResult.getStadiumserver().getId())
                .voteTime(saveResult.getVoteTime())
                .resultTime(saveResult.getResultTime())
                .homeCount(saveResult.getHomeCount())
                .awayCount(saveResult.getAwayCount())
                .eventState(saveResult.getEventState())
                .startDateTime(saveResult.getStartDateTime())
                .build();
        return getResponseEntity(runEventDto, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Event List")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/result", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getEventResult(@RequestParam Long eventId) throws Exception {
        Runevent runevent = runEventRepository.findById(eventId).orElse(null);

        return getResponseEntity(runevent, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Event List")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/vote", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> voteEvent(@RequestParam String teamType,
                                                        @RequestParam String type, @RequestParam Long eventId) throws Exception {
        Runevent runevent = runEventRepository.findById(eventId).orElse(null);
        int voteCount = 1;
        if(type.equalsIgnoreCase("MVP"))
            voteCount = 3;

        if(teamType.equalsIgnoreCase("HOME"))
        {
            int homeCount = runevent.getHomeCount();
            runevent.setHomeCount(homeCount + voteCount);
        }
        else if(teamType.equalsIgnoreCase("AWAY"))
        {
            int awayConunt = runevent.getAwayCount();
            runevent.setAwayCount(awayConunt + voteCount);
        }
        runEventRepository.save(runevent);

        return getResponseEntity("success", "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Event List")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/vote/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> voteSave(@RequestParam String teamType,
                                                        @RequestParam Long eventId, @RequestParam Long eventType) throws Exception {


        Runevent runevent = runEventRepository.findById(eventId).orElse(null);
        LocalDateTime startTime = runevent.getStartDateTime();
        int vTime = runevent.getVoteTime();
        LocalDateTime endTime = startTime.plusMinutes(vTime);
        LocalDateTime nowTime = LocalDateTime.now();
        if(nowTime.isAfter(endTime))
        {
            VoteResultDto voteResultDto = new VoteResultDto("50%", "50%", 0, 0);
            return getResponseEntity(voteResultDto, "success", HttpStatus.OK);
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

        VoteResultDto voteResultDto =getVoteResult(runevent);
        //VoteResultDto voteResultDto = new VoteResultDto("95%", "5%");
        return getResponseEntity(voteResultDto, "success", HttpStatus.OK);
    }

    private VoteResultDto getVoteResult(Runevent runevent)
    {
        int homeCount = 0;
        int awayCount = 0;
        if(runevent.getHomeCount() != null)
            homeCount = runevent.getHomeCount();
        if(runevent.getAwayCount() != null)
            awayCount = runevent.getAwayCount();

        int total = homeCount + awayCount;
        int home = (homeCount*100)/ total;
        int away = (awayCount*100) / total;
        return new VoteResultDto(home+"%", away+"%", homeCount, awayCount);
    }
    /////////// timer task

    class EventStateTimer extends TimerTask
    {
        long eventId;
        public EventStateTimer(long eventId)
        {
            this.eventId = eventId;
        }

        @Override
        public void run() {
            Calendar nowTime = Calendar.getInstance();
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strNowTime = sd.format(nowTime.getTime());

            Runevent runevent = runEventRepository.findById(eventId).orElse(null);
            if(runevent != null && runevent.getEventState().equalsIgnoreCase("START"))
            {
                LocalDateTime endDateTime = LocalDateTime.now();
                runevent.setEndDateTime(endDateTime);
                runevent.setEventState("STOP");
                runEventRepository.save(runevent);

                strNowTime = runevent.getEdtDateTime().toString();
            }
            System.out.println("[ " + strNowTime + " ] : " + eventId + " STOP");
        }
    }
}
