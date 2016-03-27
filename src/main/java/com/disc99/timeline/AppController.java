package com.disc99.timeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Controller
public class AppController {

    // TODO use
    private static final int SEARCH_LIMIT = 30;

    @Autowired
    TimelineItemRepository timelineItemRepository;

    /**
     * Top page.
     *
     * @return
     */
    @RequestMapping("/")
    String index() {
        return "index";
    }

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
     * @param lastId last item id
     * @return if exist last item id getting later items, else getting all items
     */
    @RequestMapping("/items/{lastId}")
    @ResponseBody
    Items items(@PathVariable Long lastId) {
        // TODO get session
        String userId = "Tom";

        List<TimelineItem> items = timelineItemRepository.find(lastId, userId);
        return new Items(items);
    }


    /**
     * Append user data.
     *
     * @param userId
     * @param accessToken
     * @param json
     */
    @RequestMapping(value = "/hooks/{userId}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    void post(@PathVariable String userId, String accessToken, String json) {
        // check access token

        // create store data
        TimelineItem item = TimelineItem.builder()
                .userId(userId)
                .serviceId(userId)
                .json(json)
                .build();

        // store
        timelineItemRepository.save(item);

        // subscribe store event
    }


    /**
     * Register Server-sent events notification.
     *
     * @return
     */
    @RequestMapping("registNotification")
    SseEmitter registerNotification() {
        SseEmitter sse = new SseEmitter();

        // TODO

        return sse;
    }
}
