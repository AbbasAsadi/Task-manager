package com.example.hw10.model;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class Task {
    @Id(autoincrement = true)
    private Long mId;
    private String mName;
    @Convert(converter = StateConverter.class , columnType = Integer.class)
    private State mState;
    private String mDescription;
    private long mTime;
    private Date mDate;
    private Long userId;
    @ToOne(joinProperty = "userId")
    private User mUser;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1469429066)
    private transient TaskDao myDao;
    @Generated(hash = 814242847)
    public Task(Long mId, String mName, State mState, String mDescription,
            long mTime, Date mDate, Long userId) {
        this.mId = mId;
        this.mName = mName;
        this.mState = mState;
        this.mDescription = mDescription;
        this.mTime = mTime;
        this.mDate = mDate;
        this.userId = userId;
    }
    @Generated(hash = 733837707)
    public Task() {
    }
    public Long getMId() {
        return this.mId;
    }
    public void setMId(Long mId) {
        this.mId = mId;
    }
    public String getMName() {
        return this.mName;
    }
    public void setMName(String mName) {
        this.mName = mName;
    }
    public State getMState() {
        return this.mState;
    }
    public void setMState(State mState) {
        this.mState = mState;
    }
    public String getMDescription() {
        return this.mDescription;
    }
    public void setMDescription(String mDescription) {
        this.mDescription = mDescription;
    }
    public long getMTime() {
        return this.mTime;
    }
    public void setMTime(long mTime) {
        this.mTime = mTime;
    }
    public Date getMDate() {
        return this.mDate;
    }
    public void setMDate(Date mDate) {
        this.mDate = mDate;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    @Generated(hash = 1377221062)
    private transient Long mUser__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 59229727)
    public User getMUser() {
        Long __key = this.userId;
        if (mUser__resolvedKey == null || !mUser__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User mUserNew = targetDao.load(__key);
            synchronized (this) {
                mUser = mUserNew;
                mUser__resolvedKey = __key;
            }
        }
        return mUser;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1420854415)
    public void setMUser(User mUser) {
        synchronized (this) {
            this.mUser = mUser;
            userId = mUser == null ? null : mUser.getMId();
            mUser__resolvedKey = userId;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1442741304)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskDao() : null;
    }
}
