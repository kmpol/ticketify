package io.malicki.ticketify.common.kafka;

public class TopicNames {
    public static final String TICKET = "ticket-events";
    public static final String NOTIFICATION = "notification-events";

    public static class DLT {
        public static final String TICKET_DLT = "ticket-events.DLT";
    }

    public static class Streams {
        public static final String SPAM_USERS = "spam-users";
        public static final String URGENT_TICKETS = "urgent-tickets";
    }
}