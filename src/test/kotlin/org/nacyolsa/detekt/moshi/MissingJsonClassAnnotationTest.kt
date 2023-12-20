package org.nacyolsa.detekt.moshi

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.rules.KotlinCoreEnvironmentTest
import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import io.kotest.matchers.collections.shouldHaveSize
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.junit.jupiter.api.Test

@KotlinCoreEnvironmentTest
internal class MissingJsonClassAnnotationTest(private val env: KotlinCoreEnvironment) {

    @Test
    fun `reports missing @JsonClass(generateAdapter = true) on class with specific keyword in name`() {
        val code = """
            data class UserRequest {
              val abc
            }
        """
        val findings = MissingJsonClassAnnotation(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `should not report missing @JsonClass(generateAdapter = true) for non data class`() {
        val code = """
            class UserRequest {
              val abc
            }
        """
        val findings = MissingJsonClassAnnotation(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `reports missing generateAdapter = true in @JsonClass annotation`() {
        val code = """
            @JsonClass(generateAdapter = false)
            data class UserRequest {
              val abc
            }
        """
        val findings = MissingJsonClassAnnotation(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `reports missing 'generateAdapter' parameter in @JsonClass annotation`() {
        val code = """
            @JsonClass
            data class UserRequest {
              val abc
            }
        """
        val findings = MissingJsonClassAnnotation(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

    @Test
    fun `doesn't report missing @JsonClass(generateAdapter = true)`() {
        val code = """
            @JsonClass(generateAdapter = true)
            data class UserRequest {
              val abc
            }
        """
        val findings = MissingJsonClassAnnotation(Config.empty).compileAndLintWithContext(env, code)
        findings shouldHaveSize 0
    }

    @Test
    fun `should use keywords from config`() {
        val config = TestConfig(
            "keywords" to "[Key1,Key2,Key3]"
        )
        val code = """
            data class UserKey2ABC {
              val abc
            }
        """
        val findings = MissingJsonClassAnnotation(config).compileAndLintWithContext(env, code)
        findings shouldHaveSize 1
    }

}
