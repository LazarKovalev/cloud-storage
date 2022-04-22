package com.elephascloud.storage.common.data.jackson;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * To circumvent system exceptions caused by NULL, use this rule to replace the NULL value with the
 * default value of the variable during serialization or deserialization.
 * <p>
 * The rule is number will be replaced by -1, string will be replaced by "", date will be replaced
 * by "", boolean will be replaced by false, array will be replaced by [], Object will be replaced
 * by{}.
 *
 * @author Rei
 */
public class SerializerModifier extends BeanSerializerModifier {

  @Override
  public List<BeanPropertyWriter> changeProperties(
      SerializationConfig config, BeanDescription beanDesc,
      List<BeanPropertyWriter> beanProperties) {
    beanProperties.forEach(writer -> {
      if (writer.hasNullSerializer()) {
        return;
      }
      JavaType type = writer.getType();
      Class<?> clazz = type.getRawClass();
      if (type.isTypeOrSubTypeOf(Number.class)) {
        writer.assignNullSerializer(NullJsonSerializers.NUMBER_JSON_SERIALIZER);
      } else if (type.isTypeOrSubTypeOf(Boolean.class)) {
        writer.assignNullSerializer(NullJsonSerializers.BOOLEAN_JSON_SERIALIZER);
      } else if (type.isTypeOrSubTypeOf(Character.class)) {
        writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
      } else if (type.isTypeOrSubTypeOf(String.class)) {
        writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
      } else if (type.isArrayType() || clazz.isArray() || type.isTypeOrSubTypeOf(
          Collection.class)) {
        writer.assignNullSerializer(NullJsonSerializers.ARRAY_JSON_SERIALIZER);
      } else if (type.isTypeOrSubTypeOf(OffsetDateTime.class)) {
        writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
      } else if (type.isTypeOrSubTypeOf(Date.class) || type.isTypeOrSubTypeOf(
          TemporalAccessor.class)) {
        writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
      } else {
        writer.assignNullSerializer(NullJsonSerializers.OBJECT_JSON_SERIALIZER);
      }
    });
    return super.changeProperties(config, beanDesc, beanProperties);
  }

  public interface NullJsonSerializers {

    JsonSerializer<Object> STRING_JSON_SERIALIZER = new JsonSerializer<>() {
      @Override
      public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
          throws IOException {
        gen.writeString(StringPool.EMPTY);
      }
    };

    JsonSerializer<Object> NUMBER_JSON_SERIALIZER = new JsonSerializer<>() {
      @Override
      public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
          throws IOException {
        gen.writeNumber(-1);
      }
    };

    JsonSerializer<Object> BOOLEAN_JSON_SERIALIZER = new JsonSerializer<>() {
      @Override
      public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
          throws IOException {
        gen.writeObject(Boolean.FALSE);
      }
    };

    JsonSerializer<Object> ARRAY_JSON_SERIALIZER = new JsonSerializer<>() {
      @Override
      public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
          throws IOException {
        gen.writeStartArray();
        gen.writeEndArray();
      }
    };

    JsonSerializer<Object> OBJECT_JSON_SERIALIZER = new JsonSerializer<>() {
      @Override
      public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
          throws IOException {
        gen.writeStartObject();
        gen.writeEndObject();
      }
    };

  }
}
