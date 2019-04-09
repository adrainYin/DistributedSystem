package zookeeperMaster;

import java.io.Serializable;

public class UserConfig implements Serializable {

    private int userId;
    private String name;

    public UserConfig(int userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public UserConfig() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserConfig{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                '}';
    }
}
