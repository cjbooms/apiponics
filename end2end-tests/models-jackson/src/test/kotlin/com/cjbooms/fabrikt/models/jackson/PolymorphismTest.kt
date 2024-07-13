package com.cjbooms.fabrikt.models.jackson

import com.cjbooms.fabrikt.models.jackson.Helpers.mapper
import com.example.models.PolymorphicSuperType
import com.example.models.PolymorphicTypeOne
import com.example.models.PolymorphicTypeTwo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PolymorphismTest {
    private val objectMapper = mapper()
    private val writer = objectMapper.writerWithDefaultPrettyPrinter()

    @Test
    fun `must serialize PolymorphicTypeOne`() {
        val obj = PolymorphicTypeOne(firstName = "first name", lastName = "last name", childOneName = "child on name")
        val result = writer.writeValueAsString(obj)

        val expected = javaClass.getResource("/polymorphic/polymorphic_type_one.json")!!.readText()
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `must deserialize PolymorphicTypeOne`() {
        val jsonString = javaClass.getResource("/polymorphic/polymorphic_type_one.json")!!.readText()

        val result = objectMapper.readValue(jsonString, PolymorphicSuperType::class.java)
        assertThat(result).isEqualTo(PolymorphicTypeOne(firstName = "first name", lastName = "last name", childOneName = "child on name"))
    }

    @Test
    fun `must serialize PolymorphicTypeTwo`() {
        val obj = PolymorphicTypeTwo(firstName = "first name", lastName = "last name", childTwoAge = 1)
        val result = writer.writeValueAsString(obj)

        val expected = javaClass.getResource("/polymorphic/polymorphic_type_two.json")!!.readText()
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `must deserialize PolymorphicTypeTwo`() {
        val jsonString = javaClass.getResource("/polymorphic/polymorphic_type_two.json")!!.readText()

        val result = objectMapper.readValue(jsonString, PolymorphicSuperType::class.java)
        assertThat(result).isEqualTo(PolymorphicTypeTwo(firstName = "first name", lastName = "last name", childTwoAge = 1))
    }
}