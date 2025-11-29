error id: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/ReporteView.scala:local3
file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/ReporteView.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -com/raquo/laminar/api/L.Var#
	 -Var#
	 -scala/Predef.Var#
offset: 492
uri: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/ReporteView.scala
text:
```scala
package app.views

import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object ReporteView {

  case class Compra(
    nombreLibro: String,
    categoria: String,
    nombrePersona: String,
    dni: String,
    correo: String,
    precio: Double
  )

  def apply(
    currentView: Var[HtmlElement],
    librosVar: @@Var[List[PaginaPrincipal.Libro]]
  ): HtmlElement = {

    val mesVar = Var("")
    val anioVar = Var("")
    val comprasVar = Var(List.empty[Compra])
    val totalVar = Var(0.0)

    // Buscar reportes
    def buscarReporte(): Unit = {
      val mes  = mesVar.now()
      val anio = anioVar.now()

      Ajax.get(
        s"https://tl7vhlzb-8081.brs.devtunnels.ms/reportes/reporte?mes=$mes&anio=$anio"
      ).map { xhr =>
        if (xhr.status == 200) {
          val json = JSON.parse(xhr.responseText)
          val data = json.reporte.asInstanceOf[js.Array[js.Dynamic]]
          val total = json.total.toString.toDouble

          val compras = data.map { item =>
            Compra(
              nombreLibro    = item.nombre_libro.toString,
              categoria      = item.categoria.toString,
              nombrePersona  = item.nombre_persona.toString,
              dni            = item.dni.toString,
              correo         = item.correo.toString,
              precio         = item.precio.toString.toDouble
            )
          }.toList

          comprasVar.set(compras)
          totalVar.set(total)
        } else dom.window.alert("Error obteniendo reporte.")
      }.recover { case ex =>
        dom.window.alert("Error: " + ex.getMessage)
      }
    }

    div(
      styleAttr := """
        width: 100%;
        display: flex;
        justify-content: center;
        font-family: 'Poppins', sans-serif;
        padding-top: 30px;
      """,

      div(
        styleAttr := """
          width: 80%;
        """,

        // Título
        h2(
          "Reportes",
          styleAttr := """
            font-size: 26px;
            font-weight: 700;
            margin-bottom: 20px;
          """
        ),

        // Filtros
        div(
          styleAttr := """
            background: white;
            padding: 25px;
            border-radius: 14px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            margin-bottom: 25px;
          """,

          div(
            styleAttr := """
              display: flex;
              justify-content: flex-start;
              gap: 25px;
              align-items: flex-end;
            """,

            // Mes
            div(
              label(
                "Mes",
                styleAttr := """
                  font-weight: 600;
                  font-size: 14px;
                """
              ),
              input(
                placeholder := "Ej: 01-12",
                onInput.mapToValue --> mesVar.writer,
                styleAttr := """
                  margin-top: 5px;
                  padding: 10px;
                  width: 180px;
                  border-radius: 8px;
                  border: 1px solid #DCDCDC;
                """
              )
            ),

            // Año
            div(
              label(
                "Año",
                styleAttr := """
                  font-weight: 600;
                  font-size: 14px;
                """
              ),
              input(
                placeholder := "Ej: 2025",
                onInput.mapToValue --> anioVar.writer,
                styleAttr := """
                  margin-top: 5px;
                  padding: 10px;
                  width: 180px;
                  border-radius: 8px;
                  border: 1px solid #DCDCDC;
                """
              )
            ),

            // Botón Aceptar
            button(
              "Aceptar",
              onClick --> { _ => buscarReporte() },
              disabled <-- mesVar.signal.combineWith(anioVar.signal).map { case (m, a) =>
                m.isEmpty || a.isEmpty
              },
              styleAttr := """
                background: #FF7A00;
                color: white;
                border: none;
                padding: 10px 25px;
                border-radius: 8px;
                font-weight: 600;
                cursor: pointer;
                margin-bottom: 3px;
              """
            )
          )
        ),

        // Tabla de reportes
        div(
          styleAttr := """
            background: white;
            padding: 25px;
            border-radius: 14px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
          """,

          table(
            styleAttr := """
              width: 100%;
              border-collapse: separate;
              border-spacing: 0 12px;
            """,

            thead(
              tr(
                List("Título", "Categoría", "Cliente", "DNI", "Correo", "Precio").map { titulo =>
                  th(
                    titulo,
                    styleAttr := """
                      text-align: left;
                      padding: 10px;
                      font-size: 14px;
                      font-weight: 600;
                      color: #444;
                    """
                  )
                }
              )
            ),

            tbody(
              children <-- comprasVar.signal.map(
                _.map { c =>
                  tr(
                    styleAttr := """
                      background: #F8F8F8;
                      border-radius: 10px;
                    """,

                    td(c.nombreLibro, styleAttr := "padding: 12px;"),
                    td(c.categoria, styleAttr := "padding: 12px;"),
                    td(c.nombrePersona, styleAttr := "padding: 12px;"),
                    td(c.dni, styleAttr := "padding: 12px;"),
                    td(c.correo, styleAttr := "padding: 12px;"),
                    td(f"S/ ${c.precio}%.2f", styleAttr := "padding: 12px; font-weight: 600;")
                  )
                }
              )
            )
          ),

          // TOTAL
          div(
            styleAttr := """
              margin-top: 20px;
              display: flex;
              justify-content: flex-end;
              font-size: 18px;
              font-weight: bold;
            """,
            span("Total: S/ "),
            child.text <-- totalVar.signal.map(t => f"$t%.2f")
          )
        ),

        button(
          "Volver",
          onClick --> { _ => currentView.set(AdminView(currentView, librosVar)) },
          styleAttr := """
            margin-top: 25px;
            background: #A6A6A6;
            color: white;
            padding: 10px 20px;
            border-radius: 8px;
            border: none;
            cursor: pointer;
          """
        )
      )
    )
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 