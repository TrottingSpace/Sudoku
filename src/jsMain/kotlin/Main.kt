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
    val sudokuBackend: MutableList<MutableList<MutableList<Int>>> = mutableListOf(*(0..8).map { mutableListOf(*(0..8).map { mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9) }.toTypedArray()) }.toTypedArray())
    val sudokuGenerated: MutableList<MutableList<Int>> = mutableListOf(*(0..8).map { mutableListOf(*(0..8).map { 404 }.toTypedArray()) }.toTypedArray())
    val sudokuFrontend: MutableList<MutableList<Int>> = mutableStateListOf(*(0..8).map { mutableStateListOf(*(0..8).map { 0 }.toTypedArray()) }.toTypedArray())


    val sudokuSquare: MutableList<MutableList<MutableList<Int>>> = mutableListOf(*(0..2).map { mutableListOf(*(0..2).map { mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9) }.toTypedArray()) }.toTypedArray())

    val sudokuByUser: MutableList<MutableList<Boolean>> = mutableStateListOf(*(0..8).map { mutableStateListOf(*(0..8).map { false }.toTypedArray()) }.toTypedArray())

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
            var randomField = 0
            if (fieldsList != 0) {
                randomField = Random.nextInt(fieldsList)
            }
            sudokuByUser[sudokuFields[randomField][0]][sudokuFields[randomField][1]] = true
            console.log("Field:", sudokuFields[randomField][0], sudokuFields[randomField][1], "is now:", sudokuByUser[sudokuFields[randomField][0]][sudokuFields[randomField][1]])
            sudokuFields.removeAt(randomField)
        }
    }
    fieldsForUser(0)

    fun resetSudoku() {
        for (i in 0..8) {
            for (j in 0..8) {
                for (n in 0..8) {
                    if (sudokuBackend[i][j].size <= n) {
                        sudokuBackend[i][j].add(0)
                    }
                    sudokuBackend[i][j][n] = n + 1
                }
                sudokuGenerated[i][j] = 707
            }
        }
    }

    fun removeFromRow(row: Int, n: Int) {
        //console.log("Removing", n, "from row", row)
        for (j in 0..8) {
            if (sudokuBackend[row][j].contains(n)) {
                sudokuBackend[row][j].remove(n)
                //console.log("\t", n, "successfully removed from field", j)
            } else {
                //console.log("\t Field", j, "don't contains", n)
            }
        }
    }

    fun removeFromColumn(col: Int, n: Int) {
        //console.log("Removing", n, "from column", col)
        for (i in 0..8) {
            if (sudokuBackend[i][col].contains(n)) {
                sudokuBackend[i][col].remove(n)
                //console.log("\t", n, "successfully removed from field", i)
            } else {
                //console.log("\t Field", i, "don't contains", n)
            }
        }
    }

    fun removeFromSquare(row: Int, col: Int) {
        val squareCenterRow = ((row / 3) * 3) + 1
        val squareCenterCol = ((col / 3) * 3) + 1
        //console.log("Removing", sudokuGenerated[row][col], "from square", (row / 3), (col / 3))
        for (i in (squareCenterRow - 1)..(squareCenterRow + 1)) {
            for (j in (squareCenterCol - 1)..(squareCenterCol + 1)) {
                if (sudokuBackend[i][j].contains(sudokuGenerated[row][col])) {
                    sudokuBackend[i][j].remove(sudokuGenerated[row][col])
                    //console.log("\t Removed", sudokuGenerated[row][col], "from field", i, j)
                } else {
                    //console.log("\t Field", i, j, "don't contains", sudokuGenerated[row][col])
                }
            }
        }
    }

    fun drawRandom(row: Int, col: Int) {
        val listLength = sudokuBackend[row][col].size
        if (listLength <= 0) {
            sudokuGenerated[row][col] = 0
        } else {
            val randomIndex = Random.nextInt(listLength)
            sudokuGenerated[row][col] = sudokuBackend[row][col][randomIndex]
        }
    }

    val boxSize: Int = if (window.innerHeight < window.innerWidth){ (window.innerHeight / 10) } else { (window.innerWidth / 10) }
    console.log(" Window height:\t", window.innerHeight, "\n Window width:\t", window.innerWidth, "\n Size factor:\t", boxSize)

    val sudokuDiv = document.getElementById("sudoku_root")
    sudokuDiv?.setAttribute("style", "padding: 0px; border: none; aspect-ratio: 1;")
    console.log(" Sudoku div width:\t", sudokuDiv?.clientWidth, "\n Sudoku div height:\t", sudokuDiv?.clientHeight)

    for (i in 0..8) {
        for (j in 0..8) {
            drawRandom(i, j)
            removeFromSquare(i, j)
            removeFromRow(i, sudokuGenerated[i][j])
            removeFromColumn(j, sudokuGenerated[i][j])
        }
    }

    renderComposable(rootElementId = "sudoku_root") {
        Div ({ style { padding(1.px) } }){
            Table({
                style {
                    fontSize((boxSize * 0.35).px)
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
                }
            }) {
                for (i in 0..8) {
                    Tr ({ style { /*height(boxSize.px)*/ } }){
                        for (j in 0..8) {
                            Td ({ style { /*width(boxSize.px); */border(1.px, LineStyle.Solid, Color.blueviolet);padding(0.px); backgroundColor(if (sudokuGenerated[i][j] == 0) {Color.lightpink} else if (sudokuBackend[i][j].size != 0) {Color.lightyellow} else {Color.lightgray}) } }){
                                if (sudokuByUser[i][j]) {
                                    Select({
                                        style { if (sudokuFrontend[i][j] == 0) { backgroundColor(Color.aquamarine) }; property("width", "100%"); property("height", "100%"); fontSize((boxSize * 0.35).px); textAlign("center"); outline("none"); margin(0.px); padding(0.px); property("border", "none") }
                                        onChange {
                                            sudokuFrontend[i][j] = it.value!!.toInt()
                                            console.log("Field:\t $i\t $j\n\t", sudokuFrontend[i][j])
                                        }
                                    }) {
                                        (0..9).forEach {
                                            Option(it.toString()) {
                                                Text(if (it == 0) { "~" } else { it.toString() })
                                            }
                                        }
                                    }
                                } else {
                                    Text(sudokuGenerated[i][j].toString() + sudokuBackend[i][j].toString())
                                    //Text(sudokuGenerated[i][j].toString())
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
