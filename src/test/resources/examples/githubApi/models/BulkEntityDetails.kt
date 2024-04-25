package examples.githubApi.models

import com.fasterxml.jackson.`annotation`.JsonAnyGetter
import com.fasterxml.jackson.`annotation`.JsonAnySetter
import com.fasterxml.jackson.`annotation`.JsonIgnore
import com.fasterxml.jackson.`annotation`.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull
import kotlin.Any
import kotlin.String
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableMap

public data class BulkEntityDetails(
  @param:JsonProperty("entities")
  @get:JsonProperty("entities")
  @get:NotNull
  @get:Valid
  public val entities: List<EntityDetails>,
  @get:JsonIgnore
  public val properties: MutableMap<String, Any?> = mutableMapOf(),
) {
  @JsonAnyGetter
  public fun `get`(): Map<String, Any?> = properties

  @JsonAnySetter
  public fun `set`(name: String, `value`: Any?) {
    properties[name] = value
  }
}
