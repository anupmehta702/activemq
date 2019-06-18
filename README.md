Intention is to test JMSXGroupId, composite queue ,concurrency level.

Refer - https://nofluffjuststuff.com/blog/bruce_snyder/2011/08/tuning_jms_message_consumption_in_spring 
http://activemq.apache.org/what-is-the-prefetch-limit-for.html
for informaiton related to factory settings and options

THe code has a queue named mailbox
it has three consumers (threads listening to same queue)
However we write msg to mailbox queue along with setting JMSXGroupID
You would notice
receiver0 would receive only msg from one JMSXGroupID in correct order .Same with other receivers(1 and 2)

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

Received by CompositeQueueReceiver foo queue  <Msg from MY.QUEUE delivered at 5>

as you would notice
receiver0 receives msgs from only yahoo
receiver1 from gmail
receiver2 from microsoft


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


