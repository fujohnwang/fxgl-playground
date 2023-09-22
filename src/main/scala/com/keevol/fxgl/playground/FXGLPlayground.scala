package com.keevol.fxgl.playground

import com.almasb.fxgl.achievement.{Achievement, AchievementEvent}
import com.almasb.fxgl.app.{GameApplication, GameSettings, MenuItem}
import com.almasb.fxgl.audio.Music
import com.almasb.fxgl.cutscene.Cutscene
import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.FXGL._
import com.almasb.fxgl.input.{InputModifier, MouseTrigger, TriggerListener, UserAction}
import com.almasb.fxgl.ui.FXGLButton
import com.keevol.javafx.utils.Labels
import javafx.event.EventType
import javafx.scene.control.Button
import javafx.scene.input.{KeyCode, MouseButton}
import javafx.scene.paint.{Color, Paint}
import javafx.scene.text.Font

import java.util

class Game extends GameApplication {
  override def initSettings(gameSettings: GameSettings): Unit = {
    gameSettings.setWidth(900);
    gameSettings.setHeight(600)
    gameSettings.setTitle("福强游戏demo");
    gameSettings.setVersion("0.1");
    //    gameSettings.setMainMenuEnabled(true)
    gameSettings.setGameMenuEnabled(true);
    gameSettings.setFullScreenAllowed(true);
    gameSettings.setEnabledMenuItems(util.EnumSet.of(MenuItem.EXTRA));
    gameSettings.getCredits().addAll(util.Arrays.asList(
      "Fuqiang Wang", "https://afoo.me"
    ));

    gameSettings.getAchievements.add(new Achievement("sampleAchievement", "desc", "lives", 100))
    gameSettings.getCSSList.add("style.css")
  }


  override def initGameVars(vars: util.Map[String, AnyRef]): Unit = {
    vars.put("lives", Integer.valueOf(3))
  }

  override def initUI(): Unit = {
    //    getGameScene.setBackgroundColor(Color.GRAY)
    getGameScene.setBackgroundRepeat("bg/bug_elephant.jpg")

    val label = FXGL.getUIFactoryService.newText("lives in the game: 3", Color.RED, 111)
    //    label.setFont(Font.font(111))
    label.setTranslateX(300)
    label.setTranslateY(300)
    label.textProperty().bind(FXGL.getWorldProperties.intProperty("lives").asString())
    FXGL.addUINode(label)

    val button = new FXGLButton("notify demo")
    button.setFocusTraversable(false)
    button.setOnAction(_ => FXGL.getNotificationService.pushNotification("demo message"))
    FXGL.addUINode(button, FXGL.getSettings.getWidth - 250, FXGL.getSettings.getHeight - button.getHeight - 100)

  }

  override def initInput(): Unit = {
    FXGL.onKeyDown(KeyCode.A, "左", () => FXGL.inc("lives", 1))
    FXGL.onKeyDown(KeyCode.D, "右", () => FXGL.inc("lives", -1))
    FXGL.onKeyDown(KeyCode.W, "Up", () => {
      FXGL.inc("lives", 10)
      FXGL.play("drop.wav")
    })
    FXGL.onKeyDown(KeyCode.S, "Down", () => {
      FXGL.inc("lives", -10)
      FXGL.play("drop.wav")
    })
    // after import com.almasb.fxgl.dsl.FXGL._, code can be simplified.
    onKeyUp(KeyCode.N, "push notification", () => getNotificationService.pushNotification("demo message with hotkey pressed."))

    onKeyDown(KeyCode.C, () => {
      val lines = getAssetLoader().loadText("cutscene1.txt");

      var cutscene = new Cutscene(lines);

      getCutsceneService().startCutscene(cutscene);
    })

    onBtnDown(MouseButton.PRIMARY, ()=>{
      println(s"left button clicked, ${getInput.getMouseXUI}:${getInput.getMouseYUI}")
    })
    onBtnDown(MouseButton.SECONDARY, ()=> {
      println("右键点击")
    })
  }

  override def initGame(): Unit = {
    FXGL.loopBGM("bg.mp3") // as convention, music file is under /assets/music path.
    FXGL.getEventBus.addEventHandler(AchievementEvent.ACHIEVED, (e: AchievementEvent) => getNotificationService.pushNotification(s"${e.getAchievement.getName} is done! ✅"))
  }
}

object FXGLPlayground {
  def main(args: Array[String]): Unit = {
    GameApplication.launch(classOf[Game], args)
  }
}



