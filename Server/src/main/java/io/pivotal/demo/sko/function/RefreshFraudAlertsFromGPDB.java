package io.pivotal.demo.sko.function;

import io.pivotal.demo.sko.RegionName;
import io.pivotal.gemfire.gpdb.service.GpdbService;
import io.pivotal.gemfire.gpdb.service.ImportConfiguration;
import io.pivotal.gemfire.gpdb.service.ImportResult;
import io.pivotal.gemfire.gpdb.util.RegionFunctionAdapter;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.execute.RegionFunctionContext;

public class RefreshFraudAlertsFromGPDB extends RegionFunctionAdapter implements Declarable {

    private static final Logger log = LogManager.getLogger();
    public static final String ID = "RefreshFraudAlertsFromGPDB";


    @Override
    public boolean isHA() {
        return false;
    }

    @Override
    public String getId() {
        return ID;
    }


    @Override
    public void init(Properties props) {
    }


    @Override
    public void execute(RegionFunctionContext arg0) {

        // load data for fraudulent transactions
        try {
            Cache cache = CacheFactory.getAnyInstance();
            String name = RegionName.Suspect.name();
            ImportResult r = GpdbService.importRegion(ImportConfiguration.builder(cache.getRegion(name)).build());
            long count = r.getImportedCount();
            log.info("Loaded " + count + " rows from GPDB on region " + name);
            arg0.getResultSender().lastResult("Loaded " + count + " entities");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }
}
