package com.example.aplazo.service;

import java.time.Duration;
import java.time.Instant;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {
	private final Map<String, Deque<Instant>> requestLog = new ConcurrentHashMap<>();

	public boolean isAllowed(String key, int limit, int timeWindow) {
		Instant now = Instant.now();
		Deque<Instant> timestamps = requestLog.computeIfAbsent(key, k -> new LinkedList<>());

		synchronized (timestamps) {

			while (!timestamps.isEmpty() && Duration.between(timestamps.peek(), now).getSeconds() > timeWindow) {
				timestamps.poll();
			}

			if (timestamps.size() < limit) {
				timestamps.add(now);
				return true;
			} else {
				return false;
			}
		}
	}
}