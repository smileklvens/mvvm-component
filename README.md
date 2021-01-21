# libmvi 

项目整体采用：

该项目是由kotlin、androidx、协程、组件化整合，旨在帮助开发者快速搭建项目，敏捷开发：


1. 网络请求采用协程+retrofit 
2. 采用viewmodule和databing 
3. 编写基类activity实现了统一处理toolbar、dialog和异常的toast。
4. 一些常用kotlin的扩展
5. mvvm + arouter 组件化
   
----------

# android 组件化

随着移动端快产品速迭代，多人合作已成为一种标杆，如果是传统的项目都放在一个module中代码耦合严重，存在各种各样的诟病，所以组件化越来越盛行。下面我们一步步揭开组件化的神秘面纱。

### 首先我们看下整体结构

![代码结构](https://github.com/smileklvens/mvvm-component/blob/master/img/%E7%BB%84%E4%BB%B6%E4%BB%A3%E7%A0%81%E7%BB%93%E6%9E%84.png)
![组件化](https://github.com/smileklvens/mvvm-component/blob/master/img/%E7%BB%84%E4%BB%B6%E5%8C%96.png)

说明：app是壳工程，在往下是业务模块（登录，消息，用户），都依赖lib-base基础库和按需依赖util模块,provider-service模块用于组件通信和application生命周期分发。

基础库里面有业务组件都会用到的功能比如:基础类、glide、retrofit等，有些组价用到，有些用不到的工具模块，单独抽成工具模块比如libutil，libshare，可被组件按需依赖。

### 实施组件化

#### 模块划分

按项目业务抽取module和工具模块，为了方便管理，抽取config.gradle做版本统一管理，util.gradle做工具模块依赖......

#### 单独编译

在gradle.properties中声明isBuildModule，isBuildModule 为 true 时可以使每个组件独立运行, false 则可以将所有组件集成到宿主 App 中，编写业务module.gradle，每个业务组件必须依赖这个。

单独编译需要新建alone文件夹放入AndroidManifest.xml标识启动入口，关于源码控制和isBuildModule 切换详见module.gradle配置

```
if (isBuildModule.toBoolean()) {
    apply plugin: 'com.android.application'
    configSigning project
} else {
    apply plugin: 'com.android.library'
}
 sourceSets {
        main {
            if (isBuildModule.toBoolean()) {
                //独立运行
                manifest.srcFile 'src/main/alone/AndroidManifest.xml'
            } else {
                //合并到宿主
                manifest.srcFile 'src/main/AndroidManifest.xml'
                resources {
                    //正式版本时，排除alone文件夹下所有调试文件
                    exclude 'src/main/alone/*'
                }
            }
        }
    }
```



#### Application业务分发

以前的项目第三方的初始化全在 项目的application中做的，现在组件化需要单一职责，不同的三方库的初始化放到不同的业务模块，具体步骤如下：

1.新建module模块：providerservice （作为组件通信和application业务分发）,业务模块需依赖它进行通信

2.新建interface IApp ，代理生命App周期

```
interface IApp {

    fun attachBaseContext(base: Context)

    fun onCreate(base: Context)

    fun onTerminate(base: Context)
}
```

3.不同的业务模块实现IApp，并在对应AndroidManifest.xml注册

```
class LoginApp: IApp {
    override fun attachBaseContext(base: Context) {
    }

    override fun onCreate(base: Context) {
	//初始化一键登录，第三方库啥的
    }

    override fun onTerminate(base: Context) {
    //释放一些资源
    }
}

<meta-data
                android:name="com.ikang.loginmodule.LoginApp"
                android:value="ConfigAppLife" />
```

4.编写CommonManifestParser解析所有的ConfigAppLife到集合中，再编写代理类AppDelegate，代理所有模块的生命周期函数调用，最后在BaseApp相应调用。



```
  // CommonManifestParser
  val appInfo =context.packageManager.getApplicationInfo( context.packageName, PackageManager.GET_META_DATA)
            if (appInfo.metaData != null) {
                for (key in appInfo.metaData.keySet()) {
                    if (MODULE_VALUE == appInfo.metaData[key]) {
                        modules.add(parseModule(key))
                    }
                }
            }
           
   //  BaseApp      
 override fun onTerminate() {
        super.onTerminate()
        mAppDelegate.onTerminate(this)
 }
```



#### 业务组件通信

①采用原始方式

1、providerservice模块中在声明接口IService，然后在声明登陆模块暴露的功能有哪些

```
interface ILoginService : IService {
   val accountId: String
}
```

2、在登陆模块loginmodule实现ILoginService

```
class LoginServiceImpl : ILoginService {
    override val accountId: String = "张三"
}
```

3、在loginApp传入实现

```
class LoginApp: IApp {

    override fun onCreate(base: Context) {
        //传统方式通信
	     LoginServiceControl.accountService = LoginServiceImpl()
    }
       ......
```

4.现在即可在mymodule模块MeFragment中获取登陆模块的用户Id

```
LoginServiceControl.accountService.accountId
```

②采用arouter通信跟上面的类似，只不过arouter是编译期apt自动生成代码，放入到路由表map中的，然后扫描所有的dex中的指定报名的IProvider实现的

1、providerservice模块中在声明接口ILoginService ，然后在声明登陆模块暴露的功能有哪些

```
interface ILoginService :IProvider {
    val accountId: String
}
```

- 2.在登陆模块loginmodule实现ILoginService

```
@Route(path = ProviderPath.Provider.LOGIN)
class LoginServiceRouter : ILoginService {
    override val accountId: String = "张三"
	.......
}
```

3.现在即可在mymodule模块MeFragment中获取登陆模块的用户Id

```
 @Autowired
 @JvmField var loginService: ILoginService? = null
```

③基于事件总线

使用 LoacaBroadcastManage或者RxBus进行通信

#### 其他约定

- [ ] 实体类放在本身的 module 中是无法传递的，可以下沉，放到业务模块下边的providerservice中，或者在抽取一个datamodule类

- [ ] 权限管理我们将 normal 级别的权限申请都放到 Base module 中，然后在各个 module 中分别申请 dangerous 的权限 。 这样分配的好处在于当添加或移除单一模块时，隐私权限申请也 会跟随移除 ，能做到最大程度的权限解祸 。不要将权限全部转交到每个 module 中，包括普通权限的声明 ，因为这样会增加 AndroidManifest的合并检测的耗时’。

- [ ] 组件化资源冲突

  ```
  //1、AndroidMainfest 冲突问题
  AndroidMainfest 中引用了 Application 的 app:nam巳 属性 ，当出现冲 突 时， 需要使 用 tools:replace=”android:name”来声明 Application 是可被替换 的 。
  //2、依赖jar包冲突
  当包冲突出现时，可以先检查依赖报告，使用命令 gradledependencies查看依赖目录树
  //3、防止资源重复，resourcePrefix 这个值只能 限定 XML 中的资源，井不能限定图片资源，所有图片资源仍然需要手动去修改资源名 。
  android {
  resourcePrefix "纽件名”
  }
  //组件化混淆,每个module独有的引用库混淆放到各自 的 proguard-rule.pro 中 。
   defaultConfig {
       ......
       consumerProguardFiles 'proguard-rules.pro'
      }
  ```

  ### 代码部署

  多模块可以采用 git submodule\git subtree，或者上传到 公司私服maven上统一管理。









# 用法：

```
1、统一 继承子类 activity或者fragment ，常规写法不需要viewmodule和databing，用法详见 MeFragment

2、如果界面逻辑复杂需要自定义ViewModel：BaseViewModel()，
详见 LoginFragment : BaseRefreshFragment<LoginViewModule,FragmentLoginBinding>() 

3、 自定义BindingAdapter详见 ViewBindingAdapters xml一句话加载网络图片和 打开链接

```


具体详见代码

# 进一步插件化（todo）

**插件化要解决的三个核心问题：1类加载、2资源加载、3组件生命周期管理。**

* 加载插件代码

  平时编我们写的无论是java、kotlin还是Groovy，最终都会被编译成字节码，然后通过类加载器加载到内存中，在andorid平台多了一个.class悠优化为 .dex过程。

下图

![编译](https://github.com/smileklvens/mvvm-component/blob/master/img/compile.png)


android中加载一个类到内存中也是使用双亲委托机制，在 Android 虚拟机里是无法直接运行 .class 文件的，Android 会将所有的 .class 文件转换成一个 .dex 文件，并且 Android 将加载 .dex 文件的实现封装在 BaseDexClassLoader 中，而我们一般只使用它的两个子类：PathClassLoader 和 DexClassLoader。

PathClassLoade 只能加载已经安装应用的 dex 或 apk 文件，DexClassLoader 则没有此限制，可以从 SD 卡上加载包含 class.dex 的 .jar 和 .apk 文件，这也是插件化和热修复的基础，在不需要安装应用的情况下，完成需要使用的 dex 的加载。

``` 
//1、构造 DexClassLoade
ClassLoader classLoader = new DexClassLoader(dexPath, cachDexPath, solib, getClassLoader());
//2、调用方法
classLoader mLoadClassBean = classLoader.loadClass("com.jd.plugin.Bean");
        Object beanObject = mLoadClassBean.newInstance();
        Method getNameMethod = mLoadClassBean.getMethod("getName");
        getNameMethod.setAccessible(true);
        String name = (String) getNameMethod.invoke(beanObject);

```

* 加载插件资源

  Android里面说资源(除了代码)一般分为两类，一类是在/res目录，一类是在/assets目录。/res目录下的资源会在编译的时候通过aapt工具在项目R类中生成对应的资源ID，通过`resources.arsc`文件就能映射到对应资源，/res目录下可以包括/drawable图像资源，/layout布局资源，/mipmap启动器图标，/values字符串颜色style等资源。而/assets目录下会保存原始文件名和文件层次结构，以原始形式保存任意文件，但是这些文件没有资源ID，只能使用`AssetManager`读取这些文件。

![资源](https://github.com/smileklvens/mvvm-component/blob/master/img/resource.png)




把插件apk的路径添加到`addAssetPath`中，构造对应的Resources，那么就可以拿到插件里面res目录下的资源了。

```    java
 public static void addAssetPath(Context context, String apkName) {
   ...省略部分代码...
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, pluginInfos.get(apkName).getDexPath());
            sAssetManager = assetManager;
            sResources = new Resources(assetManager,
                    context.getResources().getDisplayMetrics(),
                    context.getResources().getConfiguration()); 
   
         CharSequence res = assetManager.getResourceText(id);
    }

```

* 
# 感谢 

[SmartTabLayout](https://github.com/smileklvens/SmartTabLayout)

[BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
此项目参考了众多优秀开源项目的优秀思想，整合了很多别人的代码，在此一并表示感谢。
