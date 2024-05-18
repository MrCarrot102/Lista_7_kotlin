import java.math.BigInteger
import kotlin.concurrent.thread
import java.math.BigDecimal
import java.math.MathContext

// zadanie 1 obliczanie duzej silni
fun factorial(n:Int):BigInteger{
    // liczba dostepnych rdzeni procesora
    val numThread = Runtime.getRuntime().availableProcessors()
    // liczba podzadan dla kazdego watku
    val rangeSize = n / numThread
    // lista przedzialow dla kazdego watku
    val ranges = mutableListOf<Pair<Int, Int>>()
    // tworzenie przedziałow dlakazdego watku
    var start = 1
    repeat ( numThread - 1 ) {
        val end = start + rangeSize - 1
        ranges.add( Pair ( start, end ))
        start = end + 1
    }
    ranges.add( Pair ( start, n))
    val threads = mutableListOf<Thread>()
    val results = mutableListOf<BigInteger>()
    // funkcja do obliczania silni dla kazdego przedzialu
    fun calculateFactorial( start : Int , end : Int ) {
        var result = BigInteger.ONE
        for(i in start..end){
            result *= BigInteger.valueOf(i.toLong())
        }
        synchronized(result){
            results.add(result)
        }
    }
    // uruchamianie watkow dla kazdego przedzialu
    for( range in ranges ){
        val thread = thread(start=true){
            calculateFactorial(range.first, range.second)
        }
        threads.add(thread)
    }
    // czekanie na zakonczenie wszystkich watkow
    threads.forEach { it.join() }
    // obliczanie koncowego wyniku na podstawie wynikow czesciowych
    var finalResult = BigInteger.ONE
    for( partialResult in results ){
        finalResult *= partialResult
    }
    return finalResult
}
// zadanie 2
fun calculateEulerNumber(n:Int):BigDecimal{
    var result=BigDecimal.ONE
    var factorial = BigDecimal.ONE
    for(i in 1..n){
        factorial *=BigDecimal.valueOf(i.toLong())
        result += BigDecimal.ONE.divide(factorial, MathContext.DECIMAL128)
    }
    return result
}

fun main(){
    // sprawdzanie 1 zadania czyli duzej silni
    val n=100 // przykladowa wartosc dla ktorej obliczamy silnie
    val result = factorial (n)
    println("silnia z $n to $result")
    // sprawdzanie 2 zadania liczenie z wzoru
    val n_2 = 17
    val eulerNumber=calculateEulerNumber(n_2)
    println("EulerNumber: $eulerNumber")
}