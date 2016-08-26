# blink-java
Simplified http server, made primarily for using in tests
It also doesn't have any dependencies other than those that come with Oracle's Jdk 

## Artifactory 
The package is available at https://bintray.com/rockem/maven/blink-java

## Examples
```java
new BlinkServer(1234) {{
	get("/hello", (req, res) -> "Hello World");
}};
```
```java
new BlinkServer(1234) {{
	delete("/hello/{id}", (req, res) -> "Delete " + req.pathParam("id"));
}};
```
</code>
