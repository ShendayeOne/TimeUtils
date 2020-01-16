package com.sy.timeutis;

import android.content.Context;

public class NBUtilResource {
    /**
     * 获取资源ID
     * @param context 上下文对象
     * @param defType 资源类型
     * @param name 资源名称
     * @return
     */
    public static int getId(Context context, String defType, String name){

        int id=0;

        try {

            id = context.getResources().getIdentifier(name, defType, context.getPackageName());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }
}
