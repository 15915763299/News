# 新闻客户端
* 使用MVVM框架

### MVC、MVP、MVVM
* MVC Model是实体类，View是视图，Controller是Activity、Fragment
* MVP Model是自定义的数据获取类，View是视图+Activity、Fragment，Presenter持有View、Model的对象。
使用时我们会先将MVP所需要的功能抽象成三个接口，再去实现，View一般由Activity本身实现。
最终我们从Activity中调用Presenter对象的各个接口，再由Presenter来控制View与Model完成整个操作。
* MVVM Model也是数据获取类，View也是视图+Activity、Fragment，ViewModel 用于将 Model 和 View 进行关联，
我们可以在 View 中通过 ViewModel 从 Model 中获取数据；当获取到了数据之后，会通过自动绑定，比如 DataBinding，来将结果自动刷新到界面上。
* MVP到MVVM主要是引入的DataBinding，MVP的interface会产生较多代码，DataBinding可以省去很多代码。
* [不错的参考文章](https://www.jianshu.com/p/aeb7dad34f05)

### 应用开发注意事项：
* 网络永远是不稳定的
* 电量总是不多的
* 手机运行环境是多样的
* 编码遵循SOLID原则
* 视图、数据、逻辑分离
* 模块化、层次化、控件化

### 应用层次
业务模块、common、网络、Base、JNI库、Android操作系统、硬件

### 新增知识点
* 自定义config.gradle，方便libs版本管理
* 自建私库（不用nexus，它不能放ios库，用Artifactory），下载包的速度大大提升，还可以存自己的开发包，方便统一规范。
* 使用 CoordinatorLayout 的 layout_behavior 属性控制动画
* 组件化开发（测试独立打包，发布合体），使用 [CC框架](https://github.com/luckybilly/CC) （总线原理）

