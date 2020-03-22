package io.idwangmo.testing.config;

/**
 * 接口请求的类型，对应 swagger 上不同的 spec.
 *
 * @author idwangmo
 * @since 1.0
 */
public enum SpecType {

    WEB("web端接入"),
    OAUTH("OAuth2接入"),
    ADMIN("bos后台接口"),
    MOBILE("移动端接入"),
    WECHAT("租客端");

    private String value;

    SpecType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
