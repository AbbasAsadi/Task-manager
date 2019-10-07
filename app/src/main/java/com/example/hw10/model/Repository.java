package com.example.hw10.model;

import com.example.hw10.green_dao.GreenDaoApplication;

import java.util.List;

public class Repository {
    public static Repository sRepository;
    private DaoSession mDaoSession;
    private UserDao mUserDao;
    private TaskDao mTaskDao;

    private Repository(){
        mDaoSession = GreenDaoApplication.getInstance().getDaoSession();
        mTaskDao = mDaoSession.getTaskDao();
        mUserDao = mDaoSession.getUserDao();
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
    public Task getTask(Long id) {
        return mTaskDao.queryBuilder()
                .where(TaskDao.Properties.UserId.eq(id))
                .unique();
    }
    public List<Task> getTaskList(Long id) {
       return mTaskDao.queryBuilder()
                .where(TaskDao.Properties.UserId.eq(id))
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
    public void insertUser(User user){
        mUserDao.insert(user);
    }
    public void deleteUser(User user) throws Exception{
        try {
            mUserDao.delete(user);
        }catch (Exception e){
            return;
        }
    }
    public void deleteTask(Task task) throws Exception {
        try {
            mTaskDao.delete(task);
        }catch (Exception e){
            return;
        }
    }
    public void deleteAllTask(Long id){
       mTaskDao.queryBuilder()
               .where(TaskDao.Properties.UserId.eq(id))
               .buildDelete();

    }
}
