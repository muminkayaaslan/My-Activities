package com.aslansoft.myactivities

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform