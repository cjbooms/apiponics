package examples.customExtensions.multipleFiles.models

import com.fasterxml.jackson.`annotation`.JsonProperty
import kotlin.String

public data class NoMergePatchInlineInner(
  @param:JsonProperty("p")
  @get:JsonProperty("p")
  public val p: String? = null,
)
