#TemperatureSensor

 ### Task

Create an API, to be used by a client connected to a temperature sensor. The sensor sends to the
client a continuous stream of temperature data that must be sent to the API. It's possible for the
client not to have an internet connection in which case, the data is stored locally and synchronized,
in bulk, to the API as soon as the network connection is established. The client displays this
information in a chart where data can be seen per hour or daily, however since the client has
resource limitations, it relies on the API to aggregate data.

Create the following endpoints in your API:
- an endpoint to save temperature data
- an endpoint to retrieve the aggregated temperature data (hourly, daily). Performance is
  critical on this endpoint so it should return the data as fast as possible.


**1. Clone the repository**

```bash
 https://github.com/MithileshRavindiran/TemperatureSensor.git
```

**2. Run the app using maven**

```bash
mvn spring-boot:run
```

The application can be accessed at `http://localhost:8191`.


**3. API Documentation**


The application documentation can also be accessed at `http://localhost:8191/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/temperature-record-controller/` .Also can be tested from swagger end point too

Added postman collection on  the project 


**4. Improvements to Make**
  * Authentication  and Authorization has to be included so  that deviceId can be  taken from  the JWT tokens
  * Time series database like Influx, AWS time stream should be  more opt for the situation 
  * AWS/Azure Deployment to  collect resources from device across the world

