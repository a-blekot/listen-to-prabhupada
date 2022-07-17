package com.listentoprabhupada.common.utils

sealed interface TextResource {
    fun resolve(vararg formatArgs: Any) : String =
        when (this) {
            is PlainText -> this.text
            is IdText -> Strings.get(id)
            is PluralText -> Strings.get(id, quantity)
            is FormattedText -> Strings.format(id, *formatArgs)
        }
}

fun plainText(text : String) : TextResource = PlainText(text)
fun idText(id : String) : TextResource = IdText(id)
fun pluralText(id: String, pluralValue : Int) : TextResource = PluralText(id, pluralValue)
fun formattedText(id: String) : TextResource = FormattedText(id)

private data class PlainText(
    val text : String
) : TextResource

private data class IdText(
    val id : String
) : TextResource

private data class PluralText(
    val id : String,
    val quantity: Int
) : TextResource

private data class FormattedText(
    val id : String
) : TextResource
