package examples.inLinedObject.models

import com.fasterxml.jackson.`annotation`.JsonProperty
import javax.validation.Valid
import kotlin.String

public data class SecondInlineObjectGeneration(
  @param:JsonProperty("call_home")
  @get:JsonProperty("call_home")
  @get:Valid
  public val callHome: SecondInlineObjectCallHome? = null,
  @param:JsonProperty("database_view")
  @get:JsonProperty("database_view")
  @get:Valid
  public val databaseView: SecondInlineObjectDatabaseView? = null,
  @param:JsonProperty("direct")
  @get:JsonProperty("direct")
  public val direct: String? = null,
)
