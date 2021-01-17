package make.pdf.text.util.bean;

import java.util.Map;

public class PDFDynamicParam {
    private String templatePath;// 模版路径
    private String templateName;// 模板文件名(.ftl结尾)
    private String pdfPath;// PDF生成路径
    private String pdfName;// PDF文件名(.pdf结尾)
    private Map<String, Object> pdfParam;// pdf模板所需参数

    public PDFDynamicParam(String templatePath, String templateName,
                           String pdfPath, String pdfName,
                           Map<String, Object> pdfParam) {
        this.templatePath = templatePath;
        this.templateName = templateName;
        this.pdfPath = pdfPath;
        this.pdfName = pdfName;
        this.pdfParam = pdfParam;
    }


    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public Map<String, Object> getPdfParam() {
        return pdfParam;
    }

    public void setPdfParam(Map<String, Object> pdfParam) {
        this.pdfParam = pdfParam;
    }
}
