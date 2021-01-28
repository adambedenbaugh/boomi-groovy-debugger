package com.boomi.execution;

import groovy.json.JsonOutput;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Properties;
import java.util.stream.Stream;
import java.lang.System;

public class ContextCreator {

    private final ArrayList<Properties> _incomingDocumentProperties;
    private final ArrayList<InputStream> _incomingStreams;
    private final ArrayList<InputStream> _resultStreams;
    private final ArrayList<Properties> _resultDocumentProperties;
    private boolean consoleOutput = false;

    //Default Constructor
    public ContextCreator() {

        _incomingDocumentProperties = new ArrayList<>();
        _resultDocumentProperties = new ArrayList<>();
        _incomingStreams = new ArrayList<>();
        _resultStreams = new ArrayList<>();

    }

    public void OutputToConsole() {
        consoleOutput = true;
    }

    //Adds files to stream objects
    public void AddFiles(String path) {
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            try {
                paths
                        .filter(Files::isRegularFile)
                        .forEach(file -> createStream(file.toString()));
            } catch (Exception e) {
				System.out.println("Error with AddFiles() method: ");
                e.printStackTrace();
            }
        } catch (Exception ex) {
            System.out.println("Error with AddFiles() method: ");
            ex.printStackTrace();
        }

        int numberOfProperties = _incomingStreams.size();
        for (int i = 0; i < numberOfProperties; i++) {
            Properties prop = new Properties();
            _incomingDocumentProperties.add(i, prop);
            System.out.println("i: " + i);
        }

    }

    //creates stream of files
    private void createStream(String path) {

        try {
            InputStream is = new FileInputStream(path);
            _incomingStreams.add(is);
        } catch (Exception ex) {
            System.out.println("Error with createStream()");
        }
    }

    //returns the amount of streamobjects.
    public int getDataCount() {
        return _incomingStreams.size();
    }

    //Store stream and properties
    public void storeStream(InputStream stream, Properties props) {
        _resultDocumentProperties.add(props);
        _resultStreams.add(stream);

        // Need to get a ByteArrayInputStream to convert to a String
//		if(consoleOutput){
//			System.out.println("storeStream Output: ");
//			System.out.println(stream.toString());
//		}

    }

    //Get stream by it's index
    public InputStream getStream(int index) {

    	// Need to finish this to allow for reset() method.
        // is = new ByteArrayInputStream(is.toString().getBytes());
        // is.mark(0);

        return _incomingStreams.get(index);
    }

    //Creates empty property objects
    public void createEmptyProperties(int numberOfProperties) {
        for (int i = 0; i < numberOfProperties; i++) {
            Properties prop = new Properties();
            _incomingDocumentProperties.add(prop);
        }
    }

    //Add property Key/Value pairs to the corresponding property
    public void addPropertyValues(int index, String key, String value) {
        Properties prop = _incomingDocumentProperties.get(index);
        key = "document.dynamic.userdefined." + key;
        prop.put(key, value);
    }

    //Get properties by its index
    public Properties getProperties(int index) {

		return _incomingDocumentProperties.get(index);
    }
}
