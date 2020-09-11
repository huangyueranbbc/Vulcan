package org.huangyr.project.vulcan.status;

public enum RunnerStatus {

    //  启动中
    STARTING("starting"),
    // 运行中
    RUNNING("running"),
    // 停止
    DEAD("dead"),
    // 已移除
    REMOVED("removed");

    private String statusName = null;

    RunnerStatus(String statusName) {
        this.statusName = statusName;
    }
}
