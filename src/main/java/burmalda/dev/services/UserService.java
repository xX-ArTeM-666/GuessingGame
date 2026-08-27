package burmalda.dev.services;


import burmalda.dev.TransactionHelper;
import burmalda.dev.User;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final SessionFactory sessionFactory;
    private final TransactionHelper helper;

    public UserService(SessionFactory sessionFactory, TransactionHelper helper) {
        this.sessionFactory = sessionFactory;
        this.helper = helper;
    }

    public User saveUser(User user){
        return helper.executeInTransaction(session -> {
            session.persist(user);
            return user;
        });
    }

    public void deleteUser(Long id){
        helper.executeInTransaction(session -> {
            User user = session.find(User.class, id);
            session.remove(user);
        });
    }

    public Optional<User> findByUserName(String name){
        return helper.executeInTransaction(session -> {
            return session.createQuery("from User where userName = :name", User.class).setParameter("name", name).uniqueResultOptional();
        });
    }

    public User incrementScore(String name){
        return helper.executeInTransaction(session -> {
            User user = session.createQuery("from User where userName = :name", User.class).setParameter("name", name).uniqueResult();
            user.setCount(user.getCount() + 1);
            session.merge(user);
            return user;
        });
    }

    public void updateUserName(String oldName, String newName){
        helper.executeInTransaction(session -> {
            User user = session.createQuery("from User where userName = :name", User.class).setParameter("name", oldName).uniqueResult();
            user.setUserName(newName);
            session.merge(user);
        });
    }

    public void updateUserPassword(String name, String newPassword){
        helper.executeInTransaction(session -> {
            User user = session.createQuery("from User where userName = :name", User.class).setParameter("name", name).uniqueResult();
            user.setPassword(newPassword);
            session.merge(user);
        });
    }

    public List<User> getLeaderBoard(){
        return helper.executeInTransaction(session -> {
            return session.createQuery("from User order by count desc", User.class).setMaxResults(50).list();
        });
    }
}
