package kr.co.thiscat.stadiumamp.rest;

import io.swagger.annotations.ApiOperation;
import kr.co.thiscat.stadiumamp.dto.EventNowResultDto;
import kr.co.thiscat.stadiumamp.dto.RunEventDto;
import kr.co.thiscat.stadiumamp.dto.RunEventStartDto;
import kr.co.thiscat.stadiumamp.dto.StadiumServerDto;
import kr.co.thiscat.stadiumamp.entity.Runevent;
import kr.co.thiscat.stadiumamp.entity.Stadiumserver;
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
    public ResponseEntity<ApiResultWithValue> updateEvent(@RequestBody StadiumServerDto stadiumServerDto) throws Exception {
        Stadiumserver stadiumserver = stadiumServerRepository.findById(stadiumServerDto.getId()).orElseThrow(EntityNotFoundException::new);
        stadiumserver.setAwayImage(stadiumServerDto.getAwayImage());
        stadiumserver.setAwayMusic1(stadiumServerDto.getAwayMusic1());
        stadiumserver.setAwayMusic2(stadiumServerDto.getAwayMusic2());
        stadiumserver.setHomeImage(stadiumServerDto.getHomeImage());
        stadiumserver.setHomeMusic1(stadiumServerDto.getHomeMusic1());
        stadiumserver.setHomeMusic2(stadiumServerDto.getHomeMusic2());
        stadiumserver.setDefaultImage(stadiumServerDto.getDefaultImage());
        stadiumserver.setDefaultMusic(stadiumServerDto.getDefaultMusic());

        stadiumServerRepository.save(stadiumserver);
        return getResponseEntity("success", "success", HttpStatus.OK);
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

    @ApiOperation(value = "Event List")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/server/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getEventList() throws Exception {
        List<Stadiumserver> result = stadiumServerRepository.findAll();
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

        stadiumserver.setDefaultMusic(runEvent.getDefaultMusic());
        stadiumserver.setHomeMusic1(runEvent.getHomeMusic1());
        stadiumserver.setHomeMusic2(runEvent.getHomeMusic2());
        stadiumserver.setAwayMusic1(runEvent.getAwayMusic1());
        stadiumserver.setAwayMusic2(runEvent.getAwayMusic2());
        stadiumserver.setDefaultImage(runEvent.getDefaultImage());
        stadiumserver.setHomeImage(runEvent.getHomeImage());
        stadiumserver.setAwayImage(runEvent.getAwayImage());
        stadiumServerRepository.save(stadiumserver);

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
