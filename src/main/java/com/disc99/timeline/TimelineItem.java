package com.disc99.timeline;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimelineItem {
    private long id;
    private String userId;
    private String serviceId;
    private String json;
}
