import kotlin.IllegalArgumentException
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Extiende la clase [Float] para permitir el redondeo del número a un número específico de posiciones decimales.
 *
 * @param posiciones El número de posiciones decimales a las que se redondeará el valor.
 * @return Un [Float] redondeado al número de posiciones decimales especificadas.
 */
fun Float.redondear(posiciones: Int): Float {
    val factor = 10.0.pow(posiciones.toDouble()).toFloat()
    return (this * factor).roundToInt() / factor
}

/**
 * Punto de entrada del programa. Crea una lista de vehículos y una carrera, e inicia la carrera mostrando
 * los resultados al finalizar.
 */
fun main() {

    val vehiculos = creacionParticipantes()

    val carrera = Carrera("Gran Carrera de Filigranas", 1000f, vehiculos)

    println("\n*** ${carrera.nombreCarrera} ***\n")
    carrera.iniciarCarrera()

    val resultados = carrera.obtenerResultados()

    println("* Clasificación:\n")
    resultados.forEach { println("${it.posicion} -> ${it.vehiculo.nombre} (${it.vehiculo.kilometrosActuales} kms)") }

    println("\n" + resultados.joinToString("\n") { it.toString() })

    println("\n* Historial Detallado:\n")
    resultados.forEach { println("${it.posicion} -> ${it.vehiculo.nombre}\n${it.historialAcciones.joinToString("\n")}\n") }
}


/**
 *  Función que crea la lista de vehículos dependiendo del número de participantes introducido. Se ha de introducir el nombre
 *  de cada participante durante la creación de los vehículos.
 *
 *  @return Lista de vehículos con todos los participantes.
 */
fun creacionParticipantes(): List<Vehiculo> {
    val vehiculos = mutableListOf<Vehiculo>()
    print("Introduce el número de participantes: ")
    try {
        var numParticipantes = readln().toIntOrNull() ?: throw IllegalArgumentException("El valor ha de ser un número")
        println()

        val listaNombres = mutableListOf<String>()

        val numMaxParticipantes = numParticipantes

        while (numParticipantes != 0) {
            print("* Nombre del vehículo ${(numMaxParticipantes - numParticipantes) + 1} -> ")
            val nombre = readln()
            println()
            if (nombre.lowercase() in listaNombres) {
                throw IllegalArgumentException("No se permiten repetir nombres.")
            }
            listaNombres.add(nombre.lowercase())
            val vehiculo = generarVehiculo(nombre)
            if (vehiculo != null) {
                vehiculos.add(vehiculo)
            }
            numParticipantes--
            if (vehiculo is Motocicleta) {
                println("Te ha tocado una $vehiculo")
                println()
            }
            else {
                println("Te ha tocado un $vehiculo")
                println()
            }
        }

    } catch (e: IllegalArgumentException) {
        println(e)
    }

    return vehiculos

}


/**
 * Función que genera los vehículos para ser añadidos a la lista.
 *
 * @param nombre El nombre del vehículo, introducido por el usuario previamente.
 *
 * @retun El vehículo construido, cuyo tipo será aleatorio.
 */
fun generarVehiculo(nombre: String): Vehiculo? {
    val listaMarcas = listOf<String>("Toyota","Volkswagen","Seat","Citroen")
    val listaModelos = listOf<String>("XSARA","BMW","Variant","Fiesta","Focus")
    val listaCilindradas = listOf<Int>(125,250,400,500,750,900,1000)

    val eleccion = (1..4).random()

    when (eleccion) {
        1 -> {
            val capacidadCombustible = ((30..60).random()).toFloat()
            val esHibrido = eleccionHibrido()
            val marca = eleccionMarca(listaMarcas)
            val modelo = eleccionModelo(listaModelos)
            val combustibleActual = calculadorCombustible(capacidadCombustible)
            val vehiculo = Automovil(nombre = nombre.lowercase(),
                                     marca = marca,
                                     modelo = modelo,
                                     capacidadCombustible = capacidadCombustible,
                                     combustibleActual = combustibleActual,
                                     kilometrosActuales = 0.0f,
                                     esHibrido = esHibrido)
            return vehiculo
        }
        2 -> {
            val capacidadCombustible = ((15..30).random()).toFloat()
            val combustibleActual = calculadorCombustible(capacidadCombustible)
            val marca = eleccionMarca(listaMarcas)
            val modelo = eleccionModelo(listaModelos)
            val vehiculo = Motocicleta(nombre = nombre.lowercase(),
                                       marca = marca,
                                       modelo = modelo,
                                       capacidadCombustible = capacidadCombustible,
                                       combustibleActual = combustibleActual,
                                       kilometrosActuales = 0.0f,
                                       cilindrada = (listaCilindradas[0]..listaCilindradas[6]).random())
            return vehiculo
        }
        3 -> {
            val capacidadCombustible = ((90..150).random()).toFloat()
            val esHibrido = eleccionHibrido()
            val combustibleActual = calculadorCombustible(capacidadCombustible)
            val vehiculo = Camion(nombre = nombre.lowercase(),
                                  marca = "",
                                  modelo = "",
                                  capacidadCombustible = capacidadCombustible,
                                  combustibleActual = combustibleActual,
                                  kilometrosActuales = 0.0f,
                                  esHibrido = esHibrido,
                                  peso = ((1000..10000).random()).toFloat())
            return vehiculo
        }
        4 -> {
            val capacidadCombustible = ((20..40).random()).toFloat()
            val tipoQuad = eleccionTipoQuad()
            val combustibleActual = calculadorCombustible(capacidadCombustible)
            val vehiculo = Quad(nombre = nombre.lowercase(),
                                marca = "",
                                modelo = "",
                                capacidadCombustible = capacidadCombustible,
                                combustibleActual = combustibleActual,
                                kilometrosActuales = 0.0f,
                                cilindrada = (listaCilindradas[0]..listaCilindradas[6]).random(),
                                tipo = tipoQuad)
            return vehiculo
        }
    }
    return null
}


/**
 * Función de apoyo que pone la primera letra de cada palabra de un String en mayúscula.
 */
fun capitalize(str: String): String {
    return str.trim().split("\\s+".toRegex()).joinToString(" ") { it.capitalize() }
}


/**
 * Función que ayuda a elegir si un vehículo será híbrido o no.
 */
fun eleccionHibrido(): Boolean {
    val elecHibrido = (1..2).random()
    return elecHibrido == 1
}


/**
 * Función que ayuda a elegir el tipo que tendrá el Quad al ser creado.
 */
fun eleccionTipoQuad(): Quad.TipoQuad {
    val elecTipo = (1..3).random()
    return if (elecTipo == 1) {
        Quad.TipoQuad.LIGERO
    }
    else if (elecTipo == 2) {
        Quad.TipoQuad.NO_LIGERO
    }
    else {
        Quad.TipoQuad.ESPECIAL
    }
}


/**
 * Función que decide la cantidad de combustible que el vehículo tendrá al ser creado.
 */
fun calculadorCombustible(capacidad: Float): Float {
    val porcentaje = (20..100).random()
    val combustible = (capacidad * porcentaje) / 100
    return combustible.redondear(2)
}


/**
 * Función que ayudará a elegir la marca que tendrá el vehículo dentro de la lista de marcas.
 */
fun eleccionMarca(listaMarcas: List<String>): String {
    var eleccionMarca = listaMarcas.indices.random()
    return if (eleccionMarca != 0) {
        listaMarcas[eleccionMarca--]
    }
    else {
        listaMarcas[eleccionMarca]
    }
}


/**
 * Función que ayudará a elegir el modelo que tendrá el vehículo dentro de la lista de modelos.
 */
fun eleccionModelo(listaModelos: List<String>): String {
    var eleccionModelos = listaModelos.indices.random()
    return if (eleccionModelos != 0) {
        listaModelos[eleccionModelos--]
    }
    else {
        listaModelos[eleccionModelos]
    }
}

/**
 * RESUMEN DE TODO LO ACTUALIZADO:
 * PARTE 1:
 * - La creación de Camión y Quad no ha habido problemas, funciona todo en orden.
 * - Se ha creado el generador de participantes correctamtente con la función creaciónParticipantes,
 *   ademas de varias funciones complementarias para que los participantes puedan ser creados correctamente
 *   como generarVehiculo, que genera el vehículo del participante en sí, eleccionHibrido que ayuda a elegir
 *   si el vehículo será híbrido o no, eleccionTipoQuad que realiza lo mismo para los diferentes tipos de Quad,
 *   y eleccionMarca/eleccionModelo que realizan lo mismo para las listas de Marcas y Modelos por defecto que
 *   se pueden modificar en cualquier momento para añadir más o menos. calculadorCombustible calcula el porcentaje
 *   de combustible que tendrá el vehículo elegido al empezar, entre un 20% y 100% de su capacidad.
 * PARTE 2:
 * - Creación de la función de clase clasificacionParcial exitosa, utilizando un contador interno para poner en
 *   orden a cada vehículo, y añadiendo una constante a companion object de cada cuantas rondas ha de suceder la
 *   clasificación parcial, el método obtenerInformacion de cada clase de vehículo ha sido modificado y sobreescrito
 *   para ser usado con clasificacionParcial, enseñando la información requerida.
 * - La propiedad de las posiciones y las funciones relacionadas con ellas han sido eliminadas, las posiciones finales
 *   son determinadas durante la obtención de resultados, ordenando todos los participantes por su cantidad de kilómetros
 *   actuales.
 * - Las paradas de repostaje ahora se contabilizan con una lista de pares que incluye cada vehículo junto con su total de
 *   paradas de repostaje durante la carrera.
 * - Ahora todos los vehículos pueden llegar a la meta y la carrera solo finalizará cuando todos hayan pasado la meta,
 *   avisando cada vez que un vehículo pase la meta obteniendo un estado de 'ganador'. Para esto he creado una lista auxiliar
 *   que empieza igual que la lista de participantes, pero va menguando conforme los vehiculos cruzan la meta, hasta que lleguen
 *   todos y pueda finalizar la carrera.
 * - Ahora pueden ocurrer de 0 a 3 filigranas y estas pueden quitar una cantidad de kilometros entre 10 y 50, sin embargo, tras
 *   varios tests, esto causa que la carrera sea interminable en la mayoria de los casos ya que las filigranas pueden llegar a quitar
 *   mucho, y aun mas si son varias. Pero si lo quitamos deberia de funcionar correctamente.
 */