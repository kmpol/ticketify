package io.malicki.ticketify.streams.model;

import java.time.LocalDateTime;

public record SpamUserEvent(
        String visitorId,
        long ticketCount,
        LocalDateTime detectedAt,
        String reason
) {
    public static SpamUserEvent of(String visitorId, long ticketCount) {
        return new SpamUserEvent(
                visitorId,
                ticketCount,
                LocalDateTime.now(),
                "Created " + ticketCount + " tickets within 1 minute (threshold: 5)"
        );
    }
}
