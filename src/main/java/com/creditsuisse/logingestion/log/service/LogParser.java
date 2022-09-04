package com.creditsuisse.logingestion.log.service;

import com.creditsuisse.logingestion.log.model.LogEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Slf4j
@Service
public class LogParser {
  private final ObjectMapper objectMapper;

  @Autowired
  public LogParser(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @EventListener
  public LogEntry parseLogLine(String logEntryLine) {
    try {
      LogEntry logEntry = objectMapper.readValue(logEntryLine, LogEntry.class);
      log.info("Line {} is transformed to {}", logEntryLine, logEntry);
      return logEntry;
    } catch (IOException e) {
      log.error("Couldn't parse " + logEntryLine, e);
      return null;
    }
  }
}
