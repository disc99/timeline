package com.disc99.timeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Controller
public class TimelineController {

    // TODO use
    private static final int REQUEST_ITEM_LIMIT = 30;

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
     * <p>
     *     Get all items if there is no lastId, otherwise get the items that has greater id than lastId.
     * </p>
     *
     * @param lastItemId last item id
     * @return timeline items
     */
    @RequestMapping("/timeline/items")
    @ResponseBody
    Items items(@PathVariable Long lastItemId) {
        // TODO get session
        String userId = "Tom";

        List<TimelineItem> items = timelineItemRepository.find(lastItemId, userId);
        return new Items(items);
    }

    /**
     * Register Server-sent events notification.
     *
     * @return
     */
    @RequestMapping("/timeline/registNotification")
    SseEmitter registerNotification() {
        SseEmitter sse = new SseEmitter();

        // TODO

        return sse;
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
     * @param json
     */
    @RequestMapping(value = "/hooks/{userId}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    void post(@PathVariable String userId, String accessToken, String json) {
        // check user id and access token

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

}
