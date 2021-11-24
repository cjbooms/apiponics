package examples.enumExamples.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import kotlin.String
import kotlin.collections.List

data class EnumHolder(
    @param:JsonProperty("array_of_enums")
    @get:JsonProperty("array_of_enums")
    val arrayOfEnums: List<EnumHolderArrayOfEnums>? = null,
    @param:JsonProperty("inlined_enum")
    @get:JsonProperty("inlined_enum")
    val inlinedEnum: EnumHolderInlinedEnum? = null,
    @param:JsonProperty("inlined_extensible_enum")
    @get:JsonProperty("inlined_extensible_enum")
    val inlinedExtensibleEnum: EnumHolderInlinedExtensibleEnum? = null,
    @param:JsonProperty("enum_ref")
    @get:JsonProperty("enum_ref")
    val enumRef: EnumObject? = null,
    @param:JsonProperty("extensible_enum_ref")
    @get:JsonProperty("extensible_enum_ref")
    val extensibleEnumRef: ExtensibleEnumObject? = null
)

enum class EnumHolderArrayOfEnums(
    @JsonValue
    val value: String
) {
    ARRAY_ENUM_ONE("array_enum_one"),

    ARRAY_ENUM_TWO("array_enum_two");
}

enum class EnumHolderInlinedEnum(
    @JsonValue
    val value: String
) {
    INLINED_ONE("inlined_one"),

    INLINED_TWO("inlined_two"),

    INLINED_THREE("inlined_three");
}

enum class EnumHolderInlinedExtensibleEnum(
    @JsonValue
    val value: String
) {
    INLINED_ONE("inlined_one"),

    INLINED_TWO("inlined_two"),

    INLINED_THREE("inlined_three");
}

enum class EnumObject(
    @JsonValue
    val value: String
) {
    ONE("one"),

    TWO("two"),

    THREE("three");
}

enum class ExtensibleEnumObject(
    @JsonValue
    val value: String
) {
    ACTIVE("active"),

    INACTIVE("inactive");
}

enum class ContentType(
    @JsonValue
    val value: String
) {
    APPLICATION_JSON("application/json"),

    APPLICATION_X_SOME_TYPE_JSON("application/x.some-type+json"),

    APPLICATION_X_SOME_OTHER_TYPE_JSON_VERSION_2("application/x.some-other-type+json;version=2");
}
