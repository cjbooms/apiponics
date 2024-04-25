package examples.nestedPolymorphicModels.models

import com.fasterxml.jackson.`annotation`.JsonValue
import kotlin.String
import kotlin.collections.Map

public enum class RootDiscriminator(
  @JsonValue
  public val `value`: String,
) {
  FIRST_LEVEL_CHILD("firstLevelChild"),
  ;

  public companion object {
    private val mapping: Map<String, RootDiscriminator> =
        values().associateBy(RootDiscriminator::value)

    public fun fromValue(`value`: String): RootDiscriminator? = mapping[value]
  }
}
