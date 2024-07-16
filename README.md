#  boomi-groovy-debugger

Boomi is a low code environment for integrations, which means that most situations will not require coding, but occasionally 
it will be necessary. 
While Boomi can be helpful with testing, having an IDE will provide a substantially faster way to debug your Groovy code.

Detains on how to use and set up an IntelliJ to use this library can be found at
[Setting up IntelliJ to Test Groovy Code in Boomi](https://community.boomi.com/s/article/Setting-up-IntelliJ-to-Test-Groovy-Code-in-Boomi).

This project is a fork from [OfficialBoomi/pso/BoomiGroovyDebugger](https://github.com/OfficialBoomi/pso/tree/master/BoomiGroovyDebugger).

Below is an example script that would be run within an IDE (IntelliJ) to mock Boomi's Data Process shape.
The portion of the script above the line is context that would run when the Data Process shape is loaded. 
The portion of the script below the line is what would be seen within Boomi. 

```groovy
import com.boomi.execution.ContextCreator

// Set up for script to be tested
String pathFiles = "${System.getenv("PROJECT_DIR")}/input_files/emptyfile.txt"
dataContext = new ContextCreator()
dataContext.AddFiles(pathFiles)
ExecutionUtil ExecutionUtil = new ExecutionUtil()

ExecutionUtil.setDynamicProcessProperty("method","DPP_value",false)
dataContext.addDynamicDocumentPropertyValues(0,"DDP_name","DDP_value")

// Place Boomi script after this line.
//----------------------------------------------------------------------------------------------------

import java.util.Properties
import java.io.InputStream
import com.boomi.execution.ExecutionUtil

for(int i=0; i<dataContext.getDataCount(); i++){
    InputStream is=dataContext.getStream(i)
    Properties props=dataContext.getProperties(i)
 
    dataContext.storeStream(is, props)
}
```