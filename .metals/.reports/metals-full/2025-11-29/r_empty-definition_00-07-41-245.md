error id: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/AnadirAdminView.scala:local37
file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/AnadirAdminView.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -com/raquo/laminar/api/L.e.target.asInstanceOf.
	 -com/raquo/laminar/api/L.e.target.asInstanceOf#
	 -com/raquo/laminar/api/L.e.target.asInstanceOf().
	 -e/target/asInstanceOf.
	 -e/target/asInstanceOf#
	 -e/target/asInstanceOf().
	 -scala/Predef.e.target.asInstanceOf.
	 -scala/Predef.e.target.asInstanceOf#
	 -scala/Predef.e.target.asInstanceOf().
offset: 8421
uri: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/AnadirAdminView.scala
text:
```scala
package app.views

import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object AnadirAdminView {
  def apply(
    currentView: Var[HtmlElement],
    librosVar: Var[List[PaginaPrincipal.Libro]] // por consistencia
  ): HtmlElement = {

    val nombreVar = Var("")
    val dniVar = Var("")
    val correoVar = Var("")
    val contrasenaVar = Var("")

    def registrarAdmin(): Unit = {
      val data = js.Dynamic.literal(
        nombre = nombreVar.now(),
        dni = dniVar.now(),
        correo = correoVar.now(),
        contrasena = contrasenaVar.now()
      )

      Ajax
        .post(
          url = "https://tl7vhlzb-8081.brs.devtunnels.ms/admin/registraradmin",
          data = JSON.stringify(data),
          headers = Map("Content-Type" -> "application/json")
        )
        .map { xhr =>
          if (xhr.status == 200) {
            dom.window.alert("Registro de admin exitoso")
            currentView.set(AdminView(currentView, librosVar))
          } else {
            dom.window.alert(s"Error al registrar admin: ${xhr.status}")
          }
        }
        .recover { case ex =>
          dom.window.alert("Error de red: " + ex.getMessage)
        }
    }

    div(
      // Overlay oscuro de fondo
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
        z-index: 1000;
        font-family: 'Roboto', sans-serif;
      """,
      
      // Cerrar al hacer clic fuera del modal
      onClick --> { _ => currentView.set(AdminView(currentView, librosVar)) },

      // Modal card
      div(
        styleAttr := """
          background-color: white;
          padding: 30px 35px;
          border-radius: 15px;
          width: 500px;
          max-width: 90%;
          max-height: 90vh;
          overflow-y: auto;
          display: flex;
          flex-direction: column;
          gap: 18px;
          box-shadow: 0 10px 40px rgba(0,0,0,0.3);
          position: relative;
        """,
        
        // Prevenir que el clic en el modal cierre el overlay
        onClick.stopPropagation --> { _ => () },

        // ---------- HEADER con X ----------
        div(
          styleAttr := "display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;",
          
          h2(
            child <-- Signal.fromValue("Agregar ").combineWith(Signal.fromValue("administrador")).map { case (a, b) =>
              span(
                span(a, styleAttr := "color: #2c3e50;"),
                span(b, styleAttr := "color: #ff6b35;")
              )
            },
            styleAttr := """
              font-size: 1.5em;
              font-weight: 700;
              margin: 0;
            """
          ),
          
          // Botón X para cerrar
          button(
            "×",
            styleAttr := """
              background: transparent;
              border: none;
              font-size: 2em;
              color: #999;
              cursor: pointer;
              padding: 0;
              width: 30px;
              height: 30px;
              display: flex;
              align-items: center;
              justify-content: center;
              transition: color 0.2s;
            """,
            onClick --> { _ => currentView.set(AdminView(currentView, librosVar)) },
            onMouseOver --> { e =>
              e.target.asInstanceOf[dom.html.Element].style.color = "#333"
            },
            onMouseOut --> { e =>
              e.target.asInstanceOf[dom.html.Element].style.color = "#999"
            }
          )
        ),

        // Texto descriptivo
        p(
          "Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos.",
          styleAttr := """
            color: #7f8c8d;
            font-size: 0.9em;
            margin: 0 0 10px 0;
            line-height: 1.5;
          """
        ),

        // ---------- Nombres ----------
        label("Nombres", styleAttr := "font-weight: 600; color: #333; font-size: 0.95em; margin-bottom: -10px;"),
        input(
          placeholder := "Ingresa nombre completo",
          styleAttr := """
            padding: 12px 15px;
            font-size: 0.95em;
            border: 1px solid #ddd;
            border-radius: 8px;
            width: 100%;
            transition: border 0.2s;
            box-sizing: border-box;
          """,
          onInput.mapToValue --> nombreVar.writer,
          onFocus --> { e =>
            e.target.asInstanceOf[dom.html.Element].style.border = "1px solid #ff6b35"
          },
          onBlur --> { e =>
            e.target.asInstanceOf[dom.html.Element].style.border = "1px solid #ddd"
          }
        ),

        // ---------- DNI ----------
        label("DNI", styleAttr := "font-weight: 600; color: #333; font-size: 0.95em; margin-bottom: -10px;"),
        input(
          placeholder := "Ingresar dni",
          styleAttr := """
            padding: 12px 15px;
            font-size: 0.95em;
            border: 1px solid #ddd;
            border-radius: 8px;
            width: 100%;
            box-sizing: border-box;
          """,
          onInput.mapToValue --> dniVar.writer
        ),

        // ---------- Correo electrónico ----------
        label("Correo electronico", styleAttr := "font-weight: 600; color: #333; font-size: 0.95em; margin-bottom: -10px;"),
        input(
          typ := "email",
          placeholder := "nombre@gmail.com",
          styleAttr := """
            padding: 12px 15px;
            font-size: 0.95em;
            border: 1px solid #ddd;
            border-radius: 8px;
            width: 100%;
            box-sizing: border-box;
          """,
          onInput.mapToValue --> correoVar.writer
        ),

        // ---------- Contraseña ----------
        label("Contraseña", styleAttr := "font-weight: 600; color: #333; font-size: 0.95em; margin-bottom: -10px;"),
        input(
          typ := "password",
          placeholder := "Agregar contraseña",
          styleAttr := """
            padding: 12px 15px;
            font-size: 0.95em;
            border: 1px solid #ddd;
            border-radius: 8px;
            width: 100%;
            box-sizing: border-box;
          """,
          onInput.mapToValue --> contrasenaVar.writer
        ),

        // ---------- BOTONES DE ACCIÓN ----------
        div(
          styleAttr := "display: flex; gap: 12px; margin-top: 15px;",

          // Botón Cancelar
          button(
            "Cancelar",
            styleAttr := """
              flex: 1;
              background: white;
              border: 1px solid #ddd;
              color: #666;
              padding: 12px;
              font-size: 0.95em;
              font-weight: 600;
              border-radius: 8px;
              cursor: pointer;
              transition: all 0.2s;
            """,
            onClick --> { _ => currentView.set(AdminView(currentView, librosVar)) },
            onMouseOver --> { e =>
              val btn = e.target.asInstanceOf[dom.html.Element]
              btn.style.background = "#f5f5f5"
            },
            onMouseOut --> { e =>
              val btn = e.target.asInstanceOf[dom.html.Element]
              btn.style.background = "white"
            }
          ),

          // Botón Agregar Admin
          button(
            "Agregar Admin",
            styleAttr := """
              flex: 1;
              background: #3B6B3C;
              border: none;
              color: white;
              padding: 12px;
              font-size: 0.95em;
              font-weight: 600;
              border-radius: 8px;
              cursor: pointer;
              transition: all 0.2s;
            """,
            onClick --> { _ => registrarAdmin() },
            onMouseOver --> { e =>
              val btn = e.target.asInstanc@@eOf[dom.html.Element]
              btn.style.background = "#3B6B3C"
            },
            onMouseOut --> { e =>
              val btn = e.target.asInstanceOf[dom.html.Element]
              btn.style.background = "#3B6B3C"
            }
          )
        )
      )
    )
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 