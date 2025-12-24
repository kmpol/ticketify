package io.malicki.ticketify.common.kafka.model;

import java.time.LocalDateTime;
import java.util.Objects;

public record DltMessage(
        String originalTopic,
        String originalKey,
        String originalValue,

        String errorMessage,
        String exceptionClass,

        int retryCount,
        LocalDateTime failedAt
) {}
