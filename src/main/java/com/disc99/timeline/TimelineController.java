package com.disc99.timeline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class TimelineController {

    // TODO use
    private static final int REQUEST_ITEM_LIMIT = 30;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TimelineItemRepository timelineItemRepository;

    Map<AccountId, SseEmitter> emitters = new HashMap<>();

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
    Items items(@RequestParam(required = false) Long lastItemId, Principal principal) {
        User user = (User) ((Authentication) principal).getPrincipal();

        List<TimelineItem> items = lastItemId == null
            ? timelineItemRepository.findAll(user.accountId())
            : timelineItemRepository.find(lastItemId, user.accountId());

        return new Items(items);
    }

    /**
     * Register Server-sent events notification.
     *
     * @return
     */
    @RequestMapping("/timeline/registNotification")
    SseEmitter registerNotification(Principal principal) {
        User user = (User) ((Authentication) principal).getPrincipal();

        SseEmitter emitter = new SseEmitter();
        emitters.put(user.accountId(), emitter);
        emitter.onCompletion(() -> {
            System.out.println(LocalDateTime.now() + " onCompletion");
            emitters.remove(user.accountId());
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
     * @param userName
     * @param accessToken
     * @param itemRequest
     */
    @RequestMapping(value = "/hooks/{userName}", method = POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    void post(@PathVariable String userName, String accessToken, @ModelAttribute ItemRequest itemRequest) {
        // TODO check user id and access token
        Account account = accountRepository.findByName(userName).get();

        // create store data
        TimelineItem item = TimelineItem.builder()
                .accountId(account.getId().getValue())
                .serviceId(itemRequest.getServiceId())
                .contents(itemRequest.getContents())
                .build();

        timelineItemRepository.save(item);

        SseEmitter emitter = emitters.get(account.getId());
        if (emitter != null) {
            try {
                emitter.send("update", MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(account.getId());
                e.printStackTrace();
            }
        }
    }

}
