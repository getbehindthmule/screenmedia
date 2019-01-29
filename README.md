**Build The Project**

This project can be built and automated tests run via using gradle, for example

`./gradlew clean build`

**Run The Project**

It can then be run with the following command

`java -jar build/libs/greenhills-fuel-manager-0.1.0.jar`

**Sample REST Call**

The REST call can then be tested locally, for example

`http://localhost:8080/fuel/cost?from=2013-04-17&fuel_type=diesel&mpg=10&mileage=99.99`

It is recommended that a RESTful client such as POSTMAN be used for informal application testing.

** Sample JSON Response**

This call will return a json structure describing the cost, duty and comparison information in UK pence of the requested
journey. For example

`{
    "cost": 6440,
    "duty": 2634,
    "comparisonWithToday": -579
}`

Note that the _comparisonWithToday_ will return a negative value to indicate the loss when compared to current fuel costs. A 
positive value represents the fuel cost saving in comparison to current fuel costs.


**Notes**

The fuel cost csv is bundled within the delivered jar and is loaded at start of day. Ideally, this could be held in a separate
persistent store that can be updated and maintained separately. Note that the source CSV file was editted to remove the 
multiple header lines to simplify load logic. The file was also converted to UTF-8 character set.

The REST call uses a GET with request parameters within the request URL. This allows Spring validation to handle missing
parameters etc. However, when large amount of paramters are required in a REST call, a JSON strucutre may become more
appropriate

Further work would typically be done to configure for deployment on pipelines servers to production, including integrating
with security authentication framework, and supporting SSL.
