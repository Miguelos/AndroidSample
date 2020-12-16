package me.miguelos.sample.utils

import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.mockito.configuration.DefaultMockitoConfiguration
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues
import org.mockito.invocation.InvocationOnMock

class MockitoConfiguration : DefaultMockitoConfiguration() {

    override fun getDefaultAnswer() = object : ReturnsEmptyValues() {
        override fun answer(inv: InvocationOnMock): Any {
            val type = inv.method.returnType
            return when {
                type.isAssignableFrom(Observable::class.java) -> {
                    Observable.error<Any>(createException(inv))
                }
                type.isAssignableFrom(Single::class.java) -> {
                    Single.error<Any>(createException(inv))
                }
                else -> {
                    super.answer(inv)
                }
            }
        }
    }

    private fun createException(
        invocation: InvocationOnMock
    ): @NonNull RuntimeException {
        val s = invocation.toString()
        return RuntimeException("No mock defined for invocation $s")
    }
}