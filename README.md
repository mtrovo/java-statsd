# java-statsd [<img src="https://secure.travis-ci.org/mtrovo/java-statsd.png" />](http://travis-ci.org/mtrovo/java-statsd)

[StatsD](https://github.com/etsy/statsd) client library for Java.
 
# Installing

For maven you can use:

``` 
<dependency>
    <groupId>com.mtrovo</groupId>
    <artifactId>java-statsd</artifactId>
    <version>1.0.0</version>
</dependency>
```

Or while in gradle you can use:
```groovy
dependencies {
  compile 'com.mtrovo:java-statsd:1.0.0'
}
```

And if you like to live on the bleeding edge of technology, you can checkout this code on the repository and generate a
package from it.
```sh
git clone https://github.com/mtrovo/java-statsd
cd java-statsd
gradle clean build install
```

# Basic Usage
```java
StatsdClient statsd = new StatsdClient(new UdpEndpoint(host, port));

// you can use metrics by name
statsd.counter.increment("my.awesomeness.counter");

// or you can cache its reference into a field and use it later
BCounter myAwesomeness = statsd.counter.build("my.awesomeness.counter");  
myAwesomeness.increment();

// for time buckets you can send your measurement
statsd.timing.sendTimeMs("backup.the.whole.interwebs", 123);

// or you can use Java 7 try-with-resources
try(Context _ = statsd.timing.time("process.user.very.important.stuff")){
  // code to be measured
}

// or you can use Java 8 lambdas
statsd.timing.time("measure.how.long.does.it.take", () -> {
  // code to be measured
});
```

Things to notice:

* It's useful to cache `StatsdClient` instance on a singleton or bundle it with the DI framework your using, they are thread-safe but you will end up with a open socket for each instance.
* The field reference syntax works for every measurement type, always using its `build` method.
* With the try-with-resources syntax all the code inside the try will be measured and sent to the backend as soon as the try code exit.
* Although the lambda version of time uses Runnable interface the code itself is executed on the caller's thread synchronously.   

# Testing
Run all the tests using
```
gradle check
```
Currently there's only unit tests on mock instances. 
Integration tests on the way.



