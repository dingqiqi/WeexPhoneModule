package com.lakala.appcomponent.phonemodule;

import com.taobao.weex.bridge.JSCallback;

/**
 * 拍照选择图片
 * Created by dingqq on 2018/4/17.
 */

public interface IPhoneModule {

    //获取联系人
    boolean getContactsMessage(JSCallback callback);

    //打电话
    boolean callTelephone(String phoneNum);

}
