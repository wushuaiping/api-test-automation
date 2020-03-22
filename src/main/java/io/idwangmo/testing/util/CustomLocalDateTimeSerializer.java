package io.idwangmo.testing.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * 序列化的时间是毫秒时间.
 *
 * @author idwangmo
 * @since 1.0
 */
@Slf4j
public class CustomLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    private static List<String> fieldNameList = ImmutableList.of("termBeginDate", "termEndDate");

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(value.atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli());

        if (fieldNameList.contains(gen.getOutputContext().getCurrentName())) {
            gen.writeStringField(gen.getOutputContext().getCurrentName(), value.toLocalDate().toString());
        }
    }

}
