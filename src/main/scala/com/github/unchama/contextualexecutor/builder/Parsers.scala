package com.github.unchama.contextualexecutor.builder

import com.github.unchama.contextualexecutor.builder.TypeAliases.SingleArgumentParser
import com.github.unchama.targetedeffect.EmptyEffect
import com.github.unchama.targetedeffect.TargetedEffect.TargetedEffect
import org.bukkit.command.CommandSender

object Parsers {

  import ArgumentParserScope._

  val identity: SingleArgumentParser = {
    succeedWith(_)
  }
  // TODO not safe
  val boolean: SingleArgumentParser = { args => succeedWith(args.toBoolean) }

  def nonNegativeInteger(failureMessage: TargetedEffect[CommandSender] = EmptyEffect): SingleArgumentParser =
    closedRangeInt(0, Int.MaxValue, failureMessage)

  /**
   * @return [smallEnd]より大きいか等しく[largeEnd]より小さいか等しい整数のパーサ
   */
  def closedRangeInt(
                      smallEnd: Int, largeEnd: Int,
                      failureMessage: TargetedEffect[CommandSender] = EmptyEffect): SingleArgumentParser = { arg =>
    integer(failureMessage)(arg).flatMap { parsed =>
      if ((smallEnd to largeEnd).contains(parsed.asInstanceOf[Int]))
        succeedWith(parsed)
      else
        failWith(failureMessage)
    }
  }

  def integer(failureEffect: TargetedEffect[CommandSender] = EmptyEffect): SingleArgumentParser = { arg =>
    arg.toIntOption match {
      case Some(value) => succeedWith(value)
      case None => failWith(failureEffect)
    }
  }

  def double(failureEffect: TargetedEffect[CommandSender] = EmptyEffect): SingleArgumentParser = { arg =>
    arg.toDoubleOption match {
      case Some(value) => succeedWith(value)
      case None => failWith(failureEffect)
    }
  }

  def fromOptionParser[T](fromString: String => Option[T],
                          failureMessage: TargetedEffect[CommandSender] = EmptyEffect): SingleArgumentParser = {
    fromString.andThen(_.toRight(failureMessage))
  }
}