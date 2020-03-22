package io.idwangmo.testing.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * 对 Feign 的报错进行封装.
 *
 * @author idwangmo
 * @since 1.0
 */
@Slf4j
public class TestErrorDecode implements ErrorDecoder {


    @Override
    public Exception decode(String methodKey, Response response) {

        String message = response.request().url() + " is " + response.reason() + " , status code: " + response.status();

        if (ImmutableList.of(400, 500).contains(response.status())) {

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String body = IOUtils.toString(response.body().asInputStream(), "UTF-8");
                JsonNode jsonNode = objectMapper.readTree(body);
                return new TestException(message, jsonNode.get("error").asText());
            } catch (IOException e) {
                log.error("响应内容序列化失败", e);
            }

        }

        if (response.request().requestBody().length() > 0) {
            return new DecodeException(message + " , request Body: " + response.request().requestBody().asString());
        }

        return new DecodeException(message);

    }
}

