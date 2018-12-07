package com.sly.app.model;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * 作者 by K
 * 时间：on 2017/9/20 10:16
 * 邮箱 by  vip@devkit.vip
 * <p/>
 * 类用途：
 * 最后修改：
 */
public class BaseDao {
    private Realm realm;


    public BaseDao(Realm realm) {
        this.realm = realm;
    }


    /**
     * 添加(性能优于下面的saveOrUpdate（）方法)
     *
     * @param object
     * @return 保存或者修改是否成功
     */
    public boolean insert(RealmObject object) {
        try {
            realm.beginTransaction();
            realm.insert(object);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }
    /**
     * 添加(性能优于下面的saveOrUpdateBatch（）方法)
     *
     * @param list
     * @return 批量保存是否成功
     */
    public boolean insert(List<? extends RealmObject> list) {
        try {
            realm.beginTransaction();
            realm.insert(list);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            realm.cancelTransaction();
            return false;
        }
    }


}
