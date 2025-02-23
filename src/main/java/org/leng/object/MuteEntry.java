package org.leng.object;

public class MuteEntry {
    private String target;
    private String staff;
    private String reason;
    private long time; 

    public MuteEntry(String target, String staff, String reason) {
        this.target = target;
        this.staff = staff;
        this.reason = reason;
        this.time = System.currentTimeMillis(); // 初始化时间戳
    }

    public String getTarget() {
        return target;
    }

    public String getStaff() {
        return staff;
    }

    public String getReason() {
        return reason;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return target + ":" + staff + ":" + reason;
    }
}