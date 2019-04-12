import akka.actor.typed.ActorSystem

fun main() {
//
//    val system = ActorSystem.create(HelloWorldMain.main, "hello")
//
//    system.tell(HelloWorldMain.Start("World"))
//    system.tell(HelloWorldMain.Start("Akka"))

    val poleo = ActorSystem.create(Poleo.main, "Poleo")


    poleo.tell(Iniciar("Starbucks Torre Boston"))

    repeat(30) {
        poleo.tell(Iniciar("Starbucks Torre Boston$it"))
    }


//    val a: Behavior<Int> = Behaviors.supervise<Int>(
//        Behaviors.receiveMessage {
//            if (it > 2)
//                throw RuntimeException()
//            println(it)
//            Behavior.same()
//        }
//    ).onFailure(SupervisorStrategy.restart())
//
//    val systemDeA = ActorSystem.create(a, "a")
//
//    systemDeA.tell(1)
//    systemDeA.tell(2)
//    systemDeA.tell(3)
//    systemDeA.tell(1)


//    reply.thenAccept { greeting ->
//        println("result: " + greeting.whom)
//        system.terminate()
//    }
//
//    val reply2 = AskPattern.ask(
//        system,
//        { replyTo: ActorRef<HelloWorld.Greeted> -> HelloWorld.Greet("world", replyTo) },
//        Timeout(3, TimeUnit.SECONDS), system.scheduler()
//    )
//
//    reply2.thenAccept { greeting ->
//        println("result: " + greeting.whom)
//        system.terminate()
//    }
}
