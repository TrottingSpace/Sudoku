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
    val sudokuSquares: MutableList<MutableList<MutableList<Int>>> = mutableListOf(*(0..2).map { mutableListOf(*(0..2).map { mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9) }.toTypedArray()) }.toTypedArray())
    for (i in 0..2) {
        for (j in 0..2) {
            sudokuSquares[i][j].shuffle()
        }
    }
    val sudokuSquaresFieldUnsorted: MutableList<MutableList<MutableList<Boolean>>> = mutableListOf(*(0..2).map { mutableListOf(*(0..2).map { mutableListOf(*(0..8).map { true }.toTypedArray()) }.toTypedArray()) }.toTypedArray())

    //assign indexes from squares to entire field
    val sudokuSquareIndex: MutableList<MutableList<Int>> = mutableListOf(*(0..8).map { mutableListOf(*(0..8).map { 0 }.toTypedArray()) }.toTypedArray())
    for (i in listOf(1, 4, 7)) {
        for (j in listOf(1, 4, 7)) {
            var squareIndex = 0
            for (k in (i-1)..(i+1)) {
                for (l in (j-1)..(j+1)) {
                    sudokuSquareIndex[k][l] = squareIndex
                    squareIndex += 1
                }
            }
        }
    }

    //copy numbers from squares to entire field
    fun mapField() {
        for (i in listOf(1, 4, 7)) {
            for (j in listOf(1, 4, 7)) {
                for (k in (i - 1)..(i + 1)) {
                    for (l in (j - 1)..(j + 1)) {
                        sudokuGenerated[k][l] = sudokuSquares[i / 3][j / 3][sudokuSquareIndex[k][l]]
                    }
                }
            }
        }
    }
    mapField()

    //searching duplicates in current row on previous fields
    fun verifyPreviousInRow(row: Int, col: Int): Boolean {
        val verifyNumber = sudokuGenerated[row][col]
        //console.log("Verifying number", verifyNumber, "at field", row, col)
        for (i in 0 until col) {
            if (sudokuGenerated[row][i] == verifyNumber) {
                //console.log("\t Number $verifyNumber is duplicated in row $row")
                return true
            }
        }
        //console.log("\t Number $verifyNumber is unique in row $row")
        return false
    }

    //searching duplicates in current column on previous fields
    fun verifyPreviousInCol(row: Int, col: Int): Boolean {
        val verifyNumber = sudokuGenerated[row][col]
        //console.log("Verifying number", verifyNumber, "at field", row, col)
        for (j in 0 until row) {
            if (sudokuGenerated[j][col] == verifyNumber) {
                //console.log("\t Number $verifyNumber is duplicated in column $col")
                return true
            }
        }
        //console.log("\t Number $verifyNumber is unique in column $col")
        return false
    }

    fun sortSudoku() {
        val sortingIndex = mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
        for (n in sortingIndex) {
            //for fields in row n
            for (j in 0..8) {
                var whileRepeatJ = 0
                while (verifyPreviousInRow(n, j)) {
                    val currentSquareIndex = sudokuSquareIndex[n][j]
                    for (swapIndex in (currentSquareIndex + 1)..8) {
                        if (sudokuSquaresFieldUnsorted[n/3][j/3][swapIndex]) {
                            val swappingNumber = sudokuSquares[n / 3][j / 3][currentSquareIndex]
                            sudokuSquares[n / 3][j / 3][currentSquareIndex] = sudokuSquares[n / 3][j / 3][swapIndex]
                            sudokuSquares[n / 3][j / 3][swapIndex] = swappingNumber
                        }
                    }
                    whileRepeatJ += 1
                    if (whileRepeatJ > 20) {
                        console.log("%c error $n $j ", "color: white; font-weight: bold; background-color: red;")
                        break
                    }
                }
            }
            //for fields in column n
            for (i in 0..8) {
                var whileRepeatJ = 0
                while (verifyPreviousInCol(i, n)) {
                    val currentSquareIndex = sudokuSquareIndex[i][n]
                    for (swapIndex in (currentSquareIndex + 1)..8) {
                        if (sudokuSquaresFieldUnsorted[i/3][n/3][swapIndex]) {
                            val swappingNumber = sudokuSquares[i / 3][n / 3][currentSquareIndex]
                            sudokuSquares[i / 3][n / 3][currentSquareIndex] = sudokuSquares[i / 3][n / 3][swapIndex]
                            sudokuSquares[i / 3][n / 3][swapIndex] = swappingNumber
                        }
                    }
                    whileRepeatJ += 1
                    if (whileRepeatJ > 20) {
                        console.log("%c error $i $n ", "color: white; font-weight: bold; background-color: red;")
                        break
                    }
                }
            }
        }
    }
    sortSudoku()

    val testVal = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 4, 5, 6, 7).distinct()
    console.log("Test:", testVal.toString())


//things that stay for now

    //verifying uniqueness in row and column
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
