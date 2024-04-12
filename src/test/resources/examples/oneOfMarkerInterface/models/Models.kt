package examples.oneOfMarkerInterface.models

import com.fasterxml.jackson.`annotation`.JsonProperty
import javax.validation.constraints.NotNull
import kotlin.Any
import kotlin.String

public data class Container(
    @param:JsonProperty("state")
    @get:JsonProperty("state")
    public val state: Any? = null,
)

public sealed interface State

public data class StateA(
    @get:JsonProperty("status")
    @get:NotNull
    public val status: String,
) : State

public data class StateB(
    @get:JsonProperty("mode")
    @get:NotNull
    public val mode: String,
) : State
