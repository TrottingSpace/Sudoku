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

    //val testArr3d: MutableList<MutableList<MutableList<Int>>> = mutableListOf(*(0..8).map { mutableListOf(*(0..8).map { mutableListOf(*(0..8).map { 0 }.toTypedArray()) }.toTypedArray()) }.toTypedArray())

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
        } else {
            val randomIndex = Random.nextInt(listLength)
            sudokuFrontend[row][col] = sudokuBackend[row][col][randomIndex]
        }
        //removeFromRow(x, sudokuFrontend[x][y])
        //removeFromColumn(y, sudokuFrontend[x][y])
    }

    val boxSize: Int = if (window.innerHeight < window.innerWidth){ (window.innerHeight / 10) } else { (window.innerWidth / 10) }
    console.log(" Window height:\t", window.innerHeight, "\n Window width:\t", window.innerWidth, "\n Size factor:\t", boxSize)

    for (i in 0..8) {
        for (j in 0..8) {
            drawRandom(i, j)
            removeFromSquare(i, j)
            removeFromRow(i, sudokuFrontend[i][j])
            removeFromColumn(j, sudokuFrontend[i][j])
        }
    }

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
        Text("Project under development.")
    }
}
