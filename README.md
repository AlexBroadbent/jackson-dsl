
![Github Actions Build](https://img.shields.io/github/workflow/status/AlexBroadbent/jackson-dsl/CI)
[![codecov](https://codecov.io/gh/AlexBroadbent/jackson-dsl/branch/master/graph/badge.svg?token=zHRcRB6bLk)](https://codecov.io/gh/AlexBroadbent/jackson-dsl)
![Maven Central](https://img.shields.io/maven-central/v/uk.co.alexbroadbent/jackson-dsl)
---

# Jackson DSL


## UPDATE:
This project has been archived in favour of [JSON-DSL](https://github.com/AlexBroadbent/json-dsl), which supports both Jackson and Gson in a single project.

The artifacts have changed to:
```
implementation("com.abroadbent:jackson-dsl:0.2.0")
implementation("com.abroadbent:gson-dsl:0.2.0")
```


---



A type-safe builder wrapped around the [Jackson JSON library](https://github.com/FasterXML/jackson).

The aim of this project is to reduce the amount of boilerplate code required to instantiate JSON objects in code.

 

## Examples


### Primitives

The following Koltin data types are supported via the Jackson equivalents:

Kotlin Type Parameter | Jackson Type Equivalent
---|---
String | TextNode
Int | TextNode
Long | LongNode
Double | DoubleNode
Boolean | BooleanNode

The functions are used in objects via the `put` method, and in arrays via the `add` method. _The usage of the type-based function names has been deprecated in order to align closer to the Jackson API._


### Objects

The `object` (or `obj` as an alias) function provides a wrapper for the Jackson Type `ObjectNode`, which takes a `key` and a `value` where the `value` is any primitive, object or array.

_Note that the `object` function is wrapped with backticks (\`) as "object" is a keyword in Kotlin._ 

The function:

```kotlin
val json = `object` {
    put("one", "two")
    put("three", 4)
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
    add(67214621784621)
    add(true)
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
    add("foo")
    array {
        add("bar")
        `object` {
            put("two", "three")
            `object`("four") {
                put("five", 6)
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
