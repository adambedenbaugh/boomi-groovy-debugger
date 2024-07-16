package com.boomi.execution;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.stream.Stream;
import java.lang.System;


/**
 * The ContextCreator class is used to mock Boomi's ContextCreator class within the Data process Shape.
 * It creates and manage input and output streams, as well as dynamic document properties.
 *
 * This is a basic example of how to use this class:
 *
 * <pre>
 *      import com.boomi.execution.ContextCreator
 *
 *      String pathFiles = "${System.getenv("PROJECT_DIR")}/input_files/emptyfile.txt"
 *      dataContext = new ContextCreator()
 *      dataContext.AddFiles(pathFiles)
 *      ExecutionUtil ExecutionUtil = new ExecutionUtil()

 *      ExecutionUtil.setDynamicProcessProperty("method","DPP_value",false)
 *      ExecutionUtil.setDynamicProcessProperty("path","DPP_value",false)
 *      ExecutionUtil.setDynamicProcessProperty("timestamp","DPP_value",false)
 *      dataContext.addDynamicDocumentPropertyValues(0,"DDP_name","DDP_value")
 *
 *      // Place script after this line.
 *      //----------------------------------------------------------------------------------------------------
 *
 *      import java.util.Properties
 *      import java.io.InputStream
 *      import com.boomi.execution.ExecutionUtil
 *
 *      for(int i=0; i&lt;dataContext.getDataCount(); i++){
 *          InputStream is=dataContext.getStream(i);
 *          Properties props=dataContext.getProperties(i);
 *
 *          dataContext.storeStream(new ByteArrayInputStream(outData.getBytes('UTF-8')),props);
 *      }
 * </pre>
 */
public class ContextCreator {

    private ArrayList<Properties> incomingDynamicDocumentProperties;
    private ArrayList<InputStream> incomingStreams;
    private ArrayList<InputStream> resultStreams;
    private ArrayList<Properties> resultDocumentProperties;


    /**
     * Initializes a new instance of the ContextCreator class.
     * <p>
     * This constructor initializes the lists that will hold the dynamic document properties,
     * result document properties, incoming streams, and result streams.
     * </p>
     */
    public ContextCreator() {

        incomingDynamicDocumentProperties = new ArrayList<>();
        resultDocumentProperties = new ArrayList<>();
        incomingStreams = new ArrayList<>();
        resultStreams = new ArrayList<>();

    }


    /**
     * Adds all regular files from the specified directory path to the incoming streams list and initializes
     * corresponding dynamic document properties.
     * <p>
     * The path parameter can be either a specific file or a directory. If a file is specified, then file is added.
     * If a directory is spcified, then all regular files in the directory are added. It also
     * initializes a new Properties object for each file and adds it to the list of incoming dynamic document properties.
     * </p>
     *
     * @param path the root directory path to walk and add files from or a specific file path to add
     */
    public void AddFiles(String path) {

        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.filter(Files::isRegularFile)
                    .forEach(file -> {
                        createStream(file.toString());
                    });
        } catch (Exception e) {
            System.out.println("Error with AddFiles() method: ");
            e.printStackTrace();
        }

        int numberOfProperties = incomingStreams.size();
        for (int i = 0; i < numberOfProperties; i++) {
            Properties prop = new Properties();
            incomingDynamicDocumentProperties.add(i, prop);
        }

    }

    /**
     * Creates an input stream from the specified file path and adds it to the list of incoming streams.
     * <p>
     * If an error occurs during file reading or stream creation, the exception is caught and an error message
     * is printed to the console.
     * </p>
     *
     * @param path the file path to create an input stream from
     */
    private void createStream(String path) {

        try (FileInputStream fis = new FileInputStream(path)) {
            byte[] bytes = IOUtils.toByteArray(fis);
            InputStream is = new ByteArrayInputStream(bytes);
            incomingStreams.add(is);
        } catch (Exception e) {
            System.out.println("Error with createStream()");
            e.printStackTrace();
        }
    }

    /**
     * Returns the number of incoming streams.
     * <p>
     * This method retrieves the current count of input streams that have been added
     * to the incoming streams list. This method is generally used in the conditional
     * section of the main for loop within th Data Process shape.
     * </p>
     *
     * @return the number of incoming streams
     */
    public int getDataCount() {
        return incomingStreams.size();
    }

    /**
     * Stores the given input stream and associated properties.
     * <p>
     * This method adds the specified input stream and properties to the lists of result streams
     * and result document properties. This method is generally used at the end of the for loop
     * within a Data Process shape to return data back to the flow.
     * </p>
     *
     * @param stream the input stream to store
     * @param props the properties associated with the input stream
     * @throws IOException if an I/O error occurs while storing the stream
     */
    public void storeStream(InputStream stream, Properties props) throws IOException {

        resultDocumentProperties.add(props);
        resultStreams.add(stream);
    }

    /**
     * Retrieves the input stream at the specified index from the list of incoming streams.
     * <p>
     * This method fetches the InputStream object located at the given index in the list of incoming
     * streams.
     * </p>
     *
     * @param index the index of the input stream to retrieve
     * @return the InputStream object at the specified index
     * @throws IOException if an I/O error occurs
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public InputStream getStream(int index) throws IOException {

        return incomingStreams.get(index);
    }

    /**
     * Adds a dynamic document property key-value pair to the properties at the specified index.
     * <p>
     * This method adds a new key-value pair to the Properties object located at the given index
     * in the list of incoming dynamic document properties. The key is prefixed with
     * "document.dynamic.userdefined." before being added to the properties. This is used right after
     * AddFiles() method to add dynamic document properties to the incoming documents.
     * </p>
     *
     * @param index the index of the properties to update
     * @param key the key of the property to add
     * @param value the value of the property to add
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void addDynamicDocumentPropertyValues(int index, String key, String value) {
        Properties prop = incomingDynamicDocumentProperties.get(index);
        key = "document.dynamic.userdefined." + key;
        prop.put(key, value);
    }

    /**
     * Retrieves the properties at the specified index from the list of incoming dynamic document properties.
     * <p>
     * This method fetches the Properties object located at the given index in the list of incoming
     * dynamic document properties.
     * </p>
     *
     * @param index the index of the properties to retrieve
     * @return the Properties object at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Properties getProperties(int index) {

        return incomingDynamicDocumentProperties.get(index);
    }

}


