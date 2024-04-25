package examples.polymorphicModels.models

import com.fasterxml.jackson.`annotation`.JsonProperty
import javax.validation.constraints.NotNull
import kotlin.Int
import kotlin.String

public data class PolymorphicTypeTwo(
  @param:JsonProperty("first_name")
  @get:JsonProperty("first_name")
  @get:NotNull
  override val firstName: String,
  @param:JsonProperty("last_name")
  @get:JsonProperty("last_name")
  @get:NotNull
  override val lastName: String,
  @param:JsonProperty("some_integer_propery")
  @get:JsonProperty("some_integer_propery")
  public val someIntegerPropery: Int? = null,
  @param:JsonProperty("child_two_age")
  @get:JsonProperty("child_two_age")
  public val childTwoAge: Int? = null,
  @get:JsonProperty("generation")
  @get:NotNull
  @param:JsonProperty("generation")
  override val generation: String = "polymorphic_type_two",
) : PolymorphicSuperType(firstName, lastName)
