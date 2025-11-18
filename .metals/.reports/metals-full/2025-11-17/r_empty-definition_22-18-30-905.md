error id: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/AdminView.scala:
file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/AdminView.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -com/raquo/laminar/api/L.LibroCompleto#
	 -LibroCompleto#
	 -scala/Predef.LibroCompleto#
offset: 6477
uri: file:///D:/ProyectoFinalLenguajesFront/frontend/src/main/scala/app/views/AdminView.scala
text:
```scala
package app.views

import app.views.{AgregarView, EliminarView, ReporteView, AnadirAdminView, PaginaPrincipal}
import com.raquo.laminar.api.L._
import org.scalajs.dom
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom.html

object AdminView {
  def apply(
    currentView: Var[HtmlElement],
    librosVar: Var[List[PaginaPrincipal.Libro]] // Asumiendo PaginaPrincipal.Libro es el tipo correcto
  ): HtmlElement = {

    // Obtener el email para mostrar el usuario. Usaremos solo la parte antes del '@' si es un email.
    val fullAdminEmail = dom.window.localStorage.getItem("email")
    val adminUsername = fullAdminEmail.split('@').headOption.getOrElse("UsuarioDesconocido")
    val naranjaPrincipal = "#f39c12"
    val naranjaClaro = "#fffaf0"

    // --- Helper function for Admin Tiles ---
    def adminTile(text: String, iconCode: String, onClickAction: => Unit) = {
      val isHovered = Var(false)

      button(
        // Estilos base y condicionales
        styleAttr <-- isHovered.signal.map { hovered =>
          val baseStyle = s"""
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 20px;
            background-color: white;
            color: #333;
            border: 1px solid #ddd; /* Borde sutil */
            border-radius: 8px;
            cursor: pointer;
            font-size: 1.1em;
            font-weight: 500;
            transition: all 0.2s ease;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
            min-height: 120px; /* Asegura un tamaño consistente */
          """
          if (hovered) {
            s"$baseStyle transform: translateY(-3px); box-shadow: 0 6px 12px rgba(0,0,0,0.1); background-color: $naranjaClaro; border-color: $naranjaPrincipal;"
          } else {
            s"$baseStyle transform: translateY(0); border-color: #ddd;"
          }
        },
        // Manejadores de eventos
        onMouseOver --> { _ => isHovered.set(true) },
        onMouseOut --> { _ => isHovered.set(false) },
        onClick --> { _ => onClickAction },

        // Ícono (Font Awesome)
        i(
          styleAttr := s"font-family: 'Font Awesome 6 Free'; font-weight: 900; font-size: 2.5em; margin-bottom: 10px; color: $naranjaPrincipal; content: '$iconCode';",
          // Aplicamos color naranja al ícono
        ),
        // Texto del botón
        span(text)
      )
    }
    // --- End Helper function ---

    div(
      // Contenedor general (usa el fondo de la imagen de referencia si es posible, sino blanco)
      styleAttr := """
        display: flex;
        flex-direction: column;
        height: 100vh;
        font-family: 'Roboto', sans-serif;
        background-color: #f5f5f5; /* Fondo claro para contraste */
      """,

      // 1. Barra superior NARANJA
      div(
        styleAttr := s"""
          display: flex;
          justify-content: space-between;
          align-items: center;
          padding: 15px 30px;
          background-color: $naranjaPrincipal; /* Naranja bonito */
          box-shadow: 0 3px 6px rgba(0,0,0,0.1);
          color: white;
        """,
        // Logo (simulado con texto estilizado)
        span(
          styleAttr := "font-size: 1.8em; font-weight: 700;",
          "RecetApp Admin"
        ),
        // Botón Cerrar Sesión
        button(
          "Cerrar Sesión",
          // Estilo y hover reactivo para el botón de Cerrar Sesión
          {
            val isLogoutHovered = Var(false)
            Seq(
              styleAttr <-- isLogoutHovered.signal.map { hovered =>
                val baseStyle = s"""
                  color: white;
                  border: 1px solid white;
                  padding: 8px 15px;
                  border-radius: 5px;
                  cursor: pointer;
                  font-size: 1em;
                  transition: background-color 0.2s ease, color 0.2s ease;
                  background-color: transparent;
                """
                if (hovered) {
                  s"$baseStyle background-color: white; color: $naranjaPrincipal;" // Invertir colores en hover
                } else {
                  baseStyle
                }
              },
              onMouseOver --> { _ => isLogoutHovered.set(true) },
              onMouseOut --> { _ => isLogoutHovered.set(false) }
            )
          },
          onClick --> { _ =>
            dom.window.localStorage.removeItem("rol")
            dom.window.localStorage.removeItem("id_persona")
            dom.window.localStorage.removeItem("email")
            dom.window.alert("Sesión cerrada correctamente.")
            currentView.set(PaginaPrincipal(currentView, librosVar))
          }
        )
      ),

      // 2. Contenido central (Saludo y Paneles)
      div(
        styleAttr := """
          flex: 1;
          display: flex;
          flex-direction: column;
          align-items: center;
          padding: 50px 30px;
        """,
        
        // Saludo estético centrado
        div(
          styleAttr := "text-align: center; margin-bottom: 50px;",
          h1(
            styleAttr := "font-size: 2.5em; font-weight: 300; color: #333; margin: 0;",
            "¡Bienvenido Administrador!"
          ),
          p(
            styleAttr := s"font-size: 1.8em; font-weight: 500; color: $naranjaPrincipal; margin-top: 10px;",
            adminUsername // Muestra solo el nombre de usuario
          )
        ),

        // Contenedor de los paneles (Organización 2x2)
        div(
          styleAttr := """
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 30px;
            max-width: 700px; /* Tamaño máximo para el bloque de botones */
            width: 100%;
          """,

          // Uso de la función adminTile para cada opción con códigos Unicode de Font Awesome
          adminTile(
            "Agregar Producto",
            "\\f055", // plus-circle
            currentView.set(AgregarView(currentView, librosVar))
          ),
          adminTile(
            "Eliminar Producto",
            "\\f057", // minus-circle (o trash para un mejor ícono de eliminar: \f1f8)
            currentView.set(EliminarView(currentView, Var(List.empty[EliminarView.LibroC@@ompleto])))
          ),
          adminTile(
            "Ver Reportes",
            "\\f200", // chart-line
            currentView.set(ReporteView(currentView, librosVar))
          ),
          adminTile(
            "Añadir Administrador",
            "\\f007", // user-plus
            currentView.set(AnadirAdminView(currentView, librosVar))
          )
        )
      )
    )
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 