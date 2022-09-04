package com.creditsuisse.logingestion.jackson;

import com.creditsuisse.logingestion.log.model.LogEntry;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiter.api.Test;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

public class JacksonConfigurationTest {

  @Test
  public void logEntryHierarchyModule() throws JsonMappingException {
    JacksonConfiguration configuration = new JacksonConfiguration();
    Module module = configuration.logEntryHierarchyModule();

    assertEquals("LogEntryHierarchyModule", module.getModuleName());
    assertTrue(module instanceof SimpleModule);

    SimpleModule simpleModule = (SimpleModule) module;

    Field deserializers = ReflectionUtils.findField(SimpleModule.class, "_deserializers");
    ReflectionUtils.makeAccessible(deserializers);
    SimpleDeserializers field = (SimpleDeserializers) ReflectionUtils.getField(deserializers, simpleModule);

    JavaType javaType = TypeFactory.defaultInstance().constructSimpleType(LogEntry.class, null);
    JsonDeserializer<?> beanDeserializer = field.findBeanDeserializer(javaType, null, null);
    assertTrue(beanDeserializer instanceof LogEntryDeserializer);
  }
}