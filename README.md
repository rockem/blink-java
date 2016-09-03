# blink-java
Simplified http server, made primarily for using in tests
It also doesn't have any dependencies other than those that come with Oracle's Jdk 

## Artifactory 
This package is available at https://bintray.com/rockem/maven/blink-java

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
req.body()						// request body
req.param(String)				// Query parameter
req.pathParam(String)			// Path parameter
req.uri()						// Request uri
req.header(String)				// header value 
```
### Response
```java
res.status(int)					// set retrun status code
res.header(String, String)		// Set header 
res.type(String)				// Set content type
```