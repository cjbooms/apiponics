package examples.nestedPolymorphicModels.models

import com.fasterxml.jackson.`annotation`.JsonSubTypes
import com.fasterxml.jackson.`annotation`.JsonTypeInfo
import kotlin.Boolean
import kotlin.String

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "rootDiscriminator",
  visible = true,
)
@JsonSubTypes(JsonSubTypes.Type(value = FirstLevelChild::class, name = "firstLevelChild"))
public sealed class RootType(
  public open val rootField1: String,
  public open val rootField2: Boolean? = null,
) {
  public abstract val rootDiscriminator: RootDiscriminator
}
