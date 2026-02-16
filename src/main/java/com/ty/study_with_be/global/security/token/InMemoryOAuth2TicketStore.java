package com.ty.study_with_be.global.security.token;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class InMemoryOAuth2TicketStore implements OAuth2ExchangeTokenStore{

    private static class Entry {
        final OAuth2TicketPayload payload;
        final Instant expiresAt;

        Entry(OAuth2TicketPayload payload, Instant expiresAt) {
            this.payload = payload;
            this.expiresAt = expiresAt;
        }

        boolean isExpired() {
            return Instant.now().isAfter(expiresAt);
        }
    }

    private final ConcurrentHashMap<String, Entry> store = new ConcurrentHashMap<>();

    @Override
    public void save(String ticket, OAuth2TicketPayload payload, Duration ttl) {
        cleanupExpired();
        store.put(ticket, new Entry(payload, Instant.now().plus(ttl)));
    }

    @Override
    public Optional<OAuth2TicketPayload> consume(String ticket) {
        Entry entry = store.remove(ticket);
        if (entry == null) return Optional.empty();
        if (entry.isExpired()) return Optional.empty();
        return Optional.of(entry.payload);
    }

    private void cleanupExpired() {
        store.entrySet().removeIf(e -> e.getValue().isExpired());
    }
}
