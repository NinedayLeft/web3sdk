package org.fisco.bcos.channel.test.parallel.parallelok;

import java.math.BigInteger;
import org.fisco.bcos.web3j.crypto.EncryptType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceDT {
    private static Logger logger = LoggerFactory.getLogger(PerformanceDT.class);

    public static void Usage() {
        System.out.println(" Usage:");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* org.fisco.bcos.channel.test.parallel.parallelok.PerformanceDT groupID add count tps file.");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* org.fisco.bcos.channel.test.parallel.parallelok.PerformanceDT groupID transfer count tps file strategy queryAccountQPS.");
        System.out.println(
                " \t java -cp conf/:lib/*:apps/* org.fisco.bcos.channel.test.parallel.parallelok.PerformanceDT groupID transferRevert count tps file strategy queryAccountQPS.");
        System.exit(0);
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            Usage();
        }

        String groupID = args[0];
        String command = args[1];
        BigInteger count = new BigInteger(args[2]);
        BigInteger tps = new BigInteger(args[3]);
        String file = null;
        BigInteger deci = new BigInteger("0");
        BigInteger queryAccountQPS = tps;
        if (args.length > 4) {
            file = args[4];
            if (args.length > 5) {
                deci = new BigInteger(args[5]);
            }
            if (args.length > 6) {
                queryAccountQPS = new BigInteger(args[6]);
            }
        }

        // deci can not bigger than 10.
        if (deci.compareTo(new BigInteger("10")) > 0) {
            deci = new BigInteger("10");
        }

        logger.info(
                " dag transfer test begin, command is {}, count is {}, tps is {}, file is {}, deci is {}, queryAccountQPS is {}",
                command,
                count,
                tps,
                file,
                deci,
                queryAccountQPS);

        DagUserMgr d = new DagUserMgr();
        d.setFile(file);

        PerformanceDTCollector collector = new PerformanceDTCollector();
        collector.setTotal(count.intValue());
        collector.setDagUserMrg(d);

        PerformanceDTTest PerformanceDTTest = new PerformanceDTTest(groupID);
        PerformanceDTTest.setCollector(collector);
        PerformanceDTTest.setDagUserMgr(d);
        collector.setPerformanceDTTest(PerformanceDTTest);

        System.out.println(
                (EncryptType.encryptType == EncryptType.ECDSA_TYPE)
                        ? " ===>> normal parallel "
                        : " ===>> gm parallel ");

        switch (command) {
            case "add":
                d.setTestType("add");
                PerformanceDTTest.userAddTest(count, tps);
                break;
            case "transfer":
                d.setTestType("transfer");
                d.loadDagTransferUser();
                PerformanceDTTest.userTransferTest(count, tps, deci, queryAccountQPS);
                break;
            case "transferRevert":
                d.setTestType("transferRevert");
                d.loadDagTransferUser();
                PerformanceDTTest.userTransferRevertTest(count, tps, deci, queryAccountQPS);
            default:
                Usage();
        }
    }
}
