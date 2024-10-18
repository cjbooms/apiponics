package examples.discriminatedOneOf.models

import kotlin.String
import kotlin.collections.Map
import kotlinx.serialization.SerialName

public enum class Status(
  public val `value`: String,
) {
  @SerialName("a")
  A("a"),
  @SerialName("b")
  B("b"),
  ;

  public companion object {
    private val mapping: Map<String, Status> = values().associateBy(Status::value)

    public fun fromValue(`value`: String): Status? = mapping[value]
  }
}
