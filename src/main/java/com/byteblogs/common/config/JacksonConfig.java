package com.byteblogs.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @Author:byteblogs
 * @Date:2018/09/27 12:52
 */
public class JacksonConfig {

    public static ObjectMapper jacksonObjectMapper() {

        // 初始化全局Jackson 序列化工具
        ObjectMapper objectMapper = new ObjectMapper();

        // 忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 忽略无法序列化的属性
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // 序列化是忽略空值字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 初始化Java8 时间序列化器
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeNumber(String.valueOf(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            }
        });
        javaTimeModule.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeNumber(String.valueOf(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            }
        });
        javaTimeModule.addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                String value = jsonParser.getValueAsString();
                return StringUtils.isBlank(value) ? null : new Timestamp(Long.valueOf(value.trim())).toLocalDateTime().toLocalDate();
            }
        });
        javaTimeModule.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                String value = jsonParser.getValueAsString();
                return StringUtils.isBlank(value) ? null : new Timestamp(Long.valueOf(value.trim())).toLocalDateTime();
            }
        });

        // 注册JAVA 时间序列化器
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
}