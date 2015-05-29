package com.dotTracePlugin.agent.runner;

import com.dotTracePlugin.agent.model.ProfiledMethod;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by Alexey.Totin on 5/29/2015.
 */
public class dotTraceProfilerConfigReader {
    private Map<String, String> myPlaceholdersMap;

    public dotTraceProfilerConfigReader(Map<String, String> placeholdersMap) {
        this.myPlaceholdersMap = placeholdersMap;
    }

    public void ReplacePlaceholders(String source, String destination){
        Path sourcePath = Paths.get(source);
        Path destPath = Paths.get(destination);
        Charset charset = StandardCharsets.UTF_8;

        String content = null;
        try {
            content = new String(Files.readAllBytes(sourcePath), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String,String> entry : myPlaceholdersMap.entrySet()) {
            entry.setValue(entry.getValue().replace("\\", "\\\\"));
            content = content.replaceAll(entry.getKey(), entry.getValue());
        }


        try {
            Files.write(destPath, content.getBytes(charset));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
