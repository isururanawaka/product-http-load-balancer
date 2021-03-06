# HTTP Load Balancer On Top Of WSO2 Gateway
This is a Google Summer of Code (GSoC 2016) Project.  It is under development.

 `Abstract:` http://summerofcode.withgoogle.com/projects/#6175096838291456

 `Proposal:` http://docs.google.com/document/d/1Agl-Y_UKM5eMon8IDZa02aDGj0Yh_vZ7kFC2b1IzFI4/edit?usp=sharing
 
 `Mid Term :` https://docs.google.com/document/d/1kEk706KkwjK3gFpjx56vNwAKzVQpa-G52MHIC_I7-0Y/edit?usp=sharing
 
High Level Architecture
-----------------------

![alt tag] (docs/High Level Architecture.PNG)


Building Product
----------------

- Clone and do `mvn install` on https://github.com/wso2/carbon-gateway-framework.git as this product
  is dependent on it.

- Then do `mvn install` on this product.

- Now Clone and do `mvn install` on https://github.com/Venkat2811/carbon-gateway-framework.git as it 
  has support for LB.

- Again do `mvn install` on this product.

- Goto `<CARBON_HOME>\product-http-load-balancer\product\target\` and 
  extract `wso2gwlbserver-1.0.0-SNAPSHOT.zip`.

- Then goto `<CARBON_HOME>\product-http-load-balancer\product\target\wso2gwlbserver-1.0.0-SNAPSHOT\wso2gwlbserver-1.0.0-SNAPSHOT\bin` and start `carbon.bat` or `carbon.sh` accordingly. 


Sample Configuration
--------------------

```
@startuml

participant StocksInbound : InboundEndpoint(protocol("http"),port("8290"),context("/stocks"))

participant StocksPipeline : Pipeline("Stocks_Flow")

participant Endpoint1 : OutboundEndpoint(protocol("http"),host("http://localhost:8080/stockquote/all"))

participant Endpoint2 : OutboundEndpoint(protocol("http"),host("http://localhost:8082/stockquote/all"))

StocksInbound -> StocksPipeline : "client request"

LoadBalancer(algorithm(name(ROUND_ROBIN)),persistence(type(LB_COOKIE)),healthCheck(type(PASSIVE),requestTimeout(1m),unHealthyRetries(5times),healthyRetries(1time),healthyCheckInterval(5m)))

StocksPipeline -> StocksInbound : "Final Response"

@enduml

```

More Samples
------------

You can find more samples in `/product/carbon-home/samples/`.


Traffic Flows
-------------

##### Simple HTTP
Traffic between `Client -> LB -> Back-End` and `Back-End -> LB -> Client` are un-encrypted.

![alt tag] (docs/Simple HTTP.PNG)

##### SSL Offload
 - Traffic from `Client -> LB` and `LB -> Client` are encrypted.
 - Traffic from `LB -> Back-End` and `Back-End -> LB` are un-encrypted.

![alt tag] (docs/SSL Offload.PNG)

##### SSL Re-Encryption
Traffic between `Client -> LB -> Back-End` and `Back-End -> LB -> Client` are encrypted.


![alt tag] (docs/SSL ReEncrypt.PNG)








