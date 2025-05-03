package com.moneymanagement.core.tools;

import redis.clients.jedis.Jedis;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class RedisHashDeserializer {
    public static void main(String[] args) {
        String redisHost = "localhost"; // Change if needed
        int redisPort = 6379; // Change if needed
        String redisKey = "spring:session:sessions:ef848090-695a-475c-982a-4dd2c54260f2"; // Change to your Redis hash key

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try (Jedis jedis = new Jedis(redisHost, redisPort)) {
            Map<byte[], byte[]> hash = jedis.hgetAll(redisKey.getBytes());

            for (Map.Entry<byte[], byte[]> entry : hash.entrySet()) {
                String field = new String(entry.getKey());
                byte[] valueBytes = entry.getValue();

                if (valueBytes == null || valueBytes.length == 0) {
                    System.out.println("Field: " + field + " => Value is empty or null");
                    continue;
                }

                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(valueBytes))) {
                    Object value = ois.readObject();
                    if ("sessionAttr:SPRING_SECURITY_CONTEXT".equals(field)) {
                        // Convert to JSON string with JavaTimeModule support
                        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
                        System.out.println("Field: " + field + " => JSON Value: " + json);
                    } else {
                        System.out.println("Field: " + field + " => Value: " + value);
                    }
                } catch (Exception e) {
                    System.out.println("Field: " + field + " => Could not deserialize value");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
