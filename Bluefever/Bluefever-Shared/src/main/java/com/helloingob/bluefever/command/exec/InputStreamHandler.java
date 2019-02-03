package com.helloingob.bluefever.command.exec;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.helloingob.bluefever.util.LogFileWriter;

public class InputStreamHandler implements Runnable {
    private InputStream inputStream;
    private List<String> output;

    public InputStreamHandler(InputStream inputStream, List<String> output) {
        this.inputStream = inputStream;
        this.output = output;
    }

    @Override
    public void run() {
        String line = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
            reader.close();
        } catch (Exception e) {
            LogFileWriter.writeDebugLogLine(e.getMessage());
        }
    }
}