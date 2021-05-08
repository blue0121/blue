package test.mqtt.core.client;

import blue.mqtt.internal.core.client.ConnectionSet;
import org.fusesource.mqtt.client.CallbackConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * @author Jin Zheng
 * @since 1.0 2021-05-08
 */
public class ConnectionSetTest {
	private ConnectionSet set;

	public ConnectionSetTest() {
	}

	@BeforeEach
	public void beforeEach() {
		this.set = new ConnectionSet();
	}

	@Test
	public void test1() {
		CallbackConnection conn1 = Mockito.mock(CallbackConnection.class);
		String id1 = "ClientId1";
		String topic1 = "Topic1";
		set.connect(id1, conn1);
		Assertions.assertEquals(conn1, set.getConnection());
		set.subscribe(topic1, id1);
		Assertions.assertEquals(conn1, set.getConnectionByTopic(topic1));
		set.unsubscribe(topic1);
		Assertions.assertNull(set.getConnectionByTopic(topic1));
		set.disconnect(id1);
		Assertions.assertNull(set.getConnection());
	}

}
