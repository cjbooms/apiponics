package examples.defaultValues.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import java.net.URI
import javax.validation.constraints.NotNull
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.Map

data class PersonWithDefaults(
    @param:JsonProperty("required_so_default_ignored")
    @get:JsonProperty("required_so_default_ignored")
    @get:NotNull
    val requiredSoDefaultIgnored: String,
    @param:JsonProperty("integer_default")
    @get:JsonProperty("integer_default")
    @get:NotNull
    val integerDefault: Int = 18,
    @param:JsonProperty("enum_default")
    @get:JsonProperty("enum_default")
    @get:NotNull
    val enumDefault: PersonWithDefaultsEnumDefault = PersonWithDefaultsEnumDefault.TALL,
    @param:JsonProperty("boolean_default")
    @get:JsonProperty("boolean_default")
    @get:NotNull
    val booleanDefault: Boolean = true,
    @param:JsonProperty("string_phrase")
    @get:JsonProperty("string_phrase")
    @get:NotNull
    val stringPhrase: String = "Cowabunga Dude",
    @param:JsonProperty("uri_type")
    @get:JsonProperty("uri_type")
    @get:NotNull
    val uriType: URI = URI("about:blank")
)

enum class PersonWithDefaultsEnumDefault(
    @JsonValue
    val value: String
) {
    TALL("tall"),

    SHORT("short");

    companion object {
        private val mapping: Map<String, PersonWithDefaultsEnumDefault> =
            values().associateBy(PersonWithDefaultsEnumDefault::value)

        fun fromValue(value: String): PersonWithDefaultsEnumDefault? = mapping[value]
    }
}
