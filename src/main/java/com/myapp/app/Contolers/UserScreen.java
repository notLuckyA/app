package com.myapp.app.Contolers;



import com.myapp.app.App.Alerts;
import com.myapp.app.Main;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.IOException;

public class UserScreen {

    public Label timer;
    public ImageView imageProfile;
    public Label fioField;

    private static final int TIME_SESSION = 60 * 10;
    private static final int WARNING_MESSAGE = 60 * 5;
    //    public static final int BLOCK_LOGIN = 60 * 1;
    public String h = "Hello ";
    Timeline timeline = new Timeline ();

    @FXML
    public void toLogin(ActionEvent actionEvent) throws IOException {
        timeline.stop();
        Main.setRoot("loginScreen");
    }

    @FXML
    public void initialize(){
        timerStart();
    }

    public void timerStart(){
        int[] time = {TIME_SESSION}; //Чтобы внутри события был доступен, делаем в виде массива.
        timer.setText(convert(TIME_SESSION));

        timeline = new Timeline (
                new KeyFrame(
                        Duration.millis(1000), //1000 мс * 60 сек = 1 мин
                        ae -> {
                            time[0]--;
                            timer.setText("" + convert(time[0]));
                            if(time[0] == WARNING_MESSAGE){
                                Alerts.infoAlert("До конца сессии осталось : 5 минут.","Предупреждение","Сеанс автоматически завершится");
                            }
                            if(time[0] == 0){
                                try {
                                    Main.setRoot("loginScreen");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Alerts.infoAlert("Время активной сессии завершилось","Внимание","Сеанс автоматически завершился");
                            }
                        }
                )
        );

        timeline.setCycleCount(TIME_SESSION); //Ограничим число повторений
        timeline.play(); //Запускаем
    }

    public String convert(int sessionTime){
        long hour = sessionTime / 3600,
                min = sessionTime / 60 % 60,
                second = sessionTime;
        return String.format("%02d:%02d:%02d", hour, min, second);
    }


    public void getBio(ActionEvent actionEvent) {
    }

    public void generateReport(ActionEvent actionEvent) {
    }
}
