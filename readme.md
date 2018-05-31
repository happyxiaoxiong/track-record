common模块：公共类
dao模块：数据库操作类（使用的mybatis），依赖与data模块
data模块：model类，依赖common模块
env：开发和测试环境配置文件，生产环境配置在track-record-env部署文档中
plugin模块：使用到的插件类，包括ffmpeg、hadoop、lucene，依赖common模块
service模块：业务处理模块，依赖data和dao模块
webapi模块：api接口和任务模块
