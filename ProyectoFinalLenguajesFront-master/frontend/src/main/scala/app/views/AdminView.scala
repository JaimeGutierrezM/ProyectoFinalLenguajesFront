package app.views

import app.views.{AgregarView, EliminarView, ReporteView, AnadirAdminView, PaginaPrincipal}
import com.raquo.laminar.api.L._
import org.scalajs.dom
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.html

object AdminView {

  def apply(
    currentView: Var[HtmlElement],
    librosVar: Var[List[PaginaPrincipal.Libro]]
  ): HtmlElement = {

    val adminEmail = dom.window.localStorage.getItem("email")
    val adminName = if (adminEmail != null && adminEmail.nonEmpty) adminEmail.split("@")(0) else "Admin"

    // ------------------ TARJETA DE ESTADÍSTICA ------------------
    def statsCard(number: String, label: String, iconPath: String) = {
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
          
          // Ícono SVG
          div(
            styleAttr := "width: 50px; height: 50px;",
            img(
              src := iconPath,
              styleAttr := "width: 100%; height: 100%; object-fit: contain;"
            )
          ),
          
          // Número
          div(
            number,
            styleAttr := """
              font-size: 2.5em;
              font-weight: 700;
              color: #ff6b35;
            """
          )
        ),
        
        // Label
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

    // ------------------ DISEÑO GENERAL ------------------
    div(
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
        
        // Logo izquierdo
        div(
          styleAttr := "display: flex; align-items: center; gap: 10px; max-width: 1400px; width: 100%; margin: 0 auto; justify-content: space-between;",
          img(
            src := "assets/logo.svg",
            styleAttr := "width: 120px; height: 50px;"
          ),
        
        // Perfil derecho (movido aquí para mantener el margen)
          div(
            styleAttr := "display: flex; align-items: center; gap: 12px;",
            
            div(
              styleAttr := "text-align: right;",
              div(
                "Xiomara Puma",
                styleAttr := "color: white; font-weight: 600; font-size: 0.95em;"
              ),
              div(
                "Admin",
                styleAttr := "color: rgba(255,255,255,0.8); font-size: 0.85em;"
              )
            ),
            
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
              "XP"
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
            s"Bienvenido(a) , $adminName",
            styleAttr := """
              font-size: 2em;
              font-weight: 700;
              color: #2c3e50;
              margin: 0 0 8px 0;
            """
          ),
          p(
            "Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Vestibulum efficitur nibh sit amet dignissim lobortis.",
            styleAttr := """
              color: #7f8c8d;
              font-size: 0.95em;
              margin: 0;
              line-height: 1.5;
            """
          )
        ),

        // ------------------ TARJETAS DE ESTADÍSTICAS ------------------
        div(
          styleAttr := """
            display: flex;
            gap: 20px;
            margin-bottom: 30px;
            flex-wrap: wrap;
          """,
          
          statsCard("12", "Libros vendidos", "assets/book-icon.svg"),
          statsCard("32", "Libros publicados", "assets/books-icon.svg"),
          statsCard("350,45", "Ganancia total", "assets/money-icon.svg"),
          
          // Tarjeta especial de Agregar Administrador
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
              transition: all 0.2s ease;
            """,
            onMouseOver --> { e =>
              e.target.asInstanceOf[dom.html.Element].style.boxShadow = "0 4px 12px rgba(0,0,0,0.15)"
            },
            onMouseOut --> { e =>
              e.target.asInstanceOf[dom.html.Element].style.boxShadow = "0 2px 8px rgba(0,0,0,0.08)"
            },
            onClick --> { _ => currentView.set(AnadirAdminView(currentView, librosVar)) },
            
            img(
              src := "assets/admi-plus-icon.svg",
              styleAttr := "width: 50px; height: 50px; object-fit: contain; -webkit-filter: none !important; filter: none !important; box-shadow: none !important;"
            ),
            
            span(
              "Agregar Administrador",
              styleAttr := """
                font-size: 0.95em;
                color: #666;
                font-weight: 500;
                text-shadow: none !important;
                box-shadow: none !important;
                filter: none !important;
              """
            )
          )
        ),

        // ------------------ BOTÓN AGREGAR PRODUCTO ------------------
        button(
          styleAttr := """
            background: #ff6b35;
            color: white;
            border: none;
            padding: 12px 25px;
            border-radius: 8px;
            font-weight: 600;
            font-size: 0.95em;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 20px;
            transition: all 0.2s ease;
          """,
          onMouseOver --> { e =>
            e.target.asInstanceOf[dom.html.Element].style.background = "#e55a25"
          },
          onMouseOut --> { e =>
            e.target.asInstanceOf[dom.html.Element].style.background = "#ff6b35"
          },
          onClick --> { _ => currentView.set(AgregarView(currentView, librosVar)) },
          
          "Agregar Producto ",
          span("+", styleAttr := "font-size: 1.3em; font-weight: 700;")
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
          
          // Encabezado de tabla
          div(
            styleAttr := """
              display: grid;
              grid-template-columns: 80px 2fr 1.5fr 1fr 1fr 150px;
              padding: 18px 25px;
              background: #fafafa;
              border-bottom: 1px solid #e0e0e0;
              font-weight: 600;
              color: #555;
              font-size: 0.9em;
            """,
            div("Imagen"),
            div("Título"),
            div("Categoría"),
            div("Precio"),
            div("Stock"),
            div("Acción")
          ),
          
          // Filas de productos
          children <-- librosVar.signal.map { libros =>
            libros.map { libro =>
              div(
                styleAttr := """
                  display: grid;
                  grid-template-columns: 80px 2fr 1.5fr 1fr 1fr 150px;
                  padding: 15px 25px;
                  border-bottom: 1px solid #f0f0f0;
                  align-items: center;
                  transition: background 0.2s ease;
                """,
                onMouseEnter --> { e =>
                  e.currentTarget.asInstanceOf[dom.html.Element].style.background = "#f9f9f9"
                },
                onMouseLeave --> { e =>
                  e.currentTarget.asInstanceOf[dom.html.Element].style.background = "white"
                },
                
                // Imagen
                div(
                  img(
                    src := s"https://tl7vhlzb-8081.brs.devtunnels.ms/frontimg/imagen/${libro.nombreimagen}",
                    styleAttr := """
                      width: 50px;
                      height: 70px;
                      object-fit: cover;
                      border-radius: 6px;
                      box-shadow: 0 2px 6px rgba(0,0,0,0.1);
                    """
                  )
                ),
                
                // Título
                div(
                  libro.nombre,
                  styleAttr := "color: #333; font-size: 0.9em;"
                ),
                
                // Categoría
                div(
                  span(
                    libro.nombre_categoria,
                    styleAttr := """
                      display: inline-block;
                      padding: 6px 16px;
                      background: transparent;
                      border: 1px solid #e6e6e6ff;
                      border-radius: 10px;
                      color: #666;
                      font-size: 0.85em;
                      font-weight: 500;
                    """
                  )
                ),
                
                // Precio
                div(
                  f"$$ ${libro.precio}%.2f",
                  styleAttr := "color: #333; font-size: 0.9em; font-weight: 500;"
                ),
                
                // Stock (hardcodeado ya que no está en el modelo)
                div(
                  "2",
                  styleAttr := "color: #666; font-size: 0.9em;"
                ),
                
                // Acciones
                div(
                  styleAttr := "display: flex; gap: 12px; align-items: center;",
                  
                  // Botón editar
                  div(
                    styleAttr := """
                      width: 36px;
                      height: 36px;
                      background: transparent;
                      border: 1px solid #e6e6e6ff;
                      border-radius: 8px;
                      display: flex;
                      align-items: center;
                      justify-content: center;
                      cursor: pointer;
                      transition: all 0.2s;
                    """,
                    onMouseOver --> { e =>
                      val elem = e.target.asInstanceOf[dom.html.Element]
                      elem.style.background = "#f5f5f5"
                    },
                    onMouseOut --> { e =>
                      val elem = e.target.asInstanceOf[dom.html.Element]
                      elem.style.background = "transparent"
                    },
                    img(
                      src := "assets/edit-icon.svg",
                      styleAttr := "width: 18px; height: 18px;"
                    )
                  ),
                  
                  // Botón eliminar
                  div(
                    styleAttr := """
                      width: 36px;
                      height: 36px;
                      background: transparent;
                      border: 1px solid #e6e6e6ff;
                      border-radius: 8px;
                      display: flex;
                      align-items: center;
                      justify-content: center;
                      cursor: pointer;
                      transition: all 0.2s;
                    """,
                    onMouseOver --> { e =>
                      val elem = e.target.asInstanceOf[dom.html.Element]
                      elem.style.background = "#f5f5f5"
                    },
                    onMouseOut --> { e =>
                      val elem = e.target.asInstanceOf[dom.html.Element]
                      elem.style.background = "transparent"
                    },
                    onClick --> { _ => currentView.set(EliminarView(currentView, Var(List.empty))) },
                    img(
                      src := "assets/delete-icon.svg",
                      styleAttr := "width: 18px; height: 18px;"
                    )
                  )
                )
              )
            }
          },
          
          // Paginación
          div(
            styleAttr := """
              display: flex;
              justify-content: center;
              align-items: center;
              gap: 8px;
              padding: 20px;
            """,
            
            // Flecha izquierda
            img(
              src := "assets/close-arrow-icon.svg",
              styleAttr := "width: 20px; height: 20px; cursor: pointer; opacity: 0.4; transform: rotate(180deg);",
              alt := "Anterior"
            ),
            
            span("1", styleAttr := "padding: 8px 12px; background: #ff6b35; color: white; border-radius: 5px; font-size: 0.9em; cursor: pointer; font-weight: 600;"),
            span("2", styleAttr := "padding: 8px 12px; color: #666; cursor: pointer; font-size: 0.9em;"),
            span("3", styleAttr := "padding: 8px 12px; color: #666; cursor: pointer; font-size: 0.9em;"),
            
            // Flecha derecha
            img(
              src := "assets/open-arrow-icon.svg",
              styleAttr := "width: 20px; height: 20px; cursor: pointer; opacity: 0.7;transform: rotate(180deg);",
              alt := "Siguiente"
            )
          )
        )
      ),

      // ------------------ FOOTER ------------------
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
