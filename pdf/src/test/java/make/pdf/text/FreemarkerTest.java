package make.pdf.text;

import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.*;

public class FreemarkerTest {

    @Test
    public void test() throws IOException {
        URLTemplateLoader loader = new RemoteTemplateLoader();
        Configuration cfg = new Configuration();
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateLoader(loader);

        // templateName.ftl为要装载的模板
        Template template = cfg.getTemplate("蜡笔小新");
        File outHtmFile = File.createTempFile("test", ".html",
                new File("F:\\IDEA\\tmp"));

        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outHtmFile)));
        template.dump(out);
        out.close();
    }
}
