package com.dotTracePlugin.agent.runner;

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


    public void ReplacePlaceholdersInFile(Map<String, String> placeholdersMap, String source, String destination){
        Path sourcePath = Paths.get(source);
        Path destPath = Paths.get(destination);
        Charset charset = StandardCharsets.UTF_8;

        String content = null;
        try {
            content = new String(Files.readAllBytes(sourcePath), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String,String> entry : placeholdersMap.entrySet()) {
            entry.setValue(entry.getValue().replace("\\", "\\\\"));
            assert content != null;
            content = content.replaceAll(entry.getKey(), entry.getValue());
        }


        try {
            assert content != null;
            Files.write(destPath, content.getBytes(charset));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
