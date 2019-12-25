package com.myipcdemo.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author JohnYang
 * @description:
 * @date :2019/12/24 0024 下午 3:07
 */
public class Message implements Parcelable {
    private String content;
    private Boolean isSendSuccess;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getSendSuccess() {
        return isSendSuccess;
    }

    public void setSendSuccess(Boolean sendSuccess) {
        isSendSuccess = sendSuccess;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeValue(this.isSendSuccess);
    }

    public Message() {
    }

    protected Message(Parcel in) {
        this.content = in.readString();
        this.isSendSuccess = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public void readFromParcel(Parcel parcel){
        this.content = parcel.readString();
        this.isSendSuccess = (Boolean) parcel.readValue(Boolean.class.getClassLoader());
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", isSendSuccess=" + isSendSuccess +
                '}';
    }
}
