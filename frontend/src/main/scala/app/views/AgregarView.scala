package app.views

import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

case class Categoria(id: Int, nombre: String)


object AgregarView {
  def apply(
    currentView: Var[HtmlElement],
    librosVar: Var[List[PaginaPrincipal.Libro]]
  ): HtmlElement = {
    
    val nombreVar = Var("")
    val precioVar = Var("")
    //val categoriasVar = Var(List.empty[js.Dynamic])
    val categoriasVar = Var(List.empty[Categoria])

    val categoriaSeleccionada = Var("")

    val pdfFileVar = Var(Option.empty[dom.File])
    val imagenFileVar = Var(Option.empty[dom.File])

    // Obtener categorias
    Ajax.get("https://tl7vhlzb-8081.brs.devtunnels.ms/categoria/obtener_categoria")
        .map { xhr =>
            if (xhr.status == 200) {
            try {
                dom.console.log("Texto recibido:", xhr.responseText)

                val response = JSON.parse(xhr.responseText).asInstanceOf[js.Array[js.Dynamic]]

                val categorias = response.map { cat =>
                Categoria(
                    id = cat.id_categoria.toString.toInt, //id = cat.id.asInstanceOf[Int], // o cat.id_categoria si así es tu JSON
                    nombre = cat.nombre.toString
                )
                }.toList

                dom.console.log("Categorías recibidas:", categorias)
                categoriasVar.set(categorias)
            } catch {
                case e: Throwable =>
                dom.console.error("Error parseando JSON:", e)
                dom.window.alert("Respuesta inválida del servidor. Revisa consola.")
            }
            } else {
            dom.console.log("Estado HTTP:", xhr.status)
            dom.window.alert("Error obteniendo categorías")
            }
            () // <- IMPORTANTE: retornar algo (unidad) para que el map compile
        }
        .recover {
            case ex =>
            dom.console.error("Error en petición de categorías:", ex)
            dom.window.alert("Error conectando con el servidor. Revisa consola para más detalles.")
        }

    // 🔹 Función para agregar libro
    def agregarLibro(): Unit = {
      val pdfFile = pdfFileVar.now().orNull
      val imagenFile = imagenFileVar.now().orNull
      val nombre = nombreVar.now()
      val precio = precioVar.now()
      val idCategoria = categoriaSeleccionada.now()

      if (pdfFile != null && imagenFile != null && nombre.nonEmpty && precio.nonEmpty && idCategoria.nonEmpty) {
        val pdfForm = new dom.FormData()
        pdfForm.append("file", pdfFile)
        Ajax.post(s"https://tl7vhlzb-8081.brs.devtunnels.ms/documento/uploadpdf/${pdfFile.name}", pdfForm)
          .flatMap { _ =>
            val imgForm = new dom.FormData()
            imgForm.append("file", imagenFile)
            Ajax.post(s"https://tl7vhlzb-8081.brs.devtunnels.ms/imagen/upload/${imagenFile.name}", imgForm)
          }
          .flatMap { _ =>
            val data = js.Dynamic.literal(
              nombre = nombre,
              precio = precio.toFloat,
              id_categoria = idCategoria.toInt,
              nombrepdf = pdfFile.name,
              nombreimagen = imagenFile.name
            )
            Ajax.post(
              url = "https://tl7vhlzb-8081.brs.devtunnels.ms/libro/insertar_libro",
              data = JSON.stringify(data),
              headers = Map("Content-Type" -> "application/json")
            )
          }
          .map { xhr =>
            if (xhr.status == 200) {
              dom.window.alert("Libro agregado con éxito")
              currentView.set(AdminView(currentView, librosVar))
            } else {
              dom.window.alert("Error agregando libro: " + xhr.status)
            }
          }
          .recover { case ex =>
            dom.window.alert("Error: " + ex.getMessage)
          }
      } else {
        dom.window.alert("Completa todos los campos y selecciona archivos")
      }
    }

  div(
    styleAttr := """
      display: flex;
      justify-content: center;
      align-items: flex-start;
      padding-top: 40px;
      min-height: 100vh;
      background-image: url('/frontend/inicioSesion.png');
      background-size: 500px;
      background-repeat: repeat;
      background-position: center;
      font-family: 'Roboto', sans-serif;
    """,

    div(
      styleAttr := """
        background-color: white;
        padding: 35px;
        border-radius: 20px;
        width: 420px;
        display: flex;
        flex-direction: column;
        gap: 18px;
        box-shadow: 0 8px 20px rgba(0,0,0,0.15);
      """,

      h2(
        "Agregar Libro",
        styleAttr := """
          text-align: center;
          color: #2e7d32;
          font-size: 26px;
          font-weight: bold;
          margin-bottom: 12px;
        """
      ),

      // ---------- Nombre ----------
      label("Nombre de la receta", styleAttr := "font-weight: bold; color: #333; font-size: 15px;"),
      input(
        placeholder := "Ejemplo: Delicias Caseras",
        styleAttr := """
          padding: 12px;
          font-size: 15px;
          border: 2px solid #e0e0e0;
          border-radius: 10px;
          width: 100%;
          transition: 0.2s;
        """,
        onInput.mapToValue --> nombreVar.writer
      ),

      // ---------- Precio ----------
      label("Precio", styleAttr := "font-weight: bold; color: #333; font-size: 15px;"),
      input(
        typ := "number", stepAttr := "0.01",
        placeholder := "Ejemplo: 19.99",
        styleAttr := """
          padding: 12px;
          font-size: 15px;
          border: 2px solid #e0e0e0;
          border-radius: 10px;
          width: 100%;
        """,
        onInput.mapToValue --> precioVar.writer
      ),

      // ---------- Categoría ----------
      label("Categoría", styleAttr := "font-weight: bold; color: #333; font-size: 15px;"),
      select(
        onChange.mapToValue --> categoriaSeleccionada.writer,
        styleAttr := """
          padding: 12px;
          font-size: 15px;
          border: 2px solid #e0e0e0;
          border-radius: 10px;
          width: 100%;
          background-color: #fff;
        """,
        children <-- categoriasVar.signal.map(_.map { cat =>
          option(value := cat.id.toString, cat.nombre)
        })
      ),

      // ---------- PDF ----------
      label("Archivo PDF", styleAttr := "font-weight: bold; color: #333; font-size: 15px;"),
      div(
        styleAttr := "display: flex; flex-direction: column; gap: 6px;",

        // Botón estilizado
        button(
          "Subir PDF",
          styleAttr := """
            bwidth: 100%;
            background: white;
            border: 2px solid #ff6b00;
            color: #ff6b00;
            font-weight: bold;
            padding: 13px;
            border-radius: 30px;
            cursor: pointer;
            font-size: 15px;
            transition: 0.3s;
          """,
          onClick --> { _ =>
            dom.document.getElementById("pdfFileInput").asInstanceOf[dom.html.Input].click()
          },
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

        // Input real oculto
        input(
          idAttr := "pdfFileInput",
          typ := "file",
          accept := "application/pdf",
          styleAttr := "display:none;",
          onChange.mapToFiles --> { files => pdfFileVar.set(files.headOption) }
        ),

        // Nombre del archivo
        child.maybe <-- pdfFileVar.signal.map(_.map(f => 
          div(
            s"Archivo: ${f.name}",
            styleAttr := "font-size: 13px; color: #555; margin-left: 4px;"
          )
        ))
      ),

      // ---------- Imagen ----------
      label("Imagen de portada", styleAttr := "font-weight: bold; color: #333; font-size: 15px;"),
      div(
        styleAttr := "display: flex; flex-direction: column; gap: 6px;",

        // Botón estilizado
        button(
          "Subir Imagen",
          styleAttr := """
            bwidth: 100%;
            background: white;
            border: 2px solid #ff6b00;
            color: #ff6b00;
            font-weight: bold;
            padding: 13px;
            border-radius: 30px;
            cursor: pointer;
            font-size: 15px;
            transition: 0.3s;
          """.stripMargin,
          onClick --> { _ =>
            dom.document.getElementById("imgFileInput").asInstanceOf[dom.html.Input].click()
          },
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

        // Input real oculto
        input(
          idAttr := "imgFileInput",
          typ := "file",
          accept := "image/*",
          styleAttr := "display:none;",
          onChange.mapToFiles --> { files => imagenFileVar.set(files.headOption) }
        ),

        // Nombre del archivo
        child.maybe <-- imagenFileVar.signal.map(_.map(f => 
          div(
            s"Imagen: ${f.name}",
            styleAttr := "font-size: 13px; color: #555; margin-left: 4px;"
          )
        ))
      ),

      // ---------- Botón agregar ----------
      button(
        "Agregar",
        styleAttr := """
          background-color: #2e7d32;
          color: white;
          padding: 12px;
          font-size: 16px;
          font-weight: bold;
          border: none;
          border-radius: 12px;
          cursor: pointer;
          transition: 0.2s;
        """,
        onClick --> { _ => agregarLibro() }
      ),

      // ---------- Botón volver ----------
      a(
        "← Atrás",
        href := "#",
        onClick.preventDefault --> { _ => currentView.set(AdminView(currentView, librosVar)) },
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