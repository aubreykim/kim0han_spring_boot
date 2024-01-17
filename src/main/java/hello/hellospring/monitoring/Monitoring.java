package hello.hellospring.monitoring;

import com.sun.management.OperatingSystemMXBean;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Arrays;

public class Monitoring {
    public static void main(String[] args) {
        try {

            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

            for(int i=0; i < 100; i++){
                System.out.println("===================");
                System.out.println("CPU Usage : " + String.format("%.2f", osBean.getSystemCpuLoad() * 100 )); //CPU
                System.out.println("Memory Free Space : " + String.format("%.2f", (double)osBean.getFreePhysicalMemorySize()/1024/1024/1024)); //메모리 잔여량
                System.out.println("Memory Total Space : " + String.format("%.2f", (double)osBean.getTotalPhysicalMemorySize()/1024/1024/1024  )); //전체 물리 메모리량
                System.out.println("Disk : " + Arrays.toString(getDiskSpace())); //디스크 용량 - 총 용량과 사용 가능 용량

                Thread.sleep(1000);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String[] getDiskSpace() {
        File root = null;
        try {
            root = new File("/");
            String[] list = new String[2];
            list[0] = toMB(root.getTotalSpace());
            list[1] = toMB(root.getUsableSpace());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toMB(long size) {
        return String.valueOf((int) (size / (1024 * 1024)));
    }
}