import androidx.compose.runtime.*
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import kotlin.random.Random

//Hello

fun main() {
    val fullSudoku = mutableStateListOf<Array<Int>>()
    for (i in 0..8){
        for (j in 0..8){
            //fullSudoku[i][j] = Random.nextInt(8)
            console.log(i, j)
        }
    }
    val boxSize: Int = if (window.innerHeight < window.innerWidth){ (window.innerHeight / 10) } else { (window.innerWidth / 10) }
    console.log(window.innerHeight, window.innerWidth, boxSize)

    renderComposable(rootElementId = "root") {
        Div {
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
                                Text(i.toString() + j.toString())
                            }//Td-end
                        }
                    }//Tr-end
                }
            }//Table-end
        }//Div-end
        Span ({style { fontSize((boxSize * 0.5).px) }}){ Text("Welcome") }
    }
}