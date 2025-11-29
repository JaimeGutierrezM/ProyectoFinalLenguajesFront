package app.views

import com.raquo.laminar.api.L._
import app.views.PaginaPrincipal.Libro
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object FiltroView {

  def apply(
    currentView: Var[HtmlElement],
    librosVar: Var[List[Libro]]
  ): HtmlElement = {

    val categoriaSeleccionada = Var("0")
    val precioMinVar = Var("")
    val precioMaxVar = Var("")
    val ordenVar = Var("A-Z")

    def aplicarFiltros(): Unit = {
      val idCategoria = categoriaSeleccionada.now()
      val precioMin = precioMinVar.now()
      val precioMax = precioMaxVar.now()
      val orden = if (ordenVar.now() == "A-Z") "true" else "false"

      val idCategoriaParam = if (idCategoria != "0") s"id_categoria=$idCategoria&" else ""
      val precioMinParam = if (precioMin.nonEmpty) s"precio_minimo=$precioMin&" else ""
      val precioMaxParam = if (precioMax.nonEmpty) s"precio_maximo=$precioMax&" else ""
      val ordenParam = s"orden=$orden"

      val url = s"https://tl7vhlzb-8081.brs.devtunnels.ms/filtrar/obtener_librosfiltro?$idCategoriaParam$precioMinParam$precioMaxParam$ordenParam"

      Ajax.get(url).foreach { xhr =>
        if (xhr.status == 200) {
          val response = JSON.parse(xhr.responseText).asInstanceOf[js.Array[js.Dynamic]]
          val librosFiltrados = response.map { libro =>
            Libro(
              id_libro = libro.id_libro.toString.toInt,
              nombre = libro.nombre.toString,
              precio = libro.precio.toString.toDouble,
              id_categoria = libro.id_categoria.toString,
              nombre_categoria = if (js.isUndefined(libro.nombre_categoria)) "" else libro.nombre_categoria.toString,
              nombrepdf = libro.nombrepdf.toString,
              nombreimagen = libro.nombreimagen.toString
            )
          }.toList
          librosVar.set(librosFiltrados)
          currentView.set(PaginaPrincipal(currentView, librosVar))
        }
      }
    }

    // --- INTERFAZ VISUAL ---
    div(
      styleAttr := """
        display: flex;
        flex-direction: column;
        align-items: center;
        font-family: 'Inter', sans-serif;
        background-color: white;
        padding: 15px;
        border-radius: 12px;
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        gap: 15px;
        width: 100%;
        max-width: 900px;
        margin: 0 auto;
      """,

      // FILA DE CATEGORÍAS
      div(
        styleAttr := """
          display: flex;
          justify-content: space-around;
          flex-wrap: wrap;
          gap: 10px;
          background-color: #f7f7f7;
          padding: 10px;
          border-radius: 10px;
        """,
        Seq(
          ("0", "Sopas"),
          ("1", "Bebidas"),
          ("2", "Navideñas"),
          ("3", "Postres"),
          ("4", "Platos Principales"),
          ("5", "Saludables")
        ).map { case (id, nombre) =>
          button(
            nombre,
            onClick.mapTo(id) --> categoriaSeleccionada.writer,
            styleAttr <-- categoriaSeleccionada.signal.map { sel =>
              val active = sel == id
              s"""
                padding: 8px 15px;
                border-radius: 10px;
                border: none;
                background-color: ${if (active) "#ffffff" else "transparent"};
                color: #333;
                font-weight: ${if (active) "600" else "500"};
                border: ${if (active) "1px solid #2d6a4f" else "none"};
                box-shadow: ${if (active) "0 0 4px rgba(45,106,79,0.3)" else "none"};
                cursor: pointer;
                transition: all 0.2s ease;
              """
            }
          )
        }
      ),

      // FILA DE FILTROS
      div(
        styleAttr := """
          display: flex;
          justify-content: space-between;
          align-items: center;
          flex-wrap: wrap;
          gap: 10px;
          width: 100%;
          margin-top: 10px;
        """,

        // Precio mínimo
        div(
          styleAttr := "display: flex; flex-direction: column; flex: 1; min-width: 120px;",
          label("Precio mínimo", styleAttr := "font-size: 0.9em; color: #555; margin-bottom: 4px;"),
          input(
            typ := "number",
            placeholder := "Ej: 10.00",
            value <-- precioMinVar.signal,
            onInput.mapToValue --> precioMinVar.writer,
            styleAttr := """
              padding: 8px;
              border-radius: 8px;
              border: 1px solid #ddd;
              font-size: 0.95em;
              background-color: #f5f5f5;
            """
          )
        ),

        // Precio máximo
        div(
          styleAttr := "display: flex; flex-direction: column; flex: 1; min-width: 120px;",
          label("Precio máximo", styleAttr := "font-size: 0.9em; color: #555; margin-bottom: 4px;"),
          input(
            typ := "number",
            placeholder := "Ej: 100.00",
            value <-- precioMaxVar.signal,
            onInput.mapToValue --> precioMaxVar.writer,
            styleAttr := """
              padding: 8px;
              border-radius: 8px;
              border: 1px solid #ddd;
              font-size: 0.95em;
              background-color: #f5f5f5;
            """
          )
        ),

        // Orden
        div(
          styleAttr := "display: flex; flex-direction: column; flex: 1; min-width: 120px;",
          label("Ordenar por", styleAttr := "font-size: 0.9em; color: #555; margin-bottom: 4px;"),
          select(
            value <-- ordenVar.signal,
            onChange.mapToValue --> ordenVar.writer,
            option(value := "A-Z", "A - Z"),
            option(value := "Z-A", "Z - A"),
            styleAttr := """
              padding: 8px;
              border-radius: 8px;
              border: 1px solid #ddd;
              font-size: 0.95em;
              background-color: #f5f5f5;
            """
          )
        ),

        // Botón buscar
        button(
          "BUSCAR",
          onClick --> { _ => aplicarFiltros() },
          styleAttr := """
            background-color: #f97316;
            color: white;
            font-weight: bold;
            border: none;
            border-radius: 10px;
            padding: 10px 20px;
            cursor: pointer;
            transition: background-color 0.2s ease, transform 0.1s ease;
          """,
          onMouseOver --> { _.target.asInstanceOf[dom.html.Button].style.backgroundColor = "#ea580c" },
          onMouseOut --> { _.target.asInstanceOf[dom.html.Button].style.backgroundColor = "#f97316" }
        )
      )
    )
  }
}
