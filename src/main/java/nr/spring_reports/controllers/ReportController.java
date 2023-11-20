package nr.spring_reports.controllers;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;

@Slf4j
@RestController
public class ReportController {

    // https://prosbeginner.medium.com/jasper-report-%E0%B8%89%E0%B8%9A%E0%B8%B1%E0%B8%9A%E0%B8%97%E0%B8%B3%E0%B8%87%E0%B8%B2%E0%B8%99-1-3-5ca15cddabab
    // https://github.com/4SoftwareDevelopers/demo-spring-boot-jasper/blob/main/src/main/java/com/example/demospringbootjasper/model/ReporteVentasDTO.java

    @GetMapping(value = "/", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<?> generate() {
        try (var out = new ByteArrayOutputStream()) {
            var srcPath = ResourceUtils.getFile("classpath:templates/reports/hello.jrxml").getAbsolutePath();
            var jasperReport = JasperCompileManager.compileReport(srcPath);

            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            var params = new HashMap<String, Object>();
            params.put("voucher_id", "B548-00045634");
            params.put("current_date", formatter.format(localDateTime));
            params.put("AmountPaid", new BigDecimal(259234));
            params.put("payment_method", "PAYPAL");
            params.put("parent_name", "ACC");
            params.put("child_name", "Fernando");
            // params.put("imageDir", "classpath:/static/images/");
            log.info("params: {}", params);

            var name = "Boleta.pdf";

            var destPath = ResourceUtils.getURL("src/main/resources/static/" + name).getPath();
            var jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

            JasperExportManager.exportReportToPdfFile(jasperPrint, destPath);
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);

            // return ResponseEntity.ok(out.toByteArray());
            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=%s".formatted(name))
                    .body(out.toByteArray());
        } catch (/* IOException | JRException e | */ Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

}
