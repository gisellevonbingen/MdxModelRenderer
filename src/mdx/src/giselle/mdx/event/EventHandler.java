package giselle.mdx.event;

@FunctionalInterface
public interface EventHandler<T extends EventArgs>
{
	void call(Object sender, T args);
}
