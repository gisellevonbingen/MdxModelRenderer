package giselle.mdx.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EventManager<T extends EventArgs>
{
	private final EventHandlerList<T> handlers;

	public EventManager()
	{
		this.handlers = new EventHandlerList<>();
	}

	public void post(Object sender, T e)
	{
		this.handlers.post(sender, e);
	}

	public EventHandlerList<T> getHandlers()
	{
		return this.handlers;
	}

	public static class EventHandlerList<T extends EventArgs> implements Iterable<EventHandler<T>>
	{
		private final List<EventHandler<T>> list;

		public EventHandlerList()
		{
			this.list = new ArrayList<>();
		}

		private void post(Object sender, T e)
		{
			for (EventHandler<T> handler : this)
			{
				handler.call(sender, e);
			}

		}

		public void add(EventHandler<T> handler)
		{
			synchronized (this.list)
			{
				this.list.add(handler);
			}

		}

		public boolean remove(EventHandler<T> handler)
		{
			synchronized (this.list)
			{
				return this.list.remove(handler);
			}

		}

		public boolean contains(EventHandler<T> handler)
		{
			synchronized (this.list)
			{
				return this.list.contains(handler);
			}

		}

		@Override
		public Iterator<EventHandler<T>> iterator()
		{
			synchronized (this.list)
			{
				return new ArrayList<>(this.list).iterator();
			}

		}

	}

}
