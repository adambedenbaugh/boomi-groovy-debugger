import com.boomi.execution.ExecutionUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ExecutionUtilTest {

    private ExecutionUtil executionUtil;

    @Before
    public void setUp() {
        executionUtil = new ExecutionUtil();
    }

    @Test
    public void testInitialization() {
        assertNotNull(executionUtil.getProcessProperty(null, "lastrun"));
        assertNotNull(executionUtil.getProcessProperty(null, "lastsuccessfulrun"));
    }

    @Test
    public void testSetAndGetProcessProperty() {
        String componentID = "component1";
        String propertyKey = "testKey";
        String propertyValue = "testValue";

        executionUtil.setProcessProperty(componentID, propertyKey, propertyValue);
        assertEquals(propertyValue, executionUtil.getProcessProperty(componentID, propertyKey));
    }

    @Test
    public void testGetNonExistingProcessProperty() {
        assertEquals("", executionUtil.getProcessProperty(null, "nonExistingKey"));
    }

    @Test
    public void testSetAndGetDynamicProcessProperty() {
        String propertyName = "dynamicKey";
        String propertyValue = "dynamicValue";

        executionUtil.setDynamicProcessProperty(propertyName, propertyValue, true);
        assertEquals(propertyValue, executionUtil.getDynamicProcessProperty(propertyName));
    }

    @Test
    public void testGetNonExistingDynamicProcessProperty() {
        assertEquals("", executionUtil.getDynamicProcessProperty("nonExistingDynamicKey"));
    }

    @Test
    public void testGetBaseLogger() {
        assertNotNull(executionUtil.getBaseLogger());
    }
}
