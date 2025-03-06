package org.leng.object;

public class WarnEntry {
    private String player;
    private int warnCount;
    private String reason;

    public WarnEntry(String player, int warnCount) {
        this.player = player;
        this.warnCount = warnCount;
    }

    public String getPlayer() {
        return player;
    }

    public int getWarnCount() {
        return warnCount;
    }

    public void setWarnCount(int warnCount) {
        this.warnCount = warnCount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}