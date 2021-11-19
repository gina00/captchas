package com.gina.captcha;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CaptchaApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(CaptchaApplication.class, args);
    }

    public static char[] ch = "A23456789".toCharArray();
//    public static char[] ch = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjklmnpqrstuwxyz23456789".toCharArray();
    /**
     * 生成的图片数量
     */
    @Value("${num:0}")
    private Integer num;
    /**
     * 图片中干扰线数量
     */
    @Value("${lineNum:3}")
    private Integer lineNum;
    /**
     * 图片中干扰点数量
     */
    @Value("${pointNum:15}")
    private Integer pointNum;
    /**
     * 图片中文字数量
     */
    @Value("${textLength:4}")
    private Integer textLength;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("开始生成图片，" + num + " 个");
        File file = new File("./Captchas");
        if (!file.exists()) {//如果文件夹不存在
            file.mkdir();//创建文件夹
        }

        Set<String> tSet = getTextList(num);
        BufferedImage img = null;
        FileOutputStream ss = null;
        for (String t : tSet) {
            System.out.println(t);
            img = createImage(t);
            ss = new FileOutputStream("./Captchas/" + t + ".png");
            ImageIO.write(img, "png", ss);
        }

    }

    private Set<String> getTextList(int num) {
        Set<String> stringSet = new HashSet<>();
        for (int i = 0; i < num; i++) {
            boolean add = false;
            while (!add) {
                add = stringSet.add(getText(textLength));
            }
        }
        return stringSet;

    }

    private BufferedImage createImage(String text) {
        BufferedImage img = new BufferedImage(68, 22, BufferedImage.TYPE_INT_RGB);
        //得到该图片的绘图对象
        Graphics g = img.getGraphics();
        Random r = new Random();
        Color c = new Color(255, 255, 255); //创建背景颜色
        g.setColor(c);
        //填充整个图片的颜色
        g.fillRect(0, 0, 68, 22);
        g.setColor(new Color(0, 0, 255)); //设置字体颜色
        g.setFont(new Font("Arial", Font.ITALIC, 20));// 输出的字体属性

        for (int i = 0; i < text.length(); i++) {
            g.drawString("" + text.charAt(i), (i * 15) + 3, 18);// 写什么数字，在图片.
        }

        //输出干扰线和点
        for (int i = 0; i < lineNum; i++) {
            g.drawLine(r.nextInt(68), r.nextInt(22), r.nextInt(68), r.nextInt(22));
        }
        for (int i = 0; i < pointNum; i++) {
            g.drawOval(r.nextInt(68), r.nextInt(22), 1, 1);
        }

        return img;
    }

    private String getText(int length) {
        int index, len = ch.length;
        //向图片中输出数字和字母
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            index = r.nextInt(len); //获取随机字符序号
            sb.append(ch[index]);
        }
        return sb.toString();
    }
}
