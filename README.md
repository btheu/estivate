# jsoup-mapper
Mapping from DOM to POJO with JSoup API

# Sample

Mapping a document to a POJO is quite simple.

```java
InputStream document = ...

JsoupMapper mapper = new JsoupMapper();

Result result = mapper.fromBody(document, Result.class);
```

With Result class POJO definition as follow :

```java
@Data
public static class Result {

	@JsoupSelect("#name")
	@JsoupText
	public String name;

}
```
