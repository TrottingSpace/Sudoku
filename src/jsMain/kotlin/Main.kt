import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.browser.window
//import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import kotlin.random.Random

fun main() {
    console.log("%c Welcome in Sudoku! ", "color: white; font-weight: bold; background-color: black;")
    val sudokuGenerated: MutableList<MutableList<Int>> = mutableListOf(*(0..8).map { mutableListOf(*(0..8).map { 404 }.toTypedArray()) }.toTypedArray())
    //val sudokuConflict: MutableList<MutableList<Boolean>> = mutableListOf(*(0..8).map { mutableListOf(*(0..8).map { false }.toTypedArray()) }.toTypedArray())
    val sudokuSquares: MutableList<MutableList<MutableList<Int>>> = mutableListOf(*(0..2).map { mutableListOf(*(0..2).map { mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9) }.toTypedArray()) }.toTypedArray())
    for (i in 0..2) {
        for (j in 0..2) {
            sudokuSquares[i][j].shuffle()
        }
    }
    //console.log(sudokuSquares.toString())


    //squares to fields prerender
    val sudokuSquareIndex: MutableList<MutableList<Int>> = mutableListOf(*(0..8).map { mutableListOf(*(0..8).map { 0 }.toTypedArray()) }.toTypedArray())
    for (i in listOf(1, 4, 7)) {
        for (j in listOf(1, 4, 7)) {
            //console.log("Field selected:", i, j)
            var squareIndex = 0
            for (k in (i-1)..(i+1)) {
                for (l in (j-1)..(j+1)) {
                    sudokuGenerated[k][l] = sudokuSquares[i / 3][j / 3][squareIndex]
                    sudokuSquareIndex[k][l] = squareIndex
                    //console.log("\t Field:", k, l, "\t Number:", sudokuGenerated[k][l], "\t From index:", squareIndex)
                    squareIndex += 1
                }
            }
        }
    }


    fun verifyField(row: Int, col: Int): Boolean {
        val verifyNumber = sudokuGenerated[row][col]
        for (n in 0..8) {
            if (sudokuGenerated[row][n] == verifyNumber && n != col) {
                return true
            }
            if (sudokuGenerated[n][col] == verifyNumber && n != row) {
                return true
            }
        }
        return false
    }


    fun shuffleAgain(row: Int, col: Int) {
        val sRow = row / 3
        val sCol = col / 3
        var sIndex = sudokuSquareIndex[row][col]
        if (sIndex < 8) {
            val toShuffle: MutableList<Int> = mutableListOf(*(0..(8 - sIndex)).map { 0 }.toTypedArray())
            console.log("Reshuffling", sudokuSquares[sRow][sCol].toString(), "from index", sudokuSquareIndex[row][col], "which is number", sudokuSquares[sRow][sCol][sIndex])
            for (k in 0 until toShuffle.size) {
                toShuffle[k] = sudokuSquares[sRow][sCol][sIndex]
                sIndex += 1
            }
            console.log(toShuffle.toString())
            while (toShuffle[0] == sudokuSquares[sRow][sCol][sudokuSquareIndex[row][col]]) {
                toShuffle.shuffle()
                console.log("\t Shuffled ")
            }
            console.log(toShuffle.toString())
            //sIndex = sudokuSquareIndex[row][col]
            for (l in 0 until toShuffle.size) {
                sudokuSquares[sRow][sCol][sudokuSquareIndex[row][col] + l] = toShuffle[l]
            }
            console.log(sudokuSquares[sRow][sCol].toString())
        }
        else {
            console.log("%c Critical ERROR! \n  at field  $row $col  ", "color: white; font-weight: bold; background-color: red;")
        }
    }
    //shuffleAgain(1, 1)


    fun verifyPrevious(row: Int, col: Int): Boolean {
        val verifyNumber = sudokuGenerated[row][col]
        console.log("Verifying number", verifyNumber, "at field", row, col)
        for (i in 0 until col) {
            if (sudokuGenerated[row][i] == verifyNumber) {
                console.log("\t Number $verifyNumber is duplicated")
                return true
            }
        }
        for (j in 0 until row) {
            if (sudokuGenerated[j][col] == verifyNumber) {
                console.log("\t Number $verifyNumber is duplicated")
                return true
            }
        }
        console.log("\t Number $verifyNumber is unique")
        return false
    }
    /*if (verifyPrevious(2, 3)) {
        console.log("Number found")
    }*/


    //squares to fields function
    for (i in 0..8) {
        val boxRow = ((i / 3) * 3) +1
        val sRow = i / 3
        for (j in 0..8) {
            val boxCol = ((j / 3) * 3) +1
            val sCol = j / 3
            var errorLoop = 0
            while (verifyPrevious(i, j)) {
                shuffleAgain(i, j)
                for (k in (boxRow-1)..(boxRow+1)) {
                    for (l in (boxCol-1)..(boxCol+1)) {
                        sudokuGenerated[i][j] = sudokuSquares[sRow][sCol][sudokuSquareIndex[i][j]]
                    }
                }
                errorLoop += 1
                console.log("Try", errorLoop)
                if (errorLoop > 100) {
                    console.log("%c  LOOP ERROR!  \n at field  $i $j ", "color: white; font-weight: bold; background-color: red;")
                    break
                }
            }
        }
    }


    val testVal = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 4, 5, 6, 7).distinct()
    console.log("Test:", testVal.toString())


//things that stay for now

    val sudokuFrontend: MutableList<MutableList<Int>> = mutableStateListOf(*(0..8).map { mutableStateListOf(*(0..8).map { 0 }.toTypedArray()) }.toTypedArray())
    val fieldByUser: MutableList<MutableList<Boolean>> = mutableStateListOf(*(0..8).map { mutableStateListOf(*(0..8).map { false }.toTypedArray()) }.toTypedArray())

    //list of fields locations
    val sudokuFields: MutableList<MutableList<Int>> = mutableListOf(*(0..80).map { mutableListOf(*(0..1).map { 404 }.toTypedArray()) }.toTypedArray())
    for (i in 0..8) {
        for (j in 0..8) {
            sudokuFields[(i * 9) + j][0] = i
            sudokuFields[(i * 9) + j][1] = j
            //console.log("Position:", ((i * 9) + j), "\n\t Field:", sudokuFields[(i * 9) + j][0], sudokuFields[(i * 9) + j][1])
        }
    }

    fun fieldsForUser(n: Int) {
        for (i in 0 until n) {
            val fieldsList = sudokuFields.size
            if (fieldsList != 0) {
                val randomField = Random.nextInt(fieldsList)
                fieldByUser[sudokuFields[randomField][0]][sudokuFields[randomField][1]] = true
                console.log("Field:", sudokuFields[randomField][0], sudokuFields[randomField][1], "is now:", fieldByUser[sudokuFields[randomField][0]][sudokuFields[randomField][1]])
                sudokuFields.removeAt(randomField)
            }
        }
    }
    fieldsForUser(0)

    val boxSize: Int = if (window.innerHeight < window.innerWidth){ (window.innerHeight / 10) } else { (window.innerWidth / 10) }
    console.log(" Window height:\t", window.innerHeight, "\n Window width:\t", window.innerWidth, "\n Size factor:\t", boxSize)

    val sudokuDiv = document.getElementById("sudoku_root")
    sudokuDiv?.setAttribute("style", "padding: 0px; border: none; aspect-ratio: 1;")
    val divFontSize: Int = if (sudokuDiv != null) { sudokuDiv.clientWidth / 28 } else { 10 }
    console.log(" Sudoku div width:\t", sudokuDiv?.clientWidth, "\n Sudoku div height:\t", sudokuDiv?.clientHeight, "\n Sudoku font size:\t", divFontSize)


    val sudokuFieldSelect: MutableList<MutableList<Int>> = mutableListOf(*(0..80).map { mutableListOf(*(0..1).map { 404 }.toTypedArray()) }.toTypedArray())
    val sudokuFieldSelectTest: MutableList<MutableList<Int>> = mutableListOf(*(0..8).map { mutableListOf(*(0..8).map { 0 }.toTypedArray()) }.toTypedArray())
    var numHorizontal = 0
    var numVertical = 9
    //console.log("numHorizontal-0", numHorizontal)
    for (i in 0..8) {
        for (j in i..8) {
            //console.log("Index:", numHorizontal, "\t Location:", i, j)
            sudokuFieldSelect[numHorizontal][0] = i
            sudokuFieldSelect[numHorizontal][1] = j
            sudokuFieldSelectTest[i][j] = numHorizontal
            numHorizontal += 1
        }
        //console.log("numHorizontal-1", numHorizontal)
        numHorizontal += 8 - i
        //console.log("numHorizontal-2", numHorizontal)
    }
    //console.log("numVertical-0", numVertical)
    for (j in 0..7) {
        for (i in (j + 1)..8) {
            //console.log("Index:", numVertical, "\t Location:", i, j)
            sudokuFieldSelect[numVertical][0] = i
            sudokuFieldSelect[numVertical][1] = j
            sudokuFieldSelectTest[i][j] = numVertical
            numVertical += 1
        }
        //console.log("numVertical-1", numVertical)
        numVertical += 8 - j
        //console.log("numVertical-2", numVertical)
    }
    //console.log(sudokuFieldSelect.toString())


    renderComposable(rootElementId = "sudoku_root") {
        Div ({ style { padding(1.px) } }){
            Table({
                style {
                    fontSize(divFontSize.px)
                    border(1.px, LineStyle.Solid, Color.blueviolet)
                    textAlign("center")
                    //property("vertical-align", "center")
                    property("table-layout", "fixed")
                    property("border-spacing", "0px")
                    //width(((boxSize * 9) + 2).px)
                    //height(((boxSize * 9) + 2).px)
                    property("width", "100%")
                    property("height", "100%")
                    property("aspect-ratio", "1")
                    backgroundColor(Color.lightgray)
                }
            }) {
                for (i in 0..8) {
                    Tr ({ style { /*height(boxSize.px)*/ } }){
                        for (j in 0..8) {
                            Td ({ style { /*width(boxSize.px); */border(1.px, LineStyle.Solid, Color.blueviolet); padding(0.px); if (verifyField(i, j)) { backgroundColor(Color.lightcoral) } } }){
                                if (fieldByUser[i][j]) {
                                    Select({
                                        style { if (sudokuFrontend[i][j] == 0) { backgroundColor(Color.lightyellow) }; property("width", "100%"); property("height", "100%"); fontSize(divFontSize.px); textAlign("center"); outline("none"); margin(0.px); padding(0.px); property("border", "none") }
                                        onChange {
                                            sudokuFrontend[i][j] = it.value!!.toInt()
                                            console.log("Field: $i $j updated")
                                        }
                                    }) {
                                        (0..9).forEach {
                                            Option(it.toString()) {
                                                Text(if (it == 0) { "~" } else { it.toString() })
                                            }
                                        }
                                    }
                                } else {
                                    Text(sudokuGenerated[i][j].toString())
                                    //Text(sudokuFieldSelectTest[i][j].toString())
                                }
                            }//Td-end
                        }
                    }//Tr-end
                }
            }//Table-end
        }//Div-end
        //window.alert("Project under development.")
        //Br()
        //Text("Project under development.")
    }
}
