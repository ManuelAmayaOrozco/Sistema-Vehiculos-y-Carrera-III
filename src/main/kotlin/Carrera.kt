import kotlin.math.ceil

/**
 * Representa una carrera que incluye múltiples vehículos como participantes. La carrera tiene un nombre, una distancia total
 * a recorrer, y maneja el estado y el avance de cada vehículo participante.
 *
 * @property nombreCarrera El nombre de la carrera para identificación.
 * @property distanciaTotal La distancia total que los vehículos deben recorrer para completar la carrera.
 * @constructor Inicializa una carrera con una lista de vehículos participantes y valida la distancia mínima requerida.
 */
class Carrera(
    val nombreCarrera: String,
    private val distanciaTotal: Float,
    private val participantes: List<Vehiculo> = listOf()
) {
    private val historialAcciones = mutableMapOf<String, MutableList<String>>()
    private var estadoCarrera = false // Indica si la carrera está en curso o ha finalizado.
    private val posiciones = mutableMapOf<String, Float>()

    init {
        require(distanciaTotal >= 1000) { "La distancia total de la carrera debe ser al menos 1000 km." }
        participantes.forEach { vehiculo -> inicializaDatosParticipante(vehiculo) }
    }

    companion object {
        private const val KM_PARA_FILIGRANA = 20f // Cada 20 km, se realiza una filigrana.
    }

    /**
     * Representa el resultado final de un vehículo en la carrera, incluyendo su posición final, el kilometraje total recorrido,
     * el número de paradas para repostar, y un historial detallado de todas las acciones realizadas durante la carrera.
     *
     * @property vehiculo El [Vehiculo] al que pertenece este resultado.
     * @property posicion La posición final del vehículo en la carrera, donde una posición menor indica un mejor rendimiento.
     * @property kilometraje El total de kilómetros recorridos por el vehículo durante la carrera.
     * @property paradasRepostaje El número de veces que el vehículo tuvo que repostar combustible durante la carrera.
     * @property historialAcciones Una lista de cadenas que describen las acciones realizadas por el vehículo a lo largo de la carrera, proporcionando un registro detallado de su rendimiento y estrategias.
     */
    data class ResultadoCarrera(
        val vehiculo: Vehiculo,
        val posicion: Int,
        val kilometraje: Float,
        val paradasRepostaje: Int,
        val historialAcciones: List<String>
    )

    /**
     * Proporciona una representación en cadena de texto de la instancia de la carrera, incluyendo detalles clave como
     * el nombre de la carrera, la distancia total a recorrer, la lista de participantes, el estado actual de la carrera
     * (en curso o finalizada), el historial de acciones realizadas por los vehículos durante la carrera y las posiciones
     * actuales de los participantes.
     *
     * @return Una cadena de texto que describe los atributos principales de la carrera, incluyendo el nombre,
     * distancia total, participantes, estado actual, historial de acciones y posiciones de los vehículos participantes.
     */
    override fun toString(): String {
        return "NombreCarrera: $nombreCarrera, DistanciaTotal: $distanciaTotal, Participantes: $participantes, EstadoCarrera: $estadoCarrera, HistorialAcciones: $historialAcciones, Posiciones: $posiciones." }

    /**
     * Inicializa los datos de un participante en la carrera, preparando su historial de acciones y estableciendo
     * su posición inicial. Este método se llama automáticamente al agregar un nuevo vehículo a la carrera.
     *
     * @param vehiculo El [Vehiculo] cuyos datos se inicializan.
     */
    private fun inicializaDatosParticipante(vehiculo: Vehiculo) {
        historialAcciones[vehiculo.nombre] = mutableListOf()
        posiciones[vehiculo.nombre] = 0f
    }

    /**
     * Inicia el proceso de la carrera, haciendo que cada vehículo avance de forma aleatoria hasta que un vehículo
     * alcanza o supera la distancia total de la carrera, determinando así el ganador.
     */
    fun iniciarCarrera() {
        println("¡Comienza la carrera!")

        estadoCarrera = true // Indica que la carrera está en curso.
        while (estadoCarrera) {

            Thread.sleep(100)
            print(".")

            val vehiculoSeleccionado = seleccionaVehiculoQueAvanzara()
            avanzarVehiculo(vehiculoSeleccionado)

            val vehiculoGanador = determinarGanador()
            if (vehiculoGanador != null) {
                estadoCarrera = false
                println("\n¡Carrera finalizada!")
                println("\n¡¡¡ENHORABUENA ${vehiculoGanador.nombre}!!!\n")
            }

        }
    }

    /**
     * Selecciona aleatoriamente un vehículo participante para avanzar en la carrera. Este método se utiliza dentro
     * del proceso de la carrera para decidir qué vehículo realiza el próximo movimiento.
     *
     * @return El [Vehiculo] seleccionado para avanzar.
     */
    private fun seleccionaVehiculoQueAvanzara() = participantes.random()

    /**
     * Calcula el número de tramos o segmentos en los que se divide la distancia que un vehículo intenta recorrer.
     * Esto se utiliza para simular el avance por etapas de los vehículos en la carrera.
     *
     * @param distancia La distancia total a recorrer en este turno por el vehículo.
     * @return El número de tramos como [Int] en los que se divide la distancia.
     */
    private fun obtenerNumeroDeTramos(distancia: Float) = ceil(distancia.toDouble() / KM_PARA_FILIGRANA).toInt()

    /**
     * Determina la distancia que un vehículo intentará recorrer en su próximo turno, asegurando que no exceda
     * la distancia total de la carrera.
     *
     * @param kilometrosRecorridos Los kilómetros ya recorridos por el vehículo.
     * @return La distancia a recorrer en el siguiente turno.
     */
    private fun obtenerDistanciaARecorrer(kilometrosRecorridos: Float) : Float {
        val distanciaAleatoria = (10..200).random()

        // Comprobar que no nos vamos a pasar de la distancia total a recorrer en la carrera
        // Asegurarnos que va a recorrer los km exactos para llegar a la meta
        return if (distanciaAleatoria + kilometrosRecorridos > this.distanciaTotal) {
            this.distanciaTotal - kilometrosRecorridos
        } else {
            distanciaAleatoria.toFloat()
        }
    }

    /**
     * Avanza un vehículo a lo largo de la carrera, dividiendo su avance en tramos y realizando las acciones
     * correspondientes en cada tramo, como realizar filigranas o repostar combustible.
     *
     * @param vehiculo El [Vehiculo] que avanzará en la carrera.
     */
    private fun avanzarVehiculo(vehiculo: Vehiculo) {
        val distanciaTotalEnAvance = obtenerDistanciaARecorrer(vehiculo.kilometrosActuales)
        val numeroDeTramos = obtenerNumeroDeTramos(distanciaTotalEnAvance) // Rompemos el recorrido en tramos de 20 km.

        registrarAccion(vehiculo.nombre, "Inicia viaje: A recorrer $distanciaTotalEnAvance kms (${vehiculo.kilometrosActuales} kms y ${vehiculo.combustibleActual} L actuales)")

        var distanciaRestanteEnAvance = distanciaTotalEnAvance
        repeat(numeroDeTramos) { //Tramos de KM_PARA_FILIGRANA km
            val distanciaDeTramo = minOf(KM_PARA_FILIGRANA, distanciaRestanteEnAvance)

            avanzarTramo(vehiculo, distanciaDeTramo)
            distanciaRestanteEnAvance -= distanciaDeTramo
            repeat(2) { realizarFiligrana(vehiculo) }
        }

        registrarAccion(vehiculo.nombre, "Finaliza viaje: Total Recorrido $distanciaTotalEnAvance kms (${vehiculo.kilometrosActuales} kms y ${vehiculo.combustibleActual} L actuales)")

        actualizarPosiciones(vehiculo.nombre, distanciaTotalEnAvance)
    }

    /**
     * Avanza un vehículo a través de un tramo específico de la carrera, ajustando su combustible y kilometraje según
     * la distancia recorrida.
     * Realiza las operaciones necesarias para repostar si el combustible no es suficiente para completar el tramo.
     *
     * @param vehiculo El [Vehiculo] que avanza.
     * @param distanciaEnTramo La distancia del tramo a recorrer por el vehículo.
     */
    private fun avanzarTramo(vehiculo: Vehiculo, distanciaEnTramo: Float) {
        var distanciaRestante = vehiculo.realizaViaje(distanciaEnTramo)
        registrarAccion(vehiculo.nombre, "Avance tramo: Recorrido ${distanciaEnTramo - distanciaRestante} kms")

        // Si le queda alguna distancia por recorrer debe repostar
        while (distanciaRestante > 0) {
            val repostado = vehiculo.repostar() // Llenamos el tanque
            registrarAccion(vehiculo.nombre, "Repostaje tramo: $repostado L")

            // Necesitamos de nuevo una distancia para después compararla con la distanciaRestante que devuelve realizarViaje()
            val distancia = distanciaRestante
            distanciaRestante = vehiculo.realizaViaje(distancia)
            registrarAccion(vehiculo.nombre, "Avance tramo: Recorrido ${distancia - distanciaRestante} kms")
        }
    }

    /**
     * Determina de manera aleatoria si un vehículo debe realizar una filigrana durante su turno en la carrera.
     * La decisión se basa en una probabilidad del 50%, donde un resultado menor que 0.5 indica que el vehículo
     * debería realizar una filigrana.
     *
     * @return [Boolean] Verdadero si el vehículo debe realizar una filigrana, falso en caso contrario.
     */
    private fun comprobarSiTocaHacerFiligrana() = (Math.random() < 0.5)

    /**
     * Intenta que un vehículo realice una filigrana durante su avance en la carrera. La filigrana
     * (derrape o caballito) se realiza basada en una probabilidad aleatoria y consume combustible adicional.
     *
     * @param vehiculo El [Vehiculo] que intentará realizar la filigrana.
     */
    private fun realizarFiligrana(vehiculo: Vehiculo) {
        // Lógica para realizar filigranas de motociletas y automovil y registrarlas. Se hará o no aleatoriamente.
        if (comprobarSiTocaHacerFiligrana()) {
            val combustibleRestante: Float

            if (vehiculo is Automovil) {
                combustibleRestante = vehiculo.realizaDerrape()
                registrarAccion(vehiculo.nombre, "Derrape: Combustible restante $combustibleRestante L.")
            } else if (vehiculo is Motocicleta) {
                combustibleRestante = vehiculo.realizaCaballito()
                registrarAccion(vehiculo.nombre, "Caballito: Combustible restante $combustibleRestante L")
            }
        }
    }

    /**
     * Actualiza la posición de un vehículo en la carrera, sumando la distancia recorrida en el último tramo
     * a su total acumulado.
     *
     * @param nombreVehiculo El nombre del [Vehiculo] cuya posición se actualizará.
     * @param kilometraje La distancia recorrida en el último tramo.
     */
    private fun actualizarPosiciones(nombreVehiculo: String, kilometraje: Float) {
        val kilometrosRecorridos = posiciones[nombreVehiculo] ?: 0f
        posiciones[nombreVehiculo] = kilometrosRecorridos + kilometraje
    }

    /**
     * Determina si hay un ganador de la carrera, basado en si algún vehículo ha alcanzado la distancia
     * total de la carrera.
     *
     * @return El [Vehiculo] ganador, si existe; de lo contrario, devuelve null.
     */
    private fun determinarGanador(): Vehiculo? {
        val maxKilometros = posiciones.maxByOrNull { it.value }
        var ganador: Vehiculo? = null

        if ((maxKilometros?.value ?: 0f) >= distanciaTotal)
            ganador = participantes.find { it.nombre == maxKilometros?.key }

        return ganador
    }

    /**
     * Registra una acción específica realizada por un vehículo en su historial de acciones durante la carrera.
     *
     * @param nombreVehiculo El nombre del vehículo que realiza la acción.
     * @param accion La descripción de la acción realizada.
     */
    private fun registrarAccion(nombreVehiculo: String, accion: String) {
        historialAcciones[nombreVehiculo]?.add(accion)
    }

    /**
     * Genera y devuelve una lista de los resultados de la carrera, incluyendo la posición final,
     * el kilometraje total recorrido, el número de paradas para repostar, y el historial de acciones
     * para cada vehículo participante.
     *
     * @return Una lista de objetos [ResultadoCarrera] que representan los resultados finales de la carrera.
     */
    fun obtenerResultados(): List<ResultadoCarrera> {
        val resultados = mutableListOf<ResultadoCarrera>()

        posiciones.toList().sortedByDescending { it.second }.forEachIndexed { posicion, (nombre, kilometraje) ->
            val vehiculo = participantes.find { it.nombre == nombre }
            val paradasRepostaje = historialAcciones[nombre]?.count { it.contains("Repostaje") } ?: 0
            val historial = historialAcciones[nombre] ?: emptyList()

            if (vehiculo != null)
                resultados.add(
                    ResultadoCarrera(
                        vehiculo,
                        posicion + 1,
                        kilometraje,
                        paradasRepostaje,
                        historial
                    )
                )
        }
        return resultados
    }

}