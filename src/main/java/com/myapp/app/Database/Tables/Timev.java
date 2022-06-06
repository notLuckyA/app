package com.myapp.app.Database.Tables;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "timev")
public class Timev {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtimeV", nullable = false)
    private Integer id;

    @Column(name = "loginT", length = 45)
    private String loginT;

    @Column(name = "time")
    private Instant time;

    @Column(name = "result", length = 45)
    private String result;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginT() {
        return loginT;
    }

    public void setLoginT(String loginT) {
        this.loginT = loginT;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}