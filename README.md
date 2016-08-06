# blink-java
Simplified http server, made primarily for using in tests

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
