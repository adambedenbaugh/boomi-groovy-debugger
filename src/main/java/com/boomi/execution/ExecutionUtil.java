package com.boomi.execution;

import java.util.Date;
import java.util.Properties;

/**
 * The ExecutionUtil class is used to mock Boomi's ExecutionUtil class within the Data process Shape.
 */
public class ExecutionUtil {

    private Properties _processProperties;
    private Properties _dynamicProcessProperties;

    /**
     * Initializes a new instance of the ExecutionUtil class.
     * <p>
     * This constructor initializes the process properties and dynamic process properties. It sets up
     * the initial process properties with the keys "lastrun" and "lastsuccessfulrun", both having the
     * current date and time as their values.
     * </p>
     */
    public ExecutionUtil() {
        _processProperties = new Properties();
        _dynamicProcessProperties = new Properties();

        _processProperties = new Properties();
        _processProperties.put("lastrun", new Date().toString());
        _processProperties.put("lastsuccessfulrun", new Date().toString());

    }

    /**
     * Sets a process property with the specified key and value.
     * <p>
     * This method adds or updates a process property identified by the given key with the specified value.
     * The component ID parameter is currently not used and only there to mock Boomi's setProcessProperty().
     * </p>
     *
     * @param componentId the ID of the Process Propertycomponent (currently unused)
     * @param propertyKey the key of the property to set
     * @param propertyValue the value of the property to set
     */
    public void setProcessProperty(String componentId, String propertyKey, String propertyValue) {
        _processProperties.put(propertyKey, propertyValue);
    }

    /**
     * Retrieves the value of a process property with the specified key.
     * <p>
     * This method retrieves the value of a process property identified by the given key.
     * If the property is not found, it returns an empty string. The component ID parameter
     * is not used and only there to mock Boomi's getProcessperty().
     * </p>
     *
     * @param componentId the ID of the component (currently unused)
     * @param propertyKey the key of the property to retrieve
     * @return the value of the property as a string, or an empty string if the property is not found
     */
    public String getProcessProperty(String componentId, String propertyKey) {
        Object result = _processProperties.get(propertyKey);

        if (result == null) {
            return "";
        } else {
            return result.toString();
        }
    }

    /**
     * Retrieves the value of a dynamic process property with the specified name.
     * <p>
     * This method fetches the value of a dynamic process property identified by the given name.
     * If the property is not found, it returns an empty string.
     * </p>
     *
     * @param propertyName the name of the dynamic property to retrieve
     * @return the value of the dynamic property as a string, or an empty string if the property is not found
     */
    public String getDynamicProcessProperty(String propertyName) {
        Object result = _dynamicProcessProperties.get(propertyName);

        if (result == null) {
            return "";
        } else {
            return result.toString();
        }
    }

    /**
     * Sets a dynamic process property with the specified name and value.
     * <p>
     * This method adds or updates a dynamic process property identified by the given name with the specified value.
     * The persist parameter is currently not used in this implementation.
     * </p>
     *
     * @param propertyName the name of the dynamic property to set
     * @param propertyValue the value of the dynamic property to set
     * @param persist a flag indicating whether the property should be persisted (currently unused)
     */
    public void setDynamicProcessProperty(String propertyName, String propertyValue, Boolean persist) {
        _dynamicProcessProperties.put(propertyName, propertyValue);
    }

    /**
     * Creates and returns a new instance of the Logger.
     * <p>
     * This method initializes and returns a new Logger object. The Logger can be used to log messages
     * at different severity levels.
     * </p>
     *
     * @return a new instance of the Logger
     */
    public Logger getBaseLogger() {
        return new Logger();
    }
}

