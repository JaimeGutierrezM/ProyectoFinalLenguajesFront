error id: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/AdminView.scala:app/views/`<error: <none>>`.
file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/AdminView.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -com/raquo/laminar/api/L.com.raquo.
	 -com/raquo.
	 -scala/Predef.com.raquo.
offset: 125
uri: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/AdminView.scala
text:
```scala
package app.views

import app.views.{AgregarView, EliminarView, ReporteView, AnadirAdminView, PaginaPrincipal}
import com.@@raquo.laminar.api.L._
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
        height: 100px;
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
      div(
            styleAttr := "display: flex; align-items: center; gap: 10px;",
            h2(
              "RecetApp",
              styleAttr := """
                font-size: 1.8rem;
                font-weight: 700;
                color: #ffffffff;
                margin: 0;
                font-family: 'Poppins', sans-serif;
                text-shadow: 2px 2px 6px rgba(0,0,0,0.4);
              """
            )
          ),

      {
        val hoverLogout = Var(false)

        button(
          "CERRAR SESIÓN",
          onClick --> { _ => 
            currentView.set(RegistroView(currentView, librosVar))
          },
          styleAttr :=
            """background: white;
border: 2px solid #ff6b00;
color: #ff6b00;
font-weight: bold;
padding: 10px 20px;
border-radius: 30px;
cursor: pointer;
font-size: 0.85em;
transition: 0.3s ease;
            """.stripMargin,
          
          onMouseOver --> { e =>
            val btn = e.target.asInstanceOf[dom.html.Element]
            btn.style.backgroundColor = "#ff6b00"
            btn.style.color = "white"
            btn.style.border = "2px solid #ff6b00"
          },

          onMouseOut --> { e =>
            val btn = e.target.asInstanceOf[dom.html.Element]
            btn.style.backgroundColor = "white"
            btn.style.color = "#ff6b00"
            btn.style.border = "2px solid #ff6b00"
          }
        )
      }
    ),

      // ------------------ BANNER SUPERIOR ------------------
    div(
      styleAttr := """
        width: 100%;
        height: 180px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        text-align: center;
        padding: 80px 10px;
        font-family: 'Roboto', sans-serif;
      """,

      // Línea de saludo
      div(
        "¡Bienvenido Administrador!",
        styleAttr := """
          font-size: 2.4em;
          font-weight: 800;
          color: #2c3e50;
          letter-spacing: 1px;
          text-shadow: 1px 1px 2px rgba(0,0,0,0.1);
        """
      ),

      // Línea de email
      div(
        adminEmail,
        styleAttr := """
          margin-top: 8px;
          font-size: 2.2em;
          font-weight: 600;
          color: #27ae60;
          text-shadow: 0px 0px 2px rgba(0,0,0,0.05);
        """
      ),

      // Línea decorativa inferior
      div(
        styleAttr := """
          margin-top: 18px;
          width: 40%;
          height: 4px;
          background: #e67e22;
          border-radius: 10px;
        """
      )
    ),

      // ------------------ CONTENEDOR DE TARJETAS ------------------
      div(
        styleAttr := """
          className: "fade-in";
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
      ),
      // ---------- ANIMACIONES CSS INYECTADAS ----------
      onMountCallback { _ =>
        val style = dom.document.createElement("style")
        style.innerHTML =
          """
          @keyframes fadeIn {
            from { opacity: 0; transform: translateY(15px); }
            to { opacity: 1; transform: translateY(0); }
          }
          """
        dom.document.head.appendChild(style)
      }
    )
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 