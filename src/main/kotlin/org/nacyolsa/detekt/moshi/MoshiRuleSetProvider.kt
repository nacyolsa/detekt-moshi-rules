package org.nacyolsa.detekt.moshi

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class MoshiRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "MoshiRuleSet"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                MissingJsonClassAnnotation(config),
            ),
        )
    }
}
