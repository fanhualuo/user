package com.hehe.user.verification.model;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 验证码存储实体，根据  identity+type唯一 确定一次验证
 *
 * @author xieqinghe .
 * @date 2018/3/11 下午7:48
 * @email qinghe101@qq.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCode implements Serializable {
    private static final long serialVersionUID = -2784346234234406305L;

    /**
     * 用户手机号或者邮箱
     */
    private String identity;

    /**
     * 验证码
     */
    private String code;

    /**
     * 验证码属性 注册、登录、密码重置
     */
    private Integer type;

    /**
     * 已经发送次数
     */
    private Integer count;

    /**
     *
     * 失效时间戳
     */
    private Long invalidTime;

    public enum Type {

        REGISTER(1, "注册"),
        LOGIN(2, "登录"),
        PASSWORD_RESET(3, "密码重置"),
        BINGING(4,"绑定"),
        UN_BINGING(5,"解绑"),
        REPLACE(6,"替换");

        @Getter
        @Setter
        private Integer value;
        @Getter
        @Setter
        private String display;

        Type(Integer value, String display) {
            this.value = value;
            this.display = display;
        }

        public static Type from(Integer value) {
            if (value == null) {
                return null;
            }
            for (Type type : Type.values()) {
                if (Objects.equal(value, type.value)) {
                    return type;
                }
            }
            return null;
        }
    }


}
