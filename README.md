# anijuan
app android en kotlin, aplicacion enfocada, al entretenimineto del anime, donde se puede ver capitulos de los animes publicados desde un app de escritorio.


# estructura del proyecto

# moduleSettigns
en este modulo esta enfocado el guardar las configuraciones de nuestra app, por ejemplo elegir el tipo de reproductor, o el servidor de los videos


<div align="center">
  <a>
    <img height="400" width= "400" src="screenshoots/moduleSettigns.png" alt="Logo" width="80" height="80">
  </a>
</div>

#moduleMain
este modulo es nuestra activiadad principal donde aloja 4 fragmento, el fragmento donde se muestran los animes publciados, los ultimos captitulos publicados, y un pequeno sistema de guardado de animes para ver mas tarde

# moduleSeeLater
este modulo esta enfocado para que el usuario pueda guardar, tanto capitulos como animes completos para ver mas tarde

# moduleSearchAnime
este modulo, esta enfocado en la busqueda del anime por nombre

#modulePlayer
este modulo se encarga de reproducir los videos cargados en los servidores

# moduleIssue
este modulo aun se encuentra en desarrollo es el encargado de mostrar los animes en emision, y el dia en que se publica un episodio

#moduleEpisode
este modulo se encarga de mostrar los ultimos capitulos publicados

#moduleAnimeDetails
este modulo se encarga de mostrar informacion sobre el anime tanto sus capitulos

#moduleAnime
este modulo se encarga de mostrar los ultimos animes publicados

## common
esta carpeta nos sirve para guardar scripts que cumplan una funcion en especifico, como intergrar las entidades, la base de datos, entre otros

## entities
si deseas escalar la base de datos del proyecto, basta con solo modificar estos scripts, y añadir el nuevo campo requerido

## firebase
en este apartado se guarda todo script relacionado con firebase base ya sea para notification push o las referencias.

## modules
esta carpeta contiene todos los modulos de nuestra aplicacion.

# como instalar
1. clonar el repositorio github, esto se logra desde el etorno de android studio, creando un nuevo proyecto desde github y automaticamente se agrega
2. Es necesario iniciar un nuevo proyecto en firebase console, y activar los servicios de autenticacion con google, notification push y realtime database

# conocimiento tecnico requerido
1. conocer kotlin en un nivel basico-intermedio
2. conocer un poco sobre firebase database realtime y autenticacion con google
3. conocer sobre la arquitectura MVVM (aun que gran parte del codigo falta por limpiar con esta arquitectura)

# acerca de este proyecto.
el codigo fuente queda libre para los desarrolladores que quieran seguir escalando el proyecto.
