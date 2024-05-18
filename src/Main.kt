import java.math.BigInteger
import kotlin.concurrent.thread

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

fun main(){
    val n=100 // przykladowa wartosc dla ktorej obliczamy silnie
    val result = factorial (n)
    println("silnia z $n to $result")
}