
![Github Actions Build](https://img.shields.io/github/workflow/status/ajab/jackson-dsl/CI)
![Codecov](https://img.shields.io/codecov/c/github/ajab/jackson-dsl)

---

# Jackson DSL


A type-safe builder wrapped around the [Jackson JSON library](https://github.com/FasterXML/jackson).

 

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

The `object` function provides a wrapper for the Jackson Type `ObjectNode`, which takes a `key` and a `value` where the `value` is in the type of the function.

_Note that the object function is wrapped with backticks (\`) as it is a keyword in Kotlin._ 

The function:

```kotlin
val obj = `object` {
    string("one", "two")
    int("three", 4)
}
```

produces the object:

```json5
{
  "one": "two",
  "three": 4
}
```


### Arrays

The `array` function provides a wrapper for the Jackson Type `ArrayNode`, which takes a set of values: primitives, objects arrays.

The function:

```kotlin
val obj = array {
    long(67214621784621)
    boolean(true)
}
```

produces the array:

```json5
[
  67214621784621,
  true
]
```


---

### Nesting

Objects and Arrays can be nested, to any supported depth by the Jackson JSON library.

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

produces the array:

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
