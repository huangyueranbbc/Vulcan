# Vulcan 有空就写写的任务调度系统  
    1、心跳机制租约机制  
    2、使用DAG有向无环图作为任务扫描算法  
    3、NetWork网络拓扑结构设计  
    4、DFS作为优先级算法(暂定)  
    5、Socket通信通过protoBuf作为通信协议   
    6、Server和Runner使用Socket长连接进行通信，例如Runner上报心跳或Server发送关闭Runner指令
    7、使用指数退避算法实现Socket心跳重连
    8、高可用HA模块  