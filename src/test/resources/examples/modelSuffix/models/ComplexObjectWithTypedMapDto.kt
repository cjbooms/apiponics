package examples.modelSuffix.models

import com.fasterxml.jackson.`annotation`.JsonAnyGetter
import com.fasterxml.jackson.`annotation`.JsonAnySetter
import com.fasterxml.jackson.`annotation`.JsonIgnore
import com.fasterxml.jackson.`annotation`.JsonProperty
import kotlin.Int
import kotlin.String
import kotlin.collections.Map
import kotlin.collections.MutableMap

public data class ComplexObjectWithTypedMapDto(
  @param:JsonProperty("text")
  @get:JsonProperty("text")
  public val text: String? = null,
  @param:JsonProperty("code")
  @get:JsonProperty("code")
  public val code: Int? = null,
  @get:JsonIgnore
  public val properties: MutableMap<String, ComplexObjectWithTypedMapValueDto?> = mutableMapOf(),
) {
  @JsonAnyGetter
  public fun `get`(): Map<String, ComplexObjectWithTypedMapValueDto?> = properties

  @JsonAnySetter
  public fun `set`(name: String, `value`: ComplexObjectWithTypedMapValueDto?) {
    properties[name] = value
  }
}
