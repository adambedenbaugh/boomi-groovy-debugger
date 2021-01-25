// Use for testing. Remove when adding to Data Process shape.
import com.boomi.execution.ContextCreator

// Place directory for multiple files and file name for single file
String pathFiles = "${System.getenv("PROJECT_DIR")}/input_files/books.xml"
dataContext = new ContextCreator()
dataContext.AddFiles(pathFiles)
ExecutionUtil ExecutionUtil = new ExecutionUtil()



/*
 *
 * Place script after this line.
 *
 */



// Debug classes used to mimic Boomi. Used for DPP and logging. Include in Data
// Process shape.
import com.boomi.execution.ExecutionUtil
import com.boomi.execution.Logger

Logger logger = new Logger()

// Example of how to use logger.info() to print to console.
logger.info("Number of documents: " + dataContext.getDataCount())

for( int i = 0; i < dataContext.getDataCount(); i++ ) {
    InputStream is = dataContext.getStream(i)
    Properties props = dataContext.getProperties(i)

    String Data = is.getText()

    def slurper = new XmlSlurper(false, true)
    def BaseElement = slurper.parseText(Data)
    Author = BaseElement.book.author[0].text()

    ExecutionUtil.setDynamicProcessProperty("Author",Author, false)

    dataContext.storeStream(is, props)
}