Android image service android
============
image service android apk

### 增加 http_site.xml
创建 app/res/values/http_site.xml 文件，内容如下
```
<resources>
    <!-- http_site 设置自己的开发服务器地址 -->
    <string name="http_site">http://192.168.1.38:3000</string>
</resources>
```


使用说明
---------------------
**安装**
运行本app需要安装以下image-service maven库

先安装java版本
```
git clone https://github.com/mindpin/image-service-java-lib
cd image-service-java-lib
mvn clean install
```

然后安装android版本(android版本依赖于Java版本)
```
git clone https://github.com/mindpin/image-service-android-lib
cd image-service-android-lib
mvn clean install
```


依赖库
---------------------
* [destinyd/android-archetypes][android-archetypes]
* [mindpin/image-service-java-lib][image-service-java-lib]
* [mindpin/image-service-android-lib][image-service-android-lib]


[android-archetypes]: https://github.com/destinyd/android-archetypes
[image-service-java-lib]: https://github.com/mindpin/image-service-java-lib
[image-service-android-lib]: https://github.com/mindpin/image-service-android-lib