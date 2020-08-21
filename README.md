# Demo project

This is a spring boot java project that can be imported in any dev tool such as Intellij IDE or Eclipse.

The project uses gradle as its build tool.

Demonstrate building a simple Order (Stock) accepting app using JAVA Web stack and features writing test cases.

# Run application

- To run the application within IDE
- go to Gradle > Tasks > bootRun

# Local Test

Once the server has started, invoke below URLs in your browser -

check server is running:  
-http://localhost:8080/ping

send test orders, uses a test file to generate orders.
-http://localhost:8080/test

check accepted orders (will return json)
-http://localhost:8080/getAccepted

check rejected orders (will return json)
-http://localhost:8080/getRejected

- Run Test cases

Execute Tests:
- OrderManagementServiceTest
- OrderValidatorServiceTest





