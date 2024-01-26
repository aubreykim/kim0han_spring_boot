package hello.hellospring.controller;

import com.sun.management.OperatingSystemMXBean;
import hello.hellospring.dto.CPUDTO;
import hello.hellospring.dto.HeapDTO;
import hello.hellospring.service.Prometheus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Controller
public class MonitoringController {

    private final Prometheus prometheus;

    @Autowired
    public MonitoringController(Prometheus prometheus) {
        this.prometheus = prometheus;
    }

    @GetMapping("monitoring")
    public String monitoring(Model model) throws Exception {
        // 1. 네트워크 소요 시간
        long startTime = System.currentTimeMillis();

        //Heap memory
        HeapDTO heap = new HeapDTO();
        getHeap(heap);
        model.addAttribute("heap", heap);


        //CPU 용량
        model.addAttribute("cpu", getCPU());

        //error일시 로그 읽어 와서 저장? 파일 생성?
        //readLog("2024-01-26 11:06:00", "2024-01-26 11:07:00");

        //ping test
        //String ip = "www.google.com";
        String ip = "icube.icubesystems.co.kr";
        System.out.println(pingTest(ip));


        //트랜잭션 끝난 후 시간 체크?
        long responseTime = System.currentTimeMillis() - startTime;
        model.addAttribute("responseTime", responseTime);
        System.out.println(responseTime + "milliseconds" + ", " + responseTime*0.001 + "seconds");
        return "monitoring";
    }

    // log 파일에서 특정 로직 log 가져오기
    private static String readLog(String startTime, String endTime) {
        try {
            // 시작 시간과 종료 시간을 Date 객체로 변환
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);

            // 로그 파일 읽기
            try (BufferedReader br = new BufferedReader(new FileReader("C:/workspace/git/kim0han/app-local.log"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // 각 로그 라인을 처리
                    // 로그의 시간 형식 및 위치에 따라서 적절한 파싱을 수행해야 함
                    // 이 예제에서는 시간 정보가 로그 라인의 처음에 있고, 탭(\t)으로 구분되어 있다고 가정
                    String logTimeString = line.split("\t")[0].split("\\+")[0];
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    if (isValidDateFormat(logTimeString, "yyyy-MM-dd'T'HH:mm:ss.SSS")) {
                        Date logDate = inputFormat.parse(logTimeString);
                        // 해당 시간대에 속하는 로그인지 확인
                        if ((logDate.after(startDate) || logDate.equals(startDate)) &&
                                (logDate.before(endDate) || logDate.equals(endDate))) {
                            // 시간대에 속하는 로그 출력
                            System.out.println("읽은 로그" + line);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    // 날짜 형식 체크 메서드
    private static boolean isValidDateFormat(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // 바이트 단위를 읽기 쉽게 포맷팅하는 메서드
    private static String formatBytes(long bytes) {
        long kilobytes = bytes / 1024;
        long megabytes = kilobytes / 1024;
        return megabytes + " MB";
    }

    // 바이트 단위를 읽기 쉽게 포맷팅하는 메서드
    private static void getHeap(HeapDTO heap) {
        //2. heap 사용량
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        // 힙 메모리 사용량 가져오기
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        // 힙 메모리 사용량 출력
        heap.setInitial(formatBytes(heapMemoryUsage.getInit()));
        heap.setUsed(formatBytes(heapMemoryUsage.getUsed()));
        heap.setCommitted(formatBytes(heapMemoryUsage.getCommitted()));
        heap.setMax(formatBytes(heapMemoryUsage.getMax()));
        /*
        System.out.println("Heap Memory Usage:");
        System.out.println("  Initial: " + formatBytes(heapMemoryUsage.getInit()));
        System.out.println("  Used: " + formatBytes(heapMemoryUsage.getUsed()));
        System.out.println("  Committed: " + formatBytes(heapMemoryUsage.getCommitted()));
        System.out.println("  Max: " + formatBytes(heapMemoryUsage.getMax()));
        * */
    }

    public static CPUDTO getCPU() throws Exception {
        CPUDTO cpu = new CPUDTO();
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        cpu.setCpuUsage(String.format("%.2f", osBean.getSystemCpuLoad()*100));
        cpu.setMemoryFree(String.format("%.2f", (double)osBean.getFreePhysicalMemorySize()/1024/1024/1024));
        cpu.setMemoryTotal(String.format("%.2f", (double)osBean.getTotalPhysicalMemorySize()/1024/1024/1024));
        cpu.setDiskTotal(getDiskSpace()[0]);
        cpu.setDiskUsable(getDiskSpace()[1]);
        /*
        System.out.println("===================");
        System.out.println("CPU Usage : " + String.format("%.2f", osBean.getSystemCpuLoad() * 100 )); //CPU
        System.out.println("Memory Free Space : " + String.format("%.2f", (double)osBean.getFreePhysicalMemorySize()/1024/1024/1024)); //메모리 잔여량
        System.out.println("Memory Total Space : " + String.format("%.2f", (double)osBean.getTotalPhysicalMemorySize()/1024/1024/1024  )); //전체 물리 메모리량
        System.out.println("Disk : " + Arrays.toString(getDiskSpace())); //디스크 용량 - 총 용량과 사용 가능 용량
        * */
        Thread.sleep(1000);
        return cpu;
    }

    //디스크 공간
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

    /* ping test */
    public static boolean pingTest(String ip) throws Exception {
        InetAddress pingcheck = InetAddress.getByName(ip);
        return pingcheck.isReachable(1000);
    }
}
