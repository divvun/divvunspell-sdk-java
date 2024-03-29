package no.divvun.divvunspell

import com.sun.jna.NativeLong
import com.sun.jna.Pointer
import java.nio.charset.StandardCharsets.UTF_8

data class CaseHandlingConfig(
    val startPenalty: Float = 0.0f,
    val endPenalty: Float = 0.0f,
    val midPenalty: Float = 0.0f
)

data class SpellerConfig(
    val nBest: Long? = null,
    val maxWeight: Float? = null,
    val beam: Float? = null,
    val caseHandling: CaseHandlingConfig? = null,
    val nodePoolSize: Long? = null
)

class DivvunSpellException(message: String?) : Exception(message)

class Speller internal constructor(private val handle: TraitObjectPointer.ByValue) {
    @Throws(DivvunSpellException::class)
    fun isCorrect(word: String): Boolean {
        if (word.isEmpty()) {
            return false
        }
        val res = word.withStringPointer {
            CLibrary.divvun_speller_is_correct(handle, it, errorCallback)
        }
        assertNoError()
        return res
    }

    @Throws(DivvunSpellException::class)
    fun suggest(word: String): List<String> {
        val slice = word.withStringPointer {
            CLibrary.divvun_speller_suggest(handle, it, errorCallback)
        }
        assertNoError()
        return suggest(slice)
    }

    @Throws(DivvunSpellException::class)
    fun suggest(word: String, config: SpellerConfig): List<String> {
        val slice = word.withStringPointer {
            CLibrary.divvun_speller_suggest(handle, it, errorCallback)
        }
        assertNoError()
        return suggest(slice)
    }

    @Throws(DivvunSpellException::class)
    private fun suggest(slice: SlicePointer.ByValue): List<String> {
        val len = CLibrary.divvun_vec_suggestion_len(slice, errorCallback)
        assertNoError()

        val out = mutableListOf<String>()

        for (i in 0L until len.toLong()) {
            val value = CLibrary.divvun_vec_suggestion_get_value(slice, NativeLong(i, true), errorCallback)
            assertNoError()
            val str = value.decode() ?: continue
            CLibrary.cffi_string_free(value)
            out.add(str)
        }

        return out
    }
}

class SpellerArchive private constructor(private val handle: TraitObjectPointer.ByValue) {
    companion object {
        @Throws(DivvunSpellException::class)
        fun open(path: String): SpellerArchive {
            val handle = path.withPathPointer {
                CLibrary.divvun_speller_archive_open(it, errorCallback)
            }
            assertNoError()
            return SpellerArchive(handle)
        }
    }

    fun speller(): Speller {
        val spellerHandle = CLibrary.divvun_speller_archive_speller(handle, errorCallback)
        assertNoError()
        return Speller(spellerHandle)
    }
}


private var lastError: String? = null

internal val errorCallback = ErrorCallback { ptr, size ->
    val bytes = ptr.getByteArray(0, Pointer.nativeValue(size).toInt())
    lastError = String(bytes, UTF_8)
}


@Throws(DivvunSpellException::class)
private fun assertNoError() {
    if (lastError != null) {
        val message = lastError
        lastError = null
        throw DivvunSpellException(message)
    }
}

fun <T> String.withStringPointer(callback: (StringPointer.ByValue) -> T): T {
    val s = StringPointer.ByValue.encode(this)
    return callback(s)
}

fun <T> String.withPathPointer(callback: (PathPointer.ByValue) -> T): T {
    val s = PathPointer.ByValue.encode(this)
    return callback(s)
}

fun enableDivvunSpellLogging() {
    CLibrary.divvun_enable_logging()
}