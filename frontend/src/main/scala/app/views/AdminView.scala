package app.views

import app.views.{AgregarView, EliminarView, ReporteView, AnadirAdminView, PaginaPrincipal}
import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.html

object AdminView {

  def apply(
    currentView: Var[HtmlElement],
    librosVar: Var[List[PaginaPrincipal.Libro]]
  ): HtmlElement = {

    // ===========================================================
    // DATOS DEL ADMIN: nombre dinámico y sus iniciales
    // ===========================================================

    val adminEmail = dom.window.localStorage.getItem("email")
    val adminName =
      if (adminEmail != null && adminEmail.nonEmpty)
        adminEmail.split("@")(0)
      else
        "Admin"

    val adminInitials =
      adminName.split("[^A-Za-z]").filter(_.nonEmpty).take(2).map(_.charAt(0).toUpper).mkString

    val ventasVar = Var(0)
    val gananciaVar = Var(0.0)

   


    // === REPORTE: ventas y ganancia ===
    Ajax.get("https://tl7vhlzb-8081.brs.devtunnels.ms/reporte").foreach { xhr =>
      val data = JSON.parse(xhr.responseText).asInstanceOf[js.Dynamic]

      val reporte = data.reporte.asInstanceOf[js.Array[js.Dynamic]]
      val totalGanancia = data.total.asInstanceOf[Double]

      ventasVar.set(reporte.length)
      gananciaVar.set(totalGanancia)
    }

    // ------------------ TARJETA DE ESTADÍSTICA ------------------
    def statsCard(number: Signal[String], label: String, iconPath: String) = {
      div(
        styleAttr := """
          background: white;
          border-radius: 12px;
          padding: 25px 30px;
          box-shadow: 0 2px 8px rgba(0,0,0,0.08);
          display: flex;
          flex-direction: column;
          gap: 10px;
          min-width: 200px;
          flex: 1;
        """,

        div(
          styleAttr := "display: flex; align-items: center; gap: 15px;",
          div(
            styleAttr := "width: 50px; height: 50px;",
            img(
              src := iconPath,
              styleAttr := "width: 100%; height: 100%; object-fit: contain;"
            )
          ),
          div(
            child.text <-- number,
            styleAttr := """
              font-size: 2.5em;
              font-weight: 700;
              color: #ff6b35;
            """
          )
        ),

        div(
          label,
          styleAttr := """
            font-size: 0.95em;
            color: #666;
            font-weight: 500;
          """
        )
      )
    }


    // ------------------------------------------------------------
    // DISEÑO GENERAL
    // ------------------------------------------------------------
    div(
       // === CARGAR LIBROS DESDE EL BACKEND UNA SOLA VEZ ===
      onMountCallback { _ =>
        Ajax.get("https://tl7vhlzb-8081.brs.devtunnels.ms/libros").foreach { xhr =>
          val json = JSON.parse(xhr.responseText).asInstanceOf[js.Array[js.Dynamic]]

          val libros = json.map { d =>
            PaginaPrincipal.Libro(
              d.id_libro.asInstanceOf[Int],
              d.nombre.toString,
              d.precio.asInstanceOf[Double],
              d.id_categoria.toString,
              d.nombre_categoria.toString,
              d.nombrepdf.toString,
              d.nombreimagen.toString
            )
          }.toList

          librosVar.set(libros)
        }
      },
      styleAttr := """
        width: 100%;
        min-height: 100vh;
        background: #f5f5f5;
        font-family: 'Roboto', sans-serif;
      """,

      // ------------------ HEADER NARANJA ------------------
      div(
        styleAttr := """
          width: 100%;
          background: #ff6b35;
          padding: 15px 40px;
          display: flex;
          justify-content: center;
          align-items: center;
          box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        """,

        div(
          styleAttr := "display: flex; align-items: center; gap: 10px; max-width: 1400px; width: 100%; margin: 0 auto; justify-content: space-between;",

          img(
            src := "assets/logo.svg",
            styleAttr := "width: 120px; height: 50px;"
          ),

          // =====================================================
          // NUEVA SECCIÓN: nombre dinámico + iniciales + logout
          // =====================================================
          div(
            styleAttr := "display: flex; align-items: center; gap: 20px;",

            // Nombre del admin
            div(
              styleAttr := "text-align: right;",
              div(
                adminName,
                styleAttr := "color: white; font-weight: 600; font-size: 0.95em;"
              ),
              div(
                "Administrador",
                styleAttr := "color: rgba(255,255,255,0.8); font-size: 0.85em;"
              )
            ),

            // Iniciales
            div(
              styleAttr := """
                width: 45px;
                height: 45px;
                border-radius: 50%;
                background: white;
                display: flex;
                align-items: center;
                justify-content: center;
                color: #ff6b35;
                font-weight: 700;
                font-size: 1.2em;
              """,
              adminInitials
            ),

            // ----------------- BOTÓN CERRAR SESIÓN -----------------
            button(
              "Cerrar Sesión",
              onClick --> { _ =>
                dom.window.localStorage.clear()
                currentView.set(RegistroView(currentView, librosVar))
              },
              styleAttr :=
                """background: white;
                  |border: none;
                  |padding: 10px 20px;
                  |border-radius: 8px;
                  |cursor: pointer;
                  |font-weight: 600;
                  |color: #ff6b35;
                  |transition: 0.3s;
                """.stripMargin,
              onMouseOver --> { e =>
                e.target.asInstanceOf[dom.html.Element].style.backgroundColor = "#ffe3d1"
              },
              onMouseOut --> { e =>
                e.target.asInstanceOf[dom.html.Element].style.backgroundColor = "white"
              }
            )
          )
        )
      ),

      // ------------------ CONTENIDO PRINCIPAL ------------------
      div(
        styleAttr := "padding: 40px; max-width: 1400px; margin: 0 auto;",

        // Bienvenida
        div(
          styleAttr := "margin-bottom: 25px;",
          h1(
            s"Bienvenido(a), $adminName",
            styleAttr := """
              font-size: 2em;
              font-weight: 700;
              color: #2c3e50;
              margin: 0 0 8px 0;
            """
          )
        ),

        // -------- TARJETAS DE ESTADÍSTICAS --------
        div(
          styleAttr := """
            display: flex;
            gap: 20px;
            margin-bottom: 30px;
            flex-wrap: wrap;
          """,

          statsCard(librosVar.signal.map(_.length.toString), "Total de Libros", "assets/books-icon.svg"),
          statsCard(ventasVar.signal.map(_.toString), "Ventas Totales", "assets/books-icon.svg"),
          statsCard(gananciaVar.signal.map(_.toString), "Ganancia total", "assets/money-icon.svg"),

          // Tarjeta especial: agregar admin
          div(
            styleAttr := """
              background: white;
              border-radius: 12px;
              padding: 25px 30px;
              box-shadow: 0 2px 8px rgba(0,0,0,0.08);
              display: flex;
              align-items: center;
              gap: 15px;
              min-width: 200px;
              flex: 1;
              cursor: pointer;
            """,
            onClick --> { _ => currentView.set(AnadirAdminView(currentView, librosVar)) },

            img(
              src := "assets/admi-plus-icon.svg",
              styleAttr := "width: 50px; height: 50px;"
            ),
            span(
              "Agregar Administrador",
              styleAttr := """
                font-size: 0.95em;
                color: #666;
                font-weight: 500;
              """
            )
          )
        ),

        div(
          styleAttr := "display: flex; gap: 12px; margin-bottom: 20px; align-items: center;",

          // BOTÓN AGREGAR PRODUCTO (base)
          button(
            styleAttr := """
              display: inline-flex;
              align-items: center;
              justify-content: center;
              gap: 8px;
              height: 44px;
              padding: 0 20px;
              background: #ff6b35;
              color: white;
              border: none;
              border-radius: 8px;
              font-weight: 600;
              font-size: 0.95em;
              cursor: pointer;
              box-sizing: border-box;
            """,
            onClick --> { _ => currentView.set(AgregarView(currentView, librosVar)) },
            span("Agregar Producto"),
            span("+", styleAttr := "font-size: 1.2em; font-weight: 700; line-height: 1;")
          ),

          // BOTÓN VER INFORMES (alineado)
          button(
            styleAttr := """
              display: inline-flex;
              align-items: center;
              justify-content: center;
              gap: 10px;
              height: 44px;
              padding: 0 18px;
              background: #3B6B3C;
              color: white;
              border: none;
              border-radius: 8px;
              font-weight: 600;
              font-size: 0.95em;
              cursor: pointer;
              box-sizing: border-box;
            """,
            onClick --> { _ => currentView.set(ReporteView(currentView,librosVar)) },
            span("Ver Informes", styleAttr := "line-height: 1;"),
            span("+", styleAttr := "font-size: 1.2em; font-weight: 700; line-height: 1;")
          )
        ),

        // ------------------ TABLA DE PRODUCTOS ------------------
        div(
          styleAttr := """
            background: white;
            border-radius: 12px;
            padding: 0;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
            overflow: hidden;
          """,

          // Encabezado
          div(
            styleAttr := """
              display: grid;
              grid-template-columns: 80px 2fr 1.5fr 1fr 1fr 150px;
              padding: 18px 25px;
              background: #fafafa;
              border-bottom: 1px solid #e0e0e0;
            """,
            div("Imagen"),
            div("Título"),
            div("Categoría"),
            div("Precio"),
            div("Stock"),
            div("Acción")
          ),

          // Filas dinámicas
          children <-- librosVar.signal.map { libros =>
            libros.map { libro =>
              div(
                styleAttr := """
                  display: grid;
                  grid-template-columns: 80px 2fr 1.5fr 1fr 1fr 150px;
                  padding: 15px 25px;
                  border-bottom: 1px solid #f0f0f0;
                  align-items: center;
                """,

                div(
                  img(
                    src := s"https://tl7vhlzb-8081.brs.devtunnels.ms/frontimg/imagen/${libro.nombreimagen}",
                    styleAttr := """
                      width: 50px;
                      height: 70px;
                      object-fit: cover;
                      border-radius: 6px;
                    """
                  )
                ),

                div(libro.nombre),
                div(libro.nombre_categoria),
                div(f"$$ ${libro.precio}%.2f"),
                div("2"),

                // Acciones
                div(
                  styleAttr := "display: flex; gap: 12px;",
                  img(src := "assets/edit-icon.svg", styleAttr := "width: 20px; cursor: pointer;"),
                  img(
                    src := "assets/delete-icon.svg",
                    styleAttr := "width: 20px; cursor: pointer;",
                    onClick --> { _ =>
                      currentView.set(EliminarView(currentView, Var(List.empty)))
                    }
                  )
                )
              )
            }
          }
        )
      ),

      // FOOTER
      div(
        styleAttr := """
          text-align: center;
          padding: 20px;
          background: #ff6b35;
          color: white;
          font-size: 0.9em;
          margin-top: 40px;
        """,
        "Derechos Reservados 2025"
      )
    )
  }
  }
