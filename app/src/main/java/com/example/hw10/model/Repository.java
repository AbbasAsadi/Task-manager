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

    public User getUser(Long userId) {
        return mUserDao.queryBuilder()
                .where(UserDao.Properties.MId.eq(userId))
                .unique();
    }

    public Task getTask(Long taskId) {
        return mTaskDao.queryBuilder()
                .where(TaskDao.Properties.MId.eq(taskId))
                .unique();

    }

    public List<Task> getTaskList(Long userId) {
        return mTaskDao.queryBuilder()
                .where(TaskDao.Properties.UserId.eq(userId))
                .list();
    }

    public List<Task> getTaskList(Long userId, Integer stateValue) {
        return mTaskDao.queryBuilder()
                .where(TaskDao.Properties.UserId.eq(userId))
                .where(TaskDao.Properties.MState.eq(stateValue))
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

    public void deleteAllTask(Long userId) {
        mTaskDao.queryBuilder()
                .where(TaskDao.Properties.UserId.eq(userId))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();

    }

    public void updateTask(Task task) throws TaskNotExistException {
        mTaskDao.update(task);
    }
}
