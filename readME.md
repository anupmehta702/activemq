intention is to test JMSXGroupId

THe code has a queue named mailbox
it has three consumers (threads listening to same queue)

However we write msg to mailbox queue along with setting JMSXGroupID

You would notice
receiver0 would receive only msg from one JMSXGroupID in correct order .Same with other receivers(1 and 2)

Run activeMQ first on localhost:8161

 output is as follows -
Received by receiver1 <Msg from gmail delivered at 32>
Received by receiver0  <Msg from yahoo delivered at 34>
Received by receiver0  <Msg from yahoo delivered at 36>
Received by receiver1 <Msg from gmail delivered at 38>
Received by receiver2 <Msg from microsoft delivered at 40>
Received by receiver2 <Msg from microsoft delivered at 42>
Received by receiver0  <Msg from yahoo delivered at 44>


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