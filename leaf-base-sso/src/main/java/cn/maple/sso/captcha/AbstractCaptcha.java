package cn.maple.sso.captcha;

import cn.maple.sso.enums.GXRandomType;
import cn.maple.sso.utils.GXRandomUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>
 * 验证码抽象类
 * </p>
 *
 * @author britton britton@126.com
 * @since 2021-09-16
 */
@Data
@Accessors(chain = true)
public abstract class AbstractCaptcha implements ICaptcha {
    /**
     * 是否为 GIF 验证码
     */
    protected boolean gif;

    /**
     * 字体Verdana
     */
    protected Font font;

    /**
     * RGB 颜色数组
     */
    protected int[][] rgbArr;

    /**
     * 干扰量
     */
    protected int interfere = 5;

    /**
     * 干扰色默认随机
     */
    protected Color interfereColor;

    /**
     * 验证码颜色默认随机
     */
    protected Color color;

    /**
     * 验证码随机字符长度
     */
    protected int length;

    /**
     * 验证码显示宽度
     */
    protected int width;

    /**
     * 验证码显示高度
     */
    protected int height;

    /**
     * 图片后缀
     */
    protected String suffix;

    /**
     * 验证码类型
     */
    protected GXRandomType randomType;

    /**
     * 常用汉字
     */
    protected String chineseUnicode;

    /**
     * 图片验证码票据存储接口
     */
    protected ICaptchaStore captchaStore;

    /**
     * 是否忽略验证内容大小写，默认 true
     */
    protected boolean ignoreCase = true;

    @Override
    public void generate(HttpServletRequest request, OutputStream out, String ticket) throws IOException {
        String captcha = randomCaptcha();
        if (getCaptchaStore(request).put(ticket, captcha)) {
            writeImage(captcha, out);
        }
    }

    @Override
    public boolean verification(HttpServletRequest request, String ticket, String captcha) {
        String tc = getCaptchaStore(request).get(ticket);
        if (null == tc) {
            return false;
        }
        return ignoreCase ? tc.equalsIgnoreCase(captcha) : tc.equals(captcha);
    }

    /**
     * <p>
     * 内置 HttpSession 存储验证
     * </p>
     *
     * @param request 请求对象
     * @return ICaptchaStore
     */
    private ICaptchaStore getCaptchaStore(HttpServletRequest request) {
        if (null == captchaStore) {
            return new CaptchaStoreSession(request);
        }
        return captchaStore;
    }

    /**
     * <p>
     * 输出图片验证码
     * </p>
     *
     * @param captcha 验证码
     * @param out     输出流
     * @return 字符串验证码
     * @throws IOException
     */
    protected abstract String writeImage(String captcha, OutputStream out) throws IOException;

    /**
     * 产生两个数之间的随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    protected int num(int min, int max) {
        return min + GXRandomUtil.RANDOM.nextInt(max - min);
    }

    /**
     * 产生0-num的随机数,不包括num
     *
     * @param num 最大值
     * @return 随机数
     */
    protected int num(int num) {
        return GXRandomUtil.RANDOM.nextInt(num);
    }

    /**
     * 随机验证码
     */
    protected String randomCaptcha() {
        // 默认设置
        if (null == randomType) {
            randomType = GXRandomType.MIX;
        }
        if (null == font) {
            if (GXRandomType.CHINESE == randomType) {
                font = new Font("楷体", Font.PLAIN, 28);
            } else {
                font = new Font("Arial", Font.PLAIN, 32);
            }
        }
        if (null == rgbArr) {
            rgbArr = ColorType.LIVELY;
        }
        if (null == suffix) {
            suffix = gif ? "gif" : "png";
        }
        if (width < 10) {
            width = 120;
        }
        if (height < 10) {
            height = 48;
        }
        if (length < 1) {
            length = 5;
        }
        // 生成随机码
        if (GXRandomType.CHINESE == randomType) {
            return GXRandomUtil.getChinese(chineseUnicode, length);
        }
        return GXRandomUtil.getText(randomType, length);
    }
}