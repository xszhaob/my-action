package make.pdf.text;

import freemarker.cache.URLTemplateLoader;

import java.net.MalformedURLException;
import java.net.URL;

public class RemoteTemplateLoader extends URLTemplateLoader {

    @Override
    protected URL getURL(String name) {
        try {
            System.out.println(name);
            // 这里是远程的内容
            return new URL("");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
