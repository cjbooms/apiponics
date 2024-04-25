package examples.githubApi.models

import com.fasterxml.jackson.`annotation`.JsonValue
import kotlin.String
import kotlin.collections.Map

public enum class ContributorStatus(
  @JsonValue
  public val `value`: String,
) {
  ACTIVE("active"),
  INACTIVE("inactive"),
  ;

  public companion object {
    private val mapping: Map<String, ContributorStatus> =
        values().associateBy(ContributorStatus::value)

    public fun fromValue(`value`: String): ContributorStatus? = mapping[value]
  }
}
