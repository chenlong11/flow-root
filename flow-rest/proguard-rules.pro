-forceprocessing
#不做压缩（删除注释、未被引用代码）
-dontshrink
#不做优化（变更代码实现逻辑）
-dontoptimize
#确定统一的混淆类的成员名称来增加混淆
-useuniqueclassmembernames
#混淆类名之后，对使用Class.forName('className')之类的地方进行相应替代
-adaptclassstrings
#混淆时不生成大小写混合的类名，默认是可以大小写混合
-dontusemixedcaseclassnames
-dontpreverify
-ignorewarnings
-keepdirectories
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keeppackagenames
-keepparameternames

-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}
-keepclassmembers,allowoptimization enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class **.domain.** {
    public private protected <fields>;
    public <methods>;
}

-keepclassmembers  class **.controller.** {
    *;
}

-keepclassmembers  class **.mapper.** {
    *;
}


-keepnames interface com.nuvole.flow.service.idm.** {
    *;
}
-keepnames interface com.nuvole.flow.service.orderInst.** {
    *;
}
-keepnames interface com.nuvole.flow.service.process.** {
    *;
}