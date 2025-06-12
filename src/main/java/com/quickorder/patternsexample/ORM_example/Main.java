package com.quickorder.patternsexample.ORM_example;

public class Main {
    public static void main(String[] args) {

        //
        //          ORM Usage
        //

        SessionFactory sessionFactory = new Configuration()
                .configure() // hibernate.cfg.xml by default
                .addAnnotatedClass(User.class)
                .buildSessionFactory();

        // Створення
        User user = new User();
        user.setName("markus");
        user.setEmail("markus@markus.com");

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(user); // INSERT
        session.getTransaction().commit();
        session.close();

        // Читання
        session = sessionFactory.openSession();
        User found = session.get(User.class, user.getId()); // SELECT
        session.close();
        System.out.println("Found user: " + found.getName());

        // Оновлення
        session = sessionFactory.openSession();
        session.beginTransaction();
        found.setEmail("markus.new@markus.com");
        session.merge(found); // UPDATE
        session.getTransaction().commit();
        session.close();

        // Видалення
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.remove(found); // DELETE
        session.getTransaction().commit();
        session.close();

        sessionFactory.close();
    }
}
