package com.github.unchama.seichiassist.mebius.bukkit.repository

import cats.effect.{Fiber, IO, SyncIO}
import com.github.unchama.concurrent.{BukkitSyncIOShift, RepeatingTaskContext}
import com.github.unchama.playerdatarepository.JoinToQuitPlayerDataRepository
import com.github.unchama.seichiassist.mebius.bukkit.routines.PeriodicMebiusSpeechRoutine
import com.github.unchama.seichiassist.mebius.service.MebiusSpeechService
import org.bukkit.entity.Player

class PeriodicMebiusSpeechRoutineFiberRepository(implicit serviceRepository: JoinToQuitPlayerDataRepository[MebiusSpeechService[SyncIO]],
                                                 repeatingTaskContext: RepeatingTaskContext,
                                                 bukkitSyncIOShift: BukkitSyncIOShift)
  extends JoinToQuitPlayerDataRepository[Fiber[IO, Nothing]] {

  override protected def initialValue(player: Player): Fiber[IO, Nothing] = {
    PeriodicMebiusSpeechRoutine
      .start(player)
      .start(IO.contextShift(repeatingTaskContext))
      .unsafeRunSync()
  }

  override protected def unloadData(player: Player, r: Fiber[IO, Nothing]): IO[Unit] =
    r.cancel

}
