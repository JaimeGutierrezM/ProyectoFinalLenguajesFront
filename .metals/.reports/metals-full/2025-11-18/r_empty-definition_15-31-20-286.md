error id: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/AgregarView.scala:`<error>`#`<error>`.
file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/AgregarView.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -com/raquo/laminar/api/L.label.
	 -com/raquo/laminar/api/L.label#
	 -com/raquo/laminar/api/L.label().
	 -label.
	 -label#
	 -label().
	 -scala/Predef.label.
	 -scala/Predef.label#
	 -scala/Predef.label().
offset: 6553
uri: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/AgregarView.scala
text:
```scala
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
      background-color: #f8f4ef;
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
        border: 2px solid #e67e22;
      """,

      h2(
        "Agregar Libro",
        styleAttr := """
          text-align: center;
          color: #e67e22;
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
      @@div(
  styleAttr := "display: flex; flex-direction: column; gap: 6px;",

  // Botón estilizado
  label(
    "📄 Subir PDF",
    styleAttr := """
      background-color: #e67e22;
      color: white;
      padding: 12px;
      font-size: 15px;
      text-align: center;
      border-radius: 12px;
      cursor: pointer;
      font-weight: bold;
      transition: 0.2s;
    """,
    onClick --> { _ =>
      dom.document.getElementById("pdfFileInput").asInstanceOf[dom.html.Input].click()
    }
  ),

  // Input real oculto
  input(
    id := "pdfFileInput",
    typ := "file",
    accept := "application/pdf",
    styleAttr := "display:none;",
    onChange.mapToFiles --> { files => pdfFileVar.set(files.headOption) }
  ),

  // Nombre del archivo
  child.maybe <-- pdfFileVar.signal.map(_.map(f => 
    div(
      s"📎 Archivo: ${f.name}",
      styleAttr := "font-size: 13px; color: #555; margin-left: 4px;"
    )
  ))
),,

      // ---------- Imagen ----------
      label("Imagen de portada", styleAttr := "font-weight: bold; color: #333; font-size: 15px;"),
      input(
        typ := "file",
        accept := "image/*",
        styleAttr := """
          padding: 10px;
          border-radius: 10px;
          border: 2px solid #e0e0e0;
        """,
        onChange.mapToFiles --> { files => imagenFileVar.set(files.headOption) }
      ),

      // ---------- Botón agregar ----------
      button(
        "Agregar",
        styleAttr := """
          background-color: #e67e22;
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
      button(
        "Volver",
        styleAttr := """
          background-color: #696969;
          color: white;
          padding: 12px;
          font-size: 16px;
          border: none;
          border-radius: 12px;
          cursor: pointer;
        """,
        onClick --> { _ => currentView.set(AdminView(currentView, librosVar)) }
      )
    )
  )
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 