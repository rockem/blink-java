# blink-java
Simplified http server, made primarily for using in tests
It also doesn't have any dependencies other than those that come with Oracle's Jdk 

## Examples
```java
new BlinkServer(1234) {{
	get("/hello", (req, res) -> "Hello World")
}}
```
```java
new BlinkServer(1234) {{
	delete("/hello/{id}", (req, res) -> "Delete " + req.pathParam("id"))
}}
```
</code>
