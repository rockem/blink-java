# blink-java 
[![CircleCI](https://circleci.com/gh/rockem/blink-java.svg?style=svg)](https://circleci.com/gh/rockem/blink-java)

blink is simplified http server, made primarily for using in tests.
It has no dependencies other than those that come with Oracle's Jdk
and it's inspired by Spark and Sinatra frameworks

## Dependency
### Maven
```xml
<repositories>
    <repository>
      <id>jcenter</id>
      <url>https://jcenter.bintray.com/</url>
    </repository>
</repositories>
```
```xml
<dependency>
    <groupId>org.rockem</groupId>
    <artifactId>blink-java</artifactId>
    <version>0.5.3</version>
</dependency>
```
### Gradle
```groovy
repositories {
    jcenter()
}
```
```groovy
dependencies {
    compile 'org.rockem:blink-java:0.5.3'
}
```

## Usage
### Hello World
#### Java
```java
new BlinkServer(1234) {{
	get("/hello", (req, res) -> "Hello World");
}};
```
#### Groovy
```groovy
new BlinkServer(1234) {{
	get("/hello", { req, res -> "Hello World" })
}}
```
### Path parameters
```java
new BlinkServer(1234) {{
	delete("/hello/{id}", (req, res) -> "Delete " + req.pathParam("id"));
}};
```
### Default content type
```java
new BlinkServer(1234) {{
    contentType("application/json")
    get("/hello", (req, res) -> "{\"greeting\": \"Hello World\"}");
}};
```
### Request
```java
req.body()                      // request body
req.param("name")               // Query parameter
req.pathParam("name")           // Path parameter
req.uri()                       // Request uri
req.header("name")              // header value
req.cookie("name")              // cookie value
```
### Response
```java
res.status(201)                 // set retrun status code
res.header("name", "value")     // Set header 
res.type("type")                // Set content type
res.cookie("name", "value")     // Add/Update cookie
```
