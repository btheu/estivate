# Estivate

Mapping from DOM to POJO with CSS Query Syntax and annotations.

Estivate use <a href="http://jsoup.org/">JSoup API</a> for inside CSS queries.

# Getting Started

Giving this simple HTML, we want to the POJO's ```name``` field set with the body of ```#nameId``` element. 

```html
<html>
	<head></head>
	<body>
		<div id="nameId">This is my name</div>
	</body>
</html>
```

```java
public class Result {

	@Text(select="#nameId")
	public String name;

}
```

Mapping a DOM document to a POJO is very easy.

```java
InputStream document = ...

EstivateMapper mapper = new EstivateMapper();

Result result = mapper.map(document, Result.class);
```

Definition of Result class POJO definition which is:

1. Select an JSoup Element with cssQuery ``` "#nameId" ```  on the document.
* Apply JSoup ``` element.text() ``` on the Element selected.
* Set the result to the ``` name ``` field.


## Download

```xml
<dependency>
	<groupId>com.github.btheu.estivate</groupId>
	<artifactId>estivate</artifactId>
	<version>0.4.0</version>
</dependency>
```

### Mapping of collection

```java
InputStream document = ...

EstivateMapper mapper = new EstivateMapper();

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

Estivate's annotations can be used directly on methods. 
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

### ```@Table & @Column```

Parse Table HTML DOM and match data by column name

Each column are mapped to java class field/method.

```java
InputStream document = ...

EstivateMapper mapper = new EstivateMapper();

List<Result> result = mapper.mapToList(document, Result.class);
```

```java
@Table(select="#table1")
public class Result {
	@Column("Number Column")
	public int number;
	@Column("Street Column")
	public int street;
	@Column(name="Name.*Column", regex=true)
	@Attr(select="span", value="title")
	public String name;
}
```

```html
<html>
<head>
	<title>table-u1</title>
</head>
<body>
	<div id="content">


<table id="table1">
<thead>
<tr>
	<th><span>Number Column</span></th>
	<th><span>Street Column</span></th>
	<th><span>Name Column</span></th>
</tr>
</thead>
<tbody>
<tr>
  <td>
	<span>1</span>
  </td>
  <td>
	<span>streetA</span>
  </td>
  <td>
	<span title="nameA" />
  </td>
</tr>
<tr>
  <td>
	<span>2</span>
  </td>
  <td>
	<span>streetB</span>
  </td>
  <td>
	<span title="nameB" />
  </td>
</tr>
<tr>
  <td>
	<span>3</span>
  </td>
  <td>
	<span>streetC</span>
  </td>
  <td>
	<span title="nameC" />
  </td>
</tr>
</tbody>
</table>

</body>
</html>
```

### ```@Is```

Makes JSoup's ``` element.is(...) ``` operation on the DOM Element.

Check if this element matches the given Selector CSS query.

```java
public class Result {

	@Is(select="#setting", value=".specific")
	public boolean isSpecific;

}
```

### ```Optional```

Indicate that Estivate wont throw a exception if the mapping of this field
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

```java
public class Result {

	@TagName(select=".picture", first=true)
	public String pictureTagName;

}
```

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

Estivate handles primitive types for fields or methods arguments mapping.

```java
public class Rapport {

	@Text(select="#nbTeachers")
	public Integer numberOfTeachers;
	
	@Text(select="#nbStudents")
	public int numberOfStudents;

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

# License MIT

The MIT License

© 2016-2017, Benoit Theunissen <benoit.theunissen@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
