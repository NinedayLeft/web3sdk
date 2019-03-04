package org.fisco.bcos.channel.test.contract;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;
import org.fisco.bcos.channel.test.TestBase;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.junit.Test;

public class OkTest extends TestBase {

  public static Class<?> contractClass;
  public String contractName;
  public RemoteCall<?> remoteCall;
  public String contractAddress;
  public java.math.BigInteger gasPrice = new BigInteger("300000000");
  public java.math.BigInteger gasLimit = new BigInteger("300000000");

  @Test
  public void testOkContract() throws Exception {

    Ok okDemo = Ok.deploy(web3j, credentials, gasPrice, gasLimit).send();

    if (okDemo != null) {
      System.out.println("####contract address is: " + okDemo.getContractAddress());
      TransactionReceipt receipt = okDemo.trans(new BigInteger("4")).send();
      assertTrue(receipt.getBlockNumber().intValue() > 0);
      assertTrue(receipt.getTransactionIndex().intValue() >= 0);
      assertTrue(receipt.getGasUsed().intValue() > 0);
      BigInteger oldBalance = okDemo.get().sendAsync().get(60000, TimeUnit.MILLISECONDS);
      okDemo.trans(new BigInteger("4")).sendAsync().get(60000, TimeUnit.MILLISECONDS);
      BigInteger newBalance = okDemo.get().sendAsync().get(60000, TimeUnit.MILLISECONDS);
      System.out.println("####newBalance is: " + oldBalance.intValue());
      assertTrue(newBalance.intValue() == oldBalance.intValue() + 4);
    }
  }

  public static Class[] getParameterType(Class clazz, String methodName)
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
          InvocationTargetException, InstantiationException {
    Method[] methods = clazz.getDeclaredMethods();
    Class[] type = null;
    for (Method method : methods) {
      if (methodName.equals(method.getName())) {
        Parameter[] params = method.getParameters();
        type = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
          System.out.println(params[i].getParameterizedType().getTypeName());
          type[i] = Class.forName(params[i].getParameterizedType().getTypeName());
          Class.forName(params[i].getParameterizedType().getTypeName())
              .getDeclaredConstructor(String.class)
              .newInstance("124");
        }
      }
    }

    return type;
  }
}
