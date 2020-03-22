package io.idwangmo.testing.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/**
 * 自定义返回的时间.
 *
 * @author idwangmo
 * @since 1.0
 */
@Slf4j
public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode jsonNode = p.getCodec().readTree(p);

        final String date = jsonNode.asText();

        LocalDateTime localDateTime;

        if (Objects.isNull(date)) {
            return null;
        }

        if (StringUtils.isNumeric(date)) {
            return Instant.ofEpochMilli(Long.valueOf(date)).atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
        }

        if (date.contains("Z")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.SIMPLIFIED_CHINESE);
            return LocalDateTime.parse(date, formatter);
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
            localDateTime = LocalDateTime.parse(date,formatter);
        } catch (Exception e) {
            localDateTime = LocalDate.parse(date).atTime(LocalTime.MIN);
        }

        return localDateTime;
    }
}
