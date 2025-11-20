error id: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/Main.scala:app/views/PaginaPrincipal.
file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/Main.scala
empty definition using pc, found symbol in pc: app/views/PaginaPrincipal.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -com/raquo/laminar/api/L.PaginaPrincipal.
	 -app/views/PaginaPrincipal.
	 -PaginaPrincipal.
	 -scala/Predef.PaginaPrincipal.
offset: 400
uri: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/Main.scala
text:
```scala
package app

import com.raquo.laminar.api.L._
import org.scalajs.dom
import app.views.{LoginView, RegistroView, PaginaPrincipal, FiltroView}

object Main {
  def main(args: Array[String]): Unit = {
    // 1. Variable de libros
    val librosVar = Var(
      List(
        PaginaPrincipal.Libro(1, "Cocteles para todos", 20.0, "1", "Bebidas", "libro1.pdf", "assets/libro1.jpg"),
        PaginaPrincipa@@l.Libro(2, "Cenas Navideñas llenas de magia", 25.0, "2", "Navideñas", "libro2.pdf", "assets/libro2.jpg"),
        PaginaPrincipal.Libro(3, "¡Bravazo!", 30.0, "3", "Platos principales", "libro6.pdf", "assets/libro6.jpg"),
        PaginaPrincipal.Libro(4, "Dulces caseros", 22.0, "4", "Postres", "libro3.pdf", "assets/libro3.jpg"),
        PaginaPrincipal.Libro(5, "Quien te quiere te cocina cerca", 28.0, "5", "Saludable", "libro4.pdf", "assets/libro4.jpg"),
        PaginaPrincipal.Libro(6, "Recetario invierno", 35.0, "6", "Sopas", "libro5.pdf", "assets/libro5.jpg")
      )
    )

    // 2. Inicializar correctamente con una vista válida (no null ni emptyNode)
    val currentView: Var[HtmlElement] =
      Var(PaginaPrincipal(currentView = null, librosVar)) // temporal null

    // 3. Asignar correctamente luego de creada la variable
    currentView.set(PaginaPrincipal(currentView, librosVar))

    render(
      dom.document.body,
      div(
        styleAttr := "font-family: 'Roboto', sans-serif;",
        child <-- currentView
        )
    )
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: app/views/PaginaPrincipal.