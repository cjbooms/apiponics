package examples.discriminatedOneOf.models

import com.fasterxml.jackson.`annotation`.JsonValue
import kotlin.String
import kotlin.collections.Map

public enum class StateBMode(
  @JsonValue
  public val `value`: String,
) {
  MODE1("mode1"),
  MODE2("mode2"),
  ;

  public companion object {
    private val mapping: Map<String, StateBMode> = entries.associateBy(StateBMode::value)

    public fun fromValue(`value`: String): StateBMode? = mapping[value]
  }
}
