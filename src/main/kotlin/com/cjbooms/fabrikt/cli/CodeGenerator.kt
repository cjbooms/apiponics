package com.cjbooms.fabrikt.cli

import com.cjbooms.fabrikt.cli.CodeGenerationType.*
import com.cjbooms.fabrikt.configurations.Packages
import com.cjbooms.fabrikt.generators.MutableSettings
import com.cjbooms.fabrikt.generators.client.OkHttpClientGenerator
import com.cjbooms.fabrikt.generators.controller.MicronautControllerInterfaceGenerator
import com.cjbooms.fabrikt.generators.controller.SpringControllerInterfaceGenerator
import com.cjbooms.fabrikt.generators.model.JacksonModelGenerator
import com.cjbooms.fabrikt.generators.model.QuarkusReflectionModelGenerator
import com.cjbooms.fabrikt.model.*
import com.squareup.kotlinpoet.FileSpec
import java.nio.file.Path

class CodeGenerator(
    private val packages: Packages,
    private val sourceApi: SourceApi,
    private val srcPath: Path,
    private val resourcesPath: Path
) {

    fun generate(): Collection<GeneratedFile> = MutableSettings.generationTypes().map(::generateCode).flatten()

    private fun generateCode(generationType: CodeGenerationType): Collection<GeneratedFile> =
        when (generationType) {
            CLIENT -> generateClient()
            CONTROLLERS -> generateControllerInterfaces()
            HTTP_MODELS -> generateModels()
            QUARKUS_REFLECTION_CONFIG -> generateQuarkusReflectionResource()
        }

    private fun generateModels(): Collection<GeneratedFile> = sourceSet(models().files)

    private fun generateControllerInterfaces(): Collection<GeneratedFile> =
        sourceSet(controllers().files).plus(sourceSet(models().files))

    private fun generateClient(): Collection<GeneratedFile> {
        val lib = OkHttpClientGenerator(packages, sourceApi, srcPath).generateLibrary(MutableSettings.clientOptions())
        return sourceSet(client().files).plus(lib).plus(sourceSet(models().files))
    }

    private fun generateQuarkusReflectionResource(): Collection<GeneratedFile> = resourceSet(resources(models()))

    private fun sourceSet(fileSpec: Collection<FileSpec>) = setOf(KotlinSourceSet(fileSpec, srcPath))

    private fun resourceSet(resFiles: Collection<ResourceFile>) = setOf(ResourceSourceSet(resFiles, resourcesPath))

    private fun models(): Models =
        JacksonModelGenerator(packages, sourceApi, MutableSettings.modelOptions()).generate()

    private fun resources(models: Models): List<ResourceFile> =
        listOfNotNull(QuarkusReflectionModelGenerator(models, MutableSettings.generationTypes()).generate())

    private fun controllers(): KotlinTypes {
        val generator =
            when (MutableSettings.controllerTarget()) {
                ControllerCodeGenTargetType.SPRING -> SpringControllerInterfaceGenerator(
                    packages,
                    sourceApi,
                    MutableSettings.controllerOptions()
                )

                ControllerCodeGenTargetType.MICRONAUT -> MicronautControllerInterfaceGenerator(
                    packages,
                    sourceApi,
                    MutableSettings.controllerOptions()
                )
            }
        return generator.generate()
    }

    private fun client(): Clients =
        OkHttpClientGenerator(packages, sourceApi, srcPath).generate(MutableSettings.clientOptions())
}
