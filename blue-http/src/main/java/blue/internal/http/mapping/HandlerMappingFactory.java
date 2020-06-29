package blue.internal.http.mapping;

import blue.internal.http.handler.HandlerChain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-07
 */
public class HandlerMappingFactory
{
	private static volatile HandlerMappingFactory instance;

	private List<HandlerMapping<?>> mappingList = new ArrayList<>();

	private HandlerMappingFactory()
	{
		mappingList.add(new HttpHandlerMapping());
		mappingList.add(new WebSocketHandlerMapping());
	}

	public static HandlerMappingFactory getInstance()
	{
		if (instance == null)
		{
			synchronized (HandlerMappingFactory.class)
			{
				if (instance == null)
				{
					instance = new HandlerMappingFactory();
				}
			}
		}
		return instance;
	}

	public HandlerChain getHandlerChain(Object request)
	{
		if (request == null)
			return null;

		for (HandlerMapping<?> mapping : mappingList)
		{
			if (mapping.accepted(request))
			{
				HandlerChain chain = ((HandlerMapping<Object>)mapping).getHandlerChain(request);
				return chain;
			}
		}
		return null;
	}

}
