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

	@JsoupSelect("#nameId")
	@JsoupText
	public String name;

}
```

Giving this DOM HTML, the POJO's ```name``` field will be set with ```"This is my name" ```

```html
<html>
	<head></head>
	<body>
		<div id="nameId">This is my name</div>
	</body>
</html>
```

# Annotation on methods

JSoupMapper's annotations can be use directly on methods. This provides a way to implement custom operations.

```java
@Data
public static class Result {

	public String name;
	
	@JsoupSelect("#nameId")
	@JsoupText
	public void setName(String pName){
		this.name = pName.substring(0,3).toUpperCase();
	}

}
```
 

