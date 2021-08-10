package iris.client_bff.core;

import java.lang.reflect.Type;
import java.util.Map;

import javax.annotation.Priority;

import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.IJsonRpcClient;

/**
 * @author Jens Kutzsche
 */
@Service
@Priority(value = 10)
public class DummyJsonRpcClient implements IJsonRpcClient {

	@Override
	public void invoke(String methodName, Object argument) throws Throwable {
		// TODO Auto-generated method stub

	}

	@Override
	public Object invoke(String methodName, Object argument, Type returnType) throws Throwable {

		if (returnType.getTypeName().equals("String")) {
			return "OK";
		}
		return null;
	}

	@Override
	public Object invoke(String methodName, Object argument, Type returnType, Map<String, String> extraHeaders)
			throws Throwable {

		if (returnType.getTypeName().equals("String")) {
			return "OK";
		}
		return null;
	}

	@Override
	public <T> T invoke(String methodName, Object argument, Class<T> clazz) throws Throwable {

		if (clazz == String.class) {
			return (T) "OK";
		}
		return null;
	}

	@Override
	public <T> T invoke(String methodName, Object argument, Class<T> clazz, Map<String, String> extraHeaders)
			throws Throwable {

		if (clazz == String.class) {
			return (T) "OK";
		}
		return null;
	}
}
