package utility

import org.junit.Assert.*
import org.junit.Test
import  java.io.File

class UniqTest {

    @Test
    fun testBasicFunctional() {
        val file = File("some.txt")
        file.bufferedWriter().use {
            it.write("test")
            it.newLine()
            it.write("test")
            it.newLine()
            it.write("test")
        }

        UniqLaunch().start(arrayOf("-o", "temp.txt", "some.txt"))
        assertEquals("test", File("temp.txt").readText())

        UniqLaunch().start(arrayOf("-o", "temp.txt", "some.txt", "-u"))
        assertEquals("", File("temp.txt").readText())

        UniqLaunch().start(arrayOf("-o", "temp.txt", "some.txt", "-c"))
        assertEquals("'3'test", File("temp.txt").readText())

        file.bufferedWriter().use {
            it.write("teST")
            it.newLine()
            it.write("test")
            it.newLine()
            it.write("vest")
        }

        UniqLaunch().start(arrayOf("-o", "temp.txt", "some.txt", "-i", "-u"))
        assertEquals("vest", File("temp.txt").readText())

        UniqLaunch().start(arrayOf("-o", "temp.txt", "some.txt", "-i", "-s", "1"))
        assertEquals("teST", File("temp.txt").readText())

        File("some.txt").delete()
        File("temp.txt").delete()
    }

    @Test
    fun bigDataTest() {
        val file = File("some.txt")
        file.bufferedWriter().use {
            for (j in 0..99999) {
                for (i in 0..99) {
                    it.write("TEST")
                }
                it.newLine()
            }
            it.write("success")
        }

        UniqLaunch().start(arrayOf("-o", "temp.txt", "some.txt","-u"))
        assertEquals("success", File("temp.txt").readText())

        File("some.txt").delete()
        File("temp.txt").delete()
    }
}
