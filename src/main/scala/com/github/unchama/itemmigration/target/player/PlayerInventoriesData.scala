package com.github.unchama.itemmigration.target.player

import cats.effect.IO
import com.github.unchama.itemmigration.ItemMigration.ItemConversion
import com.github.unchama.itemmigration.{ItemMigrationTarget, MigrationHelper}
import org.bukkit.entity.Player

/**
 * アイテムマイグレーションを行う対象としてのプレーヤーインベントリを表すオブジェクト
 */
class PlayerInventoriesData(player: Player) extends ItemMigrationTarget[IO] {

  override def runMigration(conversion: ItemConversion): IO[Unit] = IO {
    MigrationHelper.convertEachStackIn(player.getInventory)(conversion)
    MigrationHelper.convertEachStackIn(player.getEnderChest)(conversion)
  }

}
