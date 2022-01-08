
![Github Actions Build](https://img.shields.io/github/workflow/status/AlexBroadbent/jackson-dsl/CI)
[![codecov](https://codecov.io/gh/AlexBroadbent/jackson-dsl/branch/master/graph/badge.svg?token=zHRcRB6bLk)](https://codecov.io/gh/AlexBroadbent/jackson-dsl)
![Maven Central](https://img.shields.io/maven-central/v/uk.co.alexbroadbent/jackson-dsl)
---

# Jackson DSL


A type-safe builder wrapped around the [Jackson JSON library](https://github.com/FasterXML/jackson).

The aim of this project is to reduce the amount of boilerplate code required to instantiate JSON objects in code.

 

## Examples


### Primitives

The following functions provide basic primitive types:

Function | Kotlin Type Parameter | Jackson Type Equivalent | Example
---|---|---|---
string | String | TextNode | `string("foobar")`  
int | Int | TextNode | `int(18)`  
long | Long | LongNode | `long(1575324315000)`  
double | Double | DoubleNode | `double(3.14)`  
boolean | Boolean | BooleanNode | `boolean(true)`


### Objects

The `object` (or `obj` as an alias) function provides a wrapper for the Jackson Type `ObjectNode`, which takes a `key` and a `value` where the `value` is any primitive, object or array.

_Note that the `object` function is wrapped with backticks (\`) as "object" is a keyword in Kotlin._ 

The function:

```kotlin
val json = `object` {
    string("one", "two")
    int("three", 4)
}
```

produces the object:

```json
{
  "one": "two",
  "three": 4
}
```

which is equivalent to:

```kotlin
val jackson = mapper.createObjectNode()
  .put("one", "two")
  .put("three", 4)
```


### Arrays

The `array` function (or `arr` as an alias) provides a wrapper for the Jackson Type `ArrayNode`, which takes a list of any primitives, objects and arrays.

The function:

```kotlin
val json = array {
    long(67214621784621)
    boolean(true)
}
```

produces the array:

```json
[
  67214621784621,
  true
]
```

which is equivalent to:

```kotlin
val jackson = mapper.createArrayNode()
  .add(67214621784621)
  .add(true)
```


---

### Nesting

Objects and Arrays can be nested to any supported depth by the Jackson JSON library.

For example:

```kotlin
val json = array {
    string("foo")
    array {
        string("bar")
        `object` {
            string("two", "three")
            `object`("four") {
                int("five", 6)
            }
        }
    }
}
```

produces the JSON array:

```json5
[
  "foo",
  [
    "bar",
    {
      "two": "three",
      "four": {
        "five": 6
      }
    }
  ]
]
```

which is equivalent to:

```kotlin
val nested2 = mapper.createObjectNode()
  .put("two", "three")
  .set<ObjectNode>(
    "four", mapper.createObjectNode()
      .put("five", 6)
  )
val nested1 = mapper.createArrayNode()
  .add("bar")
  .add(nested2)
val jackson = mapper.createArrayNode()
  .add("foo")
  .add(nested1)
```
