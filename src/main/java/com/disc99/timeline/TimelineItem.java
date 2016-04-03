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
    private Long accountId;
    private String serviceId;
    private String contents;
}
