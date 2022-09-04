package com.creditsuisse.logingestion.event.service;

import com.creditsuisse.logingestion.event.model.EventEntry;
import com.creditsuisse.logingestion.log.model.LogEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
public class EventConverter implements Converter<LogEntry, EventEntry> {
  private final ConcurrentMap<String, LogEntry> map;

  public EventConverter() {
    this.map = new ConcurrentHashMap<>();
  }

  EventConverter(ConcurrentMap<String, LogEntry> map) {
    this.map = map;
  }

  @Override
  @EventListener
  public EventEntry convert(LogEntry source) {
    String logEntryId = source.getId();
    LogEntry handledLog = map.putIfAbsent(logEntryId, source);
    if (handledLog == null || source.equals(handledLog)) {
      return null;
    }
    return convertLogToEvent(source, handledLog);
  }

  private EventEntry convertLogToEvent(LogEntry logEntry, LogEntry handledLog) {
    long duration = Math.abs(logEntry.getTimestamp() - handledLog.getTimestamp());
    EventEntry eventEntry = new EventEntry(logEntry, duration);
    log.info("Logs {} and {} are transformed to {}", logEntry, handledLog, eventEntry);
    return eventEntry;
  }
}
