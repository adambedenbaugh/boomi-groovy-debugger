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
	
private ArrayList<Properties> _incomingDocumentProperties;
private ArrayList<InputStream> _incomingStreams;
private ArrayList<InputStream> _resultStreams;
private ArrayList<Properties> _resultDocumentProperties;


//Default Constructor
	public ContextCreator(){

		_incomingDocumentProperties = new ArrayList<>();
		_resultDocumentProperties = new ArrayList<>();

		_incomingStreams = new ArrayList<>();
		_resultStreams = new ArrayList<>();
	}


	//Adds files to stream objects
	public void AddFiles(String path){

//		Properties prop = new Properties();
//		prop = null;

		try (Stream<Path> paths = Files.walk(Paths.get(path))) {
		    try {
				paths
				    .filter(Files::isRegularFile)
				    .forEach(file -> createStream(file.toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		catch(Exception ex)
		{
			System.out.println("Error with AddFiles()");
		}

		int numberOfProperties = _incomingStreams.size();

		for( int i = 0; i < numberOfProperties; i++ ) {
			//Properties prop = new Properties();
			_incomingDocumentProperties.add(null);
		}

	}
	
	//creates stream of files
	private void createStream(String path){
		
		try{
			InputStream is =  new FileInputStream(path);
			_incomingStreams.add(is);
		}
		catch(Exception ex)
		{
			System.out.println("Error with createStream()");
		}	
	}
	
	//returns the amount of streamobjects.
	public int getDataCount(){
		return _incomingStreams.size();
	}
	
	//Store stream and properties
	public void storeStream(InputStream stream, Properties props){
		_resultDocumentProperties.add(props);
		_resultStreams.add(stream);
	}
	
	//Get stream by it's index
	public InputStream getStream(int index){

		// is = new ByteArrayInputStream(is.toString().getBytes());
		// is.mark(0);
		
		return _incomingStreams.get(index);
	}
	
	//Creates empty property objects
	public void createEmptyProperties(int numberOfProperties){
		for( int i = 0; i < numberOfProperties; i++ ) {
			Properties prop = new Properties();
			_incomingDocumentProperties.add(prop);
		}
	}
	
	//Add property Key/Value pairs to the corresponding property
//	public void addPropertyValues(int index, String key, String value){
//		Properties prop = null;
//		prop =  _incomingDocumentProperties.get(index);
//		prop.put(key, value);
//
//	}
	
	//Get properties by its index
	public Properties getProperties(int index){
		// Properties prop = null;
		//prop =  _incomingDocumentProperties.get(index);

		return null;
	}	
}
