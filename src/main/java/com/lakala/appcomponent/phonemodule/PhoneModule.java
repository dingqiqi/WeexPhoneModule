package com.lakala.appcomponent.phonemodule;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

/**
 * 获取联系人打电话
 * Created by dingqq on 2018/4/24.
 */

public class PhoneModule extends WXModule implements IPhoneModule {

    private JSCallback mJsCallback;

    @JSMethod
    @Override
    public boolean getContactsMessage(JSCallback callback) {
        mJsCallback = callback;

        Activity activity = (Activity) mWXSDKInstance.getContext();

        if (activity != null) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            activity.startActivityForResult(intent, 0x01);

            return true;
        }


        return false;
    }

    @JSMethod
    @Override
    public boolean callTelephone(final String phoneNum) {

        if (TextUtils.isEmpty(phoneNum)) {
            return false;
        }

        final Context context = mWXSDKInstance.getContext();

        if (context == null) {
            return false;
        }

        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            ContentResolver resolver = mWXSDKInstance.getContext().getContentResolver();
            Uri uri = data.getData();
            if (uri != null) {
                Cursor cursor = resolver.query(uri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
//                    String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                    cursor.close();

                    cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                            null,
                            null);

                    if (cursor != null) {
                        cursor.moveToFirst();
                        String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        cursor.close();

                        if (mJsCallback != null) {
                            mJsCallback.invoke(number);
                            mJsCallback = null;
                        }

                    }
                }

            }

        }

    }

}
