package com.gilgamesh.sdkapi.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author Gilgamesh
 * @date 2023/1/30
 */
public class WaterMarkUtil {

    private static final Color color = Color.BLACK;
    private static final Font font = new Font("宋体", Font.BOLD, 26);//字体
    // 水印透明度
    private static float alpha = 0.7f;

    private static int interval = -10;

    public static void addWaterMark(String type, String srcPath, String tarPath, String waterMarkContent) throws Exception {
        if (type.equals("1")) {
            picAddWaterMark(srcPath, tarPath, waterMarkContent);
        } else {
            pdfAddWaterMark(srcPath, tarPath, waterMarkContent);
        }
    }

    private static void picAddWaterMark(String srcPICPath, String tarOICPath, String content) throws Exception {
        FileOutputStream outImgStream = new FileOutputStream(tarOICPath);
        InputStream is = null;
        try {
            String[] waterMarkContents = content.split("\\|\\|");
            // 1、源图片
            Image srcImg = ImageIO.read(new File(srcPICPath));
            // 原图宽度
            int srcImgWidth = srcImg.getWidth(null);
            // 原图高度
            int srcImgHeight = srcImg.getHeight(null);
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
            // 2、得到画笔对象
            Graphics2D g = buffImg.createGraphics();
            // 3、设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(
                    srcImg.getScaledInstance(srcImg.getWidth(null),
                            srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0,
                    null);
            // 4、设置水印旋转
            g.rotate(Math.toRadians(-30),
                    (double) buffImg.getWidth() / 2,
                    (double) buffImg.getHeight() / 2);
            // 5、设置水印文字颜色
            Color color = new Color(210, 210, 210);
            g.setColor(color);
            // 6、设置水印文字Font
            int fontSize = 36;
            Font font = new Font("宋体", Font.BOLD, fontSize);
            g.setFont(font);
            // 7、设置水印文字透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
            // 获取其中最长的文字水印的大小
            int maxLen = 0;
            int maxHigh = 0;
            String waterMarkContent = "";
            for (int i = 0; i < waterMarkContents.length; i++) {
                waterMarkContent = waterMarkContents[i];
                int fontlen = getWatermarkLength(waterMarkContent, g);
                if (fontlen >= maxLen) {
                    maxLen = fontlen;
                }
                maxHigh = maxHigh + (i + 1) * fontSize + 10;
            }
            // 文字长度相对于图片宽度应该有多少行
            int line = srcImgWidth * 2 / maxLen;
            int co = srcImgHeight * 2 / maxHigh;

            int yz = 0;
            // 填充Y轴方向
            for (int a = 0; a < co; a++) {
                int t = 0;
                for (int j = 0; j < waterMarkContents.length; j++) {
                    waterMarkContent = waterMarkContents[j];
                    int y = (j + 1) * fontSize + 10 + t;

                    // 文字叠加,自动换行叠加，注意符号
                    int tempX = -srcImgWidth / 2;
                    int tempY = -srcImgHeight / 2 + y + yz;
                    // 单字符长度
                    int tempCharLen = 0;
                    // 单行字符总长度临时计算
                    int tempLineLen = 0;
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < waterMarkContent.length(); i++) {
                        char tempChar = waterMarkContent.charAt(i);
                        tempCharLen = getCharLen(tempChar, g);
                        tempLineLen += tempCharLen;

                        // 和图片的长度进行对应的比较操作
                        if (tempLineLen >= srcImgWidth) {
                            // 长度已经满一行,进行文字叠加
                            g.drawString(sb.toString(), tempX, tempY);
                            t = t + fontSize;
                            // 清空内容,重新追加
                            sb.delete(0, sb.length());
                            tempY += fontSize;
                            tempLineLen = 0;
                        }
                        // 追加字符
                        sb.append(tempChar);
                    }
                    // 填充X轴
                    for (int z = 0; z < line; z++) {
                        // 最后叠加余下的文字
                        g.drawString(sb.toString(), tempX, tempY);
                        tempX = tempX + maxLen + XMOVE;
                    }
                }
                yz = yz + maxHigh + YMOVE;
            }
            // 9、释放资源
            g.dispose();
            // 10、生成图片
            // 输出图片
            ImageIO.write(buffImg, "png", outImgStream);

            System.out.println("PIC水印添加完成！");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != outImgStream)
                    outImgStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static void pdfAddWaterMark(String srcPdfPath, String tarPdfPath, String wmcontent) throws Exception {
        String[] waterMarkContents = wmcontent.split("\\|\\|");
        PdfReader reader = new PdfReader(srcPdfPath);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(tarPdfPath));
        BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
        com.itextpdf.text.Rectangle pageRect = null;
        PdfGState gs = new PdfGState();
        gs.setFillOpacity(0.3f);
        gs.setStrokeOpacity(0.4f);
        int total = reader.getNumberOfPages() + 1;

        JLabel label = new JLabel();
        FontMetrics metrics;
        int textH = 0;
        int textW = 0;
        label.setText("中电信数智科技有限公司");
        metrics = label.getFontMetrics(label.getFont());
        textH = metrics.getHeight();
        textW = metrics.stringWidth(label.getText());

        PdfContentByte under;

        for (int i = 1; i < total; i++) {
            pageRect = reader.getPageSizeWithRotation(i);
            under = stamper.getOverContent(i);
            under.saveState();
            under.setGState(gs);
            under.beginText();
            under.setFontAndSize(base, 24);
            under.setRGBColorFill(145, 145, 145);
            int tempw = 0;
            for (int height = interval + textH; height < pageRect.getHeight();
                 height = height + textH * 15) {
                for (int width = interval + textW; width < pageRect.getWidth() + textW * 5;
                     width = width + textW * 4 + 100) {
                    int len = 0;
                    for (String waterMarkContent : waterMarkContents) {
                        under.showTextAligned(Element.ALIGN_CENTER, waterMarkContent, width - tempw, height - textH - len * 25, 30);
                        len++;
                    }
                }
                tempw = tempw + 100;
            }
            // 添加水印文字
            under.endText();
        }
        stamper.close();
        reader.close();
        System.out.println("PDF水印添加完成！");
    }

    /**
     * 水印之间的横向间隔
     */
    private static final int XMOVE = 80;

    /**
     * 水印之间的纵向间隔
     */
    private static final int YMOVE = 80;


    private static int getCharLen(char c, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charWidth(c);
    }

    /**
     * 获取水印文字总长度
     *
     * @paramwaterMarkContent水印的文字
     * @paramg
     * @return水印文字总长度
     */
    private static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }

    public static void main(String[] args) {
        try {
            addWaterMark("2", "D:\\temp\\water\\123123.pdf", "D:\\temp\\water\\123123-WW.pdf", "仅限中电信数智科技有限公司申请云中继使用||2023-01-30||100000879126||客户经理门户");
            //addWaterMark("1", "D:\\mine\\1.png", "D:\\mine\\1-wm.png", "投标专用");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
