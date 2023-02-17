package com.upfault.doordash.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StopWatch {
	public static final Map<UUID, LongPair> playerTimes = new HashMap<>();

	public void start(UUID playerId) {
		if (!playerTimes.containsKey(playerId)) {
			playerTimes.put(playerId, new LongPair(System.nanoTime(), 0L));
		} else {
			LongPair times = playerTimes.get(playerId);
			if (times.getSecond() != 0) {
				// Player has previously stopped the stopwatch, resume from where it left off
				long diff = System.nanoTime() - times.getSecond();
				times = new LongPair(times.getFirst() + diff, 0L);
				playerTimes.put(playerId, times);
			}
		}
	}

	public void stop(UUID playerId) {
		if (playerTimes.containsKey(playerId)) {
			LongPair times = playerTimes.get(playerId);
			if (times.getSecond() == 0) {
				// Player has not previously stopped the stopwatch, record the stop time
				times = new LongPair(times.getFirst(), System.nanoTime());
				playerTimes.put(playerId, times);
			}
		}
	}

	public void reset(UUID playerId) {
		playerTimes.remove(playerId);
	}

	// elapsed time in nanoseconds
	public long getElapsedTime(UUID playerId) {
		long elapsed = 0;
		if (playerTimes.containsKey(playerId)) {
			LongPair times = playerTimes.get(playerId);
			if (times.getSecond() == 0) {
				// Player is currently running the stopwatch, get elapsed time
				elapsed = System.nanoTime() - times.getFirst();
			} else {
				// Player has stopped the stopwatch, get elapsed time between start and stop
				elapsed = times.getSecond() - times.getFirst();
			}
		}
		return elapsed;
	}

	// elapsed time in seconds
	public long getElapsedTimeSecs(UUID playerId) {
		return getElapsedTime(playerId) / 1_000_000_000;
	}

	// formatted elapsed time in HH:MM:SS
	public String getFormattedElapsedTime(UUID playerId) {
		long elapsed = getElapsedTime(playerId) / 1_000_000_000;
		long hours = elapsed / 3600;
		long minutes = (elapsed % 3600) / 60;
		long seconds = elapsed % 60;
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	public boolean isRunning(UUID playerId) {
		return playerTimes.containsKey(playerId) && playerTimes.get(playerId).getSecond() == 0;
	}

}


