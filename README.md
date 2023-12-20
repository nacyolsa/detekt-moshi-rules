# Detekt Moshi Rules

Contains Detekt rule which helps to detect missing `@JsonClass(generateAdapter = true)` on data classes. It finds classes by keywords in their names.

## How to use it

Artifact is not published to public maven repo so for now you need to build jar manually.

1. Build project and publish it to your Maven repository.
2. Add it to your project by adding it to dependencies in the following way:
    ```kotlin
    dependencies {
        detektPlugins("org.nacyolsa.detekt.moshi:detekt-moshi-rules:1.0.0")
    }
    ```
3. Activate rule
    ```yaml
    MoshiRuleSet:
        MissingJsonClassAnnotation:
            active: true
            keywords: Request,Response,Event
            excludes: ['**/test/**']
    ```

Feel free to add more useful rules for Moshi.
