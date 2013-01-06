package de.schauderhaft.degraph.categorizer

import de.schauderhaft.degraph.analysis.Node

/**
 * categorizes package nodes by matching them against ant like patterns.
 *
 * * matches a node with any name not containing dots
 * * letters and dots match those letters and dots respectively
 * ** matches any combination of letters and dots.
 *
 * The category of a matched node it that part of the match that is wrapped in parenthesis.
 *
 * If no parenthesis are give the full name is returned as a category.
 *
 * examples:
 *
 * de.**.test matches de..test, de.some.test and de.some.other.test
 *
 * de.*.test matches from the examples given above only de.some.test
 *
 *
 * de.(*.test) categorizes de.some.test as 'some.test'
 * de.(*).test categorizes it as 'some'
 */
class NamedPatternMatchingCategorizer(targetType: String, pattern: String, name: String)
    extends (AnyRef => AnyRef) {
    private[this] val matcher = new PatternMatcher(pattern)

    override def apply(x: AnyRef): AnyRef = x match {
        case n: Node => matcher.matches(n.name).map(_ => Node(targetType, name)).getOrElse(n)
        case _ => x
    }

    class PatternMatcher(pattern: String) {
        private[this] val Pattern = ensureParens(escapeStars(escapeDots(pattern))).r

        def matches(name: String) = {
            name match {
                case Pattern(matchedGroup) => Some(matchedGroup)
                case _ => None
            }
        }

        private[this] def escapeStars(p: String) = {
            val doubleStarPlaceHolder = "&"
            val singleStarPlaceHolder = "§"
            p.
                replace("**", doubleStarPlaceHolder).
                replace("*", singleStarPlaceHolder).
                replace(doubleStarPlaceHolder, """.*""").
                replace(singleStarPlaceHolder, """[^\.]*""")
        }

        private[this] def escapeDots(p: String) = p.replace(".", """\.""")

        private[this] def ensureParens(p: String) =
            if (""".*\(.*\).*""".r.findFirstIn(p).isDefined)
                p
            else
                "(" + p + ")"
    }

}