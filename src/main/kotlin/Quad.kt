class Quad(
    nombre: String,
    marca: String,
    modelo: String,
    capacidadCombustible: Float,
    combustibleActual: Float,
    kilometrosActuales: Float,
    cilindrada: Int,
    val tipo: TipoQuad
) : Motocicleta(nombre, marca, modelo, capacidadCombustible, combustibleActual, kilometrosActuales, cilindrada) {

    /**
     * Clase enumerada que define los diferentes tipos de Quads disponibles.
     */
    enum class TipoQuad(val tipo: String) {
        LIGERO("Cuadriciclo ligero"),
        NO_LIGERO("Cuadriciclo no ligero"),
        ESPECIAL("Vehículo especial")
    }

    init {
        require(cilindrada in 125..1000) { "Un quad debe tener entre 125 y 100 cc." }
    }

    /**
     * Calcula y devuelve la autonomía del quad en kilómetros, considerando una eficiencia de
     * combustible específica para motocicletas y después dividiéndolo a la mitad, ya que siempre
     * será igual a la mitad de una motocicleta con la misma cilindrada.
     *
     * @return La autonomía del quad en kilómetros como [Float].
     */
    override fun calcularAutonomia() = ((combustibleActual * (KM_POR_LITRO - (1 - (cilindrada/1000)))) / 2).redondear(2)

    /**
     * Actualiza la cantidad de combustible actual basado en la distancia recorrida, considerando la eficiencia de
     * combustible del vehículo.
     *
     * @param distanciaReal La distancia real recorrida por el vehículo.
     */
    override fun actualizaCombustible(distanciaReal: Float) {
        val combustibleGastado = (distanciaReal / ((KM_POR_LITRO - (1 - (cilindrada/1000))) / 2))
        combustibleActual -= combustibleGastado.redondear(2)
    }

    /**
     * Devuelve una cadena de texto con la información actual de los kilómetros recorridos del vehículo y su
     * combustible actual.
     *
     * @return Una cadena de texto que representa la información del vehículo.
     */
    override fun obtenerInformacion(): String {
        return "${capitalize(nombre)} Quad(km = ${kilometrosActuales.redondear(2)}, combustible = ${combustibleActual.redondear(2)} L)"
    }

    /**
     * Sobrescribe el método toString de la clase [Motocicleta] para ofrecer una representación textual del
     * quad que incluye su tipo, además de los atributos heredados de la clase base.
     *
     * @return Una representación en cadena de texto del quad, detallando su identificación, características, y estado actual.
     */
    override fun toString(): String {
        return "Quad(nombre=${capitalize(nombre)}, marca=$marca, modelo=$modelo, capacidadCombustible=$capacidadCombustible, combustibleActual=$combustibleActual, kilometrosActuales=$kilometrosActuales, cilindrada=$cilindrada, tipo=$tipo)"
    }
}