import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    println("Введите количество пользователей:")
    val userCount = readLine()?.toIntOrNull() ?: 0

    println("Введите начальный символ для паролей:")
    val startChar = readLine()?.firstOrNull() ?: 'A'

    val elapsedTime = measureTimeMillis {

        val idFlow = getIdFlow(userCount)
        val passwordFlow = getPasswordFlow(startChar.toString(), userCount)

        val idPasswordMap = idFlow.zip(passwordFlow) { id, password -> id to password }
            .toList()
            .toMap()

        println("Список ID и паролей:")
        idPasswordMap.forEach { (id, password) ->
            println("$id -> $password")
        }
    }
    println("Время выполнения: $elapsedTime мс")
}

fun createPassword(): String {
    val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..6).map { index ->
        val char = chars[Random.nextInt(chars.length)]
        if (index % 2 != 0 && char.isLetter()) char.uppercaseChar() else char.lowercaseChar()
    }.joinToString("")
}

fun getListOfPassword(input: String, length: Int): List<String> {
    return generateSequence { createPassword() }
        .filter { it.startsWith(input, ignoreCase = true) }
        .take(length)
        .toList()
}

fun getListId(length: Int): List<String> {
    return (1..length).map { id -> id.toString().padStart(6, '0') }
}

fun getIdFlow(length: Int): Flow<String> {
    return flow {
        getListId(length).forEach { emit(it) }
    }
}

fun getPasswordFlow(input: String, length: Int): Flow<String> {
    return flow {
        getListOfPassword(input, length).forEach { emit(it) }
    }
}