Intention is to test JMSXGroupId, composite queue ,concurrency level.

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
Received by receiver0 msg - <Msg from microsoft sent at Tue Jun 18 14:16:05 IST 2019> at - Tue Jun 18 14:16:05 IST 2019
Received by receiver0 msg - <Msg from microsoft sent at Tue Jun 18 14:16:05 IST 2019> at - Tue Jun 18 14:16:05 IST 2019
Received by receiver1 <Msg from yahoo sent at Tue Jun 18 14:16:05 IST 2019> at - Tue Jun 18 14:16:05 IST 2019
Received by receiver2 <Msg from facebook sent at Tue Jun 18 14:16:05 IST 2019> at - Tue Jun 18 14:16:05 IST 2019
Received by receiver2 <Msg from facebook sent at Tue Jun 18 14:16:05 IST 2019> at - Tue Jun 18 14:16:05 IST 2019
Received by CompositeQueueReceiver foo queue  <Msg from MY.QUEUE delivered at 5>


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


receiver0 receives msgs from only yahoo
receiver1 from gmail
receiver2 from microsoft
