// Use for testing. Remove when adding to Data Process shape.
import com.boomi.execution.ContextCreator

// Place directory for multiple files and file name for single file
String pathFiles = "${System.getenv("PROJECT_DIR")}/input_files/its a csv.csv"
dataContext = new ContextCreator()
dataContext.AddFiles(pathFiles)
ExecutionUtil ExecutionUtil = new ExecutionUtil()

/* Add any Dynamic Process Properties or Dynamic Document Properties. If
   setting DDPs for multiple files, then index needs to be set for each and
   the index range starts at 0. */
ExecutionUtil.setDynamicProcessProperty("DPP_name", "DPP_value", false)
dataContext.addDynamicDocumentPropertyValues(0, "DDP_name", "DDP_value")


// Place script after this line.
//----------------------------------------------------------------------------------------------------


// Debug classes used to mimic Boomi. Used for DPP and logging. Include in Data
// Process shape.
import java.util.Properties
import java.io.InputStream
import com.boomi.execution.ExecutionUtil

logger = ExecutionUtil.getBaseLogger();

// Example of how to use logger.info() to print to console.
logger.info("Number of documents: " + dataContext.getDataCount())

for (int i = 0; i < dataContext.getDataCount(); i++) {
    InputStream is = dataContext.getStream(i)
    Properties props = dataContext.getProperties(i)

    BufferedReader reader = new BufferedReader(new InputStreamReader(is));

    int lineNum = 0;

    while ((line = reader.readLine()) != null) {
        lineNum++;
        println line
    }

    // Set properties
    props.setProperty("document.dynamic.userdefined.lineCount", lineNum.toString())

    // Reset the InputStream data
    is.reset()
    
    dataContext.storeStream(is, props)
}
