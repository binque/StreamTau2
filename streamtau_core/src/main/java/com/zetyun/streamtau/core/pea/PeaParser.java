/*
 * Copyright 2020 Zetyun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zetyun.streamtau.core.pea;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.zetyun.streamtau.runtime.ScriptFormat;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PeaParser {
    public static final PeaParser JSON = createJsonPeaParser();
    public static final PeaParser YAML = createYamlPeaParser();
    private final ObjectMapper mapper;

    public static PeaParser get(@Nonnull ScriptFormat format) {
        switch (format) {
            case APPLICATION_JSON:
                return JSON;
            case APPLICATION_YAML:
                return YAML;
            default:
                throw new IllegalArgumentException("Unknown script format \"" + format + "\".");
        }
    }

    @Nonnull
    private static PeaParser createJsonPeaParser() {
        JsonMapper mapper = new JsonMapper();
        return new PeaParser(mapperWithCommonProperties(mapper));
    }

    @Nonnull
    private static PeaParser createYamlPeaParser() {
        ObjectMapper mapper;
        YAMLFactory yamlFactory = new YAMLFactory()
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES);
        mapper = new ObjectMapper(yamlFactory);
        return new PeaParser(mapperWithCommonProperties(mapper));
    }

    private static ObjectMapper mapperWithCommonProperties(@Nonnull ObjectMapper mapper) {
        return mapper
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
    }

    @Nonnull
    public static Map<String, Class<?>> getSubtypeClasses(Class<?> clazz) {
        DeserializationConfig config = JSON.mapper.getDeserializationConfig();
        AnnotationIntrospector annotationIntrospector = config.getAnnotationIntrospector();
        AnnotatedClass annotatedClass = AnnotatedClassResolver.resolveWithoutSuperTypes(config, clazz);
        List<NamedType> typeList = annotationIntrospector.findSubtypes(annotatedClass);
        final Map<String, Class<?>> map = new LinkedHashMap<>(typeList.size());
        for (NamedType type : typeList) {
            String name = type.getName();
            Class<?> cls = type.getType();
            if (name == null) {
                AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, cls);
                name = annotationIntrospector.findTypeName(ac);
            }
            map.put(name, cls);
        }
        return map;
    }

    public void parse(Object pea, String json) throws IOException {
        mapper.readerForUpdating(pea).readValue(json);
    }

    public <T> T parse(String json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }

    public <T> T parse(InputStream json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }

    public String stringShowAll(Object pea) throws IOException {
        return mapper.writerWithView(Show.class).writeValueAsString(pea);
    }

    public void writeAll(OutputStream os, Object pea) throws IOException {
        mapper.writerWithView(Show.class).writeValue(os, pea);
    }

    public String stringHideSome(Object pea) throws IOException {
        return mapper.writerWithView(Hide.class).writeValueAsString(pea);
    }

    public JsonSchema createJsonSchema(Class<?> clazz) throws JsonMappingException {
        JsonSchemaGenerator generator = new JsonSchemaGenerator(mapper);
        return generator.generateSchema(clazz);
    }

    public static class Show {
    }

    public static class Hide {
    }

    public static class Public {
    }
}
