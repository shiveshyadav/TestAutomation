Build and run the service locally

backend-for-testers application should be up and running.

Build the application , wait for dependency to download first time

Update base url in src/env.properties file

Run the test using maven command and pass the value in tag to run specific test
mvn -Dcucumber.filter.tags=@users test

summary reports will be generated under target/reports/test-report/cucumber-html-report.html

To generate detailed report 

-Dmaven.test.skip=true verify

Detailed reports will be generated under target/cucumber-report-html

Add new Features :
New Features can be added under src/test/resources/features/
