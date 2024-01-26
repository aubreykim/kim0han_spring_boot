package hello.hellospring.dto;

public class CPUDTO {
    private String cpuUsage;
    private String memoryFree;
    private String memoryTotal;
    private String diskTotal;
    private String diskUsable;

    public String getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(String cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getMemoryFree() {
        return memoryFree;
    }

    public void setMemoryFree(String memoryFree) {
        this.memoryFree = memoryFree;
    }

    public String getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(String memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public String getDiskTotal() {
        return diskTotal;
    }

    public void setDiskTotal(String diskTotal) {
        this.diskTotal = diskTotal;
    }

    public String getDiskUsable() {
        return diskUsable;
    }

    public void setDiskUsable(String diskUsable) {
        this.diskUsable = diskUsable;
    }
}
