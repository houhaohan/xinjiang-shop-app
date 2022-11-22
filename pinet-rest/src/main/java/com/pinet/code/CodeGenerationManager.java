package com.pinet.code;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.pinet.core.controller.BaseController;
import com.pinet.core.entity.BaseEntity;

import java.util.Collections;

/**
 * @author chengshuanghui
 * @since 2022年11月20日
 */

public class CodeGenerationManager extends CodeGeneration {

    private String url;
    private String username;
    private String password;
    private String[] tables;

    public CodeGenerationManager(String url, String username, String password, String[] tables) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.tables = tables;
    }

    @Override
    public void execute() {
        DataSourceConfig.Builder datasourceBuilder = new DataSourceConfig.Builder(url, username, password).typeConvert(new MySqlTypeConvert() {
            @Override
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                if (("tinyint").equals(fieldType.toLowerCase())) {
                    return DbColumnType.INTEGER;
                }
                return super.processTypeConvert(globalConfig, fieldType);
            }
        });

        FastAutoGenerator.create(datasourceBuilder)
                .globalConfig(builder -> {
                    builder.author(author) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
//                            .fileOverride() // 覆盖已生成文件
                            .disableOpenDir()
                            .commentDate("yyyy-MM-dd")
                            .outputDir(outputDir); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(packagePath) // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, outputDir + "/com/pinet/rest/mapper/xml")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables)
                            .controllerBuilder()
                            .enableRestStyle()
                            .superClass(BaseController.class)
                            .entityBuilder()
                            .enableLombok()
                            .naming(NamingStrategy.underline_to_camel)
                            .columnNaming(NamingStrategy.underline_to_camel)
                            .superClass(BaseEntity.class)
                            .addSuperEntityColumns(superEntityColumns)
                            .logicDeleteColumnName("del_flag")
                            .mapperBuilder()
                            .enableBaseColumnList()
                            .enableBaseResultMap();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
