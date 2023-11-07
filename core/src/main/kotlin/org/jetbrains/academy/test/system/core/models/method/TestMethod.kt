package org.jetbrains.academy.test.system.core.models.method

import org.jetbrains.academy.test.system.core.checkType
import org.jetbrains.academy.test.system.core.models.TestKotlinType
import org.jetbrains.academy.test.system.core.models.Visibility
import org.jetbrains.academy.test.system.core.models.variable.TestVariable
import org.junit.jupiter.api.Assertions
import java.lang.reflect.Method
import kotlin.reflect.jvm.kotlinFunction

/**
 * Represents any function in the code, e.g. a member function, a top level function, etc.
 *
 * @param name represents a method name.
 * @param returnType represents a Kotlin return type, see [TestKotlinType].
 * @param arguments represents a list of arguments, see [TestVariable].
 * @param returnTypeJava represents the short name of a Java return type.
 * @param visibility represents [Visibility] of the method.
 * @param hasGeneratedPartInName indicates if the method's name contains a generated by the Kotlin compiler part.
 */
data class TestMethod(
    val name: String,
    val returnType: TestKotlinType,
    val arguments: List<TestVariable> = emptyList(),
    val returnTypeJava: String? = null,
    val visibility: Visibility = Visibility.PUBLIC,
    val hasGeneratedPartInName: Boolean = false,
) {
    fun prettyString(withToDo: Boolean = true): String {
        val args = arguments.joinToString(", ") { it.paramPrettyString() }
        val body = if (withToDo) {
            "TODO(\"Not implemented yet\")"
        } else {
            "// Some code"
        }
        return "${visibility.key} fun $name($args): ${returnType.getTypePrettyString()} = $body"
    }

    private fun TestVariable.paramPrettyString() = "$name: $javaType"

    fun checkMethod(method: Method) {
        val kotlinFunction =
            method.kotlinFunction ?: error("Can not find Kotlin method for the method ${this.prettyString()}")
        Assertions.assertEquals(kotlinFunction.name, name, "The function name must be: $name")
        val visibility = kotlinFunction.visibility?.name?.lowercase()
        Assertions.assertEquals(
            visibility,
            this.visibility.key,
            "\"The visibility of the method $name must be ${this.visibility.key}\""
        )
        kotlinFunction.returnType.checkType(returnType, returnTypeJava ?: returnType.type, "the function $name")
    }
}
