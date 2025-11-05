package app.views

import com.raquo.laminar.api.L._
import app.views.PaginaPrincipal.Libro
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object LoginView {
  def apply(
    currentView: Var[HtmlElement],
    librosVar: Var[List[Libro]]
  ): HtmlElement = {

    val usuarioVar = Var("")
    val contrasenaVar = Var("")

    // Objeto para guardar los datos de la persona logueada
    val personaVar = Var[Option[js.Dynamic]](None)

    // Función para hacer logout
    def logout(): Unit = {
      dom.window.localStorage.removeItem("rol")
      dom.window.localStorage.removeItem("id_persona")
      dom.window.localStorage.removeItem("email")
      dom.window.localStorage.removeItem("persona")
      personaVar.set(None)
      currentView.set(LoginView(currentView, librosVar))
    }

    // Función para hacer login
    def login(): Unit = {
      val correo = usuarioVar.now()
      val contrasena = contrasenaVar.now()

      if (correo == "admin" && contrasena == "123") {
        val persona = js.Dynamic.literal(
          rol = "admin",
          id_persona = 999,
          email = correo
        )
        dom.window.localStorage.setItem("rol", "admin")
        dom.window.localStorage.setItem("id_persona", "999")
        dom.window.localStorage.setItem("email", correo)
        dom.window.localStorage.setItem("persona", JSON.stringify(persona))
        personaVar.set(Some(persona))
        currentView.set(AdminView(currentView, librosVar))
      } else if (correo == "user" && contrasena == "123") {
        val persona = js.Dynamic.literal(
          rol = "cliente",
          id_persona = 111,
          email = correo
        )
        dom.window.localStorage.setItem("rol", "cliente")
        dom.window.localStorage.setItem("id_persona", "111")
        dom.window.localStorage.setItem("email", correo)
        dom.window.localStorage.setItem("persona", JSON.stringify(persona))
        personaVar.set(Some(persona))
        currentView.set(ClientView(currentView, librosVar, personaVar, logout))
      } else {
        val data = js.Dynamic.literal(
          correo = correo,
          contrasena = contrasena
        )

        val payload = JSON.stringify(data)
        val loginUrl = "https://tl7vhlzb-8081.brs.devtunnels.ms/login/login"
        val headers = Map("Content-Type" -> "application/json")

        // Debug logs: request payload and headers
        dom.console.log("Login - sending request to:", loginUrl)
        dom.console.log("Login - payload:", payload)
        dom.console.log("Login - headers:", headers.toString)

        Ajax
          .post(
            url = loginUrl,
            data = payload,
            headers = headers
          )
          .map { xhr =>
            // Always log response to help debug 400 errors
            dom.console.log("Login - response status:", xhr.status)
            dom.console.log("Login - responseText:", xhr.responseText)

            if (xhr.status == 200) {
              val response = JSON.parse(xhr.responseText)
              val rol = response.rol.toString
              val idPersona = response.id_persona.toString
              val email = response.email.toString

              // Guardar todos los datos de la persona
              dom.window.localStorage.setItem("rol", rol)
              dom.window.localStorage.setItem("id_persona", idPersona)
              dom.window.localStorage.setItem("email", email)
              dom.window.localStorage.setItem("persona", xhr.responseText)
              personaVar.set(Some(response))

              if (rol == "admin") {
                currentView.set(AdminView(currentView, librosVar))
              } else {
                // Cliente: ir a ClientView
                currentView.set(ClientView(currentView, librosVar, personaVar, logout))
              }
            } else {
              // Show server response when available to diagnose 400
              dom.console.error(s"Login failed: ${xhr.status} - ${xhr.responseText}")
              dom.window.alert(s"Error en login: ${xhr.status} - ${xhr.responseText}")
            }
          }
          .recover { case ex =>
            dom.console.error("Login request error:", ex)
            dom.window.alert("Error conectando con el servidor: " + ex.getMessage)
          }
      }
    }


    div(
      // Fondo estilo patrón
      styleAttr := """
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        background-image: url('/frontend/inicioSesion.png');
        background-size: 500px;
        background-repeat: repeat;
        background-position: center;
      """,

      // Tarjeta
      div(
        styleAttr := """
          background: white;
          padding: 45px 50px;
          border-radius: 25px;
          box-shadow: 0 15px 45px rgba(0,0,0,0.15);
          width: 380px;
          text-align: center;
        """,

        h2(
          "Iniciar Sesión",
          styleAttr := "color: #2e7d32; font-weight: 700; margin-bottom: 14px; font-size: 26px;"
        ),
        p(
          "Morbi enim elit, sagittis ac ultricies a, interdum efficitur mauris.",
          styleAttr := "color: #777; font-size: 14px; margin-bottom: 25px;"
        ),

        // Input correo
        input(
          placeholder := "Ingresa tu correo",
          onInput.mapToValue --> usuarioVar.writer,
          styleAttr :=
            """width: 100%;
               |padding: 14px;
               |border: none;
               |border-radius: 12px;
               |background: #f3f3f3;
               |font-size: 15px;
               |margin-bottom: 15px;
               |outline: none;
            """.stripMargin
        ),
        // Input contraseña
        input(
          typ := "password",
          placeholder := "Ingresa tu contraseña",
          onInput.mapToValue --> contrasenaVar.writer,
          styleAttr :=
            """width: 100%;
               |padding: 14px;
               |border: none;
               |border-radius: 12px;
               |background: #f3f3f3;
               |font-size: 15px;
               |margin-bottom: 20px;
               |outline: none;
            """.stripMargin
        ),

        // Botón iniciar sesión
        button(
          "INICIAR SESIÓN",
          onClick --> { _ => login() },
          styleAttr :=
            """width: 100%;
               |background: #2e7d32;
               |border: none;
               |color: white;
               |font-weight: bold;
               |padding: 13px;
               |border-radius: 30px;
               |cursor: pointer;
               |margin-bottom: 12px;
               |font-size: 15px;
               |transition: 0.3s;
            """.stripMargin,
          onMouseOver --> { e =>
            val btn = e.target.asInstanceOf[dom.html.Element]
            btn.style.backgroundColor = "#1b5e20"
            btn.style.color = "white"
          },
          onMouseOut --> { e =>
            val btn = e.target.asInstanceOf[dom.html.Element]
            btn.style.backgroundColor = "#2e7d32"
            btn.style.color = "white"
          }
        ),

        // Botón registrarse
        button(
          "REGÍSTRATE",
          onClick --> { _ => currentView.set(RegistroView(currentView, librosVar)) },
          styleAttr :=
            """width: 100%;
               |background: white;
               |border: 2px solid #ff6b00;
               |color: #ff6b00;
               |font-weight: bold;
               |padding: 13px;
               |border-radius: 30px;
               |cursor: pointer;
               |font-size: 15px;
               |transition: 0.3s;
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
        ),

        // Enlace atrás
        a(
          "← Atrás",
          href := "#",
          onClick.preventDefault --> { _ => currentView.set(PaginaPrincipal(currentView, librosVar)) },
          styleAttr := """
            display: block; 
            margin-top: 20px; 
            color: #7f8c8d; 
            text-decoration: none; 
            font-size: 14px; 
            text-align: left;
            """
        )
      )
    )
  }
}
