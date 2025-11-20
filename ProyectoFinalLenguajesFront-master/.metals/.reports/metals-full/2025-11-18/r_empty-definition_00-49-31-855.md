error id: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/ClientView.scala:`<error>`#`<error>`.
file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/ClientView.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -com/raquo/laminar/api/L.styleAttr.
	 -com/raquo/laminar/api/L.styleAttr#
	 -com/raquo/laminar/api/L.styleAttr().
	 -styleAttr.
	 -styleAttr#
	 -styleAttr().
	 -scala/Predef.styleAttr.
	 -scala/Predef.styleAttr#
	 -scala/Predef.styleAttr().
offset: 886
uri: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/ClientView.scala
text:
```scala
package app.views

import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import app.views.FiltroView

object ClientView {

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
      // BARRA SUPERIOR NARANJA
      // ============================
      div(
        @@styleAttr := """
          background-image: url('/frontend/src/main/scala/app/views/Rectangle_1.png');
          background-size: cover;
          background-position: center;
          background-repeat: repeat;
          color: white;
          padding-bottom: 120px;
          position: relative;
          overflow: hidden;
        """,

        // TÍTULO IZQUIERDA (“RECET APP”)
        span(
          "RECET APP",
          styleAttr := """
            font-size: 20px;
            font-weight: bold;
          """
        ),

        // BOTÓN CERRAR SESIÓN
        button(
          div(
            span("X ", styleAttr := "font-weight: bold; padding-right: 4px;"),
            span("CERRAR SESIÓN")
          ),
          onClick --> (_ => logout()),
          styleAttr := """
            background: white;
            color: #e67e22;
            border: none;
            padding: 10px 25px;
            border-radius: 40px;
            font-size: 14px;
            cursor: pointer;
            display: flex;
            align-items: center;
            font-weight: bold;
          """
        )
      ),

      // =======================================
      // TEXTO CENTRAL DE BIENVENIDA
      // =======================================
      div(
        styleAttr := """
          width: 100%;
          background-color: #e67e22;
          color: white;
          padding: 40px 0px 70px 0px;
          text-align: center;
          font-size: 24px;
          margin-top: -1px;
        """,

        child.text <-- personaVar.signal.map { p =>
          val nombre = p.flatMap(pp => Option(pp.nombre)).map(_.toString).getOrElse("USUARIO")
          s"¡BIENVENIDO!\n$nombre"
        }
      ),
      FiltroView(currentView, librosVar),
      // ==================================================
      // LISTA DE LIBROS
      // ==================================================
      div(
        styleAttr := """
          flex: 1;
          padding: 35px;
          overflow-y: auto;
          margin-top: 20px;
        """,

        child <-- librosVar.signal.map { lista =>
          if (lista.isEmpty)
            div(
              "No hay libros disponibles.",
              styleAttr := "text-align:center; margin-top:80px; font-size:20px; color:#444;"
            )
          else
            div(
              styleAttr := """
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
                gap: 30px;
                padding-bottom: 40px;
              """,
              lista.map { libro =>
                div(
                  styleAttr := """
                    border-radius: 16px;
                    padding: 18px;
                    background: white;
                    text-align: center;
                    box-shadow: 0 3px 10px rgba(0,0,0,0.15);
                    transition: transform 0.2s ease;
                  """,
                  onMouseOver --> { e => e.target.asInstanceOf[dom.html.Element].style.transform = "scale(1.03)" },
                  onMouseOut -->  { e => e.target.asInstanceOf[dom.html.Element].style.transform = "scale(1)" },

                  img(
                    src := s"https://tl7vhlzb-8081.brs.devtunnels.ms/frontimg/imagen/${libro.nombreimagen}",
                    styleAttr := """
                      width: 180px;
                      height: 240px;
                      object-fit: cover;
                      border-radius: 12px;
                      margin-bottom: 12px;
                    """
                  ),

                  h4(
                    libro.nombre,
                    styleAttr := "font-size: 18px; margin: 10px 0 4px 0;"
                  ),

                  p(libro.nombre_categoria, styleAttr := "color: #7f8c8d; margin-bottom: 8px;"),

                  strong(f"S/. ${libro.precio}%.2f"),

                  button(
                    "Ver detalles",
                    onClick --> (_ => currentView.set(ProductView(currentView, libro, personaVar, logout))),
                    styleAttr := """
                      margin-top: 15px;
                      padding: 10px 20px;
                      background-color: #e67e22;
                      border: none;
                      color: white;
                      border-radius: 10px;
                      cursor: pointer;
                      font-weight: bold;
                      transition: background 0.2s;
                    """
                  )
                )
              }
            )
        }
      )
    )
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 