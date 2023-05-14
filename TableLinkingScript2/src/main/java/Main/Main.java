package Main;

import Entities.*;
import org.hibernate.Session;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args){
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        org.hibernate.SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
        Session session = sessionFactory.openSession();

        @SuppressWarnings("unchecked")
        List<Purchase> purchaseList = session.createCriteria(Purchase.class).list();

        @SuppressWarnings("unchecked")
        List<Student> studentList = session.createCriteria(Student.class).list();
        HashMap<String, Student> students = new HashMap<>();
        for (Student student : studentList) {
            students.put(student.getName(), student);
        }

        @SuppressWarnings("unchecked")
        List<Course> courseList = session.createCriteria(Course.class).list();
        HashMap<String, Course> courses = new HashMap<>();
        for (Course course : courseList) {
            courses.put(course.getName(), course);
        }

        List<LinkedPurchase> linkedPurchaseList = new ArrayList<>();

        for(Purchase purchase : purchaseList) {
            LinkedPurchase linkedPurchase = new LinkedPurchase(purchase);
            Student student = students.get(purchase.getKey().getStudentName());
            Course course = courses.get(purchase.getKey().getCourseName());

            LinkedPurchaseCompositeKey id = new LinkedPurchaseCompositeKey();
            id.setStudentId(student.getId());
            id.setCourseId(course.getId());
            linkedPurchase.setId(id);

            linkedPurchaseList.add(linkedPurchase);
        }

        session.beginTransaction();
        for(LinkedPurchase linkedPurchase : linkedPurchaseList) {
            session.merge(linkedPurchase);
        }

        session.getTransaction().commit();
        session.close();
    }
}
