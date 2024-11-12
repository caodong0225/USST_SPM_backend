package usst.spm.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * <p>
 *     LocalDateTime反序列化配置
 * </p>
 * @author jyzxc
 * @since 2024-08-17
 */
public class LocalDateTimeSerializerConfig extends JsonDeserializer<LocalDateTime> {
    private final static ZoneId zone = ZoneId.of("Asia/Shanghai");

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        long timestamp = p.getLongValue();
        return Instant.ofEpochMilli(timestamp)
                .atZone(zone)
                .toLocalDateTime();
    }
}