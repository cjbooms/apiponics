package examples.enumPolymorphicDiscriminator.models

import com.fasterxml.jackson.`annotation`.JsonProperty
import com.fasterxml.jackson.`annotation`.JsonSubTypes
import com.fasterxml.jackson.`annotation`.JsonTypeInfo
import com.fasterxml.jackson.`annotation`.JsonValue
import javax.validation.Valid
import javax.validation.constraints.NotNull
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "some_enum",
    visible = true,
)
@JsonSubTypes(
    JsonSubTypes.Type(
        value = DiscriminatedChild1::class,
        name =
        "obj_one_only",
    ),
    JsonSubTypes.Type(
        value = DiscriminatedChild2::class,
        name =
        "obj_two_first",
    ),
    JsonSubTypes.Type(
        value = DiscriminatedChild2::class,
        name =
        "obj_two_second",
    ),
    JsonSubTypes.Type(value = DiscriminatedChild3::class, name = "obj_three"),
)
public sealed class ChildDefinition(
    public open val inlineObj: ChildDefinitionInlineObj? = null,
    public open val inlineArray: List<ChildDefinitionInlineArray>? = null,
    public open val inlineEnum: ChildDefinitionInlineEnum? = null,
) {
    public abstract val someEnum: ChildDiscriminator
}

public data class ChildDefinitionInlineArray(
    @param:JsonProperty("str")
    @get:JsonProperty("str")
    public val str: String? = null,
)

public enum class ChildDefinitionInlineEnum(
    @JsonValue
    public val `value`: String,
) {
    ONE("one"),
    TWO("two"),
    THREE("three"),
    ;

    public companion object {
        private val mapping: Map<String, ChildDefinitionInlineEnum> =
            values().associateBy(ChildDefinitionInlineEnum::value)

        public fun fromValue(`value`: String): ChildDefinitionInlineEnum? = mapping[value]
    }
}

public data class ChildDefinitionInlineObj(
    @param:JsonProperty("str")
    @get:JsonProperty("str")
    public val str: String? = null,
)

public enum class ChildDiscriminator(
    @JsonValue
    public val `value`: String,
) {
    OBJ_ONE_ONLY("obj_one_only"),
    OBJ_TWO_FIRST("obj_two_first"),
    OBJ_TWO_SECOND("obj_two_second"),
    OBJ_THREE("obj_three"),
    ;

    public companion object {
        private val mapping: Map<String, ChildDiscriminator> =
            values().associateBy(ChildDiscriminator::value)

        public fun fromValue(`value`: String): ChildDiscriminator? = mapping[value]
    }
}

public data class DiscriminatedChild1(
    @param:JsonProperty("inline_obj")
    @get:JsonProperty("inline_obj")
    @get:Valid
    override val inlineObj: ChildDefinitionInlineObj? = null,
    @param:JsonProperty("inline_array")
    @get:JsonProperty("inline_array")
    @get:Valid
    override val inlineArray: List<ChildDefinitionInlineArray>? = null,
    @param:JsonProperty("inline_enum")
    @get:JsonProperty("inline_enum")
    override val inlineEnum: ChildDefinitionInlineEnum? = null,
    @param:JsonProperty("some_prop")
    @get:JsonProperty("some_prop")
    public val someProp: String? = null,
    @get:JsonProperty("some_enum")
    @get:NotNull
    @param:JsonProperty("some_enum")
    override val someEnum: ChildDiscriminator = ChildDiscriminator.OBJ_ONE_ONLY,
) : ChildDefinition(inlineObj, inlineArray, inlineEnum)

public data class DiscriminatedChild2(
    @get:JsonProperty("some_enum")
    @get:NotNull
    override val someEnum: ChildDiscriminator,
    @param:JsonProperty("inline_obj")
    @get:JsonProperty("inline_obj")
    @get:Valid
    override val inlineObj: ChildDefinitionInlineObj? = null,
    @param:JsonProperty("inline_array")
    @get:JsonProperty("inline_array")
    @get:Valid
    override val inlineArray: List<ChildDefinitionInlineArray>? = null,
    @param:JsonProperty("inline_enum")
    @get:JsonProperty("inline_enum")
    override val inlineEnum: ChildDefinitionInlineEnum? = null,
    @param:JsonProperty("some_prop")
    @get:JsonProperty("some_prop")
    public val someProp: String? = null,
) : ChildDefinition(inlineObj, inlineArray, inlineEnum)

public data class DiscriminatedChild3(
    @param:JsonProperty("inline_obj")
    @get:JsonProperty("inline_obj")
    @get:Valid
    override val inlineObj: ChildDefinitionInlineObj? = null,
    @param:JsonProperty("inline_array")
    @get:JsonProperty("inline_array")
    @get:Valid
    override val inlineArray: List<ChildDefinitionInlineArray>? = null,
    @param:JsonProperty("inline_enum")
    @get:JsonProperty("inline_enum")
    override val inlineEnum: ChildDefinitionInlineEnum? = null,
    @get:JsonProperty("some_enum")
    @get:NotNull
    @param:JsonProperty("some_enum")
    override val someEnum: ChildDiscriminator = ChildDiscriminator.OBJ_THREE,
) : ChildDefinition(inlineObj, inlineArray, inlineEnum)

public data class Responses(
    @param:JsonProperty("entries")
    @get:JsonProperty("entries")
    @get:Valid
    public val entries: List<ChildDefinition>? = null,
)
