error id: D797D84E0E12BDC83A611C366DFB0481
file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/CompraView.scala
### scala.reflect.internal.FatalError: 
  unexpected tree: class scala.reflect.internal.Trees$Template
<dom: error>.<RequestInit: error> {
  def <init>(): <$anon: <error>> = {
    super.<init>();
    ()
  };
  <method: error> = null;
  <body: error> = JSON.stringify(data)
}
     while compiling: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/CompraView.scala
        during phase: globalPhase=<no phase>, enteringPhase=parser
     library version: version 2.13.18
    compiler version: version 2.13.18
  reconstructed args: -classpath <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.18\scala-library-2.13.18.jar -Ymacro-expand:discard -Ycache-plugin-class-loader:last-modified -Ypresentation-any-thread

  last tree to typer: Template(value <local $anon>)
       tree position: line 45 of file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/CompraView.scala
              symbol: value <local $anon>
   symbol definition: val <local $anon>: <notype> (a TermSymbol)
      symbol package: app.views
       symbol owners: value <local $anon> -> <$anon: <error>> -> method comprar -> method apply -> object CompraView
           call site: <none> in <none>

== Source file context for tree position ==

    42 
    43             dom.fetch(
    44         "https://tl7vhlzb-8081.brs.devtunnels.ms/comprarlibro/comprar",
    45         new dom.RequestInit {
    46           method = [error] <WORKSPACE>\frontend\src\main\scala\app\views\CompraView.scala:46:20: type mismatch;
    47 [error]  found   : String("POST")
    48 [error]  required: scala.scalajs.js.UndefOr[org.scalajs.dom.HttpMethod]                                                                                                                                                                                               

occurred in the presentation compiler.



action parameters:
offset: 2638
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
    val nombrePDFVar = Var(Option.empty[String])

    def comprar(): Unit = {
      val idLibro = libro.id_libro
      val idPersona = personaVar.now().map(p => p.id_persona.asInstanceOf[Int]).getOrElse(0)
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
        cvv_numero = cvv
      )

      mensajeVar.set(Some("Procesando compra..."))

            dom.fetch(
        "https://tl7vhlzb-8081.brs.devtunnels.ms/comprarlibro/comprar",
        new dom.RequestInit {
          method = [error] <WORKSPACE>\frontend\src\main\scala\app\views\CompraView.scala:46:20: type mismatch;
[error]  found   : String("POST")
[error]  required: scala.scalajs.js.UndefOr[org.scalajs.dom.HttpMethod]                                                                                                                                                                                               
[error]     (which expands to)  org.scalajs.dom.HttpMethod | Unit                                                                                                                                                                                                     
[error]           method = "POST"                                                                                                                                                                                                                                     
[error]                    ^                                                                                                                                                                                                                                          
[error] one error found
[error] (frontend / Compile / compileIncremental@@) Compilation failed
[error] Total time: 2 s, completed 29 nov. 2025, 1:41:03 a.┬ám.
          headers = js.Dictionary(
            "Content-Type" -> "application/json"
          )
          body = JSON.stringify(data)
        }
      ).toFuture
        .flatMap(_.text().toFuture)
        .map { responseText =>
          val response = JSON.parse(responseText)
          mensajeVar.set(Some(s"¡Compra exitosa! ${response.ok.toString}"))

          if (js.typeOf(response.nombrePDF) != "undefined") {
            val nombrePDF = response.nombrePDF.toString
            nombrePDFVar.set(Some(nombrePDF))

            val enlaceDescarga =
              s"https://tl7vhlzb-8081.brs.devtunnels.ms/comprarlibro/descargar/$nombrePDF"

            dom.window.open(enlaceDescarga, "_blank")
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


presentation compiler configuration:
Scala version: 2.13.18
Classpath:
<HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.18\scala-library-2.13.18.jar [exists ]
Options:





#### Error stacktrace:

```
scala.reflect.internal.Reporting.abort(Reporting.scala:70)
	scala.reflect.internal.Reporting.abort$(Reporting.scala:66)
	scala.reflect.internal.SymbolTable.abort(SymbolTable.scala:28)
	scala.tools.nsc.typechecker.Typers$Typer.typedOutsidePatternMode$1(Typers.scala:6297)
	scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:6314)
	scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:6360)
	scala.tools.nsc.typechecker.Typers$Typer.typedQualifier(Typers.scala:6458)
	scala.meta.internal.pc.PcDefinitionProvider.definitionTypedTreeAt(PcDefinitionProvider.scala:190)
	scala.meta.internal.pc.PcDefinitionProvider.definition(PcDefinitionProvider.scala:69)
	scala.meta.internal.pc.PcDefinitionProvider.definition(PcDefinitionProvider.scala:17)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$definition$1(ScalaPresentationCompiler.scala:490)
	scala.meta.internal.pc.CompilerAccess.retryWithCleanCompiler(CompilerAccess.scala:182)
	scala.meta.internal.pc.CompilerAccess.$anonfun$withSharedCompiler$1(CompilerAccess.scala:155)
	scala.Option.map(Option.scala:242)
	scala.meta.internal.pc.CompilerAccess.withSharedCompiler(CompilerAccess.scala:154)
	scala.meta.internal.pc.CompilerAccess.$anonfun$withNonInterruptableCompiler$1(CompilerAccess.scala:132)
	scala.meta.internal.pc.CompilerAccess.$anonfun$onCompilerJobQueue$1(CompilerAccess.scala:209)
	scala.meta.internal.pc.CompilerJobQueue$Job.run(CompilerJobQueue.scala:152)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614)
	java.base/java.lang.Thread.run(Thread.java:1474)
```
#### Short summary: 

scala.reflect.internal.FatalError: 
  unexpected tree: class scala.reflect.internal.Trees$Template
<dom: error>.<RequestInit: error> {
  def <init>(): <$anon: <error>> = {
    super.<init>();
    ()
  };
  <method: error> = null;
  <body: error> = JSON.stringify(data)
}
     while compiling: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/CompraView.scala
        during phase: globalPhase=<no phase>, enteringPhase=parser
     library version: version 2.13.18
    compiler version: version 2.13.18
  reconstructed args: -classpath <HOME>\AppData\Local\Coursier\cache\v1\https\repo1.maven.org\maven2\org\scala-lang\scala-library\2.13.18\scala-library-2.13.18.jar -Ymacro-expand:discard -Ycache-plugin-class-loader:last-modified -Ypresentation-any-thread

  last tree to typer: Template(value <local $anon>)
       tree position: line 45 of file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/CompraView.scala
              symbol: value <local $anon>
   symbol definition: val <local $anon>: <notype> (a TermSymbol)
      symbol package: app.views
       symbol owners: value <local $anon> -> <$anon: <error>> -> method comprar -> method apply -> object CompraView
           call site: <none> in <none>

== Source file context for tree position ==

    42 
    43             dom.fetch(
    44         "https://tl7vhlzb-8081.brs.devtunnels.ms/comprarlibro/comprar",
    45         new dom.RequestInit {
    46           method = [error] <WORKSPACE>\frontend\src\main\scala\app\views\CompraView.scala:46:20: type mismatch;
    47 [error]  found   : String("POST")
    48 [error]  required: scala.scalajs.js.UndefOr[org.scalajs.dom.HttpMethod]                                                                                                                                                                                               