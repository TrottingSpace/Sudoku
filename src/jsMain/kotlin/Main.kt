import androidx.compose.runtime.*
import kotlinx.browser.window
//import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import kotlin.random.Random

fun main() {
    console.log("%c Welcome in Sudoku! ", "color: white; font-weight: bold; background-color: black;")
    val sudokuBackend: MutableList<MutableList<MutableList<Int>>> = mutableListOf(*(0..8).map { mutableListOf(*(0..8).map { mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9) }.toTypedArray()) }.toTypedArray())
    val sudokuFrontend: MutableList<MutableList<Int>> = mutableStateListOf(*(0..8).map { mutableStateListOf(*(0..8).map { 404 }.toTypedArray()) }.toTypedArray())

    /*
    val sudokuFields: MutableList<MutableList<Int>> = mutableListOf(*(0..80).map { mutableListOf(*(0..1).map { 404 }.toTypedArray()) }.toTypedArray())
    for (i in 0..8) {
        for (j in 0..8) {
            sudokuFields[(i * 9) + j][0] = i
            sudokuFields[(i * 9) + j][1] = j
            //console.log("Position:", ((i * 9) + j), "\n\t Field:", sudokuFields[(i * 9) + j][0], sudokuFields[(i * 9) + j][1])
        }
    }
    */

    //val testArr3d: MutableList<MutableList<MutableList<Int>>> = mutableListOf(*(0..8).map { mutableListOf(*(0..8).map { mutableListOf(*(0..8).map { 0 }.toTypedArray()) }.toTypedArray()) }.toTypedArray())

    var errorFound = false
    var errorCounter = 0
    var rebuildCounter = 0

    fun resetSudoku() {
        for (i in 0..8) {
            for (j in 0..8) {
                for (n in 0..8) {
                    if (sudokuBackend[i][j].size <= n) {
                        sudokuBackend[i][j].add(0)
                    }
                    sudokuBackend[i][j][n] = n + 1
                }
                sudokuFrontend[i][j] = 707
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
        //console.log("Removing", sudokuFrontend[row][col], "from square", (row / 3), (col / 3))
        for (i in (squareCenterRow - 1)..(squareCenterRow + 1)) {
            for (j in (squareCenterCol - 1)..(squareCenterCol + 1)) {
                if (sudokuBackend[i][j].contains(sudokuFrontend[row][col])) {
                    sudokuBackend[i][j].remove(sudokuFrontend[row][col])
                    //console.log("\t Removed", sudokuFrontend[row][col], "from field", i, j)
                } else {
                    //console.log("\t Field", i, j, "don't contains", sudokuFrontend[row][col])
                }
            }
        }
    }

    fun drawRandom(row: Int, col: Int) {
        val listLength = sudokuBackend[row][col].size
        if (listLength <= 0) {
            sudokuFrontend[row][col] = 0
            errorFound = true
            errorCounter += 1
            //console.log("Error has been found\n\t Counted $errorCounter errors")
        } else {
            val randomIndex = Random.nextInt(listLength)
            sudokuFrontend[row][col] = sudokuBackend[row][col][randomIndex]
        }
        //removeFromRow(x, sudokuFrontend[x][y])
        //removeFromColumn(y, sudokuFrontend[x][y])
    }

    val boxSize: Int = if (window.innerHeight < window.innerWidth){ (window.innerHeight / 10) } else { (window.innerWidth / 10) }
    console.log(" Window height:\t", window.innerHeight, "\n Window width:\t", window.innerWidth, "\n Size factor:\t", boxSize)

    /*do {
        if (errorFound) {
            rebuildCounter += 1
            console.log("Errors found: $errorCounter\n Rebuild $rebuildCounter started ...")
            errorFound = false
            errorCounter = 0
            resetSudoku()
        }*/
        for (i in 0..8) {
            for (j in 0..8) {
                drawRandom(i, j)
                removeFromSquare(i, j)
                removeFromRow(i, sudokuFrontend[i][j])
                removeFromColumn(j, sudokuFrontend[i][j])
            }
        }
        /*if (!errorFound) {
            console.log("%c Generated sudoku is correct.", "color: black; font-weight: bold; background-color: lightgreen;")
        }
    } while (errorFound)*/

    /*
    for (n in 0..80) {
        val fieldsCount = sudokuFields.size
        val randomIndex = Random.nextInt(fieldsCount)
        drawRandom(sudokuFields[randomIndex][0], sudokuFields[randomIndex][1])
        removeFromSquare(sudokuFields[randomIndex][0], sudokuFields[randomIndex][1])
        removeFromRow(sudokuFields[randomIndex][0], sudokuFrontend[sudokuFields[randomIndex][0]][sudokuFields[randomIndex][1]])
        removeFromColumn(sudokuFields[randomIndex][1], sudokuFrontend[sudokuFields[randomIndex][0]][sudokuFields[randomIndex][1]])
        sudokuFields.removeAt(randomIndex)
    }
    */

    renderComposable(rootElementId = "root") {
        Div ({ style { padding(1.px) } }){
            Table({
                style {
                    fontSize((boxSize * 0.35).px)
                    border(1.px, LineStyle.Solid, Color.blueviolet)
                    textAlign("center")
                    property("vertical-align", "center")
                    property("table-layout", "fixed")
                    property("border-spacing", "0px")
                    width(((boxSize * 9) + 2).px)
                    height(((boxSize * 9) + 2).px)
                }
            }) {
                for (i in 0..8) {
                    Tr ({ style { height(boxSize.px) } }){
                        for (j in 0..8) {
                            Td ({ style { width(boxSize.px); border(1.px, LineStyle.Solid, Color.blueviolet); backgroundColor(if (sudokuFrontend[i][j] == 0) {Color.lightpink} else if (sudokuBackend[i][j].size != 0) {Color.lightyellow} else {Color.white}) } }){
                                Text(sudokuFrontend[i][j].toString() + sudokuBackend[i][j].toString())
                                //Text(sudokuFrontend[i][j].toString())
                            }//Td-end
                        }
                    }//Tr-end
                }
            }//Table-end
        }//Div-end
        //window.alert("Project under development.")
        //Text("Generated correct sudoku at try: " + (rebuildCounter + 1).toString())
        Br()
        Text("Project under development.")
    }
}
