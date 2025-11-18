error id: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/PaginaPrincipal.scala:app/views/FiltroView.
file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/PaginaPrincipal.scala
empty definition using pc, found symbol in pc: app/views/FiltroView.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -com/raquo/laminar/api/L.FiltroView.
	 -com/raquo/laminar/api/L.FiltroView#
	 -com/raquo/laminar/api/L.FiltroView().
	 -app/views/FiltroView.
	 -app/views/FiltroView#
	 -app/views/FiltroView().
	 -FiltroView.
	 -FiltroView#
	 -FiltroView().
	 -scala/Predef.FiltroView.
	 -scala/Predef.FiltroView#
	 -scala/Predef.FiltroView().
offset: 4488
uri: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/PaginaPrincipal.scala
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

object PaginaPrincipal {

  case class Libro(
    id_libro: Int,
    nombre: String,
    precio: Double,
    id_categoria: String,
    nombre_categoria: String,
    nombrepdf: String,
    nombreimagen: String
  )

  def apply(currentView: Var[HtmlElement], librosVar: Var[List[Libro]]): HtmlElement = {

    div(
      // ---------- HERO (fondo compartido + cabecera + slogan) ----------
      div(
        // contenedor con fondo (imagen local)
        styleAttr := """
          background-image: url('/frontend/src/main/scala/app/views/Rectangle_1.png');
          background-size: cover;
          background-position: center;
          background-repeat: no-repeat;
          color: white;
          padding-bottom: 120px;
          position: relative;
          overflow: hidden;
        """,

        // capa oscura translúcida para contraste
        div(
          styleAttr := """
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(244, 234, 234, 0.1);
            z-index: 0;
          """
        ),

        // CABECERA flotante (sobre la imagen)
        div(
          styleAttr := """
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 20px 50px;
            position: relative;
            z-index: 2;
          """,

          // Lado izquierdo: logo / nombre
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

          // Lado derecho: botones
          div(
            styleAttr := "display: flex; gap: 14px;",
            button(
              "REGÍSTRATE",
              title := "Registrarse",
              styleAttr := """
                background: transparent;
                border: 2px solid #2ecc71;
                color: #2ecc71;
                border-radius: 50px;
                padding: 8px 20px;
                font-size: 1rem;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.3s ease;
              """,
              onClick --> { _ =>
                currentView.set(RegistroView(currentView, librosVar))
              }
            ),
            button(
              "INICIAR SESIÓN",
              title := "Iniciar sesión",
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
                currentView.set(LoginView(currentView, librosVar))
              }
            )
          )
        ),

        // TEXTO central sobre el fondo (slogan)
        div(
          styleAttr := """
            text-align: center;
            margin-top: 120px;
            position: relative;
            z-index: 2;
            max-width: 900px;
            margin-left: auto;
            margin-right: auto;
            padding: 0 20px;
          """,
          h1(
            "Descubre nuestras recetas",
          styleAttr := """
            font-size: 2.2rem; 
              color: #2f3640; 
              margin-bottom: 6px; 
              font-family: 'Poppins',
              sans-serif; font-weight: 700;
            """
          ),
          h2(
            "más solicitadas",
            styleAttr := """"
              font-size: 1.8rem; 
              color: #00572b; 
              margin-top: 0; 
              font-family: 'Poppins', sans-serif; 
              font-weight: 700;
            """
          )
        )
      ), // <- cierra hero div

      // ---------- FILTRO DE CATEGORÍAS ----------
      Fi@@ltroView(currentView, librosVar),
      // ---------- TARJETAS DE LIBROS ----------
      div(
        styleAttr := """
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
          gap: 32px;
          justify-items: center;
          padding: 30px 20px 60px;
          background: #fff;
          border-radius: 15px;
          margin: 0 15px;
          transition: all 0.4s ease;
        """,
        children <-- librosVar.signal.split(_.id_libro) { (_, libro, _) =>
          div(
            styleAttr := """
              width: 250px;
              border: 1px solid #eee;
              border-radius: 18px;
              padding: 18px 14px;
              text-align: center;
              background-color: #fff;
              box-shadow: 0 3px 12px rgba(0,0,0,0.08);
              transition: transform 0.3s, box-shadow 0.3s;
              cursor: pointer;
              position: relative;
              overflow: hidden;
              animation: fadeIn 0.7s ease;
            """,
            onMouseEnter --> { _ => /* opcional: animación hover */ },
            img(
              src := s"https://tl7vhlzb-8081.brs.devtunnels.ms/frontimg/imagen/${libro.nombreimagen}",
              alt := libro.nombre,
              width := "170",
              height := "240",
              styleAttr := """
                border-radius: 12px;
                margin-bottom: 14px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                object-fit: cover;
                object-position: top center;
                max-width: 100%;
                background: #f1f2f6;
              """
            ),
            h4(libro.nombre, styleAttr := "font-size: 1.1rem; color: #2f3640; margin: 12px 0 6px 0; font-weight: 700;"),
            p(libro.nombre_categoria, styleAttr := "color: #636e72; font-size: 0.95rem; margin-bottom: 8px; font-weight: 500;"),
            div(
              styleAttr := "display: flex; align-items: center; justify-content: center; gap: 8px; margin-bottom: 10px;",
              span("Precio:", styleAttr := "color: #777; font-size: 0.95rem;"),
              span(f"S/ ${libro.precio}%.2f", styleAttr := "color: #ff914d; font-size: 1.15rem; font-weight: bold;")
            )
          )
        }
      ),

      // ---------- FOOTER ----------
      div(
        styleAttr := """
          background: linear-gradient(90deg, #ff914d 0%, #ffb84d 100%);
          color: white;
          text-align: center;
          padding: 20px 0;
          font-family: 'Poppins', sans-serif;
        """,
        p("RecetApp © 2025 - Todos los derechos reservados", styleAttr := "font-size: 0.95rem; margin: 0;")
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

empty definition using pc, found symbol in pc: app/views/FiltroView.