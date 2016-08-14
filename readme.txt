1、SYComm工程主要包含两个部分：测试代码和可供其他工程引用的通用代码。
   测试代码在：app模块。
   可供其他工程引用的通用代码：sycommcomponentlib。
2、以后通用代码的第三方工程，sycommcomponentlib必须以这个工程为准。
   即第三方工程将sycommcomponent工程的sycommcomponentlib模块直接import到本工程即可。
   如果sycommcomponent工程的sycommcomponentlib模块，发生了更新，最好重新import一次。
  