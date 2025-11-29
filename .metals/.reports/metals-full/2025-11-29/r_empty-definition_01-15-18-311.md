error id: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/CompraView.scala:
file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/CompraView.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -com/raquo/laminar/api/L.cvv_numero.
	 -com/raquo/laminar/api/L.cvv_numero#
	 -com/raquo/laminar/api/L.cvv_numero().
	 -cvv_numero.
	 -cvv_numero#
	 -cvv_numero().
	 -scala/Predef.cvv_numero.
	 -scala/Predef.cvv_numero#
	 -scala/Predef.cvv_numero().
offset: 1137
uri: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/CompraView.scala
text:
```scala
package app.views

import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object CompraView {
  def apply(
    currentView: Var[HtmlElement],
    libro: PaginaPrincipal.Libro,
    personaVar: Var[Option[js.Dynamic]],
    logout: () => Unit
  ): HtmlElement = {

    val numeroTarjetaVar = Var("")
    val cvvVar = Var("")
    val mensajeVar = Var(Option.empty[String])
    val nombrePDFVar = Var(Option.empty[String]) // << NUEVO

    def comprar(): Unit = {
      val idLibro = libro.id_libro
      val idPersona = personaVar.now().map(p => p.id_persona.toString).getOrElse("")
      val numero = numeroTarjetaVar.now()
      val cvv = cvvVar.now()

      if (numero.length < 16 || cvv.length < 3) {
        mensajeVar.set(Some("Por favor, introduce un número de tarjeta y CVV válidos."))
        return
      }

      val data = js.Dynamic.literal(
        id_libro = idLibro,
        id_persona = idPersona,
        numero = numero,
        cv@@v_numero = cvv
      )

      mensajeVar.set(Some("Procesando compra..."))

      Ajax
        .post(
          url = "https://tl7vhlzb-8081.brs.devtunnels.ms/comprarlibro/comprar",
          data = JSON.stringify(data),
          headers = Map("Content-Type" -> "application/json")
        )
        .map { xhr =>
          if (xhr.status == 200) {
            val response = JSON.parse(xhr.responseText)

            mensajeVar.set(Some(s"¡Compra exitosa! ${response.ok.toString}"))

            // Si el backend devuelve nombrePDF, iniciar descarga
            if (js.typeOf(response.nombrePDF) != "undefined") {
              val nombrePDF = response.nombrePDF.toString
              nombrePDFVar.set(Some(nombrePDF))

              val enlaceDescarga =
                s"https://tl7vhlzb-8081.brs.devtunnels.ms/comprarlibro/descargar/$nombrePDF"

              // Abrir en nueva pestaña
              dom.window.open(enlaceDescarga, "_blank")
            }

          } else {
            mensajeVar.set(Some(s"Error en la compra (código ${xhr.status}). Por favor, revisa tus datos."))
          }
        }
        .recover { case ex =>
          mensajeVar.set(Some("Error conectando con el servidor: " + ex.getMessage))
        }
    }

    val navButtonStyle = """
      background-color: #e67e22;
      color: white;
      border: none;
      border-radius: 6px;
      padding: 8px 16px;
      font-size: 0.95rem;
      cursor: pointer;
      font-weight: 600;
      margin: 10px;
      transition: background-color 0.2s, transform 0.1s;
      box-shadow: 0 2px 4px rgba(0,0,0,0.2);
    """

    val navButtonHover =
      onMouseOver --> { _.target.asInstanceOf[dom.html.Button].style.backgroundColor = "#d35400" }
    val navButtonOut =
      onMouseOut --> { _.target.asInstanceOf[dom.html.Button].style.backgroundColor = "#e67e22" }

    div(
      styleAttr := """
        min-height: 100vh;
        background-image: url('/frontend/inicioSesion.png');
        background-size: cover;
        background-position: center;
        background-repeat: no-repeat;
        background-attachment: fixed;
        padding: 20px;
        display: flex;
        flex-direction: column;
        align-items: center;
      """,

      div(
        styleAttr := "width: 100%; max-width: 900px; display: flex; justify-content: space-between; margin-bottom: 20px;",

        button(
          "Volver",
          styleAttr := navButtonStyle,
          navButtonHover,
          navButtonOut,
          onClick --> { _ =>
            currentView.set(ProductView(currentView, libro, personaVar, logout))
          }
        ),

        button(
          "Cerrar sesión",
          styleAttr := navButtonStyle,
          navButtonHover,
          navButtonOut,
          onClick --> { _ => logout() }
        )
      ),

      div(
        styleAttr := "display: flex; flex-direction: column; align-items: center; background: rgba(255, 255, 255, 0.95); border-radius: 16px; box-shadow: 0 8px 30px rgba(0,0,0,0.25); padding: 40px; max-width: 450px; width: 90%; margin: 20px auto; backdrop-filter: blur(5px);",

        h1("Finalizar Compra", styleAttr := "font-size: 1.8rem; color: #333; margin-bottom: 25px; font-weight: 800;"),

        div(
          styleAttr := "display: flex; gap: 20px; align-items: center; margin-bottom: 30px; padding: 15px; border-bottom: 2px solid #f1f2f6; width: 100%;",

          img(
            src := s"https://tl7vhlzb-8081.brs.devtunnels.ms/frontimg/imagen/${libro.nombreimagen}",
            alt := libro.nombre,
            width := "80",
            height := "120",
            styleAttr := "border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);"
          ),

          div(
            h3(libro.nombre, styleAttr := "font-size: 1.2rem; color: #333; margin: 0; font-weight: 700;"),
            div(libro.nombre_categoria, styleAttr := "color: #7f8c8d; font-size: 0.9rem; margin-top: 4px;"),
            div(
              styleAttr := "display: flex; align-items: baseline; gap: 4px; margin-top: 10px;",
              b("Total:"),
              span(f"S/.${libro.precio}%.2f",
                styleAttr := "color: #e67e22; font-size: 1.4rem; font-weight: bold;"
              )
            )
          )
        ),

        form(
          styleAttr := "width: 100%;",
          onSubmit.preventDefault.mapTo(()) --> { _ => comprar() },

          div(
            label("Número de tarjeta:", styleAttr := "display: block; margin-bottom: 8px; font-weight: 600; color: #555;"),
            input(
              typ := "text",
              placeholder := "XXXX XXXX XXXX XXXX",
              styleAttr := "margin-bottom: 20px; padding: 12px; border-radius: 8px; border: 1px solid #ced6e0; width: 100%; font-size: 1rem;",
              onInput.mapToValue --> numeroTarjetaVar.writer,
              value <-- numeroTarjetaVar.signal
            )
          ),

          div(
            styleAttr := "display: flex; justify-content: flex-start; margin-bottom: 30px;",
            div(
              styleAttr := "width: 150px;",
              label("CVV:", styleAttr := "display: block; margin-bottom: 8px; font-weight: 600; color: #555;"),
              input(
                typ := "password",
                placeholder := "CVV",
                maxLength := 4,
                styleAttr := "padding: 12px; border-radius: 8px; border: 1px solid #ced6e0; width: 100%; font-size: 1rem;",
                onInput.mapToValue --> cvvVar.writer,
                value <-- cvvVar.signal
              )
            )
          ),

          button(
            "Confirmar Pago",
            typ := "submit",
            styleAttr := """
              background-color: #2e7d32;
              color: white;
              border: none;
              border-radius: 10px;
              padding: 15px 40px;
              font-size: 1.15rem;
              cursor: pointer;
              font-weight: 700;
              margin-top: 10px;
              width: 100%;
              box-shadow: 0 4px 10px rgba(46, 125, 50, 0.5);
              transition: all 0.2s ease;
            """,
            onMouseOver --> { _.target.asInstanceOf[dom.html.Button].style.backgroundColor = "#1b5e20" },
            onMouseOut --> { _.target.asInstanceOf[dom.html.Button].style.backgroundColor = "#2e7d32" }
          )
        ),

        child.maybe <-- mensajeVar.signal.map(_.map { msg =>
          val isError = msg.toLowerCase().contains("error") || msg.toLowerCase().contains("inválidos")
          div(
            msg,
            styleAttr := s"margin-top: 25px; padding: 15px; border-radius: 8px; font-size: 1rem; font-weight: 600; width: 100%; text-align: center; background-color: ${if (isError) "#ffeaa7" else "#d0f0c0"}; color: ${if (isError) "#d35400" else "#2e7d32"};"
          )
        }),

        // BOTÓN DE DESCARGA (SI SE RECIBIÓ nombrePDF)
        child.maybe <-- nombrePDFVar.signal.map {
          case Some(nombrePDF) =>
            Some(
              a(
                href := s"https://tl7vhlzb-8081.brs.devtunnels.ms/comprarlibro/descargar/$nombrePDF",
                target := "_blank",
                download := nombrePDF,
                "Descargar comprobante (PDF)",
                styleAttr := """
                  margin-top: 20px;
                  background-color: #3498db;
                  color: white;
                  padding: 12px 20px;
                  border-radius: 8px;
                  font-weight: 700;
                  text-decoration: none;
                  display: inline-block;
                """
              )
            )
          case None => None
        }
      )
    )
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 