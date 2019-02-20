
lsof -i:8080

https://blog.csdn.net/holmofy/article/details/81271839


        String url=args[0];
		String name=args[1];
		String method=args[2];
		String body=args[3];
		int count=Integer.parseInt(args[4]);
		String logPath=args[5];








cd /Users/zhangxiao/Desktop/lessons/hook/





/Library/Java/JavaVirtualMachines/jdk1.8.0_191.jdk/Contents/Home/bin/java -jar target/hook-0.0.1-SNAPSHOT.jar com.agilean.lessons.hook.HookApplication https://tkb.agilean.cn/#/?viewId=33c0665d79ee4409976fbcd9aeba361d 0220-板-2个人团 GET null 10  /Users/zhangxiao/logs/result.log



java -jar target/hook-0.0.1-SNAPSHOT.jar  https://tkb.agilean.cn/#/?viewId=33c0665d79ee4409976fbcd9aeba361d 0220-板-2个人团 GET null 10  /Users/zhangxiao/logs/result.log












-》》系统吞度量要素：

  一个系统的吞度量（承压能力）与request对CPU的消耗、外部接口、IO等等紧密关联。
  
  单个reqeust 对CPU消耗越高，外部系统接口、IO影响速度越慢。
  
  系统吞吐能力越低，反之越高。

系统吞吐量几个重要參数：QPS（TPS）、并发数、响应时间

        QPS（TPS）：每秒钟request/数量： Quantity Per Second 

        并发数： 系统同一时候处理的request/事务数

        响应时间：  一般取平均响应时间

（非常多人常常会把并发数和TPS理解混淆）

理解了上面三个要素的意义之后，就能推算出它们之间的关系：
QPS（TPS）= 并发数/平均响应时间    或者   并发数 = QPS*平均响应时间


        一个典型的上班签到系统，早上8点上班。7点半到8点这30分钟的时间里用户会登录签到系统进行签到。公司员工为1000人，平均每一个员上登录签到系统的时长为5分钟。能够用以下的方法计算。
QPS = 1000/(30*60) 事务/秒
平均响应时间为 = 5*60  秒
并发数= QPS*平均响应时间 = 1000/(30*60) *(5*60)=166.7











