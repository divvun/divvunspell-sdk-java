import no.divvun.divvunspell.SpellerArchive

fun main() {
    val ar = SpellerArchive.open("/Library/Services/se.bundle/Contents/Resources/se.zhfst")

    println(ar)

    println(ar.speller().suggest("same"))
}