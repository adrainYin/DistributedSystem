import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Run {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        context.start();

       //这里使用于阻塞

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
