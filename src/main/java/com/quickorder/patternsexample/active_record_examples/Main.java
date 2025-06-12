package com.quickorder.patternsexample.active_record_examples;

//
//          Active Record Usage
//

public class Main {
    public static void main(String[] args) {
        User u1 = new User();
        u1.name = "markus";
        u1.email = "markus@aurelius.com";
        u1.save(); // INSERT

        System.out.println("Saved user ID: " + u1.id);

        User u2 = User.findById(u1.id);
        System.out.println("Loaded user: " + u2.name + ", " + u2.email);

        u2.name = "markus updated";
        u2.save(); // UPDATE
        System.out.println("User after update: " + User.findById(u2.id).name);

        u2.delete(); // DELETE
        System.out.println("Deleted user: " + (User.findById(u2.id) == null ? "not found" : "found"));
    }
}
