package com.elephascloud.storage.common.data.util;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.elephascloud.storage.common.core.util.Exceptions;
import com.elephascloud.storage.common.data.jackson.SerializerModifier;
import com.elephascloud.storage.common.data.jackson.TimeModule;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@UtilityClass
public final class JsonUtil {

  /**
   * Serialize objects into json string
   *
   * @param value java bean
   * @param <T>   T
   * @return json string
   */
  public static <T> String toJson(T value) {
    try {
      return getInstance().writeValueAsString(value);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  /**
   * Serialize objects into json byte arrays
   *
   * @param object java bean
   * @return json byte array
   */
  public static byte[] toJsonAsBytes(Object object) {
    try {
      return getInstance().writeValueAsBytes(object);
    } catch (JsonProcessingException e) {
      throw Exceptions.unchecked(e);
    }
  }

  /**
   * Deserialize json string into object
   *
   * @param json      json string
   * @param valueType class
   * @param <T>       T
   * @return java bean
   */
  public static <T> T parse(String json, Class<T> valueType) {
    try {
      return getInstance().readValue(json, valueType);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  /**
   * Deserialize json string into object
   *
   * @param json          json string
   * @param typeReference typeReference
   * @param <T>           T
   * @return java bean
   */
  public static <T> T parse(String json, TypeReference<T> typeReference) {
    try {
      return getInstance().readValue(json, typeReference);
    } catch (IOException e) {
      throw Exceptions.unchecked(e);
    }
  }

  /**
   * Deserialize json byte array into object
   *
   * @param bytes     json byte array
   * @param valueType class
   * @param <T>       T
   * @return java bean
   */
  public static <T> T parse(byte[] bytes, Class<T> valueType) {
    try {
      return getInstance().readValue(bytes, valueType);
    } catch (IOException e) {
      throw Exceptions.unchecked(e);
    }
  }


  /**
   * Deserialize json byte array into object.
   *
   * @param bytes         json byte array
   * @param typeReference type Reference
   * @param <T>           T
   * @return java bean
   */
  public static <T> T parse(byte[] bytes, TypeReference<T> typeReference) {
    try {
      return getInstance().readValue(bytes, typeReference);
    } catch (IOException e) {
      throw Exceptions.unchecked(e);
    }
  }

  /**
   * Deserialize json from input stream array into object.
   *
   * @param inputStream InputStream
   * @param valueType   class
   * @param <T>         T
   * @return java bean
   */
  public static <T> T parse(InputStream inputStream, Class<T> valueType) {
    try {
      return getInstance().readValue(inputStream, valueType);
    } catch (IOException e) {
      throw Exceptions.unchecked(e);
    }
  }

  /**
   * Deserialize json from input stream array into object.
   *
   * @param inputStream   InputStream
   * @param typeReference typeReference
   * @param <T>           T
   * @return java bean
   */
  public static <T> T parse(InputStream inputStream, TypeReference<T> typeReference) {
    try {
      return getInstance().readValue(inputStream, typeReference);
    } catch (IOException e) {
      throw Exceptions.unchecked(e);
    }
  }

  /**
   * Deserialize json string into list.
   *
   * @param json         json string
   * @param valueTypeRef class
   * @param <T>          T
   * @return list
   */
  public static <T> List<T> parseArray(String json, Class<T> valueTypeRef) {
    try {

      if (!StringUtils.startsWithIgnoreCase(json, StringPool.LEFT_SQ_BRACKET)) {
        json = StringPool.LEFT_SQ_BRACKET + json + StringPool.RIGHT_SQ_BRACKET;
      }

      List<Map<String, Object>> list = getInstance().readValue(json, new TypeReference<>() {
      });
      List<T> result = new ArrayList<>();
      for (Map<String, Object> map : list) {
        result.add(toPojo(map, valueTypeRef));
      }
      return result;
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  /**
   * Deserialize json string into map.
   *
   * @param json json string
   * @return map
   */
  public static Map<String, Object> parseMap(String json) {
    try {
      return getInstance().readValue(json, Map.class);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  /**
   * Deserialize json string into map.
   *
   * @param json         json string
   * @param valueTypeRef class
   * @param <T>          T
   * @return map
   */
  public static <T> Map<String, T> parseMap(String json, Class<T> valueTypeRef) {
    try {
      Map<String, Map<String, Object>> map = getInstance().readValue(json,
          new TypeReference<>() {
          });
      Map<String, T> result = new HashMap<>(16);
      for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
        result.put(entry.getKey(), toPojo(entry.getValue(), valueTypeRef));
      }
      return result;
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public static <T> T toPojo(Map fromValue, Class<T> toValueType) {
    return getInstance().convertValue(fromValue, toValueType);
  }

  /**
   * Convert json string to JsonNode
   *
   * @param json json
   * @return json json
   */
  public static JsonNode toJsonNode(String json) {
    try {
      return getInstance().readTree(json);
    } catch (IOException e) {
      throw Exceptions.unchecked(e);
    }
  }

  /**
   * Convert json string to JsonNode
   *
   * @param inputStream InputStream
   * @return jsonString jsonString
   */
  public static JsonNode toJsonNode(InputStream inputStream) {
    try {
      return getInstance().readTree(inputStream);
    } catch (IOException e) {
      throw Exceptions.unchecked(e);
    }
  }

  /**
   * Convert json string to JsonNode
   *
   * @param content content
   * @return jsonString json string
   */
  public static JsonNode toJsonNode(byte[] content) {
    try {
      return getInstance().readTree(content);
    } catch (IOException e) {
      throw Exceptions.unchecked(e);
    }
  }

  /**
   * Convert json string to JsonNode
   *
   * @param jsonParser JsonParser
   * @return json string
   */
  public static JsonNode toJsonNode(JsonParser jsonParser) {
    try {
      return getInstance().readTree(jsonParser);
    } catch (IOException e) {
      throw Exceptions.unchecked(e);
    }
  }

  private static ObjectMapper getInstance() {
    return JacksonHolder.INSTANCE;
  }

  private static class JacksonHolder {

    private static final ObjectMapper INSTANCE = new JacksonObjectMapper();
  }

  private static class JacksonObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 0L;

    private static final Locale DEFAULT_LOCALE = Locale.getDefault();

    public JacksonObjectMapper() {
      super();
      super.setLocale(DEFAULT_LOCALE);
      super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      super.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
      super.setDateFormat(new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN, DEFAULT_LOCALE));
      super.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
      super.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
      super.findAndRegisterModules();
      super.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      super.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      super.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
      super.getDeserializationConfig()
          .withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      super.setSerializerFactory(
          this.getSerializerFactory().withSerializerModifier(new SerializerModifier()));
      super.getSerializerProvider()
          .setNullValueSerializer(SerializerModifier.NullJsonSerializers.STRING_JSON_SERIALIZER);
      super.registerModule(new TimeModule());
      super.findAndRegisterModules();
    }

    @Override
    public ObjectMapper copy() {
      return super.copy();
    }
  }
}
