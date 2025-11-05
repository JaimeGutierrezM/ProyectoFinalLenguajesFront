package app.views

import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object RegistroView {
  def apply(currentView: Var[HtmlElement], librosVar: Var[List[PaginaPrincipal.Libro]]): HtmlElement = {

    val nombreVar = Var("")
    val dniVar = Var("")
    val correoVar = Var("")
    val contrasenaVar = Var("")
    val mensajeVar = Var(Option.empty[String])

    def registrarCliente(): Unit = {
      val data = js.Dynamic.literal(
        nombre = nombreVar.now(),
        dni = dniVar.now(),
        correo = correoVar.now(),
        contrasena = contrasenaVar.now()
      )
      Ajax
        .post(
          url = "https://tl7vhlzb-8081.brs.devtunnels.ms/persona/registrarcliente",
          data = JSON.stringify(data),
          headers = Map("Content-Type" -> "application/json")
        )
        .map { xhr =>
          if (xhr.status == 200) {
            mensajeVar.set(Some("Registro válido. ¡Bienvenido!"))
            js.timers.setTimeout(1800) {
              currentView.set(LoginView(currentView, librosVar))
            }
          } else {
            mensajeVar.set(Some(s"Error al registrar: ${xhr.status}"))
          }
        }
        .recover { case ex =>
          mensajeVar.set(Some("Error de red: " + ex.getMessage))
        }
    }

    div(
      // Fondo
      styleAttr := """
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        background-image: url("/frontend/registro.png");
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
      """,

      // Tarjeta del formulario
      div(
        styleAttr := """
          background: white;
          padding: 40px 50px;
          border-radius: 20px;
          box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
          width: 350px;
          text-align: center;
        """,

        h2(
          "Regístrate",
          styleAttr := "color: #ff6b00; font-weight: 700; margin-bottom: 10px; font-size: 24px;"
        ),

        input(
          typ := "text",
          placeholder := "Nombre Completo",
          onInput.mapToValue --> nombreVar.writer,
          styleAttr :=
            """width: 100%;
               |padding: 12px;
               |border: none;
               |border-radius: 10px;
               |background: #f3f3f3;
               |font-size: 14px;
               |margin-bottom: 15px;
               |outline: none;
            """.stripMargin
        ),
        input(
          typ := "text",
          placeholder := "Ingresa tu DNI",
          onInput.mapToValue --> dniVar.writer,
          styleAttr :=
            """width: 100%;
               |padding: 12px;
               |border: none;
               |border-radius: 10px;
               |background: #f3f3f3;
               |font-size: 14px;
               |margin-bottom: 15px;
               |outline: none;
            """.stripMargin
        ),
        input(
          typ := "email",
          placeholder := "Ingresa tu correo",
          onInput.mapToValue --> correoVar.writer,
          styleAttr :=
            """width: 100%;
               |padding: 12px;
               |border: none;
               |border-radius: 10px;
               |background: #f3f3f3;
               |font-size: 14px;
               |margin-bottom: 15px;
               |outline: none;
            """.stripMargin
        ),
        input(
          typ := "password",
          placeholder := "Ingresa tu contraseña",
          onInput.mapToValue --> contrasenaVar.writer,
          styleAttr :=
            """width: 100%;
               |padding: 12px;
               |border: none;
               |border-radius: 10px;
               |background: #f3f3f3;
               |font-size: 14px;
               |margin-bottom: 20px;
               |outline: none;
            """.stripMargin
        ),

        button(
          "REGISTRATE",
          onClick.preventDefault --> { _ => registrarCliente() },
          styleAttr :=
            """width: 100%;
               |background: white;
               |border: 2px solid #ff6b00;
               |color: #ff6b00;
               |font-weight: bold;
               |padding: 12px;
               |border-radius: 30px;
               |cursor: pointer;
               |transition: 0.3s;
            """.stripMargin,
          onMouseOver --> { e =>
            val btn = e.target.asInstanceOf[dom.html.Element]
            btn.style.backgroundColor = "#ff6b00"
            btn.style.color = "white"
          },
          onMouseOut --> { e =>
            val btn = e.target.asInstanceOf[dom.html.Element]
            btn.style.backgroundColor = "white"
            btn.style.color = "#ff6b00"
          }
        ),

        child.maybe <-- mensajeVar.signal.map(_.map(msg =>
          div(msg, styleAttr := "color: #00b894; margin-top: 10px; font-weight: 500; font-size: 14px;")
        )),

        a(
          "← Atrás",
          href := "#",
          onClick.preventDefault --> { _ =>
            currentView.set(PaginaPrincipal(currentView, librosVar))
          },
          styleAttr := "display: block; margin-top: 20px; color: #7f8c8d; text-decoration: none; font-size: 14px; text-align: left;"
        )
      )
    )
  }
}
