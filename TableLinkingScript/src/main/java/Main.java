import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.management.Query;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    public static Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
    public static SessionFactory factory = metadata.getSessionFactoryBuilder().build();
    public static Session session = factory.openSession();
    public static Transaction transaction = session.beginTransaction();

    public static void main(String[] args){

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_14_3?useSSL=false&serverTimezone=UTC", "root", "123456");
            Statement statement = connection.createStatement();

            ResultSet studentNameSet = statement.executeQuery("SELECT `student_name` FROM `purchaselist`");
            List<String> studentNames = new ArrayList<>();

            while(studentNameSet.next()){
                studentNames.add(studentNameSet.getString("student_name"));
            }

            ResultSet courseNameSet = statement.executeQuery("SELECT `course_name` FROM `purchaselist`");
            List<String> courseNames = new ArrayList<>();

            while(courseNameSet.next()){
                courseNames.add(courseNameSet.getString("course_name"));
            }

            for (int i = 0; i < courseNames.size(); i++) {
                Purchase purchase = session.get(Purchase.class, new PurchaseCompositeKey(studentNames.get(i), courseNames.get(i)));
                LinkedPurchase linkedPurchase = new LinkedPurchase();

                linkedPurchase.setId(new LinkedPurchaseCompositeKey(purchase.getStudent().getId(), purchase.getCourse().getId()));

                session.persist(linkedPurchase);
            }

            transaction.commit();
            session.close();
            connection.close();
            statement.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
}