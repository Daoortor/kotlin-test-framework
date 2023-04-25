package org.jetbrains.academy.test.system

import org.jetbrains.academy.test.system.models.classes.ClassType
import org.jetbrains.academy.test.system.models.classes.TestClass
import java.lang.reflect.Modifier

private fun Class<*>.getVisibility() = this.modifiers.getVisibility()

fun Class<*>.getDeclaredFieldsWithoutCompanion() =
    this.declaredFields.filter { it.name !in listOf("Companion", "INSTANCE") }

@Suppress("ReturnCount", "ForbiddenComment")
private fun Class<*>.getClassType(): ClassType {
    if (this.isInterface) {
        if (this.isSamInterface()) {
            return ClassType.SAM_INTERFACE
        }
        return ClassType.INTERFACE
    }
    if (this.isEnum) {
        return ClassType.ENUM
    }
    if (this.isObject()) {
        return ClassType.OBJECT
    }
    // TODO: think about companion object
    return ClassType.CLASS
}

fun Class<*>.isSamInterface(): Boolean {
    if (methods.size != 1) {
        return false
    }
    return Modifier.isAbstract(methods.first().modifiers)
}

fun Class<*>.getInstanceFiled() = this.fields.find { it.name == "INSTANCE" }

fun Class<*>.isObject() = this.fields.all { Modifier.isStatic(it.modifiers) } && this.getInstanceFiled() != null

fun Class<*>.checkIfIsDataClass(testClass: TestClass) {
    val methods = this.methods
    val methodsNames = methods.map { it.name }
    val dataClassMethods = listOf(
        "copy",
        "equals",
        "hashCode",
        "toString",
    )
    dataClassMethods.forEach { dataClassMethod ->
        assert(dataClassMethod in methodsNames || methodsNames.any{ dataClassMethod in it }) { "${testClass.getFullName()} must be a data class" }
    }
    val (primary, _) = testClass.declaredFields.partition { it.isInPrimaryConstructor }
    val componentNFunctions = methodsNames.filter { "component" in it }
    val componentNErrorMessage =
        "You must put only ${primary.size} fields into the primary constructor: ${primary.joinToString(", ") { it.name }}."
    assert(componentNFunctions.size == primary.size) { componentNErrorMessage }
    primary.forEachIndexed { index, _ ->
        val name = "component${index + 1}"
        assert(name in methodsNames || methodsNames.any { name in it }) { componentNErrorMessage }
    }
}

private fun Class<*>.hasSameVisibilityWith(testClass: TestClass) = this.getVisibility() == testClass.visibility

private fun Class<*>.hasSameClassTypeWith(testClass: TestClass) = this.getClassType() == testClass.classType

fun Class<*>.isSameWith(testClass: TestClass) =
    this.hasSameVisibilityWith(testClass) && this.hasSameClassTypeWith(testClass)

fun Class<*>.toTestClass(name: String, classPackage: String?): TestClass {
    val visibility = this.getVisibility() ?: throwInternalLibError()
    return TestClass(name, classPackage, visibility, this.getClassType())
}