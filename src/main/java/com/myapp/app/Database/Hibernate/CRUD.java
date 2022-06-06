package com.myapp.app.Database.Hibernate;

import com.myapp.app.Database.Tables.Timev;
import com.myapp.app.Database.Tables.User;
import org.hibernate.Session;

import java.util.List;

public class CRUD {
    public static User getUser(String login) {
        return (User) Hibernate
                .getSessionFactory().
                openSession().
                createQuery("FROM User U WHERE U.login = '" + login + "'")
                .uniqueResult();
    }
    public static List<Timev> selectFromTimev() {
        return (List<Timev>) Hibernate.getSessionFactory().openSession().createQuery("from Timev ").list();
    }
    public static void createTimev(Timev timev) {
        Session session = Hibernate.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(timev);
        session.getTransaction().commit();
        session.close();
    }
}
