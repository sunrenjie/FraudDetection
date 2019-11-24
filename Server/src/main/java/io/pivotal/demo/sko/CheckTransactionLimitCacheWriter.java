package io.pivotal.demo.sko;

import java.util.Properties;
import java.util.Random;

import org.apache.geode.cache.CacheWriterException;
import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.util.CacheWriterAdapter;
import org.apache.geode.pdx.PdxInstance;

public class CheckTransactionLimitCacheWriter extends CacheWriterAdapter implements Declarable {

	static Random random = new Random();

	@Override
	public void beforeCreate(EntryEvent event) throws CacheWriterException {

		PdxInstance transaction = (PdxInstance)event.getNewValue();

		// randomly mark 0.2% of the transactions as suspicious
		if (random.nextDouble()<0.002){
			long transactionId;
			long deviceId;

			transactionId = ((Number)transaction.getField("id")).longValue();
			deviceId = ((Number)transaction.getField("deviceId")).longValue();

			Suspect suspect = new Suspect(transactionId, deviceId, System.currentTimeMillis(), "LIMIT");
			event.getRegion().getRegionService().getRegion(RegionName.Suspect.name()).put(transactionId, suspect);

		}

	}

	@Override
	public void init(Properties arg0) {
		// TODO Auto-generated method stub

	}




}
