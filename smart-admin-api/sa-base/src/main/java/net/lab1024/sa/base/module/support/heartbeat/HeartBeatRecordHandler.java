package net.lab1024.sa.base.module.support.heartbeat;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.module.support.heartbeat.core.HeartBeatRecord;
import net.lab1024.sa.base.module.support.heartbeat.core.IHeartBeatRecordHandler;
import net.lab1024.sa.base.module.support.heartbeat.domain.HeartBeatRecordEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 心跳记录
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-01-09 20:57:24
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright  <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class HeartBeatRecordHandler implements IHeartBeatRecordHandler {

    @Resource
    private HeartBeatRecordDao heartBeatRecordDao;

    /**
     * 心跳日志处理方法
     * @param heartBeatRecord 心跳日志记录
     *                        查询是否存在同一服务器和进程号的记录
     *                        如果没有，则新增一条，可观测所有服务的运行及重启全貌 非常 nice
     *                        如果存在，则更新当前服务运行记录，通过当前自增 id 可以看出运行了多少分钟
     */
    @Override
    public void handler(HeartBeatRecord heartBeatRecord) {
        HeartBeatRecordEntity heartBeatRecordEntity = SmartBeanUtil.copy(heartBeatRecord, HeartBeatRecordEntity.class);
        HeartBeatRecordEntity heartBeatRecordOld = heartBeatRecordDao.query(heartBeatRecordEntity);
        if (heartBeatRecordOld == null) {
            heartBeatRecordDao.insert(heartBeatRecordEntity);
        } else {
            heartBeatRecordDao.updateHeartBeatTimeById(heartBeatRecordOld.getHeartBeatRecordId(), heartBeatRecordEntity.getHeartBeatTime());
        }
    }

}
