package doug;

import com.google.code.yanf4j.buffer.IoBuffer;
import com.google.code.yanf4j.config.Configuration;
import com.google.code.yanf4j.core.Session;
import com.google.code.yanf4j.core.impl.HandlerAdapter;
import com.google.code.yanf4j.core.impl.StandardSocketOption;
import com.google.code.yanf4j.core.impl.TextLineCodecFactory;
import com.google.code.yanf4j.nio.TCPController;

public class Yanf4j {
	public static void main(String[] args) throws Exception {
		final int PORT = 1234;
		final int THREAD_POOL_SIZE = 10;
		final int INITIAL_READ_BUFFER_SIZE = 1024;

		boolean threadPoolDisabled = args.length > 0 && args[0].equals("nothreadpool");
		Configuration configuration = new Configuration();
		configuration.setCheckSessionTimeoutInterval(0);
		configuration.setSessionIdleTimeout(0);
		configuration.setSessionReadBufferSize(INITIAL_READ_BUFFER_SIZE);
		TCPController controller = new TCPController(configuration, new TextLineCodecFactory());
		controller.setSocketOption(StandardSocketOption.SO_REUSEADDR, true);
		controller.setSocketOption(StandardSocketOption.TCP_NODELAY, true);
		controller.setHandler(new EchoHandler());
		if (!threadPoolDisabled) {
			controller.setReadThreadCount(THREAD_POOL_SIZE);
		}
		controller.bind(PORT);
		System.out.println("Yanf4j EchoServer is ready to serve at port " + PORT + ".");
		System.out.println("Enter 'ant benchmark' on the client side to begin.");
		System.out.println("Thread pool: " + (threadPoolDisabled ? "DISABLED" : "ENABLED"));
	}

	static class EchoHandler extends HandlerAdapter {
		@Override
		public void onMessageReceived(final Session session, final Object msg) {
			System.out.println("msg:" + msg);
			System.out.println(Thread.currentThread().getName());
			// session.write(((IoBuffer) msg).duplicate());
			session.write("haha");
		}

		@Override
		public void onExceptionCaught(Session session, Throwable t) {
			t.printStackTrace();
			session.close();
		}
	}
}
