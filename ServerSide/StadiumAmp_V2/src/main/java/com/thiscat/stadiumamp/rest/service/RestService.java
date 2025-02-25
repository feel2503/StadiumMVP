package com.thiscat.stadiumamp.rest.service;

import com.thiscat.stadiumamp.dto.*;
import com.thiscat.stadiumamp.entity.Event;
import com.thiscat.stadiumamp.entity.EventImage;
import com.thiscat.stadiumamp.entity.EventMusic;
import com.thiscat.stadiumamp.entity.RunEvent;
import com.thiscat.stadiumamp.entity.repository.*;
import com.thiscat.stadiumamp.rest.RestApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    EventMusicRepository eventMusicRepository;
    @Autowired
    private EventImageRepository eventImageRepository;
    @Autowired
    RunEventRepository runEventRepository;

    public EventDto getEventInfo(Event event) throws Exception
    {
//        List<EventMusic> eventMusicList =  eventMusicRepository.findAllByEventOrderBySequenceAsc(event);
//        List<EventMusicDto> eventMusicDtoList = eventMusicList
//                .stream()
//                .map(x -> new EventMusicDto(x))
//                .collect(Collectors.toList());
        List<Object[]> objects = eventMusicRepository.findAllEventMusic(event.getId());
        List<EventMusicDto> eventMusicDtos = objects.stream()
                .map(x -> new EventMusicDto(((BigInteger)(x[0])).longValue(), ((BigInteger)(x[1])).longValue(),
                        (String)x[2], (Integer)x[3], (String)x[4],(String)x[5]))
                .collect(Collectors.toList());

        List<EventImage> eventImageList = eventImageRepository.findAllByEventOrderByImageTypeAsc(event);
        List<EventImageDto> eventImageDtoList = eventImageList
                .stream()
                .map(x -> new EventImageDto(x))
                .collect(Collectors.toList());

        EventDto eventDto = EventDto.builder()
                .eventId(event.getId())
                .eventName(event.getName())
                .triggerType(event.getTriggerType())
                .triggerTime(event.getTriggerTime())
                .triggerVote(event.getTriggerVote())
                .webUrl(event.getWebUrl())
                .openchatUrl(event.getOpenchatUrl())
                .continuityTime(event.getContinuityTime())
                .continuityType(event.getContinuityType())
                .eventMusicList(new ArrayList<>(eventMusicDtos))
                .eventImageList(new ArrayList<>(eventImageDtoList))
                .homeColor(event.getHomeColor())
                .homeFont(event.getHomeFont())
                .awayColor(event.getAwayColor())
                .awayFont(event.getAwayFont())
                .cheerUrl1(event.getCheerUrl1())
                .cheerUrl2(event.getCheerUrl2())
                .build();

        RunEvent runEvent = runEventRepository.findByEventLimit(event.getId()).orElse(null);
        if(runEvent != null)
        {
            eventDto.setRunEvent(runEvent.getId());
            eventDto.setEventState(runEvent.getEventState());
        }

        return eventDto;
    }

    public RunEventDto startEvent(Long id) throws Exception
    {
        Event event = eventRepository.findById(id).orElseThrow(()->new Exception("event-not-found"));
        RunEvent lastEvent = runEventRepository.findByEventLimit(event.getId()).orElse(null);
        if(lastEvent != null && lastEvent.getEventState().equalsIgnoreCase("START")){
            throw new Exception("event-is-running");
        }

        Event saveEvent = eventRepository.save(event);

        LocalDateTime startDateTime = LocalDateTime.now();
        RunEvent runEvent = RunEvent.builder()
                .event(saveEvent)
                .startDateTime(startDateTime)
                .eventState("START")
                .build();

        RunEvent saveSaveRunEvent =  runEventRepository.save(runEvent);

        if(saveEvent.getTriggerType() == 0){ // 시간
            long stopTime = 1000 * saveEvent.getTriggerTime();
            EventStateTimer eventStateTimer = new EventStateTimer(event.getId(), saveSaveRunEvent.getId());
            Timer timer = new Timer();
            timer.schedule(eventStateTimer, stopTime );
        }else if(saveEvent.getTriggerType() == 1){  // 득표

        }

        List<Object[]> objects = eventMusicRepository.findAllEventMusic(event.getId());
        List<EventMusicDto> eventMusicDtos = objects.stream()
                .map(x -> new EventMusicDto(((BigInteger)(x[0])).longValue(), ((BigInteger)(x[1])).longValue(),
                        (String)x[2], (Integer)x[3], (String)x[4],(String)x[5]))
                .collect(Collectors.toList());

        List<EventImage> eventImageList = eventImageRepository.findAllByEventOrderByImageTypeAsc(saveEvent);
        List<EventImageDto> eventImageDtoList = eventImageList
                .stream()
                .map(x -> new EventImageDto(x))
                .collect(Collectors.toList());

        RunEventDto runEventDto = RunEventDto.builder()
                .id(saveSaveRunEvent.getId())
                .eventId(saveSaveRunEvent.getEvent().getId())
                .eventState(saveSaveRunEvent.getEventState())
                .triggerType(saveEvent.getTriggerType())
                .triggerTime(saveEvent.getTriggerTime())
                .triggerVote(saveEvent.getTriggerVote())
                .continuityType(saveEvent.getContinuityType())
                .continuityTime(saveEvent.getContinuityTime())
                .webUrl(saveEvent.getWebUrl())
                .openchatUrl(saveEvent.getOpenchatUrl())
                .eventMusicList(new ArrayList<>(eventMusicDtos))
                .eventImageList(new ArrayList<>(eventImageDtoList))
                .build();

        eventRepository.flush();
        runEventRepository.flush();

        return runEventDto;
    }

    public RunEventDto startEvent(EventDto eventDto) throws Exception
    {
        long id = eventDto.getEventId();
        Event event = eventRepository.findById(eventDto.getEventId()).orElseThrow(()->new Exception("event-not-found"));
        RunEvent lastEvent = runEventRepository.findByEventLimit(event.getId()).orElse(null);
        if(lastEvent != null && lastEvent.getEventState().equalsIgnoreCase("START")){
            throw new Exception("event-is-running");
        }

        if(eventDto.getTriggerType() >= 0)
            event.setTriggerType(eventDto.getTriggerType());
        if(eventDto.getTriggerTime() > 0)
            event.setTriggerTime(eventDto.getTriggerTime());
        if(eventDto.getTriggerVote() > 0)
            event.setTriggerVote(eventDto.getTriggerVote());
        if(eventDto.getContinuityType() >= 0)
        {
            event.setContinuityType(eventDto.getContinuityType());
            if(eventDto.getContinuityType() == 1)
            {
                event.setAutoRunState(1);
            }
            else
            {
                event.setAutoRunState(0);
            }
        }
        if(eventDto.getContinuityTime() > 0)
            event.setContinuityTime(eventDto.getContinuityTime());

//        if(eventDto.getVolumeValue() > -1)
//            event.setVolumeValue(event.getVolumeValue());


        Event saveEvent = eventRepository.save(event);

        LocalDateTime startDateTime = LocalDateTime.now();
        RunEvent runEvent = RunEvent.builder()
                .event(saveEvent)
                .startDateTime(startDateTime)
                .eventState("START")
                .build();

        RunEvent saveSaveRunEvent =  runEventRepository.save(runEvent);

        if(saveEvent.getTriggerType() == 0){ // 시간
            long stopTime = 1000 * saveEvent.getTriggerTime();
            EventStateTimer eventStateTimer = new EventStateTimer(event.getId(), saveSaveRunEvent.getId());
            Timer timer = new Timer();
            timer.schedule(eventStateTimer, stopTime );
        }else if(saveEvent.getTriggerType() == 1){  // 득표

        }

//        long stopTime = 1000 * saveEvent.getTriggerTime();
//        EventStateTimer eventStateTimer = new EventStateTimer(event.getId(), saveSaveRunEvent.getId());
//        Timer timer = new Timer();
//        timer.schedule(eventStateTimer, stopTime );



//        List<EventMusic> eventMusicList =  eventMusicRepository.findAllByEventOrderBySequenceAsc(saveEvent);
//        List<EventMusicDto> eventMusicDtoList = eventMusicList
//                .stream()
//                .map(x -> new EventMusicDto(x))
//                .collect(Collectors.toList());

        List<Object[]> objects = eventMusicRepository.findAllEventMusic(event.getId());
        List<EventMusicDto> eventMusicDtos = objects.stream()
                .map(x -> new EventMusicDto(((BigInteger)(x[0])).longValue(), ((BigInteger)(x[1])).longValue(),
                        (String)x[2], (Integer)x[3], (String)x[4],(String)x[5]))
                .collect(Collectors.toList());

        List<EventImage> eventImageList = eventImageRepository.findAllByEventOrderByImageTypeAsc(saveEvent);
        List<EventImageDto> eventImageDtoList = eventImageList
                .stream()
                .map(x -> new EventImageDto(x))
                .collect(Collectors.toList());

        RunEventDto runEventDto = RunEventDto.builder()
                .id(saveSaveRunEvent.getId())
                .eventId(saveSaveRunEvent.getEvent().getId())
                .eventState(saveSaveRunEvent.getEventState())
                .triggerType(saveEvent.getTriggerType())
                .triggerTime(saveEvent.getTriggerTime())
                .triggerVote(saveEvent.getTriggerVote())
                .continuityType(saveEvent.getContinuityType())
                .continuityTime(saveEvent.getContinuityTime())
                .webUrl(saveEvent.getWebUrl())
                .openchatUrl(saveEvent.getOpenchatUrl())
                .eventMusicList(new ArrayList<>(eventMusicDtos))
                .eventImageList(new ArrayList<>(eventImageDtoList))
                .build();

        eventRepository.flush();
        runEventRepository.flush();

        return runEventDto;
    }

    public RunEventDto stopEvent(RunEvent runEvent) throws Exception
    {
        //RunEvent runEvent = runEventRepository.findById(runEventId).orElseThrow(() -> new Exception("runevent-not-fount"));
        runEvent.setEventState("STOP");
        LocalDateTime endDateTime = LocalDateTime.now();
        runEvent.setEndDateTime(endDateTime);
        RunEvent saveSaveRunEvent = runEventRepository.save(runEvent);

//        if(runEvent.getEvent().getTriggerType() == 0
//            && runEvent.getEvent().getContinuityType() == 1){
//            long delayTime = 1000 * runEvent.getEvent().getContinuityTime();
//            EventContinueTimer eventContinueTimer = new EventContinueTimer(saveSaveRunEvent.getEvent().getId());
//            Timer timer = new Timer();
//            timer.schedule(eventContinueTimer, delayTime );
//        }

        RunEventDto runEventDto = RunEventDto.builder()
                .id(saveSaveRunEvent.getId())
                .eventId(saveSaveRunEvent.getEvent().getId())
                .eventState(saveSaveRunEvent.getEventState())
                .build();

        return runEventDto;
    }

    public VoteResultDto voteSave(Long eventId, String teamType, Long eventType) throws Exception
    {
        RunEvent runEvent = runEventRepository.findByEventLimit(eventId).orElseThrow(EntityNotFoundException::new);
        Event event = eventRepository.findById(eventId).orElse(null);

        int triVote = runEvent.getEvent().getTriggerVote();
        int homeCount = 0;
        int awayCount = 0;
        if(runEvent.getHomeCount() != null)
            homeCount = runEvent.getHomeCount();
        if(runEvent.getAwayCount() != null)
            awayCount = runEvent.getAwayCount();

        VoteResultDto voteResultDto = getVoteResult(runEvent);
        if(event.getTriggerType() == 0)
        {
            RunEvent resultEvent = updateVoteCount(runEvent, teamType, eventType);
            voteResultDto.setEventState(runEvent.getEventState());
        }
        else
        {
            if(triVote <= homeCount || triVote <= awayCount)
            {
                voteResultDto.setEventState("STOP");
            }
            else
            {
                RunEvent resultEvent = updateVoteCount(runEvent, teamType, eventType);
                if(runEvent.getHomeCount() != null)
                    homeCount = runEvent.getHomeCount();
                if(runEvent.getAwayCount() != null)
                    awayCount = runEvent.getAwayCount();

                if(triVote <= homeCount || triVote <= awayCount)
                {
                    RunEventDto runEventDto = stopEvent(resultEvent);
                    voteResultDto.setEventState("STOP");


                    if(event.getContinuityType() == 1){
                        System.out.println("[ EventContinueTimer ] : " +" voteSave");
                        long delayTime = 2000;
                        EventContinueTimer eventContinueTimer = new EventContinueTimer(eventId);
                        Timer timer = new Timer();
                        timer.schedule(eventContinueTimer, delayTime );
                    }
                }
                else
                {
                    voteResultDto.setEventState("START");
                }
            }
        }
        return voteResultDto;
    }

    public VoteResultDto voteSave(VoteDto voteDto) throws Exception
    {
        RunEvent runEvent = runEventRepository.findByEventLimit(voteDto.getEventId()).orElseThrow(EntityNotFoundException::new);
        Event event = eventRepository.findById(voteDto.getEventId()).orElse(null);

        int triVote = runEvent.getEvent().getTriggerVote();
        int homeCount = 0;
        int awayCount = 0;
        if(runEvent.getHomeCount() != null)
            homeCount = runEvent.getHomeCount();
        if(runEvent.getAwayCount() != null)
            awayCount = runEvent.getAwayCount();

        VoteResultDto voteResultDto = getVoteResult(runEvent);
        if(event.getTriggerType() == 0)
        {
            RunEvent resultEvent = updateVoteCount(runEvent, voteDto.getTeamType(), voteDto.getEventType());
            resultEvent = updateTagState(runEvent, voteDto);

            voteResultDto.setEventState(runEvent.getEventState());
        }
        else
        {
            if(triVote <= homeCount || triVote <= awayCount)
            {
                voteResultDto.setEventState("STOP");
            }
            else
            {
                RunEvent resultEvent = updateVoteCount(runEvent, voteDto.getTeamType(), voteDto.getEventType());
                resultEvent = updateTagState(runEvent, voteDto);
                if(runEvent.getHomeCount() != null)
                    homeCount = runEvent.getHomeCount();
                if(runEvent.getAwayCount() != null)
                    awayCount = runEvent.getAwayCount();

                if(triVote <= homeCount || triVote <= awayCount)
                {
                    RunEventDto runEventDto = stopEvent(resultEvent);
                    voteResultDto.setEventState("STOP");


                    if(event.getContinuityType() == 1){
                        System.out.println("[ EventContinueTimer ] : " +" voteSave");
                        long delayTime = 2000;
                        EventContinueTimer eventContinueTimer = new EventContinueTimer(voteDto.getEventId());
                        Timer timer = new Timer();
                        timer.schedule(eventContinueTimer, delayTime );
                    }
                }
                else
                {
                    voteResultDto.setEventState("START");
                }
            }
        }

        return voteResultDto;
    }

    public VoteResultDto getVoteResult(RunEvent runevent)
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

    public RunEvent updateVoteCount(RunEvent runEvent, String teamType, Long eventType)
    {
        int voteCount = 1;
        if(teamType.equalsIgnoreCase("1") || teamType.equalsIgnoreCase("3"))
            voteCount = 3;

        if(teamType.equalsIgnoreCase("0") || teamType.equalsIgnoreCase("1"))
        {
            Integer homeCount = runEvent.getHomeCount();
            int total = voteCount;
            if(homeCount != null)
                total += homeCount;
            runEvent.setHomeCount(total);

            if(eventType == 1)
            {
                Integer home1 = runEvent.getHome1Count();
                int tot1 = voteCount;
                if(home1 != null)
                    tot1 += home1;
                runEvent.setHome1Count(tot1);
            }
            else if(eventType == 2)
            {
                Integer home2 = runEvent.getHome2Count();
                int tot2 = voteCount;
                if(home2 != null)
                    tot2 += home2;
                runEvent.setHome2Count(tot2);
            }
            else if(eventType == 3)
            {
                Integer home = runEvent.getHome3Count();
                int tot2 = voteCount;
                if(home != null)
                    tot2 += home;
                runEvent.setHome3Count(tot2);
            }
            else if(eventType == 4)
            {
                Integer home2 = runEvent.getHome4Count();
                int tot2 = voteCount;
                if(home2 != null)
                    tot2 += home2;
                runEvent.setHome4Count(tot2);
            }
            else if(eventType == 5)
            {
                Integer home2 = runEvent.getHome5Count();
                int tot2 = voteCount;
                if(home2 != null)
                    tot2 += home2;
                runEvent.setHome5Count(tot2);
            }

        }
        else
        {
            Integer awayCount = runEvent.getAwayCount();
            int total = voteCount;
            if(awayCount != null)
                total += awayCount;
            runEvent.setAwayCount(total);

            if(eventType == 1)
            {
                Integer away = runEvent.getAway1Count();
                int tot = voteCount;
                if(away != null)
                    tot += away;
                runEvent.setAway1Count(tot);
            }
            else if(eventType == 2)
            {
                Integer away = runEvent.getAway2Count();
                int tot = voteCount;
                if(away != null)
                    tot += away;
                runEvent.setAway2Count(tot);
            }
            else if(eventType == 3)
            {
                Integer away = runEvent.getAway3Count();
                int tot = voteCount;
                if(away != null)
                    tot += away;
                runEvent.setAway3Count(tot);
            }
            else if(eventType == 4)
            {
                Integer away = runEvent.getAway4Count();
                int tot = voteCount;
                if(away != null)
                    tot += away;
                runEvent.setAway4Count(tot);
            }
            else if(eventType == 5)
            {
                Integer away = runEvent.getAway5Count();
                int tot = voteCount;
                if(away != null)
                    tot += away;
                runEvent.setAway5Count(tot);
            }
        }
        RunEvent saveResult = runEventRepository.save(runEvent);
        return saveResult;
    }

    private int getBoolToInt(Boolean value)
    {
        if(value == null)
            return 0;
        else
            return value ? 1 : 0;
    }

    public RunEvent updateTagState(RunEvent runEvent, VoteDto voteDto)
    {
        for (Map.Entry<String, Boolean> entry : voteDto.getTags().entrySet()) {
            String key = entry.getKey();
            Boolean value = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + value);
            updateValue(runEvent, key, value);
        }

//        runEvent.setTag1(runEvent.getTag1().intValue() + getBoolToInt(voteDto.getTagState1()));
//        runEvent.setTag2(runEvent.getTag1().intValue() + getBoolToInt(voteDto.getTagState1()));
//        runEvent.setTag3(runEvent.getTag2().intValue() + getBoolToInt(voteDto.getTagState1()));
//        runEvent.setTag4(runEvent.getTag3().intValue() + getBoolToInt(voteDto.getTagState1()));
//        runEvent.setTag5(runEvent.getTag4().intValue() + getBoolToInt(voteDto.getTagState1()));
//        runEvent.setTag5(runEvent.getTag5().intValue() + getBoolToInt(voteDto.getTagState1()));
//        runEvent.setTag7(runEvent.getTag6().intValue() + getBoolToInt(voteDto.getTagState1()));
//        runEvent.setTag8(runEvent.getTag7().intValue() + getBoolToInt(voteDto.getTagState1()));
//        runEvent.setTag9(runEvent.getTag1().intValue() + getBoolToInt(voteDto.getTagState1()));
//        runEvent.setTag10(runEvent.getTag1().intValue() + getBoolToInt(voteDto.getTagState1()));

        RunEvent saveResult = runEventRepository.save(runEvent);
        return saveResult;
    }

    private void updateValue(RunEvent runEvent, String key, boolean value)
    {
        if(key.equalsIgnoreCase("tag0"))
            runEvent.setTag0(getValue(runEvent.getTag0(), value));
        else if(key.equalsIgnoreCase("tag1"))
            runEvent.setTag1(getValue(runEvent.getTag1(), value));
        else if(key.equalsIgnoreCase("tag2"))
            runEvent.setTag2(getValue(runEvent.getTag2(), value));
        else if(key.equalsIgnoreCase("tag3"))
            runEvent.setTag3(getValue(runEvent.getTag3(), value));
        else if(key.equalsIgnoreCase("tag4"))
            runEvent.setTag4(getValue(runEvent.getTag4(), value));
        else if(key.equalsIgnoreCase("tag5"))
            runEvent.setTag5(getValue(runEvent.getTag5(), value));
        else if(key.equalsIgnoreCase("tag6"))
            runEvent.setTag6(getValue(runEvent.getTag6(), value));
        else if(key.equalsIgnoreCase("tag7"))
            runEvent.setTag7(getValue(runEvent.getTag7(), value));
        else if(key.equalsIgnoreCase("tag8"))
            runEvent.setTag8(getValue(runEvent.getTag8(), value));
        else if(key.equalsIgnoreCase("tag9"))
            runEvent.setTag9(getValue(runEvent.getTag9(), value));

//        if(key.equalsIgnoreCase("tag0"))
//            runEvent.setTag0(runEvent.getTag0().intValue() + getBoolToInt(value));
//        else if(key.equalsIgnoreCase("tag1"))
//            runEvent.setTag1(runEvent.getTag1().intValue() + getBoolToInt(value));
//        else if(key.equalsIgnoreCase("tag2"))
//            runEvent.setTag2(runEvent.getTag2().intValue() + getBoolToInt(value));
//        else if(key.equalsIgnoreCase("tag3"))
//            runEvent.setTag3(runEvent.getTag3().intValue() + getBoolToInt(value));
//        else if(key.equalsIgnoreCase("tag4"))
//            runEvent.setTag4(runEvent.getTag4().intValue() + getBoolToInt(value));
//        else if(key.equalsIgnoreCase("tag5"))
//            runEvent.setTag5(runEvent.getTag5().intValue() + getBoolToInt(value));
//        else if(key.equalsIgnoreCase("tag6"))
//            runEvent.setTag6(runEvent.getTag6().intValue() + getBoolToInt(value));
//        else if(key.equalsIgnoreCase("tag7"))
//            runEvent.setTag7(runEvent.getTag7().intValue() + getBoolToInt(value));
//        else if(key.equalsIgnoreCase("tag8"))
//            runEvent.setTag8(runEvent.getTag8().intValue() + getBoolToInt(value));
//        else if(key.equalsIgnoreCase("tag9"))
//            runEvent.setTag9(runEvent.getTag9().intValue() + getBoolToInt(value));
    }

    private int getValue(Integer tag, Boolean value)
    {
        int result = 0;
        if(tag != null)
            result = tag.intValue();

        int iValue = 0;
        if(value != null)
            iValue = value ? 1 : 0;

        result += iValue;

        return result;

    }

    class EventStateTimer extends TimerTask
    {
        long eventId;
        long runEventId;
        public EventStateTimer(long eventId, long runEventId)
        {
            this.eventId = eventId;
            this.runEventId = runEventId;
        }

        @Override
        public void run() {
            Calendar nowTime = Calendar.getInstance();
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strNowTime = sd.format(nowTime.getTime());

            RunEvent runevent = runEventRepository.findById(runEventId).orElse(null);
            if(runevent != null && runevent.getEventState().equalsIgnoreCase("START"))
            {
                LocalDateTime endDateTime = LocalDateTime.now();
                runevent.setEndDateTime(endDateTime);
                runevent.setEventState("STOP");
                runEventRepository.save(runevent);

                Event event = eventRepository.findById(eventId).orElse(null);
                if(event.getContinuityType() == 1){
                    //long delayTime = 2000 * event.getContinuityTime();
                    System.out.println("[ EventContinueTimer ] : " +" EventStateTimer");
                    long delayTime = 2000;
                    EventContinueTimer eventContinueTimer = new EventContinueTimer(event.getId());
                    Timer timer = new Timer();
                    timer.schedule(eventContinueTimer, delayTime );
                }

                strNowTime = runevent.getEdtDateTime().toString();
            }
            System.out.println("[ " + strNowTime + " ] : " + runEventId + " STOP");
        }
    }

    class EventContinueTimer extends TimerTask
    {
        long eventId;
        public EventContinueTimer(long eventId)
        {
            this.eventId = eventId;
        }

        @Override
        public void run() {
            Event event = eventRepository.findById(eventId).orElse(null);
            if(event == null || event.getAutoRunState() == 0)
                return;

            LocalDateTime startDateTime = LocalDateTime.now();
            RunEvent runEvent = RunEvent.builder()
                    .event(event)
                    .startDateTime(startDateTime)
                    .eventState("START")
                    .build();

            RunEvent saveSaveRunEvent =  runEventRepository.save(runEvent);

            if(event.getTriggerType() == 0) {
                long stopTime = 1000 * event.getTriggerTime();
                EventStateTimer eventStateTimer = new EventStateTimer(eventId, saveSaveRunEvent.getId());
                Timer timer = new Timer();
                timer.schedule(eventStateTimer, stopTime );
            }

            System.out.println("EventContinueTimer [ " + startDateTime + " ] : " + runEvent.getId() + " START");
        }
    }
}
