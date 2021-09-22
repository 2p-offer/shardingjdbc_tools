package com.biligame.access.algorithm;

import com.biligame.access.utils.ShardingSphereUtil;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import java.util.Collection;

/**
 * @author chaiyueying
 * @Date: 2019/10/15 14:49
 * @Description: 分表规则
 */
public class TableShardingAlgorithm implements PreciseShardingAlgorithm<Integer> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Integer> shardingValue) {
        String result = shardingValue.getLogicTableName() + makeEndShardKey(shardingValue.getValue(), ShardingSphereUtil.getInstance().getUserShardConfig().getShardTableNum());
        return result;
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
        } else {
            return "_" + lastTableNum;
        }
    }
}