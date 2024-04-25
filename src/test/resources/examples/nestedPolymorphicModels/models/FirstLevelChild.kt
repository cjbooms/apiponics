package examples.nestedPolymorphicModels.models

import com.fasterxml.jackson.`annotation`.JsonProperty
import com.fasterxml.jackson.`annotation`.JsonSubTypes
import com.fasterxml.jackson.`annotation`.JsonTypeInfo
import javax.validation.constraints.NotNull
import kotlin.Boolean
import kotlin.Int
import kotlin.String

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "firstLevelDiscriminator",
  visible = true,
)
@JsonSubTypes(JsonSubTypes.Type(value = SecondLevelChild1::class, name =
    "secondLevelChild1"),JsonSubTypes.Type(value = SecondLevelChild2::class, name =
    "secondLevelChild2"))
public sealed class FirstLevelChild(
  @param:JsonProperty("rootField1")
  @get:JsonProperty("rootField1")
  @get:NotNull
  override val rootField1: String,
  @param:JsonProperty("rootField2")
  @get:JsonProperty("rootField2")
  override val rootField2: Boolean? = null,
  public open val firstLevelField1: String,
  public open val firstLevelField2: Int? = null,
  @get:JsonProperty("rootDiscriminator")
  @get:NotNull
  @param:JsonProperty("rootDiscriminator")
  override val rootDiscriminator: RootDiscriminator = RootDiscriminator.FIRST_LEVEL_CHILD,
) : RootType(rootField1, rootField2) {
  public abstract val firstLevelDiscriminator: FirstLevelDiscriminator
}

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "secondLevelDiscriminator",
  visible = true,
)
@JsonSubTypes(JsonSubTypes.Type(value = ThirdLevelChild11::class, name =
    "thirdLevelChild1"),JsonSubTypes.Type(value = ThirdLevelChild12::class, name =
    "thirdLevelChild2"))
public sealed class SecondLevelChild1(
  @param:JsonProperty("rootField1")
  @get:JsonProperty("rootField1")
  @get:NotNull
  override val rootField1: String,
  @param:JsonProperty("rootField2")
  @get:JsonProperty("rootField2")
  override val rootField2: Boolean? = null,
  @param:JsonProperty("firstLevelField1")
  @get:JsonProperty("firstLevelField1")
  @get:NotNull
  override val firstLevelField1: String,
  @param:JsonProperty("firstLevelField2")
  @get:JsonProperty("firstLevelField2")
  override val firstLevelField2: Int? = null,
  public open val metadata: SecondLevelMetadata,
  @get:JsonProperty("firstLevelDiscriminator")
  @get:NotNull
  @param:JsonProperty("firstLevelDiscriminator")
  override val firstLevelDiscriminator: FirstLevelDiscriminator =
      FirstLevelDiscriminator.SECOND_LEVEL_CHILD1,
) : FirstLevelChild(rootField1, rootField2, firstLevelField1, firstLevelField2) {
  public abstract val secondLevelDiscriminator: SecondLevelDiscriminator
}

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "secondLevelDiscriminator",
  visible = true,
)
@JsonSubTypes(JsonSubTypes.Type(value = ThirdLevelChild21::class, name =
    "thirdLevelChild1"),JsonSubTypes.Type(value = ThirdLevelChild22::class, name =
    "thirdLevelChild2"))
public sealed class SecondLevelChild2(
  @param:JsonProperty("rootField1")
  @get:JsonProperty("rootField1")
  @get:NotNull
  override val rootField1: String,
  @param:JsonProperty("rootField2")
  @get:JsonProperty("rootField2")
  override val rootField2: Boolean? = null,
  @param:JsonProperty("firstLevelField1")
  @get:JsonProperty("firstLevelField1")
  @get:NotNull
  override val firstLevelField1: String,
  @param:JsonProperty("firstLevelField2")
  @get:JsonProperty("firstLevelField2")
  override val firstLevelField2: Int? = null,
  public open val metadata: SecondLevelMetadata,
  @get:JsonProperty("firstLevelDiscriminator")
  @get:NotNull
  @param:JsonProperty("firstLevelDiscriminator")
  override val firstLevelDiscriminator: FirstLevelDiscriminator =
      FirstLevelDiscriminator.SECOND_LEVEL_CHILD2,
) : FirstLevelChild(rootField1, rootField2, firstLevelField1, firstLevelField2) {
  public abstract val secondLevelDiscriminator: SecondLevelDiscriminator
}
