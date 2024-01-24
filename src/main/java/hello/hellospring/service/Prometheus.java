package hello.hellospring.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
@Service
public class Prometheus {

    public static Map<String, Object> resultMap = new HashMap<>();

    public Map<String, Object> getPrometheus() throws Exception {
        //Prometheus의 쿼리 언어 PromQL
        String prometheusUrl = "http://localhost:9090/api/v1/query";

        // 다양한 쿼리 예제
        String[] queries = {
                "up", //전체 인스턴스 업타입 'up' 메트릭
                "http_requests_total", //웹 서버의 HTTP 요청수 http_requests_total 메트릭
                "rate(login_attempts[1m])", //웹 서버의 1분 간격 로그인 시도 횟수 (login_attempts 메트릭)
                "100 - (avg(irate(node_cpu_seconds_total{mode=\"idle\"}[5m])) * 100)", //노드의 CPU 사용률 (node_cpu_seconds_total 메트릭)
                "avg(http_request_duration_seconds)", //웹 서버의 요청 지연 시간 평균 (http_request_duration_seconds 메트릭)
                "up{job=\"web-server\", instance=\"instance-1\"}", //특정 레이블 값을 가진 인스턴스의 상태 (up 메트릭의 특정 job 인스턴스)
                "avg_over_time(node_cpu_seconds_total{mode=\\\"system\\\"}[5m])", //웹 서버의 5분 동안의 평균 CPU 사용량 (node_cpu_seconds_total 메트릭)
                "histogram_quantile(0.95, sum(rate(http_request_duration_seconds_bucket[5m])) by (le))", //웹 서버의 95th 백분위 지연 시간 (http_request_duration_seconds 메트릭)
                "http_requests_total{job=\"web-server\"}", //http_requests_total 메트릭 중 job name이 web-server인 것만
                "node_cpu_seconds_total{mode=\"idle\"}",
                "sum(http_requests_total)",
                "node_filesystem_size_bytes",
                "node_network_receive_bytes",
                "node_filesystem_free_bytes ",
                "container_cpu_usage_seconds_total ",
                // 추가 쿼리
        };
        for (String query : queries) {
            URL url = new URL(prometheusUrl + "?query=" + query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println(response.toString() + ", " + responseCode);
                resultMap.put(query, response.toString());
            } else {
                System.out.println("Failed to query Prometheus: " + responseCode);
                resultMap.put("error ::: "+query, responseCode);
            }

            System.out.println(resultMap.toString());
        }
        return resultMap;
    }
}