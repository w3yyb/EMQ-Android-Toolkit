package io.emqtt.emqandroidtoolkit.util;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * ClassName: RealmHelper
 * Desc:
 * Created by zhiw on 2017/3/29.
 */

public class RealmHelper {

    private static RealmHelper INSTANCE;

    private Realm mRealm;

    public static RealmHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RealmHelper();
        }
        return INSTANCE;
    }

    private RealmHelper() {
        try {
            mRealm = Realm.getDefaultInstance();
        } catch (Exception e) {
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
            mRealm = Realm.getInstance(config);
        }


    }

    public <T extends RealmObject> void addData(T data) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(data);
        mRealm.commitTransaction();
    }

    public <T extends RealmObject> T queryFirst(Class<T> clazz) {
        return mRealm.where(clazz).findFirst();
    }

    public <T extends RealmObject> RealmResults<T> queryAll(Class<T> clazz) {
        return mRealm.where(clazz).findAll();
    }

    public <T extends RealmObject> RealmResults<T> queryTopicMessage(Class<T> clazz, String topic) {
        return mRealm.where(clazz).equalTo("topic", topic).findAll();
    }


    public <T extends RealmObject> void delete(T data) {
        mRealm.beginTransaction();
        data.deleteFromRealm();
        mRealm.commitTransaction();
    }

    public <T extends RealmObject> void deleteAll(Class<T> clazz) {
        final RealmResults results = mRealm.where(clazz).findAll();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();

            }
        });
    }

    public <T extends RealmObject> void deleteTopic(Class<T> clazz, String topic) {
        final RealmResults results = mRealm.where(clazz)
                .equalTo("topic", topic)
                .findAll();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();

            }
        });
    }

    public Realm getRealm() {
        return mRealm;
    }
}
