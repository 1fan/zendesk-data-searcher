package com.zendesk.datasearcher.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.zendesk.datasearcher.model.entity.AbstractEntity;
import com.zendesk.datasearcher.processor.Processor;
import org.springframework.stereotype.Service;

@Service
public class JsonFileReader {

    public InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader classLoader = Processor.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    public <T extends AbstractEntity> List<T> parseJson(String path, Class<T> clazz) throws Exception {
        List<T> elements = new ArrayList<>();
        try (
                InputStream inputStream = getResourceFileAsInputStream(path);
                JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        ) {
            reader.beginArray();
            while (reader.hasNext()) {
                T element = new Gson().fromJson(reader, clazz);
                elements.add(element);
            }
            reader.endArray();
        }
        return elements;
    }
}
