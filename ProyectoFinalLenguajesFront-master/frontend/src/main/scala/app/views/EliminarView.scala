package app.views

import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import app.views.PaginaPrincipal

object EliminarView {

  case class LibroCompleto(
    id_libro: Int,
    nombre: String,
    precio: Double,
    id_categoria: String,
    nombrepdf: String,
    nombreimagen: String
  )

  def apply(
    currentView: Var[HtmlElement],
    librosVar: Var[List[LibroCompleto]]
  ): HtmlElement = {

    val mostrarConfirmacion = Var(false)
    val libroAEliminar = Var[Option[LibroCompleto]](None)

    def cargarLibros(): Unit = {
      Ajax.get("https://tl7vhlzb-8081.brs.devtunnels.ms/pagina/pagina_principal")
        .map { xhr =>
          if (xhr.status == 200) {
            val data = JSON.parse(xhr.responseText).asInstanceOf[js.Array[js.Dynamic]]
            val libros = data.map { item =>
              LibroCompleto(
                id_libro = item.id_libro.asInstanceOf[Int],
                nombre = item.nombre.toString,
                precio = item.precio.toString.toDouble,
                id_categoria = item.id_categoria.toString,
                nombrepdf = item.nombrepdf.toString,
                nombreimagen = item.nombreimagen.toString
              )
            }.toList
            librosVar.set(libros)
          } else {
            dom.window.alert("Error obteniendo libros: status " + xhr.status)
          }
        }
        .recover { case ex =>
          dom.window.alert("Error cargando libros: " + ex.getMessage)
        }
    }

    cargarLibros()

    def confirmarEliminar(libro: LibroCompleto): Unit = {
      libroAEliminar.set(Some(libro))
      mostrarConfirmacion.set(true)
    }

    def eliminarLibro(): Unit = {
      libroAEliminar.now().foreach { libro =>
        Ajax.delete(s"https://tl7vhlzb-8081.brs.devtunnels.ms/eliminar/eliminar_libro?id_libro=${libro.id_libro}")
          .map { xhr =>
            if (xhr.status == 200) {
              dom.window.alert("Eliminado correctamente")
              mostrarConfirmacion.set(false)
              libroAEliminar.set(None)
              cargarLibros()
            } else {
              dom.window.alert("Error al eliminar: " + xhr.status)
            }
          }
          .recover { case ex =>
            dom.window.alert("Error conectando: " + ex.getMessage)
          }
      }
    }

    def cancelarEliminar(): Unit = {
      mostrarConfirmacion.set(false)
      libroAEliminar.set(None)
    }

    div(
      styleAttr := """
        min-height: 100vh;
        background: #f5f5f5;
        font-family: 'Roboto', sans-serif;
      """,

      // Header
      div(
        styleAttr := """
          background: #ff6b35;
          padding: 15px 40px;
          display: flex;
          justify-content: space-between;
          align-items: center;
          box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        """,
        div(
          styleAttr := "display: flex; align-items: center; gap: 15px;",
          img(
            src := "frontend/assets/logo.svg",
            styleAttr := "width: 50px; height: 50px;"
          ),
          h1(
            "Panel de Administración",
            styleAttr := """
              color: white;
              margin: 0;
              font-size: 1.5em;
              font-weight: 600;
            """
          )
        ),
        div(
          styleAttr := "display: flex; align-items: center; gap: 15px; color: white;",
          span(
            "Xiomara Puma",
            styleAttr := "font-weight: 500;"
          ),
          div(
            "Admin",
            styleAttr := """
              background: white;
              color: #ff6b35;
              width: 45px;
              height: 45px;
              border-radius: 50%;
              display: flex;
              align-items: center;
              justify-content: center;
              font-weight: 700;
              font-size: 0.9em;
            """
          )
        )
      ),

      // Contenido principal
      div(
        styleAttr := """
          max-width: 1400px;
          margin: 0 auto;
          padding: 30px 40px;
        """,

        h3("Eliminar Libros", styleAttr := "text-align: center; margin-bottom: 30px; color: #2c3e50; font-size: 1.8em; font-weight: 600;"),

        table(
          styleAttr := """
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
          """,
          thead(
            tr(
              th("ID", styleAttr := "background: #f8f9fa; padding: 15px; text-align: left; font-weight: 600; color: #2c3e50; border-bottom: 2px solid #e0e0e0;"),
              th("Nombre", styleAttr := "background: #f8f9fa; padding: 15px; text-align: left; font-weight: 600; color: #2c3e50; border-bottom: 2px solid #e0e0e0;"),
              th("Precio", styleAttr := "background: #f8f9fa; padding: 15px; text-align: left; font-weight: 600; color: #2c3e50; border-bottom: 2px solid #e0e0e0;"),
              th("Categoría", styleAttr := "background: #f8f9fa; padding: 15px; text-align: left; font-weight: 600; color: #2c3e50; border-bottom: 2px solid #e0e0e0;"),
              th("PDF", styleAttr := "background: #f8f9fa; padding: 15px; text-align: left; font-weight: 600; color: #2c3e50; border-bottom: 2px solid #e0e0e0;"),
              th("Imagen", styleAttr := "background: #f8f9fa; padding: 15px; text-align: left; font-weight: 600; color: #2c3e50; border-bottom: 2px solid #e0e0e0;"),
              th("Acciones", styleAttr := "background: #f8f9fa; padding: 15px; text-align: center; font-weight: 600; color: #2c3e50; border-bottom: 2px solid #e0e0e0;")
            )
          ),
          tbody(
            children <-- librosVar.signal.map(_.map { libro =>
              tr(
                styleAttr := "border-bottom: 1px solid #e0e0e0;",
                td(libro.id_libro.toString, styleAttr := "padding: 15px; color: #555;"),
                td(libro.nombre, styleAttr := "padding: 15px; color: #2c3e50; font-weight: 500;"),
                td(f"S/. ${libro.precio}%.2f", styleAttr := "padding: 15px; color: #555;"),
                td(libro.id_categoria, styleAttr := "padding: 15px; color: #555;"),
                td(libro.nombrepdf, styleAttr := "padding: 15px; color: #555; font-size: 0.9em;"),
                td(libro.nombreimagen, styleAttr := "padding: 15px; color: #555; font-size: 0.9em;"),
                td(
                  button(
                    "🗑️ Eliminar",
                    styleAttr := """
                      background-color: #e74c3c;
                      color: white;
                      border: none;
                      padding: 8px 16px;
                      border-radius: 6px;
                      cursor: pointer;
                      font-weight: 500;
                      transition: background-color 0.2s;
                    """,
                    onClick --> { _ => confirmarEliminar(libro) },
                    onMouseOver --> { e =>
                      e.target.asInstanceOf[dom.html.Element].style.backgroundColor = "#c0392b"
                    },
                    onMouseOut --> { e =>
                      e.target.asInstanceOf[dom.html.Element].style.backgroundColor = "#e74c3c"
                    }
                  ),
                  styleAttr := "padding: 15px; text-align: center;"
                )
              )
            })
          )
        ),

        button(
          "Volver",
          styleAttr := """
            background-color: #95a5a6;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 500;
            font-size: 1em;
            transition: background-color 0.2s;
          """,
          onClick --> { _ =>
            currentView.set(AdminView(currentView, Var(List.empty[PaginaPrincipal.Libro])))
          },
          onMouseOver --> { e =>
            e.target.asInstanceOf[dom.html.Element].style.backgroundColor = "#7f8c8d"
          },
          onMouseOut --> { e =>
            e.target.asInstanceOf[dom.html.Element].style.backgroundColor = "#95a5a6"
          }
        )
      ),

      // ---------- POPUP DE CONFIRMACIÓN ----------
      child.maybe <-- mostrarConfirmacion.signal.map { mostrar =>
        if (mostrar) Some(
          div(
            // Overlay oscuro
            styleAttr := """
              position: fixed;
              top: 0;
              left: 0;
              width: 100%;
              height: 100%;
              background: rgba(0, 0, 0, 0.5);
              display: flex;
              justify-content: center;
              align-items: center;
              z-index: 2000;
              font-family: 'Roboto', sans-serif;
            """,
            onClick --> { _ => cancelarEliminar() },

            // Modal de confirmación
            div(
              styleAttr := """
                background: white;
                padding: 30px 40px;
                border-radius: 12px;
                width: 450px;
                max-width: 90%;
                box-shadow: 0 10px 40px rgba(0,0,0,0.3);
                text-align: center;
                position: relative;
              """,
              onClick.stopPropagation --> { _ => () },

              // Botón X
              button(
                "×",
                styleAttr := """
                  position: absolute;
                  top: 15px;
                  right: 15px;
                  background: transparent;
                  border: none;
                  font-size: 1.8em;
                  color: #999;
                  cursor: pointer;
                  padding: 0;
                  width: 30px;
                  height: 30px;
                  transition: color 0.2s;
                """,
                onClick --> { _ => cancelarEliminar() },
                onMouseOver --> { e =>
                  e.target.asInstanceOf[dom.html.Element].style.color = "#333"
                },
                onMouseOut --> { e =>
                  e.target.asInstanceOf[dom.html.Element].style.color = "#999"
                }
              ),

              // Título
              h2(
                "¿Estas seguro de eliminar producto?",
                styleAttr := """
                  color: #2c3e50;
                  font-size: 1.4em;
                  font-weight: 700;
                  margin: 0 0 15px 0;
                  line-height: 1.3;
                """
              ),

              // Subtítulo
              p(
                "Esta acción no se puede deshacer.",
                styleAttr := """
                  color: #7f8c8d;
                  font-size: 0.95em;
                  margin: 0 0 30px 0;
                """
              ),

              // Botones
              div(
                styleAttr := "display: flex; gap: 12px; justify-content: center;",

                // Botón Cancelar
                button(
                  "Cancelar",
                  styleAttr := """
                    flex: 1;
                    max-width: 180px;
                    background: white;
                    border: 2px solid #ff6b35;
                    color: #ff6b35;
                    padding: 12px 24px;
                    font-size: 0.95em;
                    font-weight: 600;
                    border-radius: 8px;
                    cursor: pointer;
                    transition: all 0.2s;
                  """,
                  onClick --> { _ => cancelarEliminar() },
                  onMouseOver --> { e =>
                    val btn = e.target.asInstanceOf[dom.html.Element]
                    btn.style.background = "#fff5f0"
                  },
                  onMouseOut --> { e =>
                    val btn = e.target.asInstanceOf[dom.html.Element]
                    btn.style.background = "white"
                  }
                ),

                // Botón Eliminar
                button(
                  "Eliminar",
                  styleAttr := """
                    flex: 1;
                    max-width: 180px;
                    background: #4caf50;
                    border: none;
                    color: white;
                    padding: 12px 24px;
                    font-size: 0.95em;
                    font-weight: 600;
                    border-radius: 8px;
                    cursor: pointer;
                    transition: all 0.2s;
                  """,
                  onClick --> { _ => eliminarLibro() },
                  onMouseOver --> { e =>
                    val btn = e.target.asInstanceOf[dom.html.Element]
                    btn.style.background = "#45a049"
                  },
                  onMouseOut --> { e =>
                    val btn = e.target.asInstanceOf[dom.html.Element]
                    btn.style.background = "#4caf50"
                  }
                )
              )
            )
          )
        ) else None
      }
    )
  }
}