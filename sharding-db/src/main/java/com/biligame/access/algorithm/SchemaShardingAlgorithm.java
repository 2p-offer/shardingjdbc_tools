package com.biligame.access.algorithm;

import com.biligame.access.config.UserShardConfig;
import com.biligame.access.utils.ShardingSphereUtil;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import java.util.Collection;

/**
 * @author chaiyueying
 * @Date: 2019/10/15 14:50
 * @Description: 分库规则
 */
public class SchemaShardingAlgorithm implements PreciseShardingAlgorithm<Integer> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Integer> shardingValue) {
        UserShardConfig userShardConfig = ShardingSphereUtil.getInstance().getUserShardConfig();
        String dbName = userShardConfig.getLogicSchema() + makeEndShardKey(shardingValue.getValue(), userShardConfig.getShardSchemaNum());
        for (String each : availableTargetNames) {
            if (each.equals(dbName)) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * 通过数字生成后缀
     *
     * @param index
     * @return
     */
    private String makeEndShardKey(long index, int mode) {
        long lastTableNum = index % mode;
        if (lastTableNum < 10) {
            return "_0" + lastTableNum;
        }
        return "_" + lastTableNum;
    }
}
