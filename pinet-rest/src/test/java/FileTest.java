import com.pinet.PinetApplication;
import com.pinet.common.file.util.OssUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @program: xinjiang-shop-app
 * @description: 文件上传测试
 * @author: hhh
 * @create: 2022-12-12 11:29
 **/
@SpringBootTest(classes = PinetApplication.class)
public class FileTest {
    @Resource
    private OssUtil ossUtil;

    @Test
    public void test() throws IOException {
        File file = new File("D:\\004c7331cb314ce29eaaad852c0dc5ae.jpg");

        FileInputStream in_file = new FileInputStream(file);

        MultipartFile multi = new MockMultipartFile("004c7331cb314ce29eaaad852c0dc5ae.jpg", in_file);

        String url =  ossUtil.upload(multi);
        System.out.println(url);

    }


}
