# JsonTemplate
**A Java tool for generating json strings.**

## Getting started

```xml
<dependency>
    <groupId>com.github.json-template</groupId>
    <artifactId>jsontemplate</artifactId>
    <version>0.1.0</version>
</dependency>
```

Suppose that we want to create the following json String,
```json
{
  "name" : "John",
  "age" : 23,
  "role" : [ "developer", "tester" ],
  "address" : {
    "city" : "Utrecht",
    "street" : "Musicallaan",
    "number" : 413
  }  
}
```

With JsonTemplate, you can do it in the following way. Compared to the typical solution,
JsomTemplate saves you effort in reading and writing the escaped quotes "\\"".
```java
String template = "{" +
                  "  name : John," +
                  "  age : 23," +
                  "  role : [ developer, tester ]," +
                  "  address : {" +
                  "    city : Utrecht," +
                  "    street : Musicallaan," +
                  "    number : 413" +
                  "  }" +
                  "}";
String json = new JsonTemplate(template).prettyString();                      
``` 

Furthermore, suppose that what you need is only a schema-compatible json and you don't want to bother with the specific
values. This is usually the case when you test the validation logic in the controllers. 
JsonTemplate allows you to code in the following way:

```java
String template = "{" +
                  "  name : @s," +
                  "  age : @i(max=100)," +
                  "  role : @s[](max=3)," +
                  "  address : {" +
                  "    city : @s," +
                  "    street : @s," +
                  "    number : @i" +
                  "  }" +
                  "}";
String json = new JsonTemplate(template).prettyString();                      
``` 

## Examples
### Smart conversion
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
{
  name : John,
  age : 23,
  accountBalance: 30.4,
  married : true,
  role : programmer
}
</pre></td><td><pre>
{
  "name" : "John",
  "age" : 23,
  "accountBalance": 30.4,
  "married" : true,
  "role" : "programmer"
}
</pre></td></tr>
</table>
JsonTemplate automatically converts a string into a json value with the type which the string
looks like. For example, "23" looks like a number, therefore it is converted to a json numeric value.

If this is not the intention, a specific **value producer** needs to be given, e.g. `@s(23)`.  


### Value producers 
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
{
  aField : @s
}
</pre></td><td><pre>
{
  "aField" : "ISCZd"
}
</pre></td></tr>
</table>

In JsonTemplate, **@x** can refer to a **value producer** or a **value producer declaration** 
(will be described later). When it is at the value part, it is a value producer. 

Following is the table about all the pre-installed value producers:

| value producer | description |
| ---        | ---         |
| @smart     | used for smart conversion, it is the default value producer which is implicitly used. |
| @s         | produces a string |
| @i         | produces an integer |
| @f         | produces a float |
| @b         | produces a boolean |
| @raw       | produces a raw string content | 
| @ip        | produces an ip string |
| @ipv6      | produces an ipv6 string |
| @base64    | produces a base64 string |

### Value producers with a single parameter
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
{
  aField : @s(myValue)
}
</pre></td><td><pre>
{
  "aField" : "myValue"
}
</pre></td></tr>
</table>

If a **single parameter** is given, the value producer produces a fixed value correspondingly by default.

### Value producers with a list parameter
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
{
  aField : @s(A, B, C, D)
}
</pre></td><td><pre>
{
  "aField" : "C"
}
</pre></td></tr>
</table>

If a **list parameter** is given, the value producer randomly selects a value from that list by default.

### Value producers with a map parameter
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
{
  aField : @s(length=10)
}
</pre></td><td><pre>
{
  "aField" : "awpVXpJTxb"
}
</pre></td></tr>
<tr><td><pre>
{
  aField : @s(min=10, max=20)
}
</pre></td><td><pre>
{
  "aField" : "awpVXpJTxb"
}
</pre></td></tr>
</table>

If a **map parameter** is given, the value producer produces a value according to the map values. 

Following is the table about whether each type of parameter is supported by the pre-installed producers.

| producer |  none              | single | list | map |
| ---      | ---                | ---    | ---  | --- |
| @smart   | no, produce null   | yes    | no   | no |
| @s       | yes                | yes    | yes  | yes, supported parameters: length, max, min |
| @i       | yes                | yes    | yes  | yes, supported parameters: max, min |
| @f       | yes                | yes    | yes  | yes, supported parameters: max, min |
| @b       | yes                | yes    | yes  | no |
| @raw     | no                 | yes    | no   | no |
| @ip      | yes                | no     | no   | no |
| @ipv6    | yes                | no     | no   | no |
| @base64  | yes                | no     | no   | yes, supported parameters: length |
 

### Json objects
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
{
  anObject : {
    aField : @s, 
    bField : @s
  }
}
</pre></td><td><pre>
{
  "anObject" : {
    "aField" : "hhnNc",
    "bField" : "EyHbB"
  }
}
</pre></td></tr>
</table>

A json object consists of a set of properties. Each property can be specified
with a value producer. The default type is @smart which is implicitly used.

### Array producer with size configuration
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
@s[](3)
</pre></td><td><pre>
[ "hwhCL", "tDcPO", "OgdGC" ]
</pre></td></tr>
<tr><td><pre>
@s[](1, 10)
</pre></td><td><pre>
[ "QwWxg", "ytaGY", "NGZBr", "DsBKx", "MvwSb", "qsEXA", "YHkxC" ]
</pre></td></tr>
</table>

Array tag **[]** indicates producing a json array. 
The default value producer for the array elements is placed before `[]`. 
The size configuration of the generated array is placed after `[]`. 

- `(3)` means size of 3 and it is a shorthand of `(size=3)`
- `(1, 10)` means the size range is between 1 and 10 and it is a short hand of
`(min=1, max=10)`

### Array producer with elements
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
@s [ 1, 2, 3, 4 ]
</pre></td><td><pre>
[ "1", "2", "3", "4" ]
</pre></td></tr>
<tr><td><pre>
@s [ 1, 2, 3, 4 ](6)
</pre></td><td><pre>
[ "1", "2", "3", "4", "qRTWm", "RTBik" ]
</pre></td></tr>
<tr><td><pre>
@s [ 1, @i(2), @b(false), @s(4) ]
</pre></td><td><pre>
[ "1", 2, false, "4" ]
</pre></td></tr>
</table>

- In the first example, it produces string elements.
- In the second exmaple, the size is greater than the amount of the listed elements.
The extra elements will be filled by the value producer. In this case, the values
are random strings of length 5. If the size is not greater, all listed elements
are remained without filled elements. Same rule applies to the range size.
- In the third example, each elements can have its own value producer the it
overwrites the default value producer.


### Customized value producer definition
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
@address : {
  city : @s,
  street : @s,
  number : @i
},
{
  office : @address, 
  home : @address
}
</pre></td><td><pre>
{
  "office" : {
    "city" : "MavBr",
    "street" : "odcjd",
    "number" : 79
  },
  "home" : {
    "city" : "zdNCm",
    "street" : "UsBcv",
    "number" : 63
  }
}
</pre></td></tr>
</table>

A **value producer** `@x` can be used for value producer declaration.  
The declaration can be before or after the json root, separated by comma `,`.
The order between multiple declarations do not matter.

### Default value producer
<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
@s {
  fieldA, 
  fieldB,
  fieldC: @i
}
</pre></td><td><pre>
{
  "fieldA" : "yUiIE",
  "fieldB" : "vrMwv"
  "fieldC" : 54
}
</pre></td><td>
</table>
 
In the above example, the default value producer is explicitly specified as `@s`.
If nothing is specified, `@smart` is used. In case of `@s`, it supports none parameters.
Therefore, the values of fieldA and fieldB can be omitted. `@s` will generate
a random value form them.

The mechanism of searching the default value producer is the same as Java inheritance:
It starts from itself to the root, util it finds a value producer.

## Inject variables
JsonTemplate allows to inject variables to the template, it provides two methods to do that:

- `JsonTemplate.withVar(String, Object)`
- `JsonTemplate.withVars(Map<String, Object>)`

### Use the variable as values

<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
{
  name : $name
}
</pre></td><td><pre>
{
  "name" : "John"
}
</pre></td></tr>
</table>

In the above example, token `$` indicates a variable. Variable `name` refers to a string `John`.


<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
{
  letters : $letters
}
</pre></td><td><pre>
{
  "letters" : [ "A", "B", "C" ]
}
</pre></td></tr>
</table>

In the above example, variable `letters` refers to an array ["A", "B", "C"] or a collection with
elements "A", "B", "C".

<table><tr><th width="600">Template</th><th width="50%">Generated Json</th></tr>
<tr><td><pre>
{
  person : $person
}
</pre></td><td><pre>
{
  "person" : {
    "languages" : [ "Chinese", "English", "Dutch" ],
    "name" : "Haihan",
    "age" : 33,
    "male" : true
  }
}
</pre></td></tr>
</table>

In the above example, variable `person` refers to an map object.


### Use variables as parameters
Variables can be also put in the parameters of a value producer. 

When variable used as a single parameter, the parameter type (single, list, or map)
is adjusted according to the type of the vaiable.

- If the variable refers to a collection or an array, it becomes a list parameter;
- If the variable refers to map, it becomes a map parameter;
- Otherwise, it keeps as a single parameter;

When variable used as list or map parameter, the semantics won't change.

## Customize value producers
JsonTemplate does not aim for providing the full-fledged value generation. Other libraries, such as
Guava, Apache Commons, JFaker, etc., provide powerful value generation apis.
 
The pre-installed value producers are designed in a way which can be extended. 
With `JsomTemplate.withValueProducer(IValueProducer)`, users can freely
extend the pre-installed values producers or create new ones.

Above is just a peek for the list of features.
For more examples, [examples.txt](test/resources/examples.txt) provides part of the logs in tests. 

## Support 
If you have issues, great ideas, or comments, please let us know. 
We have a mailing list located at: jsontemplate2019@gmail.com

 


