package examples.modelSuffix.models

import com.fasterxml.jackson.`annotation`.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

public data class SecondLevelMetadataDto(
  @param:JsonProperty("obj")
  @get:JsonProperty("obj")
  @get:NotNull
  @get:Valid
  public val obj: CommonObjectDto,
)
