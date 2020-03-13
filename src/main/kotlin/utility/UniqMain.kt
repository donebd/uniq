package utility

import java.io.File
import java.io.IOException

class  Uniq(private val ignoreCase : Boolean, private val uniqueLinesOutput : Boolean,
            private val countLinesOutput: Boolean, private val ignoredSymbols : Int,
            private val outputFileName : String, private val inputFileName : String){

    @Throws(IOException::class)

    fun start(){
        var text = mutableListOf<String>()
        if (inputFileName == ""){
            println("Enter text (\"exit\", for output): ")
            var c = readLine() ?:  ""
            while (c != "exit"){
                text.add(c)
                c = readLine() ?: ""
            }
        }
        else{
            text = File(inputFileName).readLines().toMutableList()
        }
        write(functional(text))
    }

    private fun functional(data : List<String>) : List<String>{
        var text = data
        if (data.size <= 1) {
            return data
        }
        val answer = mutableListOf<String>()
        if (ignoreCase) text = text.map { it.toLowerCase() }
        if (ignoredSymbols != 0) text = text.map { it.drop(ignoredSymbols) }
        var count = if (countLinesOutput) 1 else -1
        if (uniqueLinesOutput) {
            for ((i,string) in text.withIndex()) {
                if (text.indexOfFirst { it == string } == text.indexOfLast { it == string })
                answer.add(data[i])
            }
            return answer
        }

        text = text.reversed()
        var lastString = text[0]
        for ((i,string) in text.withIndex().drop(1)){
            if (string != lastString){
                if (count != -1){
                    answer.add("'$count'${data[text.size - i]}")
                    count = 1
                }
                else {
                    answer.add(data[text.size - i])
                }
            }
            else{
                if (count != -1) count++
            }
            lastString = string
        }
        if (text[text.size - 1] != text[text.size - 2] || (text[text.size - 1] == text[text.size - 2] && text[text.size - 1] !in answer))
            if (count != -1) answer.add("'${count}'${data[0]}")
            else answer.add(data[0])

        return answer.reversed()
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
