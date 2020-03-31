package utility

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
        val text : Iterator<String> = if (inputFileName == ""){
            println("Enter text : ")
            System.`in`.bufferedReader().lineSequence().iterator()
        } else{
            File(inputFileName).bufferedReader().lineSequence().iterator()
        }
        while (text.hasNext())
            functional(text.next())

        if (countLinesOutput){
            if ( uniqueLinesOutput) {
                outputText = outputText.map { it.replace(it, "'1'$it") }.toMutableList()
            }
            else
                for ((i,string) in outputText.withIndex())
                        outputText[i] = "'${countLines[i]}'$string"

        }

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

    private fun write(data : List<String>){
        if (outputFileName == "") {
            println("\nOutput : ")
            for (string in data) println(string)
        }
        else{
            val writer = File(outputFileName).bufferedWriter()
            for ((i, string) in data.withIndex()) {
                if (i != data.size-1)
                    writer.appendln(string)
                else
                    writer.append(string)
            }
            writer.close()
        }
    }

}
