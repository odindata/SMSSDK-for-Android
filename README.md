# 奥丁短信验证SDK

- [奥丁SMSSDK官网](http://www.odinanalysis.com/sms.html)

## 一、集成说明

### 配置build.gradle

- 项目根目录的build.gradle

```groovy
buildscript {

    repositories {
        ...
        maven { url "http://maven.odinlk.com:58080/repository/android/" }
    }
    dependencies {
       ...
    }
}
allprojects {
    repositories {
       ...
        maven {
            url "http://maven.odinlk.com:58080/repository/android/"
        }
    }
}
```

- module工程的build.gradle


```groovy
implementation 'com.odin.sms:OdinSMS:1.0.2_beta'
```

### 权限申请

- 请在AndroidManifest中添加如下权限，并申请权限

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

### 混淆设置

- OdinSMSSDK已经做了混淆处理，再次混淆会导致不可预期的错误，请在您的混淆脚本中添加如下的配置，跳过对OdinSMSSDK的混淆操作


```groovy
-keep class com.odin.sms.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn com.odin.sms.**
-dontwarn **.R$*
```

## 二、OdinSMS的Api说明

### 初始化

```java
/**
 * 初始化操作
 *
 * @param context       context
 * @param odinAppKey    申请的APP的odinAppKey
 * @param odinAppSecret 申请的APP的odinAppSecret
 */
public static void init(@NonNull Context context, @NonNull String odinAppKey, @NonNull String odinAppSecret)
```



### 发送验证码

```java
/**
 * 请求发送验证码
 *
 * @param phoneNum 手机号码
 * @param smsTplId 短信模板ID（null：默认模板），在奥丁官网开发者中心申请
 */
public static void getSMSVerificationCode(@NonNull String phoneNum, @Nullable String smsTplId)
```



### 提交验证验证码

```java
/**
 * 提交验证验证码
 *
 * @param phoneNum         手机号码
 * @param smsSid           短信ID，请求发送验证码成功后返回的数据
 * @param verificationCode 验证码
 */
public static void submitSMSValidationCode(@NonNull String phoneNum, @NonNull String smsSid, @NonNull String verificationCode)
```



### 显示OdinSMS自定义的UI界面

```JAVA
/**
 * 显示OdinSMS自定义的UI界面
 *
 * @param context  上下文
 * @param smsTplId 短信模板ID（null：默认模板），在奥丁官网开发者中心申请
 */
public static void showOdinSMSPage(@Nullable Context context, @Nullable String smsTplId)
```



### 关闭OdinSMS自定义的UI界面

```JAVA
/**
 * 关闭OdinSMS自定义的UI界面，最好在Activity的onStop()方法中调用
 */
public static void closeOdinSMSPage() {
```



### 监听事件对象

```Java
/**
 * 需要重写OdinSMSEventHandler的相应方法
 */
new OdinSMSEventHandler() {
    /**
     * 请求发送验证码的回调事件
     * @param resultCode 结果码
     * @param smsSid     短信sid
     * @param msg        消息
     */
    @Override
    public void afterGetValidationCodeEvent(final int resultCode, final String smsSid, final String msg) {
        //获取smsSid，用于后续验证码的验证
        if (resultCode == OdinSMSEventHandler.RESULT_FAILED) {
            Toast.makeText(MainActivity.this, "发送验证码失败：" + msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 请求验证验证码的回调事件
     * @param resultCode 结果码
     * @param msg        消息
     */
    @Override
    public void afterSubmitValidationCodeEvent(final int resultCode, final String msg) {
        if (resultCode == OdinSMSEventHandler.RESULT_SUCCESS) {
            Toast.makeText(MainActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
        }
    }
};
```



### 注册回调监听事件

```java
/**
 * 注册回调监听事件
 *
 * @param eventHandler 回调事件
 */
public static void registerSMSEventHandler(OdinSMSEventHandler eventHandler)
```



### 注销回调监听事件

```java
/**
 * 注销回调监听事件
 *
 * @param eventHandler 回调事件
 */
public static void unRegisterSMSEventHandler(OdinSMSEventHandler eventHandler)
```



### 注销所有的回调监听事件

```java
/**
 * 注销所有的回调监听事件
 */
public static void unRegisterAllSMSEventHandler()
```



### 设置OdinSMS自定义的界面配置项

```java
/**
 * 设置OdinSMS自定义的界面配置项
 *
 * @param smsPageConfig 配置项信息
 */
public static void setOdinSMSPageConfig(OdinSMSPageConfig smsPageConfig)
```



### 获取OdinSMS自定义的界面配置项

```JAVA
/**
 * 获取OdinSMS自定义的界面配置项
 *
 * @return 配置项信息
 */
public static OdinSMSPageConfig getOdinSMSPageConfig()
```