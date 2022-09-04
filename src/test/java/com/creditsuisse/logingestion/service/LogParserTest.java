package com.creditsuisse.logingestion.service;

import com.creditsuisse.logingestion.log.model.LogEntry;
import com.creditsuisse.logingestion.log.service.LogParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LogParserTest {
  @Mock
  private ObjectMapper objectMapper;
  private LogParser logParser;

  @BeforeEach
  public void setUp() throws Exception {
    logParser = new LogParser(objectMapper);
  }

  @Test
  public void parseLogLineSuccessfully() throws IOException {
    String logLine = "success";
    LogEntry toReturn = new LogEntry("id", "state", 1L);
    when(objectMapper.readValue(logLine, LogEntry.class)).thenReturn(toReturn);
    LogEntry logEntry = logParser.parseLogLine(logLine);
    assertSame(toReturn, logEntry);
  }

  @Test
  public void parseLogLineWithError() throws JsonProcessingException {
    String logLine = "error";
    when(objectMapper.readValue(logLine, LogEntry.class)).thenThrow(JsonProcessingException.class);
    LogEntry logEntry = logParser.parseLogLine(logLine);
    assertNull(logEntry);
  }
}