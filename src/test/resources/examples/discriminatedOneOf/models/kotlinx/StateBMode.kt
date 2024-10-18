package examples.discriminatedOneOf.models

import kotlin.String
import kotlin.collections.Map
import kotlinx.serialization.SerialName

public enum class StateBMode(
  public val `value`: String,
) {
  @SerialName("mode1")
  MODE1("mode1"),
  @SerialName("mode2")
  MODE2("mode2"),
  ;

  public companion object {
    private val mapping: Map<String, StateBMode> = values().associateBy(StateBMode::value)

    public fun fromValue(`value`: String): StateBMode? = mapping[value]
  }
}
