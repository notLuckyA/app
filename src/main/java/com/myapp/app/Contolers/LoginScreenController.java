package com.myapp.app.Contolers;

import com.myapp.app.App.Alerts;
import com.myapp.app.Database.Hibernate.CRUD;
import com.myapp.app.Database.Tables.Role;
import com.myapp.app.Database.Tables.Timev;
import com.myapp.app.Database.Tables.User;
import com.myapp.app.Main;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

public class LoginScreenController {

    public ImageView logoImage;
    public TextField loginLable;
    public PasswordField passwordLable;
    public Tooltip toolTip;
    public CheckBox checkBox;

    public static User user;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();;
    public void authorization(ActionEvent actionEvent) throws IOException {
        CRUD crud = new CRUD();
        if (!loginLable.getText().isBlank() || !passwordLable.getText().isBlank()) {
            user = crud.getUser(loginLable.getText());
            Role role = user.getRole();
            Timev timev = new Timev();
            if (loginLable.getText().equals(user.getLogin()) && passwordLable.getText().equals(user.getPassword())) {
                timev.setLoginT(loginLable.getText());
                timev.setTime(dateFormat.format(date)));
                timev.setResult("Success");
                crud.createTimev(timev);
                if (role.getId() == 1 ){
                   Main.setRoot("mainScreen");
                } else if (role.getId() == 2) {
                    Main.setRoot("hello-view");
                }
            }
        }
    }

    public void viewPass(ActionEvent actionEvent) {
        if (checkBox.isSelected()) {
            showPassword();
        } else {
            hidePassword();
        }
    }

    // Метод для появления окна с паролем
    private void showPassword() {
        Point2D p = passwordLable.localToScene(passwordLable.getBoundsInLocal().getMaxX(), passwordLable.getBoundsInLocal().getMaxY());
        toolTip.setText(passwordLable.getText());
        toolTip.show(passwordLable,
                p.getX() * 1.5,
                p.getY() * 1.5);
    }

    // Метод для сокрытия окна с паролем
    private void hidePassword() {
        toolTip.setText("");
        toolTip.hide();
    }

}