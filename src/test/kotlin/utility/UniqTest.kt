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
            it.newLine()
            it.write("abc")
            it.newLine()
            it.write("test")
        }

        UniqLaunch().start(arrayOf("-o", "temp.txt", "some.txt"))
        assertEquals(listOf("test","abc","test"), File("temp.txt").readLines())

        UniqLaunch().start(arrayOf("-o", "temp.txt", "some.txt", "-u"))
        assertEquals("abc", File("temp.txt").readText())

        UniqLaunch().start(arrayOf("-o", "temp.txt", "some.txt", "-c"))
        assertEquals(listOf("'3'test","'1'abc","'1'test"), File("temp.txt").readLines())

        UniqLaunch().start(arrayOf("-o", "temp.txt", "some.txt", "-c", "-u"))
        assertEquals("'1'abc", File("temp.txt").readText())

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

        file.delete()
        File("temp.txt").delete()
    }

    @Test
    fun bigDataTest() {// WARNING 4 GB file
        val file = File("some.txt")
        file.bufferedWriter().use {
            for (j in 0..9999999) {
                for (i in 0..99) {
                    it.write("TEST")
                }
                it.newLine()
            }
            it.write("success")
        }

        UniqLaunch().start(arrayOf("-o", "temp.txt", "some.txt","-u"))
        assertEquals("success", File("temp.txt").readText())

        file.delete()
        File("temp.txt").delete()
    }
}
