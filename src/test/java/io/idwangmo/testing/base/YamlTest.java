package io.idwangmo.testing.base;

import io.idwangmo.testing.model.config.TestConfig;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class YamlTest {

    @Test(description = "配置文件读取测试")
    public void testReadYaml() {
        Yaml yaml = new Yaml(new Constructor(TestConfig.class));
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("testing-config.yml");
        TestConfig testConfig = yaml.load(inputStream);

        log.info(testConfig.toString());

        assertThat(testConfig).isNotNull();

    }

}
