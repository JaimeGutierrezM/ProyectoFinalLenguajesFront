package app

import com.raquo.laminar.api.L._
import org.scalajs.dom
import app.views.{LoginView, RegistroView, PaginaPrincipal, FiltroView}

object Main {
  def main(args: Array[String]): Unit = {
    // 1. Variable de libros
    val librosVar = Var(
      List(
        PaginaPrincipal.Libro(1, "Cazuela con verduritas", 20.0, "1", "Sopas", "CazuelaConVerduritas.pdf", "CazuelaConVerduritas.jpg"),
        PaginaPrincipal.Libro(2, "Canelones de verduras", 25.0, "2", "Saludables", "CanelonesDeVerduras.pdf", "CanelonesDeVerduras.jpg"),
        PaginaPrincipal.Libro(3, "Escalivada de berenjenas y pimientas", 30.0, "3", "PlatosPrincipales", "EscalivadaDeBerenjenasyPimientos.pdf", "EscalivadaDeBerenjenasyPimientos.jpg"),
        PaginaPrincipal.Libro(4, "Crujientes de champiñones", 22.0, "4", "PlatosPrincipales", "CrujientesDeChampiñones.pdf", "CrujientesDeChampiñones.jpg"),
        PaginaPrincipal.Libro(5, "Quien te quiere te cocina cerca", 28.0, "5", "Saludable", "SorbeteDeLimonAlCavaParaNavidad.pdf", "SorbeteDeLimonAlCavaParaNavidad.jpg"),
        PaginaPrincipal.Libro(6, "Recetario invierno", 35.0, "6", "Sopas", "SalsaDeEsparragos.pdf", "SalsaDeEsparragos.jpg")
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
