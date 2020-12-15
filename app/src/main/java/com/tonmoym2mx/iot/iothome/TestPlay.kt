package com.tonmoym2mx.iot.iothome

import java.util.*


fun aVeryBigSum(ar: Array<Long>): Long  {
    var sum:Long = 0
    if(ar.size in 1..10){
        ar.forEach {l ->
            if (l in 0..10000000000L){
                sum +=l
            }
        }
    }
    ar.sortedArrayDescending()
    return sum
}


fun main(args: Array<String>) {
    val scan = Scanner(System.`in`)

    val arCount = scan.nextLine().trim().toInt()

    val ar = scan.nextLine().split(" ").map{ it.trim().toLong() }.toTypedArray()

    val result = aVeryBigSum(ar)

    println(result)
}

