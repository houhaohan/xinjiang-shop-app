package com.pinet.code;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author chengshuanghui
 * @since 2022年11月20日
 */
@Data
@Accessors(chain = true)
public abstract class CodeGeneration {
    public static String author = "wlbz";
    public static String packagePath = "com.pinet.rest";
    public static String[] superEntityColumns = {"id","create_by","create_time","update_by","update_time","del_flag"};
    public static String projectPath = System.getProperty("user.dir");
    public static String moduleName = "pinet-rest";
    public static String outputDir = projectPath + "/" + moduleName + "/src/main/java";

    public abstract void execute();
}

