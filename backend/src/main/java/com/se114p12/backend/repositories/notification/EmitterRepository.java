package com.se114p12.backend.repositories.notification;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void addOrReplace(String key, SseEmitter emitter) {
        emitters.put(key, emitter);
    }

    public Optional<SseEmitter> get(String key) {
        return Optional.ofNullable(emitters.get(key));
    }

    public void remove(String key) {
        emitters.remove(key);
    }

    public boolean contains(String key) {
        return emitters.containsKey(key);
    }

    public int size() {
        return emitters.size();
    }
}