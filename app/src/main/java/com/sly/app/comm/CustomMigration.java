/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2017, DevKit.vip, china, qd. sd
**                          All Rights Reserved
**
**                           By(K)
**                         
**-----------------------------------版本信息------------------------------------
** 版    本: V0.1
**
**------------------------------------------------------------------------------
********************************End of Head************************************\
*/
package com.sly.app.comm;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import vip.devkit.library.Logcat;

/**
 * 文 件 名: MyMigration
 * 创 建 人: By k
 * 创建日期: 2017/10/26 17:11
 * 邮   箱:  vip@devkit.vip
 * 博   客: www.devkit.vip
 * 修改时间：
 * 修改备注： 数据库升级文件
 */
public class CustomMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        Logcat.e("oldVersion: " + oldVersion + " ; newVersion: " + newVersion);
        RealmObjectSchema personSchema = schema.get("UserInfoBean");
        Logcat.e("personSchema.getFieldNames: " + personSchema.getFieldNames() + "personSchema");
        String[] UserFieldLength = personSchema.getFieldNames().toString().split(",");
        if (oldVersion<newVersion&&UserFieldLength.length==12) {
            RealmSchema schemas = realm.getSchema();
            RealmObjectSchema personSchemas = schemas.get("UserInfoBean");
            personSchemas.addField("EmployeeLevel", String.class, FieldAttribute.REQUIRED);
            for (int i = 0; i < newVersion; i++) {
                if (oldVersion < newVersion) {
                    oldVersion++;
                }
            }
        } else  {
            for (int i = 0; i < newVersion; i++) {
                if (oldVersion < newVersion) {
                    oldVersion++;
                }
            }
        }
        //  if (oldVersion <4) {
//            RealmObjectSchema personSchema = schema.get("UserInfoBean");
        //    personSchema.addField("EmployeeLevel", String.class, FieldAttribute.REQUIRED);
          /*  schema.get("Person")
                    .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                    .addRealmObjectField("favoriteDog", schema.get("Dog"))
                    .addRealmListField("dogs", schema.get("Dog"));
            RealmObjectSchema studentSchema = schema.create("Student");
            studentSchema.addField("grade", int.class);
            studentSchema.addField("name", String.class);
            personSchema.addRealmListField("studentList", studentSchema);*/
        //     oldVersion++;
        //  }
    }
}
