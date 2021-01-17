package make.pdf.text.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import make.pdf.text.util.bean.PDFDynamicParam;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class PDFGenerator {

    /**
     * 临时HTML文件，用于转PDF
     */
    private static final String TEMPORARY_DIR = "src/main/resources/temporary";
    private static final String TEMPORARY_FILE_PREFIX = "temporary";
    private static final String TEMPORARY_FILE_SUFFIX = ".html";

    /**
     * 添加字体，解决中文支持问题
     */
    private static final String SIMSUM_FILE = "src/main/resources/common/simsun.ttc";


    /**
     * 生成PDF
     */
    public static String generate(PDFDynamicParam param) throws Exception {
        // 生成html文件
        String fileUrl = createTmpHTML(param.getTemplatePath(), param.getTemplateName(), param.getPdfParam());
        // 根据html生成pdf
        createPDF1(fileUrl, param.getPdfPath() + param.getPdfName());
        // 删除临时html文件
        removeFile(fileUrl);
        return null;
    }

    /**
     * 生成html文件
     */
    private static String createTmpHTML(String templatePath, String templateName, Map<String, Object> paramMap) throws Exception {

        Configuration cfg = new Configuration();
        cfg.setDefaultEncoding("UTF-8");
        /*
         * 1.setClassForTemplateLoading(this.getClass(), "/HttpWeb");
         * 基于类路径，HttpWeb包下的framemaker.ftl文件
         * 2.setDirectoryForTemplateLoading(new File("/template"));
         * 基于文件系统,template目录下的文件
         * 3.setServletContextForTemplateLoading(request.getSession().getServletContext(), "/template");
         * 基于Servlet Context，指的是基于WebRoot下的template下的framemaker.ftl文件
         */
        cfg.setDirectoryForTemplateLoading(new File(templatePath));

        // templateName.ftl为要装载的模板
        Template template = cfg.getTemplate(templateName);

        File outHtmFile = File.createTempFile(TEMPORARY_FILE_PREFIX, TEMPORARY_FILE_SUFFIX, new File(TEMPORARY_DIR));


        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outHtmFile)));
        // 将参数输出到模版，并操作到HTML上
        template.process(paramMap, out);
        out.close();
        return outHtmFile.toString();
    }

    /**
     * 根据html生成pdf文件
     */
    private static void createPDF(String tempHtmlUrl, String pdfUrl) throws Exception {
        File htmFile = new File(tempHtmlUrl);
        File pdfFile = new File(pdfUrl);

        String url = htmFile.toURI().toURL().toString();

        OutputStream os = new FileOutputStream(pdfFile);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(url);



        ITextFontResolver fontResolver = renderer.getFontResolver();
        // 解决中文支持问题
        fontResolver.addFont(SIMSUM_FILE, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.layout();
        renderer.createPDF(os);
        os.close();
    }


    private static void createPDF2(String tempHtmlUrl, String pdfUrl) throws Exception {
        File htmFile = new File(tempHtmlUrl);
        File pdfFile = new File(pdfUrl);

        String url = htmFile.toURI().toURL().toString();

        OutputStream os = new FileOutputStream(pdfFile);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(url);

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));



        ITextFontResolver fontResolver = renderer.getFontResolver();
        // 解决中文支持问题
        fontResolver.addFont(SIMSUM_FILE, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.layout();
        renderer.createPDF(os);
        os.close();
    }


    private static void createPDF1(String tempHtmlUrl, String pdfUrl) throws Exception {
        File htmFile = new File(tempHtmlUrl);
        File pdfFile = new File(pdfUrl);

//        String url = htmFile.toURI().toURL().toString();
//
//        OutputStream os = new FileOutputStream(pdfFile);
//        ITextRenderer renderer = new ITextRenderer();
//        renderer.setDocument(url);
//        ITextFontResolver fontResolver = renderer.getFontResolver();
//        // 解决中文支持问题
//        fontResolver.addFont(SIMSUM_FILE, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//        renderer.layout();
//        renderer.createPDF(os);
//        os.close();
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(htmFile)));
//        String s = reader.readLine();
//        while (s != null) {
//            System.out.println(s);
//            s = reader.readLine();
//        }


        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

        //添加水印和页码
        PDFBuilder builder = new PDFBuilder();
        builder.bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        writer.setPageEvent(builder);
        //打开文件
        document.open();
        //html文件解析
        XMLWorkerHelper instance = XMLWorkerHelper.getInstance();
        instance.parseXHtml(writer, document,
                new FileInputStream(htmFile), StandardCharsets.UTF_8);
        //关闭
        document.close();
    }

    /**
     * 移除文件
     */
    private static void removeFile(String tempHtmlUrl) {
        File file = new File(tempHtmlUrl);
        file.delete();
    }
}
