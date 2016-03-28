package com.disc99.timeline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimelineItem {
    private long id;
    private String userId;
    private String serviceId;
    private String contents;
}
