package examples.customExtensions.models

import com.fasterxml.jackson.`annotation`.JsonProperty
import kotlin.String

public data class TopLevelLevelMergePatchInlineInner(
  @param:JsonProperty("p")
  @get:JsonProperty("p")
  public val p: String? = null,
)
