package blue.redis.core.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

import java.io.IOException;

/**
 * Fastjson 的实现
 *
 * @author Jin Zheng
 * @since 1.0 2019-02-03
 */
public class FastjsonCodec extends BaseCodec {
	private static SerializerFeature[] serializer = new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat,
			SerializerFeature.WriteClassName};
	private static Feature[] feature = new Feature[]{};

	private Encoder encoder = new FastjsonEncoder();
	private Decoder<Object> decoder = new FastjsonDecoder();

	static {
		ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
	}

	public FastjsonCodec() {
	}

	@Override
	public Decoder<Object> getValueDecoder() {
		return decoder;
	}

	@Override
	public Encoder getValueEncoder() {
		return encoder;
	}

	class FastjsonEncoder implements Encoder {
		@Override
		public ByteBuf encode(Object in) throws IOException {
			ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
			try {
				ByteBufOutputStream os = new ByteBufOutputStream(out);
				os.write(JSON.toJSONBytes(in, serializer));
				return os.buffer();
			}
			catch (IOException e) {
				out.release();
				throw e;
			}
			catch (Exception e) {
				out.release();
				throw new IOException(e);
			}
		}
	}

	class FastjsonDecoder implements Decoder<Object> {
		@Override
		public Object decode(ByteBuf buf, State state) throws IOException {
			ByteBufInputStream is = new ByteBufInputStream(buf);
			return JSON.parseObject(is, Object.class, feature);
		}
	}

}
