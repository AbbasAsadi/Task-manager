package com.example.hw10.model;

import com.example.hw10.exception.TaskNotExistException;
import com.example.hw10.exception.UserNotExistException;
import com.example.hw10.green_dao.GreenDaoApplication;

import java.util.List;

public class Repository {
    public static Repository sRepository;
    private DaoSession mDaoSession;
    private UserDao mUserDao;
    private TaskDao mTaskDao;

    private Repository() {
        mDaoSession = GreenDaoApplication.getInstance().getDaoSession();
        mTaskDao = mDaoSession.getTaskDao();
        mUserDao = mDaoSession.getUserDao();
        /*User user = new User();
        user.setMUserName("admin");
        user.setMPassword("123");
        mUserDao.insert(user);*/
    }

    public static Repository getInstance() {
        if (sRepository == null) {
            sRepository = new Repository();
        }
        return sRepository;
    }

    public List<User> getUserList() {
        return mUserDao.loadAll();
    }

    public User getUser(String userName) {
        return mUserDao.queryBuilder()
                .where(UserDao.Properties.MUserName.eq(userName))
                .unique();
    }

    public User getUser(Long id) {
        return mUserDao.queryBuilder()
                .where(UserDao.Properties.MId.eq(id))
                .unique();
    }

    public Task getTask(Long id) {
        return mTaskDao.queryBuilder()
                .where(TaskDao.Properties.MId.eq(id))
                .unique();

    }

    public List<Task> getTaskList(Long id) {
        return mTaskDao.queryBuilder()
                .where(TaskDao.Properties.UserId.eq(id))
                .list();
    }

    public List<Task> getTaskList(Long id, State state) {
        return mTaskDao.queryBuilder()
                .where(TaskDao.Properties.UserId.eq(id))
                .where(TaskDao.Properties.MState.eq(state))
                .list();
    }

/*    public int getPosition(UUID uuid) {
//        return mCrimes.indexOf(getCrime(uuid));
        List<Task> tasks = getTaskList();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(uuid))
                return i;
        }

        return 0;
    }*/

    public void insertTask(Task task) {
        mTaskDao.insert(task);
    }

    public void insertUser(User user) {
        mUserDao.insert(user);
    }

    public void deleteUser(User user) throws UserNotExistException {
        mUserDao.delete(user);
    }

    public void deleteTask(Task task) throws TaskNotExistException {
        mTaskDao.delete(task);
    }

    public void deleteAllTask(Long id) {
        mTaskDao.queryBuilder()
                .where(TaskDao.Properties.UserId.eq(id))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();

    }

    public void updateTask(Task task) throws TaskNotExistException {
        mTaskDao.update(task);
    }
}
