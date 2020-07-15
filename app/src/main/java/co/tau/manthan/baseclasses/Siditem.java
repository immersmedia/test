package co.tau.manthan.baseclasses;

import androidx.annotation.NonNull;

@SuppressWarnings("WeakerAccess")
public class Siditem {
    String sid;
    String name;

    public Siditem() {
    }

    public Siditem(@NonNull String sid, @NonNull String name) {
        this.sid = sid;
        this.name = name;
    }

    @NonNull
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Siditem{" +
                "sid='" + sid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }


}
