package com.thiscat.stadiumamp.rest;

import com.thiscat.stadiumamp.dto.*;
import com.thiscat.stadiumamp.dto.request.*;
import com.thiscat.stadiumamp.entity.*;
import com.thiscat.stadiumamp.entity.repository.*;
import com.thiscat.stadiumamp.rest.service.RestService;
import com.thiscat.stadiumamp.system.common.ApiResultResponse;
import com.thiscat.stadiumamp.system.common.ApiResultWithValue;
import com.thiscat.stadiumamp.system.common.InputRequestBodyWriteFile;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class RestApiController extends BaseController{
    @Autowired
    RestService restService;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private EventMusicRepository eventMusicRepository;
    @Autowired
    private EventImageRepository eventImageRepository;
    @Autowired
    private RunEventRepository runEventRepository;


    Logger logger = LoggerFactory.getLogger(RestApiController.class);

    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> saveEvent(@RequestBody ReqAddEvent event) throws Exception {
        eventRepository.save(Event.builder()
                        .name(event.getName())
                        .homeName(event.getHoemName())
                        .awayName(event.getAwayName())
                        .build());

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/modify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> modifyEvent(@RequestBody ReqModifyEvent event) throws Exception {
        Event findEvent = eventRepository.findById(event.getEventId()).orElseThrow(() -> new Exception("event-not-fount"));
        findEvent.setHomeName(event.getHomeName());
        findEvent.setAwayName(event.getAwayName());
        findEvent.setName(event.getEventName());

        eventRepository.save(findEvent);
        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Modify Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> updateEvent(@RequestBody UpdateEventDto eventDto) throws Exception {
        Event event = eventRepository.findById(eventDto.getEventId()).orElseThrow(() -> new Exception("event-not-found"));
        event.setContinuityTime(eventDto.getContinuityTime());
        event.setContinuityType(eventDto.getContinuityType());
        event.setTriggerType(eventDto.getTriggerType());
        event.setTriggerVote(eventDto.getTriggerVote());
        event.setTriggerTime(eventDto.getTriggerTime());
        event.setWebUrl(eventDto.getWebUrl());
        eventRepository.save(event);

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> deleteEvent(@RequestBody EventDto eventDto) throws Exception {

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Event List")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/server/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getEventList() throws Exception {
        List<Event> result = eventRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<EventDto> eventDtos = result.stream()
                .map(x -> EventDto.builder()
                        .eventId(x.getId())
                        .eventName(x.getName())
                        .triggerType(x.getTriggerType())
                        .triggerTime(x.getTriggerTime())
                        .triggerVote(x.getTriggerVote())
                        .webUrl(x.getWebUrl())
                        .continuityType(x.getContinuityType())
                        .continuityTime(x.getContinuityTime())
                        .build())
                .collect(Collectors.toList());
        return getResponseEntity(eventDtos, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Event music file Upload")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/upload-music", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> uploadEventMusic(
            @ApiParam("디바이스 시리얼넘버")
            @RequestParam("event id") String id,
            @ApiParam("멀티파트 파일")
            @RequestParam(value = "file") MultipartFile[] files) throws Exception {

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Event information")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/add-music", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> addServerMusic(@RequestBody ArrayList<MusicDto> musics) throws Exception {
        List<Music> listMusic = musics
                .stream()
                .map(x -> {
                    return Music.builder()
                            .musicUrl(x.getMusicUrl())
                            .musicName(x.getMusicName())
                            .build();
                })
                .collect(Collectors.toList());

        musicRepository.saveAll(listMusic);

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Set Event information")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/update-music", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> updateEventImage(@RequestBody MusicDto musicDto) throws Exception {
        Music music = musicRepository.findById(musicDto.getId()).orElse(null);
        music.setMusicUrl(musicDto.getMusicUrl());
        music.setMusicName(musicDto.getMusicName());

        musicRepository.save(music);


        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Music list")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/server/musiclist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getMusicList() throws Exception {
        List<Music> music = musicRepository.findAll();
        return getResponseEntity( music, "success", HttpStatus.OK);
    }


    @ApiOperation(value = "Event information")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/add-image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> addServerImage(@RequestBody ArrayList<ImageDto> images) throws Exception {
        List<Image> listImage = images
                .stream()
                .map(x -> {
                    return Image.builder()
                            .imageUrl(x.getImageUrl())
                            .imageName(x.getImageName())
                            .build();
                })
                .collect(Collectors.toList());
        imageRepository.saveAll(listImage);

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Set Event information")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/update-image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> updateEventImage(@RequestBody ImageDto imageDto) throws Exception {
        Image image = imageRepository.findById(imageDto.getId()).orElse(null);
        image.setImageUrl(imageDto.getImageUrl());
        image.setImageName(imageDto.getImageName());
        imageRepository.save(image);


        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Image list")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/server/imagelist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getImageList() throws Exception {
        List<Image> images = imageRepository.findAll();
        return getResponseEntity( images, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Set Event music")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/set-event-music", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> setEventMusic(@RequestBody ReqEventMusics reqEventMusics) throws Exception {
        Event event = eventRepository.findById(reqEventMusics.getEventId()).orElseThrow(()->new Exception("event-not-found"));

        List<EventMusic> eventMusicList = reqEventMusics.getEventMusics()
                .stream()
                .map(x -> {
                    Music music = musicRepository.findById(x.getMusicId()).orElse(null);
                    return EventMusic.builder()
                            .event(event)
                            .music(music)
                            .sequence(x.getSequence())
                            .teamType(x.getTeamType())
                            .build();
                })
                .collect(Collectors.toList());

        eventMusicRepository.saveAll(eventMusicList);

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Set Event information")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/set-event-image", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> setEventImage(@RequestBody ReqEventImages reqEventImages) throws Exception {
        Event event = eventRepository.findById(reqEventImages.getEventId()).orElseThrow(()->new Exception("event-not-found"));
        List<EventImage> eventImages = reqEventImages.getEventImages()
                .stream()
                .map(x -> {
                    Image image = imageRepository.findById(x.getImageId()).orElse(null);
                    return EventImage.builder()
                            .event(event)
                            .image(image)
                            .imageType(x.getImageType())
                            .build();

                })
                .collect(Collectors.toList());
        eventImageRepository.saveAll(eventImages);
        return getResponseEntity( "success", HttpStatus.OK);
    }



    @ApiOperation(value = "Event Info")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/server/event-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getEventInfo(@RequestParam Long eventId) throws Exception {
        Event event = eventRepository.findById(eventId).orElseThrow(()->new Exception("event-not-found"));
        EventDto eventDto = restService.getEventInfo(event);
        return getResponseEntity(eventDto, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Start Event")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/event/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> startEvent(@RequestBody EventDto eventDto) throws Exception {
        RunEventDto runEventDto = restService.startEvent(eventDto);
        return getResponseEntity( runEventDto, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Event Stop")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/stop", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> stopEvent(@RequestParam Long runEventId) throws Exception {
        RunEventDto runEventDto = restService.stopEvent(runEventId);
        return getResponseEntity( runEventDto, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "RunEvent State")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/runstate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getRunEventState(@RequestParam Long runEventId) throws Exception {
        RunEvent runEvent = runEventRepository.findById(runEventId).orElseThrow(() -> new Exception("runevent-not-fount"));

//        Long evtId = runEventRepository.findEventId(runEventId);
//        Long eid = (Long)evtId;
//        Event event = eventRepository.findById(eid).orElseThrow(() -> new Exception("runevent-not-fount"));

        RunEventDto runEventDto = RunEventDto.builder()
                .id(runEvent.getId())
                .eventId(runEvent.getEvent().getId())
                .eventState(runEvent.getEventState())
                .serverName(runEvent.getEvent().getName())
                .homeName(runEvent.getEvent().getHomeName())
                .awayName(runEvent.getEvent().getAwayName())
                .startDateTime(runEvent.getStartDateTime())
                .triggerType(runEvent.getEvent().getTriggerType())
                .triggerTime(runEvent.getEvent().getTriggerTime())
                .triggerVote(runEvent.getEvent().getTriggerVote())
                .continuityTime(runEvent.getEvent().getContinuityTime())
                .continuityType(runEvent.getEvent().getContinuityType())
                .homeCount(runEvent.getHomeCount())
                .home1Count(runEvent.getHome1Count())
                .home2Count(runEvent.getHome2Count())
                .home3Count(runEvent.getHome3Count())
                .home4Count(runEvent.getHome4Count())
                .home5Count(runEvent.getHome5Count())
                .awayCount(runEvent.getAwayCount())
                .away1Count(runEvent.getAway1Count())
                .away2Count(runEvent.getAway2Count())
                .away3Count(runEvent.getAway3Count())
                .away4Count(runEvent.getAway4Count())
                .away5Count(runEvent.getAway5Count())
                .build();
        return getResponseEntity( runEventDto, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "RunEvent State")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/runscorestate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getRunScoreEventState(@RequestParam Long runEventId) throws Exception {
        RunEvent runEvent = runEventRepository.findById(runEventId).orElseThrow(() -> new Exception("runevent-not-fount"));
        RunEventDto runEventDto = RunEventDto.builder()
                .id(runEvent.getId())
                .eventId(runEvent.getEvent().getId())
                .eventState(runEvent.getEventState())
                .startDateTime(runEvent.getStartDateTime())
                .triggerType(runEvent.getEvent().getTriggerType())
                .triggerTime(runEvent.getEvent().getTriggerTime())
                .triggerVote(runEvent.getEvent().getTriggerVote())
                .homeCount(runEvent.getHomeCount())
                .home1Count(runEvent.getHome1Count())
                .home2Count(runEvent.getHome2Count())
                .home3Count(runEvent.getHome3Count())
                .home4Count(runEvent.getHome4Count())
                .home5Count(runEvent.getHome5Count())
                .awayCount(runEvent.getAwayCount())
                .away1Count(runEvent.getAway1Count())
                .away2Count(runEvent.getAway2Count())
                .away3Count(runEvent.getAway3Count())
                .away4Count(runEvent.getAway4Count())
                .away5Count(runEvent.getAway5Count())
                .build();
        return getResponseEntity( runEventDto, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Last Event")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/eventdto", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getLastEventDto(@RequestParam Long serverId) throws Exception {
        Event event = eventRepository.findById(serverId).orElseThrow(EntityNotFoundException::new);
        RunEvent runevent = runEventRepository.findFirstByEventOrderByIdDesc(event).orElseThrow(EntityNotFoundException::new);
        VoteResultDto voteResultDto = getVoteResult(runevent);
        if(event.getTriggerType() == 0)
        {
            LocalDateTime startTime = runevent.getStartDateTime();
            int vTime = event.getTriggerTime();
            LocalDateTime endTime = startTime.plusSeconds(vTime);

            LocalDateTime nowTime = LocalDateTime.now();
            if(nowTime.isAfter(endTime) || runevent.getEventState().equalsIgnoreCase("STOP"))
            {
                voteResultDto.setEventState("이벤트 종료");
                LocalDateTime finishTime = runevent.getEndDateTime();
                Duration duration = Duration.between(finishTime, nowTime);
                long sec = duration.getSeconds();
                if(sec > 30)
                    voteResultDto.setPlayVideo(false);
                else
                    voteResultDto.setPlayVideo(true);
            }
            else {
                Duration duration = Duration.between(nowTime, endTime);
                long sec = duration.getSeconds();
                long minV = sec / 60;
                long secV = sec % 60;
                voteResultDto.setEventState("현재 응원 이벤트 남은 시간 " + minV + "분" + secV + "초");
                System.out.println("--------Sec : " +sec + " con : " + vTime);
                System.out.println("--------Sec : " +sec + " con : " + vTime);
                System.out.println("--------Sec : " +sec + " con : " + vTime);
                if(sec > vTime-1)
                    voteResultDto.setStart(true);
                else
                    voteResultDto.setStart(false);
            }
        }
        else
        {
            if(runevent.getEventState().equalsIgnoreCase("STOP"))
            {
                voteResultDto.setEventState("이벤트 종료");
//                LocalDateTime finishTime = runevent.getEndDateTime();
//                Duration duration = Duration.between(finishTime, nowTime);
//                long sec = duration.getSeconds();
//                if(sec > 30)
//                    voteResultDto.setPlayVideo(false);
//                else
//                    voteResultDto.setPlayVideo(true);
            }
            else
            {
                int triVote = event.getTriggerVote();
                int remainCount = triVote - 0;
                int homeCount = 0;
                int awayCount = 0;
                if(runevent.getHomeCount() != null)
                    homeCount = runevent.getHomeCount();
                if(runevent.getAwayCount() != null)
                    awayCount = runevent.getAwayCount();

                if(homeCount > awayCount)
                    remainCount -= homeCount;
                else
                    remainCount -= awayCount;

                voteResultDto.setEventState("현재 남은 투표 : " + remainCount );
            }
        }


        voteResultDto.setHomeName(event.getHomeName());
        voteResultDto.setAwayName(event.getAwayName());

        //VoteResultDto voteResultDto = new VoteResultDto("95%", "5%");
        return getResponseEntity(voteResultDto, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Last Event")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/last-event", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getLastEvent(@RequestParam Long serverId) throws Exception {
        Event event = eventRepository.findById(serverId).orElseThrow(EntityNotFoundException::new);
        RunEvent runEvent = runEventRepository.findFirstByEventOrderByIdDesc(event).orElseThrow(EntityNotFoundException::new);
        RunEventDto runEventDto = RunEventDto.builder()
                .id(runEvent.getId())
                .eventId(runEvent.getEvent().getId())
                .eventState(runEvent.getEventState())
                .serverName(runEvent.getEvent().getName())
                .homeName(runEvent.getEvent().getHomeName())
                .awayName(runEvent.getEvent().getAwayName())
                .startDateTime(runEvent.getStartDateTime())
                .triggerType(runEvent.getEvent().getTriggerType())
                .triggerTime(runEvent.getEvent().getTriggerTime())
                .triggerVote(runEvent.getEvent().getTriggerVote())
                .homeCount(runEvent.getHomeCount())
                .home1Count(runEvent.getHome1Count())
                .home2Count(runEvent.getHome2Count())
                .home3Count(runEvent.getHome3Count())
                .home4Count(runEvent.getHome4Count())
                .home5Count(runEvent.getHome5Count())
                .awayCount(runEvent.getAwayCount())
                .away1Count(runEvent.getAway1Count())
                .away2Count(runEvent.getAway2Count())
                .away3Count(runEvent.getAway3Count())
                .away4Count(runEvent.getAway4Count())
                .away5Count(runEvent.getAway5Count())
                .build();
        return getResponseEntity( runEventDto, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Event List")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/vote/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> voteSave(@RequestParam String teamType,
                                                       @RequestParam Long eventId, @RequestParam Long eventType) throws Exception
    {
        // 종료 타입이 득표일 경우 여기서 종료 체크
        //RunEvent runEvent = runEventRepository.findById(eventId).orElseThrow(() -> new Exception("runevent-not-fount"));
        VoteResultDto voteResultDto = restService.voteSave(eventId, teamType, eventType);
        return getResponseEntity(voteResultDto, "success", HttpStatus.OK);

//        if(runEvent.getEvent().getTriggerType() == 0)
//        {
//            LocalDateTime startTime = runEvent.getStartDateTime();
//            int vTime = runEvent.getEvent().getTriggerTime();
//            LocalDateTime endTime = startTime.plusSeconds(vTime);
//            LocalDateTime nowTime = LocalDateTime.now();
//            if(nowTime.isAfter(endTime) || runEvent.getEventState().equalsIgnoreCase("STOP"))
//            {
//                VoteResultDto voteResultDto = getVoteResult(runEvent);
//                voteResultDto.setEventState("STOP");
//                return getResponseEntity(voteResultDto, "success", HttpStatus.OK);
//            }
//            else
//            {
//                RunEvent resultEvent = restService.updateVoteCount(runEvent, teamType, eventType);
//                VoteResultDto voteResultDto =getVoteResult(resultEvent);
//                voteResultDto.setEventState("START");
//                return getResponseEntity(voteResultDto, "success", HttpStatus.OK);
//            }
//        }
//        else
//        { // vote count 로 종료
//            int triVote = runEvent.getEvent().getTriggerVote();
//            int homeCount = 0;
//            int awayCount = 0;
//            if(runEvent.getHomeCount() != null)
//                homeCount = runEvent.getHomeCount();
//            if(runEvent.getAwayCount() != null)
//                awayCount = runEvent.getAwayCount();
//
//            if(triVote < homeCount || triVote < awayCount)
//            {
//                VoteResultDto voteResultDto = getVoteResult(runEvent);
//                voteResultDto.setEventState("STOP");
//                return getResponseEntity(voteResultDto, "success", HttpStatus.OK);
//            }
//            else
//            {
//                RunEvent resultEvent = restService.updateVoteCount(runEvent, teamType, eventType);
//                if(runEvent.getHomeCount() != null)
//                    homeCount = runEvent.getHomeCount();
//                if(runEvent.getAwayCount() != null)
//                    awayCount = runEvent.getAwayCount();
//
//                VoteResultDto voteResultDto = getVoteResult(runEvent);
//                if(triVote <= homeCount || triVote <= awayCount)
//                {
//                    RunEventDto runEventDto = restService.stopEvent(resultEvent.getId());
//                    voteResultDto.setEventState("STOP");
//                    return getResponseEntity(voteResultDto, "success", HttpStatus.OK);
//                }
//                else
//                {
//                    voteResultDto.setEventState("START");
//                    return getResponseEntity(voteResultDto, "success", HttpStatus.OK);
//                }
//            }
//        }
    }

    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/update-video", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> updateVideo(@RequestBody VideoDto videoDto) throws Exception {
        Event event = eventRepository.findById(videoDto.getEventId()).orElseThrow(() -> new Exception("event-not-found"));
//        event.setContinuityTime(eventDto.getContinuityTime());
//        event.setContinuityType(eventDto.getContinuityType());
//        event.setTriggerType(eventDto.getTriggerType());
//        event.setTriggerVote(eventDto.getTriggerVote());
//        event.setTriggerTime(eventDto.getTriggerTime());
//        event.setWebUrl(eventDto.getWebUrl());
        eventRepository.save(event);

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/update-web", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> updateWebUrl(@RequestBody WebDto webDto) throws Exception {
        Event event = eventRepository.findById(webDto.getEventId()).orElseThrow(() -> new Exception("event-not-found"));
        event.setWebUrl(webDto.getWebUrl());
        eventRepository.save(event);

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/team-color", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> setTeamColor(@RequestBody TeamColorDto teamColorDto) throws Exception {
        Event event = eventRepository.findById(teamColorDto.getEventId()).orElseThrow(() -> new Exception("event-not-found"));
        event.setHomeColor(teamColorDto.getHomeColor());
        event.setHomeFont(teamColorDto.getHomeFont());
        event.setAwayColor(teamColorDto.getAwayColor());
        event.setAwayFont(teamColorDto.getAwayFont());
        eventRepository.save(event);

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/openchatUrl", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> setOpenchatUrl(@RequestBody OpenchatUrlDto openchatUrlDto) throws Exception {
        Event event = eventRepository.findById(openchatUrlDto.getEventId()).orElseThrow(() -> new Exception("event-not-found"));
        event.setOpenchatUrl(openchatUrlDto.getOpenchatUrl());
        eventRepository.save(event);

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/continuityType", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> setcontinuityType(@RequestBody ReqContinuityType reqContinuityType) throws Exception {
        Event event = eventRepository.findById(reqContinuityType.getEventId()).orElseThrow(() -> new Exception("event-not-found"));
        event.setContinuityType(reqContinuityType.getContinuityType());
        eventRepository.save(event);

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Volume Update")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/set-volume", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> volumeSave( @RequestParam Long eventId, @RequestParam Integer volume) throws Exception
    {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new Exception("event-not-found"));
        event.setVolumeValue(volume);
        eventRepository.save(event);
        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Volume Update")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/set-bgcolor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> setBgColor( @RequestParam Long eventId, @RequestParam String color) throws Exception
    {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new Exception("event-not-found"));
        event.setEventBkcolor(color);
        eventRepository.save(event);
        return getResponseEntity( "success", HttpStatus.OK);
    }






    private VoteResultDto getVoteResult(RunEvent runevent)
    {
        int homeCount = 0;
        int awayCount = 0;
        if(runevent.getHomeCount() != null)
            homeCount = runevent.getHomeCount();
        if(runevent.getAwayCount() != null)
            awayCount = runevent.getAwayCount();

        int total = homeCount + awayCount;
        if(total == 0){
            return new VoteResultDto("50%", "50%", homeCount, awayCount);
        }

        int home = (homeCount*100)/ total;
        int away = (awayCount*100) / total;
        VoteResultDto voteResultDto = new VoteResultDto(home+"%", away+"%", homeCount, awayCount);
        voteResultDto.setHomeName(runevent.getEvent().getHomeName());
        voteResultDto.setAwayName(runevent.getEvent().getAwayName());
        return voteResultDto;
    }
    /////////// timer task





    public String getFileNameFromURL(String url)
    {
        //https://www.dropbox.com/scl/fi/iygp8yuspiek3bjs8yzns/.mp3?rlkey=ax0uwqk7jie57zj4mxsutj492&dl=0
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }
}
