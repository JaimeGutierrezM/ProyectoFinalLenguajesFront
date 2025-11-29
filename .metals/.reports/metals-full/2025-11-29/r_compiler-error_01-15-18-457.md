error id: 511AAB99E22287AC4D5D7C74426C8F2C
file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/ReporteView.scala
### java.util.NoSuchElementException: head of empty String

occurred in the presentation compiler.



action parameters:
offset: 2658
uri: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/ReporteView.scala
text:
```scala
package app.views

import com.raquo.laminar.api.L._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object ReporteView {

  case class Compra(
    nombreLibro: String,
    categoria: String,
    nombrePersona: String,
    dni: String,
    correo: String,
    precio: Double
  )

  def apply(
    currentView: Var[HtmlElement],
    librosVar: Var[List[PaginaPrincipal.Libro]]
  ): HtmlElement = {

    val mesVar = Var("")
    val anioVar = Var("")
    val comprasVar = Var(List.empty[Compra])
    val totalVar = Var(0.0)

    // Buscar reportes
    def buscarReporte(): Unit = {
      val mes  = mesVar.now()
      val anio = anioVar.now()

      Ajax.get(
        s"https://tl7vhlzb-8081.brs.devtunnels.ms/reportes/reporte?mes=$mes&anio=$anio"
      ).map { xhr =>
        if (xhr.status == 200) {
          val json = JSON.parse(xhr.responseText)
          val data = json.reporte.asInstanceOf[js.Array[js.Dynamic]]
          val total = json.total.toString.toDouble

          val compras = data.map { item =>
            Compra(
              nombreLibro    = item.nombre_libro.toString,
              categoria      = item.categoria.toString,
              nombrePersona  = item.nombre_persona.toString,
              dni            = item.dni.toString,
              correo         = item.correo.toString,
              precio         = item.precio.toString.toDouble
            )
          }.toList

          comprasVar.set(compras)
          totalVar.set(total)
        } else dom.window.alert("Error obteniendo reporte.")
      }.recover { case ex =>
        dom.window.alert("Error: " + ex.getMessage)
      }
    }

    div(
      styleAttr := """
        padding: 20px;
        font-family: Roboto, sans-serif;
      """,

      h3("Reporte de Compras", styleAttr := "text-align: center; margin-bottom: 20px;"),

      // Filtros
      div(
        styleAttr := """
          display: flex;
          justify-content: center;
          gap: 10px;
          margin-bottom: 20px;
        """,

        input(
          placeholder := "Mes (01-12)",
          onInput.mapToValue --> mesVar.writer,
          styleAttr := "padding: 8px; border: 1px solid #ccc; border-radius: 5px; width: 100px;"
        ),
        input(
          placeholder := "Año (e.g., 2025)",
          onInput.mapToValue --> anioVar.writer,
          styleAttr := "padding: 8px; border: 1px solid #ccc; border-radius: 5px; width: 120px;"
        ),
        @@button(
          "Buscar",
          styleAttr := """
            background-color: #3498db;
            color: white;
            border: none;
            padding: 8px 12px;
            border-radius: 5px;
            cursor: pointer;
          """,
          onClick --> { _ => buscarReporte() },
          disabled <-- mesVar.signal.combineWith(anioVar.signal).map { case (m, a) =>
            m.isEmpty || a.isEmpty
          }
        )
      ),

            // Botón Aceptar
            button(
              "Aceptar",
              onClick --> { _ => buscarReporte() },
              disabled <-- mesVar.signal.combineWith(anioVar.signal).map { case (m, a) =>
                m.isEmpty || a.isEmpty
              },
              styleAttr := """
                background: #FF7A00;
                color: white;
                border: none;
                padding: 10px 25px;
                border-radius: 8px;
                font-weight: 600;
                cursor: pointer;
                margin-bottom: 3px;
              """
            )
          )
        ),

        // Tabla de reportes
        div(
          styleAttr := """
            background: white;
            padding: 25px;
            border-radius: 14px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
          """,

          table(
            styleAttr := """
              width: 100%;
              border-collapse: separate;
              border-spacing: 0 12px;
            """,

            thead(
              tr(
                List("Título", "Categoría", "Cliente", "DNI", "Correo", "Precio").map { titulo =>
                  th(
                    titulo,
                    styleAttr := """
                      text-align: left;
                      padding: 10px;
                      font-size: 14px;
                      font-weight: 600;
                      color: #444;
                    """
                  )
                }
              )
            ),

            tbody(
              children <-- comprasVar.signal.map(
                _.map { c =>
                  tr(
                    styleAttr := """
                      background: #F8F8F8;
                      border-radius: 10px;
                    """,

                    td(c.nombreLibro, styleAttr := "padding: 12px;"),
                    td(c.categoria, styleAttr := "padding: 12px;"),
                    td(c.nombrePersona, styleAttr := "padding: 12px;"),
                    td(c.dni, styleAttr := "padding: 12px;"),
                    td(c.correo, styleAttr := "padding: 12px;"),
                    td(f"S/ ${c.precio}%.2f", styleAttr := "padding: 12px; font-weight: 600;")
                  )
                }
              )
            )
          ),

          // TOTAL
          div(
            styleAttr := """
              margin-top: 20px;
              display: flex;
              justify-content: flex-end;
              font-size: 18px;
              font-weight: bold;
            """,
            span("Total: S/ "),
            child.text <-- totalVar.signal.map(t => f"$t%.2f")
          )
        ),

        button(
          "Volver",
          onClick --> { _ => currentView.set(AdminView(currentView, librosVar)) },
          styleAttr := """
            margin-top: 25px;
            background: #A6A6A6;
            color: white;
            padding: 10px 20px;
            border-radius: 8px;
            border: none;
            cursor: pointer;
          """
        )
      )
    )
  }
}

```


presentation compiler configuration:
Scala version: 2.13.18
Classpath:
<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.18\scala-library-2.13.18.jar [exists ]
Options:





#### Error stacktrace:

```
scala.collection.StringOps$.head$extension(StringOps.scala:1124)
	scala.meta.internal.metals.ClassfileComparator.compare(ClassfileComparator.scala:30)
	scala.meta.internal.metals.ClassfileComparator.compare(ClassfileComparator.scala:3)
	java.base/java.util.PriorityQueue.siftUpUsingComparator(PriorityQueue.java:660)
	java.base/java.util.PriorityQueue.siftUp(PriorityQueue.java:637)
	java.base/java.util.PriorityQueue.offer(PriorityQueue.java:330)
	java.base/java.util.PriorityQueue.add(PriorityQueue.java:311)
	scala.meta.internal.metals.ClasspathSearch.$anonfun$search$3(ClasspathSearch.scala:32)
	scala.meta.internal.metals.ClasspathSearch.$anonfun$search$3$adapted(ClasspathSearch.scala:26)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:630)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:628)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1313)
	scala.meta.internal.metals.ClasspathSearch.search(ClasspathSearch.scala:26)
	scala.meta.internal.metals.WorkspaceSymbolProvider.search(WorkspaceSymbolProvider.scala:107)
	scala.meta.internal.metals.MetalsSymbolSearch.search$1(MetalsSymbolSearch.scala:114)
	scala.meta.internal.metals.MetalsSymbolSearch.search(MetalsSymbolSearch.scala:118)
	scala.meta.internal.pc.AutoImportsProvider.autoImports(AutoImportsProvider.scala:58)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$autoImports$1(ScalaPresentationCompiler.scala:399)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:148)
	scala.meta.internal.pc.CompilerAccess.$anonfun$withInterruptableCompiler$1(CompilerAccess.scala:92)
	scala.meta.internal.pc.CompilerAccess.$anonfun$onCompilerJobQueue$1(CompilerAccess.scala:209)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:152)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614)
	java.base/java.lang.Thread.run(Thread.java:1474)
```
#### Short summary: 

java.util.NoSuchElementException: head of empty String