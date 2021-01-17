package make.pdf.text;

import make.pdf.text.util.PDFGenerator;
import make.pdf.text.util.bean.PDFDynamicParam;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PDFGeneratorTest {

    private static final String TEMPLATES_PATH = "src/main/resources/templates/";

    private static final String PDF_PATH = "src/main/resources/pdf/";

    @Test
    public void generateContractTest() throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("capitalMoney", "一千万元整");
        paramMap.put("lowercaseMoney", "10000000.00");
        paramMap.put("phone", "027-123456");
        paramMap.put("mobilePhone", "185****5565");
        paramMap.put("signingTime", "2018.10.21");
        paramMap.put("endTime", "2020.10.21");

        List<Map<String, String>> orderList = new ArrayList<>(1);
        Map<String, String> order = new HashMap<>();
        order.put("item", "鞋包");
//        order.put("brand", "足下");
        order.put("model", "韩风");
        order.put("unitPrice", "100");
        order.put("count", "10");
        order.put("totalMoney", "1000");
        orderList.add(order);
        paramMap.put("orderList", orderList);
        PDFDynamicParam param = new PDFDynamicParam(TEMPLATES_PATH, "contract.ftl", PDF_PATH, "contract.pdf", paramMap);
        PDFGenerator.generate(param);
        System.out.println("====生成PDF成功====");
    }

    @Test
    public void generatePdfTest() throws Exception {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("XM", "张三");
        paramMap.put("AH", "(2015)****字第0***0号");
        paramMap.put("CSKSRQ", "2016年10月31日00时00分");
        paramMap.put("KHZH", "271**********07279975");
        paramMap.put("FYMC", "****人民法院");
        paramMap.put("JSRQ", "2017-06-14");
        paramMap.put("KZZT", "1");
        paramMap.put("LCZH", "987234234");
        paramMap.put("DATE", "2017年03月24日09时39分");
        paramMap.put("CKWH", "(2015)*****字第0**20-1**0号裁定书");
        paramMap.put("SKSE", "100");
        paramMap.put("CSJSRQ", "2016年10月31日 00时00分");

        paramMap.put("KHWD", "../images/stamp.jpg");
        PDFDynamicParam param = new PDFDynamicParam(TEMPLATES_PATH, "pdfDemo.ftl", PDF_PATH, "pdfDemo.pdf", paramMap);
        PDFGenerator.generate(param);
        System.out.println("====生成PDF成功====");
    }
}
