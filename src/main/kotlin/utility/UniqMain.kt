package utility

import java.io.BufferedWriter
import java.io.File
import java.io.IOException

class  Uniq(private val ignoreCase : Boolean, private val uniqueLinesOutput : Boolean,
            private val countLinesOutput: Boolean, private val ignoredSymbols : Int,
            private val outputFileName : String, private val inputFileName : String){

    private var outputWithEdit = mutableListOf<String>()

    private var outputText = mutableListOf<String>()

    private val ignoreSet = mutableSetOf<String>()

    private var countLines = mutableListOf<Int>()

    private var counts = 1

    @Throws(IOException::class)

    fun start(){
        val reader=  if (inputFileName == "")
            System.`in`.bufferedReader()
        else
            File(inputFileName).bufferedReader()

        if (inputFileName == "")
            println("Enter text : ")

        val text = reader.lineSequence().iterator()

        while (text.hasNext())
            functional(text.next())
        reader.close()

        checkCountLinesOutput()

        write(outputText)
    }

    private fun functional(data : String) {
        var current = data
        if (ignoreCase) current = current.toLowerCase()
        if (ignoredSymbols != 0) current = current.drop(ignoredSymbols)

        if (uniqueLinesOutput){//uniq block
            if (current !in outputWithEdit && current !in ignoreSet){
                outputWithEdit.add(current)
                outputText.add(data)
            }
            else if(current !in ignoreSet) {
                ignoreSet.add(current)
                outputText.removeAt(outputWithEdit.indexOf(current))
                outputWithEdit.indexOf(current)
            }
        }else {//other block
            if (outputWithEdit.isEmpty()) {
                outputWithEdit.add(current)
                outputText.add(data)
                countLines.add(counts)
            }
            else{
                if (current == outputWithEdit.last()) {
                    if (countLinesOutput){
                        counts ++
                        countLines = countLines.dropLast(1).toMutableList()
                        countLines.add(counts)
                    }
                } else {
                    if (countLinesOutput) {
                        counts = 1
                        countLines.add(counts)
                        outputText.add(data)
                        outputWithEdit.add(current)
                    } else {
                        outputWithEdit.add(current)
                        outputText.add(data)
                    }
                }
            }
        }
    }

    private fun checkCountLinesOutput(){
        if (countLinesOutput){
            if ( uniqueLinesOutput) {
                outputText = outputText.map { it.replace(it, "'1'$it") }.toMutableList()
            }
            else
                for ((i,string) in outputText.withIndex())
                    outputText[i] = "'${countLines[i]}'$string"

        }
    }

    private fun write(data : List<String>){
        val writer : BufferedWriter = if (outputFileName == "") {
            println("\nOutput:")
            System.out.bufferedWriter()
        }
        else
            File(outputFileName).bufferedWriter()
        writer.use {
            it.write(data.joinToString("\n"))
        }
        writer.close()
    }
}
