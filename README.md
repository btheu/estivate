# JSoupMapper

Mapping from DOM to POJO with <a href="http://jsoup.org/">JSoup API</a>

# Getting Started

Mapping a DOM document to a POJO is very easy.

```java
InputStream document = ...

JsoupMapper mapper = new JsoupMapper();

Result result = mapper.map(document, Result.class);
```

Definition of Result class POJO definition which is:

1. Select an JSoup Element with cssQuery ``` "#nameId" ```  on the document.
* Apply JSoup ``` element.text() ``` on the Element selected.
* Set the result to the ``` name ``` field.

```java
public class Result {

	@Text(select="#nameId")
	public String name;

}
```

Giving this simple HTML, the POJO's ```name``` field will be set with ```"This is my name" ```

```html
<html>
	<head></head>
	<body>
		<div id="nameId">This is my name</div>
	</body>
</html>
```

### Mapping of collection

```java
InputStream document = ...

JsoupMapper mapper = new JsoupMapper();

List<Result> result = mapper.mapToList(document, Result.class);
```

```java
@Select("div.someClass")
public class Result {

	@Text(select=".name")
	public String name;

}
```

### Annotation on methods

JSoupMapper's annotations can be used directly on methods. 
This provides a way to implement custom operations just after mapping.

```java
public class Result {

	public String name;
	
	@Text(select="#nameId")
	public void setName(String pName){
		this.name = pName.substring(0,3).toUpperCase();
	}

}
```

### ```@Select``` 

Makes JSoup's ``` element.select(...) ``` operation on the DOM Document.

Do cssQuery on the DOM Document then return the DOM Element corresponding. 
When combined with ``` @Text ``` (or ``` @Attr ```), the 
final result will be the application of ```text()``` (or ``` attr(...) ```)
on this DOM Element.

```java
public class Result {

	@Text(select="div#content > span p")
	public String description;

}
```

Also, the JSoup Element object can be mapped to the field or method.

```java
public class Result {

	@Select(select="div#content > span p")
	public Element paragraphElement;

}
```

Method mapping is a way to perform further JSoup operations.

```java
public class Result {

	public String name;
	
	@Select(select="div#content > span p")
	public void setName(Element pElement){
		name = pElement.siblingNodes().first().text();
	}

}
```

### ```@Text```

Makes JSoup's ``` element.text() ``` operation on the DOM Element when own attribute is set to false.

Maps the combined text of this element and all its children. Whitespace is 
normalized and trimmed.

```java
public class Result {

	@Text(select="#description")
	public String description;

}
```

Makes JSoup's ``` element.ownText() ``` operation on the DOM Element when value is true.

Maps the text owned by this element only; does not get the combined text of all children. 

```java
public class Result {

	@Text(select="#description")
	public String description;

}
```

### ```@Attr```

Makes JSoup's ``` element.attr(...) ``` operation on the DOM Element.

Maps an attribute's value by its key. To get an absolute URL from an attribute
that may be a relative URL, prefix the key with abs, which is a shortcut to
the absUrl method. E.g.:

```java
public class Result {

	@Attr(select="#picture", value="abs:href")
	public String absoluteUrl;

}
```

### ```@Optional```

Indicate that JSoupMapper wont throw a exception if the mapping of this field
or method is not satisfied.

```java
public class Result {

	@Text(select="#description", optional=true)
	public String description;

}
```

### ```@TagName```

Makes JSoup's ``` element.TagName() ``` operation on the DOM Element.

Maps the name of the tag for this element. E.g. div

### ```@Title```

Makes JSoup's ``` element.title() ``` operation on the DOM Document.

Maps the string contents of the document's title element.

```java
public class Result {

	@Title
	public String pageTitle;

}
```

### ```@Val```

Makes JSoup's ``` element.val() ``` operation on the DOM Element.

Maps the value of a form element (input, textarea, etc).

```java
public class Result {

	@Val("#form_field_1")
	public String name;

}
```

### Recursive mapping

#### Single Element

POJO can have complexe mapping having sub POJO themself mapped with a sub DOM Element.

```java
public class Page {

	@Select(select="div#content2")
	public Content content;

}

/**
* All fields will be mapped with the sub DOM 
* selected by <code>Page</code> content rule
*/
public class Content {

	@Text(select=".name")
	public String name;
	
	@Text(select=".description")
	public String description;

}
```

The ```name``` field will be setted as ```"Actual name2"``` with the following HTML.


```html
<html>
	<head></head>
	<body>
		<div id="content1">
			<div class="name">
				Actual name1
			</div>
			...
			<div class="description">
				This is the description of content 1.
			</div>
		</div>
		<div id="content2">
			<div class="name">
				Actual name2
			</div>
			...
			<div class="description">
				This is the description of content 2.
			</div>
		</div>
	</body>
</html>
```


#### List of Element

```java
public class Page {

	@Select(select="div.article p")
	public List<Article> articles;

}

/**
* All fields will be mapped with the sub DOM 
* selected by <code>Page</code> articles rule for one <code>P</code>
*/
// JSoupSelectList is not necessary as long Page already specify the select rule.
public class Article {

	@Text(select=".author")
	public String author;
	
	@Text(select=".date")
	public String date;

}
```

This will perfectly macht all aticles giving this HTML DOM.

```html
<html>
	<head></head>
	<body>
		<div class="article">
			<p>
				<div class="author">
					Author first article.
				</div>
				...
				<div class="date">
					Nov. 1st 2015
				</div>
			</p>
		</div>
		... 
		<div class="article">
			<p>
				<div class="author">
					Author last article.
				</div>
				...
				<div class="date">
					Nov. 30th 2015
				</div>
			</p>
		</div>
	</body>
</html>
```

#### Primitive types

JSoupMapper handle primitive types for fields or methods arguments mapping.

```java
public class Rapport {

	@Text(select="#nbTeachers")
	public Integer ;
	
	@Text(select="#nbStudents")
	public int ;

}
```

```html
<html>
	<head></head>
	<body>
		<div id="nbTeachers">
			123
		</div>
		<div id="nbStudents">
			456
		</div>
	</body>
</html>
```

