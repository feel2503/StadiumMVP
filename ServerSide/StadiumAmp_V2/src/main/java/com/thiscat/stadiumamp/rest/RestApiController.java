package com.thiscat.stadiumamp.rest;

import com.thiscat.stadiumamp.dto.*;
import com.thiscat.stadiumamp.dto.request.*;
import com.thiscat.stadiumamp.entity.*;
import com.thiscat.stadiumamp.entity.repository.*;
import com.thiscat.stadiumamp.entity.value.TeamType;
import com.thiscat.stadiumamp.rest.service.RestService;
import com.thiscat.stadiumamp.system.common.ApiResultResponse;
import com.thiscat.stadiumamp.system.common.ApiResultWithValue;
import com.thiscat.stadiumamp.system.common.InputRequestBodyWriteFile;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private CheertagRepository cheertagRepository;
    @Autowired
    private DefeventRepository defeventRepository;


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
                            .youtubeUrl(x.getYoutubeUrl())
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
        music.setYoutubeUrl(musicDto.getYoutubeUrl());

        musicRepository.save(music);


        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Set Event information")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/update-music-youtube", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> updateMusicYoutube(@RequestBody MusicYoutubeDto musicYoutubeDto) throws Exception {
        if(musicYoutubeDto.getMusicArrayList() != null)
        {
            ArrayList<Music> musicArryaList = new ArrayList<>();

            for(MusicYoutube musicYoutube: musicYoutubeDto.getMusicArrayList())
            {
                Music music = musicRepository.findById(musicYoutube.getId()).orElse(null);
                music.setYoutubeUrl(musicYoutube.getYoutubeUrl());
                musicArryaList.add(music);
            }
            musicRepository.saveAll(musicArryaList);
        }


        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Music list")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/server/musiclist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getMusicList() throws Exception {
        List<Music> music = musicRepository.findAll(Sort.by("id").ascending());
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
        List<Image> images = imageRepository.findAll(Sort.by("id").ascending());
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
        RunEvent runEvent = runEventRepository.findById(runEventId).orElseThrow(() -> new Exception("runevent-not-fount"));
        RunEventDto runEventDto = restService.stopEvent(runEvent);

        Event event = runEvent.getEvent();
        event.setAutoRunState(0);
        eventRepository.save(event);

        return getResponseEntity( runEventDto, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Event Stop")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/stop-lastevent", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> stopLastEvent(@RequestParam Long eventId) throws Exception {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new Exception("runevent-not-fount"));
        RunEvent runEvent = runEventRepository.findFirstByEventOrderByIdDesc(event).orElseThrow(EntityNotFoundException::new);

        RunEventDto runEventDto = restService.stopEvent(runEvent);
        event.setAutoRunState(0);
        eventRepository.save(event);

        return getResponseEntity( runEventDto, "success", HttpStatus.OK);

//        if(runEvent.getEventState().equalsIgnoreCase("START")) {
//            RunEventDto runEventDto = restService.stopEvent(runEvent);
//            event.setAutoRunState(0);
//            eventRepository.save(event);
//            return getResponseEntity( runEventDto, "success", HttpStatus.OK);
//        }
//        else {
//            RunEventDto runEventDto = restService.startEvent(eventId);
//            return getResponseEntity( runEventDto, "success", HttpStatus.OK);
//        }

    }

    @ApiOperation(value = "RunEvent State")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/runstate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getRunEventState(@RequestParam Long runEventId) throws Exception {
        RunEvent runEvent = runEventRepository.findById(runEventId).orElseThrow(() -> new Exception("runevent-not-fount"));

//        Long evtId = runEventRepository.findEventId(runEventId);
//        Long eid = (Long)evtId;
//        Event event = eventRepository.findById(eid).orElseThrow(() -> new Exception("runevent-not-fount"));

        RunEventDto runEventDto = getRunEventDto(runEvent);

        return getResponseEntity( runEventDto, "success", HttpStatus.OK);
    }

    @ApiOperation(value = "RunEvent State")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/runscorestate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getRunScoreEventState(@RequestParam Long runEventId) throws Exception {
        RunEvent runEvent = runEventRepository.findById(runEventId).orElseThrow(() -> new Exception("runevent-not-fount"));
        RunEventDto runEventDto = getRunEventDto(runEvent);

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
        RunEventDto runEventDto = getRunEventDto(runEvent);

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

    @ApiOperation(value = "Event List")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/vote/save-tag", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> voteSave2(@RequestBody VoteDto voteDto) throws Exception
    {

        VoteResultDto voteResultDto = restService.voteSave(voteDto);
        return getResponseEntity(voteResultDto, "success", HttpStatus.OK);
        //return getResponseEntity( "success", HttpStatus.OK);
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
    @PostMapping(value = "/v1/server/update-web-img", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> updateWebImg(@RequestBody WebImgDto webImgDto) throws Exception {
        Event event = eventRepository.findById(webImgDto.getEventId()).orElseThrow(() -> new Exception("event-not-found"));
        event.setWebImg(webImgDto.getWebImg());
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
    @PostMapping(value = "/v1/server/openchatImg", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> setOpenchatImg(@RequestBody OpenchatImgDto openchatImgDto) throws Exception {
        Event event = eventRepository.findById(openchatImgDto.getEventId()).orElseThrow(() -> new Exception("event-not-found"));
        event.setOpenchatImg(openchatImgDto.getOpenchatImg());
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

    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v1/event/next-runevent", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getNexRunEvent(@RequestParam Long eventId, @RequestParam Long runEventId) throws Exception {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new Exception("event-not-found"));

        if(event.getAutoRunState() == 0)
        {
            return getResponseEntity( null, "last-event", HttpStatus.OK);
        }
        //RunEvent runEvent = runEventRepository.findFirstByIdGreaterThanAndByEventOrderByIdAsc(runEventId, event).orElse(null);
        RunEvent runEvent = runEventRepository.findNextRunEvent(eventId, runEventId).orElse(null);

        if(runEvent != null)
        {
            RunEventDto result = getRunEventDto(runEvent);

            List<Object[]> objects = eventMusicRepository.findAllEventMusic(event.getId());
            List<EventMusicDto> eventMusicDtos = objects.stream()
                    .map(x -> new EventMusicDto(((BigInteger)(x[0])).longValue(), ((BigInteger)(x[1])).longValue(),
                            (String)x[2], (Integer)x[3], (String)x[4],(String)x[5], (String)x[6]))
                    .collect(Collectors.toList());
            result.setEventMusicList(new ArrayList<>(eventMusicDtos));

            return getResponseEntity( result, "success", HttpStatus.OK);
        }
        else {
            return getResponseEntity( null, "last-event", HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/donaitonURL", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> setCheerUrl(@RequestBody CheerUrlDto cheerUrlDto) throws Exception {
        Event event = eventRepository.findById(cheerUrlDto.getEventId()).orElseThrow(() -> new Exception("event-not-found"));
        event.setCheerUrl1(cheerUrlDto.getCheerUrl1());
        event.setCheerUrl2(cheerUrlDto.getCheerUrl2());
        eventRepository.save(event);

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @Transactional
    @ApiOperation(value = "Set Event information")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v1/server/tag", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> setCheerTag(@RequestBody TagDto tagDto) throws Exception {
        Event event = eventRepository.findById(tagDto.getEventId()).orElseThrow(() -> new Exception("event-not-found"));

        ArrayList<String> tagList = tagDto.getTagList();
        if(tagList != null && tagList.size() > 0)
        {
            cheertagRepository.deleteAllByEvent(event);

            ArrayList<Cheertag> cheertags = new ArrayList<>();
            for(int i = 0; i < tagList.size(); i++ )
            {
                String tagStr = tagList.get(i);
                String tagId = "tag"+i;
                Cheertag cheertag = Cheertag.builder()
                        .event(event)
                        .tag_id(tagId)
                        .value(tagStr)
                        .label(tagStr)
                        .build();

                cheertags.add(cheertag);
            }
            cheertagRepository.saveAll(cheertags);
        }

        return getResponseEntity( "success", HttpStatus.OK);
    }




    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v2/server/externalURL1", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> setExternalUrl1(@RequestBody OpenchatDto openchatDto) throws Exception {
        Event event = eventRepository.findById(openchatDto.getEventId()).orElseThrow(() -> new Exception("event-not-found"));
        event.setOpenchatImg(openchatDto.getOpenchatImg());
        event.setOpenchatUrl(openchatDto.getOpenchatUrl());

        eventRepository.save(event);

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v2/server/externalURL2", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> setExternalUrl2(@RequestBody External2Dto external2Dto) throws Exception {
        Event event = eventRepository.findById(external2Dto.getEventId()).orElseThrow(() -> new Exception("event-not-found"));
        event.setWebUrl(external2Dto.getWebUrl());
        event.setWebImg(external2Dto.getWebImg());

        eventRepository.save(event);

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @Transactional
    @ApiOperation(value = "Set Event music")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v2/server/set-event-music", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> setEventMusic2(@RequestBody ReqEventMusics2 reqEventMusics) throws Exception {
        Event event = eventRepository.findById(reqEventMusics.getEventId()).orElseThrow(()->new Exception("event-not-found"));
        if(reqEventMusics.getHomeMusics() != null || reqEventMusics.getAwayMusics() != null)
        {
            eventMusicRepository.deleteAllByEvent(event);
        }
        if(reqEventMusics.getHomeMusics() != null && reqEventMusics.getHomeMusics().size() > 0)
        {
            ArrayList<EventMusic> homeMusics = new ArrayList<>();
            for(int i = 0; i < reqEventMusics.getHomeMusics().size(); i++)
            {
//                EventMusicDto2 eventMusicDto2 = reqEventMusics.getHomeMusics().get(i);
//                Music music = musicRepository.findById(eventMusicDto2.getMusicId()).orElse(null);
                Long eventMusicDto2 = reqEventMusics.getHomeMusics().get(i);
                Music music = musicRepository.findById(eventMusicDto2).orElse(null);
                EventMusic eventMusic = EventMusic.builder()
                        .event(event)
                        .music(music)
                        .sequence(i)
                        .teamType(TeamType.TEAM_HOME)
                        .build();

                homeMusics.add(eventMusic);
            }
            eventMusicRepository.saveAll(homeMusics);
        }

        if(reqEventMusics.getAwayMusics() != null && reqEventMusics.getAwayMusics().size() > 0)
        {
            ArrayList<EventMusic> awayMusics = new ArrayList<>();
            for(int i = 0; i < reqEventMusics.getAwayMusics().size(); i++)
            {
//                EventMusicDto2 eventMusicDto2 = reqEventMusics.getAwayMusics().get(i);
//                Music music = musicRepository.findById(eventMusicDto2.getMusicId()).orElse(null);
                Long eventMusicDto2 = reqEventMusics.getAwayMusics().get(i);
                Music music = musicRepository.findById(eventMusicDto2).orElse(null);
                EventMusic eventMusic = EventMusic.builder()
                        .event(event)
                        .music(music)
                        .sequence(i)
                        .teamType(TeamType.TEAM_AWAY)
                        .build();

                awayMusics.add(eventMusic);
            }
            eventMusicRepository.saveAll(awayMusics);
        }

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v2/server/count-effect", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> setExternalUrl2(@RequestBody CountEffectDto countEffectDto) throws Exception {
        Event event = eventRepository.findById(countEffectDto.getEventId()).orElseThrow(() -> new Exception("event-not-found"));

        event.setAnimationCount(countEffectDto.getAnimationCount());
        event.setAnimationColor(countEffectDto.getAnimationColor());
        event.setEmoji(countEffectDto.getEmoji());

        eventRepository.save(event);

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping(value = "/v2/server/sync-volume", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultResponse> setSyncVolume(@RequestBody VolumeDto volumeDto) throws Exception {
        Event event = eventRepository.findById(volumeDto.getEventId()).orElseThrow(() -> new Exception("event-not-found"));
        int value = volumeDto.getValue();
        if(value > 15)
            value = 15;
        else if(value < -1)
            value = -1;

        event.setVolumeValue(value);

        eventRepository.save(event);

        return getResponseEntity( "success", HttpStatus.OK);
    }

    @ApiOperation(value = "Add Event Server")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping(value = "/v2/server/get-volume", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResultWithValue> getSyncVolume(@RequestParam Long eventId) throws Exception {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new Exception("event-not-found"));
        VolumeDto volumeDto = new VolumeDto(eventId, event.getVolumeValue());

        return getResponseEntity( volumeDto, "success", HttpStatus.OK);
    }

    @GetMapping("/v2/eventResult")
    public HashMap<String, Object> eventResult(@RequestParam Long eventId){

        // 데이터를 담아 페이지로 보내기 위해 Model 자료형을 인자로
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException("No value present"));
        List<RunEvent> runEventList = runEventRepository.findByEvent(event);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<RunEventDto> runEventDtoList= runEventList
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
                .result(getResult(x))
                .volumeValue(x.getEvent().getVolumeValue())
                .volumeSync(x.getEvent().getVolumeSync())
                .build())
            .collect(Collectors.toList());


        //HashMap<String,Object> result = new HashMap<>();
        HashMap<String,Object> gameInfoMap = new HashMap<>();

        gameInfoMap.put("id", event.getId());
        gameInfoMap.put("name", event.getName());
        gameInfoMap.put("homeName", event.getName());
        gameInfoMap.put("awayName", event.getAwayName());
        gameInfoMap.put("triggerType", event.getTriggerType());
        gameInfoMap.put("triggerTime", event.getTriggerTime());
        gameInfoMap.put("triggerVote", event.getTriggerVote());
        gameInfoMap.put("continuityType", event.getContinuityType());
        gameInfoMap.put("continuityTime", event.getContinuityTime());
        gameInfoMap.put("eventList", runEventDtoList);


        //result.put("gameInfo", gameInfoMap);
        //result.put("eventList", runEventDtoList);

       return gameInfoMap;
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


    private RunEventDto getRunEventDto(RunEvent runEvent)
    {
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
                .volumeValue(runEvent.getEvent().getVolumeValue())
                .volumeSync(runEvent.getEvent().getVolumeSync())
                .homeCount(runEvent.getHomeCount())
                .home1Count(runEvent.getHome1Count())
                .home2Count(runEvent.getHome2Count())
                .home3Count(runEvent.getHome3Count())
                .home4Count(runEvent.getHome4Count())
                .home5Count(runEvent.getHome5Count())
                .home6Count(runEvent.getHome6Count())
                .home7Count(runEvent.getHome7Count())
                .home8Count(runEvent.getHome8Count())
                .home9Count(runEvent.getHome9Count())
                .home10Count(runEvent.getHome10Count())
                .home11Count(runEvent.getHome11Count())
                .home12Count(runEvent.getHome12Count())
                .home13Count(runEvent.getHome13Count())
                .home14Count(runEvent.getHome14Count())
                .home15Count(runEvent.getHome15Count())
                .home16Count(runEvent.getHome16Count())
                .home17Count(runEvent.getHome17Count())
                .home18Count(runEvent.getHome18Count())
                .home19Count(runEvent.getHome19Count())
                .home20Count(runEvent.getHome20Count())
                .awayCount(runEvent.getAwayCount())
                .away1Count(runEvent.getAway1Count())
                .away2Count(runEvent.getAway2Count())
                .away3Count(runEvent.getAway3Count())
                .away4Count(runEvent.getAway4Count())
                .away5Count(runEvent.getAway5Count())
                .away6Count(runEvent.getAway6Count())
                .away7Count(runEvent.getAway7Count())
                .away8Count(runEvent.getAway8Count())
                .away9Count(runEvent.getAway9Count())
                .away10Count(runEvent.getAway10Count())
                .away11Count(runEvent.getAway11Count())
                .away12Count(runEvent.getAway12Count())
                .away13Count(runEvent.getAway13Count())
                .away14Count(runEvent.getAway14Count())
                .away15Count(runEvent.getAway15Count())
                .away16Count(runEvent.getAway16Count())
                .away17Count(runEvent.getAway17Count())
                .away18Count(runEvent.getAway18Count())
                .away19Count(runEvent.getAway19Count())
                .away20Count(runEvent.getAway20Count())
                .build();

        return runEventDto;
    }


    public String getFileNameFromURL(String url)
    {
        //https://www.dropbox.com/scl/fi/iygp8yuspiek3bjs8yzns/.mp3?rlkey=ax0uwqk7jie57zj4mxsutj492&dl=0
        return url.substring(url.lastIndexOf('/') + 1, url.length());
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

    public String getResult(RunEvent x)
    {
        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("home1", x.getHome1Count() != null ? x.getHome1Count() : 0);
        counts.put("home2", x.getHome2Count() != null ? x.getHome2Count() : 0);
        counts.put("home3", x.getHome3Count() != null ? x.getHome3Count() : 0);
        counts.put("home4", x.getHome4Count() != null ? x.getHome4Count() : 0);
        counts.put("home5", x.getHome5Count() != null ? x.getHome5Count() : 0);
        counts.put("home6", x.getHome6Count() != null ? x.getHome6Count() : 0);
        counts.put("home7", x.getHome7Count() != null ? x.getHome7Count() : 0);
        counts.put("home8", x.getHome8Count() != null ? x.getHome8Count() : 0);
        counts.put("home9", x.getHome9Count() != null ? x.getHome9Count() : 0);
        counts.put("home10", x.getHome10Count() != null ? x.getHome10Count() : 0);
        counts.put("home11", x.getHome11Count() != null ? x.getHome11Count() : 0);
        counts.put("home12", x.getHome12Count() != null ? x.getHome12Count() : 0);
        counts.put("home13", x.getHome13Count() != null ? x.getHome13Count() : 0);
        counts.put("home14", x.getHome14Count() != null ? x.getHome14Count() : 0);
        counts.put("home15", x.getHome15Count() != null ? x.getHome15Count() : 0);
        counts.put("home16", x.getHome16Count() != null ? x.getHome16Count() : 0);
        counts.put("home17", x.getHome17Count() != null ? x.getHome17Count() : 0);
        counts.put("home18", x.getHome18Count() != null ? x.getHome18Count() : 0);
        counts.put("home19", x.getHome19Count() != null ? x.getHome19Count() : 0);
        counts.put("home20", x.getHome20Count() != null ? x.getHome20Count() : 0);
        counts.put("away1", x.getAway1Count() != null ? x.getAway1Count() : 0);
        counts.put("away2", x.getAway2Count() != null ? x.getAway2Count() : 0);
        counts.put("away3", x.getAway3Count() != null ? x.getAway3Count() : 0);
        counts.put("away4", x.getAway4Count() != null ? x.getAway4Count() : 0);
        counts.put("away5", x.getAway5Count() != null ? x.getAway5Count() : 0);
        counts.put("away6", x.getAway6Count() != null ? x.getAway6Count() : 0);
        counts.put("away7", x.getAway7Count() != null ? x.getAway7Count() : 0);
        counts.put("away8", x.getAway8Count() != null ? x.getAway8Count() : 0);
        counts.put("away9", x.getAway9Count() != null ? x.getAway9Count() : 0);
        counts.put("away10", x.getAway10Count() != null ? x.getAway10Count() : 0);
        counts.put("away11", x.getAway11Count() != null ? x.getAway11Count() : 0);
        counts.put("away12", x.getAway12Count() != null ? x.getAway12Count() : 0);
        counts.put("away13", x.getAway13Count() != null ? x.getAway13Count() : 0);
        counts.put("away14", x.getAway14Count() != null ? x.getAway14Count() : 0);
        counts.put("away15", x.getAway15Count() != null ? x.getAway15Count() : 0);
        counts.put("away16", x.getAway16Count() != null ? x.getAway16Count() : 0);
        counts.put("away17", x.getAway17Count() != null ? x.getAway17Count() : 0);
        counts.put("away18", x.getAway18Count() != null ? x.getAway18Count() : 0);
        counts.put("away19", x.getAway19Count() != null ? x.getAway19Count() : 0);
        counts.put("away20", x.getAway20Count() != null ? x.getAway20Count() : 0);


        Map.Entry<String, Integer> resultMap =  counts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .orElseThrow(() -> new NoSuchElementException("No max value found"));

        String result = resultMap.getKey();
        return result;
    }
}
