package app.views

import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import app.views.FiltroView

// Import asumido para resolver el error de ProductView utilizado en el onClick
import app.views.ProductView 


object ClientView {

  // Asumiendo que PaginaPrincipal.Libro está accesible o importado.

  def apply(
    currentView: Var[HtmlElement],
    librosVar: Var[List[PaginaPrincipal.Libro]],
    personaVar: Var[Option[js.Dynamic]],
    logout: () => Unit
  ): HtmlElement = {

    div(
      // FONDO GENERAL
      styleAttr := """
        display: flex;
        flex-direction: column;
        height: 100vh;
        font-family: 'Roboto', sans-serif;
        color: white;
        position: relative;
        overflow: hidden;
      """,

      // ============================
      // BARRA SUPERIOR
      // ============================
      div(
        styleAttr := """
          background-image: url('/frontend/src/main/scala/app/views/Rectangle_1.png');
          background-size: cover;
          background-position: center;
          background-repeat: no-repeat;
          padding-bottom: 120px;
          padding-top: 20px; /* Ajuste para dar espacio superior */
          padding-left: 40px; /* Ajuste para dar espacio lateral */
          padding-right: 40px; /* Ajuste para dar espacio lateral */
          display: flex;             
          flex-direction: column;    
          align-items: stretch;      
          position: relative;
          overflow: hidden;
        """,

        // --- Contenedor del Título y el Botón (Alineados a los extremos) ---
        div(
          styleAttr := """
            display: flex;
            align-items: center;
            justify-content: space-between; 
            margin-bottom: 20px;
          """,

          // 1. Título "RecetApp" (Izquierda)
          div(
            styleAttr := "display: flex; align-items: center; gap: 10px;",
            h2(
              "RecetApp",
              styleAttr := """
                font-size: 1.8rem;
                font-weight: 700;
                color: #e67e22;
                margin: 0;
                font-family: 'Poppins', sans-serif;
                text-shadow: 2px 2px 6px rgba(0,0,0,0.4);
              """
            )
          ),
          
          // 2. Botón "CERRAR SESIÓN" (Derecha)
          div(
            styleAttr := "display: flex; gap: 14px;",
            button(
              "CERRAR SESIÓN",
              title := "Cerrar sesión",
              styleAttr := """
                background: #e67e22;
                border: none;
                color: white;
                border-radius: 50px;
                padding: 8px 20px;
                font-size: 1rem;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.3s ease;
              """,
              onClick --> { _ =>
                logout() 
              }
            )
          ),
        ), // Fin del div para el Título y Botón

        // 3. Mensaje de Bienvenida 
        child <-- personaVar.signal.map {
          case Some(persona) =>
            // Acceso robusto: verifica undefined/null antes de usar .toString
            val nombre = Option(persona.nombre) match {
              case Some(n) if !js.isUndefined(n) && n != null => n.toString
              case _ => "USUARIO"
            }
            s"¡BIENVENIDO!\n$nombre"
          
          case None => 
            "¡BIENVENIDO!\nUSUARIO"
        }
          .map(texto =>
            div(
              texto,
              styleAttr := """
                font-size: 2.2rem;
                font-weight: 800;
                color: black;
                text-align: center;
                white-space: pre-line;
                margin-top: 10px; /* Reducido el margen superior */
                margin-left: auto;
                margin-right: auto;
                position: relative;
                padding: 0px 20px;
              """
            )
          )
      ), // Fin de la BARRA SUPERIOR
      
      FiltroView(currentView, librosVar),
      // ==================================================
      // LISTA DE LIBROS
      // ==================================================
      // Contenido principal de la vista del cliente
      div(
        styleAttr := """
          flex: 1;
          display: flex;
          flex-direction: column;
          align-items: center;
          padding: 30px;
          overflow-y: auto;
        """,
        child <-- librosVar.signal.map { lista => 
          if (lista.isEmpty) div("No hay libros disponibles.", styleAttr := "margin-top: 50px; font-size: 1.2em; color: #555;")
          else {
            div(
              styleAttr := "display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 32px; justify-items: center; padding: 30px 10px; background: #f8f8f8; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); max-width: 1200px; width: 100%;",
              
              // FIX 1: Type Mismatch / Ambiguity (Se usa el spread operator :_*)
              lista.map { libro =>
                div(
                  styleAttr := "width: 250px; border: 1px solid #dfe6e9; border-radius: 16px; padding: 18px 14px; text-align: center; background-color: #fff; box-shadow: 0 2px 12px rgba(0,0,0,0.08); transition: box-shadow 0.2s, transform 0.2s ease; cursor: pointer; position: relative; overflow: hidden; margin-bottom: 10px;",
                  
                  // FIX 2: Missing Parameter Type (Se usa el parámetro explícito 'ev')
                  onMouseOver --> { (ev: dom.MouseEvent) => 
                    ev.target.asInstanceOf[dom.html.Div].style.boxShadow = "0 8px 20px rgba(0,0,0,0.15)"
                    ev.target.asInstanceOf[dom.html.Div].style.transform = "scale(1.02)"
                  },
                  
                  // FIX 2: Missing Parameter Type (Se usa el parámetro explícito 'ev')
                  onMouseOut --> { (ev: dom.MouseEvent) => 
                    ev.target.asInstanceOf[dom.html.Div].style.boxShadow = "0 2px 12px rgba(0,0,0,0.08)"
                    ev.target.asInstanceOf[dom.html.Div].style.transform = "scale(1)"
                  },
                  
                  img(
                    src := s"https://tl7vhlzb-8081.brs.devtunnels.ms/frontimg/imagen/${libro.nombreimagen}",
                    alt := libro.nombre,
                    width := "170",
                    height := "240",
                    styleAttr := "border-radius: 10px; margin-bottom: 14px; box-shadow: 0 2px 8px rgba(0,0,0,0.10); object-fit: cover; object-position: top center; max-width: 100%; max-height: 240px; display: block; margin-left: auto; margin-right: auto; background: #f1f2f6;"
                  ),
                  h4(libro.nombre, styleAttr := "font-size: 1.15rem; color: #333; margin: 12px 0 6px 0; font-weight: 700; min-height: 48px;"),
                  p(libro.nombre_categoria, styleAttr := "color: #7f8c8d; font-size: 1rem; margin-bottom: 8px; font-weight: 500;"),
                  
                  div(
                    styleAttr := "display: flex; align-items: center; justify-content: center; gap: 8px; margin-bottom: 10px;",
                    span("Precio:", styleAttr := "color: #636e72; font-size: 0.98rem;"),
                    // Estilo de precio unificado (naranja)
                    span(f"S/.${libro.precio}%.2f", styleAttr := "color: #e67e22; font-size: 1.2rem; font-weight: bold;")
                  ),
                  
                  button(
                    "Ver detalles",
                    styleAttr := """
                      background-color: #e67e22; 
                      color: white;
                      border: none;
                      border-radius: 8px;
                      padding: 10px 22px;
                      font-size: 1rem;
                      cursor: pointer;
                      font-weight: 600;
                      margin-top: 10px;
                      letter-spacing: 0.5px;
                      box-shadow: 0 4px 6px rgba(230, 126, 34, 0.4); 
                      transition: background 0.2s, transform 0.1s ease;
                    """,
                    // FIX 2: Missing Parameter Type (Se usa el parámetro explícito 'ev')
                    onMouseOver --> { (ev: dom.MouseEvent) =>
                      ev.target.asInstanceOf[dom.html.Button].style.background = "#d35400"
                      ev.target.asInstanceOf[dom.html.Button].style.transform = "translateY(-1px)"
                    },
                    // FIX 2: Missing Parameter Type (Se usa el parámetro explícito 'ev')
                    onMouseOut --> { (ev: dom.MouseEvent) =>
                      ev.target.asInstanceOf[dom.html.Button].style.background = "#e67e22"
                      ev.target.asInstanceOf[dom.html.Button].style.transform = "translateY(0)"
                    },
                    onClick --> { _ => currentView.set(ProductView(currentView, libro, personaVar, logout)) }
                  )
                )
              }
            )
          }
        }
      )
    )
  }
}