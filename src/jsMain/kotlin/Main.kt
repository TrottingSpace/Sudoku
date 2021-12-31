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

    fun removeFromRow(row: Int, k: Int) {
        //console.log("Removing", k, "from row", row)
        for (j in 0..8) {
            if (sudokuBackend[row][j].contains(k)) {
                sudokuBackend[row][j].remove(k)
                //console.log("\t", k, "successfully removed from field", j)
            } else {
                //console.log("\t Field", j, "don't contains", k)
            }
        }
    }

    fun removeFromColumn(col: Int, k: Int) {
        //console.log("Removing", k, "from column", col)
        for (i in 0..8) {
            if (sudokuBackend[i][col].contains(k)) {
                sudokuBackend[i][col].remove(k)
                //console.log("\t", k, "successfully removed from field", i)
            } else {
                //console.log("\t Field", i, "don't contains", k)
            }
        }
    }

    fun drawRandom(x: Int, y: Int) {
        val listLength = sudokuBackend[x][y].size
        if (listLength <= 0) {
            sudokuFrontend[x][y] = 0
        } else {
            val randomIndex = Random.nextInt(listLength)
            sudokuFrontend[x][y] = sudokuBackend[x][y][randomIndex]
            //removeFromRow(x, sudokuBackend[x][y][randomIndex])
            //removeFromColumn(y, sudokuBackend[x][y][randomIndex])
            //sudokuBackend[x][y].removeAt(randomIndex)
        }
    }

    val boxSize: Int = if (window.innerHeight < window.innerWidth){ (window.innerHeight / 10) } else { (window.innerWidth / 10) }
    console.log(" Window height:\t", window.innerHeight, "\n Window width:\t", window.innerWidth, "\n Size factor:\t", boxSize)

    for (i in 0..8) {
        for (j in 0..8) {
            drawRandom(i, j)
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
                            Td ({ style { width(boxSize.px); border(1.px, LineStyle.Solid, Color.blueviolet); backgroundColor(if (sudokuFrontend[i][j] == 0) {Color.lightpink} else {Color.white}) } }){
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
