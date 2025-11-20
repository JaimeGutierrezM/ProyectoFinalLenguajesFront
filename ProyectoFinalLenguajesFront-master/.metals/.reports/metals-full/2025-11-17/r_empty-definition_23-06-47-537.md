error id: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/AdminView.scala:`<error>`#`<error>`.
file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/AdminView.scala
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
offset: 2719
uri: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/AdminView.scala
text:
```scala
package app.views

import app.views.{AgregarView, EliminarView, ReporteView, AnadirAdminView, PaginaPrincipal}
import com.raquo.laminar.api.L._
import org.scalajs.dom

object AdminView {

  def apply(
    currentView: Var[HtmlElement],
    librosVar: Var[List[PaginaPrincipal.Libro]]
  ): HtmlElement = {

    val adminEmail = dom.window.localStorage.getItem("email")

    // ------------------ TARJETAS DE ADMIN ------------------
    def adminCard(text: String, icon: String)(action: => Unit) = {
      val hover = Var(false)

      div(
        styleAttr <-- hover.signal.map { h =>
          s"""
            width: 240px;
            height: 170px;
            background: white;
            border-radius: 18px;
            box-shadow: ${if (h) "0 10px 22px rgba(0,0,0,0.15)" else "0 4px 12px rgba(0,0,0,0.10)"};
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            cursor: pointer;
            transition: all 0.2s ease;
            transform: ${if (h) "translateY(-5px)" else "translateY(0)"};
          """
        },
        onMouseOver --> (_ => hover.set(true)),
        onMouseOut  --> (_ => hover.set(false)),
        onClick --> (_ => action),

        i(
          className := s"fa-solid $icon",
          styleAttr := "font-size: 38px; color: #e67e22; margin-bottom: 15px;"
        ),

        span(
          text,
          styleAttr := "font-size: 1.1em; font-weight: 600; color: #333;"
        )
      )
    }

    // ------------------ DISEÑO GENERAL ------------------
    div(
      styleAttr := """
        width: 100%;
        min-height: 100vh;
        height: auto;
        display: flex;
        flex-direction: column;
        background-image: url('/frontend/inicioSesion.png');
        background-size: 500px;
        background-repeat: repeat;
        background-position: center;
        font-family: 'Roboto', sans-serif;
      """,

      // ------------------ BARRA SUPERIOR ------------------
    div(
      styleAttr := """
        width: 100%;
        height: 70px;
        background: #e67e22;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 35px;
        color: white;
        font-size: 1.3em;
        font-weight: 600;
        box-sizing: border-box;
        overflow: hidden;
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        z-index: 1000;
      """,
      span(s"Usuario: $adminEmail"),

      {
        val hoverLogout = Var(false)

        button(
          "CERRAR SESIÓN",
          @@styleAttr <-- hoverLogout.signal.map { h =>
            s"""
              padding: 10px 20px;
              background: ${if (h) "#d35400" else "#e67e22"};
              color: white;
              border: 2px solid white;
              border-radius: 30px;
              cursor: pointer;
              font-size: 0.85em;
              transition: background-color .2s ease;
            """
          },
          onMouseOver --> (_ => hoverLogout.set(true)),
          onMouseOut  --> (_ => hoverLogout.set(false)),
          onClick --> { _ =>
            dom.window.localStorage.clear()
            currentView.set(PaginaPrincipal(currentView, librosVar))
          }
        )
      }
    ),


      // ------------------ BANNER SUPERIOR ------------------
      div(
        styleAttr := """
          width: 100%;
          min-height: 100vh;
          height: auto;
          display: flex;
          justify-content: center;
          align-items: center;
          color: #333;
          font-size: 2.2em;
          font-weight: 700;
        """,
        div(
          div("¡Bienvenido Administrador!,", styleAttr := "color:#2c3e50;"),
          div(adminEmail, styleAttr := "color:#27ae60; font-size:0.8em; margin-top:10px;")
        )
      ),

      // ------------------ CONTENEDOR DE TARJETAS ------------------
      div(
        styleAttr := """
          width: 100%;
          display: flex;
          justify-content: center;
          margin-top: -60px;
        """,

        div(
          styleAttr := """
            width: 85%;
            background: white;
            padding: 40px 30px;
            border-radius: 20px;
            box-shadow: 0 6px 18px rgba(0,0,0,0.12);
            display: flex;
            justify-content: space-around;
            flex-wrap: wrap;
            gap: 35px;
          """,

          adminCard("Agregar Producto", "fa-plus") {
            currentView.set(AgregarView(currentView, librosVar))
          },

          adminCard("Eliminar Producto", "fa-trash") {
            currentView.set(EliminarView(currentView, Var(List.empty)))
          },

          adminCard("Informes", "fa-database") {
            currentView.set(ReporteView(currentView, librosVar))
          },

          adminCard("Agregar Administrador", "fa-user-plus") {
            currentView.set(AnadirAdminView(currentView, librosVar))
          }
        )
      )
    )
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 