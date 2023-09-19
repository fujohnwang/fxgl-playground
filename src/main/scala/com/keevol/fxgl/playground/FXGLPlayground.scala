package com.keevol.fxgl.playground

import com.almasb.fxgl.app.{GameApplication, GameSettings}
import com.almasb.fxgl.audio.Music
import com.almasb.fxgl.dsl.FXGL
import com.keevol.javafx.utils.Labels
import javafx.scene.input.KeyCode
import javafx.scene.text.Font

import java.util

class Game extends GameApplication {
  override def initSettings(gameSettings: GameSettings): Unit = {
    gameSettings.setMainMenuEnabled(true)
    gameSettings.setWidth(900);
    gameSettings.setHeight(600);
    gameSettings.setTitle("福强的第一个FXGL游戏");
    gameSettings.setVersion("0.1");
  }


  override def initGameVars(vars: util.Map[String, AnyRef]): Unit = {
    vars.put("lives", Integer.valueOf(3))
  }

  override def initUI(): Unit = {
    val label = Labels.default("lives in the game: 3")
    label.setFont(Font.font(111))
    label.setTranslateX(300)
    label.setTranslateY(300)
    label.textProperty().bind(FXGL.getWorldProperties.intProperty("lives").asString())
    FXGL.addUINode(label)
  }

  override def initInput(): Unit = {
    FXGL.onKey(KeyCode.A, ()=> FXGL.inc("lives", 1))
    FXGL.onKey(KeyCode.D, ()=> FXGL.inc("lives", -1))
    FXGL.onKey(KeyCode.W, ()=> FXGL.inc("lives", 10))
    FXGL.onKey(KeyCode.S, ()=> FXGL.inc("lives", -10))
  }

  override def initGame(): Unit = {
    FXGL.loopBGM("bg.mp3") // as convention, music file is under /assets/music path.
  }
}

object FXGLPlayground {
  def main(args: Array[String]): Unit = {
    GameApplication.launch(classOf[Game], args)
  }
}