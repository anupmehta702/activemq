Intention is to test 
JMSXGroupId,
JMSType, 
composite queue ,
concurrency level.

Refer - https://nofluffjuststuff.com/blog/bruce_snyder/2011/08/tuning_jms_message_consumption_in_spring 
http://activemq.apache.org/what-is-the-prefetch-limit-for.html
for informaiton related to factory settings and options

JMSXGroupId Test -
THe code has a queue named mailbox
it has three consumers [Receiver 1,2,and 3](threads listening to same queue)
However we write msg to mailbox queue along with setting JMSXGroupID
You would notice
receiver0 would receive only msg from one JMSXGroupID in correct order .
Same with other receivers(1 and 2)

Run activeMQ first on localhost:8161
output is as follows -
 Received by receiver2 <Msg from gmail sent at Tue Jun 18 14:16:05 IST 2019> at - Tue Jun 18 14:16:05 IST 2019
 Received by receiver1 <Msg from yahoo sent at Tue Jun 18 14:16:05 IST 2019> at - Tue Jun 18 14:16:05 IST 2019
 Received by receiver1 <Msg from yahoo sent at Tue Jun 18 14:16:05 IST 2019> at - Tue Jun 18 14:16:05 IST 2019
 Received by receiver2 <Msg from gmail sent at Tue Jun 18 14:16:05 IST 2019> at - Tue Jun 18 14:16:05 IST 2019
 Received by receiver0 <Msg from microsoft sent at Tue Jun 18 14:16:05 IST 2019> at - Tue Jun 18 14:16:05 IST 2019
 Received by receiver0 <Msg from microsoft sent at Tue Jun 18 14:16:05 IST 2019> at - Tue Jun 18 14:16:05 IST 2019
 Received by receiver1 <Msg from yahoo sent at Tue Jun 18 14:16:05 IST 2019> at - Tue Jun 18 14:16:05 IST 2019
 Received by receiver2 <Msg from facebook sent at Tue Jun 18 14:16:05 IST 2019> at - Tue Jun 18 14:16:05 IST 2019
 Received by receiver2 <Msg from facebook sent at Tue Jun 18 14:16:05 IST 2019> at - Tue Jun 18 14:16:05 IST 2019
 
as you would notice
receiver0 receives msgs from only yahoo
receiver1 from gmail
receiver2 from microsoft

Composite Queue Test
For composite queue edit activemq.xml in bin folder ,add below code
			<destinationInterceptors>
				 <virtualDestinationInterceptor>
				   <virtualDestinations>
					 <compositeQueue name="MY.QUEUE">
					   <forwardTo>
						 <queue physicalName="FOO" />
						 <topic physicalName="BAR" />
					   </forwardTo>
					 </compositeQueue>
				   </virtualDestinations>
				 </virtualDestinationInterceptor>
			</destinationInterceptors>

Output seen
Received by CompositeQueueReceiver foo queue  <Msg from MY.QUEUE delivered at 50>

Consumer side selector test
We have Receiver 3 with selector YahooMessage 
Receiver 4 with selector NonYahooMessage
Receiver5 with no selector

I have also enabled JMXGroupID to show how things behave between JMSType and JMXGroupID

1) JMSType + JMXGroupID 
receiver 4 - gmail msgs Consumer based selector
receiver 3 - yahoo msgs Consumer based selector
receiver 5 - facebook,outlook no selector

Received by receiver4 <Msg from gmail sent at Sun Oct 20 13:20:27 IST 2019> at - Sun Oct 20 13:20:29 IST 2019 with selector - GmailMessage
Received by receiver3 <Msg from yahoo sent at Sun Oct 20 13:20:29 IST 2019> at - Sun Oct 20 13:20:31 IST 2019 with selector - YahooMessage
Received by receiver3 <Msg from yahoo sent at Sun Oct 20 13:20:31 IST 2019> at - Sun Oct 20 13:20:33 IST 2019 with selector - YahooMessage
Received by receiver4 <Msg from gmail sent at Sun Oct 20 13:20:33 IST 2019> at - Sun Oct 20 13:20:35 IST 2019 with selector - GmailMessage
Received by receiver3 <Msg from yahoo sent at Sun Oct 20 13:20:35 IST 2019> at - Sun Oct 20 13:20:35 IST 2019 with selector - YahooMessage
Received by receiver5 <Msg from facebook sent at Sun Oct 20 13:20:35 IST 2019> at - Sun Oct 20 13:20:37 IST 2019 with no selector
Received by receiver5 <Msg from facebook sent at Sun Oct 20 13:20:37 IST 2019> at - Sun Oct 20 13:20:37 IST 2019 with no selector
Received by receiver5 <Msg from outlook sent at Sun Oct 20 13:20:37 IST 2019> at - Sun Oct 20 13:20:39 IST 2019 with no selector
Received by receiver5 <Msg from outlook sent at Sun Oct 20 13:20:40 IST 2019> at - Sun Oct 20 13:20:40 IST 2019 with no selector

2) Only JMSType (but JMSType on two receivers and one receiver has no JMSType)
This is dangerous bcoz non selector consumer can receive any msgs and selector consumer cannot gurantee that all selector msgs would reach him
Receiver 5 is not consumer based selector yet it received Yahoo msg meant for receiver 3
 
Received by receiver4 <Msg from gmail sent at Sun Oct 20 13:14:57 IST 2019> at - Sun Oct 20 13:14:59 IST 2019 with selector - GmailMessage
Received by receiver3 <Msg from yahoo sent at Sun Oct 20 13:14:59 IST 2019> at - Sun Oct 20 13:15:01 IST 2019 with selector - YahooMessage
<b>Received by receiver5 <Msg from yahoo sent at Sun Oct 20 13:15:01 IST 2019> at - Sun Oct 20 13:15:01 IST 2019 with no selector</b>
Received by receiver4 <Msg from gmail sent at Sun Oct 20 13:15:01 IST 2019> at - Sun Oct 20 13:15:03 IST 2019 with selector - GmailMessage
Received by receiver3 <Msg from yahoo sent at Sun Oct 20 13:15:03 IST 2019> at - Sun Oct 20 13:15:05 IST 2019 with selector - YahooMessage
Received by receiver5 <Msg from facebook sent at Sun Oct 20 13:15:05 IST 2019> at - Sun Oct 20 13:15:07 IST 2019 with no selector
Received by receiver5 <Msg from facebook sent at Sun Oct 20 13:15:07 IST 2019> at - Sun Oct 20 13:15:07 IST 2019 with no selector
Received by receiver5 <Msg from outlook sent at Sun Oct 20 13:15:07 IST 2019> at - Sun Oct 20 13:15:07 IST 2019 with no selector
Received by receiver5 <Msg from outlook sent at Sun Oct 20 13:15:07 IST 2019> at - Sun Oct 20 13:15:09 IST 2019 with no selector







I also tried master slave configuration -
In short ,I started two brokers (61616,61626) both connected to common kahadb. Out of them only one would be active
and the other would be slave since the slave would not have lock on the common kahadb.

To have two activemq brokers follow below steps
 (https://www.javacodegeeks.com/2014/04/using-activemq-masterslave-configuration-with-failover-protocol.html)-

1) create two brokers using activemq-admin create command .
D:\apache-activemq-5.15.19\bin>activemq-admin create ..\cluster\broker-1
Note - dont have folders with spaces ,it creates problems

2) change the activemq.xml of both the clusters to -
 a) change the tcp port
 b) enable useJmx=true and it's connectorPort (1099,2099)

3) Ensure both point to same kahadb by updating the below property -
    <persistenceAdapter>
          <kahaDB directory="D:/apache-activemq-5.15.9/kahadb"/>
    </persistenceAdapter>

3) Go to jetty.xml and change the port of one broker to 8171

4) Simply start both brokers( one would be master other would be slave)

5) In the javacodegeeks link,they try to deploy a common war on external tomcat by pointing to both the
brokers using -
set JAVA_OPTS=-Dwebconsole.type=properties -Dwebconsole.jms.url=failover:(tcp://localhost:61616,tcp://localhost:61626) -Dwebconsole.jmx.url=service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi,service:jmx:rmi:///jndi/rmi://localhost:2099/jmxrmi
however it is not required ! .
You can simply start both the brokers pointing to different webconsole (master would work fine ,but slave would be temporarily down

You can also refer to http://chamilad.github.io/blog/2015/11/17/creating-a-simple-activemq-master-slash-slave-setup/
for setup




