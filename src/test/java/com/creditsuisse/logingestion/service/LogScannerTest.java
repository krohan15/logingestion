package com.creditsuisse.logingestion.service;

import com.creditsuisse.logingestion.log.service.LogScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LogScannerTest {
  @Mock
  private ApplicationEventPublisher eventPublisher;
  private LogScanner logScanner;

  @BeforeEach
  public void setUp() {
    logScanner = new LogScanner(eventPublisher);
  }

  @Test
  public void read() {
    String path = ClassLoader.getSystemResource("testInput.txt").getPath();
    logScanner.read(path);

    ArgumentCaptor<String> eventCaptor = ArgumentCaptor.forClass(String.class);
    verify(eventPublisher, times(6)).publishEvent(eventCaptor.capture());
  }
}