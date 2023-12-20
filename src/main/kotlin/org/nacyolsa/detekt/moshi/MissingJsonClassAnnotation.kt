package org.nacyolsa.detekt.moshi

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.api.config
import io.gitlab.arturbosch.detekt.api.internal.Configuration
import org.jetbrains.kotlin.psi.KtClass

class MissingJsonClassAnnotation(config: Config) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.Defect,
        "Missing @JsonClass(generateAdapter = true)",
        Debt.FIVE_MINS,
    )

    @Configuration("Keywords in class name used by this rule")
    private val keywords by config(listOf("Request", "Response", "Event")) { it.toSet() }

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        val className = klass.name ?: return
        if (klass.isData() && keywords.any { className.contains(it) }) {
            val jsonClassAnnotation = klass.annotationEntries.firstOrNull {
                it.shortName?.asString() == "JsonClass"
            }
            if (jsonClassAnnotation == null) {
                report(CodeSmell(issue, Entity.atName(klass), "Missing @JsonClass(generateAdapter = true) on class $className"))
                return
            }

            val generateAdapterParameter = jsonClassAnnotation.valueArguments.firstOrNull {
                it.getArgumentName()?.asName?.asString() == "generateAdapter"
            }
            val hasParameterCorrectValue = generateAdapterParameter?.getArgumentExpression()?.text == "true"
            if (!hasParameterCorrectValue) {
                report(CodeSmell(issue, Entity.atName(klass), "Missing `generateAdapter = true` on class $className"))
            }
        }
    }
}
