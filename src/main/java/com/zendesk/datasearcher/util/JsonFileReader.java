package com.zendesk.datasearcher.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.zendesk.datasearcher.model.entity.AbstractEntity;
import org.springframework.stereotype.Service;

@Service
public class JsonFileReader {

    public InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    public <T extends AbstractEntity> List<T> parseJson(String path, Class<T> clazz) throws IOException {
        List<T> elements = new ArrayList<>();
        try (InputStream inputStream = getResourceFileAsInputStream(path)) {
            if (inputStream == null) {
                throw new FileNotFoundException("File is not found in the path " + path);
            }
            try (JsonReader reader = new JsonReader(new InputStreamReader(inputStream))) {
                reader.beginArray();
                while (reader.hasNext()) {
                    T element = new Gson().fromJson(reader, clazz);
                    elements.add(element);
                }
                reader.endArray();
            }
        }
        return elements;
    }
}
