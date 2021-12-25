import androidx.compose.runtime.*
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import kotlin.random.Random

fun main() {
    val fullSudoku: MutableList<MutableList<Int>> = mutableStateListOf(
        mutableStateListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableStateListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableStateListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableStateListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableStateListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableStateListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableStateListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableStateListOf(0, 0, 0, 0, 0, 0, 0, 0, 0),
        mutableStateListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    )
    for (i in 0..8){
        for (j in 0..8){
            fullSudoku[i][j] = (Random.nextInt(9) + 1)
            console.log(i, j, fullSudoku[i][j])
        }
    }
    val boxSize: Int = if (window.innerHeight < window.innerWidth){ (window.innerHeight / 10) } else { (window.innerWidth / 10) }
    console.log(window.innerHeight, window.innerWidth, boxSize)

    renderComposable(rootElementId = "root") {
        Div ({ style { padding((boxSize / 2).px) } }){
            Table({
                style {
                    fontSize((boxSize * 0.75).px)
                    border(1.px, LineStyle.Solid, Color.blueviolet)
                    textAlign("center")
                    property("vertical-align", "center")
                }
            }) {
                for (i in 0..8) {
                    Tr ({ style { height(boxSize.px) } }){
                        for (j in 0..8) {
                            Td ({ style { width(boxSize.px) } }){
                                //Text(i.toString() + j.toString())
                                Text(fullSudoku[i][j].toString())
                            }//Td-end
                        }
                    }//Tr-end
                }
            }//Table-end
        }//Div-end
        Span ({style { fontSize((boxSize * 0.5).px) }}){ Text("Welcome\n This thing doesn't want to work.") }
    }
}
