package com.cjbooms.fabrikt.cli

import com.beust.jcommander.*
import com.beust.jcommander.converters.PathConverter
import com.cjbooms.fabrikt.model.Destinations
import com.cjbooms.fabrikt.util.NormalisedString.isValidJavaPackage
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.system.exitProcess

class CodeGenArgs {

    companion object {
        private const val DEFAULT_API_FILE_NAME = "api.yaml"
        val DefaultApiPath: Path = Paths.get(".", DEFAULT_API_FILE_NAME)

        fun parse(args: Array<String>): CodeGenArgs {
            val codeGenArgs = CodeGenArgs()
            val parser: JCommander = JCommander.newBuilder()
                .addObject(codeGenArgs)
                .build()
            parser.usageFormatter = MarkdownUsageFormatter(parser)
            parser.parse(*args)

            if (codeGenArgs.printUsage) {
                parser.usage()
                exitProcess(0)
            }
            return codeGenArgs
        }
    }

    @Parameter(
        names = ["--help"],
        help = true
    )
    var printUsage: Boolean = false

    @Parameter(
        names = ["--output-directory"],
        description = "Allows the generation dir to be overridden. Defaults to current dir",
        converter = com.cjbooms.fabrikt.cli.PathConverter::class
    )
    var outputDirectory: Path = Paths.get(".")

    @Parameter(
        names = ["--base-package"],
        description = "The base package which all code will be generated under.",
        required = true,
        validateValueWith = [PackageNameValidator::class]
    )
    lateinit var basePackage: String

    @Parameter(
        names = ["--api-file"],
        description = "This must be a valid Open API v3 spec. All code generation will be based off this input.",
        converter = PathConverter::class
    )
    var apiFile: Path = DefaultApiPath

    @Parameter(
        names = ["--api-fragment"],
        description = "A partial Open API v3 fragment, to be combined with the primary API for code generation purposes.",
        converter = PathConverter::class
    )
    var apiFragments: List<Path> = emptyList()

    @Parameter(
        names = ["--targets"],
        description = "Targets are the parts of the application that you want to be generated.",
        converter = CodeGenerationTypesConverter::class
    )
    var targets: Set<CodeGenerationType> = emptySet()

    @Parameter(
        names = ["--http-controller-opts"],
        description = "Select the options for the controllers that you want to be generated.",
        converter = ControllerCodeGenOptionConverter::class
    )
    var controllerOptions: Set<ControllerCodeGenOptionType> = emptySet()

    @Parameter(
        names = ["--http-controller-target"],
        description = "Select the target framework for the controllers that you want to be generated.",
        converter = ControllerCodeGenTargetConverter::class
    )
    var controllerTarget: ControllerCodeGenTargetType = ControllerCodeGenTargetType.SPRING

    @Parameter(
        names = ["--http-model-opts"],
        description = "Select the options for the http models that you want to be generated.",
        converter = ModelCodeGenOptionConverter::class
    )
    var modelOptions: Set<ModelCodeGenOptionType> = emptySet()

    @Parameter(
        names = ["--http-client-opts"],
        description = "Select the options for the http client code that you want to be generated.",
        converter = ClientCodeGenOptionConverter::class
    )
    var clientOptions: Set<ClientCodeGenOptionType> = emptySet()

    @Parameter(
        names = ["--src-path"],
        description = "Allows the path for generated source files to be overridden. Defaults to `src/main/kotlin`",
        converter = PathConverter::class
    )
    var srcPath: Path = Destinations.MAIN_KT_SOURCE

    @Parameter(
        names = ["--resources-path"],
        description = "Allows the path for generated resources to be overridden. Defaults to `src/main/resources`",
        converter = PathConverter::class
    )
    var resourcesPath: Path = Destinations.MAIN_RESOURCES
}

class CodeGenerationTypesConverter : IStringConverter<CodeGenerationType> {
    override fun convert(value: String): CodeGenerationType =
        convertToEnumValue(value)
}

class ControllerCodeGenOptionConverter : IStringConverter<ControllerCodeGenOptionType> {
    override fun convert(value: String): ControllerCodeGenOptionType = convertToEnumValue(value)
}

class ControllerCodeGenTargetConverter : IStringConverter<ControllerCodeGenTargetType> {
    override fun convert(value: String): ControllerCodeGenTargetType = convertToEnumValue(value)
}

class ModelCodeGenOptionConverter : IStringConverter<ModelCodeGenOptionType> {
    override fun convert(value: String): ModelCodeGenOptionType = convertToEnumValue(value)
}

class ClientCodeGenOptionConverter : IStringConverter<ClientCodeGenOptionType> {
    override fun convert(value: String): ClientCodeGenOptionType =
        convertToEnumValue(value)
}

class PackageNameValidator : IValueValidator<String> {
    override fun validate(name: String, value: String) {
        if (!value.isValidJavaPackage()) {
            throw ParameterException("Requested package [$value] was not a valid java package.")
        }
    }
}

class PathConverter : IStringConverter<Path> {
    override fun convert(value: String): Path = try {
        Paths.get(value)
    } catch (e: InvalidPathException) {
        throw ParameterException("$value is not a valid file directory", e)
    }
}

inline fun <reified T : Enum<T>> convertToEnumValue(value: String): T {
    return try {
        enumValueOf(value.toUpperCase())
    } catch (e: IllegalArgumentException) {
        throw ParameterException("$value is not a valid option. Please choose from ${enumValues<T>().joinToString()}")
    }
}
