package examples.anyOfOneOfAllOf.multipleFiles.models

import com.fasterxml.jackson.`annotation`.JsonProperty
import kotlin.Any
import kotlin.String

public data class SecondOneB(
  @param:JsonProperty("second_property")
  @get:JsonProperty("second_property")
  public val secondProperty: String? = null,
  @param:JsonProperty("third_property")
  @get:JsonProperty("third_property")
  public val thirdProperty: String? = null,
  @param:JsonProperty("forth_property")
  @get:JsonProperty("forth_property")
  public val forthProperty: Any? = null,
)
