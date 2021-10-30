package cn.maple.sso.captcha;

/**
 * <p>
 * 颜色色系分类<br>
 * http://tool.c7sky.com/webcolor/
 * </p>
 *
 * @author unknown
 * @since 2018-08-11
 */
public interface ColorType {

    /**
     * 柔和、明亮、温柔
     */
    int[][] SOFT = {{255, 255, 204}, {204, 255, 255}, {255, 204, 204}, {255, 255, 153}, {204, 204, 255}, {153, 204, 153}};

    /**
     * 活泼、快乐、有趣
     */
    int[][] LIVELY = {{255, 102, 102}, {51, 153, 204}, {0, 153, 153}, {0, 153, 51}, {255, 102, 0}, {102, 102, 153}};
}
