package examples.discriminatedOneOf.models

import javax.validation.constraints.NotNull
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("b")
@Serializable
public data class StateB(
  @SerialName("mode")
  @get:NotNull
  public val mode: StateBMode,
  @SerialName("status")
  @get:NotNull
  public val status: Status = Status.B,
) : State
