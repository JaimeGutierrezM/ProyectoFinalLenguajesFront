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
          "Agregar producto",
          styleAttr := """
            color: #2c3e50;
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

      // ---------- Título de la receta ----------
      label("Título de la receta", styleAttr := "font-weight: 600; color: #333; font-size: 0.95em; margin-bottom: -10px;"),
      input(
        placeholder := "Ejemplo : Delicias Caseras",
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

      // ---------- Categoría ----------
      label("Categoría", styleAttr := "font-weight: 600; color: #333; font-size: 0.95em; margin-bottom: -10px;"),
      select(
        onChange.mapToValue --> categoriaSeleccionada.writer,
        styleAttr := """
          padding: 12px 15px;
          font-size: 0.95em;
          border: 1px solid #ddd;
          border-radius: 8px;
          width: 100%;
          background-color: #fff;
          cursor: pointer;
          box-sizing: border-box;
        """,
        option(value := "", "Selecciona la categoría", disabled := true, selected := true),
        children <-- categoriasVar.signal.map(_.map { cat =>
          option(value := cat.id.toString, cat.nombre)
        })
      ),

      // ---------- Precio ----------
      label("Precio", styleAttr := "font-weight: 600; color: #333; font-size: 0.95em; margin-bottom: -10px;"),
      input(
        typ := "number", stepAttr := "0.01",
        placeholder := "S/",
        styleAttr := """
          padding: 12px 15px;
          font-size: 0.95em;
          border: 1px solid #ddd;
          border-radius: 8px;
          width: 100%;
          box-sizing: border-box;
        """,
        onInput.mapToValue --> precioVar.writer
      ),

      // ---------- Archivo PDF ----------
      label("Archivo PDF", styleAttr := "font-weight: 600; color: #333; font-size: 0.95em; margin-bottom: -10px;"),
      div(
        styleAttr := "display: flex; flex-direction: column; gap: 8px;",

        // Botón subir PDF
        button(
          "Subir PDF",
          styleAttr := """
            width: 100%;
            background: transparent;
            border: 2px dashed #ff6b35;
            color: #ff6b35;
            font-weight: 600;
            padding: 12px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 0.95em;
            transition: all 0.2s;
          """,
          onClick --> { _ =>
            dom.document.getElementById("pdfFileInput").asInstanceOf[dom.html.Input].click()
          },
          onMouseOver --> { e =>
            val btn = e.target.asInstanceOf[dom.html.Element]
            btn.style.background = "#fff5f0"
          },
          onMouseOut --> { e =>
            val btn = e.target.asInstanceOf[dom.html.Element]
            btn.style.background = "transparent"
          }
        ),

        input(
          idAttr := "pdfFileInput",
          typ := "file",
          accept := "application/pdf",
          styleAttr := "display:none;",
          onChange.mapToFiles --> { files => pdfFileVar.set(files.headOption) }
        ),

        child.maybe <-- pdfFileVar.signal.map(_.map(f => 
          div(
            s"📄 ${f.name}",
            styleAttr := "font-size: 0.85em; color: #666; padding: 5px 10px; background: #f5f5f5; border-radius: 5px;"
          )
        ))
      ),

      // ---------- Imagen de portada ----------
      label("Imagen de portada", styleAttr := "font-weight: 600; color: #333; font-size: 0.95em; margin-bottom: -10px;"),
      div(
        styleAttr := "display: flex; flex-direction: column; gap: 8px;",

        button(
          "Subir imagen",
          styleAttr := """
            width: 100%;
            background: transparent;
            border: 2px dashed #ff6b35;
            color: #ff6b35;
            font-weight: 600;
            padding: 12px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 0.95em;
            transition: all 0.2s;
          """,
          onClick --> { _ =>
            dom.document.getElementById("imgFileInput").asInstanceOf[dom.html.Input].click()
          },
          onMouseOver --> { e =>
            val btn = e.target.asInstanceOf[dom.html.Element]
            btn.style.background = "#fff5f0"
          },
          onMouseOut --> { e =>
            val btn = e.target.asInstanceOf[dom.html.Element]
            btn.style.background = "transparent"
          }
        ),

        input(
          idAttr := "imgFileInput",
          typ := "file",
          accept := "image/*",
          styleAttr := "display:none;",
          onChange.mapToFiles --> { files => imagenFileVar.set(files.headOption) }
        ),

        child.maybe <-- imagenFileVar.signal.map(_.map(f => 
          div(
            s"${f.name}",
            styleAttr := "font-size: 0.85em; color: #666; padding: 5px 10px; background: #f5f5f5; border-radius: 5px;"
          )
        ))
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

        // Botón Agregar libro
        button(
          "Agregar libro",
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
          onClick --> { _ => agregarLibro() },
          onMouseOver --> { e =>
            val btn = e.target.asInstanceOf[dom.html.Element]
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