package utility

import org.kohsuke.args4j.*
import java.io.IOException

class UniqLaunch {
    @Option(name="-i",metaVar = "ignoreCase",required = false,usage = "Ignore letters case")
    private var ignoreCase = false

    @Option(name="-u",metaVar = "outputUniqs",required = false,usage = "Unique lines for output")
    private var uniqueLinesOutput = false

    @Option(name="-c",metaVar = "linesCount",required = false,usage = "Count lines for output")
    private var countLinesOutput = false

    @Option(name="-s",metaVar = "ignoreSymbols",required = false,usage = "Ignore first N symbols")
    private var ignoredSymbols = 0

    @Option(name="-o",metaVar = "outputFile",required = false,usage = "File for output")
    private var outputFileName = ""

    @Argument(required = false, metaVar = "InputFiles", usage = "Input files names")
    private var inputFileName = ""

    fun start(args : Array<String>){
        val parser = CmdLineParser(this)
        try {
            parser.parseArgument(*args)
        } catch (e: CmdLineException) {
            println(e.message)
            println("uniq [-i] [-u] [-c] [-s num] [-o ofile] [file]")
            parser.printUsage(System.out)
            return
        }

        try {
            Uniq(ignoreCase, uniqueLinesOutput, countLinesOutput, ignoredSymbols, outputFileName, inputFileName).start()
        } catch (e: IOException) {
            println(e.message)
            return
        }

    }
}

fun main(args : Array<String>){
    UniqLaunch().start(args)
}