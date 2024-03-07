package InsightInflux.flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"InsightInflux.flux"})
public class InsightInfluxApplication {
    public static void main(String[] args) {
        SpringApplication.run(InsightInfluxApplication.class, args);
    }
}