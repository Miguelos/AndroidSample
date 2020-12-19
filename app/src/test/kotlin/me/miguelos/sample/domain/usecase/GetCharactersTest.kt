package me.miguelos.sample.domain.usecase

import io.reactivex.rxjava3.internal.operators.completable.CompletableEmpty
import io.reactivex.rxjava3.internal.operators.completable.CompletableError
import junit.framework.TestCase
import me.miguelos.sample.domain.MarvelRepository
import me.miguelos.sample.domain.common.ExecutionSchedulers
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit


class GetCharactersTest : TestCase() {

    @Rule
    var rule = MockitoJUnit.rule()

    @Mock
    private lateinit var marvelRepository: MarvelRepository

    @Mock
    private lateinit var executionSchedulers: ExecutionSchedulers

    @InjectMocks
    private lateinit var getCharacters: GetCharacters

    @Before
    override fun setUp() {
        super.setUp()
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testValidate() {
        assertThat(
            getCharacters.validate(GetCharacters.RequestValues(false, "", 0, 10)),
            instanceOf(CompletableEmpty.INSTANCE::class.java)
        )
    }

    @Test
    fun testValidateError() {
        assertThat(
            getCharacters.validate(null),
            instanceOf(CompletableError::class.java)
        )
    }
}
