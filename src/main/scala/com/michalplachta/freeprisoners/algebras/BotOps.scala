package com.michalplachta.freeprisoners.algebras

import cats.:<:
import cats.free.Free
import com.michalplachta.freeprisoners.PrisonersDilemma._

object BotOps {
  type Strategy = Prisoner => Decision
  object Strategies {
    val alwaysBlame: Strategy = _ => Guilty
    val alwaysSilent: Strategy = _ => Silence
  }

  sealed trait Bot[A]
  final case class CreateBot(name: String, strategy: Strategy)
      extends Bot[Prisoner]
  final case class GetDecision(prisoner: Prisoner, otherPrisoner: Prisoner)
      extends Bot[Decision]

  object Bot {
    class Ops[S[_]](implicit s: Bot :<: S) {
      def createBot(name: String, strategy: Strategy): Free[S, Prisoner] =
        Free.liftF(s.inj(CreateBot(name, strategy)))

      def getDecision(prisoner: Prisoner,
                      otherPrisoner: Prisoner): Free[S, Decision] =
        Free.liftF(s.inj(GetDecision(prisoner, otherPrisoner)))
    }
  }
}