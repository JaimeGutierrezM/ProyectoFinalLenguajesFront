error id: file:///D:/ProyectoFinalLenguajesFront-main/frontend/src/main/scala/app/views/PaginaPrincipal.scala:
file:///D:/ProyectoFinalLenguajesFront-main/frontend/src/main/scala/app/views/PaginaPrincipal.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -com/raquo/laminar/api/L.footer.
	 -com/raquo/laminar/api/L.footer#
	 -com/raquo/laminar/api/L.footer().
	 -footer.
	 -footer#
	 -footer().
	 -scala/Predef.footer.
	 -scala/Predef.footer#
	 -scala/Predef.footer().
offset: 6906
uri: file:///D:/ProyectoFinalLenguajesFront-main/frontend/src/main/scala/app/views/PaginaPrincipal.scala
text:
```scala
package app.views

import com.raquo.laminar.api.L._
import org.scalajs.dom
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

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

    val mostrarInicio = Var(true)

    div(
      onMountCallback { _ =>
        val styleTag = dom.document.createElement("style")
        styleTag.textContent =
          """
          @keyframes fadeIn {
            from { opacity: 0; transform: scale(0.97); }
            to { opacity: 1; transform: scale(1); }
          }
          @keyframes fadeOut {
            from { opacity: 1; transform: scale(1); }
            to { opacity: 0; transform: scale(0.97); }
          }
          .fade-in {
            animation: fadeIn 0.7s ease forwards;
          }
          .fade-out {
            animation: fadeOut 0.6s ease forwards;
          }

          body {
            font-family: 'Poppins', sans-serif;
            margin: 0;
            background-color: #fff;
            overflow-x: hidden;
          }

          .hero {
            background: url('https://img.freepik.com/free-photo/top-view-ingredients-cooking-pasta_23-2147964928.jpg') center/cover no-repeat;
            color: #2d3436;
            padding: 120px 20px 100px;
            text-align: center;
          }

          .hero h1 {
            font-size: 2.5rem;
            font-weight: 600;
            margin-bottom: 10px;
          }

          .hero h2 {
            font-size: 3rem;
            font-weight: 700;
            color: #2d3436;
            margin-bottom: 10px;
          }

          .hero span {
            color: #2d6a4f;
          }

          .search-bar {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 10px;
            margin-top: 30px;
          }

          .search-bar input {
            width: 400px;
            padding: 12px 18px;
            border: 1px solid #ccc;
            border-radius: 30px;
            font-size: 1rem;
          }

          .search-bar button {
            background-color: #e76f51;
            border: none;
            color: white;
            padding: 12px 24px;
            border-radius: 30px;
            cursor: pointer;
            font-weight: 600;
            transition: background-color 0.3s ease;
          }

          .search-bar button:hover {
            background-color: #f2845c;
          }

          .navbar {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            padding: 20px 50px;
            background-color: white;
          }

          .navbar button {
            border: 2px solid #2d6a4f;
            border-radius: 30px;
            padding: 8px 20px;
            font-weight: 600;
            font-size: 0.95rem;
            margin-left: 10px;
            cursor: pointer;
            transition: all 0.3s ease;
          }

          .navbar button:first-child {
            background-color: white;
            color: #2d6a4f;
          }

          .navbar button:last-child {
            background-color: #e76f51;
            color: white;
            border-color: #e76f51;
          }

          .navbar button:last-child:hover {
            background-color: #f2845c;
          }

          .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 25px;
            padding: 40px 80px;
          }

          .card {
            border: 1px solid #eee;
            border-radius: 14px;
            background-color: white;
            box-shadow: 0 3px 8px rgba(0,0,0,0.1);
            text-align: center;
            padding: 16px;
            transition: transform 0.2s, box-shadow 0.2s;
          }

          .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 12px rgba(0,0,0,0.15);
          }

          .card img {
            border-radius: 10px;
            width: 100%;
            height: 200px;
            object-fit: cover;
            margin-bottom: 10px;
          }

          .card h3 {
            font-size: 1.1rem;
            color: #222;
            margin: 8px 0 4px 0;
          }

          .card p {
            color: #555;
            font-size: 0.95rem;
            margin-bottom: 6px;
          }

          footer {
            background-color: #f8f8f8;
            text-align: center;
            padding: 40px 20px;
            color: #444;
            border-top: 2px solid #e76f51;
          }

          footer img {
            height: 80px;
            margin-bottom: 10px;
          }
          """
        dom.document.head.appendChild(styleTag)
      },

      child <-- mostrarInicio.signal.map { inicio =>
        if (inicio) {
          div(
            cls := "fade-in hero",
            h1("Descubre"),
            h2("nuestras recetas ", span("más solicitadas")),
            div(
              cls := "search-bar",
              input(placeholder := "Buscar receta"),
              button("BUSCAR", onClick --> { _ =>
                val el = dom.document.querySelector(".fade-in")
                if (el != null) {
                  el.classList.remove("fade-in")
                  el.classList.add("fade-out")
                  dom.window.setTimeout(() => mostrarInicio.set(false), 600)
                } else {
                  mostrarInicio.set(false)
                }
              })
            )
          )
        } else {
          div(
            cls := "fade-in",
            div(
              cls := "navbar",
              button("REGÍSTRATE"),
              button("INICIAR SESIÓN")
            ),

            div(
              cls := "hero",
              h1("Descubre"),
              h2("nuestras recetas ", span("más solicitadas")),
              div(
                cls := "search-bar",
                input(placeholder := "Buscar receta"),
                button("BUSCAR")
              )
            ),

            div(
              cls := "grid",
              children <-- librosVar.signal.map { lista =>
                if (lista.isEmpty)
                  Seq(div("No hay libros disponibles."))
                else
                  lista.map { libro =>
                    div(
                      cls := "card",
                      img(src := s"https://tl7vhlzb-8081.brs.devtunnels.ms/frontimg/imagen/${libro.nombreimagen}"),
                      h3(libro.nombre),
                      p(libro.nombre_categoria),
                      p(f"S/ ${libro.precio}%.2f")
                    )
                  }
              }
            ),

            @@footer(
              img(src := "https://cdn-icons-png.flaticon.com/512/2784/2784459.png"),
              p("Recipes Lorem Ipsum"),
              p("Derechos Reservados 2025")
            )
          )
        }
      }
    )
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: 