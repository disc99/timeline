package com.disc99.timeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TimelineController {

    // TODO use
    private static final int REQUEST_ITEM_LIMIT = 30;

    @Autowired
    TimelineItemRepository timelineItemRepository;

    Map<String, SseEmitter> emitters = new HashMap<>();



    /**
     * Timeline page.
     *
     * @return
     */
    @RequestMapping("/timeline")
    String timeline() {
        return "timeline";
    }

    /**
     * Get timeline items.
     *
     * <p>
     *     Get all items if there is no lastId, otherwise get the items that has greater id than lastId.
     * </p>
     *
     * @param lastItemId last item id
     * @return timeline items
     */
    @RequestMapping("/timeline/items")
    @ResponseBody
    Items items(@RequestParam(required = false) Long lastItemId) {
        // TODO get session
        String userId = "Tom";

        List<TimelineItem> items = lastItemId == null
            ? timelineItemRepository.findAll(userId)
            : timelineItemRepository.find(lastItemId, userId);
        return new Items(items);
    }

    /**
     * Register Server-sent events notification.
     *
     * @return
     */
    @RequestMapping("/timeline/registNotification")
    SseEmitter registerNotification() {
        // TODO get session
        String userId = "Tom";

        SseEmitter emitter = new SseEmitter();
        emitters.put(userId, emitter);
        emitter.onCompletion(() -> {
            System.out.println(LocalDateTime.now() + " onCompletion");
            emitters.remove(userId);
        });
        emitter.onTimeout(() -> System.out.println(LocalDateTime.now() + " onCompletion"));

        return emitter;
    }

    /**
     * Append user data.
     *
     * <p>
     *     This url is not authorization.
     * </p>
     *
     * @param userId
     * @param accessToken
     * @param itemRequest
     */
    @RequestMapping(value = "/hooks/{userId}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    void post(@PathVariable String userId, String accessToken, @ModelAttribute ItemRequest itemRequest) {
        // check user id and access token

        // create store data
        TimelineItem item = TimelineItem.builder()
                .userId(userId)
                .serviceId(itemRequest.getServiceId())
                .contents(itemRequest.getContents())
                .build();

        // store
        timelineItemRepository.save(item);

        // subscribe store event
        SseEmitter emitter = emitters.get(userId);
        try {
            emitter.send("update", MediaType.APPLICATION_JSON);
        } catch (IOException e) {
            emitter.complete();
            emitters.remove(userId);
            e.printStackTrace();
        }
    }

}
