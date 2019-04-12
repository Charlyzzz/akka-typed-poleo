import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.PreRestart
import akka.actor.typed.RestartSupervisorStrategy
import akka.actor.typed.javadsl.Behaviors
import java.time.Duration


object Poleo {

    val deTienda: Behavior<MensajeDePoleo> = Behaviors.setup { setupContext ->
        setupContext.self.tell(Arrancar)
        deTiendaConPaso(1)
    }

    fun deTiendaConPaso(pasoActual: Int): Behavior<MensajeDePoleo> = Behaviors.receive { ctx, mensaje ->
        when (mensaje) {
            Arrancar -> {
                ctx.log.info("Iniciando pasos")
                ctx.spawn(paso(1, ctx.self), "paso1")
                Behaviors.same()
            }
            PasoRealizado -> {
                if (pasoActual == 3) {
                    ctx.log.info("Poleo terminado")
                    Behaviors.stopped()
                } else {
                    val nuevoPaso = pasoActual + 1
                    ctx.spawn(paso(nuevoPaso, ctx.self), "paso$nuevoPaso")
                    deTiendaConPaso(nuevoPaso)
                }
            }
        }
    }


    fun paso(numeroPaso: Int, replyTo: ActorRef<MensajeDePoleo>): Behavior<IniciarPaso> =
        Behaviors.supervise<IniciarPaso>(
            Behaviors.setup { setupContext ->
                setupContext.self.tell(IniciarPaso)
                Behaviors.receive({ ctx, _ ->
                    ctx.log.info("Iniciando paso $numeroPaso")
                    if (Math.random() > 0.5)
                        throw RuntimeException("Boom")
                    ctx.log.info("Terminando paso $numeroPaso")
                    ctx.scheduleOnce(Duration.ofSeconds(Math.random().times(3.0).toLong()), replyTo, PasoRealizado)
                    Behavior.stopped()

                }, { ctx, signal ->
                    if (signal is PreRestart)
                        ctx.log.info("Restart!")
                    Behavior.same()
                })
            }).onFailure(RestartSupervisorStrategy.restart().withLoggingEnabled(false))

    val main: Behavior<Iniciar> = Behaviors.receive { ctx, mensaje ->
        when (mensaje) {
            is Iniciar -> {
                ctx.log.info("Iniciando Poleo para ${mensaje.nombre}")
                ctx.spawn(deTienda, mensaje.nombre.replace(" ", ""))
            }
        }
        Behavior.same()
    }
}

data class Iniciar(val nombre: String)

object IniciarPaso

sealed class MensajeDePoleo
object Arrancar : MensajeDePoleo()
object PasoRealizado : MensajeDePoleo()


