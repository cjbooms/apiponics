package com.cjbooms.fabrikt.cli

enum class CodeGenerationType(val description: String) {
    HTTP_MODELS(
        "Jackson annotated data classes to represent the schema objects defined in the input."
    ),
    CONTROLLERS(
        "Spring / Micronaut annotated HTTP controllers for each of the endpoints defined in the input."
    ),
    CLIENT(
        "Simple http rest client."
    ),
    QUARKUS_REFLECTION_CONFIG(
        "This options generates the reflection-config.json file for quarkus integration projects"
    );

    override fun toString() = "`${super.toString()}` - $description"
}

enum class ClientCodeGenOptionType(private val description: String) {
    RESILIENCE4J("Generates a fault tolerance service for the client using the following library \"io.github.resilience4j:resilience4j-all:+\"");

    override fun toString() = "`${super.toString()}` - $description"
}

enum class ModelCodeGenOptionType(val description: String) {
    X_EXTENSIBLE_ENUMS("This option treats x-extensible-enums as enums"),
    JAVA_SERIALIZATION("This option adds Java Serializable interface to the generated models"),
    QUARKUS_REFLECTION("This option adds @RegisterForReflection to the generated models. Requires dependency \"'io.quarkus:quarkus-core:+\""),
    MICRONAUT_INTROSPECTION("This option adds @Introspected to the generated models. Requires dependency \"'io.micronaut:micronaut-core:+\""),
    MICRONAUT_REFLECTION("This option adds @ReflectiveAccess to the generated models. Requires dependency \"'io.micronaut:micronaut-core:+\"");

    override fun toString() = "`${super.toString()}` - $description"
}

enum class ControllerCodeGenOptionType(val description: String) {
    SUSPEND_MODIFIER("This option adds the suspend modifier to the generated controller functions"),
    MICRONAUT("This option causes the generated controllers to use the Micronaut framework instead of the Spring framework.");

    override fun toString() = "`${super.toString()}` - $description"
}