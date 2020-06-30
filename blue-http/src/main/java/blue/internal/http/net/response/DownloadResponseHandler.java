package blue.internal.http.net.response;

import blue.http.message.Download;
import blue.http.message.Response;
import blue.internal.http.net.HttpStaticHandler;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @author Jin Zheng
 * @since 1.0 2020-01-14
 */
public class DownloadResponseHandler implements ResponseHandler
{
	public DownloadResponseHandler()
	{
	}

	@Override
	public boolean accepted(Object target)
	{
		return target != null && target instanceof Download;
	}

	@Override
	public void handle(Channel ch, HttpRequest request, Response response)
	{
		Download download = (Download) response.getResult();
		HttpStaticHandler.download(ch, request, download);
	}
}
