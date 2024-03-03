/**
 * Representa un camión, que es una especialización de [Automovil], añadiendo la característica de su peso, que afecta a la autonomia.
 *
 * @property peso El peso del camión, el cual afectará a su autonomía.
 * @constructor Crea un camión con los parámetros especificados, heredando propiedades y funcionalidades de [Automovil].
 */
class Camion(
    nombre: String,
    marca: String,
    modelo: String,
    capacidadCombustible: Float,
    combustibleActual: Float,
    kilometrosActuales: Float,
    esHibrido: Boolean,
    private val peso: Float
) : Automovil(nombre, marca, modelo, capacidadCombustible, combustibleActual, kilometrosActuales, esHibrido) {

    init {
        require(peso in 1000.0f..10000.0f) { "Un camión debe tener un peso entre 1000kg y 10000kg." }
    }

    companion object {
        const val KM_POR_LITRO = 6.25f //100km / 16l = 6.25 El equivalente de lo que gasta por solo 1 litro de combustible.

        var condicionBritanica: Boolean = false
            private set

        /**
         * Actualiza el estado de la condición de Britania (conducción Británica).
         */
        fun cambiarCondicionBritanica(nuevaCondicion: Boolean) {
            condicionBritanica = nuevaCondicion
        }
    }

    /**
     * Calcula y devuelve la reducción total que será añadida a la autonomia. Cada 1000kg se
     * le reducirá por 0.2.
     *
     * @return La reducción total por el peso que será añadida a la autonomía.
     */
    private fun calcularReduccionPeso(): Float {
        val divPeso = (peso / 1000).toInt()
        val reducPeso = 0.2 * divPeso
        return reducPeso.toFloat()
    }

    /**
     * Calcula y devuelve la autonomía del camión en kilómetros. Si el camión es eléctrico,
     * se ajusta la eficiencia de combustible restando el ahorro eléctrico.
     *
     * @return La autonomía del automóvil en kilómetros como [Float].
     */
    override fun calcularAutonomia(): Float {
        val reducPeso = calcularReduccionPeso()
        return if (esHibrido)
            ((combustibleActual * (KM_POR_LITRO + AHORRO_ELECTRICO)) * reducPeso).redondear(2)
        else
            ((combustibleActual * KM_POR_LITRO) * reducPeso).redondear(2)
    }

    /**
     * Actualiza la cantidad de combustible actual basado en la distancia recorrida, considerando la eficiencia de
     * combustible del vehículo.
     *
     * @param distanciaReal La distancia real recorrida por el vehículo.
     */
    override fun actualizaCombustible(distanciaReal: Float) {
        if (esHibrido) {
            val combustibleGastado = ((distanciaReal / (KM_POR_LITRO + AHORRO_ELECTRICO)) * calcularReduccionPeso())
            combustibleActual -= combustibleGastado.redondear(2)
        }
        else {
            val combustibleGastado = ((distanciaReal / KM_POR_LITRO) * calcularReduccionPeso())
            combustibleActual -= combustibleGastado.redondear(2)
        }
    }

    /**
     * Devuelve una cadena de texto con la información actual de los kilómetros recorridos del vehículo y su
     * combustible actual.
     *
     * @return Una cadena de texto que representa la información del vehículo.
     */
    override fun obtenerInformacion(): String {
        return "${capitalize(nombre)} Camión(km = ${kilometrosActuales.redondear(2)}, combustible = ${combustibleActual.redondear(2)} L)"
    }

    /**
     * Sobrescribe el método toString de la clase [Automovil] para proporcionar una representación en cadena de texto
     * específica del camión, incluyendo su peso además de los detalles heredados de Vehiculo.
     *
     * @return Una cadena de texto que representa al automóvil.
     */
    override fun toString(): String {
        return "Camión(nombre=${capitalize(nombre)}, marca=$marca, modelo=$modelo, capacidadCombustible=$capacidadCombustible, combustibleActual=$combustibleActual, kilometrosActuales=$kilometrosActuales, esElectrico=$esHibrido, peso=$peso)"
    }
}