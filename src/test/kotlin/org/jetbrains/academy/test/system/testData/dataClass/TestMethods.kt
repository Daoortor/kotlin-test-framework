package org.jetbrains.academy.test.system.testData.dataClass

import org.jetbrains.academy.test.system.models.TestKotlinType
import org.jetbrains.academy.test.system.models.Visibility
import org.jetbrains.academy.test.system.models.method.TestMethod
import org.jetbrains.academy.test.system.models.variable.TestVariable

val publicValGetterMethod = TestMethod(
    name = "getPublicVal",
    returnType = TestKotlinType("String"),
    returnTypeJava = "String",
)

val publicFooWithoutArgumentsMethod = TestMethod(
    name = "publicFooWithoutArguments",
    returnType = TestKotlinType("Int"),
    returnTypeJava = "Int",
)

val publicFooWithArgumentsMethod = TestMethod(
    name = "publicFooWithArguments",
    returnType = TestKotlinType("Int"),
    returnTypeJava = "Int",
    arguments = listOf(
        TestVariable(
            name = "a",
            javaType = "int",
        ),
        TestVariable(
            name = "b",
            javaType = "int",
        ),
    )
)

val privateFooWithoutArgumentsMethod = TestMethod(
    name = "privateFooWithoutArguments",
    returnType = TestKotlinType("Int"),
    returnTypeJava = "Int",
    visibility = Visibility.PRIVATE
)

val privateFooWithArgumentsMethod = TestMethod(
    name = "privateFooWithArguments",
    returnType = TestKotlinType("Int"),
    returnTypeJava = "Int",
    arguments = listOf(
        TestVariable(
            name = "a",
            javaType = "int",
        ),
        TestVariable(
            name = "b",
            javaType = "int",
        ),
    ),
    visibility = Visibility.PRIVATE
)
