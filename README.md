# Zonky scheduler

## Task
Create service which will retrieve new loans from [zonky marketplace](https://api.zonky.cz/loans/marketplace) 
every 5 minutes and log them.

## Solution
First thing which i have to decide was which scheduling solution i will use for this task. I was deciding between standard 
scheduler from Spring or Quartz scheduler. For this solution i decided to use Spring scheduler because of the size of 
the project. For the implementation i decided to use Spring Boot 2 and for the REST communication with the marketplace
i used new reactive client from Spring Web Reactive module called WebClient which should be to the future replacement of 
RestTemplate.

### Knowledge from documentation

[documentation](http://docs.zonky.apiary.io/#)

While i was reading the documentation i spotted that the API supports filtering so in this implementation is also used 
filtration by `datePublished__gt` query parameter which should increase the performance and reduce amount of transferred 
data.

One of the requirement in the documentation was to send header `User-Agent` with application name, application version 
and organization name. This header is configured in 
[WebClientConfig](src/main/java/com/wexom/zonkydemo/marketplace/config/WebClientConfig.java) 
and values are loaded from [pom.xml](pom.xml) through the [application.properties](src/main/resources/application.properties).

### Scheduling 

For writing down new loans i used logger and did some formatting in 
[LoanScheduledTask](src/main/java/com/wexom/zonkydemo/marketplace/task/LoanScheduledTask.java)
where the whole scheduling process is happening. Basically method in this class is triggered every 5 minutes, it will 
decide from which date we would like to get new loans and request new loans through the service and client layer and 
then writes results to the logs.

### Testing

Because the testing is also important i decided that as an example i will do implement one JUnit test class and one 
Integration test class. For the whole testing solution i used JUnit 5 framework instead of JUnit 4.

JUnit tests:
[LoanServiceTest](src/test/java/com/wexom/zonkydemo/marketplace/service/LoanServiceTest.java)

for JUnit tests implementation i decided to use Mockito.

Integration tests:
[ZonkyClientTest](src/test/java/com/wexom/zonkydemo/marketplace/api/ZonkyClientTest.java)

for Integration tests implementation i was thinking which mock server i will use. My options were `MockServer`, 
`Wiremock`, `Restito from Xebialabs` and others. Because i did not have any previous experience with Restito and i 
really wanted to try it, i decided to use it and give it a try.

## Application prerequisites
* Java 8+
* Maven
* Access to the internet so Maven will be able to download dependencies :-)